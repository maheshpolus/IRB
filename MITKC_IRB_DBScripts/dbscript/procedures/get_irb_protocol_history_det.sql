create or replace procedure GET_IRB_PROTOCOL_HISTORY_DET(
AV_PROTOCOL_ID  			IN IRB_PROTOCOL.PROTOCOL_ID%type,
AV_ACTION_ID				IN IRB_PROTOCOL_ACTIONS.ACTION_ID%type,
AV_NEXT_GROUP_ACTION_ID		IN IRB_PROTOCOL_ACTIONS.ACTION_ID%type,
AV_PREVIOUS_GROUP_ACTION_ID IN IRB_PROTOCOL_ACTIONS.ACTION_ID%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
li_active_protocol_id   NUMBER;
li_next_group_action_id NUMBER := AV_NEXT_GROUP_ACTION_ID;

BEGIN

	if li_next_group_action_id	= 0 then
		li_next_group_action_id := AV_ACTION_ID;
	else
		li_next_group_action_id	:= li_next_group_action_id - 1;
	end if;

	OPEN CUR_GENERIC FOR	
	SELECT 
		T1.PROTOCOL_ACTION_ID,		
		T1.ACTION_ID,
		T2.DESCRIPTION as PROTOCOL_ACTION,
		T1.COMMENTS, 
		T1.PROTOCOL_ACTION_TYPE_CODE,
		(
		CASE
		  WHEN T1.PROTOCOL_ACTION_TYPE_CODE in (201,202,203,204,205,206,207,209,210,211,212,300,301,302,303,304,305) THEN 'Y'		 
		  ELSE 'N'
		END
		) AS LETTER_FLAG,	
		(
		CASE
		  WHEN T1.PROTOCOL_ACTION_TYPE_CODE in (201,202,203,204,205,206,207,209,210,211,212,300,301,302,303,304,305,103,102,117) THEN 'Y'		 
		  ELSE 'N'
		END
		) AS MINUTES_FLAG,	
		(replace( replace (replace(T2.DESCRIPTION,' ','_'),'(',''),')','')||'_Letter') as PROTO_CORRESP_TYPE,
		t2.DESCRIPTION as PROTO_CORRESP_TYPE,
		to_char(T1.ACTION_DATE,'mm/dd/yyyy') as ACTION_DATE,		
		to_char(T1.UPDATE_TIMESTAMP,'mm/dd/yyyy hh:mi:ss am') as update_timestamp,
		t3.full_name as update_user_name
	FROM IRB_PROTOCOL_ACTIONS T1 
	INNER JOIN	IRB_PROTOCOL_ACTION_TYPE T2 ON T1.PROTOCOL_ACTION_TYPE_CODE = T2.PROTOCOL_ACTION_TYPE_CODE
	INNER JOIN WHOSP_PERSON_HISTORY T3 ON (T3.USER_NAME) = (T1.UPDATE_USER)	
	WHERE T1.PROTOCOL_ID = AV_PROTOCOL_ID
	AND T1.PROTOCOL_ACTION_TYPE_CODE NOT IN (109,111,112,110)
	AND T1.ACTION_ID BETWEEN AV_ACTION_ID AND li_next_group_action_id
	ORDER BY ACTION_ID desc;

	/*	
	201	Deferred
	202	Substantive Revisions Required
	203	Specific Minor Revisions Required
	204	Approved
	205	Expedited Approval
	206	Exemption Granted
	207	Closed for Enrollment
	209	IRB Acknowledgement
	210	IRB review not required
	211	Data Analysis Only
	212	Re-open Enrollment
	300	Closed (Administratively closed)
	301	Terminated
	302	Suspended
	303	Withdrawn
	304	Disapproved
	305	Expired
	*/	
	
END;
/