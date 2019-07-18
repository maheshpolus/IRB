package org.mit.irb.web.schedule.dao;

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
import org.mit.irb.web.committee.pojo.CommitteeScheduleMinutes;
import org.mit.irb.web.committee.pojo.MinuteEntryType;
import org.mit.irb.web.committee.pojo.ProtocolContingency;
import org.mit.irb.web.committee.pojo.ScheduleActItemType;
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
		criteria.addOrder(Order.asc("description"));
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

	@Override
	public List<CommitteeScheduleMinutes> getScheduleMinutes(ScheduleVo vo) {
		List<CommitteeScheduleMinutes> scheduleMinutes = new ArrayList<>();
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleMinutes> criteria = builder.createQuery(CommitteeScheduleMinutes.class);
			Root<CommitteeScheduleMinutes> minutesRoot = criteria.from(CommitteeScheduleMinutes.class);		
			criteria.multiselect(minutesRoot.get("commScheduleMinutesId"),minutesRoot.get("entryNumber")
					,minutesRoot.get("minuteEntryTypeCode"),minutesRoot.get("minuteEntrytype")
					,minutesRoot.get("protocolContingencyCode"),minutesRoot.get("protocolContingency")
					,minutesRoot.get("protocolNumber"),minutesRoot.get("protocolId")
					,minutesRoot.get("submissionId"),minutesRoot.get("privateCommentFlag")
					,minutesRoot.get("minuteEntry"),minutesRoot.get("finalFlag"),minutesRoot.get("updateTimestamp"));
			if(vo.getAcType() != null){
				criteria.where(builder.equal(minutesRoot.get("committeeSchedule").get("scheduleId"),vo.getScheduleId()));
			}else{
				criteria.where(builder.equal(minutesRoot.get("committeeSchedule").get("scheduleId"),vo.getScheduleId()),builder.notEqual(minutesRoot.get("minuteEntryTypeCode"),Constants.PROTOCOL_REVIEWER_COMMENT));
			}
			scheduleMinutes =  session.createQuery(criteria).getResultList();
		}catch (Exception e) {
			logger.info("Exception in getScheduleMinutes:" + e);
		}
		return scheduleMinutes;
	}

	@Override
	public List<CommitteeScheduleMinutes> getProtocolCommitteeComments(ScheduleVo vo) {
	List<CommitteeScheduleMinutes> scheduleMinutes = new ArrayList<>();
	try{
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<CommitteeScheduleMinutes> criteria = builder.createQuery(CommitteeScheduleMinutes.class);
		Root<CommitteeScheduleMinutes> minutesRoot = criteria.from(CommitteeScheduleMinutes.class);		
		criteria.multiselect(minutesRoot.get("commScheduleMinutesId"),minutesRoot.get("entryNumber")
				,minutesRoot.get("minuteEntryTypeCode"),minutesRoot.get("minuteEntrytype")
				,minutesRoot.get("protocolContingencyCode"),minutesRoot.get("protocolContingency")
				,minutesRoot.get("protocolNumber"),minutesRoot.get("protocolId")
				,minutesRoot.get("submissionId"),minutesRoot.get("privateCommentFlag")
				,minutesRoot.get("minuteEntry"),minutesRoot.get("finalFlag"),minutesRoot.get("updateTimestamp"));
		criteria.where(builder.equal(minutesRoot.get("submissionId"),vo.getSubmissionId()));
		scheduleMinutes =  session.createQuery(criteria).getResultList();
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
					,committeeRoot.get("rolodexId"),committeeRoot.get("personName"));
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

	@Override
	public List<CommitteeScheduleAttendance> fetchGuestMembers(Integer scheduleId) {
		List<CommitteeScheduleAttendance> committeeScheduleAttendance = null;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"));
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

	@Override
	public Boolean fetchPresentFlag(Integer scheduleId, String committeePersonId) {
		Boolean presentFlag = false;
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CommitteeScheduleAttendance> criteria = builder.createQuery(CommitteeScheduleAttendance.class);
			Root<CommitteeScheduleAttendance> committeeRoot = criteria.from(CommitteeScheduleAttendance.class);
			criteria.multiselect(committeeRoot.get("personName"),committeeRoot.get("personId")
					,committeeRoot.get("memberPresent"));
			Predicate predicateOne = builder.equal(committeeRoot.get("committeeSchedule").get("scheduleId"),scheduleId);
			Predicate predicateTwo = builder.equal(committeeRoot.get("personId"), committeePersonId);
			criteria.where(builder.and(predicateOne, predicateTwo));

			//criteria.where(builder.equal(committeeRoot.get("committeeMemberships").get("commMembershipId"),committeeMemberships.getCommMembershipId()));
			List<CommitteeScheduleAttendance> committeeScheduleAttendance = session.createQuery(criteria).getResultList();
			presentFlag = committeeScheduleAttendance.get(0).getMemberPresent();
		} catch (Exception e) {
			logger.info("Exception in fetchPresentFlag:" + e);
		}
		return presentFlag;
	}

	@Override
	public void updateScheduleAttendance(CommitteeScheduleAttendance scheduleAttendance) {
		try{
			
		} catch (Exception e) {
			logger.info("Exception in updateScheduleAttendance:" + e);
		}		
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
		 List<CommitteeScheduleAttachment> committeeScheduleAttachment = criteria.list();	
		return committeeScheduleAttachment;
	}

	@Override
	public ScheduleVo deleteMeetingAttachment(CommitteeScheduleAttachment committeeScheduleAttachment) {
		ScheduleVo scheduleVo = new ScheduleVo();	
		try{
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from CommitteeScheduleAttachment p where p.commScheduleAttachId =:commScheduleAttachId");
			queryDeletAttachment.setInteger("commScheduleAttachId", committeeScheduleAttachment.getCommScheduleAttachId());
			queryDeletAttachment.executeUpdate();
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
}
