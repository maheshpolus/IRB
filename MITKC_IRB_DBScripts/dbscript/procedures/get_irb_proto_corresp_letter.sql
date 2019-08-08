create or replace procedure GET_IRB_PROTO_CORRESP_LETTER(
AV_PROTOCOL_ACTION_ID		IN IRB_PROTOCOL_ACTIONS.PROTOCOL_ACTION_ID%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
	OPEN CUR_GENERIC FOR	
	    select t1.CORRESPONDENCE
		from IRB_PROTOCOL_CORRESPONDENCE t1		
		where t1.PROTOCOL_ACTION_ID = AV_PROTOCOL_ACTION_ID
		and t1.protocol_correspondence_id = ( select max(s1.protocol_correspondence_id) from IRB_PROTOCOL_CORRESPONDENCE s1
                                          where s1.PROTOCOL_ACTION_ID = t1.PROTOCOL_ACTION_ID);
END;
/
