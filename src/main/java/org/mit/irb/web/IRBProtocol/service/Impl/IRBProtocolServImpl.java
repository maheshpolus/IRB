package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolSubject;
import org.mit.irb.web.IRBProtocol.pojo.ScienceOfProtocol;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolInitLoadService;
import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.dto.QuestionnaireDto;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	@Qualifier(value="questionnaireService")
	QuestionnaireService questionnaireService;

	
	@Autowired
	@Qualifier(value="irbProtocolService")
	IRBProtocolService irbProtocolService;
	
	@Autowired
	IRBProtocolInitLoadService initLoadService;
	
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
	public IRBProtocolVO updateGeneralInfo(ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = irbProtocolDao.updateGeneralInfo(generalInfo);
		return irbProtocolVO;
	}

	

	@Override
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateProtocolPersonInfo(personnelInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateFundingSource(fundingSource);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateSubject(ProtocolSubject protocolSubject) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateSubject(protocolSubject);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateCollaborator(protocolCollaborator);
		return irbProtocolVO;
	}


	@Override
	public IRBProtocolVO loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolDetails(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
		try {
			irbProtocolVO = irbProtocolDao.addProtocolAttachments(files,formDataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@SuppressWarnings("unused")
	@Override
	public IRBProtocolVO loadIRBProtocolAttachmentsByProtocolNumber(String protocolNumber) {
		IRBProtocolVO irbProtocolVO = null;
		return irbProtocolVO = irbProtocolDao.loadIRBProtocolAttachmentsByProtocolNumber(protocolNumber);
	}

	@Override
	public IRBProtocolVO modifyProtocolDetails(Integer protocolId,IRBProtocolVO irbProtocolVO) {
		try{
			irbProtocolVO.setProtocolId(protocolId);
			ProtocolPersonnelInfo personnelInfo = new ProtocolPersonnelInfo();
			ProtocolLeadUnits protocolLeadUnits = new ProtocolLeadUnits();
			ProtocolFundingSource fundingSource= new ProtocolFundingSource();
		    ProtocolSubject protocolSubject= new ProtocolSubject();
		    ProtocolCollaborator protocolCollaborator = new ProtocolCollaborator();
			irbProtocolVO = initLoadService.loadProtocolTypes(irbProtocolVO);
			//irbProtocolVO = initLoadService.loadSponsorTypes(irbProtocolVO);
			irbProtocolVO = initLoadService.loadRoleTypes(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolPersonLeadunits(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolAffiliationTypes(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolSubjectTypes(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolAgeGroups(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolFundingSourceTypes(irbProtocolVO);
			irbProtocolVO = initLoadService.loadProtocolCollaboratorNames(irbProtocolVO);
			irbProtocolVO = irbProtocolService.loadProtocolDetails(irbProtocolVO);
			irbProtocolVO.setPersonnelInfo(personnelInfo);
			irbProtocolVO.setProtocolLeadUnits(protocolLeadUnits);
			irbProtocolVO.setFundingSource(fundingSource);
			irbProtocolVO.setProtocolSubject(protocolSubject);
			irbProtocolVO.setProtocolCollaborator(protocolCollaborator);
		} catch (Exception e) {
			logger.error("Error in modifyProtocolDetails method : "+e.getMessage());
		}
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO saveScienceOfProtocol(ScienceOfProtocol scienceOfProtocol) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.saveScienceOfProtocol(irbProtocolVO,scienceOfProtocol);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
		try {
			irbProtocolVO = irbProtocolDao.addCollaboratorAttachments(files,formDataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.addCollaboratorPersons(protocolCollaboratorPersons);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.loadCollaboratorPersonsAndAttachments(collaboratorId);
		return irbProtocolVO;
	}

}
