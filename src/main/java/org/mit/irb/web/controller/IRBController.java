package org.mit.irb.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.common.view.ServiceAttachments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public ResponseEntity<String> getIRBprotocolDetails(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
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
		logger.info("getIRBprotocolPersons---------------");
		String protocolNumber = vo.getProtocolNumber();
		logger.info("protocolNumber:---"+protocolNumber);
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
}
