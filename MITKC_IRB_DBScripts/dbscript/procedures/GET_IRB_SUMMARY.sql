create or replace PROCEDURE GET_IRB_SUMMARY(
AV_PERSON_ID        IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE IN VARCHAR2,
CUR_GENERIC         OUT SYS_REFCURSOR)
IS
  li_amend_renew_count pls_integer;
  li_revsion_req_count pls_integer;
  li_awaiting_resp_count pls_integer;
  
  ls_committee_name_prev VARCHAR2(60);
  ls_scheduled_date_prev VARCHAR2(20);
  ls_scheduled_day_prev  VARCHAR2(20);
  
  ls_committee_name_next VARCHAR2(60);
  ls_scheduled_date_next VARCHAR2(20);
  ls_scheduled_day_next  VARCHAR2(20);
BEGIN

IF AV_PERSON_ROLE_TYPE IN( 'ADMIN','CHAIR') THEN
	select distinct t2.committee_name,to_char(t1.scheduled_date,'MM/DD/YYYY') as scheduled_date,to_char(t1.scheduled_date, 'Day') as scheduled_day
	into ls_committee_name_prev,ls_scheduled_date_prev,ls_scheduled_day_prev
	from comm_schedule t1
	inner join committee t2 on t1.committee_id_fk = t2.ID
	where t1.id = (select max(id)from comm_schedule 
					where scheduled_date = (select max(scheduled_date) from comm_schedule where scheduled_date < sysdate));
					
	select distinct t2.committee_name,to_char(t1.scheduled_date,'MM/DD/YYYY') as scheduled_date,to_char(t1.scheduled_date, 'Day') as scheduled_day
	into ls_committee_name_next,ls_scheduled_date_next,ls_scheduled_day_next
	from comm_schedule t1
	inner join committee t2 on t1.committee_id_fk = t2.ID
	where t1.id = (select max(id)from comm_schedule 
					where scheduled_date = (select max(scheduled_date) from comm_schedule where scheduled_date >= sysdate));
	
	SELECT sum(count(t1.protocol_status_code))  
    into li_revsion_req_count
    FROM MITKC_IRB_PROTOCOL t1       
    INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id        
    WHERE t1.is_latest = 'Y'
    AND t1.protocol_status_code in (102,104,107)
    AND t4.person_id = AV_PERSON_ID
    group by t1.protocol_status_code;
	
	OPEN CUR_GENERIC FOR
    SELECT 
          ls_committee_name_prev as committee_name_prev,
		  ls_scheduled_date_prev as scheduled_date_prev,
		  ls_scheduled_day_prev	 as scheduled_day_prev,  
		  ls_committee_name_next as committee_name_next,
		  ls_scheduled_date_next as scheduled_date_next,
		  ls_scheduled_day_next as scheduled_day_next,
		  li_revsion_req_count AS REVISION_REQ_COUNT         
    FROM DUAL;

ELSE

    SELECT sum(count(t1.protocol_status_code))  
    into li_amend_renew_count
    FROM MITKC_IRB_PROTOCOL t1       
    INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id        
    WHERE t1.is_latest = 'Y'
    AND t1.protocol_status_code in (105,106)
    AND t4.person_id = AV_PERSON_ID
    group by t1.protocol_status_code;

    SELECT sum(count(t1.protocol_status_code))  
    into li_revsion_req_count
    FROM MITKC_IRB_PROTOCOL t1       
    INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id        
    WHERE t1.is_latest = 'Y'
    AND t1.protocol_status_code in (102,104,107)
    AND t4.person_id = AV_PERSON_ID
    group by t1.protocol_status_code;
    
    SELECT sum(count(t1.protocol_status_code))  
    into li_awaiting_resp_count
    FROM MITKC_IRB_PROTOCOL t1       
    INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id        
    WHERE t1.is_latest = 'Y'
    AND t1.protocol_status_code = 101 
    AND t4.person_id = AV_PERSON_ID
    group by t1.protocol_status_code;
	
	OPEN CUR_GENERIC FOR
    SELECT 
         li_amend_renew_count AS AMMEND_RENEW_COUNT,
         li_revsion_req_count AS REVISION_REQ_COUNT, 
         li_awaiting_resp_count AS AWAITING_RESP_COUNT
    FROM DUAL;

END IF;


END;
/