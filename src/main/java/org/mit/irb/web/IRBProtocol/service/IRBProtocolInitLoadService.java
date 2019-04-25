package org.mit.irb.web.IRBProtocol.service;

import java.util.concurrent.Future;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.springframework.scheduling.annotation.Async;

public interface IRBProtocolInitLoadService {
	/**
	 * @param generalInfo
	 * @return IRBProtocolVO with updated general information
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO);
	
	/**
	 * @param irbProtocolVO
	 * @return updated VO Object
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO);

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
	@Async
	public Future<IRBProtocolVO> loadProtocolTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * return all the possible protocol types. 
	 */
	@Async
	public Future<IRBProtocolVO> loadRoleTypes(IRBProtocolVO irbProtocolVO);

	/**
	 * return all lead units. 
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO);

	/**
	 * return list of affiliation. 
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO);

	/**
	 * return list of subject types. 
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO);

	/**
	 * return list of subject types. 
	 */
	@Async
	public Future<IRBProtocolVO> loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO);
	
	/**
	 * load the list of committees 
	 */
	public IRBProtocolVO loadCommitteeList();

	/**
	 * load the list of  schedules 
	 */
	public IRBProtocolVO loadCommitteeScheduleList();

	/**
	 * @param irbProtocolVO
	 * @return load the list of  Department types
	 */
	public Future<IRBProtocolVO> loadProtocolUnitTypes(IRBProtocolVO irbProtocolVO);

	/**
	 * @param irbProtocolVO
	 * @return load the list of Admin Contact Types
	 */
	public Future<IRBProtocolVO> loadProtocolAdminContactType(IRBProtocolVO irbProtocolVO);
}
