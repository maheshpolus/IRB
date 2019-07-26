package org.mit.irb.web.schedule.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.committee.constants.Constants;
import org.mit.irb.web.committee.dao.CommitteeDao;
import org.mit.irb.web.committee.pojo.CommitteeMemberRoles;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeScheduleActItems;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachType;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttachment;
import org.mit.irb.web.committee.pojo.CommitteeScheduleAttendance;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinuteDoc;
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.MinuteEntryType;
import org.mit.irb.web.committee.pojo.ProtocolContingency;
import org.mit.irb.web.committee.pojo.ScheduleActItemType;
import org.mit.irb.web.committee.pojo.ScheduleAgenda;
import org.mit.irb.web.committee.view.ProtocolView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.mit.irb.web.dbengine.DBEngine;
import org.mit.irb.web.dbengine.DBEngineConstants;
import org.mit.irb.web.dbengine.Parameter;
import org.mit.irb.web.schedule.vo.ScheduleVo;

@Transactional
@Service(value = "scheduleDao")
public class ScheduleDaoImpl implements ScheduleDao {

	protected static Logger logger = Logger.getLogger(ScheduleDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired 
	private CommitteeDao committeeDao;
	
	@Autowired 
	private ScheduleDao scheduleDao;
	
	DBEngine dbEngine;	

	ScheduleDaoImpl() {	
		dbEngine = new DBEngine();
	}

	@Override
	public ProtocolView fetchProtocolViewByParams(Integer protocolId, String personId, String fullName) {
		ProtocolView protocolView = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolView.class);
		criteria.add(Restrictions.eq("protocolId", protocolId));
		criteria.add(Restrictions.eq("personId", personId));
		criteria.add(Restrictions.eq("fullName", fullName));
		protocolView = (ProtocolView) criteria.uniqueResult();
		return protocolView;
	}

	@Override
	public List<ScheduleActItemType> fetchAllScheduleActItemType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleActItemType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("scheduleActItemTypecode"), "scheduleActItemTypecode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleActItemType.class));
		criteria.addOrder(Order.asc("scheduleActItemTypecode"));
		@SuppressWarnings("unchecked")
		List<ScheduleActItemType> scheduleActItemTypes = criteria.list();
		return scheduleActItemTypes;
	}

	@Override
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);
		return committeeSchedule;
	}

	@Override
	public CommitteeScheduleActItems addOtherActions(CommitteeScheduleActItems committeeScheduleActItems) {
		hibernateTemplate.save(committeeScheduleActItems);
		return committeeScheduleActItems;
	}

	@Override
	public List<MinuteEntryType> fetchAllMinuteEntryTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MinuteEntryType.class);
		criteria.addOrder(Order.asc("sortId"));
		@SuppressWarnings("unchecked")
		List<MinuteEntryType> minuteEntrytypes = criteria.list();
		return minuteEntrytypes;
	}

	@Override
	public List<ProtocolContingency> fetchAllProtocolContingency() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolContingency.class);
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ProtocolContingency> protocolContingencies = criteria.list();
		return protocolContingencies;
	}

	@Override
	public List<CommitteeScheduleAttachType> fetchAllCommitteeScheduleAttachType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeScheduleAttachType.class);
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<CommitteeScheduleAttachType> committeeScheduleAttachTypes = criteria.list();
		return committeeScheduleAttachTypes;
	}

	@Override
	public CommitteeScheduleMinutes addCommitteeScheduleMinute(CommitteeScheduleMinutes committeeScheduleMinutes) {
		hibernateTemplate.saveOrUpdate(committeeScheduleMinutes);
		return committeeScheduleMinutes;
	}

	@Override
	public CommitteeScheduleAttendance addCommitteeScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance) {
		hibernateTemplate.save(scheduleAttendance);
		return scheduleAttendance;
	}

	@Override
	public CommitteeScheduleAttachment addScheduleAttachment(CommitteeScheduleAttachment committeeScheduleAttachment) {
		hibernateTemplate.save(committeeScheduleAttachment);
		return committeeScheduleAttachment;
	}

	@Override
	public CommitteeScheduleAttachment fetchAttachmentById(Integer attachmentId) {
		return hibernateTemplate.get(CommitteeScheduleAttachment.class, attachmentId);
	}



	@Override
	public CommitteeSchedule getCommitteeScheduleDetail(Integer scheduleId) {
		CommitteeSchedule committeeSchedule = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeSchedule> criteria = builder.createQuery(CommitteeSchedule.class);
			Root<CommitteeSchedule> committeeRoot = criteria.from(CommitteeSchedule.class);		
			criteria.multiselect(committeeRoot.get("scheduleId"),committeeRoot.get("scheduledDate"),committeeRoot.get("meetingDate")
					,committeeRoot.get("scheduleStatusCode"),committeeRoot.get("place")
					,committeeRoot.get("time"),committeeRoot.get("protocolSubDeadline")
					,committeeRoot.get("maxProtocols"),committeeRoot.get("startTime")
					,committeeRoot.get("endTime"),committeeRoot.get("availableToReviewers"),
					committeeRoot.get("comments"));/*attachmentRoot.get("researchAreas"),*/
				//	committeeRoot.get("homeUnitName"),committeeRoot.get("reviewTypeDescription"));
			criteria.where(builder.equal(committeeRoot.get("scheduleId"),scheduleId));
			committeeSchedule = session.createQuery(criteria).getResultList().get(0);
		}catch (Exception e) {
			logger.info("Exception in getCommitteeScheduleDetail:" + e);
		}
		return committeeSchedule;
	}

	@Override
	public ArrayList<HashMap<String, Object>> loadScheduledProtocols(Integer scheduleId) {	
		ArrayList<HashMap<String, Object>> result  = new ArrayList<HashMap<String, Object>>();
		try {
			/*Query scheduledProtocols = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("select p.committeeId,p.protocolNumber,p.protocolSubmissionType,p.protocolReviewType,"
							+ "p.submissionDate, g.protocolTitle ,pi.personName from ProtocolSubmission p INNER JOIN "
							+ "ProtocolGeneralInfo g on p.protocolId = g.protocolId inner join ProtocolPersonnelInfo pi "
							+ "on p.protocolId = pi.protocolGeneralInfo.protocolId and pi.protocolPersonRoleId ='PI'  "
							+ "where p.committeeSchedule.scheduleId =:scheduleId");	*/	
			ArrayList<Parameter> inParam = new ArrayList<>();			
			inParam.add(new Parameter("<<AV_SCHEDULE_ID>>", DBEngineConstants.TYPE_INTEGER, scheduleId));
			result  = dbEngine.executeQuery(inParam,"GET_SCHEDULED_PROTOCOLS");
		} catch (Exception e) {
			logger.info("Exception in loadScheduledProtocols:" + e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommitteeScheduleMinutes> getScheduleMinutes(ScheduleVo vo) {
		List<CommitteeScheduleMinutes> scheduleMinutes = new ArrayList<>();
		try{			
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(CommitteeScheduleMinutes.class);
		    ProjectionList projList = Projections.projectionList();
		    projList.add(Projections.property("commScheduleMinutesId"), "commScheduleMinutesId");
			projList.add(Projections.property("entryNumber"), "entryNumber");
			projList.add(Projections.property("minuteEntryTypeCode"), "minuteEntryTypeCode");
			projList.add(Projections.property("minuteEntrytype"), "minuteEntrytype");
			projList.add(Projections.property("protocolContingencyCode"), "protocolContingencyCode");
			projList.add(Projections.property("protocolContingency"), "protocolContingency");
			projList.add(Projections.property("protocolNumber"), "protocolNumber");
			projList.add(Projections.property("protocolId"), "protocolId");
			projList.add(Projections.property("submissionId"), "submissionId");
			projList.add(Projections.property("privateCommentFlag"), "privateCommentFlag");
			projList.add(Projections.property("minuteEntry"), "minuteEntry");
			projList.add(Projections.property("finalFlag"), "finalFlag");
			projList.add(Projections.property("updateTimestamp"), "updateTimestamp");
			criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleMinutes.class));
			if(vo.getAcType() != null){
				criteria.add(Restrictions.eq("committeeSchedule.scheduleId",vo.getScheduleId()));
			}else{
				criteria.add(Restrictions.eq("committeeSchedule.scheduleId",vo.getScheduleId()));
				criteria.add(Restrictions.neOrIsNotNull("minuteEntryTypeCode",Integer.parseInt(Constants.PROTOCOL)));
			}
			scheduleMinutes = criteria.list();
		}catch (Exception e) {
			logger.info("Exception in getScheduleMinutes:" + e);
		}
		return scheduleMinutes;
	}

	@Override
	public List<CommitteeScheduleMinutes> getProtocolCommitteeComments(Integer submissionId, Integer scheduleId) {
	List<CommitteeScheduleMinutes> scheduleMinutes = new ArrayList<>();
	try{		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeScheduleMinutes.class);
	    ProjectionList projList = Projections.projectionList();
	    projList.add(Projections.property("commScheduleMinutesId"), "commScheduleMinutesId");
		projList.add(Projections.property("entryNumber"), "entryNumber");
		projList.add(Projections.property("minuteEntryTypeCode"), "minuteEntryTypeCode");
		projList.add(Projections.property("minuteEntrytype"), "minuteEntrytype");
		projList.add(Projections.property("protocolContingencyCode"), "protocolContingencyCode");
		projList.add(Projections.property("protocolContingency"), "protocolContingency");
		projList.add(Projections.property("protocolNumber"), "protocolNumber");
		projList.add(Projections.property("protocolId"), "protocolId");
		projList.add(Projections.property("submissionId"), "submissionId");
		projList.add(Projections.property("privateCommentFlag"), "privateCommentFlag");
		projList.add(Projections.property("minuteEntry"), "minuteEntry");
		projList.add(Projections.property("finalFlag"), "finalFlag");
		projList.add(Projections.property("updateTimestamp"), "updateTimestamp");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleMinutes.class));		
		Criterion sub=Restrictions.eq("submissionId",submissionId);
		Criterion sche=Restrictions.eq("committeeSchedule.scheduleId",scheduleId);
		LogicalExpression andExp=Restrictions.and(sub,sche);
		criteria.add(andExp);
		scheduleMinutes = criteria.list();	
	}catch (Exception e) {
		logger.info("Exception in getProtocolCommitteeComments:" + e);
	}
	return scheduleMinutes;
	}
	

	@Override
	public List<CommitteeMemberships> fetchMeetingMembers(ScheduleVo vo) {
		List<CommitteeMemberships> committeeMemberships = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeMemberships> criteria = builder.createQuery(CommitteeMemberships.class);
			Root<CommitteeMemberships> committeeRoot = criteria.from(CommitteeMemberships.class);
			criteria.multiselect(committeeRoot.get("commMembershipId"),committeeRoot.get("committee"),committeeRoot.get("personId")
					,committeeRoot.get("rolodexId"),committeeRoot.get("personName"),committeeRoot.get("nonEmployeeFlag")
					,committeeRoot.get("termStartDate"),committeeRoot.get("termEndDate"));
			/*Predicate predicateOne = builder.equal(committeeRoot.get("committee").get("committeeId"),vo.getCommitteeId());
			Predicate predicateTwo = builder.equal(committeeRoot.get("active"), true);
			criteria.where(builder.and(predicateOne, predicateTwo));*/
			criteria.where(builder.equal(committeeRoot.get("committee").get("committeeId"),vo.getCommitteeId()));
			committeeMemberships = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in fetchMeetingMembers:" + e);
		}
		return committeeMemberships;
	}

	@Override
	public List<CommitteeMemberRoles> fetchCommitteeMemberRoles(CommitteeMemberships committeeMemberships) {
		List<CommitteeMemberRoles> committeeMembershipRoles = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeMemberRoles> criteria = builder.createQuery(CommitteeMemberRoles.class);
			Root<CommitteeMemberRoles> committeeRoot = criteria.from(CommitteeMemberRoles.class);
			criteria.multiselect(committeeRoot.get("commMemberRolesId"),committeeRoot.get("membershipRoleCode")
					,committeeRoot.get("membershipRoleDescription"));
			criteria.where(builder.equal(committeeRoot.get("committeeMemberships").get("commMembershipId"),committeeMemberships.getCommMembershipId()));
			committeeMembershipRoles = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in fetchCommitteeMemberRoles:" + e);
		}
		return committeeMembershipRoles;
	}

	/*@Override
	public List<CommitteeScheduleAttendance> fetchGuestMembers(Integer scheduleId) {
		List<CommitteeScheduleAttendance> committeeScheduleAttendance = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"),committeeRoot.get("guestFlag"),committeeRoot.get("committeeScheduleAttendanceId")
					,committeeRoot.get("comments"),committeeRoot.get("alternateFor"));
			Predicate predicateOne = builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId);
			Predicate predicateTwo = builder.equal(committeeRoot.get("guestFlag"), true);
			criteria.where(builder.and(predicateOne, predicateTwo));

			//criteria.where(builder.equal(committeeRoot.get("committeeMemberships").get("commMembershipId"),committeeMemberships.getCommMembershipId()));
			committeeScheduleAttendance = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in fetchGuestMembers:" + e);
		}
		return committeeScheduleAttendance;
	}*/

	@Override
	public CommitteeScheduleAttendance updateScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance) {
		try{
			hibernateTemplate.saveOrUpdate(scheduleAttendance);
		} catch (Exception e) {
			logger.info("Exception in updateScheduleAttendance:" + e);
		}	
		return scheduleAttendance;
	}	

	@Override
	public List<CommitteeScheduleAttachment> getCommitteeScheduleAttachementById(Integer scheduleId) {
		 Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		 Criteria criteria = session.createCriteria(CommitteeScheduleAttachment.class);
		 ProjectionList projList = Projections.projectionList();
		 //projList.add(Projections.property("attachmentId"),"attachmentId");
		 projList.add(Projections.property("attachmentTypeCode"),"attachmentTypeCode");
		 projList.add(Projections.property("description"),"description");
		 projList.add(Projections.property("fileName"),"fileName");
		 projList.add(Projections.property("mimeType"),"mimeType");
		 projList.add(Projections.property("updateTimestamp"),"updateTimestamp");
		 projList.add(Projections.property("updateUser"),"updateUser");
		 projList.add(Projections.property("commScheduleAttachId"),"commScheduleAttachId");
		 projList.add(Projections.property("attachmentType"),"attachmentType");
		 criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleAttachment.class));
		 criteria.add(Restrictions.eq("committeeSchedule.scheduleId",scheduleId));
		 criteria.addOrder(Order.asc("commScheduleAttachId"));
			@SuppressWarnings("unchecked")
		 List<CommitteeScheduleAttachment> committeeScheduleAttachment = criteria.list();	
		return committeeScheduleAttachment;
	}

	@Override
	public ScheduleVo deleteMeetingAttachment(CommitteeScheduleAttachment committeeScheduleAttachment,Integer scheduleId) {
		ScheduleVo scheduleVo = new ScheduleVo();	
		try{
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from CommitteeScheduleAttachment p where p.commScheduleAttachId =:commScheduleAttachId");
			queryDeletAttachment.setInteger("commScheduleAttachId", committeeScheduleAttachment.getCommScheduleAttachId());
			queryDeletAttachment.executeUpdate();
			List<CommitteeScheduleAttachment> committeeScheduleAttachmentList = scheduleDao.getCommitteeScheduleAttachementById(scheduleId);
			scheduleVo.setCommitteeScheduleAttachmentList(committeeScheduleAttachmentList);
			scheduleVo.setScheduleId(scheduleId);
		}catch (Exception e) {
			logger.error("Error in deleteMeetingAttachment: ", e);
		}			
		return scheduleVo;
	}

	@Override
	public ScheduleVo saveOrUpdateMeetingAttachment(MultipartFile[] files,
			CommitteeScheduleAttachment committeeScheduleAttachment,Integer scheduleId) {
		CommitteeSchedule committeeSchedule = committeeDao.getCommitteeScheduleById(scheduleId);
		ScheduleVo scheduleVo = new ScheduleVo();
		try {
			committeeScheduleAttachment.setCommitteeSchedule(committeeSchedule);
			committeeScheduleAttachment.setMimeType(files[0].getContentType());
			committeeScheduleAttachment.setFileName(files[0].getOriginalFilename());		
			committeeScheduleAttachment.setAttachment(files[0].getBytes());
			committeeScheduleAttachment.setUpdateTimestamp(getCurrentDate());
			hibernateTemplate.saveOrUpdate(committeeScheduleAttachment);			
		} catch (Exception e) {
			logger.error("Error in saveOrUpdateInternalProtocolAttachment: ", e);
		}
		List<CommitteeScheduleAttachment> committeeScheduleAttachmentList = scheduleDao.getCommitteeScheduleAttachementById(scheduleId);
		scheduleVo.setCommitteeScheduleAttachmentList(committeeScheduleAttachmentList);
		scheduleVo.setScheduleId(scheduleId);
		scheduleVo.setNewCommitteeScheduleAttachment(committeeScheduleAttachment);
		return scheduleVo;
	}

	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}
	
	@Override
	public ResponseEntity<byte[]> downloadMeetingAttachment(String documentId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttachment> criteria = builder.createQuery(CommitteeScheduleAttachment.class);
			Root<CommitteeScheduleAttachment> attachmentRoot=criteria.from(CommitteeScheduleAttachment.class);				
			criteria.where(builder.equal(attachmentRoot.get("commScheduleAttachId"),Integer.parseInt(documentId)));
			CommitteeScheduleAttachment protocolAttachment = session.createQuery(criteria).getResultList().get(0);	
			if (protocolAttachment != null) {				
				byte[] byteArray = null;				
				byteArray = protocolAttachment.getAttachment();				
    			HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(protocolAttachment.getMimeType()));
				String filename = protocolAttachment.getFileName();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
		logger.error("Exception in downloadMeetingAttachment method:" + e);
		}
		return attachmentData;
	}

	@Override
	public ArrayList<HashMap<String, Object>> loadScheduleIdsForAgenda(CommitteeSchedule committeeSchedule, String committeeId) {	
		ArrayList<HashMap<String, Object>> result  = new ArrayList<HashMap<String, Object>>();
		try {	
			java.sql.Date sqlDate= null;
			sqlDate = new java.sql.Date(committeeSchedule.getMeetingDate().getTime()); 
			ArrayList<Parameter> inParam = new ArrayList<>();	
			inParam.add(new Parameter("<<AV_COMMITTEE_ID>>", DBEngineConstants.TYPE_STRING, committeeId));
			inParam.add(new Parameter("<<AV_MEETING_DATE>>", DBEngineConstants.TYPE_DATE,sqlDate));
			inParam.add(new Parameter("<<AV_COMMITTEE_ID1>>", DBEngineConstants.TYPE_STRING, committeeId));
			inParam.add(new Parameter("<<AV_MEETING_DATE1>>", DBEngineConstants.TYPE_DATE,sqlDate));
			inParam.add(new Parameter("<<AV_COMMITTEE_ID2>>", DBEngineConstants.TYPE_STRING, committeeId));
			result  = dbEngine.executeQuery(inParam,"get_schedule_ids_agenda");
		} catch (Exception e) {
			logger.info("Exception in loadScheduledProtocols:" + e);
		}
		return result;
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
	
	@Override
	public CommitteeScheduleMinutes updateScheduleMinutes(CommitteeScheduleMinutes scheduleMinutes) {
		hibernateTemplate.saveOrUpdate(scheduleMinutes);
		return scheduleMinutes;
	}

	@Override
	public void deleteScheduleMinute(Integer commScheduleMinuteId) {
		Query queryScheduleMinute = hibernateTemplate.getSessionFactory().getCurrentSession()
		.createQuery("delete from CommitteeScheduleMinutes p where p.commScheduleMinutesId =:commScheduleMinutesId");
		queryScheduleMinute.setInteger("commScheduleMinutesId",commScheduleMinuteId);
		queryScheduleMinute.executeUpdate();
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAgenda(String scheduleId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ScheduleAgenda> criteria = builder.createQuery(ScheduleAgenda.class);
			Root<ScheduleAgenda> attachmentRoot=criteria.from(ScheduleAgenda.class);				
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),Integer.parseInt(scheduleId)));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleAgendaId")));
			ScheduleAgenda protocolAttachment = session.createQuery(criteria).getResultList().get(0);	
			if (protocolAttachment != null) {				
				byte[] byteArray = null;				
				byteArray = protocolAttachment.getPdfStore();				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename = "Agenda_"+scheduleId;
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Exception in downloadScheduleAgenda:" + e);
		}		
		return attachmentData;
	}

	@Override
	public List<ScheduleAgenda> loadAllScheduleAgenda(Integer scheduleId) {
		List<ScheduleAgenda> attachment = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ScheduleAgenda> criteria = builder.createQuery(ScheduleAgenda.class);
			Root<ScheduleAgenda> attachmentRoot=criteria.from(ScheduleAgenda.class);	
			criteria.multiselect(attachmentRoot.get("scheduleAgendaId"),attachmentRoot.get("scheduleId")
					,attachmentRoot.get("agendaNumber"),attachmentRoot.get("createTimestamp"),attachmentRoot.get("createUser"));
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),scheduleId));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleAgendaId")));
			attachment = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in loadAllScheduleAgenda:" + e);
		}
		return attachment;
	}
	
	
	@Override
	public List<CommitteeScheduleActItems> getCommitteeScheduleActItemsById(Integer scheduleId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		 Criteria criteria = session.createCriteria(CommitteeScheduleActItems.class);
		 ProjectionList projList = Projections.projectionList();
		 projList.add(Projections.property("commScheduleActItemsId"),"commScheduleActItemsId");
		 projList.add(Projections.property("actionItemNumber"),"actionItemNumber");
		 projList.add(Projections.property("scheduleActItemTypecode"),"scheduleActItemTypecode");
		 projList.add(Projections.property("itemDescription"),"itemDescription");
		 projList.add(Projections.property("scheduleActItemTypeDescription"),"scheduleActItemTypeDescription");
		 projList.add(Projections.property("updateTimestamp"),"updateTimestamp");
		 projList.add(Projections.property("updateUser"),"updateUser");
		 criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleActItems.class));
		 criteria.add(Restrictions.eq("committeeSchedule.scheduleId",scheduleId));
		 criteria.addOrder(Order.asc("commScheduleActItemsId"));
			@SuppressWarnings("unchecked")
		 List<CommitteeScheduleActItems> committeeScheduleActItems = criteria.list();	
		return committeeScheduleActItems;
	}

	@Override
	public List<CommitteeScheduleActItems> fetchAllCommitteeScheduleActItems() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeScheduleActItems.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("commScheduleActItemsId"), "commScheduleActItemsId");
		projList.add(Projections.property("actionItemNumber"), "actionItemNumber");
		projList.add(Projections.property("scheduleActItemTypecode"), "scheduleActItemTypecode");
		projList.add(Projections.property("itemDescription"), "itemDescription");
		projList.add(Projections.property("scheduleActItemTypeDescription"), "scheduleActItemTypeDescription");
		projList.add(Projections.property("updateTimestamp"), "updateTimestamp");
		projList.add(Projections.property("updateUser"), "updateUser");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleActItems.class));
		criteria.addOrder(Order.asc("commScheduleActItemsId"));
		@SuppressWarnings("unchecked")
		List<CommitteeScheduleActItems> scheduleActItemTypes = criteria.list();
		return scheduleActItemTypes;
	}

	@Override
	public void deleteMeetingOtherActions(Integer commScheduleActItemsId) {	
		try{
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from CommitteeScheduleActItems p where p.commScheduleActItemsId =:commScheduleActItemsId");
			queryDeletAttachment.setInteger("commScheduleActItemsId",commScheduleActItemsId);
			queryDeletAttachment.executeUpdate();
		}catch (Exception e) {
			logger.error("Error in deleteMeetingOtherActions: ", e);
		}
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleAgendaById(String scheduleAgendaId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ScheduleAgenda> criteria = builder.createQuery(ScheduleAgenda.class);
			Root<ScheduleAgenda> attachmentRoot=criteria.from(ScheduleAgenda.class);				
			criteria.where(builder.equal(attachmentRoot.get("scheduleAgendaId"),Integer.parseInt(scheduleAgendaId)));
			ScheduleAgenda protocolAttachment = session.createQuery(criteria).getResultList().get(0);	
			if (protocolAttachment != null) {				
				byte[] byteArray = null;				
				byteArray = protocolAttachment.getPdfStore();				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename = "Agenda_"+scheduleAgendaId;
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Exception in downloadScheduleAgenda:" + e);
		}		
		return attachmentData;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleMinute(String scheduleId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleMinuteDoc> criteria = builder.createQuery(CommitteeScheduleMinuteDoc.class);
			Root<CommitteeScheduleMinuteDoc> attachmentRoot=criteria.from(CommitteeScheduleMinuteDoc.class);				
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),Integer.parseInt(scheduleId)));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleMinuteDocId")));
			CommitteeScheduleMinuteDoc attachment = null;
			if(session.createQuery(criteria).getResultList() != null && !session.createQuery(criteria).getResultList().isEmpty())
			attachment = session.createQuery(criteria).getResultList().get(0);	
			if (attachment != null) {				
				byte[] byteArray = null;				
				byteArray = attachment.getPdfStore();				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename = "Minute"+scheduleId;
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Exception in downloadScheduleMinute:" + e);
		}		
		return attachmentData;
	}

	@Override
	public ResponseEntity<byte[]> downloadScheduleMinuteById(String scheduleMinuteDocId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleMinuteDoc> criteria = builder.createQuery(CommitteeScheduleMinuteDoc.class);
			Root<CommitteeScheduleMinuteDoc> attachmentRoot=criteria.from(CommitteeScheduleMinuteDoc.class);				
			criteria.where(builder.equal(attachmentRoot.get("scheduleMinuteDocId"),Integer.parseInt(scheduleMinuteDocId)));
			CommitteeScheduleMinuteDoc attachment  = null;
			if(session.createQuery(criteria).getResultList() != null && !session.createQuery(criteria).getResultList().isEmpty())
			attachment = session.createQuery(criteria).getResultList().get(0);	
			if (attachment != null) {				
				byte[] byteArray = null;				
				byteArray = attachment.getPdfStore();				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename = "Minute"+scheduleMinuteDocId;
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Exception in downloadScheduleMinuteById:" + e);
		}		
		return attachmentData;
	}

	@Override
	public List<CommitteeScheduleMinuteDoc> loadAllScheduleMinutes(Integer scheduleId) {
		List<CommitteeScheduleMinuteDoc> attachment = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleMinuteDoc> criteria = builder.createQuery(CommitteeScheduleMinuteDoc.class);
			Root<CommitteeScheduleMinuteDoc> attachmentRoot=criteria.from(CommitteeScheduleMinuteDoc.class);	
			criteria.multiselect(attachmentRoot.get("scheduleMinuteDocId"),attachmentRoot.get("scheduleId")
					,attachmentRoot.get("minuteNumber"),attachmentRoot.get("createTimestamp"),attachmentRoot.get("createUser"));
			criteria.where(builder.equal(attachmentRoot.get("committeeSchedule").get("scheduleId"),scheduleId));
			criteria.orderBy(builder.desc(attachmentRoot.get("scheduleMinuteDocId")));
			attachment = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in loadAllScheduleMinutes:" + e);
		}
		return attachment;
	}
	
	@Override
	public void deleteMeetingAttendence(Integer committeeScheduleAttendanceId) {
		try{
			Query queryMeetingAttendence = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from CommitteeScheduleAttendance p where p.committeeScheduleAttendanceId =:committeeScheduleAttendanceId");
			queryMeetingAttendence.setInteger("committeeScheduleAttendanceId",committeeScheduleAttendanceId);
			queryMeetingAttendence.executeUpdate();
		}catch (Exception e) {
			logger.error("Error in deleteMeetingAttendence: ", e);
		}		
	}

	@Override
	public List<CommitteeScheduleAttendance> fetchAttendenceData(Integer scheduleId) {
		List<CommitteeScheduleAttendance> committeeScheduleAttendance = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			 Criteria criteria = session.createCriteria(CommitteeScheduleAttendance.class);
			 ProjectionList projList = Projections.projectionList();
			 projList.add(Projections.property("committeeScheduleAttendanceId"),"committeeScheduleAttendanceId");
			 projList.add(Projections.property("personId"),"personId");
			 projList.add(Projections.property("personName"),"personName");
			 projList.add(Projections.property("guestFlag"),"guestFlag");
			 projList.add(Projections.property("alternateFlag"),"alternateFlag");
			 projList.add(Projections.property("alternateFor"),"alternateFor");
			 projList.add(Projections.property("nonEmployeeFlag"),"nonEmployeeFlag"); 
			 projList.add(Projections.property("comments"),"comments");
			 projList.add(Projections.property("updateTimestamp"),"updateTimestamp");
			 projList.add(Projections.property("updateUser"),"updateUser");
			 projList.add(Projections.property("memberPresent"),"memberPresent");
			 criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeScheduleAttendance.class));
			criteria.add(Restrictions.eq("committeeSchedule.scheduleId", scheduleId));
			committeeScheduleAttendance = criteria.list();		
			/*Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"),committeeRoot.get("guestFlag"),committeeRoot.get("committeeScheduleAttendanceId")
					,committeeRoot.get("comments"),committeeRoot.get("alternateFor"),committeeRoot.get("nonEmployeeFlag"));
			criteria.where(builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId));
			/*Predicate predicateOne = builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId);
			Predicate predicateTwo = builder.equal(committeeRoot.get("personId"), committeePersonId);
			criteria.where(builder.and(predicateOne, predicateTwo));*/

			/*committeeScheduleAttendance = session.createQuery(criteria).getResultList();*/
			/*if(!committeeScheduleAttendance.isEmpty()){
				committeeMemberships.setMemberPresent(committeeScheduleAttendance.get(0).getMemberPresent());
				committeeMemberships.setAttendanceComment(committeeScheduleAttendance.get(0).getComments());
				committeeMemberships.setScheduleAttendanceId(committeeScheduleAttendance.get(0).getCommitteeScheduleAttendanceId());
				committeeMemberships.setAlternateFor(committeeScheduleAttendance.get(0).getAlternateFor());
			}*/		
		} catch (Exception e) {
			logger.info("Exception in fetchAttendenceData:" + e);
		}
		return committeeScheduleAttendance;
	}

	@Override
	public CommitteeMemberships fetchAttendenceDataForMinutes(Integer scheduleId, String committeePersonId,
			CommitteeMemberships committeeMemberships) {
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"),committeeRoot.get("guestFlag"),committeeRoot.get("committeeScheduleAttendanceId")
					,committeeRoot.get("comments"),committeeRoot.get("alternateFor"));
			Predicate predicateOne = builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId);
			Predicate predicateTwo = builder.equal(committeeRoot.get("personId"), committeePersonId);
			criteria.where(builder.and(predicateOne, predicateTwo));

			List<CommitteeScheduleAttendance> committeeScheduleAttendance = session.createQuery(criteria).getResultList();
			if(!committeeScheduleAttendance.isEmpty()){
				committeeMemberships.setMemberPresent(committeeScheduleAttendance.get(0).getMemberPresent());
				committeeMemberships.setAttendanceComment(committeeScheduleAttendance.get(0).getComments());
				committeeMemberships.setScheduleAttendanceId(committeeScheduleAttendance.get(0).getCommitteeScheduleAttendanceId());
				committeeMemberships.setAlternateFor(committeeScheduleAttendance.get(0).getAlternateFor());
			}		
		} catch (Exception e) {
			logger.info("Exception in fetchAttendenceData:" + e);
		}
		return committeeMemberships;
	}

	@Override
	public List<CommitteeScheduleAttendance> fetchGuestMembersForMinutes(Integer scheduleId) {
		List<CommitteeScheduleAttendance> committeeScheduleAttendance = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"),committeeRoot.get("guestFlag"),committeeRoot.get("committeeScheduleAttendanceId")
					,committeeRoot.get("comments"),committeeRoot.get("alternateFor"));
			Predicate predicateOne = builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId);
			Predicate predicateTwo = builder.equal(committeeRoot.get("guestFlag"), true);
			criteria.where(builder.and(predicateOne, predicateTwo));

			//criteria.where(builder.equal(committeeRoot.get("committeeMemberships").get("commMembershipId"),committeeMemberships.getCommMembershipId()));
			committeeScheduleAttendance = session.createQuery(criteria).getResultList();
		} catch (Exception e) {
			logger.info("Exception in fetchGuestMembers:" + e);
		}
		return committeeScheduleAttendance;
	}

}
