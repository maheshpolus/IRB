package org.mit.irb.web.IRBProtocol.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IRBProtocolDao {

	IRBViewProfile getIRBProtocolDetails(String protocolNumber);

	IRBViewProfile getIRBprotocolPersons(String protocolNumber);

	IRBViewProfile getIRBprotocolFundingSource(String protocolNumber);

	IRBViewProfile getIRBprotocolLocation(String protocolNumber);

	IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber);

	IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber);

	IRBViewProfile getMITKCPersonInfo(String avPersonId);

	ResponseEntity<byte[]> downloadAttachments(String attachmentId);

	IRBViewProfile getAttachmentsList(String protocolnumber);

	IRBViewProfile getProtocolHistotyGroupList(String protocol_number);

	IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId, Integer nextGroupActionId,
			Integer previousGroupActionId);

	IRBViewProfile getPersonExemptFormList(String personID, String personRoleType, String title, String piName,
			String determination, String facultySponsorname, String exemptStartDate, String exemptEndDate)
			throws ParseException;

	void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) throws ParseException;

	ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm);

	Integer getNextExemptId();

	IRBViewProfile getPersonExemptForm(Integer exemptFormId, String string);

	ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList();

	void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode,
			String updateUser, Integer notificationNumber, PersonDTO personDTO);

	void addExemptProtocolAttachments(MultipartFile[] files, IRBExemptForm jsonObj);

	void updateExemptprotocolAttachments(IRBExemptForm jsonObj);

	ArrayList<HashMap<String, Object>> getExemptProtocolActivityLogs(Integer exemptFormID);

	ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId);

	ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID);

	IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo);

	IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo);

	IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource);

	IRBProtocolVO updateSubject(ProtocolSubject protocolSubject);

	IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator);

	IRBProtocolVO loadAttachmentType();

	IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson)
			throws JsonParseException, JsonMappingException, IOException;

	IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber);

	IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol);

	IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO);
}
