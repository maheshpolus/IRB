package org.mit.irb.web.IRBProtocol.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service(value = "irbActionsService")
public class IRBActionsServImpl implements IRBActionsService {

	@Autowired
	IRBActionsDao irbActionsDao;
	
	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	IRBProtocolInitLoadService initLoadService;
	
	protected static Logger logger = Logger.getLogger(IRBActionsServImpl.class.getName());

	@Override
	public IRBActionsVO getActionList(IRBActionsVO vo) {
		 vo = irbActionsDao.getActionList(vo);
		return vo;
	}

	@Override
	public IRBActionsVO performProtocolActions(IRBActionsVO vo,MultipartFile[] files) {		
		switch (vo.getActionTypeCode()) {
		case "101":			
			vo = irbActionsDao.submitForReviewProtocolActions(vo);
			
			break;
		case "303":
		   vo = irbActionsDao.withdrawProtocolActions(vo);
			break;
		case "103":
			vo = irbActionsDao.createAmendmentProtocolActions(vo);
			break;
		case "102":
			vo = irbActionsDao.createRenewalProtocolActions(vo);
			break;
		case "992":
			if(vo.getProtocolNumber().contains("A")){
				vo.setActionTypeCode("120");
			}else if(vo.getProtocolNumber().contains("R")){
				vo.setActionTypeCode("121");
			}else{
				vo.setActionTypeCode("124");
			}
			vo = irbActionsDao.deleteProtocolAmendmentRenewalProtocolActions(vo);
			break;			
		case "116":			
			vo = irbActionsDao.notifyIRBProtocolActions(vo,files);
			break;
		case "114":
			vo = irbActionsDao.requestForDataAnalysisProtocolActions(vo,files);
			break;
		case "105":
			vo = irbActionsDao.requestForCloseProtocolActions(vo,files);
			break;
		case "108":
			vo = irbActionsDao.requestForCloseEnrollmentProtocolActions(vo,files);
			break;
		case "115":
			vo = irbActionsDao.requestForReopenEnrollmentProtocolActions(vo,files);
			break;
		case "911":
			vo = irbActionsDao.copyProtocolActions(vo);
			break;
			
			
			     //*****************************admin actions*****************************//
			
			
		case "213":								
			vo = irbActionsDao.returnToPiAdminActions(vo,files);
			break;
		case "300":
			vo = irbActionsDao.closeAdminActions(vo);
			break;
		case "304":
			vo = irbActionsDao.disapproveAdminActions(vo);
			break;
		case "209":
			vo = irbActionsDao.irbAcknowledgementAdminActions(vo,files);
			break;
		case "212":
			if(vo.getSelectedCommitteeId() != null)
			{
			vo.getProtocolSubmissionStatuses().setCommitteeId(vo.getSelectedCommitteeId());
			}
			vo = irbActionsDao.reOpenEnrollmentAdminActions(vo,files);
			break;
		case "211":
			vo = irbActionsDao.dataAnalysisOnlyAdminActions(vo);
			break;
		case "207":
			vo = irbActionsDao.closedForEnrollmentAdminActions(vo);
			break;
		case "301":
			vo = irbActionsDao.terminateAdminActions(vo,files);
			break;	
		case "302":
			vo = irbActionsDao.suspendAdminActions(vo,files);
			break;	
		/*case "109":
			vo = irbActionsDao.notifyCommiteeAdminActions(vo);
			break;*/
		case "201":
			vo = irbActionsDao.deferAdminActions(vo,files);
			break;
		case "119":
			vo = irbActionsDao.adandonAdminActions(vo,files);
			break;	
		case "200":
			if(vo.getSelectedCommitteeId() != null && vo.getSelectedScheduleId() != null)
			{
			vo.getProtocolSubmissionStatuses().setScheduleId(Integer.parseInt(vo.getSelectedScheduleId()));
			vo.getProtocolSubmissionStatuses().setCommitteeId(vo.getSelectedCommitteeId());
			}
			vo = irbActionsDao.assignToAgendaAdminActions(vo,files);
			break;		
		case "210":
			vo = irbActionsDao.reviewNotRequiredAdminActions(vo,files);
			break;
		case "204":
			vo = irbActionsDao.approvedAdminActions(vo);
			break;
		case "203":
	    	vo = irbActionsDao.SMRRAdminActions(vo);
			break;
		case "202":
			vo = irbActionsDao.SRRAdminActions(vo);
			break;
		case "205":
			if(vo.getSelectedCommitteeId() != null && vo.getSelectedScheduleId() != null)
			{
			vo.getProtocolSubmissionStatuses().setScheduleId(Integer.parseInt(vo.getSelectedScheduleId()));
			vo.getProtocolSubmissionStatuses().setCommitteeId(vo.getSelectedCommitteeId());
			}
			vo = irbActionsDao.expeditedApprovalAdminActions(vo);
			break;
		case "208":
			vo = irbActionsDao.responseApprovalAdminActions(vo);
			break;
		case "113":
			vo = irbActionsDao.administrativeCorrectionAdminActions(vo);
			break;	
		case "910":
			vo = irbActionsDao.undoLastActionAdminActions(vo);
			break;	
		}
		return vo;
	}

	@Override
	public IRBActionsVO getAmendRenwalSummary(IRBActionsVO vo) {
		IRBActionsVO irbActionsVO = new IRBActionsVO();
		try{
			ArrayList<HashMap<String, Object>> renewalModules =irbActionsDao.iterateAmendRenewalModule(vo);	
			irbActionsVO.setModuleAvailableForAmendment(renewalModules);
			irbActionsVO.setComment(irbActionsDao.getAmendRenewalSummary(vo));
			irbActionsVO.setProtocolStatus(vo.getPrevProtocolStatusCode());
			irbActionsVO.setSuccessCode(true);
		} catch (Exception e) {
			irbActionsVO.setSuccessCode(false);
			irbActionsVO.setSuccessMessage("Get AmendRenwalSummary Failed");
			logger.info("Exception in getAmendRenwalSummary:" + e);
		}
		return irbActionsVO;
	}

	@Override
	public IRBActionsVO updateAmendRenwalSummary(IRBActionsVO vo) {
		IRBActionsVO irbActionsVO = new IRBActionsVO();
		try{
 			irbActionsDao.updateAmendRenewModule(vo);
			irbActionsVO = getAmendRenwalSummary(vo);
			irbActionsVO.setSuccessCode(true);
			irbActionsVO.setSuccessMessage("Updated Amendment Renewal Summary");
		} catch (Exception e) {
			irbActionsVO.setSuccessCode(false);
			irbActionsVO.setSuccessMessage("Get AmendRenwalSummary Failed");
			logger.info("Exception in getAmendRenwalSummary:" + e);
		}
		return irbActionsVO;
	}	

	@Override
	public IRBActionsVO getActionLookup(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> renewalModules = irbActionsDao.iterateAmendRenewalModule(vo);	
		vo.setModuleAvailableForAmendment(renewalModules);
		ArrayList<HashMap<String, Object>> submissionTypeQulifier = irbActionsDao.getSubmissionTypeQulifier();
		vo.setNotifyTypeQualifier(submissionTypeQulifier);	
		ArrayList<HashMap<String, Object>> scheduleDates = irbActionsDao.getScheduleDates(null);
		vo.setScheduleDates(scheduleDates);	
		ArrayList<HashMap<String, Object>> committeeList = irbActionsDao.getCommitteeList();
		vo.setCommitteeList(committeeList);
		ArrayList<HashMap<String, Object>> riskLevel = irbActionsDao.getRiskLevel();
		vo.setRiskLevel(riskLevel);
		ArrayList<HashMap<String, Object>> expeditedApprovalCheckList = irbActionsDao.getExpeditedApprovalCheckList();
		vo.setExpeditedApprovalCheckList(expeditedApprovalCheckList);
		ArrayList<HashMap<String, Object>> expeditedCannedComments = irbActionsDao.getExpeditedCannedComments();
		vo.setExpeditedCannedComments(expeditedCannedComments);
		return vo;	
	}

	@Override
	public IRBActionsVO getCommitteeScheduledDates(String committeeId) {
		IRBActionsVO vo = new IRBActionsVO();
		ArrayList<HashMap<String, Object>> scheduleDates = irbActionsDao.getScheduleDates(committeeId);
		vo.setScheduleDates(scheduleDates);	
		return vo;	
	}

	@Override
	public SubmissionDetailVO getCommitteeList(SubmissionDetailVO submissionDetailvo) {
		SubmissionDetailVO subDetailvo = new SubmissionDetailVO();
		try{
			ArrayList<HashMap<String, Object>> committeeList = irbActionsDao.getCommitteeList();
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getCommitteeList Failed"+e);
			logger.info("Exception in getAmendRenwalSummary:" + e);
		}
		return null;
	}
	
	@Override
	public SubmissionDetailVO getIRBAdminList(SubmissionDetailVO submissionDetailvo) {
		SubmissionDetailVO subDetailvo = new SubmissionDetailVO();
		try{
			ArrayList<HashMap<String, Object>> adminList = irbActionsDao.getIRBAdminList();
			subDetailvo.setIrbAdminsList(adminList);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getIRBAdminList Failed"+e);
			logger.info("Exception in getIRBAdminList:" + e);
		}
		return subDetailvo;
	}
	
	@Override
	public SubmissionDetailVO updateIRBAdmin(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateIRBAdmin(submissionDetailvo);
			HashMap<String, Object> irbProtocolDetail = irbProtocolDao.getIRBProtocolDetail(submissionDetailvo.getProtocolNumber());
			submissionDetailvo.setIrbViewHeader(irbProtocolDetail);
			submissionDetailvo.setSuccessCode(true);
			submissionDetailvo.setSuccessMessage("IRB Admin Assigned Successfully");
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getIRBAdminList Failed"+e);
			logger.info("Exception in updateIRBAdmin:" + e);
		}
		return submissionDetailvo;
	}
	
	@Override
	public SubmissionDetailVO updateIRBAdminReviewer(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateIRBAdminReviewers(submissionDetailvo);
			ArrayList<HashMap<String, Object>> irbAdminReviewerList = irbActionsDao.fetchIRBAdminReviewers(submissionDetailvo);
			submissionDetailvo.setIrbAdminsReviewers(irbAdminReviewerList);
			submissionDetailvo.setSuccessCode(true);
			submissionDetailvo.setSuccessMessage("IRB Reviewer Assigned Successfully");
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateIRBAdminReviewer Failed"+e);
			logger.info("Exception in updateIRBAdminReviewer:" + e);
		}
		return null;
	}

	@Override
	public SubmissionDetailVO getSubmissionLookups(SubmissionDetailVO submissionDetailvo) {
		try{
			 Future<SubmissionDetailVO> submissionTypes = initLoadService.loadSubmissionTypes(submissionDetailvo);
			 Future<SubmissionDetailVO> submissionRewiewType = initLoadService.loadsubmissionRewiewType(submissionDetailvo);
			 Future<SubmissionDetailVO> committeeList = initLoadService.loadCommitteeList(submissionDetailvo);
			 Future<SubmissionDetailVO> typeQualifierList = initLoadService.loadTypeQualifierList(submissionDetailvo);
			 Future<SubmissionDetailVO> irbAdminRewiewType = initLoadService.loadIRBAdminRewiewType(submissionDetailvo);
			 Future<SubmissionDetailVO> irbAdminList = initLoadService.loadIRBAdminList(submissionDetailvo);
			 submissionDetailvo = submissionTypes.get();
			 submissionDetailvo = submissionRewiewType.get();
			 submissionDetailvo = committeeList.get();
			 submissionDetailvo = typeQualifierList.get();
			 submissionDetailvo = irbAdminRewiewType.get();
			 submissionDetailvo = irbAdminList.get();
			 submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getSubmissionLookups Failed"+e);
			logger.info("Exception in getSubmissionLookups:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getCommitteeMembers(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<HashMap<String, Object>> committeeMembers = irbActionsDao.fetchCommitteeMembers(submissionDetailvo);
			submissionDetailvo.setCommitteeMemberList(committeeMembers);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getCommitteeMembers Failed"+e);
			logger.info("Exception in getCommitteeMembers:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getSubmissionHistory(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<HashMap<String, Object>> submissionHistory = irbActionsDao.fetchSubmissionHistory(submissionDetailvo);
			submissionDetailvo.setSubmissionHistory(submissionHistory);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getSubmissionHistory Failed"+e);
			logger.info("Exception in getSubmissionHistory:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getIRBAdminReviewDetails(SubmissionDetailVO submissionDetailvo) {
		try{
			Future<SubmissionDetailVO> irbAdminComments = initLoadService.loadIRBAdminComments(submissionDetailvo);
			Future<SubmissionDetailVO> irbAdminAttachments = initLoadService.loadIRBAdminAttachments(submissionDetailvo);
			submissionDetailvo = irbAdminComments.get();
		    submissionDetailvo = irbAdminAttachments.get();
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getSubmissionHistory Failed"+e);
			logger.info("Exception in getSubmissionHistory:" + e);
		}
		return submissionDetailvo;
	}
}
