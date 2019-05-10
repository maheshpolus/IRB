package org.mit.irb.web.IRBProtocol.dao.Impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.stereotype.Service;

@Service(value = "irbActionsDao")
public class IRBActionsDaoImpl implements IRBActionsDao {
    DBEngine dbEngine;
	
	IRBActionsDaoImpl() {
		dbEngine = new DBEngine();
	}

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
				inputparams.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
				inputparams.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
				inputparams.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSubmissionNumber()));	
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
						ArrayList<InParameter> inputparam  = new ArrayList<InParameter>();
						ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();
						inputparam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
						outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
						renewalModules=dbEngine.executeProcedure(inputparam,"GET_IRB_AMEND_RENEWAL_MODULES",outputparam);
						for(HashMap<String,Object> modules: renewalModules) {
							if(modules.get("STATUS_FLAG").toString().equals("Y")){
								modules.remove("STATUS_FLAG");
								modules.put("STATUS_FLAG",true);
							}else{
								modules.remove("STATUS_FLAG");
								modules.put("STATUS_FLAG",false);
							}
						}
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
		try {	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));	
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_STATUS_CODE", DBEngineConstants.TYPE_STRING,"102"));	
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
		ArrayList<HashMap<String, Object>> result = null;
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();		
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevSubmissonStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevProtocolStatusCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));	 
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
			result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTCOL_ACTIONS",outputParam);
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
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();		
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevSubmissonStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevProtocolStatusCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));	 
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
			result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTCOL_ACTIONS",outputParam);		
		    vo.setProtocolNumber(result.get(0).get("PROTOCOL_NUMBER").toString());
		    vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
		    updateAmendRenewModule(vo);
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Amendment created successfully");	
		} catch (Exception e) {
			updateAmendRenewModule(vo);
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Amendment creation Failed");
			e.printStackTrace();
			logger.info("Exception in createAmendmentProtocolActions:" + e);	
		}
		return vo;
	}

	private IRBActionsVO updateAmendRenewModule(IRBActionsVO vo) {
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
	}

	@Override
	public IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo) {	
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_INTEGER,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevSubmissonStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevProtocolStatusCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_ATTACHMENTS", DBEngineConstants.TYPE_STRING,new byte[0]));
		    dbEngine.executeFunction(inputParam,"UPD_irb_protcol_actions");
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Withdrawn successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Withdrawn Failed");
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);	
		}
		return vo;
	}
	
	@Override
	public IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo) {	
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_INTEGER,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevSubmissonStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevProtocolStatusCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_ATTACHMENTS", DBEngineConstants.TYPE_STRING,new byte[0]));
		    dbEngine.executeFunction(inputParam,"UPD_irb_protcol_actions");
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Withdrawn successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Withdrawn Failed");
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo) {	
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_INTEGER,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevSubmissonStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_INTEGER,vo.getPrevProtocolStatusCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_ATTACHMENTS", DBEngineConstants.TYPE_STRING,new byte[0]));
		    dbEngine.executeFunction(inputParam,"UPD_irb_protcol_actions");
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Withdrawn successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Withdrawn Failed");
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO requestForCloseProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO requestForCloseEnrollmentProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO requestForReopenEnrollmentProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO copyProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}
/*	
	public void protocolActionAttachments(MultipartFile[] files) {		
		try {					
			ArrayList<InParameter> inputParam = null;
			for (int i = 0; i < files.length; i++) {
				inputParam = new ArrayList<>();
				inputParam.add(new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER,));
				inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING, files[i].getOriginalFilename()));
				inputParam.add(new InParameter("AV_FILE_DATA", DBEngineConstants.TYPE_BLOB, files[i].getBytes()));
				inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,));
				inputParam.add(new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING,));
				dbEngine.executeProcedure(inputParam, "");
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in protocolActionAttachments method" + e);
		}*/
	
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

}
