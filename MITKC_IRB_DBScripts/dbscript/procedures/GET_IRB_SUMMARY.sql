create or replace PROCEDURE GET_IRB_SUMMARY(
AV_PERSON_ID        IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PERSON_ROLE_TYPE IN VARCHAR2,
CUR_GENERIC         OUT SYS_REFCURSOR)
IS
  li_amend_renew_count pls_integer;
  li_revsion_req_count pls_integer;
  li_awaiting_resp_count pls_integer;
BEGIN

IF AV_PERSON_ROLE_TYPE = 'PI' THEN
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

END IF;

    OPEN CUR_GENERIC FOR
    SELECT 
         li_amend_renew_count AS AMMEND_RENEW_COUNT,
         li_revsion_req_count AS REVISION_REQ_COUNT, 
         li_awaiting_resp_count AS AWAITING_RESP_COUNT
    FROM DUAL;
END;
/