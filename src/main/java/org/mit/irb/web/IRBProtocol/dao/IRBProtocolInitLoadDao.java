package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;

public interface IRBProtocolInitLoadDao {
	
	IRBProtocolVO loadAttachmentType();
	
	IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO);

	IRBProtocolVO loadCommitteeList();

	IRBProtocolVO loadCommitteeScheduleList();

	IRBProtocolVO loadCollaboratorAttachmentType();
}
