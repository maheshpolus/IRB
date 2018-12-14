package org.mit.irb.web.IRBProtocol.service.Impl;
import java.util.List;
import java.util.concurrent.Future;

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
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
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
	public ResponseEntity<byte[]> loadProtocolHistoryCorrespondanceLetter(Integer protocolActionId) {
		return irbProtocolDao.loadProtocolHistoryCorrespondanceLetter(protocolActionId);
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
	public IRBProtocolVO updateProtocolPersonInfo(ProtocolPersonnelInfo personnelInfo,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateProtocolPersonInfo(personnelInfo,generalInfo);
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
		    Future<IRBProtocolVO> protocolTypes = initLoadService.loadProtocolTypes(irbProtocolVO);
		    Future<IRBProtocolVO> roleTypes = initLoadService.loadRoleTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolPersonLeadunits = initLoadService.loadProtocolPersonLeadunits(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAffiliationTypes = initLoadService.loadProtocolAffiliationTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolSubjectTypes = initLoadService.loadProtocolSubjectTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAgeGroups = initLoadService.loadProtocolAgeGroups(irbProtocolVO);
		    Future<IRBProtocolVO> protocolFundingSourceTypes = initLoadService.loadProtocolFundingSourceTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolCollaboratorNames = initLoadService.loadProtocolCollaboratorNames(irbProtocolVO);
		    irbProtocolVO = protocolTypes.get();
			irbProtocolVO = roleTypes.get();
			irbProtocolVO = protocolPersonLeadunits.get();
			irbProtocolVO = protocolAffiliationTypes.get();
			irbProtocolVO = protocolSubjectTypes.get();
			irbProtocolVO = protocolAgeGroups.get();
			irbProtocolVO = protocolFundingSourceTypes.get();
			irbProtocolVO = protocolCollaboratorNames.get();
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

	@Override
	public IRBViewProfile loadProtocolHistoryActionComments(String protocolNumber,Integer protocolActionId, String protocolActionTypecode) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		irbViewProfile = irbProtocolDao.loadProtocolHistoryActionComments(protocolNumber,protocolActionId,protocolActionTypecode);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile checkingPersonsRightToViewProtocol(String personId, String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		irbViewProfile = irbProtocolDao.checkingPersonsRightToViewProtocol(personId, protocolNumber);
		return irbViewProfile;
	}
}
