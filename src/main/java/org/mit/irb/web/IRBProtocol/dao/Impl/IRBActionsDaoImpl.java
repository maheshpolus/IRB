package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
		ArrayList<HashMap<String, Object>> results = null;

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
			ProtocolSubmissionStatuses protocolSubmissionStatuses=new ProtocolSubmissionStatuses();
			for(int i=0;i<finalResult.size();i++){
				ArrayList<InParameter> inputparams  = new ArrayList<InParameter>();
				ArrayList<OutParameter> outputParams = new ArrayList<OutParameter>();
				if(finalResult.get(i).get("ACTION_NAME").toString().equals("Submit for Review")){					
					inputparams.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
					inputparams.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
					inputparams.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSubmissionNumber()));	
					outputParams.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
					results=dbEngine.executeProcedure(inputparams,"GET_IRB_PROTO_ACTION_SUBMISSN",outputParams);
					if(results != null && !results.isEmpty()){
					protocolSubmissionStatuses.setSubmission_Id(Integer.parseInt(results.get(0).get("SUBMISSION_ID").toString()));
					protocolSubmissionStatuses.setProtocolNumber(results.get(0).get("PROTOCOL_NUMBER").toString());
					protocolSubmissionStatuses.setSequenceNumber(Integer.parseInt(results.get(0).get("SEQUENCE_NUMBER").toString()));
					protocolSubmissionStatuses.setSubmissionNumber(Integer.parseInt(results.get(0).get("SUBMISSION_NUMBER").toString()));
					protocolSubmissionStatuses.setCommitteeId(results.get(0).get("COMMITTEE_ID").toString());
					protocolSubmissionStatuses.setProtocolId(Integer.parseInt(results.get(0).get("PROTOCOL_ID").toString()));
					protocolSubmissionStatuses.setSubmissionTypeCode(results.get(0).get("SUBMISSION_TYPE_CODE").toString());
					protocolSubmissionStatuses.setSubmissionTypeQualCode(results.get(0).get("SUBMISSION_TYPE_QUAL_CODE").toString());
					protocolSubmissionStatuses.setSubmissionStatusCode(results.get(0).get("SUBMISSION_STATUS_CODE").toString());
					protocolSubmissionStatuses.setProtocolReviewTypeCode(results.get(0).get("PROTOCOL_REVIEW_TYPE_CODE").toString());
					protocolSubmissionStatuses.setSubmissionDate((Date) results.get(0).get("SUBMISSION_DATE"));
					protocolSubmissionStatuses.setComments(results.get(0).get("COMMENTS").toString());
					protocolSubmissionStatuses.setYesVoteCount(Integer.parseInt(results.get(0).get("YES_VOTE_COUNT").toString()));
					protocolSubmissionStatuses.setNoVoteCount(Integer.parseInt(results.get(0).get("NO_VOTE_COUNT").toString()));
					protocolSubmissionStatuses.setAbstainerCount(Integer.parseInt(results.get(0).get("ABSTAINER_COUNT").toString()));
					protocolSubmissionStatuses.setVotingComments(results.get(0).get("VOTING_COMMENTS").toString());
					protocolSubmissionStatuses.setUpdateTimeStamp((Date) results.get(0).get("UPDATE_TIMESTAMP"));
					protocolSubmissionStatuses.setUpdateUser(results.get(0).get("UPDATE_USER").toString());
					protocolSubmissionStatuses.setRecusedCount(Integer.parseInt(results.get(0).get("RECUSED_COUNT").toString()));
					protocolSubmissionStatuses.setIsBillable(results.get(0).get("IS_BILLABLE").toString());
					protocolSubmissionStatuses.setCommDecisionMotionTypeCode(results.get(0).get("COMM_DECISION_MOTION_TYPE_CODE").toString());
					protocolSubmissionStatuses.setScheduleId(Integer.parseInt(results.get(0).get("SCHEDULE_ID").toString()));
					}
					else{
						protocolSubmissionStatuses.setProtocolNumber(vo.getProtocolNumber());
						protocolSubmissionStatuses.setSequenceNumber(vo.getSequenceNumber());
						protocolSubmissionStatuses.setProtocolId(vo.getProtocolId());
					}
				}
			}
			vo.setProtocolSubmissionStatuses(protocolSubmissionStatuses);
			vo.setPersonActionsList(finalResult);		
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getPersonRight:" + e);
		}
		return vo;
	}

	@Override
	public IRBActionsVO submitForReviewProtocolActions(IRBActionsVO vo) {
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));	
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,null));				
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));				
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSequenceNumber()));				
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));				
			inputParam.add(new InParameter("AV_ACTION_COMMENT", DBEngineConstants.TYPE_STRING,""));				
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));				
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevSubmissonStatusCode()));				
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPrevProtocolStatusCode()));				
			inputParam.add(new InParameter("AV_FOLLOWUP_ACTION_CODE", DBEngineConstants.TYPE_STRING,vo.getFollowUpActionCode()));				
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));				
			inputParam.add(new InParameter("AV_CREATE_USER", DBEngineConstants.TYPE_STRING,vo.getCreateUser()));				
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));				
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,vo.getAcType()));
			inputParam.add(new InParameter("AV_PROTOCOL_ACTION_ID", DBEngineConstants.TYPE_STRING,null));
		    dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTOCOL_ACTION");
		    updateSubmissionStatus(vo);
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Successfully Updated");
		}catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage(e.getMessage());
			e.printStackTrace();
			logger.info("Exception in submitForReviewProtocolActions:" + e);
		}		
		return vo;
	}

	public void updateSubmissionStatus(IRBActionsVO vo){
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));	
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));	
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));	
			inputParam.add(new InParameter("AV_SUBMISSION_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getSubmissionStatus()));	
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
			inputParam.add(new InParameter("AV_SUBMISSION_DATE", DBEngineConstants.TYPE_DATE,vo.getProtocolSubmissionStatuses().getSubmissionDate()));	
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,vo.getAcType()));	
		    dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTOCOL_SUBMISSION");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);		
			}
	}
	
	@Override
	public IRBActionsVO withdrawProtocolActions(IRBActionsVO vo) {	
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getComments()));		
			//inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_INTEGER,vo.getUpdateUser()));	 
		    dbEngine.executeFunction(inputParam,"fn_irb_protcol_actions");
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Successfully Updated");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage(e.getMessage());
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO adandonProtocolActions(IRBActionsVO vo) {
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getComments()));		
			//inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_INTEGER,vo.getUpdateUser()));	 
		    dbEngine.executeFunction(inputParam,"fn_irb_protcol_actions");
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Successfully Updated");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage(e.getMessage());
			e.printStackTrace();
			logger.info("Exception in updateSubmissionStatus:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO createAmendmentProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO notifyIRBProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO requestForDataAnalysisProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO requestForSuspensionProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRBActionsVO requestForTerminationProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
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
	public IRBActionsVO modifyAmendmentSectionProtocolActions(IRBActionsVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

}
