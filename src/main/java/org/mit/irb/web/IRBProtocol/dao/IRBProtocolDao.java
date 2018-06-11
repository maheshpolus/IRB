package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.ServiceAttachments;
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

	IRBViewProfile getPersonExemptFormList(PersonDTO personDTO);

	void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype);
}
