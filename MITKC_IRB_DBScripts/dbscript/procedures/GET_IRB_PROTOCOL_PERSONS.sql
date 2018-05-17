create or replace procedure GET_IRB_PROTOCOL_PERSONS(
AV_PROTOCOL_NUMBER  IN MITKC_IRB_PROTOCOL.protocol_number%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
    OPEN CUR_GENERIC FOR
		SELECT 
		t4.person_id,
		t4.person_name,
		t4.protocol_person_role_id,
		t9.description as affiliation_type,
		t8.unit_name,
		'Y' as is_completed
		FROM MITKC_IRB_PROTOCOL t1
		INNER JOIN mitkc_irb_protocol_persons t4 on t1.protocol_id = t4.protocol_id		
		LEFT OUTER JOIN	kc_person_mv t5 on t4.person_id = t5.prncpl_id
		LEFT OUTER JOIN unit t8 on t5.unit_number = t8.unit_number		
		LEFT OUTER JOIN affiliation_type t9 on t4.affiliation_type_code = t9.affiliation_type_code		
		WHERE t1.is_latest = 'Y'
		AND t1.protocol_number = AV_PROTOCOL_NUMBER;
END;
/