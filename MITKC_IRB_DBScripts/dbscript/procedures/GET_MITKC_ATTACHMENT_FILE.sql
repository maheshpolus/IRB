create or replace procedure GET_MITKC_ATTACHMENT_FILE(
AV_FIL_ID	 IN 	ATTACHMENT_FILE.FILE_ID%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
    OPEN CUR_GENERIC FOR
		select  t1.file_name,
				t1.file_data,
				t1.content_type
		from attachment_file t1 
		WHERE t1.FILE_ID = AV_FIL_ID;
END;
/