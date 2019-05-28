package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
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

	IRBActionsVO irbAcknowledgementAdminActions(IRBActionsVO vo);

	IRBActionsVO reOpenEnrollmentAdminActions(IRBActionsVO vo, MultipartFile[] files);

	IRBActionsVO dataAnalysisOnlyAdminActions(IRBActionsVO vo);

	IRBActionsVO closedForEnrollmentAdminActions(IRBActionsVO vo);
	
	IRBActionsVO terminateAdminActions(IRBActionsVO vo);

	IRBActionsVO suspendAdminActions(IRBActionsVO vo);

	IRBActionsVO notifyCommiteeAdminActions(IRBActionsVO vo);

	IRBActionsVO deferAdminActions(IRBActionsVO vo);

	IRBActionsVO assignToAgendaAdminActions(IRBActionsVO vo);

	IRBActionsVO grantExceptionAdminActions(IRBActionsVO vo);

	IRBActionsVO reviewNotRequiredAdminActions(IRBActionsVO vo);

	ArrayList<HashMap<String, Object>> getSubmissionTypeQulifier();

	IRBActionsVO approvedAdminActions(IRBActionsVO vo);

	IRBActionsVO SMRRAdminActions(IRBActionsVO vo);

	IRBActionsVO SRRAdminActions(IRBActionsVO vo);

	IRBActionsVO expeditedApprovalAdminActions(IRBActionsVO vo);

	IRBActionsVO responseApprovalAdminActions(IRBActionsVO vo);

	IRBActionsVO administrativeCorrectionAdminActions(IRBActionsVO vo);

	ArrayList<HashMap<String, Object>> getScheduleDates(String committeeId);
}
