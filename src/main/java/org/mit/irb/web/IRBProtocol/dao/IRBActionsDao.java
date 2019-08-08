package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBActionsDao {

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getActionList(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO submitForReviewProtocolActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files 
	 * @return
	 */
	IRBActionsVO withdrawProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO createAmendmentProtocolActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO requestForCloseProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO requestForCloseEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO requestForReopenEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO copyProtocolActions(IRBActionsVO vo);
	
	/**
	 * @param protoocolNumber
	 * @return
	 */
	List<HashMap<String, Object>> getAmendRenewalModules(String protoocolNumber);
	
	/**
	 * @param vo
	 */
	void updateActionStatus(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> iterateAmendRenewalModule(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	String getAmendRenewalSummary(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO updateAmendRenewModule(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO returnToPiAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO closeAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO disapproveAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO irbAcknowledgementAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO reOpenEnrollmentAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO dataAnalysisOnlyAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO closedForEnrollmentAdminActions(IRBActionsVO vo);
	
	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO terminateAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO suspendAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO notifyCommiteeAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO deferAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO assignToAgendaAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO grantExceptionAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO reviewNotRequiredAdminActions(IRBActionsVO vo, MultipartFile[] files);	

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO approvedAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO SMRRAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO SRRAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO expeditedApprovalAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO responseApprovalAdminActions(IRBActionsVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO administrativeCorrectionAdminActions(IRBActionsVO vo);

	/**
	 * @param committeeId
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getScheduleDates(String committeeId);
	
	/**
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getCommitteeList();

	/**
	 * @param vo
	 * @param files
	 * @return
	 */
	IRBActionsVO adandonAdminActions(IRBActionsVO vo, MultipartFile[] files);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO undoLastActionAdminActions(IRBActionsVO vo);

	/**
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getIRBAdminList();

	/**
	 * @param submissionDetailvo
	 */
	void updateIRBAdmin(SubmissionDetailVO submissionDetailvo);
	
	/**
	 * @param submissionDetailvo
	 */
	void updateIRBAdminReviewers(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailvo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> fetchIRBAdminReviewers(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailvo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> fetchCommitteeMembers(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailvo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> fetchSubmissionHistory(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailvo
	 * @return
	 */
	Integer updateIRBAdminComment(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailvo
	 * @param files
	 */
	void updateIRBAdminAttachment(SubmissionDetailVO submissionDetailvo, MultipartFile[] files);

	/**
	 * @param submissionId
	 * @return
	 */
	HashMap<String, Object> fetchCommitteeVotingDetails(Integer submissionId);

	/**
	 * @param submissionDetailvo
	 */
	void updateSubmissionDetail(SubmissionDetailVO submissionDetailvo);

	/**
	 * @param submissionDetailVO
	 */
	void updateCommitteeReviewComments(SubmissionDetailVO submissionDetailVO);

	/**
	 * @param files
	 * @param submissionDetailVO
	 */
	void updateCommitteeReviewerAttachments(MultipartFile[] files, SubmissionDetailVO submissionDetailVO);

	/**
	 * @param submissionDetailvo
	 */
	void updateIRBAdminCheckList(SubmissionDetailVO submissionDetailvo);
	
	/**
	 * @param vo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getSubmissionCommitteeList(SubmissionDetailVO vo);

	/**
	 * @param vo
	 */
	void updateCommitteeReviewers(SubmissionDetailVO vo);
	
	/**
	 * @param submissionDetailVO
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getCommitteeReviewerCommentsandAttachment(SubmissionDetailVO submissionDetailVO);

	/**
	 * @param vo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> fetchCommitteeReviewers(SubmissionDetailVO vo);

	/**
	 * @param fileDataId
	 * @return
	 */
	ResponseEntity<byte[]> downloadCommitteeFileData(String fileDataId);

	/**
	 * @param vo
	 * @return
	 */
	ArrayList<HashMap<String, Object>> getPastSubmission(SubmissionDetailVO vo);

	/**
	 * @param vo
	 * @return
	 */
	IRBActionsVO getProtocolCurrentStatus(IRBActionsVO vo);
}
