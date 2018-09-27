package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.json.JSONObject;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
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
	public IRBViewProfile getPersonExemptFormList(CommonVO vo) throws ParseException {
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptFormList(vo.getPersonId(), vo.getPersonRoleType(), vo.getTitle(), vo.getPiName(), vo.getDetermination(), vo.getExemptFormfacultySponsorName(), vo.getExemptFormStartDate(), vo.getExemptFormEndDate());
		return irbViewProfile;
	}

	@Override
	public CommonVO savePersonExemptForms(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception {
		irbExemptForm.setStatusCode("1");
		Integer exemptId = irbProtocolDao.getNextExemptId();
		irbExemptForm.setExemptFormID(exemptId);
		savePersonExemptForm(irbExemptForm,"I");
		irbProtocolDao.irbExemptFormActionLog(irbExemptForm.getExemptFormID(), irbExemptForm.getActionTypesCode(), irbExemptForm.getComment(), irbExemptForm.getStatusCode(), irbExemptForm.getUpdateUser(),irbExemptForm.getNotificationNumber(),personDTO);
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(exemptId,null);
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			irbExemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		QuestionnaireDto questionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, "0");
		CommonVO commonVO = new CommonVO();
		commonVO.setQuestionnaireDto(questionnaireDto);
		commonVO.setIrbExemptForm(irbExemptForm);		
		return commonVO;
	}

	@Override
	public CommonVO saveQuestionnaire(IRBExemptForm irbExemptForm,QuestionnaireDto questionnaireDto, String questionnaireInfobean, PersonDTO personDTO) throws Exception {		
		String moduleItemId = getModuleItemId(personDTO, irbExemptForm);
		Integer questionnaireHeaderId =  saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,moduleItemId, personDTO,irbExemptForm);	
		irbExemptForm.setExemptQuestionnaireAnswerHeaderId(questionnaireHeaderId);
		ArrayList<HashMap<String, Object>> questionArrayList = new ArrayList<>();
		boolean isSubmit = isSubmit(questionnaireInfobean); 
		if(isQuestionnaireComplete(questionnaireInfobean) && isSubmit){
			irbProtocolDao.irbExemptFormActionLog(irbExemptForm.getExemptFormID(), irbExemptForm.getActionTypesCode(), irbExemptForm.getComment(), irbExemptForm.getStatusCode(), irbExemptForm.getUpdateUser(),irbExemptForm.getNotificationNumber(),personDTO);
		}
		if(!isSubmit){
			irbExemptForm.setStatusCode("1");
		}
		savePersonExemptForm(irbExemptForm,"U");
		questionArrayList = getExemptMsg(irbExemptForm);	
		irbExemptForm.setStatusCode(irbExemptForm.getStatusCode());
		if(!questionArrayList.isEmpty()){
			int questId =Integer.parseInt(questionArrayList.get(0).get("QUESTION_ID").toString());
			int questIdFromDB= 0;
				if(questId== questIdFromDB){
					irbExemptForm.setIsExempt("O");
				} else {
					irbExemptForm.setIsExempt("N");
				}
		}else{
			irbExemptForm.setIsExempt("Y");
		} 
		savePersonExemptForm(irbExemptForm,"U");
		CommonVO commonVO = new CommonVO();
		String moduleItemsId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnairesDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemsId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID(),personDTO.getPersonID());
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			irbExemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		commonVO.setQuestionnaireDto(questionnairesDto);
		commonVO.setIrbExemptForm(irbExemptForm);
		return commonVO;
	}

	private Integer saveQuestionnaireAnswers(QuestionnaireDto questionnaireDto, String questionnaireInfobean,String moduleItemId,PersonDTO personDTO, IRBExemptForm exemptForm) throws Exception {		
		return questionnaireService.saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,KeyConstants.COEUS_MODULE_PERSON,moduleItemId, personDTO, exemptForm);
	}
	
	private boolean isSubmit(String questionnaireInfobean) throws Exception {
		JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
		String questionnaireCompletionFlag = questionnaireJsnobject.get("isSubmit").toString();
		if("Y".equalsIgnoreCase(questionnaireCompletionFlag)){
			return true;
		}
		return false;
	}		
	
	private ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm) {
		return irbProtocolDao.getExemptMsg(irbExemptForm);		
	}
	
	private void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) throws ParseException{
		irbProtocolDao.savePersonExemptForm(irbExemptForm,actype);
	}

	private boolean isQuestionnaireComplete(String questionnaireInfobean){
		JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
		String questionnaireCompletionFlag = questionnaireJsnobject.get("QuestionnaireCompletionFlag").toString();
		if("Y".equalsIgnoreCase(questionnaireCompletionFlag)){
			return true;
		}
		return false;
	}

	@Override
	public CommonVO getPersonExemptForm(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception {
		CommonVO commonVO = new CommonVO();
		String moduleItemId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID(), personDTO.getPersonID());
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			irbExemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		commonVO.setQuestionnaireDto(questionnaireDto);
		commonVO.setIrbExemptForm(irbExemptForm);
		return commonVO;		
	}
	
	private String getModuleItemId(String personId,IRBExemptForm irbExemptForm){
		String moduleItemId  = "0";
		try{
		 moduleItemId = personId+(irbExemptForm.getExemptFormNumber() == null ? "0" : (String)irbExemptForm.getExemptFormNumber().toString());
		}catch(Exception e){
			logger.info("error at getModuleItemId");
		}
		return moduleItemId;
	}
	
	private String getModuleItemId(PersonDTO personDTO,IRBExemptForm irbExemptForm){
		return getModuleItemId(irbExemptForm.getPersonId(),irbExemptForm);
	}

	@Override
	public CommonVO getEvaluateMessage(IRBExemptForm exemptForm) {
		CommonVO vo= new CommonVO();
		ArrayList<HashMap<String, Object>> questionList = new ArrayList<>();
		questionList = irbProtocolDao.getExemptMsg(exemptForm);
		vo.setExemptQuestionList(questionList);
		if(!questionList.isEmpty()){
			int questId =Integer.parseInt(questionList.get(0).get("QUESTION_ID").toString());
			int questIdFromDB= 0;
			if(questId== questIdFromDB){
				vo.setIsExemptGranted("O");
			} else {
				vo.setIsExemptGranted("N");
			}
		} else {
			vo.setIsExemptGranted("Y");
		} 
		return vo;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList() {
		ArrayList<HashMap<String, Object>> leadUnitList = irbProtocolDao.getLeadunitAutoCompleteList();
		return leadUnitList;
	}

	@Override
	public void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode, String updateUser, Integer notificationNumber, PersonDTO personDTO) {
		irbProtocolDao.irbExemptFormActionLog(formId,actionTypeCode,comment,exemptstatusCode,updateUser,notificationNumber,personDTO);
	}

	@Override
	public ArrayList<HashMap<String, Object>> addExemptProtocolAttachments(MultipartFile[] files, String formDataJson) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			IRBExemptForm jsonObj = mapper.readValue(formDataJson, IRBExemptForm.class);
			if(jsonObj.getCheckListAcType().equals("U") || (jsonObj.getCheckListAcType().equals("D"))){
				irbProtocolDao.updateExemptprotocolAttachments(jsonObj);
			} else if(jsonObj.getCheckListAcType().equals("I")){
				irbProtocolDao.addExemptProtocolAttachments(files,jsonObj);
			}
			result = getExemptProtocolAttachmentList(jsonObj.getExemptFormID());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public CommonVO getExemptProtocolActivityLogs(Integer exemptFormID) {
		CommonVO vo = new CommonVO();
		IRBExemptForm exemptForm = new IRBExemptForm();
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(exemptFormID, null);
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			exemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		ArrayList<HashMap<String, Object>> actionLogs= irbProtocolDao.getExemptProtocolActivityLogs(exemptFormID);
		vo.setIrbExemptForm(exemptForm);
		vo.setActionLogs(actionLogs);
		//ProtocolGeneralInfo generalInfo1 = new ProtocolGeneralInfo();
		//generalInfo1 = hibernateTemplate.get(ProtocolGeneralInfo.class, 113);
		
		/*Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolGeneralInfo.class);
		ProtocolGeneralInfo generalInfo = null;
		generalInfo = (ProtocolGeneralInfo) criteria.list().get(0);*/
		//logger.info("General Obj: "+generalInfo1);
		//ProtocolGeneralInfo generalInfo = new ProtocolGeneralInfo();
		//logger.info("protocol Types: "+ irbProtocolDao.loadProtocolTypes());
	/*	generalInfo.setProtocolTypeCode("2");
		generalInfo.setProtocolStatusCode("202");
		String str="2015-03-29";  
	    Date satrtDate=Date.valueOf(str);
		generalInfo.setProtocolStartDate(satrtDate);
		generalInfo.setProtocolEndDate(satrtDate);
		generalInfo.setProtocolTitle("Protocol title save test using hibernate zzzz");
		//generalInfo.setprotocold("Protocol save test using hibernate with description");
		generalInfo.setUpdateTimestamp(satrtDate);
		generalInfo.setUpdateUser("test user1 yy");
		generalInfo.setActive("Y");
		generalInfo.setProtocolId(9);
		generalInfo.setProtocolNumber("25");
		generalInfo.setSequenceNumber(255);
		irbProtocolDao.updateGeneralInfo(generalInfo);*/
		//logger.info("Roles: "+irbProtocolDao.loadRoleTypes(new IRBProtocolVO()));
		/*logger.info("Lead Units: "+irbProtocolDao.loadProtocolPersonLeadunits());
		logger.info("Affiliations: "+irbProtocolDao.loadProtocolAffiliationTypes());
		logger.info("Subjects: "+irbProtocolDao.loadProtocolSubjectTypes());
		logger.info("Funding source: "+irbProtocolDao.loadProtocolFundingSourceTypes());*/
		//IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		//logger.info("loadProtocolCollaboratorNames: "+irbProtocolDao.loadProtocolCollaboratorNames(irbProtocolVO));
		
		return vo;
	}

	@Override
	public ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadExemptProtocolAttachments(checkListId);
		return attachments;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID) {
		ArrayList<HashMap<String, Object>> checkList= irbProtocolDao.getExemptProtocolAttachmentList(exemptFormID);
		return checkList;
	}

	@Override
	public CommonVO approveOrDisapproveExemptProtocols(CommonVO vo) throws Exception {
		CommonVO commonVO = new CommonVO();
		savePersonExemptForm(vo.getIrbExemptForm(),"U");
		irbProtocolDao.irbExemptFormActionLog(vo.getIrbExemptForm().getExemptFormID(), vo.getIrbExemptForm().getActionTypesCode(), vo.getIrbExemptForm().getComment(), vo.getIrbExemptForm().getStatusCode(), vo.getIrbExemptForm().getUpdateUser(),vo.getIrbExemptForm().getNotificationNumber(),vo.getPersonDTO());
		commonVO = getPersonExemptForm(vo.getIrbExemptForm(), vo.getPersonDTO());
		return commonVO;
	}

	@Override
	public IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = irbProtocolDao.updateGeneralInfo(generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadRoleTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadRoleTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolPersonLeadunits(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolPersonLeadunits(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolAffiliationTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolAffiliationTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolSubjectTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolSubjectTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolFundingSourceTypes(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolFundingSourceTypes(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolCollaboratorNames(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolCollaboratorNames(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateProtocolPersonInfo(personnelInfo);
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
	public IRBProtocolVO loadAttachmentType() {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.loadAttachmentType();
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolDetails(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public ProtocolGeneralInfo loadProtocolById(Integer protocolId) {
		return irbProtocolDao.loadProtocolById(protocolId);
	}

	@Override
	public IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
		try {
			irbProtocolVO = irbProtocolDao.addProtocolAttachments(files,formDataJson);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return irbProtocolVO;
	}
}
