package org.mit.irb.web.IRBProtocol.servIml;

import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.ServiceAttachments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;

	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBProtocolDetails(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolPersons(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolPersons(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolFundingSource(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolFundingSource(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolLocation(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolLocation(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolVulnerableSubject(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolSpecialReview(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getMITKCPersonInfo(String avPersonId) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getMITKCPersonInfo(avPersonId);
		return irbViewProfile;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(String attachmentId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadAttachments(attachmentId);
		return attachments;
	}

	@Override
	public IRBViewProfile getAttachmentsList(String protocolnumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getAttachmentsList(protocolnumber);
		return irbViewProfile;
	}
	
	@Override
	public IRBViewProfile getProtocolHistotyGroupList(String protocol_number) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getProtocolHistotyGroupList(protocol_number);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocol_id, Integer action_id,
			Integer next_group_action_id, Integer previous_group_action_id) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getProtocolHistotyGroupDetails(protocol_id, action_id,
				next_group_action_id, previous_group_action_id);
		return irbViewProfile;
	}
}
