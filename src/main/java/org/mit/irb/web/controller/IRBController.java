package org.mit.irb.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Controller for getting IRB protocol details. 
*/

@Controller
public class IRBController {

	@Autowired
	@Qualifier(value="irbProtocolService")
	IRBProtocolService irbProtocolService;
	
	protected static Logger logger = Logger.getLogger(IRBController.class.getName());
	
	@RequestMapping(value="/getIRBprotocolDetails",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolDetails(@RequestBody CommonVO vo, HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolDetails");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolDetails(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolPersons",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolPersons(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolPersons");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolPersons(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolFundingSource",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolFundingSource(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolFundingSource(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolLocation",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolLocation(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolLocation(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolVulnerableSubject",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolVulnerableSubject(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolVulnerableSubject(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolSpecialReview",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolSpecialReview(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:"+protocolNumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolSpecialReview(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getMITKCPersonInfo",method= RequestMethod.POST)
	public ResponseEntity<String> getMITKCPersonInfo(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String avPersonId = vo.getAvPersonId();
		logger.info("avPersonId:"+avPersonId);
		IRBViewProfile irbViewProfile = irbProtocolService.getMITKCPersonInfo(avPersonId);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getAttachmentList",method= RequestMethod.POST)
	public ResponseEntity<String> getAttachments(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		logger.info("getIRBprotocolFundingSource");
		String protocolnumber = vo.getProtocolNumber();
		logger.info("protocolnumber:"+protocolnumber);
		IRBViewProfile irbViewProfile = irbProtocolService.getAttachmentsList(protocolnumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/downloadAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadAttachments(HttpServletResponse response, @RequestHeader("attachmentId") String attachmentId) {
		logger.info("downloadAttachment");
		return irbProtocolService.downloadAttachments(attachmentId);
	}
	
	@RequestMapping(value = "/getProtocolHistotyGroupList", method = RequestMethod.POST)
	public ResponseEntity<String> getProtocolHistotyGroupList(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException{
		logger.info("getProtocolHistotyGroupList");
		logger.info("protocolnumber:"+vo.getProtocolNumber());
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupList(vo.getProtocolNumber());
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getProtocolHistotyGroupDetails", method = RequestMethod.POST)
	public ResponseEntity<String> getProtocolHistotyGroupDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException{
		logger.info("getProtocolHistotyGroupList");
		logger.info("protocolId:"+vo.getProtocolId());
	 	logger.info("ActionId:"+vo.getActionId());
	 	logger.info("NextGroupActionId:"+vo.getNextGroupActionId());
	 	logger.info("PreviousGroupActionId"+ vo.getPreviousGroupActionId());
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupDetails(vo.getProtocolId(), vo.getActionId(), vo.getNextGroupActionId(), vo.getPreviousGroupActionId());
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getPersonExemptFormList", method = RequestMethod.POST)
	public ResponseEntity<String> getPersonExemptFormList(HttpServletRequest request, HttpServletResponse response, @RequestBody PersonDTO personDTO) throws JsonProcessingException{
		logger.info("getPersonExemptFormList");
		logger.info("Person DTO:"+personDTO);
		IRBViewProfile irbViewProfile = irbProtocolService.getPersonExemptFormList(personDTO);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	@RequestMapping(value = "/savePersonExemptForm", method = RequestMethod.POST)
	public ResponseEntity<String> savePersonExemptForms(HttpServletRequest request, HttpServletResponse response, @RequestBody IRBExemptForm irbExemptForm) throws Exception{
		logger.info("savePersonExemptForm");
		logger.info("Exempt title: " + irbExemptForm.getExemptTitle());
		logger.info("Person Id: " + irbExemptForm.getPersonId());
		logger.info("Update User: " + irbExemptForm.getUpdateUser());
		HttpStatus status = HttpStatus.OK;
		CommonVO commonVO = irbProtocolService.savePersonExemptForms(irbExemptForm);
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/saveQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> saveQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		logger.info("saveQuestionnaire");
		ObjectMapper mapper=new ObjectMapper();
		logger.info("IRBExemptForm: " + vo.getIrbExemptForm());
		logger.info("QuestionnaireDto: " + vo.getQuestionnaireDto());
		logger.info("QuestionnaireInfobean: " + vo.getQuestionnaireInfobean());
		logger.info("PersonDTO: " + vo.getPersonDTO());
		HttpStatus status = HttpStatus.OK;
		PersonDTO personDTO = vo.getPersonDTO();
		String saveStatus = irbProtocolService.saveQuestionnaire(vo.getIrbExemptForm(), vo.getQuestionnaireDto(),vo.getQuestionnaireInfobean(), personDTO);
		String responseData = mapper.writeValueAsString(saveStatus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getPersonExemptForm", method = RequestMethod.POST)
	public ResponseEntity<String> getPersonExemptForm(HttpServletRequest request, HttpServletResponse response, @RequestBody IRBExemptForm irbExemptForm) throws Exception{
		logger.info("getPersonExemptForm");
		ObjectMapper mapper=new ObjectMapper();
		logger.info("IRBExemptForm: " + irbExemptForm);
		HttpStatus status = HttpStatus.OK;		
		CommonVO commonVO = irbProtocolService.getPersonExemptForm(irbExemptForm);
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
}
