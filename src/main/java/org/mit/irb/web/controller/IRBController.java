package org.mit.irb.web.controller;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolDetails(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolPersons",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolPersons(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolPersons(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolFundingSource",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolFundingSource(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolFundingSource(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolLocation",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolLocation(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolLocation(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolVulnerableSubject",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolVulnerableSubject(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolVulnerableSubject(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getIRBprotocolSpecialReview",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolSpecialReview(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolSpecialReview(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getMITKCPersonInfo",method= RequestMethod.POST)
	public ResponseEntity<String> getMITKCPersonInfo(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String avPersonId = vo.getAvPersonId();
		IRBViewProfile irbViewProfile = irbProtocolService.getMITKCPersonInfo(avPersonId);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getAttachmentList",method= RequestMethod.POST)
	public ResponseEntity<String> getAttachments(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolnumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getAttachmentsList(protocolnumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/downloadAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadments(HttpServletResponse response, @RequestHeader("attachmentId") String attachmentId) {
		return irbProtocolService.downloadAttachments(attachmentId);
	}
	
	@RequestMapping(value = "/getProtocolHistotyGroupList", method = RequestMethod.POST)
	public ResponseEntity<String> getProtocolHistotyGroupList(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException{
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupList(vo.getProtocolNumber());
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getProtocolHistotyGroupDetails", method = RequestMethod.POST)
	public ResponseEntity<String> getProtocolHistotyGroupDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException{
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupDetails(vo.getProtocolId(), vo.getActionId(), vo.getNextGroupActionId(), vo.getPreviousGroupActionId());
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getPersonExemptFormList", method = RequestMethod.POST)
	public ResponseEntity<String> getPersonExemptFormList(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException, ParseException{
		IRBViewProfile irbViewProfile = irbProtocolService.getPersonExemptFormList(vo);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
	@RequestMapping(value = "/savePersonExemptForm", method = RequestMethod.POST)
	public ResponseEntity<String> savePersonExemptForms(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		HttpStatus status = HttpStatus.OK;
		CommonVO commonVO = irbProtocolService.savePersonExemptForms(vo.getIrbExemptForm(),vo.getPersonDTO());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/saveQuestionnaire", method = RequestMethod.POST)
	public ResponseEntity<String> saveQuestionnaire(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		PersonDTO personDTO = vo.getPersonDTO();
		CommonVO commonVo = irbProtocolService.saveQuestionnaire(vo.getIrbExemptForm(), vo.getQuestionnaireDto(),vo.getQuestionnaireInfobean(), personDTO);
		String responseData = mapper.writeValueAsString(commonVo);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getPersonExemptForm", method = RequestMethod.POST)
	public ResponseEntity<String> getPersonExemptForm(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;		 
		CommonVO commonVO = irbProtocolService.getPersonExemptForm(vo.getIrbExemptForm(), vo.getPersonDTO());
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getEvaluateMessage", method = RequestMethod.POST)
	public ResponseEntity<String> getEvaluateMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;		 
		CommonVO commonVO = irbProtocolService.getEvaluateMessage(vo.getIrbExemptForm());
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getLeadunitAutoCompleteList", method = RequestMethod.POST)
	public ResponseEntity<String> getLeadunitAutoCompleteList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		ArrayList<HashMap<String, Object>> leadunitList = irbProtocolService.getLeadunitAutoCompleteList();
		String responseData = mapper.writeValueAsString(leadunitList);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/approveOrDisapproveExemptProtocols", method = RequestMethod.POST)
	public ResponseEntity<String> approveOrDisapproveExemptProtocols(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		CommonVO commonVO = irbProtocolService.approveOrDisapproveExemptProtocols(vo);
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getExemptProtocolActivityLogs", method = RequestMethod.POST)
	public ResponseEntity<String> getExemptProtocolActivityLogs(HttpServletRequest request, HttpServletResponse response,@RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		CommonVO commonVO = new CommonVO();
		commonVO = irbProtocolService.getExemptProtocolActivityLogs(vo.getIrbExemptForm().getExemptFormID());
		String responseData = mapper.writeValueAsString(commonVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/addExemptProtocolAttachments", method = RequestMethod.POST)
	public ResponseEntity<String> addExemptProtocolAttachments(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) throws JsonProcessingException {
		logger.info("Requesting for exemptProtocolAttachments Modification");
		ArrayList<HashMap<String, Object>> result = null;
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status =HttpStatus.OK;
		result = irbProtocolService.addExemptProtocolAttachments(files, formDataJson);
		String responseData = mapper.writeValueAsString(result);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/downloadExemptProtocolAttachments", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadExemptProtocolAttachments(HttpServletResponse response, @RequestHeader("checkListId") String checkListId) {
		return irbProtocolService.downloadExemptProtocolAttachments(checkListId);
	}
	
	@RequestMapping(value = "/getExemptProtocolAttachmentList", method = RequestMethod.POST)
	public ResponseEntity<String> getExemptProtocolAttachmentList(HttpServletRequest request, HttpServletResponse response,@RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		ArrayList<HashMap<String, Object>> leadunitList = irbProtocolService.getExemptProtocolAttachmentList(vo.getIrbExemptForm().getExemptFormID());
		String responseData = mapper.writeValueAsString(leadunitList);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/createIRBProtocol", method = RequestMethod.POST)
	public ResponseEntity<String> createIRBProtocol(HttpServletRequest request, HttpServletResponse response,@RequestBody CommonVO vo) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolService.modifyProtocolDetails(vo.getProtocolId(),irbProtocolVO);
		String responseData = mapper.writeValueAsString(irbProtocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/updateProtocolGeneralInfo", method = RequestMethod.POST)
	public ResponseEntity<String> updateProtocolGeneralInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody IRBProtocolVO irbProtocolVO) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateGeneralInfo(irbProtocolVO.getGeneralInfo());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/updateProtocolPersonInfo", method = RequestMethod.POST)
	public ResponseEntity<String> updateProtocolPersonInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody IRBProtocolVO irbProtocolVO) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateProtocolPersonInfo(irbProtocolVO.getPersonnelInfo());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/updateFundingSource", method = RequestMethod.POST)
	public ResponseEntity<String> updateFundingSource(HttpServletRequest request, HttpServletResponse response,@RequestBody IRBProtocolVO irbProtocolVO) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateFundingSource(irbProtocolVO.getFundingSource());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/updateSubject", method = RequestMethod.POST)
	public ResponseEntity<String> updateSubject(HttpServletRequest request, HttpServletResponse response,@RequestBody IRBProtocolVO irbProtocolVO) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateSubject(irbProtocolVO.getProtocolSubject());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/updateCollaborator", method = RequestMethod.POST)
	public ResponseEntity<String> updateCollaborator(HttpServletRequest request, HttpServletResponse response,@RequestBody IRBProtocolVO irbProtocolVO) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateCollaborator(irbProtocolVO.getProtocolCollaborator());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/loadAttachmentType", method = RequestMethod.POST)
	public ResponseEntity<String> loadAttachmentType(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
		HttpStatus status = HttpStatus.OK;
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.loadAttachmentType();
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/addProtocolAttachments", method = RequestMethod.POST)
	public ResponseEntity<String> addProtocolAttachments(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) throws JsonProcessingException {
		logger.info("Request for ProtocolAttachments Modification");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status =HttpStatus.OK;
		protocolVO = irbProtocolService.addProtocolAttachments(files, formDataJson);
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/loadIRBProtocolAttachmentsByProtocolNumber", method = RequestMethod.POST)
	public ResponseEntity<String> loadIRBProtocolAttachmentsByProtocolNumber(HttpServletRequest request, HttpServletResponse response,  @RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for loadIRBProtocolAttachmentsByProtocolNumber");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		ObjectMapper mapper = new ObjectMapper();
		HttpStatus status =HttpStatus.OK;
		protocolVO = irbProtocolService.loadIRBProtocolAttachmentsByProtocolNumber(irbProtocolVO.getProtocolNumber());
		String responseData = mapper.writeValueAsString(protocolVO);
		return new ResponseEntity<String>(responseData, status);
	}	
}
