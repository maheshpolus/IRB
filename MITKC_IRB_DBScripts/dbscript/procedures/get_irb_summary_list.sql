create or replace PROCEDURE GET_IRB_SUMMARY_LIST(
AV_PERSON_ID        IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE IN VARCHAR2,
AV_SUMMARY_TYPE IN VARCHAR2,
CUR_GENERIC         OUT SYS_REFCURSOR)
IS 
BEGIN

IF AV_PERSON_ROLE_TYPE = 'PI' THEN
   
				IF AV_SUMMARY_TYPE = 'AMMEND_RENEW' THEN
									OPEN CUR_GENERIC FOR
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
										INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id										
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_status_code in (105,106)
										AND t4.person_id = AV_PERSON_ID;
										
				ELSIF AV_SUMMARY_TYPE = 'REVISION_REQ' THEN	
									OPEN CUR_GENERIC FOR	
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,										
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
										INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_status_code in (102,104,107)
										AND t4.person_id = AV_PERSON_ID;

				ELSE
									OPEN CUR_GENERIC FOR
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,										
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
										INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_status_code = 101
										AND t4.person_id = AV_PERSON_ID;
				END IF;
				
ELSIF AV_PERSON_ROLE_TYPE = 'DEPT_ADMIN' THEN

				IF AV_SUMMARY_TYPE = 'AMMEND_RENEW' THEN
									OPEN CUR_GENERIC FOR
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_id in (
																SELECT protocol_id from irb_protocol_persons where person_id = AV_PERSON_ID
																union
																SELECT protocol_id from irb_protocol_units 
																where lead_unit_flag = 'Y' 
																and unit_number in (SELECT UNIT_NUMBER FROM MITKC_USER_RIGHT_MV WHERE PERSON_ID = AV_PERSON_ID AND PERM_NM = 'IRB_Dept_Administrator')
															)										
										AND t1.protocol_status_code in (105,106);
										
				ELSIF AV_SUMMARY_TYPE = 'REVISION_REQ' THEN	
									OPEN CUR_GENERIC FOR	
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code									
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_status_code in (102,104,107)
										AND t1.protocol_id in (
																SELECT protocol_id from irb_protocol_persons where person_id = AV_PERSON_ID
																union
																SELECT protocol_id from irb_protocol_units 
																where lead_unit_flag = 'Y' 
																and unit_number in (SELECT UNIT_NUMBER FROM MITKC_USER_RIGHT_MV WHERE PERSON_ID = AV_PERSON_ID AND PERM_NM = 'IRB_Dept_Administrator')
															);

				ELSE
									OPEN CUR_GENERIC FOR
										SELECT 
										t1.protocol_number,
										t1.protocol_id,
										t1.title,
										to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
										to_char(t1.approval_date,'yyyy-mm-dd') as approval_date,
										to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
										t1.protocol_status_code,
										t2.description as protocol_status,
										t1.protocol_type_code,
										t3.description as protocol_type,
										t5.submission_status_code,
										t6.description as submission_status,
										t8.unit_name,
										to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
										FROM IRB_PROTOCOL t1
										INNER JOIN irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
										INNER JOIN irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
										LEFT OUTER JOIN unit t8 on t8.unit_number = t1.lead_unit_number
										LEFT OUTER JOIN (
											 select s1.protocol_id,s1.submission_status_code  from IRB_PROTOCOL_SUBMISSION s1
											 where s1.submission_number = ( select max(s2.submission_number)  from IRB_PROTOCOL_SUBMISSION s2
																			where s1.protocol_id = s2.protocol_id)
										)t5 on t5.protocol_id = t1.protocol_id
										left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
										WHERE t1.is_latest = 'Y'
										AND t1.protocol_status_code = 101
										AND t1.protocol_id in (
																SELECT protocol_id from irb_protocol_persons where person_id = AV_PERSON_ID
																union
																SELECT protocol_id from irb_protocol_units 
																where lead_unit_flag = 'Y' 
																and unit_number in (SELECT UNIT_NUMBER FROM MITKC_USER_RIGHT_MV WHERE PERSON_ID = AV_PERSON_ID AND PERM_NM = 'IRB_Dept_Administrator')
															);
				END IF;

END IF;

END;
/