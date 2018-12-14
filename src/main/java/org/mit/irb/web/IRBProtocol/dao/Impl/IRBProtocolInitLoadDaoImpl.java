package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.committee.pojo.Committee;
import org.mit.irb.web.committee.pojo.CommitteeSchedule;
import org.mit.irb.web.common.utils.DBEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "irbInitLoadProtocolDao")
@Transactional
public class IRBProtocolInitLoadDaoImpl implements IRBProtocolInitLoadDao{
	DBEngine dbEngine;

	@Autowired
	HibernateTemplate hibernateTemplate;

	IRBProtocolInitLoadDaoImpl() {
		dbEngine = new DBEngine();
	}
	
	Logger logger = Logger.getLogger(IRBProtocolInitLoadDaoImpl.class.getName());
	
	@Override
	public IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO) {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		final String likeCriteria = "%" + irbProtocolVO.getSearchString().toUpperCase() + "%";
		Query query = session.createQuery("SELECT NEW org.mit.irb.web.IRBProtocol.VO.SponsorSearchResult(t.sponsorCode, t.sponsorName) " +
                "FROM Sponsor t " +
                "WHERE UPPER(t.sponsorCode) like :likeCriteria OR UPPER(t.acronym) like :likeCriteria or UPPER(t.sponsorName) like :likeCriteria");
		query.setParameter("likeCriteria", likeCriteria);
		logger.info("Sponsors Type in DAO: " + ListUtils.emptyIfNull(query.setMaxResults(25).list()));
		protocolVO.setSponsorSearchResult(ListUtils.emptyIfNull(query.setMaxResults(25).list()));
		return protocolVO;
	}
	
	@Override
	public IRBProtocolVO loadAttachmentType() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachementTypes");
		irbProtocolVO.setIrbAttachementTypes(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCommitteeList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO(); 
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from Committee");
		List<Committee> committeList = query.list();
		if(!committeList.isEmpty()){
			for(Committee committee:committeList ){
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
				committee.setUpdatedDate(dateFormatter.format((Timestamp) committee.getUpdateTimestamp()));
			}
		}
		irbProtocolVO.setCommitteList(committeList);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCommitteeScheduleList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO(); 
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from CommitteeSchedule");
		List<CommitteeSchedule> committeScheduleList = query.list();
		if(!committeScheduleList.isEmpty()){
			for(CommitteeSchedule schedule:committeScheduleList ){
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
				schedule.setUpdatedDate(dateFormatter.format((Timestamp) schedule.getUpdateTimestamp()));
			}
		}
		irbProtocolVO.setCommitteSchedulList(committeScheduleList);
		return irbProtocolVO;
	}
}
