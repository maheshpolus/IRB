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
	
	/**
	 * @param vo
	 * @return update IRB Admin Reviewers
	 */
	SubmissionDetailVO updateIRBAdminReviewer(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return fetch all submission screen lookups
	 */
	SubmissionDetailVO getSubmissionLookups(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return fetch Members for selected scedule
	 */
	SubmissionDetailVO getCommitteeMembers(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return fetch submission history
	 */
	SubmissionDetailVO getSubmissionHistory(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return List of Comments,Attachments and Review CheckList of given submission
	 */
	SubmissionDetailVO getIRBAdminReviewDetails(SubmissionDetailVO vo);
	/**
	 * @param vo
	 * @return submission basic details and list of committee reviewers
	 */
	SubmissionDetailVO getSubmissionBasicDetails(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return
	 */
	SubmissionDetailVO getIRBAdminReviewers(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return update IRB Admin Review comment
	 */
	SubmissionDetailVO updateIRBAdminComment(SubmissionDetailVO vo);

	/**
	 * @param submissionDetailVO
	 * @param files
	 * @return save IRB Admin Review Attachments
	 */
	SubmissionDetailVO updateIRBAdminAttachments(SubmissionDetailVO submissionDetailVO, MultipartFile[] files);

	/**
	 * @param vo
	 * @return update voting details from committee
	 */
	SubmissionDetailVO updateCommitteeVotingDetail(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return update submission Details
	 */
	SubmissionDetailVO updateBasicSubmissionDetail(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return update IRB Admin checkList
	 */
	SubmissionDetailVO updateIRBAdminCheckList(SubmissionDetailVO vo);
}
