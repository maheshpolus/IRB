package org.mit.irb.web.IRBProtocol.service.Impl;

import java.util.List;
import java.util.concurrent.Future;
import org.hibernate.Criteria;
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
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "irbInitProtocolDao")
@Transactional
public class IRBProtocolInitLoadServImpl implements IRBProtocolInitLoadService{

	@Autowired
	IRBProtocolInitLoadDao irbProtocolInitLoadDao;
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@Async
	public Future<IRBProtocolVO> loadProtocolTypes(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolTypeCode"), "protocolTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolType.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolType> protocolType = criteria.list();
		irbProtocolVO.setProtocolType(protocolType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> loadRoleTypes(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(AgeGroups.class);
		List<AgeGroups> ageGroups = criteria.list();
		irbProtocolVO.setAgeGroups(ageGroups);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolPersonRoleTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolPersonRoleId"), "protocolPersonRoleId");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolPersonRoleTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolPersonRoleTypes> rolelType = criteria.list();
		irbProtocolVO.setPersonRoleTypes(rolelType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}


	@Async
	public Future<IRBProtocolVO> loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO){

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolPersonLeadUnits.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolPersonLeadUnits.class));
		criteria.addOrder(Order.asc("unitName"));
		List<ProtocolPersonLeadUnits> protocolPersonLeadUnits = criteria.list();
		irbProtocolVO.setProtocolPersonLeadUnits(protocolPersonLeadUnits);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}


	@Async
	public Future<IRBProtocolVO> loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolAffiliationTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("affiliationTypeCode"), "affiliationTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolAffiliationTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolAffiliationTypes> protocolAffiliationTypes = criteria.list();
		irbProtocolVO.setAffiliationTypes(protocolAffiliationTypes);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}


	@Async
	public Future<IRBProtocolVO> loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolFundingSourceTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("fundingSourceTypeCode"), "fundingSourceTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolFundingSourceTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolFundingSourceTypes> protocolFundingSourceTypes = criteria.list();
		irbProtocolVO.setProtocolFundingSourceTypes(protocolFundingSourceTypes);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}


	@Async
	public Future<IRBProtocolVO> loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CollaboratorNames.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("organizationId"), "organizationId");
		projList.add(Projections.property("organizationName"), "organizationName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CollaboratorNames.class));
		criteria.addOrder(Order.asc("organizationName"));
		List<CollaboratorNames> collaboratorNames = criteria.list();
		irbProtocolVO.setCollaboratorNames(collaboratorNames);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}
	
	@Override
	public IRBProtocolVO loadAttachmentType() {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolInitLoadDao.loadAttachmentType();
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolInitLoadDao.loadSponsorTypes(irbProtocolVO);
		return irbProtocolVO;
	}
	
	@Async
	public Future<IRBProtocolVO> loadProtocolAgeGroups(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolSubjectTypes.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("vulnerableSubjectTypeCode"), "vulnerableSubjectTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolSubjectTypes.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolSubjectTypes> protocolSubjectTypes = criteria.list();
		irbProtocolVO.setProtocolSubjectTypes(protocolSubjectTypes);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Override
	public IRBProtocolVO loadCommitteeList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolInitLoadDao.loadCommitteeList();
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCommitteeScheduleList() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolInitLoadDao.loadCommitteeScheduleList();
		return irbProtocolVO;
	}
}
