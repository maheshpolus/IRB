package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBExemptProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ExemptFundingSource;
import org.mit.irb.web.IRBProtocol.service.IRBExemptProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service(value = "irbExemptProtocolService")
public class IRBExemptProtocolServImpl implements IRBExemptProtocolService{

	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	IRBExemptProtocolDao irbExemptProtocolDao;
	
	Logger logger = Logger.getLogger(IRBExemptProtocolServImpl.class.getName());
	
	@Override
	public CommonVO getPersonExemptForm(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception {
		CommonVO commonVO = new CommonVO();
		String moduleItemId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID(), personDTO.getPersonID());
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
		boolean checkIsExempt = true;
		boolean checkOther = false;
		ArrayList<HashMap<String, Object>> questionList = new ArrayList<>();
		ArrayList<HashMap<String, Object>> nonExemptCategoryList = new ArrayList<>();
		ArrayList<HashMap<String, Object>> exemptCategoryList = new ArrayList<>();
		ArrayList<HashMap<String, Object>> otherCategoryList = new ArrayList<>();
		questionList = irbExemptProtocolDao.getExemptMsg(exemptForm);
		for(HashMap<String, Object> answerDetails: questionList){
			if(answerDetails.get("IS_EXEMPT_FLAG").equals("N")){
				checkIsExempt = false;
				nonExemptCategoryList.add(answerDetails);
			} else if(answerDetails.get("IS_EXEMPT_FLAG").equals("Y")){
				exemptCategoryList.add(answerDetails);
			}else if (answerDetails.get("IS_EXEMPT_FLAG").equals("O")){
				checkOther =true;
				otherCategoryList.add(answerDetails);
			}
		}
		if(checkOther) {
			vo.setIsExemptGranted("O");
			vo.setExemptQuestionList(otherCategoryList);
			return vo;
		}
		if(checkIsExempt == true){
			vo.setIsExemptGranted("Y");
			vo.setExemptQuestionList(exemptCategoryList);			
		} else{
			vo.setIsExemptGranted("N");
			vo.setExemptQuestionList(nonExemptCategoryList);
		}
		return vo;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList() {
		ArrayList<HashMap<String, Object>> leadUnitList = irbExemptProtocolDao.getLeadunitAutoCompleteList();
		return leadUnitList;
	}

	@Override
	public void irbExemptFormActionLog(Integer formId, String actionTypeCode, String comment, String exemptstatusCode, String updateUser, Integer notificationNumber, PersonDTO personDTO) {
		irbExemptProtocolDao.irbExemptFormActionLog(formId,actionTypeCode,comment,exemptstatusCode,updateUser,notificationNumber,personDTO);
	}

	@Override
	public ArrayList<HashMap<String, Object>> addExemptProtocolAttachments(MultipartFile[] files, String formDataJson) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			IRBExemptForm jsonObj = mapper.readValue(formDataJson, IRBExemptForm.class);
			if(jsonObj.getCheckListAcType().equals("U") || (jsonObj.getCheckListAcType().equals("D"))){
				irbExemptProtocolDao.updateExemptprotocolAttachments(jsonObj);
			} else if(jsonObj.getCheckListAcType().equals("I")){
				irbExemptProtocolDao.addExemptProtocolAttachments(files,jsonObj);
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
		IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptForm(exemptFormID, null);
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			exemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		ArrayList<HashMap<String, Object>> actionLogs= irbExemptProtocolDao.getExemptProtocolActivityLogs(exemptFormID);
		vo.setIrbExemptForm(exemptForm);
		vo.setActionLogs(actionLogs);
		return vo;
	}

	@Override
	public ResponseEntity<byte[]> downloadExemptProtocolAttachments(String checkListId) {
		ResponseEntity<byte[]> attachments = irbExemptProtocolDao.downloadExemptProtocolAttachments(checkListId);
		return attachments;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(Integer exemptFormID) {
		ArrayList<HashMap<String, Object>> checkList= irbExemptProtocolDao.getExemptProtocolAttachmentList(exemptFormID);
		return checkList;
	}

	@Override
	public CommonVO approveOrDisapproveExemptProtocols(CommonVO vo) throws Exception {
		CommonVO commonVO = new CommonVO();
		savePersonExemptForm(vo.getIrbExemptForm(),"U");
		irbExemptProtocolDao.irbExemptFormActionLog(vo.getIrbExemptForm().getExemptFormID(), vo.getIrbExemptForm().getActionTypesCode(), vo.getIrbExemptForm().getComment(), vo.getIrbExemptForm().getStatusCode(), vo.getIrbExemptForm().getUpdateUser(),vo.getIrbExemptForm().getNotificationNumber(),vo.getPersonDTO());
		commonVO = getPersonExemptForm(vo.getIrbExemptForm(), vo.getPersonDTO());
		return commonVO;
	}
	
	@Override
	public CommonVO savePersonExemptForms(IRBExemptForm irbExemptForm, PersonDTO personDTO) throws Exception {
		irbExemptForm.setStatusCode("1");
		Integer exemptId = irbExemptProtocolDao.getNextExemptId();
		irbExemptForm.setExemptFormID(exemptId);
		savePersonExemptForm(irbExemptForm,"I");
		irbExemptProtocolDao.irbExemptFormActionLog(irbExemptForm.getExemptFormID(), irbExemptForm.getActionTypesCode(), irbExemptForm.getComment(), irbExemptForm.getStatusCode(), irbExemptForm.getUpdateUser(),irbExemptForm.getNotificationNumber(),personDTO);
		IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptForm(exemptId,null);
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			irbExemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}	
		QuestionnaireDto questionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, "0");
		CommonVO commonVO = new CommonVO();
		commonVO.setQuestionnaireDto(questionnaireDto);
		commonVO.setIrbExemptForm(irbExemptForm);		
		return commonVO;
	}
	
	private void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype) throws ParseException{
		irbExemptProtocolDao.savePersonExemptForm(irbExemptForm,actype);
	}

	@Override
	public IRBViewProfile getPersonExemptFormList(CommonVO vo) throws ParseException {
		IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptFormList(vo.getPersonId(), vo.getPersonRoleType(), vo.getTitle(), vo.getPiName(), vo.getDetermination(), vo.getExemptFormfacultySponsorName(), vo.getExemptFormStartDate(), vo.getExemptFormEndDate());
		return irbViewProfile;
	}

	private Integer saveQuestionnaireAnswers(QuestionnaireDto questionnaireDto, String questionnaireInfobean,String moduleItemId,PersonDTO personDTO, IRBExemptForm exemptForm) throws Exception {		
		return questionnaireService.saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,KeyConstants.COEUS_MODULE_PERSON,moduleItemId, personDTO, exemptForm);
	}

	private ArrayList<HashMap<String, Object>> getExemptMsg(IRBExemptForm irbExemptForm) {
		return irbExemptProtocolDao.getExemptMsg(irbExemptForm);		
	}
	
	@Override
	public CommonVO saveQuestionnaire(IRBExemptForm irbExemptForm,QuestionnaireDto questionnaireDto, String questionnaireInfobean, PersonDTO personDTO) throws Exception {		
		String moduleItemId = getModuleItemId(personDTO, irbExemptForm);
		Integer questionnaireHeaderId =  saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,moduleItemId, personDTO,irbExemptForm);	
		irbExemptForm.setExemptQuestionnaireAnswerHeaderId(questionnaireHeaderId);
		ArrayList<HashMap<String, Object>> questionArrayList = new ArrayList<>();
		boolean isSubmit = isSubmit(questionnaireInfobean); 
		CommonVO commonVO = new CommonVO();
		boolean checkIsExempt = true;
		boolean checkOther = false;
		ArrayList<HashMap<String, Object>> nonExemptCategoryList = new ArrayList<>();
		ArrayList<HashMap<String, Object>> exemptCategoryList = new ArrayList<>();
		ArrayList<HashMap<String, Object>> otherCategoryList = new ArrayList<>();
		if(isQuestionnaireComplete(questionnaireInfobean) && isSubmit){
			irbExemptProtocolDao.irbExemptFormActionLog(irbExemptForm.getExemptFormID(), irbExemptForm.getActionTypesCode(), irbExemptForm.getComment(), irbExemptForm.getStatusCode(), irbExemptForm.getUpdateUser(),irbExemptForm.getNotificationNumber(),personDTO);
		}
		if(!isSubmit){
			irbExemptForm.setStatusCode("1");
		}
		savePersonExemptForm(irbExemptForm,"U");
		questionArrayList = getExemptMsg(irbExemptForm);	
		irbExemptForm.setStatusCode(irbExemptForm.getStatusCode());
		if(!questionArrayList.isEmpty()){
			for(HashMap<String, Object> answerDetails: questionArrayList){
				if(answerDetails.get("IS_EXEMPT_FLAG").equals("N")){
					checkIsExempt = false;
					nonExemptCategoryList.add(answerDetails);
				} else if(answerDetails.get("IS_EXEMPT_FLAG").equals("Y")){
					exemptCategoryList.add(answerDetails);
				}else if (answerDetails.get("IS_EXEMPT_FLAG").equals("O")){
					otherCategoryList.add(answerDetails);
					irbExemptForm.setIsExempt("O");
					checkOther= true;
					commonVO.setExemptQuestionList(otherCategoryList);
					break;
				}

			}
			if(!checkOther){
				if(checkIsExempt == true){
					irbExemptForm.setIsExempt("Y");
					commonVO.setExemptQuestionList(exemptCategoryList);
				} else{
					irbExemptForm.setIsExempt("N");
					commonVO.setExemptQuestionList(nonExemptCategoryList);
				}
			}
		}
		savePersonExemptForm(irbExemptForm,"U");
		String moduleItemsId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnairesDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemsId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID(),personDTO.getPersonID());
		if(irbViewProfile != null && irbViewProfile.getIrbExemptFormList() != null){
			irbExemptForm = irbViewProfile.getIrbExemptFormList().get(0);
		}
		commonVO.setQuestionnaireDto(questionnairesDto);
		commonVO.setIrbExemptForm(irbExemptForm);
		return commonVO;
	}
	
	private boolean isQuestionnaireComplete(String questionnaireInfobean){
		JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
		String questionnaireCompletionFlag = questionnaireJsnobject.get("QuestionnaireCompletionFlag").toString();
		if("Y".equalsIgnoreCase(questionnaireCompletionFlag)){
			return true;
		}
		return false;
	}
	
	private boolean isSubmit(String questionnaireInfobean) throws Exception {
		JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
		String questionnaireCompletionFlag = questionnaireJsnobject.get("isSubmit").toString();
		if("Y".equalsIgnoreCase(questionnaireCompletionFlag)){
			return true;
		}
		return false;
	}

	@Override
	public IRBProtocolVO updateExemptFundingSource(ExemptFundingSource exemptFundingSource, String updateUser) {
		IRBProtocolVO irbProtocolVO=irbExemptProtocolDao.updateExemptFundingSource(exemptFundingSource,updateUser);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO getExemptProtocolFundingSource(String exemptId) {
		IRBProtocolVO vo = new IRBProtocolVO();
	    try {	    	
			vo = irbExemptProtocolDao.getExemptProtocolFundingSource(exemptId);
		    vo.setProtocolFundingSourceTypes(irbExemptProtocolDao.loadProtocolFundingSourceTypes());
		    vo.setExemptFundingSource(new ExemptFundingSource());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return vo;
	}

	@Override
	public IRBProtocolVO loadHomeUnits(String homeUnitSearchString) {
		IRBProtocolVO vo = new IRBProtocolVO();
	    vo.setHomeUnits(irbExemptProtocolDao.loadhomeUnits(homeUnitSearchString));
		return vo; 
	}
}
