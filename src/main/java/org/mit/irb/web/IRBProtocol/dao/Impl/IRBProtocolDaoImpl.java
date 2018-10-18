package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.notification.ExemptProtocolEmailNotification;
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
	ExemptProtocolEmailNotification exemptProtocolEmailNotification;

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

	@SuppressWarnings("null")
	@Override
	public IRBViewProfile getPersonExemptFormList(String personID, String personRoleType, String title, String piName,
			String determination, String facultySponsor, String exemptStratDate, String ExemptEndDate)
			throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date exemptStartDates = null;
		java.sql.Date sqlExemptStartDate = null;
		java.util.Date exemptEndDates = null;
		java.sql.Date sqlExemptEndDate = null;
		if (exemptStratDate != null) {
			exemptStartDates = sdf1.parse(exemptStratDate);
			sqlExemptStartDate = new java.sql.Date(exemptStartDates.getTime());
		}
		if (ExemptEndDate != null) {
			exemptEndDates = sdf1.parse(ExemptEndDate);
			sqlExemptEndDate = new java.sql.Date(exemptEndDates.getTime());
		}
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, personID));
		inputParam.add(new InParameter("AV_PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		inputParam.add(new InParameter("AV_TITLE", DBEngineConstants.TYPE_STRING, title));
		inputParam.add(new InParameter("AV_PI_NAME", DBEngineConstants.TYPE_STRING, piName));
		inputParam.add(new InParameter("AV_DETERMINATION", DBEngineConstants.TYPE_STRING, determination));
		inputParam.add(new InParameter("AV_FACULTY_SPONSOR_NM", DBEngineConstants.TYPE_STRING, facultySponsor));
		inputParam.add(new InParameter("AV_START_DATE", DBEngineConstants.TYPE_DATE, sqlExemptStartDate));
		inputParam.add(new InParameter("AV_END_DATE", DBEngineConstants.TYPE_DATE, sqlExemptEndDate));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			if(personID != ""){
				result = dbEngine.executeProcedure(inputParam, "GET_IRB_EXEMPT_FORM_LIST", outputParam);
			} else{
				result = null;
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getPersonExemptFormList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getPersonExemptFormList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getPersonExemptFormList:" + e);
		}
		if (result != null && !result.isEmpty()) {
			List<IRBExemptForm> irbExemptFormList = new ArrayList<IRBExemptForm>();
			for (HashMap<String, Object> hmap : result) {
				IRBExemptForm exemptForm = new IRBExemptForm();
				if (hmap.get("IRB_PERSON_EXEMPT_FORM_ID") != null) {
					exemptForm.setExemptFormID(Integer.parseInt(hmap.get("IRB_PERSON_EXEMPT_FORM_ID").toString()));
				}
				if (hmap.get("PERSON_ID") != null) {
					exemptForm.setPersonId((String) hmap.get("PERSON_ID"));
					exemptForm.setPIJobTitle(getJobTitle((String) hmap.get("PERSON_ID")));
				}
				if (hmap.get("PERSON_NAME") != null) {
					exemptForm.setPersonName((String) hmap.get("PERSON_NAME"));
				}
				if (hmap.get("IS_SUBMITTED_ONCE") != null) {
					exemptForm.setSubmittedOnce(Integer.parseInt(hmap.get("IS_SUBMITTED_ONCE").toString()));
				}
				if (hmap.get("EXEMPT_TITLE") != null) {
					exemptForm.setExemptTitle((String) hmap.get("EXEMPT_TITLE"));
				}
				if (hmap.get("EXEMPT_FORM_NUMBER") != null) {
					exemptForm.setExemptFormNumber(Integer.parseInt(hmap.get("EXEMPT_FORM_NUMBER").toString()));
				}
				if (hmap.get("QUESTIONNAIRE_ANS_HEADER_ID") != null) {
					exemptForm.setExemptQuestionnaireAnswerHeaderId(
							Integer.parseInt(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID").toString()));
				}
				if (hmap.get("UPDATE_USER") != null) {
					exemptForm.setUpdateUser((String) hmap.get("UPDATE_USER"));
				}
				if (hmap.get("EXEMPT_STATUS") != null) {
					exemptForm.setStatus((String) hmap.get("EXEMPT_STATUS"));
				}
				if (hmap.get("EXEMPT_STATUS_CODE") != null) {
					exemptForm.setStatusCode((String) hmap.get("EXEMPT_STATUS_CODE"));
				}
				if (hmap.get("IS_EXEMPT_GRANTED") != null) {
					exemptForm.setIsExempt((String) hmap.get("IS_EXEMPT_GRANTED"));
				}
				if (hmap.get("FACULTY_SPONSOR_PERSON_ID") != null) {
					exemptForm.setFacultySponsorPersonId((String) hmap.get("FACULTY_SPONSOR_PERSON_ID"));
					exemptForm.setFacultySponsorJobTitle(getJobTitle((String) hmap.get("FACULTY_SPONSOR_PERSON_ID")));
				}
				if (hmap.get("FACULTY_SPONSOR_PERSON") != null) {
					exemptForm.setFacultySponsorPerson((String) hmap.get("FACULTY_SPONSOR_PERSON"));
				}
				if (hmap.get("UNIT_NUMBER") != null) {
					exemptForm.setUnitNumber((String) hmap.get("UNIT_NUMBER"));
				}
				if (hmap.get("UNIT_NAME") != null) {
					exemptForm.setUnitName((String) hmap.get("UNIT_NAME"));
				}
				if (hmap.get("UPDATE_TIMESTAMP") != null) {
					exemptForm.setUpdateTimestamp((String) hmap.get("UPDATE_TIMESTAMP"));
				}
				if (hmap.get("SUMMARY") != null) {
					exemptForm.setSummary((String) hmap.get("SUMMARY"));
				}
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if (hmap.get("START_DATE") != null) {
					exemptForm.setExemptProtocolStartDate(df.format((Timestamp) hmap.get("START_DATE")));
				}
				if (hmap.get("END_DATE") != null) {
					exemptForm.setExemptProtocolEndDate(df.format((Timestamp) hmap.get("END_DATE")));
				}
				irbExemptFormList.add(exemptForm);
			}
			irbViewProfile.setIrbExemptFormList(irbExemptFormList);
		}
		return irbViewProfile;
	}

	public String getJobTitle(String personId) {
		String jobTitle = null;
		try {
			ArrayList<OutParameter> outParam = new ArrayList<>();
			ArrayList<InParameter> inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
			outParam.add(new OutParameter("jobsTitle", DBEngineConstants.TYPE_STRING));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction(inputParam,
					"FN_MITKC_GET_PER_JOB_TITLE", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				jobTitle = (String) hmResult.get("jobsTitle");
			}
		} catch (Exception e) {
			logger.error("Error in methord getJob title function", e);
		}
		return jobTitle;
	}

	@Override
	public IRBViewProfile getPersonExemptForm(Integer exemptFormId, String loginUserPersonId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormId)); // AV_PERSON_ID
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_EXEMPT_FORM", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getPersonExemptForm:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getPersonExemptForm:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getPersonExemptForm:" + e);
		}
		if (result != null && !result.isEmpty()) {
			List<IRBExemptForm> irbExemptFormList = new ArrayList<IRBExemptForm>();
			for (HashMap<String, Object> hmap : result) {
				IRBExemptForm exemptForm = new IRBExemptForm();
				if (hmap.get("IRB_PERSON_EXEMPT_FORM_ID") != null) {
					exemptForm.setExemptFormID(Integer.parseInt(hmap.get("IRB_PERSON_EXEMPT_FORM_ID").toString()));
				}
				if (hmap.get("PERSON_ID") != null) {
					exemptForm.setPersonId((String) hmap.get("PERSON_ID"));
					exemptForm.setPIJobTitle(getJobTitle((String) hmap.get("PERSON_ID")));
				}
				if (hmap.get("PERSON_NAME") != null) {
					exemptForm.setPersonName((String) hmap.get("PERSON_NAME"));
				}
				if (hmap.get("EXEMPT_TITLE") != null) {
					exemptForm.setExemptTitle((String) hmap.get("EXEMPT_TITLE"));
				}
				if (hmap.get("EXEMPT_FORM_NUMBER") != null) {
					exemptForm.setExemptFormNumber(Integer.parseInt(hmap.get("EXEMPT_FORM_NUMBER").toString()));
				}
				if (hmap.get("QUESTIONNAIRE_ANS_HEADER_ID") != null) {
					exemptForm.setExemptQuestionnaireAnswerHeaderId(
							Integer.parseInt(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID").toString()));
				}
				if (hmap.get("FACULTY_SPONSOR_PERSON_ID") != null) {
					exemptForm.setFacultySponsorPersonId((String) hmap.get("FACULTY_SPONSOR_PERSON_ID"));
					exemptForm.setFacultySponsorJobTitle(getJobTitle((String) hmap.get("FACULTY_SPONSOR_PERSON_ID")));
				}
				if (hmap.get("SUMMARY") != null) {
					exemptForm.setSummary((String) hmap.get("SUMMARY"));
				}
				DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
				if (hmap.get("START_DATE") != null) {
					exemptForm.setExemptProtocolStartDate(df.format((Timestamp) hmap.get("START_DATE")));
				}
				if (hmap.get("END_DATE") != null) {
					exemptForm.setExemptProtocolEndDate(df.format((Timestamp) hmap.get("END_DATE")));
				}
				if (hmap.get("UPDATE_TIMESTAMP") != null) {
					exemptForm.setUpdateTimestamp((String) hmap.get("UPDATE_TIMESTAMP"));
				}
				if (hmap.get("UPDATE_USER") != null) {
					exemptForm.setUpdateUser((String) hmap.get("UPDATE_USER"));
				}
				if (hmap.get("EXEMPT_STATUS") != null) {
					exemptForm.setStatus((String) hmap.get("EXEMPT_STATUS"));
				}
				if (hmap.get("EXEMPT_STATUS_CODE") != null) {
					exemptForm.setStatusCode((String) hmap.get("EXEMPT_STATUS_CODE"));
				}
				if (hmap.get("IS_EXEMPT_GRANTED") != null) {
					exemptForm.setIsExempt((String) hmap.get("IS_EXEMPT_GRANTED"));
				}
				if (hmap.get("FACULTY_SPONSOR_PERSON") != null) {
					exemptForm.setFacultySponsorPerson((String) hmap.get("FACULTY_SPONSOR_PERSON"));
				}
				if (hmap.get("UNIT_NUMBER") != null) {
					exemptForm.setUnitNumber((String) hmap.get("UNIT_NUMBER"));
				}
				if (hmap.get("UNIT_NAME") != null) {
					exemptForm.setUnitName((String) hmap.get("UNIT_NAME"));
				}
				if ((hmap.get("FACULTY_SPONSOR_PERSON_ID") != null && loginUserPersonId != null)
						&& (loginUserPersonId.equals(hmap.get("FACULTY_SPONSOR_PERSON_ID").toString()))) {
					exemptForm.setLoggedInUserFacultySponsor(true);
				} else {
					exemptForm.setLoggedInUserFacultySponsor(false);
				}
				if ((hmap.get("PERSON_ID") != null && loginUserPersonId != null)
						&& (loginUserPersonId.equals(hmap.get("PERSON_ID").toString()))) {
					exemptForm.setLoggedInUserPI(true);
				} else {
					exemptForm.setLoggedInUserPI(false);
				}
				irbExemptFormList.add(exemptForm);
			}
			irbViewProfile.setIrbExemptFormList(irbExemptFormList);
		}
		return irbViewProfile;
	}

	@Override
	public void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) throws ParseException {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		java.util.Date exemptStartDate = null;
		java.util.Date exemptEndDate = null;
		java.sql.Date sqlExemptStartDate = null;
		java.sql.Date sqlExemptEndDate = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		if (irbExemptForm.getExemptProtocolStartDate() != null) {
			exemptStartDate = sdf1.parse(irbExemptForm.getExemptProtocolStartDate());
			sqlExemptStartDate = new java.sql.Date(exemptStartDate.getTime());
		}
		if (irbExemptForm.getExemptProtocolEndDate() != null) {
			exemptEndDate = sdf1.parse(irbExemptForm.getExemptProtocolEndDate());
			sqlExemptEndDate = new java.sql.Date(exemptEndDate.getTime());
		}
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,
				irbExemptForm.getExemptFormID())); // AV_PERSON_ID
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, irbExemptForm.getPersonId()));
		inputParam.add(new InParameter("AV_PERSON_NAME", DBEngineConstants.TYPE_STRING, irbExemptForm.getPersonName()));
		inputParam.add(new InParameter("AV_EXEMPT_FORM_NUMBER", DBEngineConstants.TYPE_INTEGER,
				irbExemptForm.getExemptFormNumber()));
		inputParam.add(
				new InParameter("AV_EXEMPT_STATUS_CODE", DBEngineConstants.TYPE_STRING, irbExemptForm.getStatusCode()));
		inputParam.add(
				new InParameter("AV_IS_EXEMPT_GRANTED", DBEngineConstants.TYPE_STRING, irbExemptForm.getIsExempt()));
		inputParam
				.add(new InParameter("AV_EXEMPT_TITLE", DBEngineConstants.TYPE_STRING, irbExemptForm.getExemptTitle()));
		inputParam.add(new InParameter("AV_QUESTIONNAIRE_ANS_HEADER_ID", DBEngineConstants.TYPE_INTEGER,
				irbExemptForm.getExemptQuestionnaireAnswerHeaderId()));
		inputParam.add(new InParameter("AV_FACULTY_SPONSOR_PERSON_ID", DBEngineConstants.TYPE_STRING,
				irbExemptForm.getFacultySponsorPersonId()));
		inputParam.add(new InParameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, irbExemptForm.getUnitNumber()));
		inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, irbExemptForm.getUpdateUser()));
		inputParam.add(new InParameter("AV_SUMMARY", DBEngineConstants.TYPE_STRING, irbExemptForm.getSummary()));
		inputParam.add(new InParameter("AV_START_DATE", DBEngineConstants.TYPE_DATE, sqlExemptStartDate));
		inputParam.add(new InParameter("AV_END_DATE", DBEngineConstants.TYPE_DATE, sqlExemptEndDate));
		inputParam.add(new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING, actype));
		try {
			dbEngine.executeProcedure(inputParam, "UPD_IRB_PERSON_EXEMPT_FORM");
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in savePersonExemptForm method" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in savePersonExemptForm method" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in savePersonExemptForm method" + e);
		}
	}

	@SuppressWarnings("null")
	@Override
	public ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,
				irbExemptForm.getExemptFormID())); // AV_PERSON_ID
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_NOT_EXEMPT_QSTN_LIST", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getExemptMsg method:" + e);
		}
		return result;
	}

	@Override
	public Integer getNextExemptId() {
		Integer exemptId = null;
		try {
			ArrayList<OutParameter> outParam = new ArrayList<>();
			outParam.add(new OutParameter("returnId", DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction("FN_MITKC_IRB_NEXT_EXEMPT_ID",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				exemptId = Integer.parseInt((String) hmResult.get("returnId"));
			}
		} catch (Exception e) {
			logger.error("Error in methord getNextExemptId", e);
		}
		return exemptId;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList() {
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure("get_mitkc_all_units", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getLeadunitAutoCompleteList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getLeadunitAutoCompleteList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getLeadunitAutoCompleteList:" + e);
		}
		return result;
	}

	@Override
	public void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode,
			String updateUser, Integer notificationNumber, PersonDTO personDTO) {
		try {
			Integer adminNotificationNumber = 703;
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			outParam.add(new OutParameter("returnId", DBEngineConstants.TYPE_INTEGER));
			inParam.add(new InParameter("av_irb_person_exempt_form_id", DBEngineConstants.TYPE_INTEGER, formId));
			inParam.add(new InParameter("av_action_type_code", DBEngineConstants.TYPE_STRING, actionTypeCode));
			inParam.add(new InParameter("av_comment", DBEngineConstants.TYPE_STRING, comment));
			inParam.add(new InParameter("av_exempt_status_code", DBEngineConstants.TYPE_STRING, exemptstatusCode));
			inParam.add(new InParameter("av_update_user", DBEngineConstants.TYPE_STRING, updateUser));
			dbEngine.executeFunction(inParam, "fn_irb_exemp_form_action_log", outParam);
			if (notificationNumber != null) {
				logger.info("Sending Email Notification with status code: " + notificationNumber);
				sendingExemptNotifications(formId, comment, personDTO.getPersonID(), notificationNumber);
				sendingExemptNotifications(formId, null, personDTO.getPersonID(), adminNotificationNumber);
			}
		} catch (Exception e) {
			logger.error("Error in methord action log exempt questionnaire", e);
		}
	}

	public void sendingExemptNotifications(Integer formId, String comment, String loginPersonId,
			Integer notificationNumber) {
		try {
			exemptProtocolEmailNotification.sendingExemptEmailNotifications(formId, comment, loginPersonId,
					notificationNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addExemptProtocolAttachments(MultipartFile[] files, IRBExemptForm irbExemptForm) {
		try {
			Integer checkListId = null;
			if (!irbExemptForm.getCheckListAcType().equals("I")) {
				checkListId = irbExemptForm.getCheckListId();
			}
			ArrayList<InParameter> inputParam = null;
			for (int i = 0; i < files.length; i++) {
				inputParam = new ArrayList<>();
				inputParam.add(
						new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER, checkListId));
				inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,
						irbExemptForm.getExemptFormID()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,
						irbExemptForm.getCheckListDescription()));
				inputParam.add(
						new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING, files[i].getOriginalFilename()));
				inputParam.add(new InParameter("AV_FILE_DATA", DBEngineConstants.TYPE_BLOB, files[i].getBytes()));
				inputParam.add(
						new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,
						irbExemptForm.getUpdateUser()));
				inputParam.add(
						new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING, irbExemptForm.getCheckListAcType()));
				dbEngine.executeProcedure(inputParam, "UPD_IRB_EXEMPT_FORM_CHECKLST");
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in addExemptProtocolAttachments method" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in addExemptProtocolAttachments method" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in addExemptProtocolAttachments method" + e);
		}
	}

	@Override
	public ArrayList<HashMap<String, Object>> getExemptProtocolActivityLogs(Integer exemptFormID) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormID));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_EXEMPT_FORM_ACTION_LOG", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getExemptProtocolActivityLogs:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getExemptProtocolActivityLogs:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getExemptProtocolActivityLogs:" + e);
		}
		return result;
	}

	@Override
	public ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId) {
		Integer checkListAttachmentId = Integer.parseInt(checkListId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER,
					checkListAttachmentId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam,
					"GET_IRB_EXEMPT_FORM_CKLST_FILE", outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("FILE_DATA");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(hmResult.get("CONTENT_TYPE").toString()));
				String filename = hmResult.get("FILENAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadExemptProtocolAttachments method:" + e);
		}
		return attachmentData;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormID));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_EXEMPT_FORM_CHECKLST", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getExemptProtocolAttachmentList:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getExemptProtocolAttachmentList:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getExemptProtocolAttachmentList:" + e);
		}
		return result;
	}

	@Override
	public void updateExemptprotocolAttachments(IRBExemptForm irbExemptForm) {
		try {
			Integer checkListId = null;
			checkListId = irbExemptForm.getCheckListId();
			byte[] fileData = "".getBytes();
			ArrayList<InParameter> inputParam = null;
			inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER, checkListId));
			inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,
					irbExemptForm.getExemptFormID()));
			inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,
					irbExemptForm.getCheckListDescription()));
			inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING, ""));
			inputParam.add(new InParameter("AV_FILE_DATA", DBEngineConstants.TYPE_BLOB, fileData));
			inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING, null));
			inputParam.add(
					new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, irbExemptForm.getUpdateUser()));
			inputParam
					.add(new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING, irbExemptForm.getCheckListAcType()));
			dbEngine.executeProcedure(inputParam, "UPD_IRB_EXEMPT_FORM_CHECKLST");
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in updateExemptprotocolAttachments method" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in updateExemptprotocolAttachments method" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in updateExemptprotocolAttachments method" + e);
		}
	}

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
			Query queryDelete = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(
					"delete from ProtocolFundingSource p where p.protocolFundingSourceId =:protocolFundingSourceId");
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
	public IRBProtocolVO loadAttachmentType() {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		Query query = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery("from IRBAttachementTypes");
		irbProtocolVO.setIrbAttachementTypes(query.list());
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
			ProtocolGeneralInfo protocolGeneralInfoObj = (ProtocolGeneralInfo) queryGeneral.list().get(0);
			irbProtocolVO.setGeneralInfo(protocolGeneralInfoObj);
			irbProtocolVO.setProtocolPersonnelInfoList(protocolGeneralInfoObj.getPersonnelInfos());
			Query queryScienceOfProtocol = hibernateTemplate.getSessionFactory().getCurrentSession()
					.createQuery("from ScienceOfProtocol p where p.protocolId =:protocolId");
			queryScienceOfProtocol.setInteger("protocolId", protocolId);
			irbProtocolVO.setScienceOfProtocol((ScienceOfProtocol)queryScienceOfProtocol.list().get(0));
			
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
	public IRBProtocolVO loadSponsorTypes(IRBProtocolVO irbProtocolVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(SponsorType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("sponsorCode"), "sponsorCode");
		projList.add(Projections.property("sponsorName"), "sponsorName");
		projList.add(Projections.property("sponsorTypeCode"), "sponsorTypeCode");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(SponsorType.class));
		criteria.addOrder(Order.asc("sponsorName"));
		List<SponsorType> sponsorType = criteria.list();
		logger.info("Protocol Type in DAO: " + sponsorType);
		irbProtocolVO.setSponsorType(sponsorType);
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO saveScienceOfProtocol(IRBProtocolVO irbProtocolVO, ScienceOfProtocol scienceOfProtocol) {
		hibernateTemplate.saveOrUpdate(scienceOfProtocol);
		irbProtocolVO.setScienceOfProtocol(scienceOfProtocol);
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
}
