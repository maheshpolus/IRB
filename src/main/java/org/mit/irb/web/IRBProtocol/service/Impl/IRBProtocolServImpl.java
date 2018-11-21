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
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@Autowired
	@Qualifier(value="questionnaireService")
	QuestionnaireService questionnaireService;

	
	@Autowired
	@Qualifier(value="irbProtocolService")
	IRBProtocolService irbProtocolService;
	
	@Autowired
	IRBProtocolInitLoadService initLoadService;
	
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(IRBProtocolServImpl.class.getName());

	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBProtocolDetails(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBProtocolPersons(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolPersons(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBProtocolFundingSource(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolFundingSource(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBProtocolLocation(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolLocation(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBProtocolVulnerableSubject(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolVulnerableSubject(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBProtocolSpecialReview(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolSpecialReview(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getMITKCPersonInfo(String avPersonId) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getMITKCPersonInfo(avPersonId);
		return irbViewProfile;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(String attachmentId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadAttachments(attachmentId);
		return attachments;
	}

	@Override
	public IRBViewProfile getAttachmentsList(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getAttachmentsList(protocolNumber);
		return irbViewProfile;
	}
	
	@Override
	public IRBViewProfile getProtocolHistotyGroupList(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getProtocolHistotyGroupList(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId, Integer nextGroupActionId, Integer previousGroupActionId) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getProtocolHistotyGroupDetails(protocolId, actionId,nextGroupActionId, previousGroupActionId);
		return irbViewProfile;
	}		

	@Override
	public IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = irbProtocolDao.updateGeneralInfo(generalInfo);
		return irbProtocolVO;
	}

	

	@Override
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateProtocolPersonInfo(personnelInfo,generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateFundingSource(fundingSource);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateSubject(ProtocolSubject protocolSubject) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateSubject(protocolSubject);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateCollaborator(protocolCollaborator);
		return irbProtocolVO;
	}


	@Override
	public IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolDetails(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
		try {
			irbProtocolVO = irbProtocolDao.addProtocolAttachments(files,formDataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@SuppressWarnings("unused")
	@Override
	public IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber) {
		IRBProtocolVO irbProtocolVO = null;
		return irbProtocolVO = irbProtocolDao.loadIRBProtocolAttachmentsByProtocolNumber(protocolNumber);
	}

	@Override
	public IRBProtocolVO modifyProtocolDetails(Integer protocolId,IRBProtocolVO irbProtocolVO) {
		try{
			irbProtocolVO.setProtocolId(protocolId);
			ProtocolPersonnelInfo personnelInfo = new ProtocolPersonnelInfo();
			ProtocolLeadUnits protocolLeadUnits = new ProtocolLeadUnits();
			ProtocolFundingSource fundingSource= new ProtocolFundingSource();
		    ProtocolSubject protocolSubject= new ProtocolSubject();
		    ProtocolCollaborator protocolCollaborator = new ProtocolCollaborator();
		    
		    Future<IRBProtocolVO> protocolTypes = loadProtocolTypes(irbProtocolVO);
		    Future<IRBProtocolVO> roleTypes = loadRoleTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolPersonLeadunits = loadProtocolPersonLeadunits(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAffiliationTypes = loadProtocolAffiliationTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolSubjectTypes = loadProtocolSubjectTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAgeGroups = loadProtocolAgeGroups(irbProtocolVO);
		    Future<IRBProtocolVO> protocolFundingSourceTypes = loadProtocolFundingSourceTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolCollaboratorNames = loadProtocolCollaboratorNames(irbProtocolVO);
		    irbProtocolVO = protocolTypes.get();
			irbProtocolVO = roleTypes.get();
			irbProtocolVO = protocolPersonLeadunits.get();
			irbProtocolVO = protocolAffiliationTypes.get();
			irbProtocolVO = protocolSubjectTypes.get();
			irbProtocolVO = protocolAgeGroups.get();
			irbProtocolVO = protocolFundingSourceTypes.get();
			irbProtocolVO = protocolCollaboratorNames.get();
			irbProtocolVO = irbProtocolService.loadProtocolDetails(irbProtocolVO);
			irbProtocolVO.setPersonnelInfo(personnelInfo);
			irbProtocolVO.setProtocolLeadUnits(protocolLeadUnits);
			irbProtocolVO.setFundingSource(fundingSource);
			irbProtocolVO.setProtocolSubject(protocolSubject);
			irbProtocolVO.setProtocolCollaborator(protocolCollaborator);
		} catch (Exception e) {
			logger.error("Error in modifyProtocolDetails method : "+e.getMessage());
		}
		return irbProtocolVO;
	}
	
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
	public IRBProtocolVO saveScienceOfProtocol(ScienceOfProtocol scienceOfProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.saveScienceOfProtocol(irbProtocolVO,scienceOfProtocol);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
		try {
			irbProtocolVO = irbProtocolDao.addCollaboratorAttachments(files,formDataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.addCollaboratorPersons(protocolCollaboratorPersons);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.loadCollaboratorPersonsAndAttachments(collaboratorId);
		return irbProtocolVO;
	}
}
