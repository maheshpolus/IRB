package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.SponsorType;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolTypeCode"), "protocolTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolType.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolType> protocolType = criteria.list();
		logger.info("Protocol Type in DAO: " + protocolType);
		irbProtocolVO.setProtocolType(protocolType);
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AgeGroups.class);
		List<AgeGroups> ageGroups = criteria.list();
		logger.info("Protocol Type in DAO: " + ageGroups);
		irbProtocolVO.setAgeGroups(ageGroups);
		return irbProtocolVO;
	}
	
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
	public IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolPersonRoleTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolPersonRoleId"), "protocolPersonRoleId");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolPersonRoleTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolPersonRoleTypes> rolelType = criteria.list();
		logger.info("Role Type in DAO: " + rolelType);
		irbProtocolVO.setPersonRoleTypes(rolelType);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolPersonLeadUnits.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolPersonLeadUnits.class));
		criteria.addOrder(Order.asc("unitName"));
		List<ProtocolPersonLeadUnits> protocolPersonLeadUnits = criteria.list();
		logger.info("Leadunits in DAO: " + protocolPersonLeadUnits);
		irbProtocolVO.setProtocolPersonLeadUnits(protocolPersonLeadUnits);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolAffiliationTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("affiliationTypeCode"), "affiliationTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolAffiliationTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolAffiliationTypes> protocolAffiliationTypes = criteria.list();
		logger.info("protocolAffiliationTypes in DAO: " + protocolAffiliationTypes);
		irbProtocolVO.setAffiliationTypes(protocolAffiliationTypes);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolSubjectTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("vulnerableSubjectTypeCode"), "vulnerableSubjectTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolSubjectTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolSubjectTypes> protocolSubjectTypes = criteria.list();
		logger.info("protocolSubjectTypes in DAO: " + protocolSubjectTypes);
		irbProtocolVO.setProtocolSubjectTypes(protocolSubjectTypes);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolFundingSourceTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("fundingSourceTypeCode"), "fundingSourceTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList)
				.setResultTransformer(Transformers.aliasToBean(ProtocolFundingSourceTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolFundingSourceTypes> protocolFundingSourceTypes = criteria.list();
		logger.info("protocolFundingSourceTypes in DAO: " + protocolFundingSourceTypes);
		irbProtocolVO.setProtocolFundingSourceTypes(protocolFundingSourceTypes);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CollaboratorNames.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("organizationId"), "organizationId");
		projList.add(Projections.property("organizationName"), "organizationName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CollaboratorNames.class));
		criteria.addOrder(Order.asc("organizationName"));
		List<CollaboratorNames> collaboratorNames = criteria.list();
		logger.info("collaboratorNames in DAO: " + collaboratorNames);
		irbProtocolVO.setCollaboratorNames(collaboratorNames);
		return irbProtocolVO;
	}
}
