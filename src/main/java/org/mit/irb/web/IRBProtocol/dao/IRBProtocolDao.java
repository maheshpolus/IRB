package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolCorrespondence;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolPersonRoles;
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
import org.springframework.web.multipart.MultipartFile;

public interface IRBProtocolDao {


	IRBViewProfile getIRBprotocolPersons(String protocolNumber);

	IRBViewProfile getIRBprotocolFundingSource(String protocolNumber);

	IRBViewProfile getIRBprotocolLocation(String protocolNumber);

	IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber);

	IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber);

	IRBViewProfile getMITKCPersonInfo(String avPersonId);

	ResponseEntity<byte[]> downloadAttachments(String attachmentId);

	IRBViewProfile getAttachmentsList(String protocolnumber);

	IRBViewProfile getProtocolHistotyGroupList(String protocol_number);

	ArrayList<HashMap<String, Object>> getProtocolHistotyGroupDetails(String protocolNumber);

	IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo, ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource, ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateSubject(ProtocolSubject protocolSubject, ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator, ProtocolGeneralInfo generalInfo);

	IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadIRBProtocolAttachments(Integer protocolId, String protocolNumber);

	IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol, ProtocolGeneralInfo generalInfo);

//	IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson) throws JsonParseException, JsonMappingException, IOException;

	IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons);

	IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId, Integer protocolId);

	ResponseEntity<byte[]> loadProtocolHistoryCorrespondanceLetter(Integer protocolActionId);

	IRBViewProfile loadProtocolHistoryActionComments(String protocolNumber, Integer protocolActionId, String protocolActionTypecode);

	IRBViewProfile checkingPersonsRightToViewProtocol(String personId, String protocolNumber);

	IRBProtocolVO updateUnitDetails(ProtocolLeadUnits protocolUnit, ProtocolGeneralInfo generalInfo);

	void modifyAdminContactDetail(ProtocolAdminContact protocolAdminContact, ProtocolGeneralInfo generalInfo);

	void deleteAdminContactDetail(ProtocolAdminContact protocolAdminContact, ProtocolGeneralInfo generalInfo);
	/**
	 * @param irbProtocolVO
	 * @return load all admin contacts for protocol
	 */
	public Future<IRBProtocolVO> getProtocolAdminContacts(IRBProtocolVO irbProtocolVO);

	IRBViewProfile getIRBprotocolUnits(String protocolNumber);

	IRBViewProfile getIRBprotocolAdminContact(String protocolNumber);

	ArrayList<HashMap<String, Object>> getIRBprotocolCollaboratorDetails(Integer protocolCollaboratorId, String acType);

	ResponseEntity<byte[]> downloadCollaboratorFileData(String fileDataId);

	IRBViewProfile getUserTrainingRight(String person_Id);

	Integer getNextGroupActionId(Integer protocolId, Integer nextGroupActionId, Integer actionId,String groupListProtocolNumber);
	
	String generateProtocolNumber();

	List<CollaboratorNames> loadCollaborators(String collaboratorSearchString);

	ArrayList<HashMap<String, Object>> getHistoryGroupComment(String protocolNumber, Integer actionId, Integer protocolId,
			Integer nextGroupActionId);
	
	HashMap<String, Object> getIRBProtocolDetail(String protocolNumber);

	ResponseEntity<byte[]> downloadAdminRevAttachment(String attachmentId);

	IRBProtocolVO addNewProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol);

	IRBProtocolVO editProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol);

	IRBProtocolVO deleteProtocolAttachment(IRBAttachmentProtocol attachmentProtocol);

	IRBProtocolVO replaceProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol);

	IRBProtocolVO loadInternalProtocolAttachments(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadPreviousProtocolAttachments(String documentId);

	ResponseEntity<byte[]> downloadProtocolAttachments(String attachmentId);

	ResponseEntity<byte[]> downloadInternalProtocolAttachments(String documentId);
	
	void updateProtocolPermission(IRBProtocolPersonRoles protocolRolePerson);

	List<IRBProtocolPersonRoles> loadProtocolPermissionPerson(IRBPermissionVO irbPermissionVO);

	IRBProtocolVO deleteInternalProtocolAttachment(IRBProtocolCorrespondence internalProtocolAttachment);

	IRBProtocolVO saveOrUpdateInternalProtocolAttachments(MultipartFile[] files,
			IRBProtocolCorrespondence internalProtocolAttachment);
	
	HashMap<String, Object> getIRBprotocolScienificData(String protocolNumber);
}
