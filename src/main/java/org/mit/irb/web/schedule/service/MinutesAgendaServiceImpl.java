package org.mit.irb.web.schedule.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.IRBAdminReviewerComment;
import org.mit.irb.web.IRBProtocol.pojo.IRBCommitteeReviewerComments;
import org.mit.irb.web.IRBProtocol.pojo.IRBFileData;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleActItems;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinuteDoc;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.ProtocolSubmission;
import org.mit.irb.web.committee.pojo.ScheduleAgenda;
import org.mit.irb.web.committee.pojo.ScheduleStatus;
import org.mit.irb.web.committee.schedule.Time24HrFmt;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.correspondence.dao.DocxDocumentMergerAndConverter;
import org.mit.irb.web.schedule.dao.ScheduleDao;
import org.mit.irb.web.schedule.pojo.MinutesEntry;
import org.mit.irb.web.schedule.pojo.ProtocolDetails;
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
	
	private static final String COLON = ":";
	
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
	
	private List<ProtocolDetails> loadScheduleIdsForAgenda(ScheduleVo vo){
		vo = scheduleService.loadScheduleIdsForAgenda(vo);
		List<ProtocolDetails> scheduleDetails = new ArrayList<>();
		for (HashMap<String, Object> id : vo.getAgendaScheduleIds()) {
			ProtocolDetails details = new ProtocolDetails();
			details.setPreviousSchMeetingDate((id.get("SCHEDULED_DATE") == null? null :id.get("SCHEDULED_DATE").toString()));
			details.setNextMeetingPlace((id.get("PLACE") == null? null :id.get("PLACE").toString()));
			scheduleDetails.add(details);
		}
		return scheduleDetails;		
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
				if(hashMap.get("REVIEWER_TYPE_CODE").toString().equals("1")){
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
				if(hashMap.get("REVIEWER_TYPE_CODE").toString().equals("2")){
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

	private List<MinutesEntry> setMinutes(List<CommitteeScheduleMinutes> scheduleMinutes){				
		List<MinutesEntry> minutes = new ArrayList<>();		
		for(CommitteeScheduleMinutes minute: scheduleMinutes){			
			minutes.add(new MinutesEntry(null,minute.getMinuteEntry()));
		}
		return minutes;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsFullIntial(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> fullIntial = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("1"))
			if(submission.getSubmissionTypeCode().equals("100")){
				fullIntial.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return fullIntial;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsfullAmd(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> fullAmed = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("1"))
			if(submission.getSubmissionTypeCode().equals("102")){
				fullAmed.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return fullAmed;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsfullCon(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> fullCon = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("1"))
			if(submission.getSubmissionTypeCode().equals("101")){
				fullCon.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return fullCon;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsfullRes(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> fullRes = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("1"))
			if(submission.getSubmissionTypeCode().equals("103")){
				fullRes.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return fullRes;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsExpIntial(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> expIntial = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("2"))
			if(submission.getSubmissionTypeCode().equals("100")){
				expIntial.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return expIntial;		
	}
		
	private List<ProtocolDetails> setProtocolDetailsExpAmd(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> expAmed = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("2"))
			if(submission.getSubmissionTypeCode().equals("102")){
				expAmed.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return expAmed;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsExpCon(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> expCon = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("2"))
			if(submission.getSubmissionTypeCode().equals("101")){
				expCon.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return expCon;		
	}
	
	private List<ProtocolDetails> setProtocolDetailsExpRes(List<ProtocolSubmission> submissions){				
		List<ProtocolDetails> expRes = new ArrayList<>();
		
		for (ProtocolSubmission submission : submissions) {
			if(submission.getProtocolReviewTypeCode().equals("2"))
			if(submission.getSubmissionTypeCode().equals("103")){
				expRes.add(new ProtocolDetails(submission.getProtocolNumber(),submission.getPersonName(),submission.getProtocolTitle(),submission.getCommitteePriRev(),submission.getCommitteeSecRev(),submission.getSubmissionTypeDescription(),adminComments(submission.getAdminComments()),submission.getExpirationDate() ==  null ? "" : submission.getExpirationDate(),submission.getYesVoteCount(),submission.getNoVoteCount(),submission.getAbstainerCount(),minuteComments(scheduleDao.getProtocolCommitteeComments(submission.getSubmissionId(),submission.getCommitteeSchedule().getScheduleId()))));
			}
		}		
		return expRes;		
	}
	
	private String  adminComments(List<IRBAdminReviewerComment> adminComments){			
		String adminComment = null;
			for (IRBAdminReviewerComment object : adminComments) {
					if(adminComment == null || adminComment.isEmpty()){
						adminComment = object.getComment() ==  null ? null : object.getComment() ;
					}else{
						adminComment = adminComment.concat(object.getComment() ==  null ? "" :"\n "+object.getComment());
					}	
				}														
		return adminComment;		
	}
	
	private String  minuteComments(List<CommitteeScheduleMinutes> adminComments){			
		String minuteComment = null;
			for (CommitteeScheduleMinutes object : adminComments) {
					if(minuteComment == null || minuteComment.isEmpty()){
						minuteComment = object.getMinuteEntry() ==  null ? null : object.getMinuteEntry() ;
					}else{
						minuteComment = minuteComment.concat(object.getMinuteEntry() ==  null ? "" :"\n "+object.getMinuteEntry());
					}	
				}														
		return minuteComment;		
	}
	
	@Override
	public ScheduleVo generateAgenda(ScheduleVo vo){
		ResponseEntity<byte[]> attachmentData = null;
		try{
			vo.setTemplateCode("4");
			byte[] data = getTemplateData(vo);
			byte[] mergedOutput = mergePlaceHoldersAgenda(data,vo);
			String generatedFileName = "agenda"+".pdf";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.setContentDispositionFormData(generatedFileName, generatedFileName);
			headers.setContentLength(mergedOutput.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(mergedOutput, headers, HttpStatus.OK);	
			boolean flag = saveScheduleAgenda(vo,attachmentData);
			vo.setFlag(flag);
		}catch (Exception e) {
			vo.setFlag(false);
			logger.error("Exception in generateAgenda"+ e.getMessage());
		}
		return vo;
	}
	
	private boolean saveScheduleAgenda(ScheduleVo vo, ResponseEntity<byte[]> attachmentData) {
		try {
			Date date = new Date(System.currentTimeMillis());		
			ScheduleAgenda agenda = new ScheduleAgenda();	
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
			ScheduleAgenda prevDetails = null;
			prevDetails = getPrevAgendaDetails(vo.getScheduleId());
			if(prevDetails == null){
				agenda.setAgendaNumber(1);
			}else{
				agenda.setAgendaNumber(prevDetails.getAgendaNumber()+1);
			}
			agenda.setScheduleId(vo.getScheduleId());
			agenda.setAgendaName(vo.getScheduleId());
			agenda.setCommitteeSchedule(committeeSchedule);
			agenda.setPdfStore(attachmentData.getBody());
			agenda.setCreateUser(vo.getUpdateUser());
			agenda.setCreateTimestamp(date);
			hibernateTemplate.saveOrUpdate(agenda);		
			vo.setFlag(true);
		} catch (Exception e) {
			vo.setFlag(false);
			logger.error("Exception in saveScheduleAgenda"+ e.getMessage());
		}
		return vo.isFlag();	
	}

	@Override
	public ScheduleAgenda getPrevAgendaDetails(Integer scheduleId) {
		ScheduleAgenda attachment = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ScheduleAgenda> criteria = builder.createQuery(ScheduleAgenda.class);
			Root<ScheduleAgenda> attachmentRoot=criteria.from(ScheduleAgenda.class);	
			criteria.multiselect(attachmentRoot.get("scheduleAgendaId"),attachmentRoot.get("scheduleId")
					,attachmentRoot.get("agendaNumber"),attachmentRoot.get("createTimestamp"),attachmentRoot.get("createUser"));
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),scheduleId));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleAgendaId")));
			if( session.createQuery(criteria).getResultList() != null && ! session.createQuery(criteria).getResultList().isEmpty())
			attachment = (ScheduleAgenda) session.createQuery(criteria).getResultList().get(0);							
			/*Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"from ScheduleAgenda p where p.scheduleId =:scheduleId order by p.updateTimestamp DESC");
			queryGeneral.setInteger("scheduleId",scheduleId);			
			if(queryGeneral.list() != null && !queryGeneral.list().isEmpty())
			attachment =  (ScheduleAgenda) queryGeneral.list().get(0);*/
		} catch (Exception e) {
			logger.info("Exception in getPrevAgendaDetails method:" + e);
		}
	return attachment;	
	}

	public byte[] getTemplateData(ScheduleVo vo) {
		byte[] attachmentData =null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();			
			inParam.add(new InParameter("AV_LETTER_TEMPLATE_TYPE_CODE", DBEngineConstants.TYPE_STRING,vo.getTemplateCode()));
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
	
	public byte[] mergePlaceHoldersAgenda(byte[] data, ScheduleVo vo) {
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			IContext context = report.createContext();
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata1 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata2 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata3 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata4 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata5 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata6 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata7 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata8 = report.createFieldsMetadata();
			fieldsMetadata.load("CommentList", MinutesEntry.class, true);
			List<CommitteeScheduleMinutes> scheduleMinutes = scheduleDao.getScheduleMinutes(vo);
			List<MinutesEntry> CommentList = setMinutes(scheduleMinutes);
			context.put("CommentList", CommentList);			
			List<ProtocolSubmission> submissions = allProtocolDetails(vo);						
			fieldsMetadata1.load("fullIntialApp", ProtocolDetails.class, true);
		    fieldsMetadata2.load("ExpIntial", ProtocolDetails.class, true);				
			fieldsMetadata3.load("fullCon", ProtocolDetails.class, true);
			fieldsMetadata4.load("ExpCon", ProtocolDetails.class, true);								
			fieldsMetadata5.load("fullAmd", ProtocolDetails.class, true);
			fieldsMetadata6.load("ExpAmd", ProtocolDetails.class, true);					
			fieldsMetadata7.load("fullRes", ProtocolDetails.class, true);	
			fieldsMetadata8.load("ExpRes", ProtocolDetails.class, true);
			fieldsMetadata.load("otherList", MinutesEntry.class, true);										
			List<ProtocolDetails> fullIntialApp = setProtocolDetailsFullIntial(submissions);
			List<ProtocolDetails> fullAmd = setProtocolDetailsfullAmd(submissions);
			List<ProtocolDetails> fullCon = setProtocolDetailsfullCon(submissions);
			List<ProtocolDetails> fullRes = setProtocolDetailsfullRes(submissions);
			List<ProtocolDetails> ExpAmd = setProtocolDetailsExpAmd(submissions);
			List<ProtocolDetails> ExpCon = setProtocolDetailsExpCon(submissions);
			List<ProtocolDetails> ExpIntial = setProtocolDetailsExpIntial(submissions);
			List<ProtocolDetails> ExpRes = setProtocolDetailsExpRes(submissions);
			List<MinutesEntry> otherList = setMinuteEntry(vo);
			context.put("otherList", otherList);
			context.put("fullIntialApp", fullIntialApp);
			context.put("fullAmd", fullAmd);
			context.put("fullCon", fullCon);
			context.put("fullRes", fullRes);
			context.put("ExpAmd", ExpAmd);
			context.put("ExpCon", ExpCon);
			context.put("ExpIntial", ExpIntial);
			context.put("ExpRes", ExpRes);
			context.put("fullIntCount", fullIntialApp.size());
			context.put("fullAmdCnt", fullAmd.size());
			context.put("fullConCnt", fullCon.size());
			context.put("FullResCnt", fullRes.size());
			context.put("ExpIntCnt", ExpIntial.size());
			context.put("ExpAmdCnt", ExpAmd.size());
			context.put("ExpConCnt", ExpCon.size());
			context.put("ExpRes", ExpRes.size());
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
			List<ProtocolDetails> details = loadScheduleIdsForAgenda(vo);
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy"); 
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
			vo.setCommitteeSchedule(committeeSchedule);	
			String pattern = "hh:mm a";
	        DateFormat dateFormat = new SimpleDateFormat(pattern);
			context.put("scheduleMeetingDate",vo.getCommitteeSchedule().getMeetingDate()  == null ? "" : formatter.format(vo.getCommitteeSchedule().getMeetingDate()));
			context.put("scheduleMeetingTime",vo.getCommitteeSchedule().getTime() == null ? "" : dateFormat.format(vo.getCommitteeSchedule().getTime()));
			context.put("scheduleLocation",vo.getCommitteeSchedule().getPlace() == null ? "" : vo.getCommitteeSchedule().getPlace());
			context.put("previousMeetingDate",details.get(0).getPreviousSchMeetingDate() == null ? "" : details.get(0).getPreviousSchMeetingDate());
			context.put("nextMeetingDate",details.get(1).getNextSchMeetingDate() == null ? "" : details.get(1).getNextSchMeetingDate());
			context.put("meetingPlace",details.get(1).getNextMeetingPlace() == null ? "" : details.get(1).getNextMeetingPlace());			
			context.put("yesVote",vo.getCommitteeSchedule().getPlace() == null ? "" : vo.getCommitteeSchedule().getPlace());
			context.put("noVote",vo.getCommitteeSchedule().getPlace() == null ? "" : vo.getCommitteeSchedule().getPlace());
			context.put("abstainers",vo.getCommitteeSchedule().getPlace() == null ? "" : vo.getCommitteeSchedule().getPlace());

		}catch (Exception e) {
			logger.info("Exception in setPlaceHolderData method:" + e);
		}
		return context;
	}

	@Override
	public ScheduleVo generateMintes(ScheduleVo vo) {
		ResponseEntity<byte[]> attachmentData = null;
		try{
			vo.setTemplateCode("5");
			byte[] data = getTemplateData(vo);
			byte[] mergedOutput = mergePlaceHoldersMinute(data,vo);
			String generatedFileName = "Minute"+".pdf";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.setContentDispositionFormData(generatedFileName, generatedFileName);
			headers.setContentLength(mergedOutput.length);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			headers.setPragma("public");
			attachmentData = new ResponseEntity<byte[]>(mergedOutput, headers, HttpStatus.OK);	
			boolean flag = saveScheduleMinute(vo,attachmentData);
			vo.setFlag(flag);
		}catch (Exception e) {
			vo.setFlag(false);
			logger.error("Exception in generateMinute"+ e.getMessage());
		}
		return vo;
	}

	private boolean saveScheduleMinute(ScheduleVo vo, ResponseEntity<byte[]> attachmentData) {
		try {
			Date date = new Date(System.currentTimeMillis());		
			CommitteeScheduleMinuteDoc minute = new CommitteeScheduleMinuteDoc();	
			CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(vo.getScheduleId());
			CommitteeScheduleMinuteDoc prevDetails = null;
			prevDetails = getPrevMinuteDetails(vo.getScheduleId());
			if(prevDetails == null){
				minute.setMinuteNumber(1);
			}else{
				minute.setMinuteNumber(prevDetails.getMinuteNumber()+1);
			}
			minute.setScheduleId(vo.getScheduleId());
			minute.setMinuteName("Minute_"+minute.getMinuteNumber());
			minute.setCommitteeSchedule(committeeSchedule);
			minute.setPdfStore(attachmentData.getBody());
			minute.setCreateUser(vo.getUpdateUser());
			minute.setCreateTimestamp(date);
			hibernateTemplate.saveOrUpdate(minute);		
			vo.setFlag(true);
		} catch (Exception e) {
			vo.setFlag(false);
			logger.error("Exception in saveScheduleMinute"+ e.getMessage());
		}
		return vo.isFlag();	
	}

	private byte[] mergePlaceHoldersMinute(byte[] data, ScheduleVo vo) {
		byte[] mergedOutput = null;
		try{
			InputStream myInputStream = new ByteArrayInputStream(data); 
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(myInputStream,TemplateEngineKind.Velocity);
			IContext context = report.createContext();
			FieldsMetadata fieldsMetadata = report.createFieldsMetadata();	
			FieldsMetadata fieldsMetadata1 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata2 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata3 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata4 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata5 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata6 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata7 = report.createFieldsMetadata();
			FieldsMetadata fieldsMetadata8 = report.createFieldsMetadata();			
			List<ProtocolSubmission> submissions = allProtocolDetails(vo);	
			fieldsMetadata.load("otherList", MinutesEntry.class, true);
			fieldsMetadata1.load("fullIntialApp", ProtocolDetails.class, true);
		    fieldsMetadata2.load("ExpIntial", ProtocolDetails.class, true);				
			fieldsMetadata3.load("fullCon", ProtocolDetails.class, true);
			fieldsMetadata4.load("ExpCon", ProtocolDetails.class, true);								
			fieldsMetadata5.load("fullAmd", ProtocolDetails.class, true);
			fieldsMetadata6.load("ExpAmd", ProtocolDetails.class, true);					
			fieldsMetadata7.load("fullRes", ProtocolDetails.class, true);	
			fieldsMetadata8.load("ExpRes", ProtocolDetails.class, true);
			List<MinutesEntry> otherList = setMinuteEntry(vo);
			List<ProtocolDetails> fullIntialApp = setProtocolDetailsFullIntial(submissions);
			List<ProtocolDetails> fullAmd = setProtocolDetailsfullAmd(submissions);
			List<ProtocolDetails> fullCon = setProtocolDetailsfullCon(submissions);
			List<ProtocolDetails> fullRes = setProtocolDetailsfullRes(submissions);
			List<ProtocolDetails> ExpAmd = setProtocolDetailsExpAmd(submissions);
			List<ProtocolDetails> ExpCon = setProtocolDetailsExpCon(submissions);
			List<ProtocolDetails> ExpIntial = setProtocolDetailsExpIntial(submissions);
			List<ProtocolDetails> ExpRes = setProtocolDetailsExpRes(submissions);
			context.put("otherList", otherList);
			context.put("fullIntialApp", fullIntialApp);
			context.put("fullAmd", fullAmd);
			context.put("fullCon", fullCon);
			context.put("fullRes", fullRes);
			context.put("ExpAmd", ExpAmd);
			context.put("ExpCon", ExpCon);
			context.put("ExpIntial", ExpIntial);
			context.put("ExpRes", ExpRes);
			context.put("fullIntCount", fullIntialApp.size());
			context.put("fullAmdCnt", fullAmd.size());
			context.put("fullConCnt", fullCon.size());
			context.put("FullResCnt", fullRes.size());
			context.put("ExpIntCnt", ExpIntial.size());
			context.put("ExpAmdCnt", ExpAmd.size());
			context.put("ExpConCnt", ExpCon.size());
			context.put("ExpRes", ExpRes.size());
		    context = setPlaceHolderData(context,vo);
			DocxDocumentMergerAndConverter docxDocumentMergerAndConverter = new DocxDocumentMergerAndConverter();
			mergedOutput = docxDocumentMergerAndConverter.mergeAndGeneratePDFOutput(myInputStream,report,null, TemplateEngineKind.Velocity,context);
		}catch (Exception e) {
			logger.info("Exception in mergePlaceHolders method:" + e);
		}
		return mergedOutput;
	}	
	
	private List<MinutesEntry> setMinuteEntry(ScheduleVo vo) {				
		List<MinutesEntry> otherEntry = new ArrayList<>();		
		List<CommitteeScheduleActItems> list = scheduleDao.getCommitteeScheduleActItemsById(vo.getScheduleId());
		for (CommitteeScheduleActItems committeeScheduleActItems : list) {
			otherEntry.add(new MinutesEntry(committeeScheduleActItems.getScheduleActItemTypeDescription(),committeeScheduleActItems.getItemDescription()));		
		}					
		return otherEntry;		
	}

	@Override
	public CommitteeScheduleMinuteDoc getPrevMinuteDetails(Integer scheduleId) {
		CommitteeScheduleMinuteDoc attachment = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleMinuteDoc> criteria = builder.createQuery(CommitteeScheduleMinuteDoc.class);
			Root<CommitteeScheduleMinuteDoc> attachmentRoot=criteria.from(CommitteeScheduleMinuteDoc.class);	
			criteria.multiselect(attachmentRoot.get("scheduleMinuteDocId"),attachmentRoot.get("scheduleId")
					,attachmentRoot.get("minuteNumber"),attachmentRoot.get("createTimestamp"));
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),scheduleId));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleMinuteDocId")));
			if( session.createQuery(criteria).getResultList() != null && ! session.createQuery(criteria).getResultList().isEmpty())
			attachment = (CommitteeScheduleMinuteDoc) session.createQuery(criteria).getResultList().get(0);				
			/*Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"from CommitteeScheduleMinuteDoc p where p.scheduleId =:scheduleId order by p.updateTimestamp DESC");
			queryGeneral.setInteger("scheduleId",scheduleId);			
			if(queryGeneral.list() != null && !queryGeneral.list().isEmpty())
			attachment =  (CommitteeScheduleMinuteDoc) queryGeneral.list().get(0);*/
		} catch (Exception e) {
			logger.info("Exception in getPrevMinuteDetails method:" + e);
		}
	return attachment;	
	}
}
