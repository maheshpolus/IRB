package org.mit.irb.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBActionsVO;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubmissionStatuses;
import org.mit.irb.web.IRBProtocol.service.IRBExemptProtocolService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBViewProfile;
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
	IRBActionsDao irbAcionDao;
	
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
	public @ResponseBody IRBViewProfile getIRBprotocolFundingSource(@RequestBody CommonVO vo,
			HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
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
	public @ResponseBody IRBViewProfile getIRBprotocolVulnerableSubject(@RequestBody CommonVO vo,
			HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolVulnerableSubject(protocolNumber);
		return irbViewProfile;
	}

	@RequestMapping(value = "/getIRBprotocolSpecialReview", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolSpecialReview(@RequestBody CommonVO vo,
			HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
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
		CommonVO commonVo = irbExemptProtocolService.saveQuestionnaire(vo.getIrbExemptForm(), vo.getQuestionnaireDto(), vo.getQuestionnaireInfobean(), personDTO);
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
	public @ResponseBody CommonVO getExemptProtocolActivityLogs(HttpServletRequest request, HttpServletResponse response, @RequestBody CommonVO vo) throws Exception {
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

	@RequestMapping(value = "/loadProtocolHistoryCorrespondanceLetter", method = RequestMethod.GET)
	public ResponseEntity<byte[]> loadProtocolHistoryCorrespondanceLetter(HttpServletResponse response,
			@RequestHeader("protocolActionId") Integer protocolActionId) throws JsonProcessingException {
		logger.info("loadProtocolHistoryCorrespondanceLetter");
		ResponseEntity<byte[]> attachmentData = irbProtocolService.loadProtocolHistoryCorrespondanceLetter(protocolActionId);
		return attachmentData;
	}

	@RequestMapping(value = "/loadProtocolHistoryActionComments", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile loadProtocolHistoryCorrespondenceComments(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		logger.info("loadProtocolHistoryActionComments");
		return irbProtocolService.loadProtocolHistoryActionComments(vo.getProtocolNumber(), vo.getProtocolActionId(), vo.getProtocolActionTypecode());
	}
	
	@RequestMapping(value = "/checkingPersonsRightToViewProtocol", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile checkingPersonsRightToViewProtocol(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		logger.info("checkingPersonsRightToViewProtocol");
		return irbProtocolService.checkingPersonsRightToViewProtocol(vo.getPersonId(), vo.getProtocolNumber());
	}

	@RequestMapping(value = "/getExemptProtocolAttachmentList", method = RequestMethod.POST)
	public @ResponseBody ArrayList<HashMap<String, Object>> getExemptProtocolAttachmentList(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws Exception {
		ArrayList<HashMap<String, Object>> leadunitList = irbExemptProtocolService.getExemptProtocolAttachmentList(vo.getIrbExemptForm().getExemptFormID());
		return leadunitList;
	}

	@RequestMapping(value = "/createIRBProtocol", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO createIRBProtocol(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CommonVO vo) throws Exception {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolService.modifyProtocolDetails(vo.getProtocolNumber(),vo.getProtocolId(), irbProtocolVO);
		return irbProtocolVO;
	}

	@RequestMapping(value = "/updateProtocolGeneralInfo", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateProtocolGeneralInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		Boolean logInitialData = false;
		if(irbProtocolVO.getGeneralInfo().getProtocolId() == null){
			logInitialData = true;
		}
		protocolVO = irbProtocolService.updateGeneralInfo(irbProtocolVO.getGeneralInfo());
		if(logInitialData){
			IRBActionsVO vo = new IRBActionsVO();
			vo.setActionTypeCode("100");
			vo.setUpdateUser(protocolVO.getGeneralInfo().getUpdateUser());
			vo.setCreateUser(protocolVO.getGeneralInfo().getCreateUser());
			vo.setAcType("I");
			vo.setProtocolStatus(protocolVO.getGeneralInfo().getProtocolStatusCode());
			ProtocolSubmissionStatuses status = new ProtocolSubmissionStatuses();
			status.setProtocolNumber(protocolVO.getGeneralInfo().getProtocolNumber());
			status.setProtocolId(protocolVO.getGeneralInfo().getProtocolId());
			status.setSequenceNumber(protocolVO.getGeneralInfo().getSequenceNumber());
			status.setProtocolId(irbProtocolVO.getGeneralInfo().getProtocolId());
			vo.setProtocolSubmissionStatuses(status);
			irbAcionDao.updateActionStatus(vo);	
		}
		return protocolVO;
	}

	@RequestMapping(value = "/updateProtocolPersonInfo", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateProtocolPersonInfo(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateProtocolPersonInfo(irbProtocolVO.getPersonnelInfo(), irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}

	@RequestMapping(value = "/updateUnitDetails", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateUnitDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateUnitDetails(irbProtocolVO.getProtocolLeadUnits(), irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}
	
	@RequestMapping(value = "/updateFundingSource", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateFundingSource(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateFundingSource(irbProtocolVO.getFundingSource(),irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}

	@RequestMapping(value = "/updateSubject", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateSubject(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateSubject(irbProtocolVO.getProtocolSubject(),irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}

	@RequestMapping(value = "/updateCollaborator", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateCollaborator(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateCollaborator(irbProtocolVO.getProtocolCollaborator(),irbProtocolVO.getGeneralInfo());
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

	@RequestMapping(value = "/loadIRBProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws JsonProcessingException {
		logger.info("Request for loadIRBProtocolAttachments");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.loadIRBProtocolAttachments(irbProtocolVO.getProtocolId(),irbProtocolVO.getProtocolNumber());
		return protocolVO;
	}

	@RequestMapping(value = "/saveScienceOfProtocol", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO saveScienceOfProtocol(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO protocolVO) throws JsonProcessingException {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolService.saveScienceOfProtocol(protocolVO.getScienceOfProtocol(),protocolVO.getGeneralInfo());
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
		protocolVO = irbProtocolService.loadCollaboratorPersonsAndAttachments(irbProtocolVO.getCollaboratorId(),irbProtocolVO.getProtocolId());
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
		
	@RequestMapping(value = "/updateExemptFundingSource", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateExemptFundingSource(HttpServletRequest request, HttpServletResponse response,
			@RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		irbProtocolVO = irbExemptProtocolService.updateExemptFundingSource(irbProtocolVO.getExemptFundingSource(), irbProtocolVO.getUpdateUser());
		return irbProtocolVO; 
	}
	
	@RequestMapping(value = "/getExemptProtocolFundingSource", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO getExemptProtocolFundingSource(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("Request for getExemptProtocolFundingSource");
		IRBProtocolVO vo = new IRBProtocolVO();
		String exemptId = request.getParameter("exemptId");
		vo = irbExemptProtocolService.getExemptProtocolFundingSource(exemptId);
		return vo;
	}
	
	@RequestMapping(value = "/updateAdminContact", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO updateAdminContact(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) throws Exception {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.updateAdminContact(irbProtocolVO.getProtocolAdminContact(), irbProtocolVO.getGeneralInfo());
		return protocolVO;
	}
	
	@RequestMapping(value = "/getIRBprotocolUnits", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolUnits( @RequestBody CommonVO vo,HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{	
		String protocolNumber =vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBprotocolUnits(protocolNumber);
		return irbViewProfile;
	}
	
	@RequestMapping(value = "/getIRBprotocolAdminContact", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolAdminContact(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{	
		String protocolNumber = vo.getProtocolNumber();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBprotocolAdminContact(protocolNumber);
		return irbViewProfile;
	}
	
	@RequestMapping(value = "/getIRBprotocolCollaboratorDetails", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile getIRBprotocolCollaboratorDetails(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{	
		Integer protocolCollaboratorId = vo.getProtocolLocationId();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBprotocolCollaboratorDetails(protocolCollaboratorId);
		return irbViewProfile;
	}
	
	@RequestMapping(value = "/downloadCollaboratorFileData", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadCollaboratorFileData(HttpServletResponse response,
			@RequestHeader("fileDataId") String fileDataId) {
		return irbProtocolService.downloadCollaboratorFileData(fileDataId);
	}
	
	@RequestMapping(value = "/getUserTrainingRight", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile loadTrainingList(HttpServletRequest request,HttpServletResponse response)
	{	
		String person_Id = request.getParameter("person_Id");
		IRBViewProfile irbViewProfile = irbProtocolService.getUserTrainingRight(person_Id);
		return irbViewProfile;
	}
	
	@RequestMapping(value = "/getProtocolSubmissionDetails", method = RequestMethod.POST)
	public @ResponseBody IRBUtilVO getProtocolSubmissionDetails(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		IRBUtilVO irbUtilVO = irbProtocolService.getProtocolSubmissionDetails(vo.getProtocolNumber());
		return irbUtilVO;
	}
	
	@RequestMapping(value = "/loadCollaborators", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile loadCollaborators(HttpServletRequest request, HttpServletResponse response) {		
		String collaboratorSearchString = request.getParameter("collaboratorSearchString");
		IRBViewProfile irbViewProfile = irbProtocolService.loadCollaborators(collaboratorSearchString);
		return irbViewProfile;
	}
	
	@RequestMapping(value = "/loadProtocolHistoryGroupComments", method = RequestMethod.POST)
	public @ResponseBody IRBViewProfile loadProtocolHistoryGroupComments(HttpServletRequest request,
			HttpServletResponse response, @RequestBody CommonVO vo) throws JsonProcessingException {
		logger.info("loadProtocolHistoryActionComments");
		return irbProtocolService.loadProtocolHistoryGroupComments(vo.getProtocolNumber(), vo.getActionId(), vo.getProtocolId(), vo.getNextGroupActionId());
	}
	
	@RequestMapping(value = "/loadInternalProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadInternalProtocolAttachments(HttpServletRequest request,
			HttpServletResponse response, @RequestBody IRBProtocolVO irbProtocolVO) {
		logger.info("Request for loadInternalAttachments");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO.setProtocolId(irbProtocolVO.getProtocolId());
		protocolVO = irbProtocolService.loadInternalProtocolAttachments(protocolVO);
		return protocolVO;
	}
	
	@RequestMapping(value = "/loadPreviousProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO loadPreviousProtocolAttachments(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("Request for loadPreviousProtocolAttachments");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		String documentId = request.getParameter("documentId");
		protocolVO = irbProtocolService.loadPreviousProtocolAttachments(documentId);
		return protocolVO;
	}
	
	@RequestMapping(value = "/downloadInternalProtocolAttachments", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> downloadInternalProtocolAttachments(HttpServletResponse response,
			@RequestHeader("documentId") String documentId) {
		logger.info("Request for downloadInternalProtocolAttachments");
		return irbProtocolService.downloadInternalProtocolAttachments(documentId);
	}
	
	@RequestMapping(value = "/loadCollaboratorAttachmentType", method = RequestMethod.GET)
	public @ResponseBody IRBProtocolVO loadCollaboratorAttachmentType(HttpServletRequest request)
    {
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolInitLoadService.loadCollaboratorAttachmentType();
		return protocolVO;
	}
	
	@RequestMapping(value = "/getProtocolPermissionDetails", method = RequestMethod.POST)
	public @ResponseBody IRBPermissionVO fetchProtocolPermissionLookup(@RequestBody IRBPermissionVO vo,HttpServletRequest request,HttpServletResponse response)
	{	
		vo = irbProtocolService.fetchProtocolPermissionDetails(vo);
		return vo;
	}
	
	@RequestMapping(value = "/updateProtocolPermission", method = RequestMethod.POST)
	public @ResponseBody IRBPermissionVO updateProtocolPermission(@RequestBody IRBPermissionVO vo,HttpServletRequest request,HttpServletResponse response)
	{	
		vo = irbProtocolService.updateProtocolPermission(vo);
		return vo;
	}
	
	@RequestMapping(value = "/saveOrUpdateInternalProtocolAttachments", method = RequestMethod.POST)
	public @ResponseBody IRBProtocolVO saveOrUpdateInternalProtocolAttachments(
			@RequestParam(value = "files", required = false) MultipartFile[] files,
			@RequestParam("formDataJson") String formDataJson) {
		logger.info("Request for saveOrUpdateInternalProtocolAttachments Modification");
		IRBProtocolVO protocolVO = new IRBProtocolVO();
		protocolVO = irbProtocolService.saveOrUpdateInternalProtocolAttachments(files, formDataJson);
		return protocolVO;
	}
}
