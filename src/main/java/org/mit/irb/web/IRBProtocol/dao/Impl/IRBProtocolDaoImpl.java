package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.Award;
import org.mit.irb.web.IRBProtocol.pojo.CollaboratorNames;
import org.mit.irb.web.IRBProtocol.pojo.EpsProposal;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.IRBFileData;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolCorrespondence;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolPersonRoles;
import org.mit.irb.web.IRBProtocol.pojo.IRBQuestionnaireAnswer;
import org.mit.irb.web.IRBProtocol.pojo.IRBWatermark;
import org.mit.irb.web.IRBProtocol.pojo.Proposal;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContact;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.pojo.Sponsor;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBWatermarkService;
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


@Service(value = "iRBProtocolDao")
@Transactional
public class IRBProtocolDaoImpl implements IRBProtocolDao {

	@Autowired
	IRBActionsDao irbAcionDao;

	@Autowired
	IRBProtocolInitLoadService initLoadService;

	DBEngine dbEngine;

	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@Autowired
	IRBWatermarkService irbWatermarkService;

	IRBProtocolDaoImpl() {
		dbEngine = new DBEngine();
	}

	Logger logger = Logger.getLogger(IRBProtocolDaoImpl.class.getName());

	/*
	 * @Override public IRBViewProfile getIRBProtocolDetails(String
	 * protocolNumber) { IRBViewProfile irbViewProfile = new IRBViewProfile();
	 * ArrayList<InParameter> inputParam = new ArrayList<>();
	 * ArrayList<OutParameter> outputParam = new ArrayList<>();
	 * inputParam.add(new InParameter("PROTOCOL_NUMBER",
	 * DBEngineConstants.TYPE_STRING, protocolNumber)); outputParam.add(new
	 * OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
	 * ArrayList<HashMap<String, Object>> result = null; try { result =
	 * dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_DETAILS",
	 * outputParam); } catch (DBException e) { e.printStackTrace();
	 * logger.info("DBException in getIRBProtocolDetails:" + e); } catch
	 * (IOException e) { e.printStackTrace();
	 * logger.info("IOException in getIRBProtocolDetails:" + e); } catch
	 * (SQLException e) { e.printStackTrace();
	 * logger.info("SQLException in getIRBProtocolDetails:" + e); } if (result
	 * != null && !result.isEmpty()) {
	 * irbViewProfile.setIrbViewHeader(result.get(0)); } return irbViewProfile;
	 * }
	 */

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
			for (HashMap<String, Object> personInfo : result) {
				ArrayList<InParameter> inParam = new ArrayList<>();
				ArrayList<OutParameter> outParam = new ArrayList<>();
				outParam.add(new OutParameter("trainingStatus", DBEngineConstants.TYPE_INTEGER));
				if (personInfo != null) {
					inParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,
							personInfo.get("PERSON_ID")));
					try {
						ArrayList<HashMap<String, Object>> trainingStatus = dbEngine.executeFunction(inParam,
								"fn_irb_per_training_completed", outParam);
						String trainingInfo = (String) trainingStatus.get(0).get("trainingStatus");
						if (trainingInfo.equals("1")) {
							personInfo.put("IS_TRAINING_COMPLETED", "COMPLETED");
						} else {
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
			result = dbEngine.executeProcedure(inputParam, "GET_PERSON_INFO", outputParam);
			if (result != null && !result.isEmpty()) {
				irbViewProfile.setIrbViewProtocolMITKCPersonInfo(result.get(0));
			}
			resultTraing = getPersonTraingInfo(avPersonId);
			ArrayList<HashMap<String, Object>> comments = null;
			ArrayList<HashMap<String, Object>> attachments = null;
			for (HashMap<String, Object> training : resultTraing) {
				comments = getPersonTrainingCommentandAttachmnt(
						Integer.parseInt(training.get("PERSON_TRAINING_ID").toString()), "1",
						"GET_IRB_PERSON_TRAING_COMMENTS");
				attachments = getPersonTrainingCommentandAttachmnt(
						Integer.parseInt(training.get("PERSON_TRAINING_ID").toString()), "1",
						"GET_IRB_PERSON_TRAING_ATTACMNT");
				training.put("comments", comments);
				training.put("attachment", attachments);
			}

			if (resultTraing != null && !resultTraing.isEmpty()) {
				irbViewProfile.setIrbViewProtocolMITKCPersonTrainingInfo(resultTraing);
			}
			outputParam.remove(0);
			outputParam.add(new OutParameter("trainingStatus", DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String, Object>> trainingStatus = dbEngine.executeFunction(inputParam,
					"fn_irb_per_training_completed", outputParam);
			String trainingInfo = (String) trainingStatus.get(0).get("trainingStatus");
			if (trainingInfo.equals("1")) {
				irbViewProfile.setTrainingStatus("COMPLETED");
			} else {
				irbViewProfile.setTrainingStatus("INCOMPLETE");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getMITKCPersonInfo:" + e);
		}
		return irbViewProfile;
	}

	public ArrayList<HashMap<String, Object>> getPersonTrainingCommentandAttachmnt(Integer avPersonTrainingId,
			String acType, String SP) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		ArrayList<HashMap<String, Object>> result = null;
		inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, avPersonTrainingId));
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, null));
		inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING, acType));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		try {
			result = dbEngine.executeProcedure(inputParam, SP, outputParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<HashMap<String, Object>> getPersonTraingInfo(String avPersonId) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAINING_INFO", outputParam);
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
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam,
					"GET_IRB_PROTO_CORRESP_LETTER", outParam);
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
		/* Integer attachmentsId = Integer.parseInt(attachmentId); */
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FIL_ID", DBEngineConstants.TYPE_STRING, attachmentId));
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getProtocolHistotyGroupList:" + e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolHistoryGroupList(result);
		}
		return irbViewProfile;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getProtocolHistotyGroupDetails(String protocolNumber) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_HISTORY_DET", outputParam);
		} catch (Exception e) {
			logger.info("Exception in getProtocolHistotyGroupDetails:" + e);
		}
		return result;
	}

	@Override
	public IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		IRBActionsVO vo = new IRBActionsVO();
		ProtocolSubmissionStatuses status = new ProtocolSubmissionStatuses();
		ProtocolPersonnelInfo protocolPersonnelInfo = new ProtocolPersonnelInfo();
		if (generalInfo.getProtocolNumber() == null) {
			generalInfo.setProtocolId(generateProtocolId());
			generalInfo.setActive("Y");
			generalInfo.setIslatest("Y");
			generalInfo.setProtocolStatusCode("100");
			String protocolNUmber = generateProtocolNumber();
			generalInfo.setProtocolNumber(protocolNUmber);
			generalInfo.setSequenceNumber(1);
			generalInfo.setCreateTimestamp(generalInfo.getUpdateTimestamp()); // since when creating a protocol both created user and updated user is same
			generalInfo.setCreateUser(generalInfo.getUpdateUser());
			generalInfo.setIsCancelled("N");
			generalInfo.setProtocolStartDate(generateSqlDate(generalInfo.getAniticipatedStartDate()));
			generalInfo.setProtocolEndDate(generateSqlDate(generalInfo.getAniticipatedEndDate()));
			List<ProtocolPersonnelInfo> protocolPersonnelInfoList = new ArrayList<ProtocolPersonnelInfo>();
			// ProtocolPersonnelInfo personnelInfo =
			// generalInfo.getPersonnelInfos().get(0);
			protocolPersonnelInfo = generalInfo.getPersonnelInfos().get(0);
			protocolPersonnelInfo.setProtocolPersonId(generatePersonId());
			protocolPersonnelInfo.setProtocolGeneralInfo(generalInfo);
			protocolPersonnelInfo.setProtocolNumber(protocolNUmber);
			protocolPersonnelInfo.setSequenceNumber(1);
			protocolPersonnelInfo.setTrainingInfo(getTrainingFlag(protocolPersonnelInfo.getPersonId()));
			protocolPersonnelInfoList.add(protocolPersonnelInfo);
			generalInfo.setPersonnelInfos(protocolPersonnelInfoList);
			List<ProtocolLeadUnits> protocolUnitList = new ArrayList<ProtocolLeadUnits>();
			ProtocolLeadUnits protocolUnit = generalInfo.getProtocolUnits().get(0);
			protocolUnit.setProtocolUnitsId(generateUnitId());
			protocolUnit.setProtocolGeneralInfo(generalInfo);
			protocolUnit.setProtocolNumber(protocolNUmber);
			protocolUnit.setSequenceNumber(1);
			protocolUnit.setUnitTypeCode("1");
			protocolUnitList.add(protocolUnit);
			generalInfo.setLeadUnitNumber(protocolUnit.getUnitNumber());
			generalInfo.setProtocolUnits(protocolUnitList);
			status.setProtocolNumber(generalInfo.getProtocolNumber());
			status.setProtocolId(generalInfo.getProtocolId());
			status.setSequenceNumber(generalInfo.getSequenceNumber());
			vo.setActionTypeCode("100");
			vo.setUpdateUser(generalInfo.getUpdateUser());
			vo.setCreateUser(generalInfo.getCreateUser());
			vo.setAcType("I");
			vo.setProtocolStatus(generalInfo.getProtocolStatusCode());
		}
		if (generalInfo.getAniticipatedStartDate() != null)
			generalInfo.setProtocolStartDate(generateSqlDate(generalInfo.getAniticipatedStartDate()));
		if (generalInfo.getAniticipatedEndDate() != null)
			generalInfo.setProtocolEndDate(generateSqlDate(generalInfo.getAniticipatedEndDate()));
		if (generalInfo.getStringRiskLvlDate() != null)
			generalInfo.setRiskLvlDateAssigned(generateSqlDate(generalInfo.getStringRiskLvlDate()));
		if (generalInfo.getStringFDARiskLvlDate() != null)
			generalInfo.setFdaRiskLvlDateAssigned(generateSqlDate(generalInfo.getStringFDARiskLvlDate()));
		hibernateTemplate.saveOrUpdate(generalInfo);
		irbProtocolVO.setGeneralInfo(generalInfo);
		if (vo.getAcType() != null) {
			status.setProtocolId(irbProtocolVO.getGeneralInfo().getProtocolId());
			vo.setProtocolSubmissionStatuses(status);
			irbAcionDao.updateActionStatus(vo);
			IRBProtocolPersonRoles personRole = new IRBProtocolPersonRoles();
			personRole.setProtocolId(generalInfo.getProtocolId());
			personRole.setProtocolNumber(generalInfo.getProtocolNumber());
			personRole.setSequenceNumber(1);
			personRole.setRoleId(2);
			personRole.setPersonId(protocolPersonnelInfo.getPersonId());
			personRole.setPersonName(protocolPersonnelInfo.getPersonName());
			personRole.setUpdateUser(generalInfo.getCreateUser());	
			personRole.setAcType("U");
			updateProtocolPermission(personRole);
			if(!protocolPersonnelInfo.getPersonId().equals(generalInfo.getCreateUserId())){
			IRBProtocolPersonRoles personRoles = new IRBProtocolPersonRoles();
			personRoles.setAcType("U");
			personRoles.setProtocolId(generalInfo.getProtocolId());
			personRoles.setProtocolNumber(generalInfo.getProtocolNumber());
			personRoles.setSequenceNumber(1);
			personRoles.setRoleId(2);
			personRoles.setPersonId(generalInfo.getCreateUserId());
			personRoles.setPersonName(generalInfo.getCreateUserName());
			personRoles.setUpdateUser(generalInfo.getCreateUser());
			updateProtocolPermission(personRoles);
			}
		}
		return irbProtocolVO;
	}

	public Date generateSqlDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date utilDate = null;
		java.sql.Date sqlDate = null;
		if (date != null) {
			try {
				utilDate = sdf.parse(date);
				sqlDate = new java.sql.Date(utilDate.getTime());
			} catch (Exception e) {
				logger.info("Exception in generateSqlActionDate:" + e);
			}
		}
		return sqlDate;
	}

	private Integer generateProtocolId() {
		Integer protocolId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_ID),0)+1 FROM ProtocolGeneralInfo");
		if (!queryGeneral.list().isEmpty()) {
			protocolId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return protocolId;
	}

	public Integer generatePersonId() {
		Integer personId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_PERSON_ID),0)+1 FROM ProtocolPersonnelInfo");
		if (!queryGeneral.list().isEmpty()) {
			personId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return personId;
	}

	public Integer generateUnitId() {
		Integer unitId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_UNITS_ID),0)+1 FROM ProtocolLeadUnits");
		if (!queryGeneral.list().isEmpty()) {
			unitId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return unitId;
	}

	public Integer generateFundingSourceId() {
		Integer fundingSourceId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_FUNDING_SOURCE_ID),0)+1 FROM ProtocolFundingSource");
		if (!queryGeneral.list().isEmpty()) {
			fundingSourceId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return fundingSourceId;
	}

	public Integer generateSubjectId() {
		Integer subjectId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_VULNERABLE_SUB_ID),0)+1 FROM ProtocolSubject");
		if (!queryGeneral.list().isEmpty()) {
			subjectId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return subjectId;
	}

	private Integer generateLocationId() {
		Integer locationId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_LOCATION_ID),0)+1 FROM ProtocolCollaborator");
		if (!queryGeneral.list().isEmpty()) {
			locationId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return locationId;
	}

	private Integer getCollabAttachmentId() {
		Integer collaboratorAttachmentId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_LOCATION_ATTMNT_ID),0)+1 FROM ProtocolCollaboratorAttachments");
		if (!queryGeneral.list().isEmpty()) {
			collaboratorAttachmentId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return collaboratorAttachmentId;
	}

	private Integer generateCollabPersonId() {
		Integer collaboratorPersonId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_LOCATION_PERSON_ID),0)+1 FROM ProtocolCollaboratorPersons");
		if (!queryGeneral.list().isEmpty()) {
			collaboratorPersonId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return collaboratorPersonId;
	}

	private Integer generatePaProtocolId() {
		Integer paProtocolId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PA_PROTOCOL_ID),0)+1 FROM IRBAttachmentProtocol");
		if (!queryGeneral.list().isEmpty()) {
			paProtocolId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return paProtocolId;
	}

	private Integer generateScientificId() {
		Integer scientificId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(SCIENTIFIC_DATA_ID),0)+1 FROM ScienceOfProtocol");
		if (!queryGeneral.list().isEmpty()) {
			scientificId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return scientificId;
	}

	private Integer generateAdminContactId() {
		Integer adminContactId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(ADMIN_CONTACT_ID),0)+1 FROM ProtocolAdminContact");
		if (!queryGeneral.list().isEmpty()) {
			adminContactId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return adminContactId;
	}

	@Override
	public String generateProtocolNumber() {
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
		String flag = "N";
		personnelInfo.setSequenceNumber(generalInfo.getSequenceNumber());
		if (personnelInfo.getAcType().equals("U")) {
			personnelInfo.setIsActive("Y");
			if (personnelInfo.getProtocolPersonId() == null) {				
				personnelInfo.setProtocolPersonId(generatePersonId());
				IRBProtocolPersonRoles assignRole = new IRBProtocolPersonRoles();
				assignRole.setAcType("U");
				assignRole.setProtocolId(generalInfo.getProtocolId());
				assignRole.setProtocolNumber(generalInfo.getProtocolNumber());
				assignRole.setSequenceNumber(1);
				assignRole.setRoleId(2);
				assignRole.setUpdateUser(generalInfo.getUpdateUser());	
				assignRole.setPersonId(personnelInfo.getPersonId());;
				assignRole.setPersonName(personnelInfo.getPersonName());
				updateProtocolPermission(assignRole);
			}
			personnelInfo.setProtocolGeneralInfo(generalInfo);
			Session sessionUpdatePersons = hibernateTemplate.getSessionFactory().openSession();
			Transaction transactionUpdatePersons = sessionUpdatePersons.beginTransaction();
			hibernateTemplate.saveOrUpdate(personnelInfo);
			transactionUpdatePersons.commit();
			sessionUpdatePersons.close();
		} else if (personnelInfo.getAcType().equals("D")) {
			logger.info("Deleting person Info");
			Query queryDeleteCollaboratorPerson = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolCollaboratorPersons p where  p.personId =:personId");
			queryDeleteCollaboratorPerson.setInteger("personId", personnelInfo.getProtocolPersonId());
			queryDeleteCollaboratorPerson.executeUpdate();
			if (generalInfo.getProtocolNumber().contains("A")) {
				Query setInActivePerson = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
						"update ProtocolPersonnelInfo p set p.isActive=:flag where  p.protocolPersonId =:protocolPersonId");
				setInActivePerson.setInteger("protocolPersonId", personnelInfo.getProtocolPersonId());
				setInActivePerson.setString("flag", flag);
				setInActivePerson.executeUpdate();
			} else {
				Query queryDeletePerson = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
						"delete from ProtocolPersonnelInfo p where  p.protocolPersonId =:protocolPersonId");
				queryDeletePerson.setInteger("protocolPersonId", personnelInfo.getProtocolPersonId());
				queryDeletePerson.executeUpdate();
			}
		}
		Query queryPersonactiveList = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolPersonnelInfo p where p.protocolNumber =:protocolNumber");
		queryPersonactiveList.setString("protocolNumber", personnelInfo.getProtocolNumber());
		List<ProtocolPersonnelInfo> protocolPersonnelInfoList = queryPersonactiveList.list();
		for (ProtocolPersonnelInfo person : protocolPersonnelInfoList) {
			person.setTrainingInfo(getTrainingFlag(person.getPersonId()));
		}
		irbProtocolVO.setProtocolPersonnelInfoList(protocolPersonnelInfoList);

		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource, ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		fundingSource.setSequenceNumber(generalInfo.getSequenceNumber());
		String sourceName = fundingSource.getSourceName();
		if (sourceName != null && !sourceName.isEmpty()) {
			fundingSource.setFundingSourceName(sourceName);
		}
		if (fundingSource.getAcType().equals("U")) {
			if (fundingSource.getProtocolFundingSourceId() == null) {
				fundingSource.setProtocolFundingSourceId(generateFundingSourceId());
			}
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
	public IRBProtocolVO updateSubject(ProtocolSubject protocolSubject, ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		protocolSubject.setSequenceNumber(generalInfo.getSequenceNumber());
		if (protocolSubject.getAcType().equals("U")) {
			if (protocolSubject.getProtocolVulnerableSubId() == null) {
				protocolSubject.setProtocolVulnerableSubId(generateSubjectId());
			}
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
	public IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator,
			ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		protocolCollaborator.setSequenceNumber(generalInfo.getSequenceNumber());
		if (protocolCollaborator.getAcType().equals("U")) {
			if (protocolCollaborator.getProtocolLocationId() == null) {
				protocolCollaborator.setProtocolLocationId(generateLocationId());
			}
			protocolCollaborator.setApprovalDate(generateSqlDate(protocolCollaborator.getStringApprovalDate()));
			protocolCollaborator.setExpirationDate(generateSqlDate(protocolCollaborator.getStringExpirationDate()));
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
		try {
			Integer protocolId = null;
			protocolId = irbProtocolVO.getProtocolId();
			ProtocolGeneralInfo protocolGeneralInfo = new ProtocolGeneralInfo();
			if (protocolId != null) {
				getGeneralPersonnelInfoList(irbProtocolVO);
				getDepartmentList(irbProtocolVO);
				getSubjectoList(irbProtocolVO);
				getScienceOfProtocol(irbProtocolVO);
				getCollaboratorList(irbProtocolVO);
				getProtocolFundingSource(protocolId, irbProtocolVO);
				Future<IRBProtocolVO> protocolAdminContacts = getProtocolAdminContacts(irbProtocolVO);
				irbProtocolVO = protocolAdminContacts.get();
				if (irbProtocolVO.getProtocolNumber().contains("A")) {
					List<HashMap<String, Object>> amendRenewalModule = irbAcionDao
							.getAmendRenewalModules(irbProtocolVO.getProtocolNumber());
					irbProtocolVO.setProtocolRenewalDetails(fetchAmendRenewalDetails(amendRenewalModule));
				} else if (irbProtocolVO.getProtocolNumber().contains("R")) {
					ProtocolRenewalDetails protocolRenewalDetail = new ProtocolRenewalDetails();
					protocolRenewalDetail.setAddModifyNoteAttachments(true);
					irbProtocolVO.setProtocolRenewalDetails(protocolRenewalDetail);
				}
			} else {
				irbProtocolVO.setGeneralInfo(protocolGeneralInfo);
			}
		} catch (Exception e) {
			logger.error("Error in loadProtocolDetails method" + e.getMessage());
		}
		return irbProtocolVO;
	}

	private ProtocolRenewalDetails fetchAmendRenewalDetails(List<HashMap<String, Object>> amendRenewalModule) {
		ProtocolRenewalDetails protocolRenewalDetail = new ProtocolRenewalDetails();
		try {
			for (HashMap<String, Object> protocolDetailKey : amendRenewalModule) {
				if (protocolDetailKey.get("STATUS_FLAG").equals("Y")) {
					switch (protocolDetailKey.get("PROTOCOL_MODULE_CODE").toString()) {
					case "001":
						protocolRenewalDetail.setGeneralInfo(true);
						break;
					case "002":
						protocolRenewalDetail.setProtocolPersonel(true);
						break;
					case "003":
						protocolRenewalDetail.setKeyStudyPersonnel(true);
						break;
					case "008":
						protocolRenewalDetail.setAddModifyNoteAttachments(true);
						break;
					case "024":
						protocolRenewalDetail.setFundingSource(true);
						break;
					case "004":
						protocolRenewalDetail.setAreaOfResearch(true);
						break;
					case "009":
						protocolRenewalDetail.setProtocolCorrespondents(true);
						break;
					case "015":
						protocolRenewalDetail.setNotes(true);
						break;
					case "027":
						protocolRenewalDetail.setProtocolUnits(true);
						break;
					case "007":
						protocolRenewalDetail.setSpecialReview(true);
						break;
					case "028":
						protocolRenewalDetail.setPointOFContact(true);
						break;
					case "006":
						protocolRenewalDetail.setSubject(true);
						break;
					case "029":
						protocolRenewalDetail.setEngangedInstitution(true);
						break;
					case "Questionnaire":
						protocolRenewalDetail.setQuestionnaire(true);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.info("Exception in fetchAmendRenewalDetails:" + e);
		}
		return protocolRenewalDetail;
	}

	@Async
	public Future<IRBProtocolVO> getDepartmentList(IRBProtocolVO irbProtocolVO) {
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
				"from ProtocolLeadUnits p where p.protocolNumber =:protocolNumber order by p.updateTimestamp DESC");
		queryGeneral.setString("protocolNumber", irbProtocolVO.getProtocolNumber());

		/*
		 * Query queryGeneralLeadUnit =
		 * hibernateTemplate.getSessionFactory().getCurrentSession()
		 * .createQuery("from ProtocolLeadUnits p "
		 * +"where p.protocolGeneralInfo.protocolId = :protocolId "
		 * +"and p.protocolUnitsId IN (SELECT MAX(U2.protocolUnitsId) " +
		 * "from ProtocolLeadUnits U2 where p.protocolGeneralInfo.protocolId = U2.protocolGeneralInfo.protocolId "
		 * + "and U2.unitType.unitTypeCode = '1')");
		 * queryGeneralLeadUnit.setInteger("protocolId",
		 * irbProtocolVO.getProtocolId());
		 * 
		 * if(!queryGeneralLeadUnit.list().isEmpty()){ ProtocolLeadUnits
		 * leadUnit=(ProtocolLeadUnits) queryGeneralLeadUnit.list().get(0);
		 * irbProtocolVO.setLeadUnit(leadUnit); }
		 */

		if (!queryGeneral.list().isEmpty()) {
			List<ProtocolLeadUnits> protocolLeadUnits = queryGeneral.list();
			irbProtocolVO.setLeadUnit(protocolLeadUnits.get(protocolLeadUnits.size() - 1));
			irbProtocolVO.setProtocolLeadUnitsList(protocolLeadUnits);
		}
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> getGeneralPersonnelInfoList(IRBProtocolVO irbProtocolVO) {
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolGeneralInfo p where p.protocolId =:protocolId");
		queryGeneral.setInteger("protocolId", irbProtocolVO.getProtocolId());

		Query querySubmissionStatus = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolSubmissionStatuses s where s.protocolId =:protocolId and s.submission_Id in("
						+ "select max(t2.submission_Id) from ProtocolSubmissionStatuses t2"
						+ "  where s.protocolId = t2.protocolId)");
		querySubmissionStatus.setInteger("protocolId", irbProtocolVO.getProtocolId());
		if (!queryGeneral.list().isEmpty()) {
			ProtocolGeneralInfo protocolGeneralInfoObj = (ProtocolGeneralInfo) queryGeneral.list().get(0);
			if (!querySubmissionStatus.list().isEmpty()) {
				protocolGeneralInfoObj.setProtocolSubmissionStatuses(
						(ProtocolSubmissionStatuses) querySubmissionStatus.list().get(0));
			}
			irbProtocolVO.setGeneralInfo(protocolGeneralInfoObj);
			List<ProtocolPersonnelInfo> personList = protocolGeneralInfoObj.getPersonnelInfos();
			for (ProtocolPersonnelInfo person : personList) {
				person.setTrainingInfo(getTrainingFlag(person.getPersonId()));
			}

			irbProtocolVO.setProtocolPersonnelInfoList(protocolGeneralInfoObj.getPersonnelInfos());
		}
		return new AsyncResult<>(irbProtocolVO);
	}

	public String getTrainingFlag(String avPersonId) {
		String Status = null;
		try {
			ArrayList<InParameter> inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			outputParam.add(new OutParameter("trainingStatus", DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String, Object>> trainingStatus = dbEngine.executeFunction(inputParam,
					"fn_irb_per_training_completed", outputParam);
			String trainingInfo = (String) trainingStatus.get(0).get("trainingStatus");
			if (trainingInfo.equals("1")) {
				Status = "COMPLETED";
			} else {
				Status = "INCOMPLETED";
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getTrainingFlag:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getTrainingFlag:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getTrainingFlag:" + e);
		}
		return Status;
	}

	/*
	 * public String getTrainingflag(ProtocolPersonnelInfo personnel) { String
	 * trainingStatus = null; LocalDate date = LocalDate.now(); java.sql.Date
	 * sqlDate = java.sql.Date.valueOf(date); Query
	 * training=hibernateTemplate.getSessionFactory().getCurrentSession()
	 * .createQuery("from PersonTraining t where t.personID =:personID and t.trainingCode in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,17,18,19,20,26,55,56) and t.followUpDate>= :sysdate"
	 * ); training.setString("personID",personnel.getPersonId());
	 * training.setDate("sysdate",sqlDate); if(training.list().size() > 0){
	 * trainingStatus="COMPLETED"; }else{ Query
	 * trainingcase2=hibernateTemplate.getSessionFactory().getCurrentSession()
	 * .createQuery("from PersonTraining t where t.personID =:personID and t.trainingCode in(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,17,18,19,20,26,55,56)"
	 * ); trainingcase2.setString("personID",personnel.getPersonId());
	 * 
	 * Query
	 * trainingcase3=hibernateTemplate.getSessionFactory().getCurrentSession()
	 * .createQuery("from PersonTraining t where t.personID =:personID and t.trainingCode in(16,23,24,25,27) and t.followUpDate>= :sysdate"
	 * ); trainingcase3.setString("personID",personnel.getPersonId());
	 * trainingcase3.setDate("sysdate",sqlDate); if(trainingcase2.list().size()
	 * > 0 && trainingcase3.list().size() > 0){ trainingStatus="COMPLETED"; }
	 * trainingStatus="INCOMPLETED"; } return trainingStatus; }
	 */

	@Async
	public Future<IRBProtocolVO> getCollaboratorList(IRBProtocolVO irbProtocolVO) {
		Query queryCollaborator = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
				"from ProtocolCollaborator p where p.protocolId =:protocolId order by p.updateTimestamp DESC");
		queryCollaborator.setInteger("protocolId", irbProtocolVO.getProtocolId());
		List<ProtocolCollaborator> collaborators = queryCollaborator.list();
		irbProtocolVO.setProtocolCollaboratorList(collaborators);
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	public Future<IRBProtocolVO> getSubjectoList(IRBProtocolVO irbProtocolVO) {
		Query queryprotocolSubject = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolSubject p where p.protocolId =:protocolId order by p.updateTimestamp DESC");
		queryprotocolSubject.setInteger("protocolId", irbProtocolVO.getProtocolId());
		irbProtocolVO.setProtocolSubjectList(queryprotocolSubject.list());
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
		return new AsyncResult<>(irbProtocolVO);
	}

	@Async
	private Future<IRBProtocolVO> getProtocolFundingSource(Integer protocolId, IRBProtocolVO irbProtocolVO) {
		try {
			Query queryfundingSource = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"from ProtocolFundingSource p where p.protocolId =:protocolId order by p.updateTimestamp DESC");
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
							protocolFundingSource.setFundingNumber(sponsor.getSponsorCode());
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
							protocolFundingSource.setFundingNumber(unit.getUnitNumber());
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
							protocolFundingSource.setFundingNumber(devProposal.getProposalNumber());
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
							protocolFundingSource.setFundingNumber(proposal.getProposalNumber());
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
							protocolFundingSource.setFundingNumber(award.getAccountNumber());
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
		return new AsyncResult<>(irbProtocolVO);
	}

	private Future<Proposal> fetchProposal(String fundingSource) {

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria queryProposal = session.createCriteria(Proposal.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("documentNumber"), "documentNumber");
		projList.add(Projections.property("title"), "title");
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("proposalNumber"), "proposalNumber");
		queryProposal.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		queryProposal.add(Restrictions.eq("proposalNumber", fundingSource));
		queryProposal.addOrder(Order.desc("sequenceNumber"));
		/*
		 * Query queryProposal =
		 * hibernateTemplate.getSessionFactory().getCurrentSession().
		 * createQuery(
		 * "select p1.documentNumber,p1.title,p1.sponsorCode from Proposal p1 where p1.proposalNumber =:proposalNumber and p1.sequenceNumber = (select max(p2.sequenceNumber) from Proposal p2 where p2.proposalNumber = p1.proposalNumber)"
		 * ); queryProposal.setString("proposalNumber", fundingSource);
		 */
		Proposal proposal = (Proposal) queryProposal.list().get(0);
		return new AsyncResult<>(proposal);
	}

	@Async
	private Future<EpsProposal> fetchDevPropDetail(String fundingSource) {

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria queryDevProposal = session.createCriteria(EpsProposal.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("documentNumber"), "documentNumber");
		projList.add(Projections.property("title"), "title");
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("proposalNumber"), "proposalNumber");
		queryDevProposal.setProjection(projList).setResultTransformer(Transformers.aliasToBean(EpsProposal.class));
		queryDevProposal.add(Restrictions.eq("proposalNumber", fundingSource));
		/*
		 * Query queryDevProposal =
		 * hibernateTemplate.getSessionFactory().getCurrentSession()
		 * .createQuery("from EpsProposal dp where dp.proposalNumber =:proposalNumber"
		 * ); queryDevProposal.setString("proposalNumber", fundingSource);
		 */
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

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria queryAwardDetails = session.createCriteria(Award.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("awardId"), "awardId");
		projList.add(Projections.property("documentNumber"), "documentNumber");
		projList.add(Projections.property("title"), "title");
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("accountNumber"), "accountNumber");
		queryAwardDetails.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Award.class));
		queryAwardDetails.add(Restrictions.eq("awardNumber", fundingSource));
		queryAwardDetails.add(Restrictions.eq("awardSequenceStatus", KeyConstants.AWARD_SEQUENCE_STATUS_ACTIVE));

		/*
		 * Query queryAwardDetails =
		 * hibernateTemplate.getSessionFactory().getCurrentSession().
		 * createQuery(
		 * "from Award a where a.awardNumber =:awardNumber and a.awardSequenceStatus =:awardSequenceStatus"
		 * ); queryAwardDetails.setString("awardNumber", fundingSource);
		 * queryAwardDetails.setString("awardSequenceStatus",
		 * KeyConstants.AWARD_SEQUENCE_STATUS_ACTIVE);
		 */
		Award award = (Award) queryAwardDetails.list().get(0);
		return new AsyncResult<>(award);
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
	public IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol,
			ProtocolGeneralInfo generalInfo) {
		if (scienceOfProtocol.getScientificId() == null) {
			scienceOfProtocol.setScientificId(generateScientificId());
		}
		scienceOfProtocol.setSequenceNumber(generalInfo.getSequenceNumber());
		hibernateTemplate.saveOrUpdate(scienceOfProtocol);
		irbProtocolVO.setScienceOfProtocol(scienceOfProtocol);
		return irbProtocolVO;
	}

	
	@Override
	public IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		for (ProtocolCollaboratorPersons person : protocolCollaboratorPersons) {
			Session session = hibernateTemplate.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			if (person.getAcType().equals("U")) {
				if (person.getCollaboratorPersonId() == null) {
					person.setCollaboratorPersonId(generateCollabPersonId());
				}
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
	public IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId , Integer protocolId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try{
			Query query = hibernateTemplate.getSessionFactory().getCurrentSession()		
				.createQuery("from ProtocolCollaboratorPersons p where p.collaboratorId =:collaboratorId");
		query.setInteger("collaboratorId", collaboratorId);
		irbProtocolVO.setProtocolCollaboratorPersons(query.list());	
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadCollaboratorPersonsAndAttachments:" + e);
		}
		irbProtocolVO  = loadCollaboratorAttachments(irbProtocolVO,collaboratorId);
		return irbProtocolVO;
	}

	private IRBProtocolVO loadCollaboratorAttachments(IRBProtocolVO irbProtocolVO,Integer protocolLocationId){
		try{
			Query queryAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachmentProtocol p where p.protocolLocationId =:protocolLocationId and p.subCategoryCode ='2'");
			queryAttachment.setInteger("protocolLocationId", protocolLocationId);
			irbProtocolVO.setProtocolCollaboratorAttachmentsList(queryAttachment.list());
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadCollaboratorAttachments:" + e);
		}	
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateUnitDetails(ProtocolLeadUnits protocolUnit, ProtocolGeneralInfo generalInfo) {
		logger.info(".................Updating unit Informations..................");
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		protocolUnit.setSequenceNumber(generalInfo.getSequenceNumber());
		if (protocolUnit.getAcType().equals("U")) {
			if (protocolUnit.getProtocolUnitsId() == null) {
				protocolUnit.setProtocolUnitsId(generateUnitId());
			}
			protocolUnit.setProtocolGeneralInfo(generalInfo);
			Session sessionUpdatePersons = hibernateTemplate.getSessionFactory().openSession();
			Transaction transactionUpdatePersons = sessionUpdatePersons.beginTransaction();
			hibernateTemplate.saveOrUpdate(protocolUnit);
			transactionUpdatePersons.commit();
			sessionUpdatePersons.close();
		} else if (protocolUnit.getAcType().equals("D")) {
			logger.info("Deleting unit Info");
			Query queryDeleteLeadUnits = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from ProtocolLeadUnits p where  p.protocolUnitsId =:protocolUnitsId");
			queryDeleteLeadUnits.setInteger("protocolUnitsId", protocolUnit.getProtocolUnitsId());
			queryDeleteLeadUnits.executeUpdate();
		}
		Query queryPersonList = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from ProtocolLeadUnits p where p.protocolNumber =:protocolNumber");
		queryPersonList.setString("protocolNumber", protocolUnit.getProtocolNumber());
		List<ProtocolLeadUnits> protocolLeadUnitsList = queryPersonList.list();
		irbProtocolVO.setProtocolLeadUnitsList(protocolLeadUnitsList);
		return irbProtocolVO;
	}

	@Override
	public void modifyAdminContactDetail(ProtocolAdminContact protocolAdminContact, ProtocolGeneralInfo generalInfo) {
		protocolAdminContact.setSequenceNumber(generalInfo.getSequenceNumber());
		protocolAdminContact.setProtocolGeneralInfo(generalInfo);
		Session sessionUpdatePersons = hibernateTemplate.getSessionFactory().openSession();
		Transaction transactionUpdatePersons = sessionUpdatePersons.beginTransaction();
		if (protocolAdminContact.getAdminContactId() == null) {
			protocolAdminContact.setAdminContactId(generateAdminContactId());
		}
		hibernateTemplate.saveOrUpdate(protocolAdminContact);
		transactionUpdatePersons.commit();
		sessionUpdatePersons.close();
	}

	@Override
	public void deleteAdminContactDetail(ProtocolAdminContact protocolAdminContact, ProtocolGeneralInfo generalInfo) {
		logger.info("Deleting Admin Contact");
		Query queryDeleteAdminContact = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("delete from ProtocolAdminContact p where  p.adminContactId =:adminContactId");
		queryDeleteAdminContact.setInteger("adminContactId", protocolAdminContact.getAdminContactId());
		queryDeleteAdminContact.executeUpdate();
	}

	@Override
	public Future<IRBProtocolVO> getProtocolAdminContacts(IRBProtocolVO irbProtocolVO) {
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
				"from ProtocolAdminContact p where p.protocolGeneralInfo.protocolId =:protocolId order by p.updateTimestamp DESC");
		queryGeneral.setInteger("protocolId", irbProtocolVO.getProtocolId());
		if (!queryGeneral.list().isEmpty()) {
			List<ProtocolAdminContact> adminContactList = queryGeneral.list();
			irbProtocolVO.setProtocolAdminContactList(adminContactList);
		}
		return new AsyncResult<>(irbProtocolVO);
	}

	@Override
	public IRBViewProfile getIRBprotocolUnits(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTO_UNIT_DTLS", outputParam);
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
			irbViewProfile.setIrbViewProtocolUnits(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolAdminContact(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_ADMIN_CONTACTS_DTLS", outputParam);
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
			irbViewProfile.setIrbViewProtocolAdminContact(result);
		}
		return irbViewProfile;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getIRBprotocolCollaboratorDetails(Integer protocolCollaboratorId,
			String acType) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_LOCATION_ID", DBEngineConstants.TYPE_INTEGER, protocolCollaboratorId));
		inputParam.add(new InParameter("AV_TYPE ", DBEngineConstants.TYPE_STRING, acType));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_LOCATION_DTLS", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getIRBprotocolCollaboratorDetails:" + e);
		}
		return result;
	}

	@Override
	public ResponseEntity<byte[]> downloadCollaboratorFileData(String fileDataId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FILE_ID ", DBEngineConstants.TYPE_STRING, fileDataId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam,
					"GET_IRB_PROTO_LOC_ATCHMNT_DWNL", outParam);
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
			logger.info("Exception in downloadFileData method:" + e);
		}
		return attachmentData;
	}

	@Override
	public IRBViewProfile getUserTrainingRight(String person_Id) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, person_Id));
		outputParam.add(new OutParameter("hasRight", DBEngineConstants.TYPE_INTEGER));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeFunction(inputParam, "FN_IRB_GET_USER_RIGHTS", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getUserTrainingRight:" + e);
		}
		if (result != null && !result.isEmpty()) {
			HashMap<String, Object> hmResult = result.get(0);
			int userHasRight = Integer.parseInt((String) hmResult.get("hasRight"));
			irbViewProfile.setUserHasRightToEditTraining(userHasRight);
		}
		return irbViewProfile;
	}

	@Override
	public Integer getNextGroupActionId(Integer protocolId, Integer nextGroupActionId, Integer actionId) {
		Integer nextGroupAction_id = null;
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER, protocolId));
		inputParam.add(new InParameter("AV_NEXT_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, nextGroupActionId));
		inputParam.add(new InParameter("AV_ACTION_ID", DBEngineConstants.TYPE_INTEGER, actionId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_NEXT_GROUP_ACTION_ID", outputParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				nextGroupAction_id = Integer.parseInt(hmResult.get("NEXT_GROUP_ACTION_ID").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("DBException in getIRBProtocolDetails:" + e);
		}
		return nextGroupAction_id;
	}

	@Override
	public List<CollaboratorNames> loadCollaborators(String collaboratorSearchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(CollaboratorNames.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("organizationId"), "organizationId");
		projList.add(Projections.property("organizationName"), "organizationName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(CollaboratorNames.class));
		criteria.addOrder(Order.asc("organizationName"));
		Criterion number = Restrictions.like("organizationId", "%" + collaboratorSearchString + "%");
		Criterion name = Restrictions.like("organizationName", "%" + collaboratorSearchString + "%").ignoreCase();
		LogicalExpression andExp = Restrictions.or(number, name);
		criteria.add(andExp);
		criteria.setMaxResults(25);
		List<CollaboratorNames> keyWordsList = criteria.list();
		return keyWordsList;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getHistoryGroupComment(String protocolNumber, Integer actionId,
			Integer protocolId, Integer nextGroupActionId) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER ", DBEngineConstants.TYPE_STRING, protocolNumber));
		inputParam.add(new InParameter("AV_PROTOCOL_ID ", DBEngineConstants.TYPE_INTEGER, protocolId));
		inputParam.add(new InParameter("AV_ACTION_ID ", DBEngineConstants.TYPE_INTEGER, actionId));
		inputParam.add(new InParameter("AV_NEXT_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, nextGroupActionId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_GRP_COMMENTS", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getHistoryGroupComment:" + e);
		}
		return result;
	}

	@Override
	public HashMap<String, Object> getIRBProtocolDetail(String protocolNumber) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_DETAILS", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("DBException in getIRBProtocolDetails:" + e);
		}
		return result.get(0);
	}

	@Override
	public ResponseEntity<byte[]> downloadAdminRevAttachment(String attachmentId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_ATTACHMENT_ID", DBEngineConstants.TYPE_STRING, attachmentId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam,
					"GET_IRB_DOWNLOAD_ADMIN_RVW_ATT", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("DATA");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(hmResult.get("MIME_TYPE").toString()));
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
	
	// **new attachment**
	private Integer generateDocumentId() {
		Integer documentId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(DOCUMENT_ID),0)+1 FROM IRBAttachmentProtocol");	
		if(!queryGeneral.list().isEmpty()){
			documentId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return documentId;	
	}
	
	private Integer generateFileId() {
		Integer fileId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(ID),0)+1 FROM IRBFileData");	
		if(!queryGeneral.list().isEmpty()){
			fileId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return fileId;	
	}
	
	@Override
	public IRBProtocolVO loadIRBProtocolAttachments(Integer protocolId, String protocolNumber) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from IRBAttachmentProtocol at"
                             +" where at.protocolGeneralInfo.protocolId = :protocolId"
                             +" and (at.documentId,at.attachmentVersion)"
                             +" IN("
                             +" select att.documentId,max(att.attachmentVersion)"
                             +" from IRBAttachmentProtocol att"
                             +" where att.protocolGeneralInfo.protocolId = :protocolId and att.subCategoryCode <>3"
                             +" group by att.protocolNumber,att.documentId"
                             +" )");                              
		query.setInteger("protocolId",protocolId);
		irbProtocolVO.setProtocolAttachmentList(query.list());	
		irbProtocolVO.setQuestionnaireAttachmentList(loadQuestionnaireAttachments(protocolNumber));
		if(protocolNumber.length() > 10){
			ProtocolRenewalDetails protocolRenewalDetail = new ProtocolRenewalDetails();
			protocolRenewalDetail.setAddModifyNoteAttachments(true);
			irbProtocolVO.setProtocolRenewalDetails(protocolRenewalDetail);
		}
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO addNewProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			for (int i = 0; i < files.length; i++) {
				attachmentProtocol.setPaProtocolId(generatePaProtocolId());
				attachmentProtocol.setAttachmentVersion(1);
				attachmentProtocol.setDocumentId(generateDocumentId());
				IRBFileData fileData = new IRBFileData();
				fileData.setFileId(generateFileId());
				fileData.setFileData(files[i].getBytes());
				attachmentProtocol.setProtocolAttachmentData(fileData);
				attachmentProtocol.setFileName(files[i].getOriginalFilename());
				attachmentProtocol.setMimeType(files[i].getContentType());
				hibernateTemplate.saveOrUpdate(attachmentProtocol);
			}
			if (attachmentProtocol.getSubCategoryCode().equals("2")) {
				irbProtocolVO = loadCollaboratorAttachments(irbProtocolVO, attachmentProtocol.getProtocolLocationId());
			} else if(attachmentProtocol.getSubCategoryCode().equals("3")){
				irbProtocolVO.setProtocolId(attachmentProtocol.getProtocolId());
				irbProtocolVO = loadInternalProtocolAttachments(irbProtocolVO);
			} else {
				irbProtocolVO = loadIRBProtocolAttachments(attachmentProtocol.getProtocolId(),attachmentProtocol.getProtocolNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO replaceProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			attachmentProtocol.setPaProtocolId(generatePaProtocolId());
			attachmentProtocol.setAttachmentVersion(attachmentProtocol.getAttachmentVersion() + 1);
			attachmentProtocol.setMimeType(files[0].getContentType());
			attachmentProtocol.setFileName(files[0].getOriginalFilename());
			IRBFileData fileData = new IRBFileData();
			fileData.setFileId(generateFileId());
			fileData.setFileData(files[0].getBytes());
			attachmentProtocol.setProtocolAttachmentData(fileData);
			attachmentProtocol.setUpdateTimeStamp(attachmentProtocol.getUpdateTimeStamp());
			attachmentProtocol.setUpdateUser(attachmentProtocol.getUpdateUser());
			hibernateTemplate.saveOrUpdate(attachmentProtocol);
			irbProtocolVO = loadIRBProtocolAttachments(attachmentProtocol.getProtocolId(),attachmentProtocol.getProtocolNumber());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO editProtocolAttachment(MultipartFile[] files, IRBAttachmentProtocol attachmentProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			attachmentProtocol.setDescription(attachmentProtocol.getDescription());
			attachmentProtocol.setTypeCode(attachmentProtocol.getTypeCode());
			attachmentProtocol.setUpdateTimeStamp(attachmentProtocol.getUpdateTimeStamp());
			attachmentProtocol.setUpdateUser(attachmentProtocol.getUpdateUser());
			hibernateTemplate.saveOrUpdate(attachmentProtocol);
			if (attachmentProtocol.getSubCategoryCode().equals("2")) {
				irbProtocolVO = loadCollaboratorAttachments(irbProtocolVO, attachmentProtocol.getProtocolLocationId());
			} else if(attachmentProtocol.getSubCategoryCode().equals("3")){
				irbProtocolVO.setProtocolId(attachmentProtocol.getProtocolId());
				irbProtocolVO = loadInternalProtocolAttachments(irbProtocolVO);
			} else {
				irbProtocolVO = loadIRBProtocolAttachments(attachmentProtocol.getProtocolId(),attachmentProtocol.getProtocolNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO deleteProtocolAttachment(IRBAttachmentProtocol attachmentProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			Query queryGetFileId = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"select p.protocolAttachmentData.fileId from IRBAttachmentProtocol p where p.documentId =:documentId");
			queryGetFileId.setInteger("documentId", attachmentProtocol.getDocumentId());
			List<Integer> fileId = queryGetFileId.list();

			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from IRBAttachmentProtocol p where p.documentId =:documentId");
			queryDeletAttachment.setInteger("documentId", attachmentProtocol.getDocumentId());
			queryDeletAttachment.executeUpdate();
			for (Integer fileID : fileId) {
				Query queryDeletProtocolAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
						.createQuery("delete from IRBFileData p where p.fileId =:fileId");
				queryDeletProtocolAttachment.setInteger("fileId", fileID);
				queryDeletProtocolAttachment.executeUpdate();
			}
			if (attachmentProtocol.getSubCategoryCode().equals("2")) {
				irbProtocolVO = loadCollaboratorAttachments(irbProtocolVO, attachmentProtocol.getProtocolLocationId());
			} else if(attachmentProtocol.getSubCategoryCode().equals("3")){
				irbProtocolVO.setProtocolId(attachmentProtocol.getProtocolId());
				irbProtocolVO = loadInternalProtocolAttachments(irbProtocolVO);
			} else {
				irbProtocolVO = loadIRBProtocolAttachments(attachmentProtocol.getProtocolId(),attachmentProtocol.getProtocolNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO loadInternalProtocolAttachments(IRBProtocolVO irbProtocolVO) {
		try{
			Query queryAttachment = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachmentProtocol p where p.protocolGeneralInfo.protocolId =:protocolId and p.subCategoryCode ='3'");
			queryAttachment.setInteger("protocolId",irbProtocolVO.getProtocolId());
			irbProtocolVO.setProtocolAttachmentList(queryAttachment.list());
			irbProtocolVO = loadInternalProtocolCorrespAttachments(irbProtocolVO);
			irbProtocolVO.setIrbInternalAttachementTypes(loadInternalAttachmentType());					
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadInternalProtocolAttachments method:" + e);
		}
		return irbProtocolVO;
	}
	
	public IRBProtocolVO loadInternalProtocolCorrespAttachments(IRBProtocolVO irbProtocolVO){
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IRBProtocolCorrespondence> criteria = builder.createQuery(IRBProtocolCorrespondence.class);
			Root<IRBProtocolCorrespondence> attachmentRoot=criteria.from(IRBProtocolCorrespondence.class);					
			criteria.where(builder.equal(attachmentRoot.get("protocolId"),irbProtocolVO.getProtocolId()));
			criteria.orderBy(builder.desc(attachmentRoot.get("updateTimeStamp")));
			List<IRBProtocolCorrespondence> internalProtocolAtachmentList = session.createQuery(criteria).getResultList();
			irbProtocolVO.setInternalProtocolAtachmentList(internalProtocolAtachmentList);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadInternalProtocolAttachments method:" + e);
		}
		return irbProtocolVO;
	}
	
	public List loadInternalAttachmentType() {
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachementTypes where subCategoryCode ='3' ");
		return query.list();
	}

	@Override
	public IRBProtocolVO loadPreviousProtocolAttachments(String documentId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IRBAttachmentProtocol> criteria = builder.createQuery(IRBAttachmentProtocol.class);
			Root<IRBAttachmentProtocol> attachmentRoot=criteria.from(IRBAttachmentProtocol.class);		
			criteria.multiselect(attachmentRoot.get("protocolAttachmentData"),attachmentRoot.get("comments"),attachmentRoot.get("updateTimeStamp")
					,attachmentRoot.get("updateUser"),attachmentRoot.get("attachmentVersion"),attachmentRoot.get("subCategoryCode"),attachmentRoot.get("fileName"),attachmentRoot.get("mimeType"));
			criteria.where(builder.equal(attachmentRoot.get("documentId"),documentId));
			criteria.orderBy(builder.desc(attachmentRoot.get("updateTimeStamp")));
			List<IRBAttachmentProtocol> previousProtocolAttachmentList = session.createQuery(criteria).getResultList();		
			if(previousProtocolAttachmentList != null)
				previousProtocolAttachmentList.remove(0);
			irbProtocolVO.setPreviousProtocolAttachmentList(previousProtocolAttachmentList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadPreviousProtocolAttachments method:" + e);
		}
		return irbProtocolVO;
	}	
	
	@Override
	public ResponseEntity<byte[]> downloadProtocolAttachments(String documentId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IRBAttachmentProtocol> criteria = builder.createQuery(IRBAttachmentProtocol.class);
			Root<IRBAttachmentProtocol> attachmentRoot=criteria.from(IRBAttachmentProtocol.class);				
			criteria.where(builder.equal(attachmentRoot.get("protocolAttachmentData"),Integer.parseInt(documentId)));
			IRBAttachmentProtocol protocolAttachment = session.createQuery(criteria).getResultList().get(0);	
			if (protocolAttachment != null) {				
				byte[] byteArray = null;				
				byteArray = protocolAttachment.getProtocolAttachmentData().getFileData();		
				if(protocolAttachment.getSubCategoryCode().equals("1") && protocolAttachment.getMimeType().equals("application/pdf")){
					byteArray = addWatermarkToPdf(byteArray,protocolAttachment);
				}
    			HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(protocolAttachment.getMimeType()));
				String filename = protocolAttachment.getFileName();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadProtocolAttachments method:" + e);
		}
		return attachmentData;
	}
	
	private byte[] addWatermarkToPdf(byte[] byteArray, IRBAttachmentProtocol protocolAttachment) {		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<IRBWatermark> criteria = builder.createQuery(IRBWatermark.class);
		Root<IRBWatermark> watermarkRoot = criteria.from(IRBWatermark.class);				
		criteria.where(builder.equal(watermarkRoot.get("statusCode"),protocolAttachment.getProtocolGeneralInfo().getProtocolStatusCode()));		
		List<IRBWatermark> watermarkDetails = session.createQuery(criteria).getResultList();
		if(watermarkDetails != null && !watermarkDetails.isEmpty()){
			IRBWatermark watermark = watermarkDetails.get(0);
			byteArray = irbWatermarkService.generateTimestampAndUsernameForPdf(byteArray, watermark ,protocolAttachment);
		}
		return byteArray;
	}

	@Override
	public ResponseEntity<byte[]> downloadInternalProtocolAttachments(String documentId) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();		
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IRBProtocolCorrespondence> criteria = builder.createQuery(IRBProtocolCorrespondence.class);
			Root<IRBProtocolCorrespondence> attachmentRoot=criteria.from(IRBProtocolCorrespondence.class);				
			criteria.where(builder.equal(attachmentRoot.get("protocolCorrespondenceId"),Integer.parseInt(documentId)));
			IRBProtocolCorrespondence protocolAttachment = session.createQuery(criteria).getResultList().get(0);	
			if (protocolAttachment != null) {
				byte[] byteArray = null;
				byteArray = protocolAttachment.getFileData();				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType("application/pdf"));
				String filename ="COUHES Connect document ProtocolNumber: "+protocolAttachment.getProtocolNumber();
				headers.setContentDispositionFormData(filename,filename);
				headers.setContentLength(byteArray.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadInternalProtocolAttachments method:" + e);
		}
		return attachmentData;
	}	
	
	private List loadQuestionnaireAttachments(String protocolNumber) {
		List<IRBQuestionnaireAnswer> questionnaireAttachmentsList = null;
		try{
			Query queryProtocolQuestionnaireAttach = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from IRBQuestionnaireAnswerAttachment p where p.questionnaireAnswer.questionnaireAnswerHeader.moduleItemKey =:moduleItemKey order by p.updateTimeStamp DESC");
			queryProtocolQuestionnaireAttach.setString("moduleItemKey", protocolNumber);		
			questionnaireAttachmentsList = queryProtocolQuestionnaireAttach.list();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in loadQuestionnaireAttachments method:" + e);
		}
		return questionnaireAttachmentsList;
	}
	@Override
	public void updateProtocolPermission(IRBProtocolPersonRoles protocolRolePerson) {
		try{
			if(protocolRolePerson.getAcType().equals("U")){				
				if(protocolRolePerson.getProtocolPersonRoleId() == null)
				protocolRolePerson.setProtocolPersonRoleId(getProtocolPersonRoleId());					
				hibernateTemplate.saveOrUpdate(protocolRolePerson);		
			}		
			if(protocolRolePerson.getAcType().equals("D")){
				Query queryDeletRole = hibernateTemplate.getSessionFactory().getCurrentSession()
						.createQuery("delete from IRBProtocolPersonRoles p where p.protocolPersonRoleId =:protocolPersonRoleId");
				queryDeletRole.setInteger("protocolPersonRoleId",protocolRolePerson.getProtocolPersonRoleId() );
				queryDeletRole.executeUpdate();			
			}
		} catch (Exception e) {
			logger.info("Exception in updateProtocolPermission method:" + e);
		}		
	}
	private Integer getProtocolPersonRoleId() {
		Integer protocolPersonRoleId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTO_PERSON_ROLE_ID),0)+1 FROM IRBProtocolPersonRoles");
		if (!queryGeneral.list().isEmpty()) {
			protocolPersonRoleId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return protocolPersonRoleId;
	}

	@Override
	public List<IRBProtocolPersonRoles> loadProtocolPermissionPerson(IRBPermissionVO irbPermissionVO) {
		List<IRBProtocolPersonRoles> irbProtocolPersonRoleList = null;
		try{
			Query queryProtocolRolePerson = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from IRBProtocolPersonRoles p where p.protocolId =:protocolId order by p.updateTimestamp DESC");
			queryProtocolRolePerson.setInteger("protocolId", irbPermissionVO.getProtocolId());
			irbProtocolPersonRoleList = queryProtocolRolePerson.list();
		} catch (Exception e) {
			logger.info("Exception in loadProtocolPermissionPerson method:" + e);
		}
		return irbProtocolPersonRoleList;
	}
	
	private Integer generateProtocolCorrespondenceId() {
		Integer protocolCorrespondenceId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(PROTOCOL_CORRESPONDENCE_ID),0)+1 FROM IRBProtocolCorrespondence");
		if (!queryGeneral.list().isEmpty()) {
			protocolCorrespondenceId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return protocolCorrespondenceId;
	}
 
	@Override
	public IRBProtocolVO saveOrUpdateInternalProtocolAttachments(MultipartFile[] files,
			IRBProtocolCorrespondence internalProtocolAttachment) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try {
			if(internalProtocolAttachment.getProtocolCorrespondenceId() == null){
				internalProtocolAttachment.setProtocolCorrespondenceId(generateProtocolCorrespondenceId());
			}
			internalProtocolAttachment.setContentType(files[0].getContentType());
			internalProtocolAttachment.setFileName(files[0].getOriginalFilename());		
			internalProtocolAttachment.setFileData(files[0].getBytes());
			hibernateTemplate.saveOrUpdate(internalProtocolAttachment);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		irbProtocolVO.setProtocolId(internalProtocolAttachment.getProtocolId());
		irbProtocolVO = loadInternalProtocolAttachments(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO deleteInternalProtocolAttachment(IRBProtocolCorrespondence internalProtocolAttachment) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try{
			Query queryDeletAttachment = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("delete from IRBProtocolCorrespondence p where p.protocolCorrespondenceId =:protocolCorrespondenceId");
			queryDeletAttachment.setInteger("protocolCorrespondenceId", internalProtocolAttachment.getProtocolCorrespondenceId());
			queryDeletAttachment.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		irbProtocolVO.setProtocolId(internalProtocolAttachment.getProtocolId());
		irbProtocolVO = loadInternalProtocolAttachments(irbProtocolVO);
		return irbProtocolVO;
	}
}
