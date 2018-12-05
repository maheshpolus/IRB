package org.mit.irb.web.IRBProtocol.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
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
	 * @param IRBProtocolVO
	 * @return updated protocol personnel Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo, ProtocolGeneralInfo generalInfo);

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
	 * @param files
	 * @param attachmentsObject
	 * @return updated VO object
	 */
	IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson);

	/**
	 * @param protocolCollaboratorPersonsList
	 * @return updated VO object
	 */
	IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons);

	/**
	 * @param collaboratorId
	 * @return updated VO object with collaborator persons and attachments
	 */
	IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId);

}
