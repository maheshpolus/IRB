package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="iRBProtocolDao")
@Transactional
public class IRBProtocolDaoImpl implements IRBProtocolDao{

	DBEngine dbEngine;
	
	IRBProtocolDaoImpl(){
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
			logger.info("DBException in getIRBProtocolDetails:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBProtocolDetails:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBProtocolDetails:"+ e);
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
			logger.info("DBException in getIRBprotocolPersons:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolPersons:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolPersons:"+ e);
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
			logger.info("DBException in getIRBprotocolFundingSource:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolFundingSource:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolFundingSource:"+ e);
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
			logger.info("DBException in getIRBprotocolLocation:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolLocation:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolLocation:"+ e);
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
			logger.info("DBException in getIRBprotocolVulnerableSubject:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolVulnerableSubject:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolVulnerableSubject:"+ e);
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
			logger.info("DBException in getIRBprotocolSpecialReview:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getIRBprotocolSpecialReview:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getIRBprotocolSpecialReview:"+ e);
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
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getMITKCPersonInfo:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getMITKCPersonInfo:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getMITKCPersonInfo:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolMITKCPersonInfo(result.get(0));
			resultTraing = getMITKCPersonTraingInfo(avPersonId);
			irbViewProfile.setIrbViewProtocolMITKCPersonTrainingInfo(resultTraing);
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
			logger.info("DBException in getMITKCPersonTraingInfo:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getMITKCPersonTraingInfo:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getMITKCPersonTraingInfo:"+ e);
		}
		if (result != null && !result.isEmpty()) {
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
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_MITKC_ATTACHMENT_FILE", outParam);
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
			logger.info("Exception in downloadAttachments method:"+ e);
		}
		return attachmentData;
	}

	@Override
	public IRBViewProfile getAttachmentsList(String protocolNumber) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_ATTACHMENT", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getAttachmentsList:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getAttachmentsList:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getAttachmentsList:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolAttachmentList(result);
		}
		return irbViewProfile;
	}
	
	@Override
	public IRBViewProfile getProtocolHistotyGroupList(String protocolNumber) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_HISTORY_GROUP", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getProtocolHistotyGroupList:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getProtocolHistotyGroupList:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getProtocolHistotyGroupList:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolHistoryGroupList(result);
		}
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId, Integer nextGroupActionId,Integer previousGroupActionId) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PROTOCOL_ID", DBEngineConstants.TYPE_INTEGER, protocolId));
		inputParam.add(new InParameter("AV_ACTION_ID", DBEngineConstants.TYPE_INTEGER, actionId));
		inputParam.add(new InParameter("AV_NEXT_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, nextGroupActionId));
		inputParam.add(new InParameter("AV_PREVIOUS_GROUP_ACTION_ID", DBEngineConstants.TYPE_INTEGER, previousGroupActionId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_HISTORY_DET", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getProtocolHistotyGroupDetails:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getProtocolHistotyGroupDetails:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getProtocolHistotyGroupDetails:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbViewProtocolHistoryGroupDetails(result);
		}
		return irbViewProfile;
	}

	@SuppressWarnings("null")
	@Override
	public IRBViewProfile getPersonExemptFormList(PersonDTO personDTO) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_INTEGER, Integer.parseInt(personDTO.getPersonID())));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_EXEMPT_PER_FORM", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getPersonExemptFormList:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getPersonExemptFormList:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getPersonExemptFormList:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			List<IRBExemptForm> irbExemptFormList = new ArrayList<IRBExemptForm>();
			for(HashMap<String, Object> hmap: result){
				IRBExemptForm exemptForm = new IRBExemptForm();
				if(hmap.get("IRB_PERSON_EXEMPT_FORM_ID") != null){
					exemptForm.setExemptFormID(Integer.parseInt(hmap.get("IRB_PERSON_EXEMPT_FORM_ID").toString()));
				}
				if(hmap.get("PERSON_ID") != null){
					exemptForm.setPersonId((String) hmap.get("PERSON_ID"));
				}
				if(hmap.get("PERSON_NAME") != null){
					exemptForm.setPersonName((String) hmap.get("PERSON_NAME"));
				}
				if(hmap.get("EXEMPT_TITLE") != null){
					exemptForm.setExemptTitle((String) hmap.get("EXEMPT_TITLE"));
				}
				if(hmap.get("EXEMPT_FORM_NUMBER") != null){
					exemptForm.setExemptFormNumber(Integer.parseInt(hmap.get("EXEMPT_FORM_NUMBER").toString()));
				}
				if(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID") != null){
					exemptForm.setExemptQuestionnaireAnswerHeaderId(Integer.parseInt(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID").toString()));
				}
				if(hmap.get("UPDATE_USER") != null){
					exemptForm.setUpdateUser((String) hmap.get("UPDATE_USER"));
				}
				if(hmap.get("EXEMPT_STATUS") != null){
					exemptForm.setStatus((String) hmap.get("EXEMPT_STATUS"));
				}
				if(hmap.get("EXEMPT_STATUS_CODE") != null){
					exemptForm.setStatusCode((String) hmap.get("EXEMPT_STATUS_CODE"));
				}
				if(hmap.get("IS_EXEMPT_GRANTED") != null){
					exemptForm.setIsExempt((String) hmap.get("IS_EXEMPT_GRANTED"));
				}
				if(hmap.get("FACULTY_SPONSOR_PERSON_ID") != null){
					exemptForm.setFacultySponsorPersonId((String) hmap.get("FACULTY_SPONSOR_PERSON_ID"));
				}
				if(hmap.get("FACULTY_SPONSOR_PERSON") != null){
					exemptForm.setFacultySponsorPerson((String) hmap.get("FACULTY_SPONSOR_PERSON"));
				}
				if(hmap.get("UNIT_NUMBER") != null){
					exemptForm.setUnitNumber((String) hmap.get("UNIT_NUMBER"));
				}
				if(hmap.get("UNIT_NAME") != null){
					exemptForm.setUnitName((String) hmap.get("UNIT_NAME"));
				}
				if(hmap.get("UPDATE_TIMESTAMP") != null){
					exemptForm.setUpdateTimestamp((String) hmap.get("UPDATE_TIMESTAMP"));
				}
				if(hmap.get("SUMMARY") != null){
					exemptForm.setSummary((String) hmap.get("SUMMARY"));
				}
				irbExemptFormList.add(exemptForm);
			}
			irbViewProfile.setIrbExemptFormList(irbExemptFormList);
		}
		return irbViewProfile;
	}
	@Override
	public IRBViewProfile getPersonExemptForm(Integer exemptFormId) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormId)); //AV_PERSON_ID
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_EXEMPT_FORM", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getPersonExemptForm:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getPersonExemptForm:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getPersonExemptForm:"+ e);
		}
		if (result != null && !result.isEmpty()) {
			List<IRBExemptForm> irbExemptFormList = new ArrayList<IRBExemptForm>();
			for(HashMap<String, Object> hmap: result){
				IRBExemptForm exemptForm = new IRBExemptForm();
				if(hmap.get("IRB_PERSON_EXEMPT_FORM_ID") != null){
					exemptForm.setExemptFormID(Integer.parseInt(hmap.get("IRB_PERSON_EXEMPT_FORM_ID").toString()));
				}
				if(hmap.get("PERSON_ID") != null){
					exemptForm.setPersonId((String) hmap.get("PERSON_ID"));
				}
				if(hmap.get("PERSON_NAME") != null){
					exemptForm.setPersonName((String) hmap.get("PERSON_NAME"));
				}
				if(hmap.get("EXEMPT_TITLE") != null){
					exemptForm.setExemptTitle((String) hmap.get("EXEMPT_TITLE"));
				}
				if(hmap.get("EXEMPT_FORM_NUMBER") != null){
					exemptForm.setExemptFormNumber(Integer.parseInt(hmap.get("EXEMPT_FORM_NUMBER").toString()));
				}
				if(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID") != null){
					exemptForm.setExemptQuestionnaireAnswerHeaderId(Integer.parseInt(hmap.get("QUESTIONNAIRE_ANS_HEADER_ID").toString()));
				}
				if(hmap.get("FACULTY_SPONSOR_PERSON_ID") != null){
					exemptForm.setFacultySponsorPersonId((String) hmap.get("FACULTY_SPONSOR_PERSON_ID"));
				}
				if(hmap.get("SUMMARY") != null){
					exemptForm.setSummary((String) hmap.get("SUMMARY"));
				}
				if(hmap.get("UPDATE_TIMESTAMP") != null){
					exemptForm.setUpdateTimestamp((String) hmap.get("UPDATE_TIMESTAMP"));
				}
				if(hmap.get("UPDATE_USER") != null){
					exemptForm.setUpdateUser((String) hmap.get("UPDATE_USER"));
				}
				if(hmap.get("EXEMPT_STATUS") != null){
					exemptForm.setStatus((String) hmap.get("EXEMPT_STATUS"));
				}
				if(hmap.get("EXEMPT_STATUS_CODE") != null){
					exemptForm.setStatusCode((String) hmap.get("EXEMPT_STATUS_CODE"));
				}
				if(hmap.get("IS_EXEMPT_GRANTED") != null){
					exemptForm.setIsExempt((String) hmap.get("IS_EXEMPT_GRANTED"));
				}
				if(hmap.get("FACULTY_SPONSOR_PERSON") != null){
					exemptForm.setFacultySponsorPerson((String) hmap.get("FACULTY_SPONSOR_PERSON"));
				}
				if(hmap.get("UNIT_NUMBER") != null){
					exemptForm.setUnitNumber((String) hmap.get("UNIT_NUMBER"));
				}
				if(hmap.get("UNIT_NAME") != null){
					exemptForm.setUnitName((String) hmap.get("UNIT_NAME"));
				}
				irbExemptFormList.add(exemptForm);
			}
			irbViewProfile.setIrbExemptFormList(irbExemptFormList);
		}
		return irbViewProfile;
	}

	@Override
	public void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, irbExemptForm.getExemptFormID())); //AV_PERSON_ID
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, irbExemptForm.getPersonId()));
		inputParam.add(new InParameter("AV_PERSON_NAME", DBEngineConstants.TYPE_STRING, irbExemptForm.getPersonName()));
		inputParam.add(new InParameter("AV_EXEMPT_FORM_NUMBER", DBEngineConstants.TYPE_INTEGER, irbExemptForm.getExemptFormNumber()));
		inputParam.add(new InParameter("AV_EXEMPT_STATUS_CODE", DBEngineConstants.TYPE_STRING, irbExemptForm.getStatusCode()));
		inputParam.add(new InParameter("AV_IS_EXEMPT_GRANTED", DBEngineConstants.TYPE_STRING, irbExemptForm.getIsExempt()));
		inputParam.add(new InParameter("AV_EXEMPT_TITLE", DBEngineConstants.TYPE_STRING, irbExemptForm.getExemptTitle()));
		inputParam.add(new InParameter("AV_QUESTIONNAIRE_ANS_HEADER_ID", DBEngineConstants.TYPE_INTEGER, irbExemptForm.getExemptQuestionnaireAnswerHeaderId()));
		inputParam.add(new InParameter("AV_FACULTY_SPONSOR_PERSON_ID", DBEngineConstants.TYPE_STRING, irbExemptForm.getFacultySponsorPersonId()));
		inputParam.add(new InParameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, irbExemptForm.getUnitNumber()));
		inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING, irbExemptForm.getUpdateUser()));
		inputParam.add(new InParameter("AV_SUMMARY", DBEngineConstants.TYPE_STRING, irbExemptForm.getSummary()));
		inputParam.add(new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING, actype));
		try {
			dbEngine.executeProcedure(inputParam, "UPD_IRB_PERSON_EXEMPT_FORM");
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in savePersonExemptForm method"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in savePersonExemptForm method"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in savePersonExemptForm method"+ e);
		}
	}

	@SuppressWarnings("null")
	@Override
	public ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, irbExemptForm.getExemptFormID())); //AV_PERSON_ID
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_NOT_EXEMPT_QSTN_LIST", outputParam);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in getExemptMsg method:"+ e);
		}
		return result;
	}

	
	@Override
	public Integer getNextExemptId() {
		Integer exemptId = null;
		try{
			ArrayList<OutParameter> outParam = new ArrayList<>();
			outParam.add(new OutParameter("returnId",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = dbEngine.executeFunction("FN_MITKC_IRB_NEXT_EXEMPT_ID",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				exemptId = Integer.parseInt((String)hmResult.get("returnId"));
			}		
		}catch(Exception e){
			logger.error("Error in methord getNextExemptId",e);
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
			logger.info("DBException in getLeadunitAutoCompleteList:"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getLeadunitAutoCompleteList:"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getLeadunitAutoCompleteList:"+ e);
		}
		return result;
	}
}
