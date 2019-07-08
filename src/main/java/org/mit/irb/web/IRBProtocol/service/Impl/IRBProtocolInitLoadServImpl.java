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
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.VO.SubmissionDetailVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.IRBProtocol.pojo.AdminCheckListDetail;
import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.FDARiskLevel;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolRiskLevel;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContactType;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolUnitType;
import org.mit.irb.web.IRBProtocol.pojo.RiskLevel;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.roles.pojo.Role;
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
	
	@Async
	public Future<SubmissionDetailVO> loadSubmissionTypes(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> submissionTypeList = dbEngine.executeProcedure("GET_IRB_SUBMISSION_TYPE",outputParam);
			submissionDetailvo.setSubmissionTypeList(submissionTypeList);
		}catch (Exception e) {
			logger.info("Exception in loadSubmissionTypes:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadsubmissionRewiewType(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> submissionReviewTypeList = dbEngine.executeProcedure("GET_IRB_REVIEW_TYPE",outputParam);
			submissionDetailvo.setSubmissionRewiewTypeList(submissionReviewTypeList);
		}catch (Exception e) {
			logger.info("Exception in loadsubmissionRewiewType:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadCommitteeList(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> committeeList = dbEngine.executeProcedure("GET_IRB_COMMITTEE_DETAILS",outputParam);
			submissionDetailvo.setCommitteeList(committeeList);
		}catch (Exception e) {
			logger.info("Exception in loadCommitteeList:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadTypeQualifierList(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> typeQualifierList = dbEngine.executeProcedure("GET_IRB_SUB_TYPE_QUALIFIER",outputParam);
			submissionDetailvo.setTypeQualifierList(typeQualifierList);
		}catch (Exception e) {
			logger.info("Exception in loadTypeQualifierList:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadIRBAdminRewiewType(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> irbAdminReviewTypeList = dbEngine.executeProcedure("GET_IRB_ADMIN_REVIEW_TYPE",outputParam);
			submissionDetailvo.setIrbAdminsReviewerType(irbAdminReviewTypeList);
		}catch (Exception e) {
			logger.info("Exception in loadIRBAdminRewiewType:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadIRBAdminList(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();		
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> irbAdminList = dbEngine.executeProcedure("GET_IRB_ADMINISTRATORS",outputParam);
			submissionDetailvo.setIrbAdminsList(irbAdminList);
		}catch (Exception e) {
			logger.info("Exception in loadIRBAdminList:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadIRBAdminComments(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> irbAdminCommentList = dbEngine.executeProcedure(inputParam,"GET_IRB_PROTO_ADMIN_REVW_CMMNT",outputParam);
			submissionDetailvo.setIrbAdminCommentList(irbAdminCommentList);
		}catch (Exception e) {
			logger.info("Exception in loadIRBAdminComments:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadIRBAdminAttachments(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> irbAdminAttachmentList = dbEngine.executeProcedure(inputParam,"GET_IRB_PROTO_ADMIN_REVW_ATMNT",outputParam);
			submissionDetailvo.setIrbAdminAttachmentList(irbAdminAttachmentList);
		}catch (Exception e) {
			logger.info("Exception in loadIRBAdminAttachments:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Override
	public Future<SubmissionDetailVO> loadIRBAdminCHeckList(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> irbAdmincheckList = dbEngine.executeProcedure(inputParam,"GET_IRB_ADMIN_REVIEW_CHECKLIST",outputParam);
			List<AdminCheckListDetail> adminCheckListDetail = iterateCheckList(irbAdmincheckList);
			submissionDetailvo.setIrbAdminCheckList(adminCheckListDetail);
		}catch (Exception e) {
			logger.info("Exception in loadIRBAdminAttachments:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	private List<AdminCheckListDetail> iterateCheckList(ArrayList<HashMap<String, Object>> irbAdmincheckList) {
		List<AdminCheckListDetail> adminCheckListDetails = new ArrayList<AdminCheckListDetail>();
		try{
			for(HashMap<String, Object> admincheckList : irbAdmincheckList){
				AdminCheckListDetail adminCheckListDetail = new AdminCheckListDetail();
				adminCheckListDetail.setAdminCheckListId(admincheckList.get("ADMIN_CHKLST_ID") == null ? null : Integer.parseInt(admincheckList.get("ADMIN_CHKLST_ID").toString()));
				adminCheckListDetail.setGroupName(admincheckList.get("GROUP_NAME") == null ? null : admincheckList.get("GROUP_NAME").toString());
				adminCheckListDetail.setDescription(admincheckList.get("CHKLST_DESC").toString());
				adminCheckListDetail.setAdminRevChecklistCode(Integer.parseInt(admincheckList.get("ADMIN_REV_CHKLST_CODE").toString()));
				adminCheckListDetail.setStatusFlag(admincheckList.get("STATUS_FLAG").equals("N") ? false : true);
				adminCheckListDetail.setUpdateTimestamp(admincheckList.get("UPDATE_TIMESTAMP") == null ? null : admincheckList.get("UPDATE_TIMESTAMP").toString());
				adminCheckListDetail.setUpdateUser(admincheckList.get("UPDATE_USER") == null ? null :admincheckList.get("UPDATE_USER").toString());
				adminCheckListDetail.setUserDescription(admincheckList.get("USER_DESCRIPTION") == null ? null :admincheckList.get("USER_DESCRIPTION").toString());
				adminCheckListDetails.add(adminCheckListDetail);
			}
		}catch (Exception e) {
			logger.info("Exception in iterateCheckList : " + e);
		}
		return adminCheckListDetails;
	}

	@Async
	public Future<SubmissionDetailVO> loadCommitteeRewiewType(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();	
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> committeeReviewType = dbEngine.executeProcedure("GET_IRB_COMMITTEE_REVIEWR_TYPE",outputParam);
			submissionDetailvo.setCommitteeReviewType(committeeReviewType);
		}catch (Exception e) {
			logger.info("Exception in loadCommitteeRewiewType:" + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> fetchsubmissionBasicDetail(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> submissionDetails = dbEngine.executeProcedure(inputParam,"get_irb_submission_details",outputparam);
			submissionDetailvo.setSubmissionDetail(submissionDetails.get(0));
		}catch (Exception e) {
			logger.info("Exception in fetchsubmissionBasicDetail: " + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> fetchCommitteeReviewers(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_SUBMISSION_ID", DBEngineConstants.TYPE_INTEGER,submissionDetailvo.getSubmissionId()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> committeeReviwer = dbEngine.executeProcedure(inputParam,"GET_IRB_COMMITTEE_REVIEWRES",outputparam);
			submissionDetailvo.setCommitteeReviewers(committeeReviwer);
		}catch (Exception e) {
			logger.info("Exception in fetchsubmissionBasicDetail: " + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<SubmissionDetailVO> loadrecomendedActionType(SubmissionDetailVO submissionDetailvo) {
		try{
			ArrayList<OutParameter> outputParam  = new ArrayList<OutParameter>();	
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> recomendedActionType = dbEngine.executeProcedure("get_irb_recommend_actions",outputParam);
			submissionDetailvo.setRecomendedActionType(recomendedActionType);
		}catch (Exception e) {
			logger.info("Exception in loadrecomendedActionType: " + e);
		}
		return new AsyncResult<>(submissionDetailvo);
	}
	
	@Async
	public Future<IRBActionsVO> getSubmissionTypeQulifier(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> submissionTypeQulifier = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			submissionTypeQulifier=dbEngine.executeProcedure("GET_IRB_SUB_TYPE_QUALIFIER",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getSubmissionTypeQulifier:" + e);	
		}
		vo.setNotifyTypeQualifier(submissionTypeQulifier);
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getScheduleDates(IRBActionsVO vo,String committeeId) {
		ArrayList<HashMap<String, Object>> scheduleDates = null;														
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,committeeId));
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			scheduleDates=dbEngine.executeProcedure(inputParam,"GET_IRB_SCHEDULE_DATE",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getScheduleDates:" + e);	
		}
		vo.setScheduleDates(scheduleDates);		
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getCommitteeList(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> committeeList = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			committeeList = dbEngine.executeProcedure("GET_IRB_COMMITTEE_DETAILS",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getCommitteeList:" + e);	
		}	
		vo.setCommitteeList(committeeList);
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getRiskLevelType(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> riskLevel = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			riskLevel = dbEngine.executeProcedure("GET_IRB_RISK_LEVEL",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getRiskLevel:" + e);	
		}
		vo.setRiskLevelType(riskLevel);
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getExpeditedApprovalCheckList(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> expeditedApprovalCheckList = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			expeditedApprovalCheckList = dbEngine.executeProcedure("GET_IRB_EXPEDITED_CHECKLIST",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getExpeditedApprovalCheckList:" + e);	
		}
		vo.setExpeditedApprovalCheckList(expeditedApprovalCheckList);
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getExpeditedCannedComments(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> expeditedCannedComments = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			expeditedCannedComments = dbEngine.executeProcedure("GET_IRB_EXP_CANNED_COMMENT",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getExpeditedCannedComments:" + e);	
		}
		vo.setExpeditedCannedComments(expeditedCannedComments);
		return new AsyncResult<>(vo);
	}
	
	@Override
	public Future<SubmissionDetailVO> getScheduleDates(SubmissionDetailVO submissionDetailvo, String committeeId) {
		ArrayList<HashMap<String, Object>> scheduleDates = null;														
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING,committeeId));
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			scheduleDates=dbEngine.executeProcedure(inputParam,"GET_IRB_SCHEDULE_DATE",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getScheduleDates:" + e);	
		}
		submissionDetailvo.setScheduleDates(scheduleDates);		
		return new AsyncResult<>(submissionDetailvo);
	}

	@Async
	public Future<IRBProtocolVO> loadRiskLevelTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(RiskLevel.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("description"), "description");
		projList.add(Projections.property("riskLevelCode"), "riskLevelCode");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(RiskLevel.class));
		criteria.addOrder(Order.asc("description"));
		List<RiskLevel> riskLevelType = criteria.list();
		irbProtocolVO.setRiskLevelType(riskLevelType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Override
	public Future<IRBProtocolVO> loadFDARiskLevelTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(FDARiskLevel.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("description"), "description");
		projList.add(Projections.property("fdaRiskLevelCode"), "fdaRiskLevelCode");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(FDARiskLevel.class));
		criteria.addOrder(Order.asc("description"));
		List<FDARiskLevel> fdaRiskLevelType = criteria.list();
		irbProtocolVO.setFdaRiskLevelType(fdaRiskLevelType);
		session.flush();
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBActionsVO> getFDARiskLevelType(IRBActionsVO vo) {
		ArrayList<HashMap<String, Object>> fdaRiskLevel = null;														
		try {
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();						
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			fdaRiskLevel = dbEngine.executeProcedure("get_irb_fda_risk_level",outputparam);
		} catch (Exception e) {
			logger.info("Exception in getFDARiskLevel:" + e);	
		}
		vo.setFdaRiskLevelType(fdaRiskLevel);
		return new AsyncResult<>(vo);
	}

	@Async
	public Future<IRBActionsVO> getPreviousRiskLevel(IRBActionsVO vo) {
		try{
			ArrayList<InParameter> inputParam = new ArrayList<InParameter>();
			ArrayList<OutParameter> outputparam = new ArrayList<OutParameter>();	
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,vo.getProtocolNumber()));
			outputparam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> previousRiskLevel = dbEngine.executeProcedure(inputParam,"GET_IRB_PROTO_PREV_RISK_LEVEL",outputparam);
			vo.setRiskLevelDetail(iterateRiskLevel(previousRiskLevel));
		} catch (Exception e) {
			logger.info("Exception in getPreviousRiskLevel:" + e);	
		}
		return new AsyncResult<>(vo);
	}

	private IRBProtocolRiskLevel iterateRiskLevel(ArrayList<HashMap<String, Object>> hashMap) {
		IRBProtocolRiskLevel protocolRiskLevel = new IRBProtocolRiskLevel();
		try{
			if(hashMap != null && !hashMap.isEmpty()){
				protocolRiskLevel.setFdaRiskLevelCode(hashMap.get(0).get("FDA_RISK_LEVEL_CODE") != null ? hashMap.get(0).get("FDA_RISK_LEVEL_CODE").toString() : null);
				protocolRiskLevel.setFdaRiskLevelComment(hashMap.get(0).get("FDA_RISK_LVL_COMMENTS") != null ? hashMap.get(0).get("FDA_RISK_LVL_COMMENTS").toString() : null);
				protocolRiskLevel.setFdariskLevelDateAssigned(hashMap.get(0).get("FDA_RISK_LVL_DATE_ASSIGNED") != null ? hashMap.get(0).get("FDA_RISK_LVL_DATE_ASSIGNED").toString() : null);
				protocolRiskLevel.setRiskLevelCode(hashMap.get(0).get("RISK_LEVEL_CODE") != null ? hashMap.get(0).get("RISK_LEVEL_CODE").toString() : null);
				protocolRiskLevel.setRiskLevelComment(hashMap.get(0).get("RISK_LEVEL_COMMENTS") != null ? hashMap.get(0).get("RISK_LEVEL_COMMENTS").toString() : null);
				protocolRiskLevel.setRiskLevelDateAssigned(hashMap.get(0).get("RISK_LVL_DATE_ASSIGNED") != null ?hashMap.get(0).get("RISK_LVL_DATE_ASSIGNED").toString() : null);
			}	
		} catch (Exception e) {
			logger.info("Exception in iterateRiskLevel:" + e);	
		}
		return protocolRiskLevel;
	}

	@Override
	public IRBProtocolVO loadCollaboratorAttachmentType() {			
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolInitLoadDao.loadCollaboratorAttachmentType();
		return irbProtocolVO;
	}

	@Async
	public Future<IRBPermissionVO> loadProtocolPersonPermissions(IRBPermissionVO vo) {
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Role.class);
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.property("roleId"), "roleId");
			projList.add(Projections.property("roleName"), "roleName");
			criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Role.class));
			criteria.add(Restrictions.lt("roleId", 3));   // Restrictions.eq("roleId",1)).add(Restrictions.eq("roleId",2));
			criteria.addOrder(Order.asc("roleId"));
			List<Role> role = criteria.list();
			vo.setProtocolRoles(role);
			session.flush();	
		} catch (Exception e) {
			logger.info("Exception in loadProtocolPersonPermissions:" + e);	
		}
		return new AsyncResult<>(vo);
	}
}
