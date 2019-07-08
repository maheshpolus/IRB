package org.mit.irb.web.IRBProtocol.service;

import java.util.List;
import java.util.concurrent.Future;

import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContact;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
	 * @param protocolGeneralInfo 
	 * @param IRBProtocolVO
	 * @return updated Funding Source Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource, ProtocolGeneralInfo protocolGeneralInfo);

	/**
	 * @param protocolGeneralInfo 
	 * @param IRBProtocolVO
	 * @return updated Subject Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateSubject(ProtocolSubject protocolSubject, ProtocolGeneralInfo protocolGeneralInfo);

	/**
	 * @param protocolGeneralInfo 
	 * @param IRBProtocolVO
	 * @return updated Collaborator Information with latest status change
	 * @throws Exception
	 */
	IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator, ProtocolGeneralInfo protocolGeneralInfo);



	/**
	 * @param IRBProtocolVO
	 * @return updated protocol Information with latest status change
	 */
	@Async
    Future<IRBProtocolVO> loadProtocolDetails(IRBProtocolVO irbProtocolVO);

	/**
	 * @param IRBProtocolVO
	 * @return updated protocol attachments Information with latest status change
	 */
	IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson);

	/**
	 * @param protocolNumber 
	 * @return list of attachments
	 */
	IRBProtocolVO loadIRBProtocolAttachments(Integer protocolId, String protocolNumber);



	/**
	 * @param protocolNumber 
	 * @param protocolId 
	 * @param irbProtocolVO
	 * @return modify protocol like view,create,modify 
	 */
	IRBProtocolVO modifyProtocolDetails(String protocolNumber, Integer protocolId, IRBProtocolVO irbProtocolVO);

	/**
	 * @param scienceOfProtocol
	 * @param protocolGeneralInfo 
	 * @return updated VO object
	 */
	IRBProtocolVO saveScienceOfProtocol(ScienceOfProtocol scienceOfProtocol, ProtocolGeneralInfo protocolGeneralInfo);

	/**
	 * @param files
	 * @param attachmentsObject
	 * @return updated VO object
	 */
	IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson);

	/**
	 * @param protocolGeneralInfo 
	 * @param protocolCollaboratorPersonsList
	 * @return updated VO object
	 */
	IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons);

	/**
	 * @param collaboratorId
	 * @param protocolId 
	 * @return updated VO object with collaborator persons and attachments
	 */
	IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId, Integer protocolId);

	/**
	 * download correspondence letter in history
	 */ 
	ResponseEntity<byte[]> loadProtocolHistoryCorrespondanceLetter(Integer protocolActionId);

	/**
	 * load Protocol History Action Comments
	 */ 
	IRBViewProfile loadProtocolHistoryActionComments(String protocolNumber, Integer protocolActionId, String protocolActionTypecode);

	/**
	 * check for users right to view the protocol
	 */ 
	IRBViewProfile checkingPersonsRightToViewProtocol(String personId, String protocolNumber);

	/**
	 * @param protocolLeadUnits
	 * @param generalInfo
	 * @return modify units
	 */
	IRBProtocolVO updateUnitDetails(ProtocolLeadUnits protocolLeadUnits, ProtocolGeneralInfo generalInfo);

	/**
	 * @param protocolAdminContact
	 * @param generalInfo
	 * @return update the Admin contact of protocol
	 */
	IRBProtocolVO updateAdminContact(ProtocolAdminContact protocolAdminContact, ProtocolGeneralInfo generalInfo);

	/**
	 * @param protocolNumber
	 * @return
	 */
	IRBViewProfile getIRBprotocolUnits(String protocolNumber);

	/**
	 * @param protocolNumber
	 * @return
	 */
	IRBViewProfile getIRBprotocolAdminContact(String protocolNumber);

	/**
	 * @param protocolCollaboratorId
	 * @return
	 */
	IRBViewProfile getIRBprotocolCollaboratorDetails(Integer protocolCollaboratorId);

	/**
	 * @param fileDataId
	 * @return
	 */
	ResponseEntity<byte[]> downloadCollaboratorFileData(String fileDataId);

    /**
     * @param person_Id
     * @return
     */
    IRBViewProfile getUserTrainingRight(String person_Id);

	/**
	 * @param protocolNumber
	 * @return
	 */
	IRBUtilVO getProtocolSubmissionDetails(String protocolNumber);

	/**
	 * @param collaboratorSearchString
	 * @return
	 */
	IRBViewProfile loadCollaborators(String collaboratorSearchString);


	/**
	 * @param protocolNumber
	 * @param protocolActionId
	 * @param protocolId
	 * @param nextGroupActionId
	 * @return
	 */
	IRBViewProfile loadProtocolHistoryGroupComments(String protocolNumber, Integer protocolActionId, Integer protocolId,
			Integer nextGroupActionId);


	/**
	 * @param protocolVO
	 * @return internal protocol attachments
	 */
	IRBProtocolVO loadInternalProtocolAttachments(IRBProtocolVO protocolVO);


	/**
	 * @param documentId
	 * @return list of previous versions of attachment
	 */
	IRBProtocolVO loadPreviousProtocolAttachments(String documentId);


	/**
	 * @param documentId
	 * @return download internal attachment
	 */
	ResponseEntity<byte[]> downloadInternalProtocolAttachments(String documentId);


	/**
	 * @param vo
	 * @return get protocol lvl permissions 
	 */
	IRBPermissionVO fetchProtocolPermissionDetails(IRBPermissionVO vo);


	/**
	 * @param vo
	 * @return 
	 */
	IRBPermissionVO updateProtocolPermission(IRBPermissionVO vo);


	/**
	 * @param files
	 * @param formDataJson
	 * @return
	 */
	IRBProtocolVO saveOrUpdateInternalProtocolAttachments(MultipartFile[] files, String formDataJson);
}
