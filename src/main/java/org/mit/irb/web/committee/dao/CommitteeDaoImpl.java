package org.mit.irb.web.committee.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeMemberExpertise;
import org.mit.irb.web.committee.pojo.CommitteeMemberRoles;
import org.mit.irb.web.committee.pojo.CommitteeMembershipType;
import org.mit.irb.web.committee.pojo.CommitteeMemberships;
import org.mit.irb.web.committee.pojo.CommitteeResearchAreas;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.committee.pojo.CommitteeType;
import org.mit.irb.web.committee.pojo.MembershipRole;
import org.mit.irb.web.committee.pojo.ProtocolReviewType;
import org.mit.irb.web.committee.pojo.ResearchArea;
import org.mit.irb.web.committee.pojo.ScheduleStatus;
import org.mit.irb.web.committee.pojo.Rolodex;
import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.committee.view.PersonDetailsView;
import org.mit.irb.web.committee.vo.CommitteeVo;

@Transactional
@Service(value = "committeeDao")
public class CommitteeDaoImpl implements CommitteeDao {

	protected static Logger logger = Logger.getLogger(CommitteeDaoImpl.class.getName());

	private static final String DESCRIPTION = "description";

    private static final String SCHEDULED = "Scheduled";

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<ProtocolReviewType> fetchAllReviewType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolReviewType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("reviewTypeCode"), "reviewTypeCode");
		projList.add(Projections.property(DESCRIPTION), DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolReviewType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProtocolReviewType> reviewTypes = criteria.list();
		return reviewTypes;
	}

	@Override
	public List<Unit> fetchAllHomeUnits() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Unit.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("unitName"));
		@SuppressWarnings("unchecked")
		List<Unit> units = criteria.list();
		return units;
	}

	@Override
	public List<ResearchArea> fetchAllResearchAreas() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ResearchArea.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchAreaCode"), "researchAreaCode");
		projList.add(Projections.property(DESCRIPTION), DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ResearchArea.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ResearchArea> researchAreas = criteria.list();
		return researchAreas;
	}

	@Override
	public CommitteeType fetchCommitteeType(Integer committeeTypeCode) {
		CommitteeType committeeType = null;
		committeeType = hibernateTemplate.get(CommitteeType.class, committeeTypeCode);
		return committeeType;
	}

	@Override
	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return new Timestamp(this.getCurrentDate().getTime());
	}

	@Override
	public String convertObjectToJSON(Object object) {
		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			response = mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Committee saveCommittee(Committee committee) {
		hibernateTemplate.saveOrUpdate(committee);
		return committee;
	}

	@Override
	public Committee fetchCommitteeById(String committeeId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Committee> criteria = builder.createQuery(Committee.class);
		Root<Committee> committeeRoot = criteria.from(Committee.class);		
		criteria.multiselect(committeeRoot.get("committeeId"),committeeRoot.get("committeeName"),committeeRoot.get("homeUnitNumber")
				,committeeRoot.get("description"),committeeRoot.get("scheduleDescription")
				,committeeRoot.get("committeeTypeCode"),committeeRoot.get("committeeType")
				,committeeRoot.get("minimumMembersRequired"),committeeRoot.get("maxProtocols")
				,committeeRoot.get("advSubmissionDaysReq"),committeeRoot.get("updateTimestamp"),
				committeeRoot.get("updateUser"),/*attachmentRoot.get("researchAreas"),*/
				committeeRoot.get("homeUnitName"));
		criteria.where(builder.equal(committeeRoot.get("committeeId"),committeeId));
		Committee committee = session.createQuery(criteria).getResultList().get(0);		
		List<CommitteeResearchAreas> committeeResearchAreas= getResearchAreasByCommitteeId(committeeId);
		committee.setResearchAreas(committeeResearchAreas);
		return committee;
	}

	@Override
	public ScheduleStatus fetchScheduleStatusByStatus(String scheduleStatus) {
		ScheduleStatus status = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleStatus.class);
		criteria.add(Restrictions.eq(DESCRIPTION, SCHEDULED));
		status = (ScheduleStatus) criteria.list().get(0);
		return status;
	}

	@Override
	public List<Committee> loadAllCommittee() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Committee.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("committeeId"), "committeeId");
		projList.add(Projections.property("committeeName"), "committeeName");
		projList.add(Projections.property("homeUnitNumber"), "homeUnitNumber");
		projList.add(Projections.property("homeUnitName"), "homeUnitName");
		projList.add(Projections.property("reviewTypeDescription"), "reviewTypeDescription");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Committee.class));
		criteria.addOrder(Order.asc("updateTimestamp"));
		@SuppressWarnings("unchecked")
		List<Committee> committees = criteria.list();
		return committees;
	}

	@Override
	public List<PersonDetailsView> getAllEmployees() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(PersonDetailsView.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("personId"), "personId");
		projList.add(Projections.property("fullName"), "fullName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(PersonDetailsView.class));
		criteria.addOrder(Order.asc("fullName"));
		@SuppressWarnings("unchecked")
		List<PersonDetailsView> employeesList = criteria.list();
		return employeesList;
	}

	@Override
	public List<Rolodex> getAllNonEmployees() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Rolodex.class);
		criteria.add(Restrictions.eq("active", true));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("rolodexId"), "rolodexId");
		projList.add(Projections.property("firstName"), "firstName");
		projList.add(Projections.property("lastName"), "lastName");
		projList.add(Projections.property("middleName"), "middleName");
		projList.add(Projections.property("prefix"), "prefix");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Rolodex.class));
		criteria.addOrder(Order.asc("lastName"));
		@SuppressWarnings("unchecked")
		List<Rolodex> nonEmployeesList = criteria.list();
		return nonEmployeesList;
	}

	@Override
	public List<CommitteeMembershipType> getMembershipTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeMembershipType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipTypeCode"), "membershipTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeMembershipType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<CommitteeMembershipType> membershipTypeList = criteria.list();
		return membershipTypeList;
	}

	@Override
	public List<MembershipRole> getMembershipRoles() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MembershipRole.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipRoleCode"), "membershipRoleCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(MembershipRole.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<MembershipRole> membershipRoleList = criteria.list();
		return membershipRoleList;
	}

	@Override
	public PersonDetailsView getPersonDetailsById(String personId) {
		PersonDetailsView person = hibernateTemplate.get(PersonDetailsView.class, personId);
		return person;
	}

	@Override
	public void deleteMemberRoles(Integer roleId) {
		hibernateTemplate.delete(hibernateTemplate.get(CommitteeMemberRoles.class, roleId));
	}

	@Override
	public void deleteExpertise(Integer expertiseId) {
		hibernateTemplate.delete(hibernateTemplate.get(CommitteeMemberExpertise.class, expertiseId));
	}

	@Override
	public Rolodex getRolodexById(Integer rolodexId) {
		Rolodex rolodex = hibernateTemplate.get(Rolodex.class, rolodexId);
		return rolodex;
	}

	@Override
	public void updateCommitteSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);
	}

	@Override
	public CommitteeSchedule getCommitteeScheduleById(Integer scheduleId) {
		CommitteeSchedule committeeSchedule = hibernateTemplate.get(CommitteeSchedule.class, scheduleId);
		return committeeSchedule;
	}

	@Override
	public CommitteeMembershipType getCommitteeMembershipTypeById(String membershipTypeCode) {
		CommitteeMembershipType committeeMembershipType = hibernateTemplate.get(CommitteeMembershipType.class, membershipTypeCode);
		return committeeMembershipType;
	}

	@Override
	public List<ScheduleStatus> fetchAllScheduleStatus() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleStatus.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("scheduleStatusCode"), "scheduleStatusCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleStatus.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ScheduleStatus> status = criteria.list();
		return status;
	}

	@Override
	public CommitteeMemberRoles saveCommitteeMemberRole(CommitteeMemberRoles memberRole) {
		hibernateTemplate.save(memberRole);
		return memberRole;
	}

	@Override
	public CommitteeMemberExpertise saveCommitteeMemberExpertise(CommitteeMemberExpertise expertise) {
		hibernateTemplate.save(expertise);
		return expertise;
	}

	@Override
	public CommitteeResearchAreas saveCommitteeResearchAreas(CommitteeResearchAreas researchAreas) {
		hibernateTemplate.saveOrUpdate(researchAreas);
		return researchAreas;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> loadhomeUnits(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Unit.class));
		criteria.addOrder(Order.asc("unitName"));
		criteria.add(Restrictions.eq("active", true));
		Criterion number=Restrictions.like("unitNumber", "%" + searchString + "%");
		Criterion name=Restrictions.like("unitName", "%" + searchString + "%").ignoreCase();
		LogicalExpression andExp=Restrictions.or(number,name);
		criteria.add(andExp);
		criteria.setMaxResults(25);		
		List<Unit> keyWordsList = criteria.list();
		return keyWordsList;
	}

	@Override
	public List<ResearchArea> loadResearchAreas(String researchsearchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ResearchArea.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchAreaCode"), "researchAreaCode");
		projList.add(Projections.property(DESCRIPTION),DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ResearchArea.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		criteria.add(Restrictions.eq("active",true));
		criteria.add(Restrictions.like(DESCRIPTION, "%" + researchsearchString + "%").ignoreCase());
		criteria.setMaxResults(25);
		//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ResearchArea> keyWordsList = criteria.list();
		return keyWordsList;
	}

	@Override
	public Future<CommitteeVo> loadMembershipTypes(CommitteeVo vo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeMembershipType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipTypeCode"), "membershipTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeMembershipType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<CommitteeMembershipType> membershipTypeList = criteria.list();
		vo.setCommitteeMembershipTypes(membershipTypeList);
		return new AsyncResult<>(vo);
	}

	@Override
	public Future<CommitteeVo> loadMembershipRoles(CommitteeVo committeeVo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MembershipRole.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("membershipRoleCode"), "membershipRoleCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(MembershipRole.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<MembershipRole> membershipRoleList = criteria.list();
		committeeVo.setMembershipRoles(membershipRoleList);
		return new AsyncResult<>(committeeVo);
		
	}

	@Override
	public Future<CommitteeVo> loadAllReviewType(CommitteeVo committeeVo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolReviewType.class);
	    ProjectionList projList = Projections.projectionList();
	    projList.add(Projections.property("reviewTypeCode"), "reviewTypeCode");
	    projList.add(Projections.property(DESCRIPTION), DESCRIPTION); 	
	    criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolReviewType.class));
		criteria.addOrder(Order.asc(DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProtocolReviewType> reviewTypes = criteria.list();
		committeeVo.setReviewTypes(reviewTypes);
		return new AsyncResult<>(committeeVo);
	}

	@Override
	public Future<CommitteeVo> loadScheduleStatus(CommitteeVo committeeVo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleStatus.class);
	    ProjectionList projList = Projections.projectionList();
	    projList.add(Projections.property("scheduleStatusCode"), "scheduleStatusCode");
		projList.add(Projections.property("description"), "description");
	    criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleStatus.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ScheduleStatus> status = criteria.list();
		committeeVo.setScheduleStatus(status);
		return new AsyncResult<>(committeeVo);
	}
	
	@Override
	public Committee loadScheduleDetailsById(String committeeId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CommitteeSchedule.class);
	    ProjectionList projList = Projections.projectionList();
	    projList.add(Projections.property("scheduleId"), "scheduleId");
		projList.add(Projections.property("scheduledDate"), "scheduledDate");
		projList.add(Projections.property("place"), "place");
		projList.add(Projections.property("time"), "time");
		projList.add(Projections.property("protocolSubDeadline"), "protocolSubDeadline");
		projList.add(Projections.property("scheduleStatus"), "scheduleStatus");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CommitteeSchedule.class));
		criteria.createAlias("committee","committee").add(Restrictions.eq("committee.committeeId",committeeId));
		@SuppressWarnings("unchecked")
		List<CommitteeSchedule> committeeSchedule = criteria.list();
		Committee committee=new Committee();
		committee.setCommitteeId(committeeId);
		committee.setCommitteeSchedules(committeeSchedule);
		return committee;
	}
	@Override
	public void deleteCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.delete(committeeSchedule);	
	}
	
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);	
		return committeeSchedule;
	}
	
	@Override
	public List<CommitteeResearchAreas> getResearchAreasByCommitteeId(String committeeId){
		Query queryCommitteeResearchAreas = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from CommitteeResearchAreas p where p.committee.committeeId =:committeeId");
		queryCommitteeResearchAreas.setString("committeeId", committeeId);		
		List<CommitteeResearchAreas> committeeResearchAreas = queryCommitteeResearchAreas.list();
		return committeeResearchAreas;		
	}

	@Override
	public void deleteCommitteeResearchArea(Integer commResearchAreasId) {
		Query queryDeleteResearchArea = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("delete from CommitteeResearchAreas p where p.commResearchAreasId =:commResearchAreasId");
		queryDeleteResearchArea.setInteger("commResearchAreasId",commResearchAreasId);
		queryDeleteResearchArea.executeUpdate();	
	}

	@Override
	public Committee loadCommitteeMembers(String committeeId) {
		Committee committee = new Committee();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<CommitteeMemberships> criteria = builder.createQuery(CommitteeMemberships.class);
		Root<CommitteeMemberships> committeeRoot = criteria.from(CommitteeMemberships.class);
		/*criteria.multiselect(committeeRoot.get("commMembershipId"),committeeRoot.get("committee"),committeeRoot.get("personId")
				,committeeRoot.get("rolodexId"),committeeRoot.get("personName")
				,committeeRoot.get("nonEmployeeFlag"),committeeRoot.get("termStartDate")
				,committeeRoot.get("termEndDate"),committeeRoot.get("updateTimestamp")
				,committeeRoot.get("updateUser"),committeeRoot.get("committeeMemberRoles"));*/
		criteria.where(builder.equal(committeeRoot.get("committee").get("committeeId"),committeeId));
		List<CommitteeMemberships> committeeMemberships = session.createQuery(criteria).getResultList();			
		committee.setCommitteeMemberships(committeeMemberships);
		return committee;
	}
}
