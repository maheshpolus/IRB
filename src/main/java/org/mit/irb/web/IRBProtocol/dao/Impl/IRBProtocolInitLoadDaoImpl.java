package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.OutParameter;
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
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachementTypes where subCategoryCode ='1' ");
		irbProtocolVO.setIrbAttachementTypes(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCommitteeList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO(); 
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure("GET_IRB_COMMITTEE_DETAILS", outputParam);
			if (result != null && !result.isEmpty()) {
				irbProtocolVO.setCommitteList(result);
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in loadCommitteeList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in loadCommitteeList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in loadCommitteeList:" + e);
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCommitteeScheduleList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO(); 
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure("GET_IRB_COMM_SCHEDULE_DETAILS", outputParam);
			if (result != null && !result.isEmpty()) {
				irbProtocolVO.setCommitteSchedulList(result);
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in loadCommitteeScheduleList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in loadCommitteeScheduleList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in loadCommitteeScheduleList:" + e);
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCollaboratorAttachmentType() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachementTypes where subCategoryCode ='2' ");
		irbProtocolVO.setIrbAttachementTypes(query.list());
		return irbProtocolVO;
	}
}
