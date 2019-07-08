package org.mit.irb.web.IRBProtocol.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.ExemptFundingSource;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBExemptProtocolService {

	/**
	 * @param CommonVO
	 * @return return list of all exempt form
	 * @throws ParseException 
	 */
	IRBViewProfile getPersonExemptFormList(CommonVO vo) throws ParseException;	
	
	/**
	 * @param personDTO 
	 * @param PersonDTO
	 * @param IRBExemptForm
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	CommonVO savePersonExemptForms(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception;
	
	/**
	 * @param personDTO 
	 * @param PersonDTO
	 * @param IRBExemptForm
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	CommonVO getPersonExemptForm(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception;


	/**
	 * @param questionnaireInfobean
	 *  @param QuestionnaireDto
	 * @param personDTO	
	 * @return Updated  answers of questionnaire
	 * @throws Exception
	 */
	CommonVO saveQuestionnaire(IRBExemptForm irbExemptForm,QuestionnaireDto questionnaireDto, String questionnaireInfobean,PersonDTO personDTO) throws Exception;

	/**
	 * @param exemptForm
	 * @return The list of questions that causes to get NotExempt status
	 */
	CommonVO getEvaluateMessage(IRBExemptForm exemptForm);

	/**
	 * @return The list of lead units
	 */
	ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList();
	
	/**
	 * @param formId
	 * @param actionTypeCode
	 * @param comment
	 * @param exemptstatusCode	
	 * @param updateUser
	 * @return void used to write the logs of each exempt form 
	 */
	void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode, String updateUser, Integer notificationNumber, PersonDTO personDTO);

	/**
	 * @param files
	 * @param formDataJson
	 * Used to add checklist to the exempt form
	 */
	ArrayList<HashMap<String, Object>> addExemptProtocolAttachments(MultipartFile[] files, String formDataJson);

	/**
	 * @param exemptFormID
	 * Used to get the action log of exempt protocol
	 */
	CommonVO getExemptProtocolActivityLogs(Integer exemptFormID);

	/**
	 * @param checkListId
	 * Used to download checklist attachments
	 */
	ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId);

	/**
	 * @param exemptFormID
	 * Used to get list of attachments associated with an exempt protocol
	 */
	ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID);

	/**
	 * @param commonVO
	 * @return updated exempt protocol with latest status change
	 * @throws Exception
	 */
	CommonVO approveOrDisapproveExemptProtocols(CommonVO vo) throws Exception;
	

	/**
	 * @param exemptFundingSourceList
	 * @param updateUser
	 * @return 
	 */
	IRBProtocolVO updateExemptFundingSource(ExemptFundingSource exemptFundingSource, String updateUser);

	/**
	 * @param exemptId
	 * @return fetch protocol funding source details
	 */
	IRBProtocolVO getExemptProtocolFundingSource(String exemptId);

	/**
	 * @param homeUnitSearchString
	 * @return load all departments
	 */ 
	IRBProtocolVO loadHomeUnits(String homeUnitSearchString);
}
