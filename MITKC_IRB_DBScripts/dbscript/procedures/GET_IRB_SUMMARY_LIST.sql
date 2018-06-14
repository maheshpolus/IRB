create or replace PROCEDURE GET_IRB_SUMMARY_LIST(
AV_PERSON_ID        IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE IN VARCHAR2,
AV_SUMMARY_TYPE IN VARCHAR2,
CUR_GENERIC         OUT SYS_REFCURSOR)
IS 
BEGIN
    
IF AV_SUMMARY_TYPE = 'AMMEND_RENEW' THEN
					OPEN CUR_GENERIC FOR
						SELECT 
						t1.protocol_number,
						t1.protocol_id,
						t1.title,
						to_char(t1.last_approval_date,'yyyy-mm-dd') as last_approval_date,
						to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
						t1.protocol_status_code,
						t2.description as protocol_status,
						t1.protocol_type_code,
						t3.description as protocol_type,
						t5.submission_status_code,
						t6.description as submission_status,
						t8.unit_name,
						to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
						FROM MITKC_IRB_PROTOCOL t1
						INNER JOIN mitkc_irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
						INNER JOIN mitkc_irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
						INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id
						LEFT OUTER JOIN mitkc_irb_protocol_units t7 on t4.protocol_person_id = t7.protocol_person_id and t7.lead_unit_flag = 'Y'
						LEFT OUTER JOIN unit t8 on t8.unit_number = t7.unit_number
						LEFT OUTER JOIN (
							 select s1.protocol_id,s1.submission_status_code  from MITKC_IRB_PROTOCOL_SUBMISSION s1
							 where s1.submission_number = ( select max(s2.submission_number)  from MITKC_IRB_PROTOCOL_SUBMISSION s2
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
						to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
						t1.protocol_status_code,
						t2.description as protocol_status,
						t1.protocol_type_code,
						t3.description as protocol_type,
						t5.submission_status_code,
						t6.description as submission_status,
						t8.unit_name,
						to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
						FROM MITKC_IRB_PROTOCOL t1
						INNER JOIN mitkc_irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
						INNER JOIN mitkc_irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
						INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id
						LEFT OUTER JOIN mitkc_irb_protocol_units t7 on t4.protocol_person_id = t7.protocol_person_id and t7.lead_unit_flag = 'Y'
						LEFT OUTER JOIN unit t8 on t8.unit_number = t7.unit_number
						LEFT OUTER JOIN (
							 select s1.protocol_id,s1.submission_status_code  from MITKC_IRB_PROTOCOL_SUBMISSION s1
							 where s1.submission_number = ( select max(s2.submission_number)  from MITKC_IRB_PROTOCOL_SUBMISSION s2
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
						to_char(t1.expiration_date,'yyyy-mm-dd') as expiration_date,
						t1.protocol_status_code,
						t2.description as protocol_status,
						t1.protocol_type_code,
						t3.description as protocol_type,
						t5.submission_status_code,
						t6.description as submission_status,
						t8.unit_name,
						to_char(t1.update_timestamp,'yyyy-mm-dd') as update_timestamp 
						FROM MITKC_IRB_PROTOCOL t1
						INNER JOIN mitkc_irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
						INNER JOIN mitkc_irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
						INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id
						LEFT OUTER JOIN mitkc_irb_protocol_units t7 on t4.protocol_person_id = t7.protocol_person_id and t7.lead_unit_flag = 'Y'
						LEFT OUTER JOIN unit t8 on t8.unit_number = t7.unit_number
						LEFT OUTER JOIN (
							 select s1.protocol_id,s1.submission_status_code  from MITKC_IRB_PROTOCOL_SUBMISSION s1
							 where s1.submission_number = ( select max(s2.submission_number)  from MITKC_IRB_PROTOCOL_SUBMISSION s2
															where s1.protocol_id = s2.protocol_id)
						)t5 on t5.protocol_id = t1.protocol_id
						left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
						WHERE t1.is_latest = 'Y'
						AND t1.protocol_status_code = 101
						AND t4.person_id = AV_PERSON_ID;
END IF;
END;
/