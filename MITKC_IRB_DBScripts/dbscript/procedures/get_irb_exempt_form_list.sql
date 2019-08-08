create or replace PROCEDURE GET_IRB_EXEMPT_FORM_LIST(
AV_PERSON_ID 			IN  KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE 	IN VARCHAR2,
AV_TITLE				IN VARCHAR2,
AV_PI_NAME				IN VARCHAR2,
AV_DETERMINATION		IN VARCHAR2,
AV_FACULTY_SPONSOR_NM	IN VARCHAR2,
AV_START_DATE			IN DATE,
AV_END_DATE				IN DATE,
CUR_GENERIC OUT SYS_REFCURSOR)
IS
   ls_filter_condition VARCHAR2(4000) := null;
   ls_dyn_sql 		  VARCHAR2(4000);
ls_user_id IRB_PERSON_EXEMPT_FORM.UPDATE_USER%TYPE;
BEGIN

	BEGIN
		select USER_NAME
		into ls_user_id
		FROM WHOSP_PERSON_HISTORY
		WHERE PERSON_ID = AV_PERSON_ID;
	
	EXCEPTION
	WHEN OTHERS THEN
		ls_user_id := NULL;
	END;
	
	 	 IF AV_PERSON_ROLE_TYPE IN ('IRB_ADMIN')  THEN
	 
		 ls_filter_condition := ' WHERE ( T1.EXEMPT_STATUS_CODE  IN (4,3)  OR  ( T1.EXEMPT_STATUS_CODE = 1 AND T1.CREATE_USER = '''||ls_user_id||''' ) )';
	
	 ELSE -- PI or Student
	 	 		
		 IF AV_PERSON_ID is not null THEN 
			ls_filter_condition := ' WHERE (T1.PERSON_ID  = '''||AV_PERSON_ID||''' OR T1.CREATE_USER = '''||ls_user_id||''' OR T1.FACULTY_SPONSOR_PERSON_ID = '''||AV_PERSON_ID||''' )';
		 END IF;
		
	 END IF;
	 
	 
	 IF AV_TITLE is not null THEN 
		ls_filter_condition := ls_filter_condition||' AND LOWER(T1.EXEMPT_TITLE) LIKE '''||replace(lower(AV_TITLE),'*','%')||'%'' ';
	 END IF;

	 IF AV_DETERMINATION is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' AND T1.IS_EXEMPT_GRANTED = '''||AV_DETERMINATION||''' ';
	 END IF;
	 
	 IF AV_FACULTY_SPONSOR_NM is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' AND LOWER(t4.FULL_NAME)  LIKE '''||replace(lower(AV_FACULTY_SPONSOR_NM),'*','%')||'%'' ';
	 END IF;
	 
   IF AV_PI_NAME is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' AND LOWER(t1.PERSON_NAME)  LIKE '''||replace(lower(AV_PI_NAME),'*','%')||'%'' ';
	 END IF;
  
	 IF AV_START_DATE is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' AND T1.START_DATE = '''||AV_START_DATE||''' ';
	 END IF;
	 
	  IF AV_END_DATE is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' AND T1.END_DATE = '''||AV_END_DATE||''' ';
	 END IF; 
	 
	-- ls_filter_condition :=  ls_filter_condition||' '||' AND ROWNUM < 100';
	
     ls_dyn_sql:= q'[
     SELECT *
     FROM (
						SELECT 
							t1.IRB_PERSON_EXEMPT_FORM_ID,
							t1.PERSON_ID,
							t1.PERSON_NAME,
							FN_MITKC_EXEMPTFORM_IS_SUMTD(t1.IRB_PERSON_EXEMPT_FORM_ID) as IS_SUBMITTED_ONCE,
							t1.EXEMPT_STATUS_CODE,
							t2.DESCRIPTION as EXEMPT_STATUS,
							t1.IS_EXEMPT_GRANTED,
							t1.EXEMPT_FORM_NUMBER,		
							t1.EXEMPT_TITLE,
							t1.QUESTIONNAIRE_ANS_HEADER_ID,
							t1.FACULTY_SPONSOR_PERSON_ID,
							t4.FULL_NAME as FACULTY_SPONSOR_PERSON,
							t1.UNIT_NUMBER,
							t5.UNIT_NAME,
							t1.SUMMARY,
							t1.START_DATE,		
							t1.END_DATE,
							to_char(t1.UPDATE_TIMESTAMP,'YYYY-MM-DD') as UPDATE_TIMESTAMP,
							nvl(t3.full_name,t1.UPDATE_USER) as UPDATE_USER		
						FROM IRB_PERSON_EXEMPT_FORM t1
						LEFT OUTER JOIN IRB_EXEMPT_FORM_STATUS t2 on t1.EXEMPT_STATUS_CODE = t2.EXEMPT_STATUS_CODE
						LEFT OUTER JOIN WHOSP_PERSON_HISTORY t3 on (t3.USER_NAME) = (t1.update_user)
						LEFT OUTER JOIN WHOSP_PERSON_HISTORY t4 on t4.PERSON_ID = t1.FACULTY_SPONSOR_PERSON_ID	
						LEFT OUTER JOIN UNIT t5 on t5.UNIT_NUMBER = t1.UNIT_NUMBER	
						]'
						||ls_filter_condition
						||' order by T1.UPDATE_TIMESTAMP desc
            ) WHERE ROWNUM < 100';
		
   			open cur_generic for ls_dyn_sql;
	
END;
/