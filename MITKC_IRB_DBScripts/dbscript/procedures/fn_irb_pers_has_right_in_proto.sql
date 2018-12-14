create or replace function fn_irb_pers_has_right_in_proto(
AV_PERSON_ID        	IN KRIM_PRNCPL_T.PRNCPL_ID%TYPE,
AV_PROTOCOL_NUMBER		IN VARCHAR2
)RETURN NUMBER
IS
li_count PLS_INTEGER;
BEGIN

	select count(mbr_id)
	into li_count
	from krim_role_t t1
	inner join krim_role_mbr_t t2 on t1.role_id = t2.role_id
	where t2.mbr_id = AV_PERSON_ID
	and t1.role_nm =  'IRB Administrator';

	if li_count > 0 then
		return 1;
	end if;

	SELECT COUNT(t4.person_id)	
	into li_count	
	FROM IRB_PROTOCOL t1
	INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id			
	WHERE t1.is_latest = 'Y'
	AND t1.protocol_number = AV_PROTOCOL_NUMBER
	and t4.person_id = av_person_id;

	if li_count > 0 then
		return 1;
	end if;
  
    SELECT COUNT(t1.protocol_number)
	into li_count	
	FROM IRB_PROTOCOL t1			
	WHERE t1.is_latest = 'Y'
	AND t1.protocol_number = AV_PROTOCOL_NUMBER
	AND t1.LEAD_UNIT_NUMBER in (SELECT UNIT_NUMBER FROM MITKC_USER_RIGHT_MV WHERE PERSON_ID = AV_PERSON_ID	AND PERM_NM = 'IRB_Dept_Administrator');
	   
	if li_count > 0 then
		return 1;
	end if;
  
	RETURN 0;	

END;
/

