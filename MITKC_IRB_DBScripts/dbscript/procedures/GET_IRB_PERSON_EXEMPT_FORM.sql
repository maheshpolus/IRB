create or replace PROCEDURE GET_IRB_PERSON_EXEMPT_FORM(
AV_IRB_PERSON_EXEMPT_FORM_ID IN  MITKC_IRB_PERSON_EXEMPT_FORM.IRB_PERSON_EXEMPT_FORM_ID%TYPE,
CUR_GENERIC OUT SYS_REFCURSOR)
IS
BEGIN
	OPEN CUR_GENERIC FOR
	SELECT 
		t1.IRB_PERSON_EXEMPT_FORM_ID,
		t1.PERSON_ID,
		t1.PERSON_NAME,
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
	FROM MITKC_IRB_PERSON_EXEMPT_FORM t1
	LEFT OUTER JOIN MITKC_IRB_EXEMPT_FORM_STATUS t2 on t1.EXEMPT_STATUS_CODE = t2.EXEMPT_STATUS_CODE
	LEFT OUTER JOIN KC_PERSON_MV t3 on (t3.PRNCPL_NM) = (t1.update_user)
	LEFT OUTER JOIN KC_PERSON_MV t4 on t4.PRNCPL_ID = t1.FACULTY_SPONSOR_PERSON_ID
	LEFT OUTER JOIN UNIT t5 on t5.UNIT_NUMBER = t1.UNIT_NUMBER
	WHERE t1.IRB_PERSON_EXEMPT_FORM_ID = AV_IRB_PERSON_EXEMPT_FORM_ID;
END;
/