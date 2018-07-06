package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.http.ResponseEntity;

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

	IRBViewProfile getPersonExemptFormList(String personID, String personRoleType, String title, String piName, String determination);

	void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype);

	 ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm);
	 
	 Integer getNextExemptId();
	 
	 IRBViewProfile getPersonExemptForm(Integer exemptFormId);

	ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList();
}
