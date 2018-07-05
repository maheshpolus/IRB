package org.mit.irb.web.IRBProtocol.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
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
import org.springframework.stereotype.Service;

@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
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
	public IRBViewProfile getPersonExemptFormList(PersonDTO personDTO) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptFormList(personDTO);
		return irbViewProfile;
	}

	@Override
	public CommonVO savePersonExemptForms(IRBExemptForm irbExemptForm, PersonDTO dto) throws Exception {
		irbExemptForm.setStatusCode("1");
		Integer exemptId = irbProtocolDao.getNextExemptId();
		irbExemptForm.setExemptFormID(exemptId); 
		savePersonExemptForm(irbExemptForm,"I");
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(exemptId);
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
		if(isQuestionnaireComplete(questionnaireInfobean)){
			savePersonExemptForm(irbExemptForm,"U");
			questionArrayList = getExemptMsg(irbExemptForm);	
			irbExemptForm.setStatusCode("2");
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
		} 
		if(!isSubmit){
			irbExemptForm.setStatusCode("1");
		}
		savePersonExemptForm(irbExemptForm,"U");
		CommonVO commonVO = new CommonVO();
		String moduleItemsId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnairesDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemsId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID());
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
	
	private void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype){
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
	public CommonVO getPersonExemptForm(IRBExemptForm irbExemptForm) throws Exception {
		CommonVO commonVO = new CommonVO();
		String moduleItemId = getModuleItemId(irbExemptForm.getPersonId(), irbExemptForm);
		QuestionnaireDto questionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, moduleItemId,irbExemptForm.getExemptQuestionnaireAnswerHeaderId());
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptForm(irbExemptForm.getExemptFormID());
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
}
