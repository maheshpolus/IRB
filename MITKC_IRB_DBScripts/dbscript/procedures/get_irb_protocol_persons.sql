create or replace procedure GET_IRB_PROTOCOL_PERSONS(
AV_PROTOCOL_NUMBER  IN IRB_PROTOCOL.protocol_number%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
    OPEN CUR_GENERIC FOR
		SELECT 
		t4.person_id,
		t4.person_name,		
		t10.description as protocol_person_role,
		t4.protocol_person_role_id,	
		decode(t4.protocol_person_role_id, 'PI',1,'COI',2,3) as SORT_ORDER,
		t9.description as affiliation_type,
		t8.unit_name,
		t4.email_address,
		t4.office_phone,
		t4.primary_title,
		'Y' as is_completed
		FROM IRB_PROTOCOL t1
		INNER JOIN irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id		
		LEFT OUTER JOIN	WHOSP_PERSON_HISTORY t5 on t4.person_id = t5.person_id
		LEFT OUTER JOIN unit t8 on t5.home_unit = t8.unit_number		
		LEFT OUTER JOIN affiliation_type t9 on t4.affiliation_type_code = t9.affiliation_type_code	
		INNER JOIN protocol_person_roles t10 on t4.protocol_person_role_id = t10.protocol_person_role_id
		WHERE t1.is_latest = 'Y'
		AND t1.protocol_number = AV_PROTOCOL_NUMBER
		ORDER BY SORT_ORDER;
END;
/