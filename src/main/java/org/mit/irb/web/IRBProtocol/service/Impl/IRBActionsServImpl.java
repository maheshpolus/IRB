package org.mit.irb.web.IRBProtocol.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.IRBReviewAttachment;
import org.mit.irb.web.IRBProtocol.pojo.IRBReviewComment;
import org.mit.irb.web.IRBProtocol.service.IRBActionsService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
		try{		 
			 ArrayList<HashMap<String, Object>> renewalModules = irbActionsDao.iterateAmendRenewalModule(vo);	
			 vo.setModuleAvailableForAmendment(renewalModules);			    
			 Future<IRBActionsVO> submissionTypeQulifier = initLoadService.getSubmissionTypeQulifier(vo);			 		
			 Future<IRBActionsVO> scheduleDates = initLoadService.getScheduleDates(vo,null);			
			 Future<IRBActionsVO> committeeList = initLoadService.getCommitteeList(vo);				
			 Future<IRBActionsVO> riskLevel = initLoadService.getRiskLevel(vo);			 	
			 Future<IRBActionsVO> expeditedApprovalCheckList = initLoadService.getExpeditedApprovalCheckList(vo);			 
			 Future<IRBActionsVO> expeditedCannedComments = initLoadService.getExpeditedCannedComments(vo);
			 vo = submissionTypeQulifier.get();	
			 vo = scheduleDates.get();
			 vo = committeeList.get();
			 vo = riskLevel.get();	
			 vo = expeditedApprovalCheckList.get();		
			 vo = expeditedCannedComments.get();		
		}catch (Exception e) {
			logger.info("Exception in getActionLookup:" + e);
		}
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
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getSubmissionLookups(SubmissionDetailVO submissionDetailvo) {
		try{
			 Future<SubmissionDetailVO> submissionTypes = initLoadService.loadSubmissionTypes(submissionDetailvo);
			 Future<SubmissionDetailVO> submissionRewiewType = initLoadService.loadsubmissionRewiewType(submissionDetailvo);
			// Future<SubmissionDetailVO> committeeList = initLoadService.loadCommitteeList(submissionDetailvo);
			 Future<SubmissionDetailVO> typeQualifierList = initLoadService.loadTypeQualifierList(submissionDetailvo);
			 Future<SubmissionDetailVO> irbAdminRewiewType = initLoadService.loadIRBAdminRewiewType(submissionDetailvo);
			 Future<SubmissionDetailVO> irbAdminList = initLoadService.loadIRBAdminList(submissionDetailvo);
			 Future<SubmissionDetailVO> committeeRewiewType = initLoadService.loadCommitteeRewiewType(submissionDetailvo);
			 Future<SubmissionDetailVO> recomendedActionType = initLoadService.loadrecomendedActionType(submissionDetailvo);
			 submissionDetailvo = submissionTypes.get();
			 submissionDetailvo = submissionRewiewType.get();
			// submissionDetailvo = committeeList.get();
			 submissionDetailvo = typeQualifierList.get();
			 submissionDetailvo = irbAdminRewiewType.get();
			 submissionDetailvo = irbAdminList.get();
			 submissionDetailvo = committeeRewiewType.get();
			 submissionDetailvo = recomendedActionType.get();
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
			Future<SubmissionDetailVO> irbAdminCheckList = initLoadService.loadIRBAdminCHeckList(submissionDetailvo);
			submissionDetailvo = irbAdminComments.get();
		    submissionDetailvo = irbAdminAttachments.get();
		    submissionDetailvo = irbAdminCheckList.get();
		    List<IRBReviewComment> irbAdminCommentAttachment = iterateCommentAndAttachment(submissionDetailvo);
		    submissionDetailvo.setIrbAdminCommentAttachment(irbAdminCommentAttachment);
		    submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getIRBAdminReviewDetails Failed"+e);
			logger.info("Exception in getIRBAdminReviewDetails:" + e);
		}
		return submissionDetailvo;
	}

	private List<IRBReviewComment> iterateCommentAndAttachment(SubmissionDetailVO submissionDetailvo) {
		List<IRBReviewComment> irbReviewComments = new ArrayList<IRBReviewComment>();
		try{
		 for(HashMap<String, Object> comment :submissionDetailvo.getIrbAdminCommentList()){
		    	IRBReviewComment irbReviewComment = new IRBReviewComment();
		    	//irbReviewComment.setAdminReviewId(Integer.parseInt(comment.get("ADMIN_REVIEWER_ID").toString()));
		    	irbReviewComment.setSubmissionId(Integer.parseInt(comment.get("SUBMISSION_ID").toString()));
		    	irbReviewComment.setSequenceNumber(Integer.parseInt(comment.get("SEQUENCE_NUMBER").toString()));
		    	irbReviewComment.setCommentDescription(comment.get("COMMENT_DESCRIPTION").toString());
		    	irbReviewComment.setProtocolNumber(comment.get("PROTOCOL_NUMBER").toString());
		    	irbReviewComment.setUdpateUser(comment.get("UPDATE_USER").toString());
		    	irbReviewComment.setFullName(comment.get("FULL_NAME").toString());
		    	irbReviewComment.setCommentId(Integer.parseInt(comment.get("COMMENT_ID").toString()));
		    	irbReviewComment.setUpdateTimestamp(comment.get("UPDATE_TIMESTAMP").toString());
		    	irbReviewComment.setActionType(comment.get("ACTION_TYPE").toString());
		    	irbReviewComment.setProtocolId(Integer.parseInt(comment.get("PROTOCOL_ID").toString()));
		    	irbReviewComment.setPublicFlag(comment.get("PUBLIC_FLAG").toString());
		    	irbReviewComment.setPersonId(comment.get("PERSON_ID").toString());
		    	irbReviewComment.setActionCode(Integer.parseInt(comment.get("ACTION_CODE").toString()));
		    	if(comment.get("ATTACH_FLG").toString().equalsIgnoreCase("Y")){
		    		List<IRBReviewAttachment> irbReviewAttachments = new ArrayList<IRBReviewAttachment>();
		    		for(HashMap<String, Object> attachment : submissionDetailvo.getIrbAdminAttachmentList()){
		    			if(irbReviewComment.getCommentId() == Integer.parseInt(attachment.get("COMMENT_ID").toString())){
		    				IRBReviewAttachment irbReviewAttachment = new IRBReviewAttachment();
		    				//irbReviewAttachment.setAdminReviewId(Integer.parseInt(attachment.get("ADMIN_REVIEWER_ID").toString()));
		    				irbReviewAttachment.setAttachmentId(Integer.parseInt(attachment.get("ATTACHMENT_ID").toString()));
		    				irbReviewAttachment.setCommentId(Integer.parseInt(attachment.get("COMMENT_ID").toString()));
		    				irbReviewAttachment.setDescription(attachment.get("DESCRIPTION").toString());
		    				irbReviewAttachment.setFullName(attachment.get("FULL_NAME").toString());
		    				irbReviewAttachment.setFileName(attachment.get("FILE_NAME").toString());
		    				irbReviewAttachment.setMimeType(attachment.get("MIME_TYPE").toString());
		    				irbReviewAttachment.setUpdateTimeStamp(attachment.get("UPDATE_TIMESTAMP").toString());
		    				irbReviewAttachment.setUpdateUser(attachment.get("UPDATE_USER").toString());
		    				irbReviewAttachment.setActionType(attachment.get("ACTION_TYPE").toString());
		    				irbReviewComment.setActionType(attachment.get("ACTION_TYPE").toString());
		    				irbReviewAttachment.setPublicFlag(attachment.get("PUBLIC_FLAG").toString());
		    				irbReviewAttachment.setPersonId(attachment.get("PERSON_ID").toString());
		    				irbReviewComment.setActionCode(Integer.parseInt(attachment.get("ACTION_CODE").toString()));
		    				irbReviewAttachments.add(irbReviewAttachment);
		    			}		   		    			
		    		}  
		    		irbReviewComment.setReviewAttachments(irbReviewAttachments);
		    	}
		    	irbReviewComments.add(irbReviewComment);
		    }
		 submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("iterateCommentAndAttachment Failed "+e);
			logger.info("Exception in iterateCommentAndAttachment:" + e);
		}
		return irbReviewComments;
	}

	@Override
	public SubmissionDetailVO getSubmissionBasicDetails(SubmissionDetailVO submissionDetailvo) {
		try{
			Future<SubmissionDetailVO> submissionDetail = initLoadService.fetchsubmissionBasicDetail(submissionDetailvo);
			Future<SubmissionDetailVO> committeeReviewers = initLoadService.fetchCommitteeReviewers(submissionDetailvo);
			submissionDetailvo = submissionDetail.get();
			if(submissionDetailvo.getSubmissionDetail() != null && !submissionDetailvo.getSubmissionDetail().isEmpty()){
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID") !=  null) {
					submissionDetailvo.setCommitteeId(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
				}				
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE") != null) {
					submissionDetailvo.setSubmissionTypeCode(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE").toString()));
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_QUAL_CODE") != null) {
					submissionDetailvo.setSubmissionQualifierCode(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_QUAL_CODE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE") != null) {
					submissionDetailvo.setSubmissionTypeCode(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE").toString()));
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE") != null) {
					submissionDetailvo.setSubmissionType(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_ID") != null) {
					submissionDetailvo.setSubmissionId(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_ID").toString()));					
				}
				if (submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE") != null) {
					submissionDetailvo.setSelectedDate(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("REVIEW_TYPE") != null) {
					submissionDetailvo.setSubmissionReviewType(submissionDetailvo.getSubmissionDetail().get("REVIEW_TYPE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("PROTOCOL_REVIEW_TYPE_CODE") != null) {
					submissionDetailvo.setReviewTypeCode(submissionDetailvo.getSubmissionDetail().get("PROTOCOL_REVIEW_TYPE_CODE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_NAME") != null) {
					submissionDetailvo.setCommitteeName(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_NAME").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID") != null) {
					submissionDetailvo.setCommitteeId(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SCHEDULE_ID") != null) {
					submissionDetailvo.setScheduleId(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_ID").toString()));
					submissionDetailvo.setSceduleId(submissionDetailvo.getScheduleId());
				}
			}			
			submissionDetailvo = committeeReviewers.get();
			Future<SubmissionDetailVO> committeeList = initLoadService.loadCommitteeList(submissionDetailvo);
			submissionDetailvo = committeeList.get();
			if(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID") == null){
				//Future<SubmissionDetailVO> committeeList = initLoadService.loadCommitteeList(submissionDetailvo);
				Future<SubmissionDetailVO> scheduleDates = initLoadService.getScheduleDates(submissionDetailvo,null);
			//	submissionDetailvo = committeeList.get();
				submissionDetailvo = scheduleDates.get();
			}else {
				if(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE") != null){
					submissionDetailvo.setSelectedDate(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE").toString());
					ArrayList<HashMap<String, Object>> scheduleDates = irbActionsDao.getScheduleDates(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
					submissionDetailvo.setScheduleDates(scheduleDates);
					ArrayList<HashMap<String, Object>> committeeMembers = irbActionsDao.fetchCommitteeMembers(submissionDetailvo);
					submissionDetailvo.setCommitteeMemberList(committeeMembers);
				}else {
					ArrayList<HashMap<String, Object>> scheduleDates = irbActionsDao.getScheduleDates(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
					submissionDetailvo.setScheduleDates(scheduleDates);
				}
			}
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getSubmissionBasicDetails Failed"+e);
			logger.info("Exception in getSubmissionBasicDetails:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getIRBAdminReviewers(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<HashMap<String, Object>> irbAdminReviewerList = irbActionsDao.fetchIRBAdminReviewers(submissionDetailvo);
			submissionDetailvo.setIrbAdminsReviewers(irbAdminReviewerList);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("getIRBAdminReviewers Failed"+e);
			logger.info("Exception in getIRBAdminReviewers:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO updateIRBAdminComment(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateIRBAdminComment(submissionDetailvo);
			submissionDetailvo = getIRBAdminReviewDetails(submissionDetailvo);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateIRBAdminComment Failed"+e);
			logger.info("Exception in updateIRBAdminComment:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO updateIRBAdminAttachments(SubmissionDetailVO submissionDetailvo, MultipartFile[] files) {
		try{
			Integer commentId = irbActionsDao.updateIRBAdminComment(submissionDetailvo);
			submissionDetailvo.setCommentId(commentId);
			irbActionsDao.updateIRBAdminAttachment(submissionDetailvo,files);
			submissionDetailvo = getIRBAdminReviewDetails(submissionDetailvo);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateIRBAdminAttachments Failed"+e);
			logger.info("Exception in updateIRBAdminAttachments:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO updateCommitteeVotingDetail(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateSubmissionDetail(submissionDetailvo);
			HashMap<String, Object> committeeVotingDetails = irbActionsDao.fetchCommitteeVotingDetails(submissionDetailvo);
			if(committeeVotingDetails != null && !committeeVotingDetails.isEmpty()){
				submissionDetailvo.setComment(committeeVotingDetails.get("COMMENTS") == null ? null : committeeVotingDetails.get("COMMENTS").toString());
				submissionDetailvo.setYesVotingCount(committeeVotingDetails.get("YES_VOTE_COUNT") == null ? null: Integer.parseInt(committeeVotingDetails.get("YES_VOTE_COUNT").toString()));
				submissionDetailvo.setNoVotingCount(committeeVotingDetails.get("NO_VOTE_COUNT") == null ? null : Integer.parseInt(committeeVotingDetails.get("NO_VOTE_COUNT").toString()));
				submissionDetailvo.setAbstainCount(committeeVotingDetails.get("ABSTAINER_COUNT") == null ? null :Integer.parseInt(committeeVotingDetails.get("ABSTAINER_COUNT").toString()));
			}
			submissionDetailvo.setCommitteeVotingDetails(committeeVotingDetails);
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateCommitteeVotingDetail Failed"+e);
			logger.info("Exception in updateCommitteeVotingDetail:" + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO updateBasicSubmissionDetail(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateSubmissionDetail(submissionDetailvo);
			submissionDetailvo = getSubmissionBasicDetails(submissionDetailvo);
			if(submissionDetailvo.getSubmissionDetail() != null && !submissionDetailvo.getSubmissionDetail().isEmpty()){
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID") !=  null) {
					submissionDetailvo.setCommitteeId(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
				}				
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE") != null) {
					submissionDetailvo.setSubmissionTypeCode(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE").toString()));
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_QUAL_CODE") != null) {
					submissionDetailvo.setSubmissionQualifierCode(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_QUAL_CODE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE") != null) {
					submissionDetailvo.setSubmissionTypeCode(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE_CODE").toString()));
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE") != null) {
					submissionDetailvo.setSubmissionType(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_TYPE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SUBMISSION_ID") != null) {
					submissionDetailvo.setSubmissionId(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SUBMISSION_ID").toString()));
				}
				if (submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE") != null) {
					submissionDetailvo.setSelectedDate(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_DATE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("REVIEW_TYPE") != null) {
					submissionDetailvo.setSubmissionReviewType(submissionDetailvo.getSubmissionDetail().get("REVIEW_TYPE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("PROTOCOL_REVIEW_TYPE_CODE") != null) {
					submissionDetailvo.setReviewTypeCode(submissionDetailvo.getSubmissionDetail().get("PROTOCOL_REVIEW_TYPE_CODE").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_NAME") != null) {
					submissionDetailvo.setCommitteeName(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_NAME").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID") != null) {
					submissionDetailvo.setCommitteeId(submissionDetailvo.getSubmissionDetail().get("COMMITTEE_ID").toString());
				}
				if (submissionDetailvo.getSubmissionDetail().get("SCHEDULE_ID") != null) {
					submissionDetailvo.setScheduleId(Integer.parseInt(submissionDetailvo.getSubmissionDetail().get("SCHEDULE_ID").toString()));
					submissionDetailvo.setSceduleId(submissionDetailvo.getScheduleId());
				}
			}	
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateBasicSubmissionDetail Failed "+e);
			logger.info("Exception in updateBasicSubmissionDetail : " + e);
		}
		return submissionDetailvo;
	}

	@Override
	public SubmissionDetailVO getCommitteeList(SubmissionDetailVO vo) {
		try{
			ArrayList<HashMap<String, Object>> committeeList = irbActionsDao.getSubmissionCommitteeList(vo);
			vo.setCommitteeList(committeeList);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("getCommitteeList Failed"+e);
			logger.info("Exception in getCommitteeList:" + e);
		}
		return vo;
	}

	@Override
	public SubmissionDetailVO updateCommitteeReviewers(SubmissionDetailVO vo) {
		try{
			irbActionsDao.updateCommitteeReviewers(vo);
			ArrayList<HashMap<String, Object>> committeeReviewers = irbActionsDao.fetchCommitteeReviewers(vo);
			vo.setCommitteeReviewers(committeeReviewers);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("getCommitteeList Failed"+e);
			logger.info("Exception in getCommitteeList:" + e);
		}
		return vo;
	}

	@Override
	public SubmissionDetailVO loadCommitteeMembers(SubmissionDetailVO vo) {
		try{
			ArrayList<HashMap<String, Object>> committeeMembers = irbActionsDao.fetchCommitteeMembers(vo);
			vo.setCommitteeMemberList(committeeMembers);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("loadCommitteeMembers Failed"+e);
			logger.info("Exception in loadCommitteeMembers:" + e);
		}
		return vo;
	}

	@Override
	public SubmissionDetailVO loadCommitteeReviewerDetails(SubmissionDetailVO vo) {
		try{
			ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment = irbActionsDao.getCommitteeReviewerCommentsandAttachment(vo);
			HashMap<String, Object> committeeVotingDetails = irbActionsDao.fetchCommitteeVotingDetails(vo);
			if(committeeVotingDetails != null && !committeeVotingDetails.isEmpty()){
				vo.setComment(committeeVotingDetails.get("COMMENTS") == null ? null : committeeVotingDetails.get("COMMENTS").toString());
				vo.setYesVotingCount(committeeVotingDetails.get("YES_VOTE_COUNT") == null ? null: Integer.parseInt(committeeVotingDetails.get("YES_VOTE_COUNT").toString()));
				vo.setNoVotingCount(committeeVotingDetails.get("NO_VOTE_COUNT") == null ? null : Integer.parseInt(committeeVotingDetails.get("NO_VOTE_COUNT").toString()));
				vo.setAbstainCount(committeeVotingDetails.get("ABSTAINER_COUNT") == null ? null :Integer.parseInt(committeeVotingDetails.get("ABSTAINER_COUNT").toString()));
			}
			vo.setCommitteeVotingDetails(committeeVotingDetails);
			vo.setCommitteeReviewerCommentsandAttachment(committeeReviewerCommentsandAttachment);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("loadCommitteeMembers Failed"+e);
			logger.info("Exception in loadCommitteeMembers:" + e);
		}
		return vo;
	}

	@Override
	public SubmissionDetailVO updateCommitteeReviewerComments(SubmissionDetailVO vo) {
		try{
			irbActionsDao.updateCommitteeReviewComments(vo);
			ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment = irbActionsDao.getCommitteeReviewerCommentsandAttachment(vo);
			vo.setCommitteeReviewerCommentsandAttachment(committeeReviewerCommentsandAttachment);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("updateCommitteeReviewerComments Failed"+e);
			logger.info("Exception in updateCommitteeReviewerComments:" + e);
		}

		return vo;
	}

	@Override
	public SubmissionDetailVO updateIRBAdminCheckList(SubmissionDetailVO submissionDetailvo) {
		try{
			irbActionsDao.updateIRBAdminCheckList(submissionDetailvo);
			Future<SubmissionDetailVO> irbAdminCheckList = initLoadService.loadIRBAdminCHeckList(submissionDetailvo);
		    submissionDetailvo = irbAdminCheckList.get();
			submissionDetailvo.setSuccessCode(true);
		} catch (Exception e) {
			submissionDetailvo.setSuccessCode(false);
			submissionDetailvo.setSuccessMessage("updateIRBAdminCheckList Failed "+e);
			logger.info("Exception in updateIRBAdminCheckList : " + e);
		}
		return submissionDetailvo;
	}
	
	@Override
	public SubmissionDetailVO updateCommitteeReviewerAttachments(SubmissionDetailVO vo, MultipartFile[] files) {
		try{
			irbActionsDao.updateCommitteeReviewerAttachments(files,vo);
			ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment = irbActionsDao.getCommitteeReviewerCommentsandAttachment(vo);
			vo.setCommitteeReviewerCommentsandAttachment(committeeReviewerCommentsandAttachment);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("updateCommitteeReviewerAttachments Failed"+e);
			logger.info("Exception in updateCommitteeReviewerAttachments:" + e);
		}
		return vo;
	}

	@Override
	public ResponseEntity<byte[]> downloadCommitteeFileData(String fileDataId) {
		ResponseEntity<byte[]> attachments = irbActionsDao.downloadCommitteeFileData(fileDataId);
		return attachments;
	}

	@Override
	public ResponseEntity<byte[]> downloadAdminRevAttachment(String attachmentId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadAdminRevAttachment(attachmentId);
		return attachments;
	}

	@Override
	public SubmissionDetailVO getPastSubmission(SubmissionDetailVO vo) {
		try{
			ArrayList<HashMap<String, Object>> pastsubmission = irbActionsDao.getPastSubmission(vo);
			vo.setPastSubmission(pastsubmission);
			vo.setSuccessCode(true);
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("getPastSubmission Failed"+e);
			logger.info("Exception in getPastSubmission:" + e);
		}
		return vo;
	}
}
