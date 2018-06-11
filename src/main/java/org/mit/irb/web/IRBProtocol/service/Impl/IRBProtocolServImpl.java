package org.mit.irb.web.IRBProtocol.service.Impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.ServiceAttachments;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.mit.irb.web.questionnaire.service.Impl.QuestionnaireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	QuestionnaireService questionnaireService;

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
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId,
			Integer nextGroupActionId, Integer previousGroupActionId) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getProtocolHistotyGroupDetails(protocolId, actionId,
				nextGroupActionId, previousGroupActionId);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getPersonExemptFormList(PersonDTO personDTO) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getPersonExemptFormList(personDTO);
		return irbViewProfile;
	}
//next
	@Override
	public QuestionnaireDto savePersonExemptForms(IRBExemptForm irbExemptForm) throws Exception {
		savePersonExemptForm(irbExemptForm,"I");
		//QuestionnaireService questionnaireService = new QuestionnaireServiceImpl();		
		QuestionnaireDto QuestionnaireDto =  questionnaireService.getQuestionnaireDetails(KeyConstants.COEUS_MODULE_PERSON, irbExemptForm.getPersonId());
		return QuestionnaireDto;
	}

	@Override
	public String saveQuestionnaire(IRBExemptForm irbExemptForm,QuestionnaireDto questionnaireDto, String questionnaireInfobean,
			PersonDTO personDTO) throws Exception {			
		saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,personDTO);
		savePersonExemptForm(irbExemptForm,"U");
		boolean isSubmit = isSubmit(questionnaireInfobean); 
		if(isQuestionnaireComplete(questionnaireInfobean) && !isSubmit){
			return getExemptMsg(questionnaireInfobean);
		}
		return "success";
	}

	private void saveQuestionnaireAnswers(QuestionnaireDto questionnaireDto, String questionnaireInfobean,
			PersonDTO personDTO) throws Exception {
		//QuestionnaireService questionnaireService = new QuestionnaireServiceImpl();			
		questionnaireService.saveQuestionnaireAnswers(questionnaireDto,questionnaireInfobean,KeyConstants.COEUS_MODULE_PERSON,personDTO.getPersonID(), personDTO);
	}
	
	private boolean isSubmit(String questionnaireInfobean) throws Exception {
		//TODO: Write the logic of submit
		return false;
	}		
	
	private String getExemptMsg(String questionnaireInfobean) {
		// TODO Auto-generated method stub
		return "success";
	}
	
	private void savePersonExemptForm(IRBExemptForm irbExemptForm, String actype){
		irbProtocolDao.savePersonExemptForm(irbExemptForm,actype);
	}

	private boolean isQuestionnaireComplete(String questionnaireInfobean){
		JSONObject questionnaireJsnobject = new JSONObject(questionnaireInfobean);
		JSONArray questionnaireJsnArray = questionnaireJsnobject.getJSONArray("answerlist");
		String questionnaireCompletionFlag = questionnaireJsnobject.get("QuestionnaireCompletionFlag").toString();
		if("Y".equalsIgnoreCase(questionnaireCompletionFlag)){
			return true;
		}
		return false;
	}
	
}
