package org.mit.irb.web.questionnaire.service;

import javax.servlet.http.HttpServletResponse;

import org.mit.irb.web.questionnaire.dto.QuestionnaireModuleDataBus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface QuestionnaireModuleService {

	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus getApplicableQuestionnaire(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;
	
	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : The list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus getQuestionnaireDetails(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;
	
	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus saveQuestionnaireAnswers(QuestionnaireModuleDataBus questionnaireDataBus,
			MultipartHttpServletRequest request) throws Exception;
	
	
	/**
	 * @param QuestionnaireDataBus
	 * @return Boolean : Check if the questionnaire is complete or not
	 * @throws Exception
	 */
	boolean isQuestionnaireComplete(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;

	
	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus configureQuestionnaire(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;
	
	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus editQuestionnaire(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;

	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus showAllQuestionnaire(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;
	
	/**
	 * @param QuestionnaireDataBus
	 * @return QuestionnaireDataBus : Save questionnaire and return back the data
	 * @throws Exception
	 */
	QuestionnaireModuleDataBus createQuestionnaire(QuestionnaireModuleDataBus questionnaireDataBus) throws Exception;
	
	/**
	 * @param questionnaireDataBus
	 * @param response
	 * @return download attachments
	 */
	ResponseEntity<byte[]> downloadAttachments(QuestionnaireModuleDataBus questionnaireDataBus, HttpServletResponse response);
}
