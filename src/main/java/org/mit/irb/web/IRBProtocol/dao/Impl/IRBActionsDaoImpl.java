package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.AdminCheckListDetail;
import org.mit.irb.web.IRBProtocol.pojo.IRBCommitteeReviewerComments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.correspondence.dao.DocxDocumentMergerAndConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Service(value = "irbActionsDao")
@Transactional
public class IRBActionsDaoImpl implements IRBActionsDao {
    DBEngine dbEngine;
	
	IRBActionsDaoImpl() {
		dbEngine = new DBEngine();
	}

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	Logger logger = Logger.getLogger(IRBUtilDaoImpl.class.getName());

	@Override
	public IRBActionsVO getActionList(IRBActionsVO vo) {		
		ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		ArrayList<HashMap<String, Object>> permissionList = null;
		ArrayList<HashMap<String, Object>> result = null;	
		ArrayList<HashMap<String, Object>> intialResult =  new ArrayList<HashMap<String, Object>>();
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
						intialResult.addAll(result);								
				}						
			}
			vo = getProtocolActionDetails(vo);	
			ArrayList<HashMap<String, Object>> finalResult = actionsAvaliableForUser(vo,intialResult);			
			vo.setPersonActionsList(finalResult);	
		} catch (Exception e) {
			logger.info("Exception in getPersonRight:" + e);
		}
		return vo;
	}

	private ArrayList<HashMap<String, Object>> actionsAvaliableForUser(IRBActionsVO vo,ArrayList<HashMap<String, Object>> intialResult) {
		String reviewTypeCode =vo.getProtocolSubmissionStatuses().getProtocolReviewTypeCode();
		if(intialResult !=null && !intialResult.isEmpty()){	
		for(int i = 0;i < intialResult.size();i++)
        {
            Object temp = intialResult.get(i).get("ACTION_NAME");
            for(int k = i+1;k < intialResult.size();k++)
            {
                if(temp.equals(intialResult.get(k).get("ACTION_NAME")))
                {
                	intialResult.remove(k); 
                }                 
            }
            temp = intialResult.get(i).get("ACTION_CODE"); 
            switch (temp.toString()) {
			case "205":
				 if(reviewTypeCode == null){
					 intialResult.remove(i);
				 }					 
				 else if(!reviewTypeCode.equals("2")){
					 intialResult.remove(i);
				 }
				break;
			case "208":
				 if(reviewTypeCode == null){
					 intialResult.remove(i);
				 }					 
				 else if(!reviewTypeCode.equals("6")){
					 intialResult.remove(i);
				 }
				break;
			case "210":
				 if(reviewTypeCode == null){
					 intialResult.remove(i);
				 }					 
				 else if(!reviewTypeCode.equals("5")){
					 intialResult.remove(i);
				 }
				break;
			case "204":
			case "304":
			case "203":	
			case "202":	
				if(reviewTypeCode == null){
					intialResult.remove(i);
				  }
				break;			
			 }	          
           }
	     }
		return intialResult;
	}         

	public IRBActionsVO getProtocolActionDetails(IRBActionsVO vo){
		try{
			ProtocolSubmissionStatuses protocolSubmissionStatuses = new ProtocolSubmissionStatuses();
			if(vo.getSubmissionId() != null){
				protocolSubmissionStatuses  = fetchSubmissionDetails(vo,protocolSubmissionStatuses);		
			}else{
				protocolSubmissionStatuses.setProtocolNumber(vo.getProtocolNumber());
				protocolSubmissionStatuses.setSequenceNumber(vo.getSequenceNumber());
				protocolSubmissionStatuses.setProtocolId(vo.getProtocolId());
			}
			vo.setProtocolSubmissionStatuses(protocolSubmissionStatuses);			
		}catch (Exception e) {
			logger.info("Exception in getProtocolSubmissionDetails:" + e);		
		}
		return vo;			
	}

	private ProtocolSubmissionStatuses fetchSubmissionDetails(IRBActionsVO vo,ProtocolSubmissionStatuses protocolSubmissionStatuses) {
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProtocolSubmissionStatuses> criteria = builder.createQuery(ProtocolSubmissionStatuses.class);
			Root<ProtocolSubmissionStatuses> submissionRoot=criteria.from(ProtocolSubmissionStatuses.class);			
			criteria.where(builder.equal(submissionRoot.get("submission_Id"),Integer.parseInt(vo.getSubmissionId())));
			if(session.createQuery(criteria).getResultList() != null)
			protocolSubmissionStatuses = session.createQuery(criteria).getResultList().get(0);					
		}catch (Exception e) {
			logger.info("Exception in getProtocolSubmissionDetails:" + e);		
		}
		return protocolSubmissionStatuses;
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
		  //  vo=generateProtocolCorrespondence(vo);
	} catch (Exception e) {
			logger.info("Exception in updateSubmissionStatus:" + e);		
			}	
		return vo;
	}

	@Override
	public void updateActionStatus(IRBActionsVO vo){
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();		
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
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
		    dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTOCOL_LOG_ACTION",outputParam);
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Submitted Successfully");
		}catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Submission Failed");
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
			logger.info("Exception in createAmendmentProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO createRenewalProtocolActions(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result=protocolActionSP(vo,null);	
			vo.setProtocolNumber(result.get(0).get("PROTOCOL_NUMBER").toString());
			vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
			updateSummarryData(vo);
		    vo.setSuccessCode(true);
		    vo.setSuccessMessage("Renewal created successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Renewal creation Failed");
			logger.info("Exception in createRenewalProtocolActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO deleteProtocolAmendmentRenewalProtocolActions(IRBActionsVO vo) {	
		vo = deleteProtocolActions(vo);
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
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam = new ArrayList<OutParameter>();		
			inputParam.add(new InParameter("AV_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getActionTypeCode()));	
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));		
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getScheduleId()));		
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));		
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getComment()));
			inputParam.add(new InParameter("AV_ACTION_DATE", DBEngineConstants.TYPE_DATE,vo.getActionDate() != null ? generateSqlDate(vo.getActionDate()): null));		
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));		
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getProtocolId()));		
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getProtocolNumber()));		
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));	
			inputParam.add(new InParameter("AV_PREV_SUB_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionStatusCode()));	 
			inputParam.add(new InParameter("AV_PREV_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolStatus()));	
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getSubmissionTypeCode()));	 
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,vo.getProtocolSubmissionStatuses().getCommitteeId()));	 
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,vo.getSelectedTypeQualifier()));	 
			inputParam.add(new InParameter("AV_NEW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,newProtocolNumber));
			inputParam.add(new InParameter("AV_NEXT_SUBMISN_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_SUBMS_NEXT_STATUS_CODE")));
			inputParam.add(new InParameter("AV_NEXT_PROTO_STATUS_CODE", DBEngineConstants.TYPE_STRING,vo.getPersonAction().get("PROTO_NEXT_STATUS_CODE"))); 
			inputParam.add(new InParameter("AV_APPROVAL_DATE", DBEngineConstants.TYPE_DATE,vo.getApprovalDate() != null ? generateSqlDate(vo.getApprovalDate()): null));		
			inputParam.add(new InParameter("AV_EXPIRATION_DATE", DBEngineConstants.TYPE_DATE,vo.getExpirationDate() != null ? generateSqlDate(vo.getExpirationDate()): null));		
			inputParam.add(new InParameter("AV_DECISION_DATE", DBEngineConstants.TYPE_DATE,vo.getDecisionDate() != null ? generateSqlDate(vo.getDecisionDate()): null));		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));	
			result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTCOL_ACTIONS",outputParam);
		} catch (Exception e) {	
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
			logger.info("Exception in protocolActionAttachments method" + e);
		}
	}

	@Async
	public ArrayList<HashMap<String, Object>> iterateAmendRenewalModule(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> renewalModules = null;
		renewalModules=(ArrayList<HashMap<String, Object>>) getAmendRenewalModules(vo.getProtocolNumber());
		renewalModules.forEach(modules ->{
			if(modules.get("STATUS_FLAG").toString().equals("Y")){
				modules.remove("STATUS_FLAG");
				modules.put("STATUS_FLAG",true);
			}else{
				modules.remove("STATUS_FLAG");
				modules.put("STATUS_FLAG",false);
			}
		});
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
			if(result != null){
				amendRenewalSummary = result.get(0).get("SUMMARY") != null ? result.get(0).get("SUMMARY").toString() : null;
			}
		} catch (Exception e) {
			logger.info("Exception in getAmendRenewalSummary method" + e);
		}
		return amendRenewalSummary;
	}

	@Override
	public IRBActionsVO updateAmendRenewModule(IRBActionsVO vo) {
		try{
			vo.getModuleAvailableForAmendment().forEach(modules ->{
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
					try {
						dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_AMND_RNEW_MODULE");
					} catch (Exception e) {
						vo.setSuccessCode(false);
						vo.setSuccessMessage("Amendment creation Failed");
						logger.info("Exception in updateAmendRenewModule:" + e);	
						}	
					}			
			});
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
			inputParam.add(new InParameter("AV_PARENT_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber().substring(0, 10)));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_AMNDRNW_SUMMARY");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("updateSummarryData Failed");
			logger.info("Exception in updateSummarryData:" + e);	
		}		
	}
	
                   //**********************Admin_Actions****************************//
	
	@Override
	public IRBActionsVO returnToPiAdminActions(IRBActionsVO vo,MultipartFile[] files) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			getIsCloseActionRequested(vo);
			result = protocolActionSP(vo,null);	
		    if(!result.isEmpty()){
		    	vo.setProtocolId(Integer.parseInt(result.get(0).get("PROTOCOL_ID").toString()));
		    	vo.setSubmissionId(result.get(0).get("SUBMISSION_ID").toString());
		    	vo.setSubmissionNumber(Integer.parseInt(result.get(0).get("SUBMISSION_NUMBER").toString()));
		    	vo.setSequenceNumber(Integer.parseInt(result.get(0).get("SEQUENCE_NUMBER").toString()));
		    }
			protocolActionAttachments(files,vo);
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
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    Integer isRequested =  getIsCloseActionRequested(vo);
			    if(isRequested.equals(1)){
				    vo.setTemplateTypeCode("22");
			    }else{
				    vo.setTemplateTypeCode("23");
			    }
			}	
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Closed successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Close Failed");
			logger.info("Exception in closeAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO disapproveAdminActions(IRBActionsVO vo) {
		try {	
			updateSubmissionDetail(vo);
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("16");
			}
			updateRiskLevelDetails(vo);
			updateReviewComments(vo);							
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Dissapproved successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Dissapprove Failed");
			logger.info("Exception in disapproveAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO irbAcknowledgementAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			protocolActionSP(vo,null);
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("COUHES acknowledged successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("COUHES acknowledgement Failed");
			logger.info("Exception in irbAcknowledgementAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO reOpenEnrollmentAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("18");
			}
			protocolActionAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Re-Open enrollment successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Re-Open enrollment Failed");
			logger.info("Exception in reOpenEnrollmentAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO dataAnalysisOnlyAdminActions(IRBActionsVO vo) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("14");
			}
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Data analysis only successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Data analysis only Failed");
			logger.info("Exception in dataAnalysisOnlyAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO closedForEnrollmentAdminActions(IRBActionsVO vo) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("13");
			}
			updateReviewComments(vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Closed for enrollment successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Closed for enrollment Failed");
			logger.info("Exception in closedForEnrollmentAdminActions:" + e);	
		}
		return vo;
	}
	

	@Override
	public IRBActionsVO terminateAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("21");
			}
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Terminated successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Terminate Failed");
			e.printStackTrace();
			logger.info("Exception in terminateAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO suspendAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("20");
			}
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Suspended successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Suspend Failed");
			e.printStackTrace();
			logger.info("Exception in suspendAdminActions:" + e);	
		}
		return vo;
	}
	
	@Override
	public IRBActionsVO notifyCommiteeAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Notify Committee successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Notify Committee Failed");
			e.printStackTrace();
			logger.info("Exception in notifyCommiteeAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO deferAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("15");
			}
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Defered successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Defer Failed");
			e.printStackTrace();
			logger.info("Exception in deferAdminActions:" + e);	
		}
		return vo;
	}


	@Override
	public IRBActionsVO assignToAgendaAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			protocolActionSP(vo,null);
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Assign to agenda successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Assign to agenda Failed");
			e.printStackTrace();
			logger.info("Exception in assignToAgendaAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO grantExceptionAdminActions(IRBActionsVO vo) {
		try {			
			//protocolActionSP(vo,null);
			//generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Exception granted successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Grant exception Failed");
			e.printStackTrace();
			logger.info("Exception in grantExceptionAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO reviewNotRequiredAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("19");
			}
			updateReviewComments(vo);
			protocolActionReviewerAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Review not required successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Review not required Failed");
			e.printStackTrace();
			logger.info("Exception in reviewNotRequiredAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO approvedAdminActions(IRBActionsVO vo) {
		try {	
			updateSubmissionDetail(vo);
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);
			updateRiskLevelDetails(vo);			
			updateReviewComments(vo);
			if(vo.getProtocolNumber().contains("A")){
			    vo.setTemplateTypeCode("8");
			}else if(vo.getProtocolNumber().contains("R")){
				vo.setTemplateTypeCode("12");
			}else{
				vo.setTemplateTypeCode("9");
			}						
			if(!result.isEmpty()){
					vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
				    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
				    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			}					
			generateProtocolCorrespondence(vo);	
			if(vo.getProtocolNumber().length() > 10){				
				vo.setProtocolNumber(vo.getProtocolNumber().substring(0, 10));
			}
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Approved successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Approve Failed");
			e.printStackTrace();
			logger.info("Exception in approvedAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO SMRRAdminActions(IRBActionsVO vo) {
		try {				
			updateSubmissionDetail(vo);
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			updateRiskLevelDetails(vo);
			updateReviewComments(vo);
			if(result != null && !result.isEmpty()){
				   vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
				   vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
				   vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
				   vo.setTemplateTypeCode("11");				   
			}
			generateProtocolCorrespondence(vo);			
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Specific Minor Revisions Required successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Specific Minor Revisions Required Failed");
			e.printStackTrace();
			logger.info("Exception in SMRRAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO SRRAdminActions(IRBActionsVO vo) {
		try {
			updateSubmissionDetail(vo);
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("25");
			}			
			updateRiskLevelDetails(vo);
			updateReviewComments(vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Substantive Revisions Required successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Substantive Revisions Required Failed");
			e.printStackTrace();
			logger.info("Exception in SRRAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO expeditedApprovalAdminActions(IRBActionsVO vo) {
		try {
			updateSubmissionDetail(vo);										
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			updateExpeditedApprovalCheckList(vo);
			updateRiskLevelDetails(vo);
			updateReviewComments(vo);
			if(vo.getProtocolNumber().length() > 10){				
				vo.setProtocolNumber(vo.getProtocolNumber().substring(0, 10));
			}
			if(result != null && !result.isEmpty()){
				   vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
				   vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
				   vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			}
			if(vo.getProtocolNumber().contains("A")){
			    vo.setTemplateTypeCode("17");
			}else if(vo.getProtocolNumber().contains("R")){
				vo.setTemplateTypeCode("6");
			}else{
				vo.setTemplateTypeCode("7");
			}
			generateProtocolCorrespondence(vo);
			if(vo.getProtocolNumber().length() > 10){				
				vo.setProtocolNumber(vo.getProtocolNumber().substring(0, 10));
			}
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Expedited approval successfully");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Expedited approval Failed");
			e.printStackTrace();
			logger.info("Exception in expeditedApprovalAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO responseApprovalAdminActions(IRBActionsVO vo) {
		try {												
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(result != null && !result.isEmpty()){
				   vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
				   vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
				   vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
				   vo.setTemplateTypeCode("10");				   
			}
			generateProtocolCorrespondence(vo);
			if(vo.getProtocolNumber().length() > 10){				
				vo.setProtocolNumber(vo.getProtocolNumber().substring(0, 10));
			}
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Response approval successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Response approval Failed");
			e.printStackTrace();
			logger.info("Exception in responseApprovalAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO administrativeCorrectionAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);						
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Adminstrative correction successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Adminstrative correction Failed");
			e.printStackTrace();
			logger.info("Exception in administrativeCorrectionAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO adandonAdminActions(IRBActionsVO vo, MultipartFile[] files) {
		try {			
			ArrayList<HashMap<String, Object>> result = null;			
			result = protocolActionSP(vo,null);	
			if(!result.isEmpty()){
				vo.setActionId(result.get(0).get("ACTION_LOG_ID") == null ? null : Integer.parseInt(result.get(0).get("ACTION_LOG_ID").toString()));
			    vo.setCorrespTemplateTypeCode(result.get(0).get("CORESSPOND_TYPE_CODE")  == null ? null : result.get(0).get("CORESSPOND_TYPE_CODE").toString());
			    vo.setCorrespTypeDescription(result.get(0).get("CORESSP_TYPE") == null ? null : result.get(0).get("CORESSP_TYPE").toString());
			    vo.setTemplateTypeCode("24");
			}		
			protocolActionAttachments(files, vo);
			generateProtocolCorrespondence(vo);
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Adandon successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Abandon Failed");
			e.printStackTrace();
			logger.info("Exception in adandonAdminActions:" + e);	
		}
		return vo;
	}

	@Override
	public IRBActionsVO undoLastActionAdminActions(IRBActionsVO vo) {
		try {			
			protocolActionSP(vo,null);						
			vo.setSuccessCode(true);
		    vo.setSuccessMessage("Undo last action successful");	
		} catch (Exception e) {
			vo.setSuccessCode(false);
			vo.setSuccessMessage("Undo last action Failed");
			e.printStackTrace();
			logger.info("Exception in undoLastActionAdminActions:" + e);	
		}
		return vo;
	}
	
	public Date generateSqlDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date utilDate = null;
		java.sql.Date sqlDate= null;
		if(date != null){
		try{
			utilDate = sdf.parse(date);
			sqlDate = new java.sql.Date(utilDate.getTime());
		}catch (Exception e) {
			logger.info("Exception in generateSqlActionDate:" + e);
		}
	  }
		return sqlDate;
	}
	
	public IRBActionsVO generateProtocolCorrespondence(IRBActionsVO vo){
		ResponseEntity<byte[]> attachmentData = null;
		try{
			vo.setProtocolHeaderDetails(irbProtocolDao.getIRBProtocolDetail(vo.getProtocolNumber()));
			byte[] data = getTemplateData(vo);
			byte[] mergedOutput = mergePlaceHolders(data,vo);
			String generatedFileName = vo.getCorrespTypeDescription()+".pdf";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.setContentDispositionFormData(generatedFileName, generatedFileName);
			headers.setContentLength(mergedOutput.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(mergedOutput, headers, HttpStatus.OK);	
			saveProtocolCorrespondence(vo,attachmentData);
		}catch (Exception e) {
			logger.error("Exception in generateProtocolCorrespondence"+ e.getMessage());
		}
		return vo;
	}
	
	public byte[] getTemplateData(IRBActionsVO vo) {
		byte[] attachmentData =null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();			
			inParam.add(new InParameter("AV_LETTER_TEMPLATE_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getTemplateTypeCode()));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_LETTER_TEMPLATE_TYPE", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("CORRESPONDENCE_TEMPLATE");
				attachmentData = byteArrayOutputStream.toByteArray();
			}
		} catch (Exception e) {
			logger.info("Exception in getTemplateData method:" + e);
		}
		return attachmentData;
	}
	
	public byte[] mergePlaceHolders(byte[] data, IRBActionsVO vo) {
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			IContext context = report.createContext();
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			fieldsMetadata.load("CommentList", IRBCommitteeReviewerComments.class, true);
			ArrayList<HashMap<String, Object>> commentsSaved = getCommitteeReviewersComments(vo.getProtocolSubmissionStatuses().getSubmission_Id());
			List<IRBCommitteeReviewerComments> CommentList = setComment(commentsSaved);
			context.put("CommentList", CommentList);
			context = setPlaceHolderData(context,vo);
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}	
	
	private List<IRBCommitteeReviewerComments> setComment(ArrayList<HashMap<String, Object>> commentsSaved){		
		
		List<IRBCommitteeReviewerComments> commentList = new ArrayList<IRBCommitteeReviewerComments>();		
		for(HashMap<String, Object> comment: commentsSaved){
			if(comment.get("INCLUDE_IN_LETTER").toString().equals("Y"))
			commentList.add(new IRBCommitteeReviewerComments(comment.get("COMMENTS").toString() == 	null ? "" :comment.get("COMMENTS").toString()));
		}
		return commentList;		
	}
	
	private IContext setPlaceHolderData(IContext context,IRBActionsVO vo) {
		try{			
			java.util.Date date = new java.util.Date();  
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); 
			String code = null;	
			ArrayList<HashMap<String, Object>> expeditedCheckListCode = getProtoExpeditedChckList(vo);
			if(expeditedCheckListCode != null && !expeditedCheckListCode.isEmpty())
			for(HashMap<String, Object> categoryCode: expeditedCheckListCode){
				if(code == null || code.isEmpty()){
					code = categoryCode.get("CATEGORY_CODE") ==  null ? null : categoryCode.get("CATEGORY_CODE").toString();
				}else{
					code = code.concat(categoryCode.get("CATEGORY_CODE") ==  null ? "" :", "+categoryCode.get("CATEGORY_CODE").toString());
				}
			}
			context.put("Date",formatter.format(date));
			context.put("ExpeditedCategorySelection",code == null ? "" :code);
			context.put("ProtocolNumber",vo.getProtocolNumber());
		    date = formatter.parse(vo.getActionDate());
			context.put("ActionDate",formatter.format(date));
			context.put("CommitteeAction",vo.getProtocolHeaderDetails().get("SUBMISSION_STATUS") == null ? "" : vo.getProtocolHeaderDetails().get("SUBMISSION_STATUS").toString());
			context.put("StudyTitle",vo.getProtocolHeaderDetails().get("TITLE") == null ? "" : vo.getProtocolHeaderDetails().get("TITLE").toString());
			context.put("PI_Name",vo.getProtocolHeaderDetails().get("PI_NAME") == null ? "" : vo.getProtocolHeaderDetails().get("PI_NAME").toString());
			context.put("RiskLevel",vo.getProtocolHeaderDetails().get("RISK_LEVEL") == null ? "" : vo.getProtocolHeaderDetails().get("RISK_LEVEL").toString());
			context.put("FDARisk",vo.getProtocolHeaderDetails().get("FDA_RISK_LEVEL") == null ? "" : vo.getProtocolHeaderDetails().get("FDA_RISK_LEVEL").toString());
			context.put("ProtocolStatus",vo.getProtocolHeaderDetails().get("PROTOCOL_STATUS") == null ? "" : vo.getProtocolHeaderDetails().get("PROTOCOL_STATUS").toString());			
			context.put("ExpirationDate",vo.getProtocolHeaderDetails().get("EXPIRATION_DATE") == null ? "" : vo.getProtocolHeaderDetails().get("EXPIRATION_DATE").toString().replace('/', '-'));
		}catch (Exception e) {
			logger.info("Exception in setPlaceHolderData method:" + e);
		}
		return context;
	}	

	private void saveProtocolCorrespondence(IRBActionsVO vo, ResponseEntity<byte[]> attachmentData) {
		try{
		    ArrayList<InParameter> inputParam =  new ArrayList<InParameter>();	
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
			inputParam.add(new InParameter("AV_PROTOCOL_ACTION_ID", DBEngineConstants.TYPE_INTEGER,vo.getActionId()));
			inputParam.add(new InParameter("AV_PROTO_CORRESP_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getCorrespTemplateTypeCode()));
			inputParam.add(new InParameter("AV_FINAL_FLAG", DBEngineConstants.TYPE_STRING,"Y"));
			inputParam.add(new InParameter("AV_CORRESPONDENCE", DBEngineConstants.TYPE_BLOB,attachmentData.getBody()));
			inputParam.add(new InParameter("AV_CREATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
			inputParam.add(new InParameter("AV_UPDATE_USER ", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
			inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,vo.getCorrespTypeDescription()));
			inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING,"application/pdf"));
			inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,"I"));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_CORRESP_ATTACMNT");	
		} catch (Exception e) {
			logger.info("Exception in saveProtocolCorrespondence:" + e);	
		}		
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getCommitteeList() {
		ArrayList<HashMap<String, Object>> committeeList = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeList = dbEngine.executeProcedure("GET_IRB_COMMITTEE_DETAILS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getCommitteeList:" + e);	
		}
		return committeeList;
	}
	
	private void updateExpeditedApprovalCheckList(IRBActionsVO vo) {		
			List<String> expeditedApprovalCheckListCode = null;
			expeditedApprovalCheckListCode = vo.getExpeditedCheckListSelectedCode();
			if(expeditedApprovalCheckListCode != null)
			expeditedApprovalCheckListCode.forEach(checkListCode ->{
			try{
				ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
				inputParam.add(new InParameter("AV_PROTO_EXPEDITED_CHKLST_ID", DBEngineConstants.TYPE_INTEGER,null));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_EXPEDITED_REV_CHKLST_CODE", DBEngineConstants.TYPE_STRING,checkListCode));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
				inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,"I"));
				dbEngine.executeProcedure(inputParam,"UPD_IRB_EXPIDITED_CHKLST");	
			} catch (Exception e) {		
				logger.info("Exception in updateExpeditedApprovalCheckList:" + e);	
			}				
		});			
	}
	
	private ArrayList<HashMap<String, Object>> getProtoExpeditedChckList(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> expeditdChckListCategory = null;														
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			expeditdChckListCategory = dbEngine.executeProcedure(inputParam,"GET_IRB_PROTO_EXPTD_CHCKLST",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getProtoExpeditedChckList:" + e);	
		}
		return expeditdChckListCategory;
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getIRBAdminList() {
		ArrayList<HashMap<String, Object>> irbAdminList = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			irbAdminList = dbEngine.executeProcedure("GET_IRB_ADMINISTRATORS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getIRBAdminList:" + e);	
		}
		return irbAdminList;
	}
	
	@Override
	public void updateIRBAdmin(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ASSIGNEE_PERSON_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonID().toString()));
			inputParam.add(new InParameter("AV_ASSIGNEE_PERSON_NAME", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonName()));
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionNumber()));
			inputParam.add(new InParameter("AV_REVIEW_TYPE_CODE", DBEngineConstants.TYPE_STRING,"1"));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_ASSIGN_ADMIN");
		} catch (Exception e) {
			logger.info("Exception in updateIRBAdmin:" + e);	
		}	
	}
	
	@Override
	public void updateIRBAdminReviewers(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_ADMIN_REVIEWER_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAdminReviewerId()));
			inputParam.add(new InParameter("AV_ASSIGNEE_PERSON_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonID() == null ? null : submissionDetailvo.getPersonID().toString()));
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionNumber()));
			inputParam.add(new InParameter("AV_REVIEW_TYPE_CODE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getReviewTypeCode()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
			inputParam.add(new InParameter("AV_STATUS_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailvo.getStatusFlag()));
			inputParam.add(new InParameter("AV_STATUS_UPD_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getMarkCompletedBy()));
			inputParam.add(new InParameter("AV_COMPLETED_ON", DBEngineConstants.TYPE_DATE,submissionDetailvo.getCompleteTimeStamp() != null ? generateSqlDate(submissionDetailvo.getCompleteTimeStamp()): null));
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getAcType()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_ADMIN_REVIEWER");
		} catch (Exception e) {
			logger.info("Exception in updateIRBAdminReviewers:" + e);	
		}			
	}

	@Override
	public ArrayList<HashMap<String, Object>> fetchIRBAdminReviewers(SubmissionDetailVO submissionDetailvo) {
		ArrayList<HashMap<String, Object>> irbAdminReviewList = null;	
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			irbAdminReviewList = dbEngine.executeProcedure(inputParam,"GET_IRB_ADMIN_REVIEWERS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in fetchIRBAdminReviewers:" + e);	
		}	
		return irbAdminReviewList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> fetchCommitteeMembers(SubmissionDetailVO submissionDetailvo) {
		ArrayList<HashMap<String, Object>> irbCommitteeMembers = null;	
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getCommitteeId()));
			inputParam.add(new InParameter("AV_SCEDULE_DATE", DBEngineConstants.TYPE_DATE, generateSqlDate(submissionDetailvo.getSelectedDate())));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			irbCommitteeMembers = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_MEMBERS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in fetchCommitteeMembers:" + e);	
		}
		return irbCommitteeMembers;
	}

	@Override
	public ArrayList<HashMap<String, Object>> fetchSubmissionHistory(SubmissionDetailVO submissionDetailvo) {
		ArrayList<HashMap<String, Object>> submissionHistory = null;	
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			submissionHistory = dbEngine.executeProcedure(inputParam,"get_irb_action_history",outputparam);
		} catch (Exception e) {
			logger.info("Exception in fetchSubmissionHistory:" + e);	
		}
		return submissionHistory;
	}

	@Override
	public Integer updateIRBAdminComment(SubmissionDetailVO submissionDetailvo) {
		Integer commentId = null;
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getAcType()));
			inputParam.add(new InParameter("AV_COMMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getCommentId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			inputParam.add(new InParameter("AV_COMMENT_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailvo.getComment()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
			inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPublicFLag()));
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonID().toString()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_ADMIN_REVW_CMMNT",outputparam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				if(hmResult.get("ADMIN_REVW_COMMENT_ID") != null){
					commentId = Integer.parseInt(hmResult.get("ADMIN_REVW_COMMENT_ID").toString());	
				}		
			}
		} catch (Exception e) {
			logger.info("Exception in updateIRBAdminComment:" + e);	
		}
		return commentId;
	}

	@Override
	public void updateIRBAdminAttachment(SubmissionDetailVO submissionDetailvo, MultipartFile[] files) {
		try{
			if(submissionDetailvo.getAcType().equalsIgnoreCase("D")){
				ArrayList<InParameter> inputParam =  new ArrayList<InParameter>();
				inputParam.add(new InParameter("AV_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAttachmentId()));
				inputParam.add(new InParameter("AV_ADMIN_REVIEWER_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAdminReviewId()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailvo.getComment()));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, null));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
				inputParam.add(new InParameter("AV_COMMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getCommentId()));
				inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getAcType()));
				inputParam.add(new InParameter("AV_DATA", DBEngineConstants.TYPE_BLOB,new byte[0]));			
				inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPublicFLag()));
				inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonID().toString()));
				dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_ADMIN_RVW_ATTMNT");
			}else{
				for (int i = 0; i < files.length; i++) {
					ArrayList<InParameter> inputParam =  new ArrayList<InParameter>();
					inputParam.add(new InParameter("AV_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAttachmentId()));
					inputParam.add(new InParameter("AV_ADMIN_REVIEWER_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAdminReviewId()));
					inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
					inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
					inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
					inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
					inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailvo.getComment()));
					inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,files[i].getOriginalFilename()));
					inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
					inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
					inputParam.add(new InParameter("AV_COMMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getCommentId()));
					inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getAcType()));
					inputParam.add(new InParameter("AV_DATA", DBEngineConstants.TYPE_BLOB,files[i].getBytes()));			
					inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPublicFLag()));
					inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getPersonID().toString()));
					dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_ADMIN_RVW_ATTMNT");
				}
			}
		} catch (Exception e) {
			logger.info("Exception in updateIRBAdminAttachment:" + e);	
		}		
	}

	@Override
	public HashMap<String, Object> fetchCommitteeVotingDetails(Integer submissionId) {
		HashMap<String, Object> committeeVotingDetails = null;
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionId));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> votingDetails  = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_VOTING_DTLS",outputparam);
			if(votingDetails != null){
				committeeVotingDetails = votingDetails.get(0);
			}
		} catch (Exception e) {
			logger.info("Exception in updateCommitteeVoting:" + e);	
		}	
		return committeeVotingDetails;
	}

	@Override
	public void updateSubmissionDetail(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,String.valueOf(submissionDetailvo.getSubmissionTypeCode())));
			inputParam.add(new InParameter("AV_PROTOCOL_REVW_TYPE_CODE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getReviewTypeCode()));
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSceduleId()));
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,submissionDetailvo.getCommitteeId()));
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,submissionDetailvo.getSubmissionQualifierCode()));
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,submissionDetailvo.getComment()));
			inputParam.add(new InParameter("AV_YES_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getYesVotingCount()));
			inputParam.add(new InParameter("AV_NO_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getNoVotingCount()));
			inputParam.add(new InParameter("AV_ABSTAINER_COUNT", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getAbstainCount()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
			dbEngine.executeProcedure(inputParam,"upd_irb_submission_details");
		} catch (Exception e) {
			logger.info("Exception in updateSubmissionDetail:" + e);	
		}		
	}
	
	public void updateReviewComments(IRBActionsVO vo){
		List<IRBCommitteeReviewerComments> irbActionsReviewerComments=null;
		irbActionsReviewerComments =vo.getIrbActionsReviewerComments();
		if(irbActionsReviewerComments != null)
			irbActionsReviewerComments.forEach(irbActionsReviewerCommentList ->{
				try{
				ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
				inputParam.add(new InParameter("AV_COMM_SCHEDULE_MINUTES_ID", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_STRING,vo.getSubmissionId()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSubmissionNumber()));
				inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,irbActionsReviewerCommentList.getComments()));
				inputParam.add(new InParameter("AV_INCLUDE_IN_MINUTES", DBEngineConstants.TYPE_STRING,irbActionsReviewerCommentList.getFlag()));
				inputParam.add(new InParameter("AV_PROTOCOL_CONTINGENCY_CODE", DBEngineConstants.TYPE_STRING,irbActionsReviewerCommentList.getContingencyCode()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
				inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,"I"));	
				inputParam.add(new InParameter("AV_INCLUDE_IN_LETTER", DBEngineConstants.TYPE_STRING,irbActionsReviewerCommentList.getLetterFlag()));			
			    dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_COMMENTS");
			} catch (Exception e) {
				logger.info("Exception in updateIRBAdminReviewers:" + e);	
			}			
	   });
	}
	
	public void protocolActionReviewerAttachments(MultipartFile[] files,IRBActionsVO vo) {		
		try {					
			ArrayList<InParameter> inputParam = null;
			for (int i = 0; i < files.length; i++) {			
				inputParam = new ArrayList<>();
				inputParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,null));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmission_Id()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getProtocolSubmissionStatuses().getSubmissionNumber()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,vo.getAttachmentDescription()));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,files[i].getOriginalFilename()));
				inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
				inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,vo.getPublicFlag()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
				inputParam.add(new InParameter("AV_ATTACHMENT", DBEngineConstants.TYPE_BLOB,files[i].getBytes()));
				inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,"I"));			
				dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_ATTACHMENTS");
			}
		} catch (Exception e) {
			logger.info("Exception in protocolActionReviewerAttachments method" + e);
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> getScheduleDates(String committeeId) {
		ArrayList<HashMap<String, Object>> scheduleDates = null;														
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,committeeId));
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			scheduleDates=dbEngine.executeProcedure(inputParam,"GET_IRB_SCHEDULE_DATE",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getScheduleDates:" + e);	
		}
		return scheduleDates;
	}
	
	@Override
	public void updateCommitteeReviewComments(SubmissionDetailVO submissionDetailVO){	
				try{
				ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
				inputParam.add(new InParameter("AV_COMM_SCHEDULE_MINUTES_ID", DBEngineConstants.TYPE_STRING,submissionDetailVO.getIrbCommitteeReviewerComments().getCommitteeScheduleMinutesId()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
				inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,submissionDetailVO.getIrbCommitteeReviewerComments().getComments()));
				inputParam.add(new InParameter("AV_INCLUDE_IN_MINUTES", DBEngineConstants.TYPE_STRING,submissionDetailVO.getIrbCommitteeReviewerComments().getFlag()));
				inputParam.add(new InParameter("AV_PROTOCOL_CONTINGENCY_CODE", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
				inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));			
				inputParam.add(new InParameter("AV_INCLUDE_IN_LETTER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getIrbCommitteeReviewerComments().getLetterFlag()));			
				dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_COMMENTS");
			} catch (Exception e) {
				logger.info("Exception in updateIRBAdminReviewers:" + e);	
			}				   
	}
	
	@Override
	public void updateCommitteeReviewerAttachments(MultipartFile[] files,SubmissionDetailVO submissionDetailVO) {		
		try {					
			if(submissionDetailVO.getAcType().equalsIgnoreCase("I")){
				ArrayList<InParameter> inputParam = null;
				for (int i = 0; i < files.length; i++) {			
					inputParam = new ArrayList<>();
					inputParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getReviewerAttachmentId()));
					inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
					inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
					inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
					inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
					inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
					inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAttachmentDescription()));
					inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,files[i].getOriginalFilename()));
					inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
					inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailVO.getFlag()));
					inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
					inputParam.add(new InParameter("AV_ATTACHMENT", DBEngineConstants.TYPE_BLOB,files[i].getBytes()));
					inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));			
					dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_ATTACHMENTS");
				}
			}else if (submissionDetailVO.getAcType().equalsIgnoreCase("D")) {
				ArrayList<InParameter> inputParam = new ArrayList<>();
				inputParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getReviewerAttachmentId()));
				inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
				inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
				inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
				inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
				inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAttachmentDescription()));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,null));
				inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, null));
				inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailVO.getFlag()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
				inputParam.add(new InParameter("AV_ATTACHMENT", DBEngineConstants.TYPE_BLOB,new byte[0]));
				inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));			
				dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_ATTACHMENTS");
			}else {
				if(files.length != 0){
					ArrayList<InParameter> inputParam = null;
					for (int i = 0; i < files.length; i++) {			
						inputParam = new ArrayList<>();
						inputParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getReviewerAttachmentId()));
						inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
						inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
						inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
						inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
						inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
						inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAttachmentDescription()));
						inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,files[i].getOriginalFilename()));
						inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
						inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailVO.getFlag()));
						inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
						inputParam.add(new InParameter("AV_ATTACHMENT", DBEngineConstants.TYPE_BLOB,files[i].getBytes()));
						inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));			
						dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_ATTACHMENTS");
					}
				}else{
					ArrayList<InParameter> inputParam = new ArrayList<>();
					inputParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getReviewerAttachmentId()));
					inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
					inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
					inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
					inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
					inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
					inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAttachmentDescription()));
					inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,null));
					inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING, null));
					inputParam.add(new InParameter("AV_PUBLIC_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailVO.getFlag()));
					inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
					inputParam.add(new InParameter("AV_ATTACHMENT", DBEngineConstants.TYPE_BLOB,new byte[0]));
					inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));			
					dbEngine.executeProcedure(inputParam,"UPD_IRB_REVIEW_ATTACHMENTS");
				}
			}
			
		} catch (Exception e) {
			logger.info("Exception in updateCommitteeReviewerAttachments method" + e);
		}
	}

	@Override
	public void updateIRBAdminCheckList(SubmissionDetailVO submissionDetailvo) {
		try{
			for(AdminCheckListDetail checkListDetail : submissionDetailvo.getIrbAdminCheckList()){
				if(checkListDetail.getActype() != null){
					ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
					inputParam.add(new InParameter("AV_ADMIN_CHKLST_ID", DBEngineConstants.TYPE_INTEGER,checkListDetail.getAdminCheckListId()));
					inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getProtocolId()));
					inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
					inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getProtocolNumber()));
					inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSequenceNumber()));
					inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionNumber()));
					inputParam.add(new InParameter("AV_ADMIN_REV_CHKLST_CODE", DBEngineConstants.TYPE_INTEGER,checkListDetail.getAdminRevChecklistCode()));
					inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailvo.getUpdateUser()));
					inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,checkListDetail.getActype()));
					inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,checkListDetail.getUserDescription()));
					inputParam.add(new InParameter("av_status_flag", DBEngineConstants.TYPE_STRING,checkListDetail.getStatusFlag() == true ? "Y":"N"));					
					dbEngine.executeProcedure(inputParam,"UPD_IRB_ADMIN_CHECKLIST");
				}
			} 
		} catch (Exception e) {
			logger.info("Exception in updateIRBAdminCheckList method" + e);
		}		
	}
	
	@Override
	public ArrayList<HashMap<String, Object>> getCommitteeReviewerCommentsandAttachment(SubmissionDetailVO submissionDetailVO){
		ArrayList<HashMap<String, Object>> committeeReviewerCommentsandAttachment = null;
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getPersonID()));
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeReviewerCommentsandAttachment=dbEngine.executeProcedure(inputParam,"GET_IRB_COMITE_RVWR_COMM_ATMNT",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getCommitteeReviewerCommentsandAttachment:" + e);
		}
	    return committeeReviewerCommentsandAttachment;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getSubmissionCommitteeList(SubmissionDetailVO vo) {
		ArrayList<HashMap<String, Object>> committeeList = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeList = dbEngine.executeProcedure("GET_IRB_COMMITTEE_DETAILS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getCommitteeList:" + e);	
		}
		return committeeList;
	}

	@Override
	public void updateCommitteeReviewers(SubmissionDetailVO submissionDetailVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PROTOCOL_REVIEWER_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolReviewerId()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getProtocolId()));
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSequenceNumber()));
			inputParam.add(new InParameter("AV_SUBMISSION_NUMBER", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSubmissionNumber()));
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,String.valueOf(submissionDetailVO.getPersonID())));
			inputParam.add(new InParameter("AV_REVIEWER_TYPE_CODE", DBEngineConstants.TYPE_STRING,submissionDetailVO.getCommitteeReviewerTypeCode()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,submissionDetailVO.getUpdateUser()));
			inputParam.add(new InParameter("AV_DUE_DATE", DBEngineConstants.TYPE_DATE,generateSqlDate(submissionDetailVO.getCommitteeMemberDueDate())));
			inputParam.add(new InParameter("AV_ASSIGNED_DATE", DBEngineConstants.TYPE_DATE,generateSqlDate(submissionDetailVO.getCommitteeMemberAssignedDate())));			
			inputParam.add(new InParameter("AV_RECOMEND_ACTION", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getSelectedRecommendedAction()));
			inputParam.add(new InParameter("AV_PROTOCOL_ONLN_RVW_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailVO.getCommitteeMemberOnlineReviewerId()));
			inputParam.add(new InParameter("AV_PROTO_ONLN_RVW_STATUS_CODE", DBEngineConstants.TYPE_STRING,submissionDetailVO.getCommitteeReviewerStatusCode()));		
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,submissionDetailVO.getAcType()));
			inputParam.add(new InParameter("AV_STATUS_FLAG", DBEngineConstants.TYPE_STRING,submissionDetailVO.getStatusFlag()));		
			inputParam.add(new InParameter("AV_COMPLETED_ON", DBEngineConstants.TYPE_DATE,submissionDetailVO.getCompleteTimeStamp() != null ? generateSqlDate(submissionDetailVO.getCompleteTimeStamp()): null));
			inputParam.add(new InParameter("AV_COMPLETED_BY", DBEngineConstants.TYPE_STRING,submissionDetailVO.getMarkCompletedBy()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_COMMITTE_REVIEWERS");
		} catch (Exception e) {
		logger.info("Exception in updateCommitteeReviewers:" + e);	
		}	
	}

	@Override
	public ArrayList<HashMap<String, Object>> fetchCommitteeReviewers(SubmissionDetailVO vo) {
		ArrayList<HashMap<String, Object>> committeeReviwer = null;
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,vo.getSubmissionId()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeReviwer = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_REVIEWRES",outputparam);
		}catch (Exception e) {
			logger.info("Exception in fetchsubmissionBasicDetail: " + e);
		}
		return committeeReviwer;
	}
	
	
	public ArrayList<HashMap<String, Object>> getCommitteeReviewersComments(Integer submissionId) {
		ArrayList<HashMap<String, Object>> committeeReviwer = null;
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionId));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeReviwer = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_REVW_COMMNTS",outputparam);
		}catch (Exception e) {
			logger.info("Exception in fetchsubmissionBasicDetail: " + e);
		}
		return committeeReviwer;
	}

	@Override
	public ResponseEntity<byte[]> downloadCommitteeFileData(String fileDataId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_REVIEWER_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,Integer.parseInt(fileDataId)));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "get_irb_download_comm_rvw_att",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("DATA");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(hmResult.get("MIME_TYPE").toString()));
				String filename = hmResult.get("FILE_NAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadFileData method:" + e);
		}
		return attachmentData;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getPastSubmission(SubmissionDetailVO vo) {
		ArrayList<HashMap<String, Object>> pastsubmissionList = null;	
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			pastsubmissionList = dbEngine.executeProcedure(inputParam,"get_irb_past_submision_history",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getPastSubmission method:" + e);
		}
		return pastsubmissionList;
	}
	
	private void updateRiskLevelDetails(IRBActionsVO vo) {   
        try{
            ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
            inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
            inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
            inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
            inputParam.add(new InParameter("AV_RISK_LEVEL_CODE", DBEngineConstants.TYPE_STRING,vo.getRiskLevelDetail().getRiskLevelCode()));
            inputParam.add(new InParameter("AV_RISK_LVL_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getRiskLevelDetail().getRiskLevelComment()));
            inputParam.add(new InParameter("AV_RISK_LVL_DATE_ASSIGNED", DBEngineConstants.TYPE_DATE,generateSqlDate(vo.getRiskLevelDetail().getRiskLevelDateAssigned())));
            inputParam.add(new InParameter("AV_FDA_RISK_LEVEL_CODE", DBEngineConstants.TYPE_STRING,vo.getRiskLevelDetail().getFdaRiskLevelCode()));
            inputParam.add(new InParameter("AV_FDA_LVL_COMMENTS", DBEngineConstants.TYPE_STRING,vo.getRiskLevelDetail().getFdaRiskLevelComment()));
            inputParam.add(new InParameter("AV_FDA_LVL_DATE_ASSIGNED", DBEngineConstants.TYPE_DATE,generateSqlDate(vo.getRiskLevelDetail().getFdariskLevelDateAssigned())));       
            inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,vo.getUpdateUser()));
            dbEngine.executeProcedure(inputParam,"upd_irb_protocol_risk_levels");        
          } catch (Exception e) {                
             logger.info("Exception in updateRiskLevelDetails:" + e);        
          }         	   	
		}
	
	public void updateSubmissionDetail(IRBActionsVO irbActionsVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,irbActionsVO.getProtocolSubmissionStatuses().getSubmission_Id()));
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_CODE", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getSubmissionTypeCode()));
			inputParam.add(new InParameter("AV_PROTOCOL_REVW_TYPE_CODE", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getProtocolReviewTypeCode()));
			inputParam.add(new InParameter("AV_SCHEDULE_ID", DBEngineConstants.TYPE_INTEGER,irbActionsVO.getProtocolSubmissionStatuses().getScheduleId()));
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getCommitteeId()));
			inputParam.add(new InParameter("AV_SUBMISSION_TYPE_QUAL_CODE", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getSubmissionTypeQualCode()));
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getVotingComments()));
			inputParam.add(new InParameter("AV_YES_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,irbActionsVO.getProtocolSubmissionStatuses().getYesVoteCount()));
			inputParam.add(new InParameter("AV_NO_VOTE_COUNT", DBEngineConstants.TYPE_INTEGER,irbActionsVO.getProtocolSubmissionStatuses().getNoVoteCount()));
			inputParam.add(new InParameter("AV_ABSTAINER_COUNT", DBEngineConstants.TYPE_INTEGER,irbActionsVO.getProtocolSubmissionStatuses().getAbstainerCount()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,irbActionsVO.getProtocolSubmissionStatuses().getUpdateUser()));
			dbEngine.executeProcedure(inputParam,"upd_irb_submission_details");
		} catch (Exception e) {
			logger.info("Exception in updateSubmissionDetail:" + e);	
		}		
	}
	
	private Integer getIsCloseActionRequested(IRBActionsVO vo){
		ArrayList<HashMap<String, Object>> isRequested = null;	
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,vo.getProtocolId()));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,vo.getSequenceNumber()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_INTEGER));
			isRequested = dbEngine.executeFunction(inputParam,"fn_irb_is_rqst_close_perform",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getPastSubmission method:" + e);
		}
		return  Integer.parseInt(isRequested.get(0).get("resultset").toString());	
	}
}
