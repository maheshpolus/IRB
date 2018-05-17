delete from MITKC_ELASTIC_INDEX where MODULE_CODE = 7
/
commit
/
INSERT INTO MITKC_ELASTIC_INDEX(
  MODULE_CODE,
  MODULE_ITEM_KEY,
  MODULE_ITEM_ID,
  PI_NAME,
  LEAD_UNIT_NUMBER,
  LEAD_UNIT_NAME,
  TITLE, 
  LOAD_TIMESTAMP,
  STATUS_CODE,
  STATUS,
  PROTOCOL_TYPE,
  SUBMISSION_STATUS,
  APPROVAL_DATE,
  EXPIRATION_DATE
)
SELECT 
7,
t1.protocol_number,
t1.protocol_id,
t4.person_name,
 t7.unit_number,
t8.unit_name,
t1.title,
sysdate,
t1.protocol_status_code,
t2.description as protocol_status,
t3.description as protocol_type,
t6.description as submission_status,
to_char(t1.last_approval_date,'mm-dd-yyyy'),
to_char(t1.expiration_date,'mm-dd-yyyy')
FROM MITKC_IRB_PROTOCOL t1
INNER JOIN mitkc_irb_protocol_status t2 on t1.protocol_status_code = t2.protocol_status_code
INNER JOIN mitkc_irb_protocol_type t3 on t1.protocol_type_code = t3.protocol_type_code
INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id and t4.PROTOCOL_PERSON_ROLE_ID = 'PI'
INNER JOIN mitkc_irb_protocol_units t7 on t4.protocol_person_id = t7.protocol_person_id and t7.lead_unit_flag = 'Y'
LEFT OUTER JOIN unit t8 on t7.unit_number = t8.unit_number
LEFT OUTER JOIN (
select s1.protocol_id,s1.submission_status_code  from MITKC_IRB_PROTOCOL_SUBMISSION s1
where s1.submission_number = ( select max(s2.submission_number)  from MITKC_IRB_PROTOCOL_SUBMISSION s2
		where s1.protocol_id = s2.protocol_id)
)t5 on t5.protocol_id = t1.protocol_id
left outer join submission_status t6 on t5.submission_status_code = t6.submission_status_code
WHERE t1.is_latest = 'Y'
/
commit
/