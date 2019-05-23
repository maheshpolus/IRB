package org.mit.irb.web.IRBProtocol.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContactType;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolUnitType;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
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
	
	DBEngine dbEngine;
	
	IRBProtocolInitLoadServImpl() {
		dbEngine = new DBEngine();
	}
	
	Logger logger = Logger.getLogger(IRBProtocolInitLoadServImpl.class.getName());
	
	@Async
	public Future<IRBProtocolVO> loadProtocolTypes(IRBProtocolVO irbProtocolVO){
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolTypeCode"), "protocolTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolType.class));
		criteria.addOrder(Order.asc("description"));
		criteria.add(Restrictions.ne("protocolTypeCode", new String("4")));
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

	@Override
	public Future<IRBProtocolVO> loadProtocolUnitTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolUnitType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitTypeCode"), "unitTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolUnitType.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolUnitType> protocolUnitType = criteria.list();
		irbProtocolVO.setProtocolUnitType(protocolUnitType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Override
	public Future<IRBProtocolVO> loadProtocolAdminContactType(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolAdminContactType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("adminContactTypeCode"), "adminContactTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolAdminContactType.class));
		criteria.addOrder(Order.asc("description"));
		List<ProtocolAdminContactType> protocolAdminContactType = criteria.list();
		irbProtocolVO.setProtocolAdminContactType(protocolAdminContactType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}	
	
	@Override
	public Future<IRBUtilVO> loadProtocolSubmissionDetail(String protocolNumber,IRBUtilVO irbUtilVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			ArrayList<OutParameter> outputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));			
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTO_SUBMISSION_DTLS",outputParam);
			irbUtilVO.setProtocolSubmissionDetails(result.isEmpty() ? null :  result.get(0));
		}catch (Exception e) {
			logger.info("Exception in loadProtocolSubmissionDetail:" + e);
		}
		return new AsyncResult<>(irbUtilVO);
	}

	@Override
	public Future<IRBUtilVO> loadProtocolSubmissionReviewer(String protocolNumber, IRBUtilVO irbUtilVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			ArrayList<OutParameter> outputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));			
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> protocolSubmissionReviewers = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTO_SUBMSN_REVIEWERS",outputParam);
			irbUtilVO.setProtocolSubmissionReviewers(protocolSubmissionReviewers);
		}catch (Exception e) {
			logger.info("Exception in loadProtocolSubmissionReviewer:" + e);
		}
		return new AsyncResult<>(irbUtilVO);
	}

	@Override
	public Future<IRBUtilVO> loadProtocolRenewalDetails(String protocolNumber, String acTpye, IRBUtilVO irbUtilVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			ArrayList<OutParameter> outputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));	
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,acTpye));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> protocolRenewalDetails = dbEngine.executeProcedure(inputParam, "GET_IRB_SUBMSN_AMEND_RENWL_DTS",outputParam);
			ProtocolRenewalDetails protocolRenewalDetail = new ProtocolRenewalDetails();
			if(acTpye == "RENEWAL_AMEND" ){
				for(HashMap<String, Object> protocolDetailKey : protocolRenewalDetails){				
				   if(protocolDetailKey.get("DESCRIPTION") != null){
					switch (protocolDetailKey.get("DESCRIPTION").toString()) {
					case "General Info":
						protocolRenewalDetail.setGeneralInfo(true);
						break;
					case "Protocol Personnel":
						protocolRenewalDetail.setProtocolPersonel(true);
						break;
					case "Add/Modify Attachments":
						protocolRenewalDetail.setAddModifyNoteAttachments(true);
						break;
					case "Funding Source":
						protocolRenewalDetail.setFundingSource(true);
						break;
					case "Area of Research":
						protocolRenewalDetail.setAreaOfResearch(true);
						break;
					case "Protocol References":
						protocolRenewalDetail.setProtocolReferences(true);
						break;
					case "Special Review":
						protocolRenewalDetail.setSpecialReview(true);
						break;
					case "Protocol Organizations":
						protocolRenewalDetail.setProtocolOrganization(true);
						break;
					case "Subjects":
						protocolRenewalDetail.setSubject(true);
						break;
					case "Others":
						protocolRenewalDetail.setOther(true);
						break;
					case "Questionnaire":
						protocolRenewalDetail.setQuestionnaire(true);
						break;
					default:						
						break;
					}
				   }
				}		
				irbUtilVO.setProtocolRenewalDetail(protocolRenewalDetail);
			}else{
				irbUtilVO.setProtocolRenewalComments(protocolRenewalDetails.isEmpty() ? null :protocolRenewalDetails.get(0).get("SUMMARY").toString());
			}		
		}catch (Exception e) {
			logger.info("Exception in loadProtocolRenewalDetails:" + e);
		}
		return new AsyncResult<>(irbUtilVO);
	}

	@Override
	public Future<IRBUtilVO> loadProtocolReviewComments(String protocolNumber, IRBUtilVO irbUtilVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			ArrayList<OutParameter> outputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));			
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> protocolReviewerComments = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTO_SUBMSN_REVW_CMTS",outputParam);
			irbUtilVO.setProtocolReviewerComments(protocolReviewerComments);
		}catch (Exception e) {
			logger.info("Exception in loadProtocolReviewComments:" + e);
		}
		return new AsyncResult<>(irbUtilVO);
	}
	

	@Override
	public Future<IRBUtilVO> loadSubmissionCheckList(String protocolNumber, IRBUtilVO irbUtilVO) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			ArrayList<OutParameter> outputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));			
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> submissionCheckList = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTO_SUBMSN_CHECKLIST",outputParam);
			irbUtilVO.setSubmissionCheckListData(submissionCheckList);
		}catch (Exception e) {
			logger.info("Exception in loadProtocolReviewComments:" + e);
		}
		return new AsyncResult<>(irbUtilVO);
	}
}
