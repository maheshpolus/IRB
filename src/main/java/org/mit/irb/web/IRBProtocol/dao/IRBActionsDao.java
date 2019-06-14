package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBActionsDao {

	IRBActionsVO getActionList(IRBActionsVO vo);

	IRBActionsVO submitForReviewProtocolActions(IRBActionsVO vo);

	IRBActionsVO withdrawProtocolActions(IRBActionsVO vo);

	IRBActionsVO createAmendmentProtocolActions(IRBActionsVO vo);

	IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo);

	IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo);

	IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO requestForCloseProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO requestForCloseEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO requestForReopenEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO copyProtocolActions(IRBActionsVO vo);
	
	List<HashMap<String, Object>> getAmendRenewalModules(String protoocolNumber);
	
	void updateActionStatus(IRBActionsVO vo);

	ArrayList<HashMap<String, Object>> iterateAmendRenewalModule(IRBActionsVO vo);

	String getAmendRenewalSummary(IRBActionsVO vo);

	IRBActionsVO updateAmendRenewModule(IRBActionsVO vo);

	IRBActionsVO returnToPiAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO closeAdminActions(IRBActionsVO vo);

	IRBActionsVO disapproveAdminActions(IRBActionsVO vo);

	IRBActionsVO irbAcknowledgementAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO reOpenEnrollmentAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO dataAnalysisOnlyAdminActions(IRBActionsVO vo);

	IRBActionsVO closedForEnrollmentAdminActions(IRBActionsVO vo);
	
	IRBActionsVO terminateAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO suspendAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO notifyCommiteeAdminActions(IRBActionsVO vo);

	IRBActionsVO deferAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO assignToAgendaAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO grantExceptionAdminActions(IRBActionsVO vo);

	IRBActionsVO reviewNotRequiredAdminActions(IRBActionsVO vo, MultipartFile[] files);	

	IRBActionsVO approvedAdminActions(IRBActionsVO vo);

	IRBActionsVO SMRRAdminActions(IRBActionsVO vo);

	IRBActionsVO SRRAdminActions(IRBActionsVO vo);

	IRBActionsVO expeditedApprovalAdminActions(IRBActionsVO vo);

	IRBActionsVO responseApprovalAdminActions(IRBActionsVO vo);

	IRBActionsVO administrativeCorrectionAdminActions(IRBActionsVO vo);

	ArrayList<HashMap<String, Object>> getScheduleDates(String committeeId);
	
	ArrayList<HashMap<String, Object>> getCommitteeList();

	IRBActionsVO adandonAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO undoLastActionAdminActions(IRBActionsVO vo);

	ArrayList<HashMap<String, Object>> getIRBAdminList();

	void updateIRBAdmin(SubmissionDetailVO submissionDetailvo);
	
	void updateIRBAdminReviewers(SubmissionDetailVO submissionDetailvo);

	ArrayList<HashMap<String, Object>> fetchIRBAdminReviewers(SubmissionDetailVO submissionDetailvo);

	ArrayList<HashMap<String, Object>> fetchCommitteeMembers(SubmissionDetailVO submissionDetailvo);

	ArrayList<HashMap<String, Object>> fetchSubmissionHistory(SubmissionDetailVO submissionDetailvo);

	Integer updateIRBAdminComment(SubmissionDetailVO submissionDetailvo);

	void updateIRBAdminAttachment(SubmissionDetailVO submissionDetailvo, MultipartFile[] files);

	HashMap<String, Object> fetchCommitteeVotingDetails(Integer submissionId);

	void updateSubmissionDetail(SubmissionDetailVO submissionDetailvo);

	void updateCommitteeReviewComments(SubmissionDetailVO submissionDetailVO);

	void updateCommitteeReviewerAttachments(MultipartFile[] files, SubmissionDetailVO submissionDetailVO);

	void updateIRBAdminCheckList(SubmissionDetailVO submissionDetailvo);
	
	ArrayList<HashMap<String, Object>> getSubmissionCommitteeList(SubmissionDetailVO vo);

	void updateCommitteeReviewers(SubmissionDetailVO vo);
	
	ArrayList<HashMap<String, Object>> getCommitteeReviewerCommentsandAttachment(SubmissionDetailVO submissionDetailVO);

	ArrayList<HashMap<String, Object>> fetchCommitteeReviewers(SubmissionDetailVO vo);

	ResponseEntity<byte[]> downloadCommitteeFileData(String fileDataId);

	ArrayList<HashMap<String, Object>> getPastSubmission(SubmissionDetailVO vo);
}
