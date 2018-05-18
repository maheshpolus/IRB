create or replace procedure GET_IRB_PROTOCOL_SPECIAL_REVW(
AV_PROTOCOL_NUMBER  IN MITKC_IRB_PROTOCOL.protocol_number%type,
CUR_GENERIC         OUT SYS_REFCURSOR
)IS
BEGIN
    OPEN CUR_GENERIC FOR
		SELECT 					
			t3.description as SPECIAL_REVIEW,
			t4.DESCRIPTION as APPROVAL_TYPE,
			t2.APPLICATION_DATE,
			t2.EXPIRATION_DATE,
			t2.APPROVAL_DATE,
			t2.COMMENTS
		FROM MITKC_IRB_PROTOCOL t1
		INNER JOIN MITKC_IRB_PROTO_SPECIAL_REVIEW t2 on t1.PROTOCOL_ID = t2.PROTOCOL_ID
		INNER JOIN SPECIAL_REVIEW t3 on t2.SPECIAL_REVIEW_CODE = t3.SPECIAL_REVIEW_CODE
		INNER JOIN SP_REV_APPROVAL_TYPE	t4 on t2.APPROVAL_TYPE_CODE = t4.APPROVAL_TYPE_CODE
		WHERE t1.is_latest = 'Y'
		AND t1.protocol_number = AV_PROTOCOL_NUMBER
		ORDER BY t2.PROTOCOL_SPECIAL_REVIEW_ID;
END;
/