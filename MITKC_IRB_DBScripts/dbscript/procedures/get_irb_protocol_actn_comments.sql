create or replace procedure get_irb_protocol_actn_comments(
AV_PROTOCOL_NUMBER 				IN IRB_PROTOCOL.protocol_number%type,
AV_PROTOCOL_ACTION_ID			IN IRB_PROTOCOL_ACTIONS.PROTOCOL_ACTION_ID%type,
AV_PROTOCOL_ACTION_TYPE_CODE	IN IRB_PROTOCOL_ACTIONS.PROTOCOL_ACTION_TYPE_CODE%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
ls_protocol_number  varchar2(60);
ls_sub_number varchar2(4);
BEGIN

	IF AV_PROTOCOL_ACTION_TYPE_CODE in (103,102,117) THEN
		
		IF AV_PROTOCOL_ACTION_TYPE_CODE = 103 THEN
			 select 'A'||Substr(comments,length('Amendment-') +1,3 ) 
			 into ls_sub_number
			 from irb_protocol_actions where protocol_action_id = AV_PROTOCOL_ACTION_ID;
		 
		ELSIF  AV_PROTOCOL_ACTION_TYPE_CODE = 102 THEN
	
			 select 'R'||Substr(comments,length('Renewal-') +1,3 ) 
			 into ls_sub_number
			 from irb_protocol_actions where protocol_action_id = AV_PROTOCOL_ACTION_ID;
		
		ELSE
		
			 select 'R'||Substr(comments,length('Renewal/Amendment-') +1,3 ) 
			 into ls_sub_number
			 from irb_protocol_actions where protocol_action_id = AV_PROTOCOL_ACTION_ID;
			 
		END IF;	
	
			ls_protocol_number := AV_PROTOCOL_NUMBER||ls_sub_number;
	
	
		OPEN CUR_GENERIC FOR			
			select summary as comments,date_created as create_timestamp from proto_amend_renewal
			where proto_amend_ren_number = ls_protocol_number
			order by create_timestamp desc;
	ELSE
		OPEN CUR_GENERIC FOR
			select  minute_entry as comments,create_timestamp from comm_schedule_minutes 
			where (PROTOCOL_NUMBER,SUBMISSION_NUMBER) in ( select PROTOCOL_NUMBER,SUBMISSION_NUMBER from IRB_PROTOCOL_ACTIONS where PROTOCOL_ACTION_ID = AV_PROTOCOL_ACTION_ID );			
	END IF;

	
END;
/