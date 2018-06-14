package org.mit.irb.web.questionnaire.service;

import java.util.List;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;

public interface  QuestionnaireService {
	
	
	
	/**
	 * @param requestId
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	QuestionnaireDto getQuestionnaireDetails(Integer moduleCode, String moduleItemId) throws Exception;
	
	
	/**
	 * @param requestId
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	QuestionnaireDto getQuestionnaireDetails(Integer moduleCode, String moduleItemId,Integer questionnaireAnswerHeader) throws Exception;
	
	
	/**
	 * @param questionnaireInfobean
	 * @param personDTO	
	 * @return save the answers of questionnaire
	 * @throws Exception
	 */
	Integer saveQuestionnaireAnswers(QuestionnaireDto questionnaireDto, String questionnaireInfobean,Integer moduleCode, String moduleItemKey,PersonDTO personDTO) throws Exception;
	
	/**
	 * @param jsonData
	 * @return List of selected ans
	 * @throws Exception
	 */
	List<String> parseSelectedAnswer(String jsonData) throws Exception;
	
	/**
	 * @param moduleCode
	 * @param moduleItemKey	
	 * @return boolean is questionnaire compete
	 * @throws Exception
	 */
	boolean isQuestionnaireComplete(Integer moduleCode, String moduleItemKey) throws Exception;

	
	/**
	 * @param questionnaireAnswerHeaderId		
	 * @return boolean is questionnaire compete
	 * @throws Exception
	 */
	boolean isQuestionnaireComplete(Integer questionnaireAnswerHeaderId) throws Exception;
}
