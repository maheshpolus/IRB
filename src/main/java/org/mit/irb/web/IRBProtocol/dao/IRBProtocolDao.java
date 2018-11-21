package org.mit.irb.web.IRBProtocol.dao;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
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

	IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo, ProtocolGeneralInfo generalInfo);

	IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource);

	IRBProtocolVO updateSubject(ProtocolSubject protocolSubject);

	IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator);

	IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson)
			throws JsonParseException, JsonMappingException, IOException;

	IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber);

	IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol);

	IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson) throws JsonParseException, JsonMappingException, IOException;

	IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons);

	IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId);
	
}
