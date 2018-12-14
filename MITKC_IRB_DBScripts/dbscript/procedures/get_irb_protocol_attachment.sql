create or replace procedure GET_IRB_PROTOCOL_ATTACHMENT(
AV_PROTOCOL_NUMBER  IN IRB_PROTOCOL.protocol_number%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
    OPEN CUR_GENERIC FOR
		select t3.description as attachment_type,
			   t2.type_cd as attachment_type_code,
			   t2.description,			
			   t2.attachment_version,			   
			   t5.file_name,
			   t2.file_id,
			   nvl(t4.full_name,t2.update_user) as update_user_name,			   
			   to_char(t2.update_timestamp,'yyyy/mm/dd hh:mi:ss am') as update_timestamp
		from IRB_PROTOCOL t1
		inner join IRB_ATTACHMENT_PROTOCOL t2 on t1.PROTOCOL_ID = t2.PROTOCOL_ID
		inner join IRB_ATTACHMENT_TYPE t3 on t2.type_cd = t3.type_cd
		left outer join WHOSP_PERSON_HISTORY t4 on (t4.USER_NAME) = (t2.update_user)
		inner join attachment_file t5 on t5.file_id = t2.file_id
		WHERE t1.is_latest = 'Y'
		AND t1.protocol_number = AV_PROTOCOL_NUMBER
		order by t2.document_id;
END;
/