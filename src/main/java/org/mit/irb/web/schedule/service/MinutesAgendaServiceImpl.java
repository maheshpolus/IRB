package org.mit.irb.web.schedule.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.pojo.IRBAdminReviewerComment;
import org.mit.irb.web.IRBProtocol.pojo.IRBCommitteeReviewerComments;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.ProtocolSubmission;
import org.mit.irb.web.committee.pojo.ScheduleStatus;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.correspondence.dao.DocxDocumentMergerAndConverter;
import org.mit.irb.web.schedule.dao.ScheduleDao;
import org.mit.irb.web.schedule.vo.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Transactional
@Service(value = "minutesAgendaService")
public class MinutesAgendaServiceImpl implements MinutesAgendaService {

	protected static Logger logger = Logger.getLogger(ScheduleServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;
	
	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
	private ScheduleService scheduleService;
	
	DBEngine dbEngine;
	
	MinutesAgendaServiceImpl() {
		dbEngine = new DBEngine();
	}
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	
	public void getAgendaInputData(ScheduleVo vo){
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
		vo = scheduleService.loadScheduledProtocols(vo);				
	}
	
	private List<ProtocolSubmission> allProtocolDetails(ScheduleVo vo){
		vo = scheduleService.loadScheduledProtocols(vo);
		for (ProtocolSubmission submission : vo.getSubmittedProtocols()) {
			List<IRBAdminReviewerComment> adminComment = loadAdminCommentsInProtocol(submission.getSubmissionId());
			submission.setAdminComments(adminComment);
			submission.setCommitteePriRev(loadProtocolPrimaryCommitteeReviewers(submission.getSubmissionId()));
			submission.setCommitteeSecRev(loadProtocolSecondaryCommitteeReviewers(submission.getSubmissionId()));
		}
		if( vo.getSubmittedProtocols() != null)
		return  vo.getSubmittedProtocols();
		return null;		
	}
	
	private String loadProtocolPrimaryCommitteeReviewers(Integer submissionId) {
		ArrayList<HashMap<String, Object>> committeeReviwer = null;
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionId));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeReviwer = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_REVIEWRES",outputparam);
		}catch (Exception e) {
			logger.info("Exception in loadProtocolCommitteeReviewers: " + e);
		}
		//for getting committee reviewers in a protocol as a string
		String primaryReviewers = null;
		if(committeeReviwer != null && !committeeReviwer.isEmpty()){
			for (HashMap<String, Object> hashMap : committeeReviwer) {
				if(hashMap.get("FULL_NAME").toString().equals("1")){
					if(primaryReviewers == null || primaryReviewers.isEmpty()){
						primaryReviewers = hashMap.get("FULL_NAME") ==  null ? null : hashMap.get("FULL_NAME").toString();
					}else{
						primaryReviewers = primaryReviewers.concat(hashMap.get("FULL_NAME") ==  null ? "" :", "+hashMap.get("FULL_NAME").toString());
					}	
				}							
			}
		}	
		return primaryReviewers;
	}
	
	private String loadProtocolSecondaryCommitteeReviewers(Integer submissionId) {
		ArrayList<HashMap<String, Object>> committeeReviwer = null;
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionId));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeReviwer = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_REVIEWRES",outputparam);
		}catch (Exception e) {
			logger.info("Exception in loadProtocolCommitteeReviewers: " + e);
		}
		//for getting committee reviewers in a protocol as a string
		String secondaryReviewers = null;
		if(committeeReviwer != null && !committeeReviwer.isEmpty()){
			for (HashMap<String, Object> hashMap : committeeReviwer) {
				if(hashMap.get("FULL_NAME").toString().equals("2")){
					if(secondaryReviewers == null || secondaryReviewers.isEmpty()){
						secondaryReviewers = hashMap.get("FULL_NAME") ==  null ? null : hashMap.get("FULL_NAME").toString();
					}else{
						secondaryReviewers = secondaryReviewers.concat(hashMap.get("FULL_NAME") ==  null ? "" :", "+hashMap.get("FULL_NAME").toString());
					}	
				}							
			}
		}	
		return secondaryReviewers;
	}

	@SuppressWarnings("unchecked")
	private List<IRBAdminReviewerComment> loadAdminCommentsInProtocol(Integer submissionId) {
		List<IRBAdminReviewerComment> comments = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			@SuppressWarnings("deprecation")
			Criteria criteria = session.createCriteria(IRBAdminReviewerComment.class);
			criteria.add(Restrictions.eq("submissionId",submissionId));
			comments = criteria.list();
		} catch (Exception e) {
			logger.info("Exception in loadAdminCommentsInProtocol:" + e);	
		}
		return comments;
	}

	private List<CommitteeScheduleMinutes> setMinutes(List<CommitteeScheduleMinutes> scheduleMinutes){				
		List<CommitteeScheduleMinutes> minutes = new ArrayList<>();		
		for(CommitteeScheduleMinutes minute: scheduleMinutes){			
			minutes.add(new CommitteeScheduleMinutes(minute.getMinuteEntry()));
		}
		return minutes;		
	}
	
	private List<ProtocolSubmission> setProtocolDetailsFullIntial(List<ProtocolSubmission> submissions){				
		List<ProtocolSubmission> fullIntial = new ArrayList<>();
		for (ProtocolSubmission submission : submissions) {
			if(submission.getSubmissionTypeCode().equals("100")){
				fullIntial.add(new ProtocolSubmission(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription()));
			}
		}		
		return fullIntial;		
	}
	
	private List<IRBAdminReviewerComment> adminComments(List<IRBAdminReviewerComment> adminComments){				
		List<IRBAdminReviewerComment> adminComment = new ArrayList<>();
		for (IRBAdminReviewerComment comment : adminComments) {
				adminComment.add(new IRBAdminReviewerComment(comment.getComment()));
			}				
		return adminComment;		
	}
	
	@Override
	public ScheduleVo generateAgenda(ScheduleVo vo){
		ResponseEntity<byte[]> attachmentData = null;
		try{
			byte[] data = getTemplateData(vo);
			byte[] mergedOutput = mergePlaceHolders(data,vo);
			String generatedFileName = "agenda"+".pdf";
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
	
	public byte[] getTemplateData(ScheduleVo vo) {
		byte[] attachmentData =null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();			
			inParam.add(new InParameter("AV_LETTER_TEMPLATE_TYPE_CODE", DBEngineConstants.TYPE_STRING,"26"));
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
	
	public byte[] mergePlaceHolders(byte[] data, ScheduleVo vo) {
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			IContext context = report.createContext();
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata1 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata2 = report.createFieldsMetadata();
			/*fieldsMetadata.load("CommentList", CommitteeScheduleMinutes.class, true);
			List<CommitteeScheduleMinutes> scheduleMinutes = scheduleDao.getScheduleMinutes(vo);
			List<CommitteeScheduleMinutes> CommentList = setMinutes(scheduleMinutes);
			context.put("CommentList", CommentList);*/
			
			List<ProtocolSubmission> submissions = allProtocolDetails(vo);
			for (ProtocolSubmission submission : submissions) {
				switch (submission.getSubmissionTypeCode()) {
				case "100":
					fieldsMetadata1.load("fullIntialApp", ProtocolSubmission.class, true);
					fieldsMetadata2.load("adminComList", IRBAdminReviewerComment.class, true);
					List<ProtocolSubmission> fullIntialApp = setProtocolDetailsFullIntial(submissions);
					List<IRBAdminReviewerComment> adminComList =  adminComments(submission.getAdminComments());
					context.put("fullIntialApp", fullIntialApp);
					context.put("adminComList", adminComList);					
					break;				
				}
			}
			context = setPlaceHolderData(context,vo);
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}	
	
	private IContext setPlaceHolderData(IContext context,ScheduleVo vo) {
		try{			
		/*	java.util.Date date = new java.util.Date();  
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); 
			String code = null;				
			context.put("Date",formatter.format(date));
			context.put("ExpeditedCategorySelection",code == null ? "" :code);
			context.put("ProtocolNumber",vo.getProtocolNumber());
		    date = formatter.parse(vo.getActionDate());
			context.put("ActionDate",formatter.format(date));*/
			context.put("scheduleMeetingDate",vo.getCommitteeSchedule().getMeetingDate()  == null ? "" : vo.getCommitteeSchedule().getMeetingDate());
			context.put("scheduleMeetingTime",vo.getCommitteeSchedule().getTime() == null ? "" : vo.getCommitteeSchedule().getTime());
			context.put("scheduleLocation",vo.getCommitteeSchedule().getPlace() == null ? "" : vo.getCommitteeSchedule().getPlace());
			context.put("previousMeetingDate",null == null ? "" : null);
			context.put("nextMeetingDate",null == null ? "" : null);
			context.put("meetingPlace",null == null ? "" : null);			
		}catch (Exception e) {
			logger.info("Exception in setPlaceHolderData method:" + e);
		}
		return context;
	}	

	private void saveProtocolCorrespondence(ScheduleVo vo, ResponseEntity<byte[]> attachmentData) {
		try{
		    ArrayList<InParameter> inputParam =  new ArrayList<InParameter>();	
			inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER,null));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			inputParam.add(new InParameter("AV_SEQUENCE_NUMBER", DBEngineConstants.TYPE_INTEGER,null));
			inputParam.add(new InParameter("AV_PROTOCOL_ACTION_ID", DBEngineConstants.TYPE_INTEGER,null));
			inputParam.add(new InParameter("AV_PROTO_CORRESP_TYPE_CODE", DBEngineConstants.TYPE_STRING,null));
			inputParam.add(new InParameter("AV_FINAL_FLAG", DBEngineConstants.TYPE_STRING,"Y"));
			inputParam.add(new InParameter("AV_CORRESPONDENCE", DBEngineConstants.TYPE_BLOB,attachmentData.getBody()));
			inputParam.add(new InParameter("AV_CREATE_USER", DBEngineConstants.TYPE_STRING,"anand"));
			inputParam.add(new InParameter("AV_UPDATE_USER ", DBEngineConstants.TYPE_STRING,"anand"));
			inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,null));
			inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING,"application/pdf"));
			inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING,"I"));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PROTO_CORRESP_ATTACMNT");	
		} catch (Exception e) {
			logger.info("Exception in saveProtocolCorrespondence:" + e);	
		}		
	}
}
