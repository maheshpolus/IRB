create or replace PROCEDURE GET_IRB_DASHBOARD(
AV_PERSON_ID        	IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE 	IN VARCHAR2,
AV_DASHBOARD_TYPE   	IN VARCHAR2,
AV_PROTOCOL_NUMBER		IN VARCHAR2,
AV_TITLE				IN VARCHAR2,
AV_PI_NAME				IN VARCHAR2,
AV_PROTOCOL_TYPE_CODE	IN VARCHAR2,
CUR_GENERIC         OUT SYS_REFCURSOR)
IS
  ls_filter_condition VARCHAR2(4000) := null;
  ls_dyn_sql 		  VARCHAR2(4000);
  ls_join_sql  VARCHAR2(1000) := ' ';
  ls_user_id VARCHAR2(60);
   
-- AV_DASHBOARD_TYPE values
	-- ALL		  : All protocol
	-- PENDING	  : Protocols that are returned to investigator from IRB office (either by IRB admin or committee decision - SMRR/SRR)
	-- DRAFT	  : Protocols that are still not submitted
	-- INPROGRESS : Protocols that are submitted
	-- ACTIVE	  : Protocols that are approved (status - Active-Open)
-- 
BEGIN
	
	BEGIN	
		select lower(prncpl_nm) into ls_user_id from krim_prncpl_t where prncpl_id = AV_PERSON_ID;
		
	EXCEPTION
	WHEN OTHERS THEN
		ls_user_id := '';
	END;	
 	 
	 IF AV_PROTOCOL_NUMBER is not null THEN 
		ls_filter_condition := ' and t1.protocol_number like '''||replace(AV_PROTOCOL_NUMBER,'*','%')||'%'' ';
	 END IF;
	 
	 IF AV_TITLE is not null THEN 
		ls_filter_condition := ls_filter_condition||' and lower(t1.title) like '''||replace(lower(AV_TITLE),'*','%')||'%'' ';
	 END IF;

	 IF AV_PROTOCOL_TYPE_CODE is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' and t1.protocol_type_code = '||AV_PROTOCOL_TYPE_CODE||' ';
	 END IF;
	 
	 IF AV_PI_NAME is not null THEN 	 
		ls_filter_condition :=  ls_filter_condition||' and lower(t7.PERSON_NAME)  like '''||replace(lower(AV_PI_NAME),'*','%')||'%'' ';
	 END IF;
	 
	 IF AV_PERSON_ROLE_TYPE = 'PI' OR AV_PERSON_ROLE_TYPE = 'DEPT_ADMIN' THEN
				
		IF AV_DASHBOARD_TYPE = 'PENDING' THEN
			ls_filter_condition :=  ls_filter_condition||' and t1.protocol_status_code in (102,104,107) ';
		ELSIF AV_DASHBOARD_TYPE = 'DRAFT' THEN
			ls_filter_condition :=  ls_filter_condition||' and t1.protocol_status_code in (100,105,106) ';
		ELSIF AV_DASHBOARD_TYPE = 'INPROGRESS' THEN
			ls_filter_condition :=  ls_filter_condition||' and t1.protocol_status_code = 101 ';
		ELSIF AV_DASHBOARD_TYPE = 'ACTIVE' THEN
			ls_filter_condition :=  ls_filter_condition||' and t1.protocol_status_code in (200,201,202,203) ';
		END IF;		
    
		if AV_PERSON_ROLE_TYPE = 'PI' then
		
			ls_join_sql := ' INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id ';
			ls_filter_condition :=  'AND ( t4.person_id = '''||AV_PERSON_ID||''' OR  lower(t1.CREATE_USER) =  '''|| ls_user_id ||''' )'||ls_filter_condition;	
			
		else -- DEPT_ADMIN 
			ls_filter_condition :=  'AND ( t1.LEAD_UNIT_NUMBER IN ( SELECT UNIT_NUMBER FROM MITKC_USER_RIGHT_MV WHERE PERSON_ID = '''||AV_PERSON_ID||'''
			AND PERM_NM = ''IRB_Dept_Administrator'' )  OR  lower(t1.CREATE_USER) =  '''||  ls_user_id ||''' )' ||ls_filter_condition;	
			
		end if;	
					

	 ELSE -- Admin
	
	 	IF AV_DASHBOARD_TYPE = 'NEW_SUBMISSION' THEN
			ls_filter_condition :=  ls_filter_condition||'  and t1.protocol_status_code = 101 and t1.sequence_number = 1 ';
		ELSIF AV_DASHBOARD_TYPE = 'PI_ACTION' THEN
			ls_filter_condition :=  ls_filter_condition||' '; -- need to implement 
		ELSIF AV_DASHBOARD_TYPE = 'ADMINISTRATOR' THEN
			ls_filter_condition :=  ls_filter_condition||' '; -- need to implement 
		ELSIF AV_DASHBOARD_TYPE = 'REVIEWER' THEN
			ls_filter_condition :=  ls_filter_condition||' '; -- need to implement 
		END IF;	
	 	
	 END IF;
	 	 
	 ls_filter_condition :=  ls_filter_condition||' ';
	
     ls_dyn_sql:= q'[
                    SELECT *
                    FROM
                    (
                      SELECT 
                      t1.protocol_number,
                      t1.protocol_id,
                      t1.title,
                      nvl(to_char(t1.last_approval_date,'yyyy-mm-dd'),' ') as last_approval_date,
					  nvl(to_char(t1.approval_date,'yyyy-mm-dd'),' ') as approval_date,
                      nvl(to_char(t1.expiration_date,'yyyy-mm-dd'),' ') as expiration_date,
                      t1.protocol_status_code,
                      t2.description as protocol_status,
                      t1.protocol_type_code,
                      t3.description as protocol_type,
                      t5.submission_status_code,
                      t7.PERSON_NAME as PI_NAME,
                      nvl(t6.description,' ') as submission_status,						
                      to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
                      FROM IRB_PROTOCOL t1
                      INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
                      INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
                       ]'
                       ||ls_join_sql||
                       q'[
                      INNER JOIN irb_protocol_persons t7 on t1.protocol_id = t7.protocol_id and t7.PROTOCOL_PERSON_ROLE_ID = 'PI'											
                      LEFT OUTER JOIN (
                         select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
                         where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
                                        where s1.protocol_id = s2.protocol_id)
                      )t5 on t5.protocol_id = t1.protocol_id
                      left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
                      WHERE t1.is_latest = 'Y' and t1.protocol_status_code not in (300,303,400,401,901) ]'
                      ||ls_filter_condition
                      ||' order by t1.update_timestamp desc
                      ) WHERE rownum < 100';
		
 
		open cur_generic for ls_dyn_sql;
		

END;
/