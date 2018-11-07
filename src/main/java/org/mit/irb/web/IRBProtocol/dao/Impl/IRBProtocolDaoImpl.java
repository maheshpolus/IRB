package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.AgeGroups;
import org.mit.irb.web.IRBProtocol.pojo.Award;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.EpsProposal;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.Proposal;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAffiliationTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAttachments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSourceTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonRoleTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubjectTypes;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolType;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.pojo.Sponsor;
import org.mit.irb.web.IRBProtocol.pojo.SponsorType;
import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.common.constants.KeyConstants;

import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "iRBProtocolDao")
@Transactional
public class IRBProtocolDaoImpl implements IRBProtocolDao {

	DBEngine dbEngine;

	@Autowired
	HibernateTemplate hibernateTemplate;

	IRBProtocolDaoImpl() {
		dbEngine = new DBEngine();
	}

	Logger logger = Logger.getLogger(IRBProtocolDaoImpl.class.getName());

	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_DETAILS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBProtocolDetails:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBProtocolDetails:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBProtocolDetails:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewHeader(result.get(0));
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolPersons(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_PERSONS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBprotocolPersons:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolPersons:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolPersons:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolPersons(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolFundingSource(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_FUNDING_SRC", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBprotocolFundingSource:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolFundingSource:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolFundingSource:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolFundingsource(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolLocation(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_LOCATION", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBprotocolLocation:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolLocation:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolLocation:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolLocation(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_VULNBLE_SUBJT", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBprotocolVulnerableSubject:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolVulnerableSubject:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolVulnerableSubject:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolVulnerableSubject(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_SPECIAL_REVW", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getIRBprotocolSpecialReview:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolSpecialReview:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolSpecialReview:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolSpecialReview(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getMITKCPersonInfo(String avPersonId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		ArrayList<HashMap<String, Object>> resultTraing = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_MITKC_PERSON_INFO", outputParam);
			if (result != null && !result.isEmpty()) {
				irbViewProfile.setIrbViewProtocolMITKCPersonInfo(result.get(0));
			}
			resultTraing = getMITKCPersonTraingInfo(avPersonId);
			if (resultTraing != null && !resultTraing.isEmpty()) {
				irbViewProfile.setIrbViewProtocolMITKCPersonTrainingInfo(resultTraing);
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getMITKCPersonInfo:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getMITKCPersonInfo:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getMITKCPersonInfo:" + e);
		}
		return irbViewProfile;
	}

	public ArrayList<HashMap<String, Object>> getMITKCPersonTraingInfo(String avPersonId) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_MITKC_PERSON_TRAINING_INFO", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getMITKCPersonTraingInfo:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getMITKCPersonTraingInfo:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getMITKCPersonTraingInfo:" + e);
		}
		return result;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(String attachmentId) {
		Integer attachmentsId = Integer.parseInt(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FIL_ID", DBEngineConstants.TYPE_INTEGER, attachmentsId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_MITKC_ATTACHMENT_FILE",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("FILE_DATA");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(hmResult.get("CONTENT_TYPE").toString()));
				String filename = hmResult.get("FILE_NAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadAttachments method:" + e);
		}
		return attachmentData;
	}

	@Override
	public IRBViewProfile getAttachmentsList(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_ATTACHMENT", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getAttachmentsList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getAttachmentsList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getAttachmentsList:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolAttachmentList(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupList(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_HISTORY_GROUP", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getProtocolHistotyGroupList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getProtocolHistotyGroupList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getProtocolHistotyGroupList:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolHistoryGroupList(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId,
			Integer nextGroupActionId, Integer previousGroupActionId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER, protocolId));
		inputParam.add(new InParameter("AV_ACTION_ID", DBEngineConstants.TYPE_INTEGER, actionId));
		inputParam.add(new InParameter("AV_NEXT_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, nextGroupActionId));
		inputParam.add(
				new InParameter("AV_PREVIOUS_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, previousGroupActionId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_HISTORY_DET", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getProtocolHistotyGroupDetails:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getProtocolHistotyGroupDetails:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getProtocolHistotyGroupDetails:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolHistoryGroupDetails(result);
		}
		return irbViewProfile;
	}
	

	@Override
	public IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		if(generalInfo.getProtocolNumber() == null){
			generalInfo.setActive("Y");
			generalInfo.setIslatest("Y");
			generalInfo.setProtocolStatusCode("100");
			String protocolNUmber = generateProtocolNumber();
			generalInfo.setProtocolNumber(protocolNUmber);
			generalInfo.setSequenceNumber(1);
			List<ProtocolPersonnelInfo> protocolPersonnelInfoList = new ArrayList<ProtocolPersonnelInfo>();
			ProtocolPersonnelInfo protocolPersonnelInfo = generalInfo.getPersonnelInfos().get(0);
			protocolPersonnelInfo.setProtocolGeneralInfo(generalInfo);
			protocolPersonnelInfo.setProtocolNumber(protocolNUmber);
			protocolPersonnelInfo.setSequenceNumber(1);
			protocolPersonnelInfoList.add(protocolPersonnelInfo);
			generalInfo.setPersonnelInfos(protocolPersonnelInfoList);
		}
		hibernateTemplate.saveOrUpdate(generalInfo);
		irbProtocolVO.setGeneralInfo(generalInfo);
		return irbProtocolVO;
	}

	private String generateProtocolNumber() {
		String generatedId = null;
		try {
			String prefix ="";
			Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH)+1;
	       
	        String currentMonth = String.valueOf(month);
	        if(currentMonth.length()>1){
	        	currentMonth = currentMonth;
	        } else{
	        	currentMonth = "0"+currentMonth;
	        }
	        prefix = String.valueOf(year).substring(2)+currentMonth;
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			SessionImpl sessionImpl = (SessionImpl) session;
			Connection connection = sessionImpl.connection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select SEQ_IRB_PROTOCOL_NUMBER.nextval from dual");
			if (rs.next()) {
				int id = rs.getInt(1);
				String Identifier = new Integer(id).toString();
				String newId = Identifier;
				if(Identifier.length()<6){
					int dif = 6- Identifier.length();
					for(int i=0;i<dif;i++){
						newId="0"+newId;
					}
				}
				generatedId = prefix + newId;
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedId;
	}

	@Override
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		personnelInfo.setSequenceNumber(1);
		if (personnelInfo.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(personnelInfo);
		} else if (personnelInfo.getAcType().equals("D")) {
			Query queryDeleteLeadUnits = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolLeadUnits p where  p.protocolUnitsId =:protocolUnitsId");
			queryDeleteLeadUnits.setInteger("protocolUnitsId",
					personnelInfo.getProtocolLeadUnits().get(0).getProtocolUnitsId());
			queryDeleteLeadUnits.executeUpdate();

			Query queryDeletePerson = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolPersonnelInfo p where  p.protocolPersonId =:protocolPersonId");
			queryDeletePerson.setInteger("protocolPersonId", personnelInfo.getProtocolPersonId());
			queryDeletePerson.executeUpdate();
		}

		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolGeneralInfo p where p.protocolId =:protocolId");
		queryGeneral.setInteger("protocolId", personnelInfo.getProtocolGeneralInfo().getProtocolId());
		ProtocolGeneralInfo protocolGeneralInfoObj = (ProtocolGeneralInfo) queryGeneral.list().get(0);
		irbProtocolVO.setProtocolPersonnelInfoList(protocolGeneralInfoObj.getPersonnelInfos());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		fundingSource.setSequenceNumber(1);
		if (fundingSource.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(fundingSource);
		} else {
			Query queryDelete = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("delete from ProtocolFundingSource p where p.protocolFundingSourceId =:protocolFundingSourceId");
			queryDelete.setInteger("protocolFundingSourceId", fundingSource.getProtocolFundingSourceId());
			queryDelete.executeUpdate();
		}
		irbProtocolVO = getProtocolFundingSource(fundingSource.getProtocolId(),irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateSubject(ProtocolSubject protocolSubject) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		protocolSubject.setSequenceNumber(1);
		if (protocolSubject.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(protocolSubject);
		} else {
			Query queryDelete = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"delete from ProtocolSubject p where p.protocolVulnerableSubId =:protocolVulnerableSubId");
			queryDelete.setInteger("protocolVulnerableSubId", protocolSubject.getProtocolVulnerableSubId());
			queryDelete.executeUpdate();
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolSubject p where p.protocolId =:protocolId");
		query.setInteger("protocolId", protocolSubject.getProtocolId());
		irbProtocolVO.setProtocolSubjectList(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/YYYY");
		protocolCollaborator.setSequenceNumber(1);
		if (protocolCollaborator.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(protocolCollaborator);
		} else {
			Query queryDelete = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolCollaborator p where p.protocolLocationId =:protocolLocationId");
			queryDelete.setInteger("protocolLocationId", protocolCollaborator.getProtocolLocationId());
			queryDelete.executeUpdate();
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolCollaborator p where p.protocolId =:protocolId");
		query.setInteger("protocolId", protocolCollaborator.getProtocolId());
		irbProtocolVO.setProtocolCollaboratorList(query.list());

		return irbProtocolVO;
	}

	

	@Override
	public IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		Integer protocolId = null;
		protocolId = irbProtocolVO.getProtocolId();
		ProtocolGeneralInfo protocolGeneralInfo = new ProtocolGeneralInfo();
		if (protocolId != null) {
			Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ProtocolGeneralInfo p where p.protocolId =:protocolId");
			queryGeneral.setInteger("protocolId", protocolId);
			if(!queryGeneral.list().isEmpty()){
				ProtocolGeneralInfo protocolGeneralInfoObj = (ProtocolGeneralInfo) queryGeneral.list().get(0);
				irbProtocolVO.setGeneralInfo(protocolGeneralInfoObj);
				irbProtocolVO.setProtocolPersonnelInfoList(protocolGeneralInfoObj.getPersonnelInfos());
			}
			Query queryScienceOfProtocol = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ScienceOfProtocol p where p.protocolId =:protocolId");
			queryScienceOfProtocol.setInteger("protocolId", protocolId);
			if(!queryScienceOfProtocol.list().isEmpty()){
				irbProtocolVO.setScienceOfProtocol((ScienceOfProtocol)queryScienceOfProtocol.list().get(0));
			}
			irbProtocolVO = getProtocolFundingSource(protocolId,irbProtocolVO);

			Query queryprotocolSubject = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ProtocolSubject p where p.protocolId =:protocolId");
			queryprotocolSubject.setInteger("protocolId", protocolId);
			irbProtocolVO.setProtocolSubjectList(queryprotocolSubject.list());
			
			Query queryCollaborator = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ProtocolCollaborator p where p.protocolId =:protocolId");
			queryCollaborator.setInteger("protocolId", protocolId);
			List<ProtocolCollaborator> collaborators = queryCollaborator.list();
			irbProtocolVO.setProtocolCollaboratorList(collaborators);
		} else {
			irbProtocolVO.setGeneralInfo(protocolGeneralInfo);
		}

		return irbProtocolVO;
	}

	private IRBProtocolVO getProtocolFundingSource(Integer protocolId,IRBProtocolVO irbProtocolVO) {
		try{
			Query queryfundingSource = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ProtocolFundingSource p where p.protocolId =:protocolId");
			queryfundingSource.setInteger("protocolId", protocolId);
			List<ProtocolFundingSource> fundingSourceList = queryfundingSource.list();
			if(!fundingSourceList.isEmpty()){
				fundingSourceList.forEach(protocolFundingSource -> {
					switch(protocolFundingSource.getFundingSourceTypeCode()){
					case KeyConstants.SPONSOR_FUNDING_SPONSOR_TYPE_CODE:
						Sponsor sponsor = fetchSponsorDetail(protocolFundingSource.getFundingSource());			
						protocolFundingSource.setSourceName(sponsor.getSponsorName());
						protocolFundingSource.setTitle(null);
					break;
					case KeyConstants.UNIT_FUNDING_SPONSOR_TYPE_CODE:
						Unit unit = fetchUnitDetail(protocolFundingSource.getFundingSource());
						protocolFundingSource.setSourceName(unit.getUnitName());
						protocolFundingSource.setTitle(null);
				    break;
					case KeyConstants.DEV_PROP_FUNDING_SPONSOR_TYPE_CODE:
						EpsProposal devProposal = fetchDevPropDetail(protocolFundingSource.getFundingSource()); 
						protocolFundingSource.setDocId(devProposal.getDocumentNumber());
						protocolFundingSource.setTitle(devProposal.getTitle());
						Sponsor devProposalsponsor = fetchSponsorDetail(devProposal.getSponsorCode());
						protocolFundingSource.setSourceName(devProposalsponsor.getSponsorName());
				    break;
					case KeyConstants.PROPOSAL_FUNDING_SPONSOR_TYPE_CODE:
						Proposal proposal = fetchProposal(protocolFundingSource.getFundingSource());
						protocolFundingSource.setTitle(proposal.getTitle());
						protocolFundingSource.setDocId(proposal.getDocumentNumber());
						Sponsor proposalsponsor = fetchSponsorDetail(proposal.getSponsorCode());
						protocolFundingSource.setSourceName(proposalsponsor.getSponsorName());
				    break;
					case KeyConstants.AWARD_FUNDING_SPONSOR_TYPE_CODE:
						Award award = fetchAwardDetail(protocolFundingSource.getFundingSource());
						protocolFundingSource.setDocId(award.getDocumentNumber());
						protocolFundingSource.setAwardId(award.getAwardId());
						protocolFundingSource.setTitle(award.getTitle());
						Sponsor awardSponsor = fetchSponsorDetail(award.getSponsorCode());	
						protocolFundingSource.setSourceName(awardSponsor.getSponsorName());
					break;
					}	
				});	
				irbProtocolVO.setProtocolFundingSourceList(fundingSourceList);
			}
		}catch(Exception e) {
			logger.error("Error in getProtocolFundingSource method"+e.getMessage());
		}
		return irbProtocolVO;
	}

	private Proposal fetchProposal(String fundingSource) {
		Query queryProposal = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Proposal p1 where p1.proposalNumber =:proposalNumber and p1.sequenceNumber = (select max(p2.sequenceNumber) from Proposal p2 where p2.proposalNumber = p1.proposalNumber)");
		queryProposal.setString("proposalNumber", fundingSource);
		Proposal proposal = (Proposal) queryProposal.list().get(0);
		return proposal;
	}

	private EpsProposal fetchDevPropDetail(String fundingSource) {
		Query queryDevProposal = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from EpsProposal dp where dp.proposalNumber =:proposalNumber");
		queryDevProposal.setString("proposalNumber", fundingSource);
		EpsProposal devProposal = (EpsProposal) queryDevProposal.list().get(0);					
		return devProposal;
	}

	private Unit fetchUnitDetail(String fundingSource) {
		Query queryUnitDetails = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Unit u where u.unitNumber =:unitNumber");
		queryUnitDetails.setString("unitNumber", fundingSource);
		Unit unit = (Unit) queryUnitDetails.list().get(0);
		return unit;
	}

	private Sponsor fetchSponsorDetail(String fundingSource) {
		Query querySponsorDetails = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Sponsor s where s.sponsorCode =:sponsorCode");
		querySponsorDetails.setString("sponsorCode", fundingSource);
		Sponsor sponsor = (Sponsor) querySponsorDetails.list().get(0);
		return sponsor;
	}

	private Award fetchAwardDetail(String fundingSource) {
		Query queryAwardDetails = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Award a where a.awardNumber =:awardNumber and a.awardSequenceStatus =:awardSequenceStatus");
		queryAwardDetails.setString("awardNumber", fundingSource);
		queryAwardDetails.setString("awardSequenceStatus", KeyConstants.AWARD_SEQUENCE_STATUS_ACTIVE);
		Award award = (Award) queryAwardDetails.list().get(0); 
		return award;
	}

	@Override
	public IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson)
			throws JsonParseException, JsonMappingException, IOException {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		ObjectMapper mapper = new ObjectMapper();
		IRBAttachmentProtocol attachmentProtocol = mapper.readValue(formDataJson, IRBAttachmentProtocol.class);
		if (attachmentProtocol.getAcType().equals("I")) {
			for (int i = 0; i < files.length; i++) {
				IRBAttachmentProtocol irbAttachmentProtocol = new IRBAttachmentProtocol();
				irbAttachmentProtocol.setAttachementStatus(attachmentProtocol.getAttachementStatus());
				irbAttachmentProtocol.setAttachmentType(attachmentProtocol.getAttachmentType());
				irbAttachmentProtocol.setAttachmentVersion(attachmentProtocol.getAttachmentVersion());
				irbAttachmentProtocol.setCreateTimestamp(attachmentProtocol.getCreateTimestamp());
				irbAttachmentProtocol.setDescription(attachmentProtocol.getDescription());
				irbAttachmentProtocol.setDocumentId(attachmentProtocol.getDocumentId());
				irbAttachmentProtocol.setProtocolGeneralInfo(attachmentProtocol.getProtocolGeneralInfo());
				irbAttachmentProtocol.setSequenceNumber(attachmentProtocol.getSequenceNumber());
				irbAttachmentProtocol.setProtocolNumber(attachmentProtocol.getProtocolNumber());
				irbAttachmentProtocol.setStatusCode(attachmentProtocol.getStatusCode());
				irbAttachmentProtocol.setTypeCode(attachmentProtocol.getTypeCode());
				irbAttachmentProtocol.setUpdateTimestamp(attachmentProtocol.getUpdateTimestamp());
				irbAttachmentProtocol.setUpdateUser(attachmentProtocol.getUpdateUser());
				ProtocolAttachments attachments = new ProtocolAttachments();
				attachments.setContentType(files[i].getContentType());
				attachments.setFileName(files[i].getOriginalFilename());
				attachments.setFileData(files[i].getBytes());
				attachments.setSequenceNumber(attachmentProtocol.getProtocolAttachment().getSequenceNumber());
				attachments.setUpdateTimestamp(attachmentProtocol.getProtocolAttachment().getUpdateTimestamp());
				attachments.setUpdateUser(attachmentProtocol.getUpdateUser());
				irbAttachmentProtocol.setProtocolAttachment(attachments);
				hibernateTemplate.saveOrUpdate(irbAttachmentProtocol);
			} 
		}else if(attachmentProtocol.getAcType().equals("U")){
				ProtocolAttachments attachments = attachmentProtocol.getProtocolAttachment();
				for (int i = 0; i < files.length; i++) {
					attachments.setContentType(files[i].getContentType());
					attachments.setFileName(files[i].getOriginalFilename());
					attachments.setFileData(files[i].getBytes());
					attachments.setUpdateTimestamp(attachmentProtocol.getProtocolAttachment().getUpdateTimestamp());
					attachments.setUpdateUser(attachmentProtocol.getUpdateUser());
				}
				hibernateTemplate.saveOrUpdate(attachmentProtocol);
		}else if (attachmentProtocol.getAcType().equals("D")) {
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("delete from IRBAttachmentProtocol p where p.paProtocolId =:paProtocolId");
			queryDeletAttachment.setInteger("paProtocolId", attachmentProtocol.getPaProtocolId());
			queryDeletAttachment.executeUpdate();
			Query queryDeletProtocolAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("delete from ProtocolAttachments p where p.fileId =:fileId");
			queryDeletProtocolAttachment.setInteger("fileId", attachmentProtocol.getProtocolAttachment().getFileId());
			queryDeletProtocolAttachment.executeUpdate();
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachmentProtocol p where p.protocolNumber =:protocolNumber");
		query.setString("protocolNumber", attachmentProtocol.getProtocolNumber());
		irbProtocolVO.setProtocolAttachmentList(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachmentProtocol p where p.protocolNumber =:protocolNumber");
		query.setString("protocolNumber", protocolNumber);
		irbProtocolVO.setProtocolAttachmentList(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol) {
		hibernateTemplate.saveOrUpdate(scienceOfProtocol);
		irbProtocolVO.setScienceOfProtocol(scienceOfProtocol);
		return irbProtocolVO;
	}

}
