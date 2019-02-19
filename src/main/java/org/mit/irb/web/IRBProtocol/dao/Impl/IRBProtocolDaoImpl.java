package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.Award;
import org.mit.irb.web.IRBProtocol.pojo.EpsProposal;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.Proposal;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAttachments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorAttachments;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.pojo.Sponsor;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
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
			for(HashMap<String, Object> personInfo: result){
				ArrayList<InParameter> inParam = new ArrayList<>();
				ArrayList<OutParameter> outParam = new ArrayList<>();
				outParam.add(new OutParameter("trainingStatus", DBEngineConstants.TYPE_INTEGER));
				if(personInfo!=null){
					inParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, personInfo.get("PERSON_ID")));
					try {
						ArrayList<HashMap<String, Object>> trainingStatus = dbEngine.executeFunction(inParam,"fn_irb_per_training_completed", outParam);
						String trainingInfo = (String) trainingStatus.get(0).get("trainingStatus");
						if(trainingInfo.equals("1")){
							personInfo.put("IS_TRAINING_COMPLETED", "COMPLETED");
						} else{
							personInfo.put("IS_TRAINING_COMPLETED", "INCOMPLETE");
						}
					} catch (DBException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
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
			outputParam.remove(0);
			outputParam.add(new OutParameter("trainingStatus", DBEngineConstants.TYPE_INTEGER));
					ArrayList<HashMap<String, Object>> trainingStatus = dbEngine.executeFunction(inputParam,"fn_irb_per_training_completed", outputParam);
					String trainingInfo = (String) trainingStatus.get(0).get("trainingStatus");
					if(trainingInfo.equals("1")){
						irbViewProfile.setTrainingStatus("COMPLETED");
					} else{
						irbViewProfile.setTrainingStatus("INCOMPLETE");
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
	public ResponseEntity<byte[]> loadProtocolHistoryCorrespondanceLetter(Integer protocolActionId) {

		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FIL_ID", DBEngineConstants.TYPE_INTEGER, protocolActionId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_PROTO_CORRESP_LETTER", outParam);
			if (result != null && !result.isEmpty()) {
				logger.info("Correspondence letter data exists");
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("CORRESPONDENCE");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename = "TEST_NAME";
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
	public IRBViewProfile loadProtocolHistoryActionComments(String protocolNumber, Integer protocolActionId,
			String protocolActionTypecode) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		inputParam.add(new InParameter("AV_PROTOCOL_ACTION_ID", DBEngineConstants.TYPE_INTEGER, protocolActionId));
		inputParam.add(
				new InParameter("AV_PROTOCOL_ACTION_TYPE_CODE", DBEngineConstants.TYPE_STRING, protocolActionTypecode));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_ACTN_COMMENTS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in loadProtocolHistoryCorrespondenceComments:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in loadProtocolHistoryCorrespondenceComments:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in loadProtocolHistoryCorrespondenceComments:" + e);
		}
		if (result != null && !result.isEmpty()) {
			logger.info("Action comments exists");
			irbViewProfile.setIrbProtocolHistoryActionComments(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile checkingPersonsRightToViewProtocol(String personId, String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		Integer userHasRight = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
			inParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
			outParam.add(new OutParameter("hasRight", DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction(inParam,
					"fn_irb_pers_has_right_in_proto", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				userHasRight = Integer.parseInt((String) hmResult.get("hasRight"));
				irbViewProfile.setUserHasRightToViewProtocol(userHasRight);
			}
		} catch (Exception e) {
			logger.error("Error in methord checkingPersonsRightToViewProtocol", e);
		}
		return irbViewProfile;
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
		if (generalInfo.getProtocolNumber() == null) {
			generalInfo.setActive("Y");
			generalInfo.setIslatest("Y");
			generalInfo.setProtocolStatusCode("100");
			String protocolNUmber = generateProtocolNumber();
			generalInfo.setProtocolNumber(protocolNUmber);
			generalInfo.setSequenceNumber(1);
			generalInfo.setCreateTimestamp(generalInfo.getUpdateTimestamp()); //since when creating a protocol both created user and updated user is same
			generalInfo.setCreateUser(generalInfo.getUpdateUser());
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
			String prefix = "";
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;

			String currentMonth = String.valueOf(month);
			if (currentMonth.length() > 1) {
				currentMonth = currentMonth;
			} else {
				currentMonth = "0" + currentMonth;

			}
			prefix = String.valueOf(year).substring(2) + currentMonth;
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			SessionImpl sessionImpl = (SessionImpl) session;
			Connection connection = sessionImpl.connection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select SEQ_IRB_PROTOCOL_NUMBER.nextval from dual");
			if (rs.next()) {
				int id = rs.getInt(1);
				String Identifier = new Integer(id).toString();
				String newId = Identifier;
				if (Identifier.length() < 6) {
					int dif = 6 - Identifier.length();
					for (int i = 0; i < dif; i++) {
						newId = "0" + newId;
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
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo,
			ProtocolGeneralInfo generalInfo) {
		logger.info(".................Updating person Informations..................");
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		personnelInfo.setSequenceNumber(1);
		if (personnelInfo.getAcType().equals("U")) {
			personnelInfo.setProtocolGeneralInfo(generalInfo);
			Session sessionUpdatePersons = hibernateTemplate.getSessionFactory().openSession();
			Transaction transactionUpdatePersons = sessionUpdatePersons.beginTransaction();
			hibernateTemplate.saveOrUpdate(personnelInfo);
			transactionUpdatePersons.commit();
			sessionUpdatePersons.close();
		} else if (personnelInfo.getAcType().equals("D")) {
			logger.info("Deleting person Info");
			Query queryDeleteLeadUnits = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolLeadUnits p where  p.protocolUnitsId =:protocolUnitsId");
			queryDeleteLeadUnits.setInteger("protocolUnitsId",
					personnelInfo.getProtocolLeadUnits().get(0).getProtocolUnitsId());
			queryDeleteLeadUnits.executeUpdate();

			Query queryDeleteCollaboratorPerson = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolCollaboratorPersons p where  p.personId =:personId");
			queryDeleteCollaboratorPerson.setInteger("personId", personnelInfo.getProtocolPersonId());
			queryDeleteCollaboratorPerson.executeUpdate();
			Query queryDeletePerson = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolPersonnelInfo p where  p.protocolPersonId =:protocolPersonId");
			queryDeletePerson.setInteger("protocolPersonId", personnelInfo.getProtocolPersonId());
			queryDeletePerson.executeUpdate();
		}
		Query queryPersonList = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolPersonnelInfo p where p.protocolNumber =:protocolNumber");
		queryPersonList.setString("protocolNumber", personnelInfo.getProtocolNumber());
		List<ProtocolPersonnelInfo> protocolPersonnelInfoList = queryPersonList.list();
		irbProtocolVO.setProtocolPersonnelInfoList(protocolPersonnelInfoList);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		fundingSource.setSequenceNumber(1);
		if (fundingSource.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(fundingSource);
		} else {
			Query queryDelete = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"delete from ProtocolFundingSource p where p.protocolFundingSourceId =:protocolFundingSourceId");
			queryDelete.setInteger("protocolFundingSourceId", fundingSource.getProtocolFundingSourceId());
			queryDelete.executeUpdate();
		}
		try {
			irbProtocolVO = getProtocolFundingSource(fundingSource.getProtocolId(), irbProtocolVO).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
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
		protocolCollaborator.setSequenceNumber(1);
		if (protocolCollaborator.getAcType().equals("U")) {
			hibernateTemplate.saveOrUpdate(protocolCollaborator);
		} else if (protocolCollaborator.getAcType().equals("D")) {
			Session sessionDelPersons = hibernateTemplate.getSessionFactory().openSession();
			Transaction transactionDelPersons = sessionDelPersons.beginTransaction();
			Query queryDelPersons = sessionDelPersons
					.createQuery("delete from ProtocolCollaboratorPersons p where p.collaboratorId =:collaboratorId");
			queryDelPersons.setInteger("collaboratorId", protocolCollaborator.getProtocolLocationId());
			queryDelPersons.executeUpdate();
			transactionDelPersons.commit();
			sessionDelPersons.close();

			Session sessionDeleAttachments = hibernateTemplate.getSessionFactory().openSession();
			Transaction transactionDeleAttachments = sessionDeleAttachments.beginTransaction();
			Query queryDeleAttachments = sessionDeleAttachments.createQuery(
					"delete from ProtocolCollaboratorAttachments p where p.collaboratorId =:collaboratorId");
			queryDeleAttachments.setInteger("collaboratorId", protocolCollaborator.getProtocolLocationId());
			queryDeleAttachments.executeUpdate();
			transactionDeleAttachments.commit();
			sessionDeleAttachments.close();

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
			getGeneralPersonnelInfoList(irbProtocolVO);
			getSubjectoList(irbProtocolVO);
			getScienceOfProtocol(irbProtocolVO);
			getCollaboratorList(irbProtocolVO);
			getProtocolFundingSource(protocolId, irbProtocolVO);
		} else {
			irbProtocolVO.setGeneralInfo(protocolGeneralInfo);
		}
		return irbProtocolVO;
	}

	@Async
	public Future<IRBProtocolVO> getGeneralPersonnelInfoList(IRBProtocolVO irbProtocolVO) {
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolGeneralInfo p where p.protocolId =:protocolId");
		queryGeneral.setInteger("protocolId", irbProtocolVO.getProtocolId());
		if (!queryGeneral.list().isEmpty()) {
			ProtocolGeneralInfo protocolGeneralInfoObj = (ProtocolGeneralInfo) queryGeneral.list().get(0);
			irbProtocolVO.setGeneralInfo(protocolGeneralInfoObj);
			irbProtocolVO.setProtocolPersonnelInfoList(protocolGeneralInfoObj.getPersonnelInfos());
			System.out.println("general info is set " + new Date());
		}
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> getCollaboratorList(IRBProtocolVO irbProtocolVO) {
		Query queryCollaborator = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolCollaborator p where p.protocolId =:protocolId");
		queryCollaborator.setInteger("protocolId", irbProtocolVO.getProtocolId());
		List<ProtocolCollaborator> collaborators = queryCollaborator.list();
		irbProtocolVO.setProtocolCollaboratorList(collaborators);
		System.out.println("Collaborator info is set " + new Date());
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> getSubjectoList(IRBProtocolVO irbProtocolVO) {
		Query queryprotocolSubject = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolSubject p where p.protocolId =:protocolId");
		queryprotocolSubject.setInteger("protocolId", irbProtocolVO.getProtocolId());
		irbProtocolVO.setProtocolSubjectList(queryprotocolSubject.list());
		System.out.println("subject list is set " + new Date());
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> getScienceOfProtocol(IRBProtocolVO irbProtocolVO) {
		Query queryScienceOfProtocol = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ScienceOfProtocol p where p.protocolId =:protocolId");
		queryScienceOfProtocol.setInteger("protocolId", irbProtocolVO.getProtocolId());
		if (!queryScienceOfProtocol.list().isEmpty()) {
			irbProtocolVO.setScienceOfProtocol((ScienceOfProtocol) queryScienceOfProtocol.list().get(0));
		}
		System.out.println("science of protocol is set " + new Date());
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	private Future<IRBProtocolVO> getProtocolFundingSource(Integer protocolId, IRBProtocolVO irbProtocolVO) {
		try {
			Query queryfundingSource = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ProtocolFundingSource p where p.protocolId =:protocolId");
			queryfundingSource.setInteger("protocolId", protocolId);
			List<ProtocolFundingSource> fundingSourceList = queryfundingSource.list();
			if (!fundingSourceList.isEmpty()) {
				fundingSourceList.forEach(protocolFundingSource -> {
					switch (protocolFundingSource.getFundingSourceTypeCode()) {
					case KeyConstants.SPONSOR_FUNDING_SPONSOR_TYPE_CODE:
						Sponsor sponsor = null;
						try {
							sponsor = fetchSponsorDetail(protocolFundingSource.getFundingSource()).get();
							protocolFundingSource.setSourceName(sponsor.getSponsorName());
							protocolFundingSource.setTitle(null);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						break;
					case KeyConstants.UNIT_FUNDING_SPONSOR_TYPE_CODE:
						Unit unit = null;
						try {
							unit = fetchUnitDetail(protocolFundingSource.getFundingSource()).get();
							protocolFundingSource.setSourceName(unit.getUnitName());
							protocolFundingSource.setTitle(null);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						break;
					case KeyConstants.DEV_PROP_FUNDING_SPONSOR_TYPE_CODE:
						EpsProposal devProposal = null;
						try {
							devProposal = fetchDevPropDetail(protocolFundingSource.getFundingSource()).get();
							protocolFundingSource.setDocId(devProposal.getDocumentNumber());
							protocolFundingSource.setTitle(devProposal.getTitle());
							Sponsor devProposalsponsor = null;
							devProposalsponsor = fetchSponsorDetail(devProposal.getSponsorCode()).get();
							protocolFundingSource.setSourceName(devProposalsponsor.getSponsorName());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						break;
					case KeyConstants.PROPOSAL_FUNDING_SPONSOR_TYPE_CODE:
						Proposal proposal = null;
						try {
							proposal = fetchProposal(protocolFundingSource.getFundingSource()).get();
							protocolFundingSource.setTitle(proposal.getTitle());
							protocolFundingSource.setDocId(proposal.getDocumentNumber());
							Sponsor proposalsponsor = null;
							proposalsponsor = fetchSponsorDetail(proposal.getSponsorCode()).get();
							protocolFundingSource.setSourceName(proposalsponsor.getSponsorName());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}

						break;
					case KeyConstants.AWARD_FUNDING_SPONSOR_TYPE_CODE:
						Award award = null;
						try {
							award = fetchAwardDetail(protocolFundingSource.getFundingSource()).get();
							protocolFundingSource.setDocId(award.getDocumentNumber());
							protocolFundingSource.setAwardId(award.getAwardId());
							protocolFundingSource.setTitle(award.getTitle());
							Sponsor awardSponsor = fetchSponsorDetail(award.getSponsorCode()).get();
							protocolFundingSource.setSourceName(awardSponsor.getSponsorName());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						break;

					}
				});
				irbProtocolVO.setProtocolFundingSourceList(fundingSourceList);
			}
		} catch (Exception e) {
			logger.error("Error in getProtocolFundingSource method" + e.getMessage());
		}
		System.out.println("Funding source is set " + new Date());
		return new AsyncResult<>(irbProtocolVO);
	}

	private Future<Proposal> fetchProposal(String fundingSource) {
		Query queryProposal = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
				"from Proposal p1 where p1.proposalNumber =:proposalNumber and p1.sequenceNumber = (select max(p2.sequenceNumber) from Proposal p2 where p2.proposalNumber = p1.proposalNumber)");
		queryProposal.setString("proposalNumber", fundingSource);
		Proposal proposal = (Proposal) queryProposal.list().get(0);
		return new AsyncResult<>(proposal);
	}

	@Async
	private Future<EpsProposal> fetchDevPropDetail(String fundingSource) {
		Query queryDevProposal = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from EpsProposal dp where dp.proposalNumber =:proposalNumber");
		queryDevProposal.setString("proposalNumber", fundingSource);
		EpsProposal devProposal = (EpsProposal) queryDevProposal.list().get(0);
		return new AsyncResult<>(devProposal);
	}

	@Async
	private Future<Unit> fetchUnitDetail(String fundingSource) {
		Query queryUnitDetails = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Unit u where u.unitNumber =:unitNumber");
		queryUnitDetails.setString("unitNumber", fundingSource);
		Unit unit = (Unit) queryUnitDetails.list().get(0);
		return new AsyncResult<>(unit);
	}

	@Async
	private Future<Sponsor> fetchSponsorDetail(String fundingSource) {
		Query querySponsorDetails = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Sponsor s where s.sponsorCode =:sponsorCode");
		querySponsorDetails.setString("sponsorCode", fundingSource);
		Sponsor sponsor = (Sponsor) querySponsorDetails.list().get(0);
		return new AsyncResult<>(sponsor);
	}

	@Async
	private Future<Award> fetchAwardDetail(String fundingSource) {
		Query queryAwardDetails = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
				"from Award a where a.awardNumber =:awardNumber and a.awardSequenceStatus =:awardSequenceStatus");
		queryAwardDetails.setString("awardNumber", fundingSource);
		queryAwardDetails.setString("awardSequenceStatus", KeyConstants.AWARD_SEQUENCE_STATUS_ACTIVE);
		Award award = (Award) queryAwardDetails.list().get(0);
		return new AsyncResult<>(award);
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
				attachments.setFileId(generateAttachmentFileId());
				attachments.setContentType(files[i].getContentType());
				attachments.setFileName(files[i].getOriginalFilename());
				attachments.setFileData(files[i].getBytes());
				attachments.setSequenceNumber(attachmentProtocol.getProtocolAttachment().getSequenceNumber());
				attachments.setUpdateTimestamp(attachmentProtocol.getProtocolAttachment().getUpdateTimestamp());
				attachments.setUpdateUser(attachmentProtocol.getUpdateUser());
				irbAttachmentProtocol.setProtocolAttachment(attachments);
				hibernateTemplate.saveOrUpdate(irbAttachmentProtocol);
			}
		} else if (attachmentProtocol.getAcType().equals("U")) {
			ProtocolAttachments attachments = attachmentProtocol.getProtocolAttachment();
			for (int i = 0; i < files.length; i++) {
				attachments.setContentType(files[i].getContentType());
				attachments.setFileName(files[i].getOriginalFilename());
				attachments.setFileData(files[i].getBytes());
				attachments.setUpdateTimestamp(attachmentProtocol.getProtocolAttachment().getUpdateTimestamp());
				attachments.setUpdateUser(attachmentProtocol.getUpdateUser());
			}
			hibernateTemplate.saveOrUpdate(attachmentProtocol);
		} else if (attachmentProtocol.getAcType().equals("D")) {
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from IRBAttachmentProtocol p where p.paProtocolId =:paProtocolId");
			queryDeletAttachment.setInteger("paProtocolId", attachmentProtocol.getPaProtocolId());
			queryDeletAttachment.executeUpdate();
			Query queryDeletProtocolAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolAttachments p where p.fileId =:fileId");
			queryDeletProtocolAttachment.setInteger("fileId", attachmentProtocol.getProtocolAttachment().getFileId());
			queryDeletProtocolAttachment.executeUpdate();
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from IRBAttachmentProtocol p where p.protocolNumber =:protocolNumber");
		query.setString("protocolNumber", attachmentProtocol.getProtocolNumber());
		irbProtocolVO.setProtocolAttachmentList(query.list());
		return irbProtocolVO;
	}

	private int generateAttachmentFileId() {
		int generatedId = 0;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			SessionImpl sessionImpl = (SessionImpl) session;
			Connection connection = sessionImpl.connection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select SEQ_ATTACHMENT_ID.nextval from dual");
			if (rs.next()) {
				int id = rs.getInt(1);
				generatedId = id;
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedId;
	}

	@Override
	public IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolCollaboratorAttachments.class);
		criteria.add(Restrictions.eq("protocolNumber", protocolNumber));
		List<ProtocolCollaboratorAttachments> attachmentsCollaborator = criteria.list();
		irbProtocolVO.setProtocolCollaboratorAttachmentsList(attachmentsCollaborator);
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from IRBAttachmentProtocol p where p.protocolNumber =:protocolNumber");
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

	@Override
	public IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson)
			throws JsonParseException, JsonMappingException, IOException {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		ObjectMapper mapper = new ObjectMapper();
		ProtocolCollaboratorAttachments protocolCollaboratorAttachments = mapper.readValue(formDataJson,
				ProtocolCollaboratorAttachments.class);
		if (protocolCollaboratorAttachments.getAcType().equals("I")) {
			for (int i = 0; i < files.length; i++) {
				ProtocolCollaboratorAttachments irbCollaboratorAttachmentProtocol = new ProtocolCollaboratorAttachments();
				irbCollaboratorAttachmentProtocol
						.setCollaboratorId(protocolCollaboratorAttachments.getCollaboratorId());
				irbCollaboratorAttachmentProtocol.setProtocolId(protocolCollaboratorAttachments.getProtocolId());
				irbCollaboratorAttachmentProtocol
						.setProtocolNumber(protocolCollaboratorAttachments.getProtocolNumber());
				irbCollaboratorAttachmentProtocol
						.setSequenceNumber(protocolCollaboratorAttachments.getSequenceNumber());
				irbCollaboratorAttachmentProtocol.setDescription(protocolCollaboratorAttachments.getDescription());
				irbCollaboratorAttachmentProtocol.setUpdateUser(protocolCollaboratorAttachments.getUpdateUser());
				irbCollaboratorAttachmentProtocol
						.setUpdateTimestamp(protocolCollaboratorAttachments.getUpdateTimestamp());
				irbCollaboratorAttachmentProtocol.setCreateDate(protocolCollaboratorAttachments.getCreateDate());
				irbCollaboratorAttachmentProtocol.setTypeCode(protocolCollaboratorAttachments.getTypeCode());
				irbCollaboratorAttachmentProtocol
						.setAttachmentType(protocolCollaboratorAttachments.getAttachmentType());
				ProtocolAttachments attachments = new ProtocolAttachments();
				attachments.setFileId(generateAttachmentFileId());
				attachments.setContentType(files[i].getContentType());
				attachments.setFileName(files[i].getOriginalFilename());
				attachments.setFileData(files[i].getBytes());
				attachments.setSequenceNumber(
						protocolCollaboratorAttachments.getProtocolAttachments().getSequenceNumber());
				attachments.setUpdateTimestamp(
						protocolCollaboratorAttachments.getProtocolAttachments().getUpdateTimestamp());
				attachments.setUpdateUser(protocolCollaboratorAttachments.getProtocolAttachments().getUpdateUser());
				irbCollaboratorAttachmentProtocol.setProtocolAttachments(attachments);
				hibernateTemplate.saveOrUpdate(irbCollaboratorAttachmentProtocol);

			}
		} else if (protocolCollaboratorAttachments.getAcType().equals("U")) {
			ProtocolAttachments attachments = protocolCollaboratorAttachments.getProtocolAttachments();
			for (int i = 0; i < files.length; i++) {
				attachments.setContentType(files[i].getContentType());
				attachments.setFileName(files[i].getOriginalFilename());
				attachments.setFileData(files[i].getBytes());
				attachments.setUpdateTimestamp(
						protocolCollaboratorAttachments.getProtocolAttachments().getUpdateTimestamp());
				attachments.setUpdateUser(protocolCollaboratorAttachments.getProtocolAttachments().getUpdateUser());

			}
			hibernateTemplate.saveOrUpdate(protocolCollaboratorAttachments);
		} else if (protocolCollaboratorAttachments.getAcType().equals("D")) {
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"delete from ProtocolCollaboratorAttachments p where p.collaboratorAttachmentId =:collaboratorAttachmentId");
			queryDeletAttachment.setInteger("collaboratorAttachmentId",
					protocolCollaboratorAttachments.getCollaboratorAttachmentId());
			queryDeletAttachment.executeUpdate();
			Query queryDeletProtocolAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolAttachments p where p.fileId =:fileId");
			queryDeletProtocolAttachment.setInteger("fileId",
					protocolCollaboratorAttachments.getProtocolAttachments().getFileId());
			queryDeletProtocolAttachment.executeUpdate();
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolCollaboratorAttachments p where p.collaboratorId =:collaboratorId");
		query.setInteger("collaboratorId", protocolCollaboratorAttachments.getCollaboratorId());
		irbProtocolVO.setProtocolCollaboratorAttachmentsList(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		for (ProtocolCollaboratorPersons person : protocolCollaboratorPersons) {
			Session session = hibernateTemplate.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			if (person.getAcType().equals("U")) {
				session.saveOrUpdate(person);
				transaction.commit();
				session.close();
			} else if (person.getAcType().equals("D")) {
				Query queryDeletePerson = session.createQuery(
						"delete from ProtocolCollaboratorPersons p where p.collaboratorPersonId =:collaboratorPersonId");
				queryDeletePerson.setInteger("collaboratorPersonId", person.getCollaboratorPersonId());
				queryDeletePerson.executeUpdate();
				transaction.commit();
				session.close();
			}
		}
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolCollaboratorPersons p where p.collaboratorId =:collaboratorId");
		query.setInteger("collaboratorId", protocolCollaboratorPersons.get(0).getCollaboratorId());
		irbProtocolVO.setProtocolCollaboratorPersons(query.list());
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolCollaboratorPersons p where p.collaboratorId =:collaboratorId");
		query.setInteger("collaboratorId", collaboratorId);
		irbProtocolVO.setProtocolCollaboratorPersons(query.list());
		Query queryAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from ProtocolCollaboratorAttachments p where p.collaboratorId =:collaboratorId");
		queryAttachment.setInteger("collaboratorId", collaboratorId);
		irbProtocolVO.setProtocolCollaboratorAttachmentsList(queryAttachment.list());
		return irbProtocolVO;
	}
}
