package org.mit.irb.web.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class IRBUtilController {
	
	@Autowired
	@Qualifier(value = "irbUtilService")
	IRBUtilService irbUtilService;
	
	protected static Logger logger = Logger.getLogger(IRBUtilController.class.getName());
	
	@RequestMapping(value = "/loadPersonTrainingList", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO loadPersonTrainingList(@RequestBody IRBUtilVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbUtilService.loadPersonTraining(vo);
		return vo;
	}
	
	@RequestMapping(value = "/updatePersonTraining", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO updatePersonTraining(@RequestBody IRBUtilVO vo,  HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbUtilService.updatePersonTraining(vo);
		return vo;
	}
	
	@RequestMapping(value = "/getPersonTrainingInfo", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO getMITKCPersonInfo(@RequestBody IRBUtilVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		Integer personTrainingId = vo.getPersonTrainingId();
		IRBUtilVO irbUtilVO = irbUtilService.getPersonTrainingInfo(personTrainingId);
		return irbUtilVO;
	}
	
	@RequestMapping(value = "/loadTrainingList", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO loadTrainingList(HttpServletRequest request,HttpServletResponse response)
	{	
		String trainingName = request.getParameter("trainingName");
		IRBUtilVO vo = irbUtilService.loadTrainingList(trainingName);
		return vo;
	}
	
	@RequestMapping(value = "/downloadFileData", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadments(HttpServletResponse response,
			@RequestHeader("fileDataId") String fileDataId) {
		return irbUtilService.downloadFileData(fileDataId);
	}
	
	@RequestMapping(value = "/addTrainingAttachments", method = RequestMethod.POST)
	public @ResponseBody ArrayList<HashMap<String, Object>> addTrainingAttachments(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson,HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException {
		logger.info("Requesting for addTrainingAttachments Modification");
		String fileDataId = request.getParameter("fileDataId");
		ArrayList<HashMap<String, Object>> result = null;
		result = irbUtilService.addTrainingAttachments(files, formDataJson,fileDataId);
		return result;
	}
	
	@RequestMapping(value = "/addTrainingComments", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO addTrainingComments(@RequestBody IRBUtilVO vo,HttpServletRequest request,HttpServletResponse response)
	{	
		vo = irbUtilService.addTrainingComments(vo.getPersonTrainingComments());
		return vo;
	}
	
	@RequestMapping(value = "/checkUserPermission", method = RequestMethod.POST)
	public @ResponseBody IRBPermissionVO checkUserPermission(@RequestBody IRBPermissionVO vo,HttpServletRequest request,HttpServletResponse response)
	{	
		vo = irbUtilService.checkUserPermission(vo);
		return vo;
	}
	
	@RequestMapping(value = "/checkProtocolLock", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO checkProtocolLock(@RequestBody IRBUtilVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbUtilService.checkLockPresent(vo);
		return vo;
	}
	
	@RequestMapping(value = "/releaseProtocolLock", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO releaseProtocolLock(@RequestBody IRBUtilVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbUtilService.releaseProtocolLock(vo); 
		return vo;
	}	
	
	@RequestMapping(value = "/loadProtocolLock", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO loadProtocolLock(@RequestBody IRBUtilVO vo, HttpServletRequest request,HttpServletResponse response)
	{
		vo = irbUtilService.loadProtocolLock(vo); 
		return vo;
	}	

}
