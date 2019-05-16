package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "irbActionsDao")
public class IRBActionsDaoImpl implements IRBActionsDao {
    DBEngine dbEngine;
	
	IRBActionsDaoImpl() {
		dbEngine = new DBEngine();
	}

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	Logger logger = Logger.getLogger(IRBUtilDaoImpl.class.getName());

	@Override
	public IRBActionsVO getPersonRight(IRBActionsVO vo) {		
		ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		ArrayList<HashMap<String, Object>> permissionList = null;
		ArrayList<HashMap<String, Object>> result = null;	
		ArrayList<HashMap<String, Object>> finalResult =  new ArrayList<HashMap<String, Object>>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,String.valueOf(vo.getPersonID())));				
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));		
		try {
			permissionList = dbEngine.executeProcedure(inputParam,"GET_IRB_PERSON_PERMISSION", outputParam);
			if(permissionList !=null && !permissionList.isEmpty()){			
				for(HashMap<String,Object> permName : permissionList){		
					inputParam=new ArrayList<InParameter>();			
					inputParam.add(new InParameter("AV_PERMISSION_NAME", DBEngineConstants.TYPE_STRING,permName.get("PERM_NM").toString()));				
					inputParam.add(new InParameter("AV_PROTOCOL_STATUS", DBEngineConstants.TYPE_STRING,vo.getProtocolStatus()));				
					inputParam.add(new InParameter("AV_PROTOCOL_SUBMISSION_STATUS", DBEngineConstants.TYPE_STRING,vo.getSubmissionStatus()));				
					result = dbEngine.executeProcedure(inputParam,"GET_IRB_AVAILABLE_ACTION", outputParam);
					if(result != null && !result.isEmpty())
					finalResult.add(result.get(0));									
				}						
			}
			
			for(int i=0;i<finalResult.size();i++)
	        {
	            Object temp = finalResult.get(i).get("ACTION_NAME");
	            for(int k=i+1;k<finalResult.size();k++)
	            {
	                if(temp.equals(finalResult.get(k).get("ACTION_NAME")))
	                {
	                	finalResult.remove(k); 
	                } 
	            }

	        }
			vo=getProtocolActionDetails(finalResult, vo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getPersonRight:" + e);
		}
		return vo;
	}

	public IRBActionsVO getProtocolActionDetails(ArrayList<HashMap<String, Object>> finalResult,IRBActionsVO vo){
		ArrayList<HashMap<String, Object>> results = null;
		ArrayList<HashMap<String, Object>> renewalModules = null;
		ProtocolSubmissionStatuses protocolSubmissionStatuses=new ProtocolSubmissionStatuses();
		try{
			for(int i=0;i<finalResult.size();i++){		
			ArrayList<InParameter> inputparams  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParams = new ArrayList<OutParameter>();
			Integer submissionId = null;
				if(vo.getSubmissionId()!=null)
					submissionId=Integer.parseInt(vo.getSubmissionId());
				inputparams.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
				inputparams.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
				inputparams.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionId));	
				outputParams.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
				results=dbEngine.executeProcedure(inputparams,"GET_IRB_PROTO_ACTION_SUBMISSN",outputParams);
				if(results != null && !results.isEmpty()){
					if (results.get(0).get("SUBMISSION_ID") != null) {
						protocolSubmissionStatuses.setSubmission_Id(Integer.parseInt(results.get(0).get("SUBMISSION_ID").toString()));
					}
					if (results.get(0).get("PROTOCOL_NUMBER") != null) {
						protocolSubmissionStatuses.setProtocolNumber(results.get(0).get("PROTOCOL_NUMBER").toString());
					}
					if (results.get(0).get("SEQUENCE_NUMBER") != null) {
						protocolSubmissionStatuses.setSequenceNumber(Integer.parseInt(results.get(0).get("SEQUENCE_NUMBER").toString()));
					}
					if (results.get(0).get("SUBMISSION_NUMBER") != null) {
						protocolSubmissionStatuses.setSubmissionNumber(Integer.parseInt(results.get(0).get("SUBMISSION_NUMBER").toString()));
					}
					if (results.get(0).get("COMMITTEE_ID") != null) {
						protocolSubmissionStatuses.setCommitteeId(results.get(0).get("COMMITTEE_ID").toString());
					}
					if (results.get(0).get("PROTOCOL_ID") != null) {
						protocolSubmissionStatuses.setProtocolId(Integer.parseInt(results.get(0).get("PROTOCOL_ID").toString()));
					}
					if (results.get(0).get("SUBMISSION_TYPE_CODE") != null) {
						protocolSubmissionStatuses.setSubmissionTypeCode(results.get(0).get("SUBMISSION_TYPE_CODE").toString());
					}
					if (results.get(0).get("SUBMISSION_TYPE_QUAL_CODE") != null) {
						protocolSubmissionStatuses.setSubmissionTypeQualCode(results.get(0).get("SUBMISSION_TYPE_QUAL_CODE").toString());
					}
					if (results.get(0).get("SUBMISSION_STATUS_CODE") != null) {
						protocolSubmissionStatuses.setSubmissionStatusCode(results.get(0).get("SUBMISSION_STATUS_CODE").toString());
					}
					if (results.get(0).get("PROTOCOL_REVIEW_TYPE_CODE") != null) {
						protocolSubmissionStatuses.setProtocolReviewTypeCode(results.get(0).get("PROTOCOL_REVIEW_TYPE_CODE").toString());
					}			
					/*if (results.get(0).get("SUBMISSION_DATE") != null) {
						protocolSubmissionStatuses.setSubmissionDate((Date) results.get(0).get("SUBMISSION_DATE"));
					}*/
					if (results.get(0).get("COMMENTS") != null) {
						protocolSubmissionStatuses.setComments(results.get(0).get("COMMENTS").toString());
					}
					if (results.get(0).get("YES_VOTE_COUNT") != null) {
						protocolSubmissionStatuses.setYesVoteCount(Integer.parseInt(results.get(0).get("YES_VOTE_COUNT").toString()));
					}
					if (results.get(0).get("NO_VOTE_COUNT") != null) {
						protocolSubmissionStatuses.setNoVoteCount(Integer.parseInt(results.get(0).get("NO_VOTE_COUNT").toString()));
					}
					if (results.get(0).get("ABSTAINER_COUNT") != null) {
						protocolSubmissionStatuses.setAbstainerCount(Integer.parseInt(results.get(0).get("ABSTAINER_COUNT").toString()));
					}
					if (results.get(0).get("VOTING_COMMENTS") != null) {
						protocolSubmissionStatuses.setVotingComments(results.get(0).get("VOTING_COMMENTS").toString());
					}
					/*if (results.get(0).get("UPDATE_TIMESTAMP") != null) {
						protocolSubmissionStatuses.setUpdateTimeStamp((Date) results.get(0).get("UPDATE_TIMESTAMP"));
					}*/
					if (results.get(0).get("UPDATE_USER") != null) {
						protocolSubmissionStatuses.setUpdateUser(results.get(0).get("UPDATE_USER").toString());
					}
					if (results.get(0).get("RECUSED_COUNT") != null) {
						protocolSubmissionStatuses.setRecusedCount(Integer.parseInt(results.get(0).get("RECUSED_COUNT").toString()));
					}
					if (results.get(0).get("IS_BILLABLE") != null) {
						protocolSubmissionStatuses.setIsBillable(results.get(0).get("IS_BILLABLE").toString());
					}
					if (results.get(0).get("COMM_DECISION_MOTION_TYPE_CODE") != null) {
						protocolSubmissionStatuses.setCommDecisionMotionTypeCode(results.get(0).get("COMM_DECISION_MOTION_TYPE_CODE").toString());
					}
					if (results.get(0).get("SCHEDULE_ID") != null) {
						protocolSubmissionStatuses.setScheduleId(Integer.parseInt(results.get(0).get("SCHEDULE_ID").toString()));
					}										
				}
				else{
					protocolSubmissionStatuses.setProtocolNumber(vo.getProtocolNumber());
					protocolSubmissionStatuses.setSequenceNumber(vo.getSequenceNumber());
					protocolSubmissionStatuses.setProtocolId(vo.getProtocolId());
				}
				 if(finalResult.get(i).get("ACTION_CODE").toString().equals("103")){
					 renewalModules = iterateAmendRenewalModule(vo,renewalModules);
					 vo.setModuleAvailableForAmendment(renewalModules);
					}else if(finalResult.get(i).get("ACTION_CODE").toString().equals("116")){						
						ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
						outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
						renewalModules=dbEngine.executeProcedure("GET_IRB_SUB_TYPE_QUALIFIER",outputparam);
						vo.setNotifyTypeQualifier(renewalModules);
					}							
				vo.setProtocolSubmissionStatuses(protocolSubmissionStatuses);
				vo.setPersonActionsList(finalResult);					
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getProtocolSubmissionDetails:" + e);		
		}
		return vo;			
	}

	@Override
	public IRBActionsVO submitForReviewProtocolActions(IRBActionsVO vo) {	
		ArrayList<HashMap<String, Object>> result = null;
		try {			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
		inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
		inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));	
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
		inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
		inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	
		inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	
		inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));	
		inputParam.add(new InParameter("AV_NEXT_SUBMISN_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_SUBMS_NEXT_STATUS_CODE")));
		inputParam.add(new InParameter("AV_REVIEW_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolReviewTypeCode()));	
		inputParam.add(new InParameter("AV_SUBMISSION_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getComments()));	
		inputParam.add(new InParameter("AV_YES_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getYesVoteCount()));	
		inputParam.add(new InParameter("AV_NO_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getNoVoteCount()));	
		inputParam.add(new InParameter("AV_ABSTAINER_COUNT", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getAbstainerCount()));	
		inputParam.add(new InParameter("AV_VOTING_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getVotingComments()));	
		inputParam.add(new InParameter("AV_RECUSED_COUNT", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getRecusedCount()));	
		inputParam.add(new InParameter("AV_IS_BILLABLE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getIsBillable()));	
		inputParam.add(new InParameter("AV_COMM_DECISION_MOTION_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommDecisionMotionTypeCode()));	
		inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));	
		inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));			
		inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,vo.getAcType()));	
		inputParam.add(new InParameter("AV_NEXT_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_NEXT_STATUS_CODE"))); 
		result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTOCOL_SUBMISSION",outputParam);
		vo.getProtocolSubmissionStatuses().setSubmission_Id(Integer.parseInt(result.get(0).get("SUBMISSION_ID").toString()));
		vo.getProtocolSubmissionStatuses().setSubmissionNumber(Integer.parseInt(result.get(0).get("SUBMISSION_NUMBER").toString()));
	    updateActionStatus(vo);
	} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);		
			}
		
		return vo;
	}

	@Override
	public void updateActionStatus(IRBActionsVO vo){
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));	
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));				
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));				
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSequenceNumber()));				
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));				
			inputParam.add(new InParameter("AV_USER_COMMENT", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getComments()));				
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));				
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevSubmissonStatusCode()));				
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolStatus()));				
			inputParam.add(new InParameter("AV_FOLLOWUP_ACTION_CODE", DBEngineConstants.TYPE_STRING,vo.getFollowUpActionCode()));				
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));				
			inputParam.add(new InParameter("AV_CREATE_USER", DBEngineConstants.TYPE_STRING,vo.getCreateUser()));				
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));				
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,vo.getAcType()));
			inputParam.add(new InParameter("AV_PROTOCOL_ACTION_ID", DBEngineConstants.TYPE_STRING,null));
			inputParam.add(new InParameter("AV_SYSTEM_COMMENT", DBEngineConstants.TYPE_STRING,null));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
		    dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTOCOL_LOG_ACTION");
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Submitted Successfully");
		}catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Submission Failed");
			e.printStackTrace();
			logger.info("Exception in submitForReviewProtocolActions:" + e);
		}		
	}
	
	@Override
	public IRBActionsVO withdrawProtocolActions(IRBActionsVO vo) {			
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Withdrawn successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Withdrawn Failed");
			e.printStackTrace();
			logger.info("Exception in withdrawProtocolActions:" + e);	
		}
		return vo;
	}	

	@Override
	public IRBActionsVO createAmendmentProtocolActions(IRBActionsVO vo) {	
		ArrayList<HashMap<String, Object>> result = null;
		try {		
			result=protocolActionSP(vo,null);
		    vo.setProtocolNumber(result.get(0).get("PROTOCOL_NUMBER").toString());
		    vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
		    updateAmendRenewModule(vo);
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Amendment created successfully");	
		} catch (Exception e) {																																																																																										
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Amendment creation Failed");
			e.printStackTrace();
			logger.info("Exception in createAmendmentProtocolActions:" + e);	
		}
		return vo;
	}

	/*private IRBActionsVO updateAmendRenewModule(IRBActionsVO vo) {
		try{
		for(HashMap<String,Object> modules:vo.getModuleAvailableForAmendment()){
			if(modules.get("AC_TYPE") != null && modules.get("AC_TYPE").toString().equals("U")){
				Integer protoAmendRenewalId = modules.get("PROTO_AMEND_RENEWAL_ID") != null ?Integer.parseInt(modules.get("PROTO_AMEND_RENEWAL_ID").toString()):null;
				ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
				inputParam.add(new InParameter("AV_PROTO_AMEND_REN_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_AMND_REN_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
				inputParam.add(new InParameter("AV_PARENT_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber().substring(0, 10)));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_PROTO_AMEND_RENEWAL_ID", DBEngineConstants.TYPE_INTEGER,protoAmendRenewalId));
				inputParam.add(new InParameter("AV_SUMMARY ", DBEngineConstants.TYPE_STRING,vo.getComment()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
				inputParam.add(new InParameter("AV_PROTO_MODULE_CODE", DBEngineConstants.TYPE_STRING,modules.get("PROTOCOL_MODULE_CODE")));
				inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,protoAmendRenewalId == null ? "I":"D"));
				dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_AMND_RNEW_MODULE");	
				}	
			}
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Amendment creation Failed");
			logger.info("Exception in updateAmendRenewModule:" + e);	
		}	  
		return vo;
	}*/

	@Override
	public IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result=protocolActionSP(vo,null);	
			vo.setProtocolNumber(result.get(0).get("PROTOCOL_NUMBER").toString());
			vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Renewal created successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Renewal creation Failed");
			e.printStackTrace();
			logger.info("Exception in createRenewalProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo) {	
		vo=deleteProtocolActions(vo);
		return vo;
		
	}
	
	public IRBActionsVO deleteProtocolActions(IRBActionsVO vo){
		try {
			protocolActionSP(vo,null);		
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Deleted successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Deletion Failed");
			e.printStackTrace();
			logger.info("Exception in deleteProtocolActions:" + e);	
		}
		return vo;
	}
	
	@Override
	public IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo, MultipartFile[] files) {	
		try {
	        protocolActionSP(vo,null);	
	        protocolActionAttachments(files,vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("NotifyIRB successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("NotifyIRB Failed");
			e.printStackTrace();
			logger.info("Exception in notifyIRBProtocolActions:" + e);	
		}
		return vo;
	}
	
	@Override
	public IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo, MultipartFile[] files) {	
		try {			
			protocolActionSP(vo,null);		
			protocolActionAttachments(files,vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Request for Data Analysis successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Request for Data Analysis Failed");
			e.printStackTrace();
			logger.info("Exception in requestForDataAnalysisProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO requestForCloseProtocolActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			protocolActionSP(vo, null);
			protocolActionAttachments(files,vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Request for Close successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Request for Close Failed");
			e.printStackTrace();
			logger.info("Exception in requestForCloseProtocolActions:" + e);	
		}
		return vo;	
	}

	@Override
	public IRBActionsVO requestForCloseEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			protocolActionSP(vo, null);
			protocolActionAttachments(files,vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Request for Close Enrollment successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Request for Close Enrollment Failed");
			e.printStackTrace();
			logger.info("Exception in requestForCloseEnrollmentProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO requestForReopenEnrollmentProtocolActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			protocolActionSP(vo, null);
			protocolActionAttachments(files,vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Request for Re-Open Enrollment successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Request for Re-Open Enrollment Failed");
			e.printStackTrace();
			logger.info("Exception in requestForReopenEnrollmentProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO copyProtocolActions(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			String newProtocolNumber = irbProtocolDao.generateProtocolNumber();
			result=protocolActionSP(vo,newProtocolNumber);			
			vo.setProtocolNumber(result.get(0).get("PROTOCOL_NUMBER").toString());
			vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Copy successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Copy Failed");
			e.printStackTrace();
			logger.info("Exception in copyProtocolActions:" + e);	
		}
		return vo;
	}
	
	@Override
	public List<HashMap<String, Object>> getAmendRenewalModules(String protoocolNumber) {
		 ArrayList<HashMap<String, Object>> result =null;
		try{
			ArrayList<InParameter> inputparams  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParams = new ArrayList<OutParameter>();	
			inputparams.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protoocolNumber));        
            outputParams.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));        
            result = dbEngine.executeProcedure(inputparams,"GET_IRB_AMEND_RENEWAL_MODULES",outputParams);
		}catch (Exception e) {
			logger.info("Exception in getAmendRenewalModules:" + e);
		}
		return result;
	}
	
	public ArrayList<HashMap<String, Object>> protocolActionSP(IRBActionsVO vo,String newProtocolNumber){
		ArrayList<HashMap<String, Object>> result = null;
		try {ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();		
		inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
		inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
		inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
		inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
		inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
		inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,vo.getSqlActionDate()));		
		inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
		inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
		inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));	
		inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevSubmissonStatusCode()));	 
		inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevProtocolStatusCode()));	
		inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
		inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
		inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getSelectedNotifyTypeQualifier()));	 
		inputParam.add(new InParameter("AV_NEW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,newProtocolNumber));
		inputParam.add(new InParameter("AV_NEXT_SUBMISN_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_SUBMS_NEXT_STATUS_CODE")));
		inputParam.add(new InParameter("AV_NEXT_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_NEXT_STATUS_CODE"))); 
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
		result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTCOL_ACTIONS",outputParam);
} catch (Exception e) {	
		        e.printStackTrace();
		    	logger.info("Exception in updateSubmissionStatus:" + e);	
		  }
		return result;	
		}
	
	public void protocolActionAttachments(MultipartFile[] files,IRBActionsVO vo) {		
		try {					
			ArrayList<InParameter> inputParam = null;
			for (int i = 0; i < files.length; i++) {
				inputParam = new ArrayList<>();
				inputParam.add(new InParameter("AV_SUBMISSION_DOC_ID ", DBEngineConstants.TYPE_INTEGER,null));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,files[i].getOriginalFilename()));
				inputParam.add(new InParameter("AV_DOCUMENT", DBEngineConstants.TYPE_BLOB,files[i].getBytes()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
				inputParam.add(new InParameter("AV_FILE_DATA_ID ", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,"I"));			
				dbEngine.executeProcedure(inputParam, "UPD_IRB_PROTO_ACTION_ATTACMNT");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in protocolActionAttachments method" + e);
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> iterateAmendRenewalModule(IRBActionsVO vo,
			ArrayList<HashMap<String, Object>> renewalModules) {
		renewalModules=(ArrayList<HashMap<String, Object>>) getAmendRenewalModules(vo.getProtocolNumber());
		for(HashMap<String,Object> modules: renewalModules) {
			if(modules.get("STATUS_FLAG").toString().equals("Y")){
				modules.remove("STATUS_FLAG");
				modules.put("STATUS_FLAG",true);
			}else{
				modules.remove("STATUS_FLAG");
				modules.put("STATUS_FLAG",false);
			}
		}
		return renewalModules;
	}

	@Override
	public String getAmendRenewalSummary(IRBActionsVO vo) {
		String amendRenewalSummary = null;
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_PROTO_AMEND_REN_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inputParam,"GET_IRB_PROTO_AMND_RENW_SUMARY",outputParam);
			if(result != null && !result.isEmpty()){
				amendRenewalSummary = result.get(0).get("SUMMARY").toString();
			}
		} catch (Exception e) {
			logger.info("Exception in getAmendRenewalSummary method" + e);
		}
		return amendRenewalSummary;
	}

	@Override
	public IRBActionsVO updateAmendRenewModule(IRBActionsVO vo) {
		try{
			for(HashMap<String,Object> modules:vo.getModuleAvailableForAmendment()){
				if(modules.get("AC_TYPE") != null && modules.get("AC_TYPE").toString().equals("U")){
					Integer protoAmendRenewalId = modules.get("PROTO_AMEND_RENEWAL_ID") != null ?Integer.parseInt(modules.get("PROTO_AMEND_RENEWAL_ID").toString()):null;
					ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
					inputParam.add(new InParameter("AV_PROTO_AMEND_REN_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
					inputParam.add(new InParameter("AV_AMND_REN_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
					inputParam.add(new InParameter("AV_PARENT_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber().substring(0, 10)));
					inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
					inputParam.add(new InParameter("AV_PROTO_AMEND_RENEWAL_ID", DBEngineConstants.TYPE_INTEGER,protoAmendRenewalId));
					inputParam.add(new InParameter("AV_SUMMARY", DBEngineConstants.TYPE_STRING,vo.getComment()));
					inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
					inputParam.add(new InParameter("AV_PROTO_MODULE_CODE", DBEngineConstants.TYPE_STRING,modules.get("PROTOCOL_MODULE_CODE")));
					inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,protoAmendRenewalId == null ? "I":"D"));
					dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_AMND_RNEW_MODULE");	
					}	
				}
			updateSummarryData(vo);
			} catch (Exception e) {
				vo.setSuccessCode(false);
				vo.setSuccessMessage("Amendment creation Failed");
				logger.info("Exception in updateAmendRenewModule:" + e);	
			}	  
			return vo;
	}

	private void updateSummarryData(IRBActionsVO vo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PROTO_AMEND_REN_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SUMMARY", DBEngineConstants.TYPE_STRING,vo.getComment()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_AMNDRNW_SUMMARY");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("updateSummarryData Failed");
			logger.info("Exception in updateSummarryData:" + e);	
		}		
	}

	@Override
	public IRBActionsVO returnToPiAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Returned to PI successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Return to PI Failed");
			e.printStackTrace();
			logger.info("Exception in returnToPiAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO closeAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("CLosed successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Close Failed");
			e.printStackTrace();
			logger.info("Exception in closeAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO disapproveAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Dissapproved successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Dissapprove Failed");
			e.printStackTrace();
			logger.info("Exception in disapproveAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO irbAcknowledgementAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("IRB Acknowledgement successfull");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("IRB Acknowledgement Failed");
			e.printStackTrace();
			logger.info("Exception in irbAcknowledgementAdminActions:" + e);	
		}
		return vo;
	}
}
