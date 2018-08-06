create or replace PROCEDURE UPD_IRB_EXEMPT_FORM_CHECKLST(
AV_EXEMPT_FORM_CHECKLST_ID		IN MITKC_IRB_EXEMPT_FORM_CHECKLST.EXEMPT_FORM_CHECKLST_ID%TYPE,
AV_IRB_PERSON_EXEMPT_FORM_ID	IN MITKC_IRB_EXEMPT_FORM_CHECKLST.IRB_PERSON_EXEMPT_FORM_ID%TYPE,
AV_DESCRIPTION					IN MITKC_IRB_EXEMPT_FORM_CHECKLST.DESCRIPTION%TYPE,
AV_FILE_NAME					IN MITKC_IRB_EXEMPT_FORM_CHECKLST.FILENAME%TYPE,
AV_FILE_DATA					IN MITKC_IRB_EXEMPT_FORM_CHECKLST.FILE_DATA%TYPE,
AV_CONTENT_TYPE					IN MITKC_IRB_EXEMPT_FORM_CHECKLST.CONTENT_TYPE%TYPE,
AV_UPDATE_USER					IN MITKC_IRB_EXEMPT_FORM_CHECKLST.UPDATE_USER%TYPE,
AC_TYPE							IN VARCHAR2
)
IS

BEGIN
	
	IF 	AC_TYPE = 'I' THEN
		
			
		INSERT INTO MITKC_IRB_EXEMPT_FORM_CHECKLST(
			EXEMPT_FORM_CHECKLST_ID,
			IRB_PERSON_EXEMPT_FORM_ID,
			DESCRIPTION,
			FILENAME,
			FILE_DATA,
			CONTENT_TYPE,
			UPDATE_TIMESTAMP,
			UPDATE_USER
		)
		VALUES(
			SEQ_EXEMPT_FORM_CHECKLST_ID.NEXTVAL,
			AV_IRB_PERSON_EXEMPT_FORM_ID,
			AV_DESCRIPTION,
			AV_FILE_NAME,
			AV_FILE_DATA,
			AV_CONTENT_TYPE,
			SYSDATE,
			AV_UPDATE_USER
		);
	ELSIF  AC_TYPE = 'U' THEN
		UPDATE MITKC_IRB_EXEMPT_FORM_CHECKLST
		SET  DESCRIPTION = AV_DESCRIPTION,
			 FILENAME = AV_FILE_NAME,
		     FILE_DATA = AV_FILE_DATA,
		     CONTENT_TYPE = AV_CONTENT_TYPE,
		     UPDATE_TIMESTAMP = SYSDATE,
		     UPDATE_USER = AV_UPDATE_USER					 
		WHERE EXEMPT_FORM_CHECKLST_ID = AV_EXEMPT_FORM_CHECKLST_ID;
	
	ELSIF  AC_TYPE = 'D' THEN
		DELETE FROM MITKC_IRB_EXEMPT_FORM_CHECKLST WHERE EXEMPT_FORM_CHECKLST_ID = AV_EXEMPT_FORM_CHECKLST_ID;
	
	END IF;
END;
/