package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.springframework.http.ResponseEntity;

public interface IRBProtocolService {

	/**
	 * @param personId
	 * @param personRoleType
	 * @return load deatails of IRB protocol
	 */
	IRBViewProfile getIRBProtocolDetails(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return list of persons associated with those protocol
	 */
	IRBViewProfile getIRBProtocolPersons(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return list the funding sources associated with those protocol
	 */
	IRBViewProfile getIRBProtocolFundingSource(String protocolNumber);
	
	/**
	 * @param protocolNumber
	 * @return list the location associated with those protocol
	 */
	IRBViewProfile getIRBProtocolLocation(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return list the VulnerableSubjects associated with those protocol
	 */
	IRBViewProfile getIRBProtocolVulnerableSubject(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return list the SpecialReviews associated with those protocol
	 */
	
	IRBViewProfile getIRBProtocolSpecialReview(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return list the Information of the persons associated with a protocol
	 */
	IRBViewProfile getMITKCPersonInfo(String avPersonId);
	
	/**
	 * @param attachmentId
	 * Service to download the attachments
	 */
	ResponseEntity<byte[]> downloadAttachments(String attachmentId);

	/**
	 * @param protocolNumber
	 * @return load attachments associated with a protocol
	 */
	IRBViewProfile getAttachmentsList(String protocolNumber);
	
	/**
	 * @param protocolNumber
	 * @return load history associated with a protocol
	 */
	IRBViewProfile getProtocolHistotyGroupList(String protocolNumber);

	/**
	 * @param protocolId
	 * @param actionId
	 * @param nextGroupActionId
	 * @param previousGroupActionId
	 * @return load history details associated with a protocol
	 */
	
	IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId, Integer nextGroupActionId,
			Integer previousGroupActionId);
	
	
	/**
	 * @param PersonDTO
	 * @return return list of all exempt form
	 */
	
	IRBViewProfile getPersonExemptFormList(PersonDTO personDTO);	
	
	/**
	 * @param PersonDTO
	 * @param IRBExemptForm
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	CommonVO savePersonExemptForms(IRBExemptForm irbExemptForm) throws Exception;
	
	/**
	 * @param PersonDTO
	 * @param IRBExemptForm
	 * @return the list of questionnaire questions,its condition,options and its answers
	 * @throws Exception
	 */
	CommonVO getPersonExemptForm(IRBExemptForm irbExemptForm) throws Exception;


	/**
	 * @param questionnaireInfobean
	 *  @param QuestionnaireDto
	 * @param personDTO	
	 * @return save the answers of questionnaire
	 * @throws Exception
	 */
	String saveQuestionnaire(IRBExemptForm irbExemptForm,QuestionnaireDto questionnaireDto, String questionnaireInfobean,PersonDTO personDTO) throws Exception;
}
