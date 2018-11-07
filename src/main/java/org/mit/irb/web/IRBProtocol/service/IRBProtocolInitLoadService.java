package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;

public interface IRBProtocolInitLoadService {
	/**
	 * @param generalInfo
	 * @return IRBProtocolVO with updated general information
	 */
	
	IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @param irbProtocolVO
	 * @return updated VO Object
	 */
	IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @param irbProtocolVO
	 * @return list of sponsors
	 */
	IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @return possible attachment types
	 */
	IRBProtocolVO loadAttachmentType();
	
	/**
	 * return all the possible protocol types. 
	 */
	IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return all the possible protocol types. 
	 */
	IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return all lead units. 
	 */
	IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return list of affiliation. 
	 */
	IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO);
	

	/**
	 * return list of subject types. 
	 */
	IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return list of subject types. 
	 */
	IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO);
}
