package org.mit.irb.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.service.IRBExemptProtocolService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/*
 * Controller for getting IRB protocol details. 
*/

@Controller
public class IRBController {

	@Autowired
	@Qualifier(value = "irbProtocolService")
	IRBProtocolService irbProtocolService;

	@Autowired
	@Qualifier(value = "irbExemptProtocolService")
	IRBExemptProtocolService irbExemptProtocolService;

	@Autowired
	IRBProtocolInitLoadService irbProtocolInitLoadService;

	protected static Logger logger = Logger.getLogger(IRBController.class.getName());

	@RequestMapping(value = "/getIRBprotocolDetails", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolDetails(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolDetails(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolPersons", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolPersons(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolPersons(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolFundingSource", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolFundingSource(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolFundingSource(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolLocation", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolLocation(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolLocation(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolVulnerableSubject", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolVulnerableSubject(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolVulnerableSubject(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolSpecialReview", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolSpecialReview(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolSpecialReview(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getMITKCPersonInfo", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getMITKCPersonInfo(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String avPersonId = vo.getAvPersonId();
		IRBViewProfile irbViewProfile = irbProtocolService.getMITKCPersonInfo(avPersonId);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getAttachmentList", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getAttachments(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		String protocolnumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getAttachmentsList(protocolnumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/downloadAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadments(HttpServletResponse response,
			@RequestHeader("attachmentId") String attachmentId) {
		return irbProtocolService.downloadAttachments(attachmentId);
	}

	@RequestMapping(value = "/getProtocolHistotyGroupList", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getProtocolHistotyGroupList(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupList(vo.getProtocolNumber());
		return irbViewProfile;
	}

	@RequestMapping(value = "/getProtocolHistotyGroupDetails", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getProtocolHistotyGroupDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		IRBViewProfile irbViewProfile = irbProtocolService.getProtocolHistotyGroupDetails(vo.getProtocolId(),
				vo.getActionId(), vo.getNextGroupActionId(), vo.getPreviousGroupActionId());
		return irbViewProfile;
	}

	@RequestMapping(value = "/getPersonExemptFormList", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getPersonExemptFormList(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException, ParseException {
		IRBViewProfile irbViewProfile = irbExemptProtocolService.getPersonExemptFormList(vo);
		return irbViewProfile;
	}

	@RequestMapping(value = "/savePersonExemptForm", method = RequestMethod.POST)
	public @ResponseBody CommonVO savePersonExemptForms(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		CommonVO commonVO = irbExemptProtocolService.savePersonExemptForms(vo.getIrbExemptForm(), vo.getPersonDTO());
		return commonVO;
	}

	@RequestMapping(value = "/saveQuestionnaire", method = RequestMethod.POST)
	public @ResponseBody CommonVO saveQuestionnaire(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		PersonDTO personDTO = vo.getPersonDTO();
		CommonVO commonVo = irbExemptProtocolService.saveQuestionnaire(vo.getIrbExemptForm(), vo.getQuestionnaireDto(),
				vo.getQuestionnaireInfobean(), personDTO);
		return commonVo;
	}

	@RequestMapping(value = "/getPersonExemptForm", method = RequestMethod.POST)
	public @ResponseBody CommonVO getPersonExemptForm(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		CommonVO commonVO = irbExemptProtocolService.getPersonExemptForm(vo.getIrbExemptForm(), vo.getPersonDTO());
		return commonVO;
	}

	@RequestMapping(value = "/getEvaluateMessage", method = RequestMethod.POST)
	public @ResponseBody CommonVO getEvaluateMessage(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		CommonVO commonVO = irbExemptProtocolService.getEvaluateMessage(vo.getIrbExemptForm());
		return commonVO;
	}

	@RequestMapping(value = "/getLeadunitAutoCompleteList", method = RequestMethod.POST)
	public @ResponseBody ArrayList<HashMap<String, Object>> getLeadunitAutoCompleteList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ArrayList<HashMap<String, Object>> leadunitList = irbExemptProtocolService.getLeadunitAutoCompleteList();
		return leadunitList;
	}

	@RequestMapping(value = "/approveOrDisapproveExemptProtocols", method = RequestMethod.POST)
	public @ResponseBody CommonVO approveOrDisapproveExemptProtocols(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws Exception {
		CommonVO commonVO = irbExemptProtocolService.approveOrDisapproveExemptProtocols(vo);
		return commonVO;
	}

	@RequestMapping(value = "/getExemptProtocolActivityLogs", method = RequestMethod.POST)
	public @ResponseBody CommonVO getExemptProtocolActivityLogs(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws Exception {
		CommonVO commonVO = new CommonVO();
		commonVO = irbExemptProtocolService.getExemptProtocolActivityLogs(vo.getIrbExemptForm().getExemptFormID());
		return commonVO;
	}

	@RequestMapping(value = "/addExemptProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody ArrayList<HashMap<String, Object>> addExemptProtocolAttachments(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson) throws JsonProcessingException {
		logger.info("Requesting for exemptProtocolAttachments Modification");
		ArrayList<HashMap<String, Object>> result = null;
		result = irbExemptProtocolService.addExemptProtocolAttachments(files, formDataJson);
		return result;
	}

	@RequestMapping(value = "/downloadExemptProtocolAttachments", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadExemptProtocolAttachments(HttpServletResponse response,
			@RequestHeader("checkListId") String checkListId) {
		return irbExemptProtocolService.downloadExemptProtocolAttachments(checkListId);
	}

	@RequestMapping(value = "/getExemptProtocolAttachmentList", method = RequestMethod.POST)
	public @ResponseBody ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws Exception {
		ArrayList<HashMap<String, Object>> leadunitList = irbExemptProtocolService
				.getExemptProtocolAttachmentList(vo.getIrbExemptForm().getExemptFormID());
		return leadunitList;
	}

	@RequestMapping(value = "/createIRBProtocol", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO createIRBProtocol(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolService.modifyProtocolDetails(vo.getProtocolId(), irbProtocolVO);
		return irbProtocolVO;
	}

	@RequestMapping(value = "/updateProtocolGeneralInfo", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateProtocolGeneralInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateGeneralInfo(irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}

	@RequestMapping(value = "/updateProtocolPersonInfo", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateProtocolPersonInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateProtocolPersonInfo(irbProtocolVO.getPersonnelInfo(),
				irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}

	@RequestMapping(value = "/updateFundingSource", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateFundingSource(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateFundingSource(irbProtocolVO.getFundingSource());
		return protocolVO;
	}

	@RequestMapping(value = "/updateSubject", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateSubject(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateSubject(irbProtocolVO.getProtocolSubject());
		return protocolVO;
	}

	@RequestMapping(value = "/updateCollaborator", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateCollaborator(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateCollaborator(irbProtocolVO.getProtocolCollaborator());
		return protocolVO;
	}

	@RequestMapping(value = "/loadAttachmentType", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadAttachmentType(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolInitLoadService.loadAttachmentType();
		return protocolVO;
	}

	@RequestMapping(value = "/addProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO addProtocolAttachments(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson) throws JsonProcessingException {
		logger.info("Request for ProtocolAttachments Modification");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.addProtocolAttachments(files, formDataJson);
		return protocolVO;
	}

	@RequestMapping(value = "/loadIRBProtocolAttachmentsByProtocolNumber", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for loadIRBProtocolAttachmentsByProtocolNumber");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.loadIRBProtocolAttachmentsByProtocolNumber(irbProtocolVO.getProtocolNumber());
		return protocolVO;
	}

	@RequestMapping(value = "/saveScienceOfProtocol", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO saveScienceOfProtocol(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO protocolVO) throws JsonProcessingException {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolService.saveScienceOfProtocol(protocolVO.getScienceOfProtocol());
		return irbProtocolVO;
	}

	@RequestMapping(value = "/addCollaboratorAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO addCollaboratorAttachments(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson) throws JsonProcessingException {
		logger.info("Request for Protocol Collaborator Attachments Modification");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.addCollaboratorAttachments(files, formDataJson);
		return protocolVO;
	}

	@RequestMapping(value = "/addCollaboratorPersons", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO addCollaboratorPersons(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for addCollaboratorPersons");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.addCollaboratorPersons(irbProtocolVO.getProtocolCollaboratorPersons());
		return protocolVO;
	}

	@RequestMapping(value = "/loadCollaboratorPersonsAndAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadCollaboratorPersonsAndAttachments(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for loadCollaboratorPersonsAndAttachments");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.loadCollaboratorPersonsAndAttachments(irbProtocolVO.getCollaboratorId());
		return protocolVO;
	}

	@RequestMapping(value = "/loadSponsorTypes", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadSponsorTypes(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for loadSponsorTypes");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolInitLoadService.loadSponsorTypes(irbProtocolVO);
		return protocolVO;
	}

	@RequestMapping(value = "/loadCommitteeList", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadCommitteeList(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		logger.info("Request for loadCommitteeList");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolInitLoadService.loadCommitteeList();
		return protocolVO;
	}

	@RequestMapping(value = "/loadCommitteeScheduleList", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadCommitteeScheduleList(HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		logger.info("Request for loadCommitteeScheduleList");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolInitLoadService.loadCommitteeScheduleList();
		return protocolVO;
	}
}
