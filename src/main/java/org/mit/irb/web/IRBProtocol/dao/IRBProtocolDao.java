package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.ServiceAttachments;

public interface IRBProtocolDao {

	IRBViewProfile getIRBProtocolDetails(String protocolNumber);

	IRBViewProfile getIRBprotocolPersons(String protocolNumber);

	IRBViewProfile getIRBprotocolFundingSource(String protocolNumber);

	IRBViewProfile getIRBprotocolLocation(String protocolNumber);

	IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber);

	IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber);

	IRBViewProfile getMITKCPersonInfo(String avPersonId);

	ServiceAttachments downloadAttachments(String attachmentId);

	IRBViewProfile getAttachmentsList(String protocolnumber);

}
