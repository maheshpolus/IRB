package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.dao.IRBExemptProtocolDao;
import org.mit.irb.web.IRBProtocol.service.IRBExemptProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.notification.ExemptProtocolEmailNotification;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "irbExemptProtocolDao")
@Transactional
public class IRBExemptProtocolDaoImpl implements IRBExemptProtocolDao{
	
	@Autowired
	ExemptProtocolEmailNotification exemptProtocolEmailNotification;
	
	@Autowired
	IRBExemptProtocolService irbExemptProtocolService;
	
	
	DBEngine dbEngine;
	Logger logger = Logger.getLogger(IRBExemptProtocolDaoImpl.class.getName());
	
	IRBExemptProtocolDaoImpl() {
		dbEngine = new DBEngine();
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
				inputParam.add(new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER, checkListId));
				inputParam.add(new InParameter("AV_IRB_PERSON_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER,irbExemptForm.getExemptFormID()));
				inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING, irbExemptForm.getCheckListDescription()));
				inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING, files[i].getOriginalFilename()));
				inputParam.add(new InParameter("AV_FILE_DATA", DBEngineConstants.TYPE_BLOB, files[i].getBytes()));
				inputParam.add(new InParameter("AV_CONTENT_TYPE", DBEngineConstants.TYPE_STRING, files[i].getContentType()));
				inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,irbExemptForm.getUpdateUser()));
				inputParam.add(new InParameter("AC_TYPE", DBEngineConstants.TYPE_STRING, irbExemptForm.getCheckListAcType()));
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
			inParam.add(new InParameter("AV_EXEMPT_FORM_CHECKLST_ID", DBEngineConstants.TYPE_INTEGER, checkListAttachmentId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_EXEMPT_FORM_CKLST_FILE", outParam);
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
				if(hmap.get("CREATE_PERSON_ID") != null){
					exemptForm.setCreatedUser((String) hmap.get("CREATE_PERSON_ID"));
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
				if(hmap.get("CREATE_PERSON_ID") != null){
					exemptForm.setCreatedUser((String) hmap.get("CREATE_PERSON_ID"));
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
					String exemptStatusCode = (String) hmap.get("EXEMPT_STATUS_CODE");
					CommonVO commonVO = new CommonVO();
					exemptForm.setStatusCode(exemptStatusCode);
					if(!exemptStatusCode.equals("1")){
						commonVO = irbExemptProtocolService.getEvaluateMessage(exemptForm);
						exemptForm.setExemptQuestionList(commonVO.getExemptQuestionList());
						exemptForm.setSubmissionDate(getExemptFormSubmissionDate(Integer.parseInt(hmap.get("IRB_PERSON_EXEMPT_FORM_ID").toString())));
					}
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
				if(notificationNumber ==702){
					sendingExemptNotifications(formId, null, personDTO.getPersonID(), adminNotificationNumber);
				}
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
	
	public String getExemptFormSubmissionDate(Integer exemptFormId) {
		String exemptFormSubmissionDate = null;
		try {
			ArrayList<OutParameter> outParam = new ArrayList<>();
			ArrayList<InParameter> inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_EXEMPT_FORM_ID", DBEngineConstants.TYPE_INTEGER, exemptFormId));
			outParam.add(new OutParameter("exemptFormSubmissionDate", DBEngineConstants.TYPE_STRING));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction(inputParam,"fn_irb_exemptform_submisn_date",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				exemptFormSubmissionDate = (String) hmResult.get("exemptFormSubmissionDate");
			}
		} catch (Exception e) {
			logger.error("Exception in methord getExemptFormSubmissionDate", e);
		}
		return exemptFormSubmissionDate;
	}
}
