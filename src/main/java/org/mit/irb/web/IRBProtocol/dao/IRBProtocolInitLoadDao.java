package org.mit.irb.web.IRBProtocol.dao;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;

public interface IRBProtocolInitLoadDao {

	IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadAttachmentType();
	IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO);
	IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO);
}
