create or replace procedure GET_MITKC_ATTACHMENT_FILE(
AV_FIL_ID	 IN 	ATTACHMENT_FILE.FILE_ID%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
li_count PLS_INTEGER;
BEGIN

	select count(file_id)
	into li_count
	from attachment_file
	where file_data is not null
	and file_id = AV_FIL_ID;
	
	if li_count > 0 then	
		OPEN CUR_GENERIC FOR
			select  t1.file_name,
					t1.file_data,
					t1.content_type
			from attachment_file t1 
			WHERE t1.FILE_ID = AV_FIL_ID;
	else
		OPEN CUR_GENERIC FOR
			select  t1.file_name,
					t2.data as file_data,
					t1.content_type
			from attachment_file t1 
			inner join file_data t2 on t1.file_data_id = t2.id
			WHERE t1.FILE_ID = AV_FIL_ID;
			
	end if;
	
END;
/