package org.mit.irb.web.schedule.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
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

@Transactional
@Service(value = "scheduleDao")
public class ScheduleDaoImpl implements ScheduleDao {

	protected static Logger logger = Logger.getLogger(ScheduleDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

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
		hibernateTemplate.save(committeeScheduleMinutes);
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
}
