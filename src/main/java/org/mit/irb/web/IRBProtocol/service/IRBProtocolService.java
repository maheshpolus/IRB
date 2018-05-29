package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.ServiceAttachments;
import org.springframework.http.ResponseEntity;

public interface IRBProtocolService {

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

	IRBViewProfile getProtocolHistotyGroupDetails(Integer protocol_id, Integer action_id, Integer next_group_action_id,
			Integer previous_group_action_id);
}
