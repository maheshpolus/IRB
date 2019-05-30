package org.mit.irb.web.IRBProtocol.service;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.springframework.web.multipart.MultipartFile;

public interface IRBActionsService {

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getActionList(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files 
	 * @return
	 */
	IRBActionsVO performProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return List of modified modules
	 */
	IRBActionsVO getAmendRenwalSummary(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return update the modules and summary section
	 */
	IRBActionsVO updateAmendRenwalSummary(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getActionLookup(IRBActionsVO vo);

	/**
	 * @param committeeId
	 * @return
	 */
	IRBActionsVO getCommitteeScheduledDates(String committeeId);

	/**
	 * @param vo
	 * @return get committee List
	 */
	SubmissionDetailVO getCommitteeList(SubmissionDetailVO vo);		

	/**
		 * @param vo
		 * @return get committee List
		 */
	SubmissionDetailVO getIRBAdminList(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return save new IRB Admin
	 */
	SubmissionDetailVO updateIRBAdmin(SubmissionDetailVO vo);
}
