package org.mit.irb.web.IRBProtocol.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBProtocolService {

	/**
	 * @param personId
	 * @param personRoleType
	 * @return load details of IRB protocol
	 */
	IRBViewProfile getIRBProtocolDetails(String protocolNumber);

	/**
	 * return all the possible protocol types. 
	 */
	IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return all the possible protocol types. 
	 */
	IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return all lead units. 
	 */
	IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return list of affiliation. 
	 */
	IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO);
	

	/**
	 * return list of subject types. 
	 */
	IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return list of subject types. 
	 */
	IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @param generalInfo
	 * @return IRBProtocolVO with updated general information
	 */
	
	IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @param generalInfo
	 * @return IRBProtocolVO with updated general information
	 */
	
	IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo);
	
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
	 * @param IRBProtocolVO
	 * @return updated protocol personnel Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo);

	/**
	 * @param IRBProtocolVO
	 * @return updated Funding Source Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource);

	/**
	 * @param IRBProtocolVO
	 * @return updated Subject Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateSubject(ProtocolSubject protocolSubject);

	/**
	 * @param IRBProtocolVO
	 * @return updated Collaborator Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator);

	/**
	 * @return possible attachment types
	 */
	IRBProtocolVO loadAttachmentType();

	/**
	 * @param IRBProtocolVO
	 * @return updated protocol Information with latest status change
	 */
	IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO);

	/**
	 * @param IRBProtocolVO
	 * @return updated protocol attachments Information with latest status change
	 */
	IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson);

	/**
	 * @return list of attachments
	 */
	IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber);

	/**
	 * @param irbProtocolVO
	 * @return list of sponsors
	 */
	IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO);

	/**
	 * @param protocolId 
	 * @param irbProtocolVO
	 * @return modify protocol like view,create,modify 
	 */
	IRBProtocolVO modifyProtocolDetails(Integer protocolId, IRBProtocolVO irbProtocolVO);

	/**
	 * @param scienceOfProtocol
	 * @return updated VO object
	 */
	IRBProtocolVO saveScienceOfProtocol(ScienceOfProtocol scienceOfProtocol);

	/**
	 * @param irbProtocolVO
	 * @return updated VO Object
	 */
	IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO);
}
