create or replace PROCEDURE GET_IRB_PERSON_EXEMPT_MESSAGE(
AV_IRB_PERSON_EXEMPT_FORM_ID IN  IRB_PERSON_EXEMPT_FORM.IRB_PERSON_EXEMPT_FORM_ID%TYPE,
CUR_GENERIC OUT SYS_REFCURSOR)
IS
li_qnr_ans_hdr 	IRB_PERSON_EXEMPT_FORM.QUESTIONNAIRE_ANS_HEADER_ID%TYPE;
ls_is_exempt_granted VARCHAR2(1);
ls_exempt_msg_A 	 VARCHAR2(300) := 'COUHES review is not required. No further action needed.'; 
ls_exempt_msg_B 	 VARCHAR2(300):= 'Your study is not eligible for exempt status and requires comprehensive review. Please see COUHES.MIT.edu for instructions.';  
ls_exempt_msg_C 	 VARCHAR2(300):= 'Your study is eligible for exempt status. Please see the next page for the Investigator Compliance Guidelines.';  
ls_exempt_msg_D 	 VARCHAR2(300):= 'Your study is eligible for exempt status. If the biospecimen or PHI (private health information) is from a covered entity, the institution sending the data must comply with HIPPA regulations. Please see the next page for the Investigator Compliance Guidelines.';  
ls_exempt_msg_E 	 VARCHAR2(300):= 'Your study does not meet any of the criteria for exempt status and requires comprehensive review. Please see the COUHES website for instructions.';  
li_count 			NUMBER;
ls_question     MITKC_QUESTIONNAIRE_QUESTION.QUESTION%TYPE;
ls_question_id  MITKC_QUESTIONNAIRE_QUESTION.QUESTION_ID%TYPE;
BEGIN

	BEGIN
		SELECT QUESTIONNAIRE_ANS_HEADER_ID
		INTO li_qnr_ans_hdr
		FROM IRB_PERSON_EXEMPT_FORM
		WHERE IRB_PERSON_EXEMPT_FORM_ID = AV_IRB_PERSON_EXEMPT_FORM_ID;
	EXCEPTION
	WHEN OTHERS THEN 
		li_qnr_ans_hdr := -1;
	END;

		
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID IN (112,113) 
	AND ANSWER = 'No';
	
	IF li_count > 0 THEN
		
		BEGIN
			SELECT T0.QUESTION_ID,T0.QUESTION
			INTO ls_question_id,ls_question
			FROM MITKC_QUESTIONNAIRE_ANSWER t1
			INNER JOIN  MITKC_QUESTIONNAIRE_QUESTION T0 on t1.QUESTION_ID = T0.QUESTION_ID
			WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr	
			AND QUESTIONNAIRE_ANSWER_ID = ( select max(t2.QUESTIONNAIRE_ANSWER_ID)
											from MITKC_QUESTIONNAIRE_ANSWER t2 
											where t1.QUESTIONNAIRE_ANS_HEADER_ID = t2.QUESTIONNAIRE_ANS_HEADER_ID
											AND t2.QUESTION_ID IN (112,113) 
											AND t2.ANSWER = 'No'
											);
		EXCEPTION
		WHEN OTHERS THEN
		ls_question_id := null;
		ls_question := null;
		END;
			
	
		OPEN CUR_GENERIC FOR
			SELECT 'O' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_A as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION
			FROM DUAL;
		return;	
	END IF;


	
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID IN (115,121,129,125,135) 
	AND ANSWER = 'No';
	
	IF li_count > 0 THEN
	
		BEGIN
			SELECT T0.QUESTION_ID,T0.QUESTION
			INTO ls_question_id,ls_question
			FROM MITKC_QUESTIONNAIRE_ANSWER t1
			INNER JOIN  MITKC_QUESTIONNAIRE_QUESTION T0 on t1.QUESTION_ID = T0.QUESTION_ID
			WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr	
			AND QUESTIONNAIRE_ANSWER_ID = ( select max(t2.QUESTIONNAIRE_ANSWER_ID)
											from MITKC_QUESTIONNAIRE_ANSWER t2 
											where t1.QUESTIONNAIRE_ANS_HEADER_ID = t2.QUESTIONNAIRE_ANS_HEADER_ID
											AND t2.QUESTION_ID IN (115,121,129,125,135)
											AND t2.ANSWER = 'No'
											);
		EXCEPTION
		WHEN OTHERS THEN
		ls_question_id := null;
		ls_question := null;
		END;
	
		OPEN CUR_GENERIC FOR
			SELECT 'N' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_B as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
	
	
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID in (120,123,128,131,138,139) 
	AND ANSWER = 'Yes';
	
	IF li_count > 0 THEN
	
		BEGIN
			SELECT T0.QUESTION_ID,T0.QUESTION
			INTO ls_question_id,ls_question
			FROM MITKC_QUESTIONNAIRE_ANSWER t1
			INNER JOIN  MITKC_QUESTIONNAIRE_QUESTION T0 on t1.QUESTION_ID = T0.QUESTION_ID
			WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr	
			AND QUESTIONNAIRE_ANSWER_ID = ( select max(t2.QUESTIONNAIRE_ANSWER_ID)
											from MITKC_QUESTIONNAIRE_ANSWER t2 
											where t1.QUESTIONNAIRE_ANS_HEADER_ID = t2.QUESTIONNAIRE_ANS_HEADER_ID
											AND t2.QUESTION_ID IN (120,123,128,131,138,139) 
											AND t2.ANSWER = 'Yes'
											);
		EXCEPTION
		WHEN OTHERS THEN
		ls_question_id := null;
		ls_question := null;
		END;
	
		OPEN CUR_GENERIC FOR
			SELECT 'N' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_B as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
	
	
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID in (116,119,122,130,132,127,135) 
	AND ANSWER = 'Yes';
	
	IF li_count > 0 THEN
	
		BEGIN
			SELECT T0.QUESTION_ID,T0.QUESTION
			INTO ls_question_id,ls_question
			FROM MITKC_QUESTIONNAIRE_ANSWER t1
			INNER JOIN  MITKC_QUESTIONNAIRE_QUESTION T0 on t1.QUESTION_ID = T0.QUESTION_ID
			WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr	
			AND QUESTIONNAIRE_ANSWER_ID = ( select max(t2.QUESTIONNAIRE_ANSWER_ID)
											from MITKC_QUESTIONNAIRE_ANSWER t2 
											where t1.QUESTIONNAIRE_ANS_HEADER_ID = t2.QUESTIONNAIRE_ANS_HEADER_ID
											AND t2.QUESTION_ID IN (116,119,122,130,132,127,135)
											AND t2.ANSWER = 'Yes'
											);
		EXCEPTION
		WHEN OTHERS THEN
		ls_question_id := null;
		ls_question := null;
		END;
	
		OPEN CUR_GENERIC FOR
			SELECT 'Y' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_C as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
		
	
		
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID in (120,123,128,131,137,138) 
	AND ANSWER = 'No';
	
	IF li_count > 0 THEN
	
		BEGIN
			SELECT T0.QUESTION_ID,T0.QUESTION
			INTO ls_question_id,ls_question
			FROM MITKC_QUESTIONNAIRE_ANSWER t1
			INNER JOIN  MITKC_QUESTIONNAIRE_QUESTION T0 on t1.QUESTION_ID = T0.QUESTION_ID
			WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr	
			AND QUESTIONNAIRE_ANSWER_ID = ( select max(t2.QUESTIONNAIRE_ANSWER_ID)
											from MITKC_QUESTIONNAIRE_ANSWER t2 
											where t1.QUESTIONNAIRE_ANS_HEADER_ID = t2.QUESTIONNAIRE_ANS_HEADER_ID
											AND t2.QUESTION_ID IN (120,123,128,131,137,138) 
											AND t2.ANSWER = 'No'
											);
		EXCEPTION
		WHEN OTHERS THEN
		ls_question_id := null;
		ls_question := null;
		END;
		
		OPEN CUR_GENERIC FOR
			SELECT 'Y' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_C as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
	
	
		
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID in (134) 
	AND ANSWER = 'Yes';
	
	IF li_count > 0 THEN
	
		SELECT T0.QUESTION_ID,T0.QUESTION
		INTO ls_question_id,ls_question
		FROM   MITKC_QUESTIONNAIRE_QUESTION T0
		WHERE T0.QUESTION_ID = 134;
	
		OPEN CUR_GENERIC FOR
			SELECT 'Y' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_D as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
	
	SELECT count(questionnaire_answer_id)
	INTO li_count
	FROM MITKC_QUESTIONNAIRE_ANSWER
	WHERE QUESTIONNAIRE_ANS_HEADER_ID = li_qnr_ans_hdr
	AND QUESTION_ID in (139) 
	AND ANSWER = 'No';
	
	IF li_count > 0 THEN
	
		SELECT T0.QUESTION_ID,T0.QUESTION
		INTO ls_question_id,ls_question
		FROM   MITKC_QUESTIONNAIRE_QUESTION T0
		WHERE T0.QUESTION_ID = 139;
	
		OPEN CUR_GENERIC FOR
			SELECT 'N' as IS_EXEMPT_GRANTED, 
			ls_exempt_msg_E as EXEMPT_MESSAGE,
			ls_question_id as QUESTION_ID,
			ls_question as QUESTION 
			FROM DUAL;
			
			return;	
	END IF;
	
	OPEN CUR_GENERIC FOR
	SELECT '' as IS_EXEMPT_GRANTED, 
			'' as EXEMPT_MESSAGE,
			'' as QUESTION_ID,
			'' as QUESTION 
	FROM DUAL; 
		

END;
/