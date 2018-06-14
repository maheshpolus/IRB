create or replace procedure GET_IRB_PROTOCOL_HISTORY_GROUP(
AV_PROTOCOL_NUMBER  IN MITKC_IRB_PROTOCOL.protocol_number%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
li_active_protocol_id NUMBER;
BEGIN

	begin
		select PROTOCOL_ID 
		into li_active_protocol_id
		from MITKC_IRB_PROTOCOL 
		where PROTOCOL_NUMBER  = AV_PROTOCOL_NUMBER 
		and active = 'Y';
	exception
	when others then
		li_active_protocol_id := 0;
	end;	
	
    OPEN CUR_GENERIC FOR
		SELECT
			  ACTION_ID, 
			  decode(PROTOCOL_ACTION_TYPE_CODE,100,'Initial Protocol Submission',
			  								   102,'Renewal -0001 Submission',
											   103,'Ammendment -001 Submission',
											   107,'Amendment/Renewal-001 Submissiom')
			  COMMENTS, 
			  PROTOCOL_ID,
			  PROTOCOL_ACTION_TYPE_CODE,
			  LAG(ACTION_ID,1,0) OVER (ORDER BY ACTION_ID) AS PREVIOUS_GROUP_ACTION_ID,
			  LEAD(ACTION_ID,1,0) OVER (ORDER BY ACTION_ID) AS NEXT_GROUP_ACTION_ID
		FROM 
		(
			  SELECT 
			  ACTION_ID, 
			  COMMENTS, 
			  PROTOCOL_ID,
			  PROTOCOL_ACTION_TYPE_CODE
			  FROM   MITKC_IRB_PROTOCOL_ACTIONS
			  WHERE PROTOCOL_ID = li_active_protocol_id
			  AND PROTOCOL_ACTION_TYPE_CODE IN (100,102,103,107)
			  UNION
			  SELECT 
			  ACTION_ID, 
			  COMMENTS, 
			  PROTOCOL_ID,
			  PROTOCOL_ACTION_TYPE_CODE
			  FROM   MITKC_IRB_PROTOCOL_ACTIONS
			  WHERE PROTOCOL_ID > li_active_protocol_id
			  AND PROTOCOL_NUMBER LIKE SUBSTR(AV_PROTOCOL_NUMBER,1,10)||'%'
			  AND PROTOCOL_ACTION_TYPE_CODE IN (100,102,103,107)
		);
END;	
/