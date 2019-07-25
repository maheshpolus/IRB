package org.mit.irb.web.IRBProtocol.service.Impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.IRBAttachmentProtocol;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolCorrespondence;
import org.mit.irb.web.IRBProtocol.pojo.IRBProtocolPersonRoles;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContact;
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
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Service(value = "irbProtocolService")
public class IRBProtocolServImpl implements IRBProtocolService {

	@Autowired
	IRBProtocolDao irbProtocolDao;
	
	@Autowired
	IRBUtilService irbUtilService;
	
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
	IRBViewProfile irbViewProfile = new IRBViewProfile();
    HashMap<String, Object> irbProtocolDetail = irbProtocolDao.getIRBProtocolDetail(protocolNumber);
    irbViewProfile.setIrbViewHeader(irbProtocolDetail);
    //IRBViewProfile irbViewProfile = irbProtocolDao.getIRBProtocolDetails(protocolNumber);
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
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadProtocolAttachments(attachmentId);
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
		ArrayList<HashMap<String, Object>> result = irbProtocolDao.getProtocolHistotyGroupDetails(protocolNumber);
		if(irbViewProfile.getIrbViewProtocolHistoryGroupList() != null){
			for(HashMap<String, Object> s1 : irbViewProfile.getIrbViewProtocolHistoryGroupList()){
				Integer nextGroupActionId = Integer.parseInt(s1.get("NEXT_GROUP_ACTION_ID").toString());  
				Integer actionId = Integer.parseInt(s1.get("ACTION_ID").toString());
				Integer protocolId = Integer.parseInt(s1.get("PROTOCOL_ID").toString());	
				String groupListProtocolNumber = s1.get("PROTOCOL_NUMBER").toString();	
				nextGroupActionId = irbProtocolDao.getNextGroupActionId(protocolId,nextGroupActionId,actionId,groupListProtocolNumber);				
				List<HashMap<String, Object>> s2List = new ArrayList<HashMap<String,Object>>();
				for(HashMap<String, Object> detailObject :result){
					Integer detailactionId = Integer.parseInt(detailObject.get("ACTION_ID").toString()); 
					String detailProtocolNumber = detailObject.get("PROTOCOL_NUMBER").toString();				
					if(detailactionId >= actionId && detailactionId <= nextGroupActionId && detailProtocolNumber.equals(groupListProtocolNumber)){	
						s2List.add(detailObject);
					/*	if(detailProtocolId.equals(protocolId)){						
						s2List.add(detailObject);
					  }*/
					}
				}
				HashMap<String, Object> groupDetailList = new HashMap<String, Object>();	
				groupDetailList.put("GROUP_DETAILS_LIST", s2List);
				s1.putAll(groupDetailList);
			}
		}	
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getProtocolHistotyGroupDetails(Integer protocolId, Integer actionId, Integer nextGroupActionId, Integer previousGroupActionId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		//irbViewProfile = irbProtocolDao.getProtocolHistotyGroupDetails(protocolId,irbViewProfile);
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
	public IRBProtocolVO updateFundingSource(ProtocolFundingSource fundingSource,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateFundingSource(fundingSource,generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateSubject(ProtocolSubject protocolSubject,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateSubject(protocolSubject,generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateCollaborator(ProtocolCollaborator protocolCollaborator,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateCollaborator(protocolCollaborator,generalInfo);
		return irbProtocolVO;
	}


	@Override
	public Future<IRBProtocolVO> loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolDetails(irbProtocolVO);
		return new AsyncResult<> (irbProtocolVO);
	}

	@Override
	public IRBProtocolVO addProtocolAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();;
		try {
			ObjectMapper mapper = new ObjectMapper();
			IRBAttachmentProtocol attachmentProtocol = mapper.readValue(formDataJson, IRBAttachmentProtocol.class);
			if(attachmentProtocol.getAcType().equals("I")){
				irbProtocolVO = irbProtocolDao.addNewProtocolAttachment(files,attachmentProtocol);
			}
			else if(attachmentProtocol.getAcType().equals("U")){
				irbProtocolVO = irbProtocolDao.editProtocolAttachment(files,attachmentProtocol);
			}
			else if(attachmentProtocol.getAcType().equals("D")){
				irbProtocolVO = irbProtocolDao.deleteProtocolAttachment(attachmentProtocol);
			}
			else if(attachmentProtocol.getAcType().equals("R")){
				irbProtocolVO = irbProtocolDao.replaceProtocolAttachment(files,attachmentProtocol);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}

	@SuppressWarnings("unused")
	@Override
	public IRBProtocolVO loadIRBProtocolAttachments(Integer protocolId, String protocolNumber) {
		IRBProtocolVO irbProtocolVO = null;
		return irbProtocolVO = irbProtocolDao.loadIRBProtocolAttachments(protocolId,protocolNumber);
	}

	@Override
	public IRBProtocolVO modifyProtocolDetails(String protocolNumber, Integer protocolId,IRBProtocolVO irbProtocolVO) {
		try{
			irbProtocolVO.setProtocolId(protocolId);
			irbProtocolVO.setProtocolNumber(protocolNumber);
			ProtocolPersonnelInfo personnelInfo = new ProtocolPersonnelInfo();
			ProtocolLeadUnits protocolLeadUnits = new ProtocolLeadUnits();
			ProtocolFundingSource fundingSource= new ProtocolFundingSource();
		    ProtocolSubject protocolSubject= new ProtocolSubject();
		    ProtocolCollaborator protocolCollaborator = new ProtocolCollaborator();
		    ProtocolAdminContact protocolAdminContact = new ProtocolAdminContact();
		    Future<IRBProtocolVO> protocolTypes = initLoadService.loadProtocolTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolUnitTypes = initLoadService.loadProtocolUnitTypes(irbProtocolVO);
		    Future<IRBProtocolVO> roleTypes = initLoadService.loadRoleTypes(irbProtocolVO);
		    Future<IRBProtocolVO> riskLevelTypes = initLoadService.loadRiskLevelTypes(irbProtocolVO);
		    Future<IRBProtocolVO> fdaRiskLevelTypes = initLoadService.loadFDARiskLevelTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolPersonLeadunits = initLoadService.loadProtocolPersonLeadunits(irbProtocolVO);
		    Future<IRBProtocolVO> protocolSubjectTypes = initLoadService.loadProtocolSubjectTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAgeGroups = initLoadService.loadProtocolAgeGroups(irbProtocolVO);
 		    Future<IRBProtocolVO> protocolFundingSourceTypes = initLoadService.loadProtocolFundingSourceTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAdminContactTypes = initLoadService.loadProtocolAdminContactType(irbProtocolVO);
		    Future<IRBProtocolVO> protocolDetailsVo = irbProtocolService.loadProtocolDetails(irbProtocolVO);
		    irbUtilService.createLock(irbProtocolVO);
		    irbProtocolVO = riskLevelTypes.get();
		    irbProtocolVO = fdaRiskLevelTypes.get();
		    irbProtocolVO = protocolTypes.get();
		    irbProtocolVO = protocolUnitTypes.get();	    
			irbProtocolVO = roleTypes.get();
			irbProtocolVO = protocolPersonLeadunits.get();
			irbProtocolVO = protocolSubjectTypes.get();
			irbProtocolVO = protocolAgeGroups.get();
			irbProtocolVO = protocolFundingSourceTypes.get();
			irbProtocolVO = protocolAdminContactTypes.get();
			irbProtocolVO = protocolDetailsVo.get();
			irbProtocolVO.setPersonnelInfo(personnelInfo);
			irbProtocolVO.setProtocolLeadUnits(protocolLeadUnits);
			irbProtocolVO.setFundingSource(fundingSource);
			irbProtocolVO.setProtocolSubject(protocolSubject);
			irbProtocolVO.setProtocolCollaborator(protocolCollaborator);
			irbProtocolVO.setProtocolAdminContact(protocolAdminContact);
			if(irbProtocolVO.getProtocolId() == null){
				irbProtocolVO.getGeneralInfo().setIrbProtocolPersonRoles(new IRBProtocolPersonRoles());
			}
		} catch (Exception e) {
			logger.error("Error in modifyProtocolDetails method : "+e.getMessage());
		}
		return irbProtocolVO;
	}
	
	@Override
	public IRBProtocolVO saveScienceOfProtocol(ScienceOfProtocol scienceOfProtocol,ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.saveScienceOfProtocol(irbProtocolVO,scienceOfProtocol,generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = null;
	/*	try {
			irbProtocolVO = irbProtocolDao.addCollaboratorAttachments(files,formDataJson);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO addCollaboratorPersons(List<ProtocolCollaboratorPersons> protocolCollaboratorPersons) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.addCollaboratorPersons(protocolCollaboratorPersons);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadCollaboratorPersonsAndAttachments(Integer collaboratorId, Integer protocolId) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		irbProtocolVO = irbProtocolDao.loadCollaboratorPersonsAndAttachments(collaboratorId,protocolId);
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

	@Override
	public IRBProtocolVO updateUnitDetails(ProtocolLeadUnits protocolUnit, ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = null;
		irbProtocolVO = irbProtocolDao.updateUnitDetails(protocolUnit,generalInfo);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO updateAdminContact(ProtocolAdminContact protocolAdminContact,
			ProtocolGeneralInfo generalInfo) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();
		try{
			irbProtocolVO.setProtocolNumber(protocolAdminContact.getProtocolNumber());
			if (protocolAdminContact.getAcType().equals("U")) {
				irbProtocolDao.modifyAdminContactDetail(protocolAdminContact,generalInfo);
			}else if (protocolAdminContact.getAcType().equals("D")) {
				irbProtocolDao.deleteAdminContactDetail(protocolAdminContact,generalInfo);
			}
			irbProtocolVO.setProtocolId(generalInfo.getProtocolId());
			Future<IRBProtocolVO> protocolAdminContacts = irbProtocolDao.getProtocolAdminContacts(irbProtocolVO);
			irbProtocolVO = protocolAdminContacts.get();	
		}catch (Exception e) {
			logger.error("Error in updateAdminContact method : "+e.getMessage());
		}
		return irbProtocolVO;
	}

	@Override
	public IRBViewProfile getIRBprotocolUnits(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolUnits(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolAdminContact(String protocolNumber) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getIRBprotocolAdminContact(protocolNumber);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolCollaboratorDetails(Integer protocolCollaboratorId) {
		//"P" for get Collaborator person list and "A" for collaborator attachment list
		IRBViewProfile irbViewProfile =new IRBViewProfile();
		ArrayList<HashMap<String, Object>> IRBprotocolCollaboratorPersons=irbProtocolDao.getIRBprotocolCollaboratorDetails(protocolCollaboratorId,"P");
		ArrayList<HashMap<String, Object>> IRBprotocolCollaboratorAttachment= irbProtocolDao.getIRBprotocolCollaboratorDetails(protocolCollaboratorId,"A");
		irbViewProfile.setIrbViewProtocolCollaboratorPersons(IRBprotocolCollaboratorPersons);
		irbViewProfile.setIrbViewProtocolCollaboratorAttachments(IRBprotocolCollaboratorAttachment);
		return irbViewProfile;
	}

	@Override
	public ResponseEntity<byte[]> downloadCollaboratorFileData(String fileDataId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadCollaboratorFileData(fileDataId);
		return attachments;
	}

	@Override
	public IRBViewProfile getUserTrainingRight(String person_Id) {
		IRBViewProfile irbViewProfile = irbProtocolDao.getUserTrainingRight(person_Id);
		return irbViewProfile;
	}

	@Override
	public IRBUtilVO getProtocolSubmissionDetails(String protocolNumber) {
		IRBUtilVO irbUtilVO = new IRBUtilVO();
		try{
			 Future<IRBUtilVO> submissionDetails = initLoadService.loadProtocolSubmissionDetail(protocolNumber, irbUtilVO);
			 Future<IRBUtilVO> submissionReviewer = initLoadService.loadProtocolSubmissionReviewer(protocolNumber, irbUtilVO);
			 Future<IRBUtilVO> renewalDetails = initLoadService.loadProtocolRenewalDetails(protocolNumber, "RENEWAL_AMEND", irbUtilVO);
			 Future<IRBUtilVO> renewalComments = initLoadService.loadProtocolRenewalDetails(protocolNumber, "SUMMARY", irbUtilVO);
			 Future<IRBUtilVO> reviewComments = initLoadService.loadProtocolReviewComments(protocolNumber, irbUtilVO);
			 Future<IRBUtilVO> submissionCheckList = initLoadService.loadSubmissionCheckList(protocolNumber, irbUtilVO);
			 irbUtilVO = submissionDetails.get();
			 irbUtilVO = submissionReviewer.get();
			 irbUtilVO = renewalDetails.get();
			 irbUtilVO = reviewComments.get();
			 irbUtilVO = renewalComments.get();
			 irbUtilVO = submissionCheckList.get();
		}catch (Exception e) {
			logger.error("Error in getProtocolSubmissionDetails method : "+e.getMessage());
		}
		return irbUtilVO;
	}

	@Override
	public IRBViewProfile loadCollaborators(String collaboratorSearchString) {
		IRBViewProfile irbViewProfile=new IRBViewProfile();
		irbViewProfile.setCollaboratorList(irbProtocolDao.loadCollaborators(collaboratorSearchString));
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile loadProtocolHistoryGroupComments(String protocolNumber, Integer protocolActionId,
			Integer protocolId, Integer nextGroupActionId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<HashMap<String, Object>> commentList = new ArrayList<HashMap<String,Object>>();
		commentList = irbProtocolDao.getHistoryGroupComment(protocolNumber,protocolActionId,protocolId,nextGroupActionId);
		if (commentList != null && !commentList.isEmpty()) {
			logger.info("Action comments exists");
			irbViewProfile.setIrbProtocolHistoryGroupComments(commentList);
		}
		return irbViewProfile;
	}
	
	@Override
	public IRBProtocolVO loadInternalProtocolAttachments(IRBProtocolVO irbProtocolVO ) {
		irbProtocolVO = irbProtocolDao.loadInternalProtocolAttachments(irbProtocolVO);
		return irbProtocolVO;
	}

	@Override
	public IRBProtocolVO loadPreviousProtocolAttachments(String documentId) {
		IRBProtocolVO irbProtocolVO = irbProtocolDao.loadPreviousProtocolAttachments(documentId);
		return irbProtocolVO;
	}

	@Override
	public ResponseEntity<byte[]>  downloadInternalProtocolAttachments(String documentId) {
		ResponseEntity<byte[]> attachments = irbProtocolDao.downloadInternalProtocolAttachments(documentId);
		return attachments;
	}

	@Override
	public IRBPermissionVO fetchProtocolPermissionDetails(IRBPermissionVO vo) {
		try{
			 Future<IRBPermissionVO> protocolpermission = initLoadService.loadProtocolPersonPermissions(vo);
			 vo = protocolpermission.get();
			 List<IRBProtocolPersonRoles> protocolRolePersonList = irbProtocolDao.loadProtocolPermissionPerson(vo);
			 vo.setProtocolRolePersonList(protocolRolePersonList);
			 vo.setProtocolRolePerson(new IRBProtocolPersonRoles());
		}catch (Exception e) {
			logger.error("Error in fetchProtocolPermissionLookup method : "+e.getMessage());
		}
		return vo;
	}

	@Override
	public IRBPermissionVO updateProtocolPermission(IRBPermissionVO irbPermissionVO) {
		try{
			irbProtocolDao.updateProtocolPermission(irbPermissionVO.getProtocolRolePerson());
			List<IRBProtocolPersonRoles> protocolRolePersonList = irbProtocolDao.loadProtocolPermissionPerson(irbPermissionVO);
			irbPermissionVO.setProtocolRolePersonList(protocolRolePersonList);
		}catch (Exception e) {
			logger.error("Error in updateProtocolPermission method : "+e.getMessage());
		}
		return irbPermissionVO;
	}

	@Override
	public IRBProtocolVO saveOrUpdateInternalProtocolAttachments(MultipartFile[] files, String formDataJson) {
		IRBProtocolVO irbProtocolVO = new IRBProtocolVO();;
		try {
			ObjectMapper mapper = new ObjectMapper();
			IRBProtocolCorrespondence internalProtocolAttachment = mapper.readValue(formDataJson, IRBProtocolCorrespondence.class);					
		    if(internalProtocolAttachment.getAcType().equals("D")){
				irbProtocolVO = irbProtocolDao.deleteInternalProtocolAttachment(internalProtocolAttachment);
			}else if(internalProtocolAttachment.getAcType() != null){
				irbProtocolVO = irbProtocolDao.saveOrUpdateInternalProtocolAttachments(files,internalProtocolAttachment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return irbProtocolVO;
	}
}
