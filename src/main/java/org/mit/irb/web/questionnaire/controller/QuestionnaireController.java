package org.mit.irb.web.questionnaire.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;
import org.mit.irb.web.questionnaire.dto.QuestionnaireModuleDataBus;
import org.mit.irb.web.questionnaire.service.QuestionnaireModuleService;

@Controller
public class QuestionnaireController {
	protected static Logger logger = Logger.getLogger(QuestionnaireController.class.getName());
	 
	@Autowired QuestionnaireModuleService questionnaireModuleService;	
	
	@RequestMapping(value = "/getApplicableQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> getApplicableQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;	
		if(questionnaireDataBus != null){			
			questionnaireDataBus = questionnaireModuleService.getApplicableQuestionnaire(questionnaireDataBus);			
		}
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> getQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;	
		if(questionnaireDataBus != null){			
			questionnaireDataBus = questionnaireModuleService.getQuestionnaireDetails(questionnaireDataBus);			
		}
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/saveQuestionnaireModule", method = RequestMethod.POST)
	public ResponseEntity<String> saveQuestionnaire(MultipartHttpServletRequest request, HttpServletResponse reponse) throws Exception {
		logger.info("Requesting for saveQuestionnaire");
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		QuestionnaireModuleDataBus questionnaireDataBus = null;
		String formDataJson = request.getParameter("formDataJson");
		questionnaireDataBus = mapper.readValue(formDataJson, QuestionnaireModuleDataBus.class);
		questionnaireDataBus = questionnaireModuleService.saveQuestionnaireAnswers(questionnaireDataBus, request);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/showAllQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> showAllQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;		
		questionnaireDataBus = questionnaireModuleService.showAllQuestionnaire(questionnaireDataBus);	
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/createQuestionnaire", method = RequestMethod.GET)
	public ResponseEntity<String> modifyQuestionnaire(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;	
		QuestionnaireModuleDataBus questionnaireDataBus = new QuestionnaireModuleDataBus();
		questionnaireDataBus = questionnaireModuleService.createQuestionnaire(questionnaireDataBus);
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/configureQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> configureQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;		
		questionnaireDataBus = questionnaireModuleService.configureQuestionnaire(questionnaireDataBus);	
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/editQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> editQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;		
		questionnaireDataBus = questionnaireModuleService.editQuestionnaire(questionnaireDataBus);	
		String responseData = mapper.writeValueAsString(questionnaireDataBus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/downloadQuesAttachment", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadAttachment(HttpServletResponse response, @RequestBody QuestionnaireModuleDataBus questionnaireDataBus) {
		return questionnaireModuleService.downloadAttachments(questionnaireDataBus, response);
	}
}
