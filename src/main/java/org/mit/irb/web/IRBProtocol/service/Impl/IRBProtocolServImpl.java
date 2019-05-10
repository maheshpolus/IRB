package org.mit.irb.web.IRBProtocol.service.Impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBActionsDao;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolAdminContact;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaborator;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolCollaboratorPersons;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolFundingSource;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolLeadUnits;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolPersonnelInfo;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolRenewalDetails;
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
import org.springframework.scheduling.annotation.AsyncResult;
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
	
	@Autowired
	IRBActionsDao irbAcionDao;
	
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
		ArrayList<HashMap<String, Object>> result = irbProtocolDao.getProtocolHistotyGroupDetails(protocolNumber);
		if(irbViewProfile.getIrbViewProtocolHistoryGroupList() != null){
			for(HashMap<String, Object> s1 : irbViewProfile.getIrbViewProtocolHistoryGroupList()){
				Integer nextGroupActionId = Integer.parseInt(s1.get("NEXT_GROUP_ACTION_ID").toString());  
				Integer actionId = Integer.parseInt(s1.get("ACTION_ID").toString());
				Integer protocolId = Integer.parseInt(s1.get("PROTOCOL_ID").toString());
				nextGroupActionId = irbProtocolDao.getNextGroupActionId(protocolId,nextGroupActionId);
				List<HashMap<String, Object>> s2List = new ArrayList<HashMap<String,Object>>();
				for(HashMap<String, Object> detailObject :result){
					Integer detailactionId = Integer.parseInt(detailObject.get("ACTION_ID").toString()); 
					if(detailactionId >= actionId && detailactionId <= nextGroupActionId){
						s2List.add(detailObject);
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
	public Future<IRBProtocolVO> loadProtocolDetails(IRBProtocolVO irbProtocolVO) {
		irbProtocolVO = irbProtocolDao.loadProtocolDetails(irbProtocolVO);
		return new AsyncResult<> (irbProtocolVO);
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
	public IRBProtocolVO modifyProtocolDetails(String protocolNumber, Integer protocolId,IRBProtocolVO irbProtocolVO) {
		try{
			irbProtocolVO.setProtocolId(protocolId);
			irbProtocolVO.setProtocolNumber(protocolNumber);
			ProtocolPersonnelInfo personnelInfo = new ProtocolPersonnelInfo();
			ProtocolLeadUnits protocolLeadUnits = new ProtocolLeadUnits();
			ProtocolFundingSource fundingSource= new ProtocolFundingSource();
		    ProtocolSubject protocolSubject= new ProtocolSubject();
		  if(protocolNumber.contains("A")){
			List<HashMap<String, Object>> amendRenewalModule = irbAcionDao.getAmendRenewalModules(protocolNumber);
			irbProtocolVO.setProtocolRenewalDetails(fetchAmendRenewalDetails(amendRenewalModule));
		  }
		    ProtocolCollaborator protocolCollaborator = new ProtocolCollaborator();
		    ProtocolAdminContact protocolAdminContact = new ProtocolAdminContact();
		    Future<IRBProtocolVO> protocolTypes = initLoadService.loadProtocolTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolUnitTypes = initLoadService.loadProtocolUnitTypes(irbProtocolVO);
		    Future<IRBProtocolVO> roleTypes = initLoadService.loadRoleTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolPersonLeadunits = initLoadService.loadProtocolPersonLeadunits(irbProtocolVO);
		    Future<IRBProtocolVO> protocolSubjectTypes = initLoadService.loadProtocolSubjectTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAgeGroups = initLoadService.loadProtocolAgeGroups(irbProtocolVO);
 		    Future<IRBProtocolVO> protocolFundingSourceTypes = initLoadService.loadProtocolFundingSourceTypes(irbProtocolVO);
		    Future<IRBProtocolVO> protocolCollaboratorNames = initLoadService.loadProtocolCollaboratorNames(irbProtocolVO);
		    Future<IRBProtocolVO> protocolAdminContactTypes = initLoadService.loadProtocolAdminContactType(irbProtocolVO);
		    Future<IRBProtocolVO> protocolDetailsVo = irbProtocolService.loadProtocolDetails(irbProtocolVO);
		    irbProtocolVO = protocolTypes.get();
		    irbProtocolVO = protocolUnitTypes.get();	    
			irbProtocolVO = roleTypes.get();
			irbProtocolVO = protocolPersonLeadunits.get();
			irbProtocolVO = protocolSubjectTypes.get();
			irbProtocolVO = protocolAgeGroups.get();
			irbProtocolVO = protocolFundingSourceTypes.get();
			irbProtocolVO = protocolCollaboratorNames.get();
			irbProtocolVO = protocolAdminContactTypes.get();
			irbProtocolVO = protocolDetailsVo.get();
			irbProtocolVO.setPersonnelInfo(personnelInfo);
			irbProtocolVO.setProtocolLeadUnits(protocolLeadUnits);
			irbProtocolVO.setFundingSource(fundingSource);
			irbProtocolVO.setProtocolSubject(protocolSubject);
			irbProtocolVO.setProtocolCollaborator(protocolCollaborator);
			irbProtocolVO.setProtocolAdminContact(protocolAdminContact);
		} catch (Exception e) {
			logger.error("Error in modifyProtocolDetails method : "+e.getMessage());
		}
		return irbProtocolVO;
	}
	
	
	
	private ProtocolRenewalDetails fetchAmendRenewalDetails(List<HashMap<String, Object>> amendRenewalModule) {
		ProtocolRenewalDetails protocolRenewalDetail = new ProtocolRenewalDetails();
		try{
			for(HashMap<String, Object> protocolDetailKey : amendRenewalModule){				
				   if(protocolDetailKey.get("IS_EDITABLE").equals("Y")){
					switch (protocolDetailKey.get("PROTOCOL_MODULE_CODE").toString()) {
					case "001":
						protocolRenewalDetail.setGeneralInfo(true);
						break;
					case "002":
						protocolRenewalDetail.setProtocolPersonel(true);
						break;
					case "003":
						protocolRenewalDetail.setKeyStudyPersonnel(true);
						break;
					case "008":
						protocolRenewalDetail.setAddModifyNoteAttachments(true);
						break;
					case "005":
						protocolRenewalDetail.setFundingSource(true);
						break;
					case "004":
						protocolRenewalDetail.setAreaOfResearch(true);
						break;
					case "009":
						protocolRenewalDetail.setProtocolCorrespondents(true);
						break;
					case "015":
						protocolRenewalDetail.setNotes(true);
						break;
					case "027":
						protocolRenewalDetail.setProtocolUnits(true);
						break;
					case "007":
						protocolRenewalDetail.setSpecialReview(true);
						break;
					case "028":
						protocolRenewalDetail.setPointOFContact(true);
						break;
					case "006":
						protocolRenewalDetail.setSubject(true);
						break;
					case "029":
						protocolRenewalDetail.setEngangedInstitution(true);
						break;
					case "Questionnaire":
						protocolRenewalDetail.setQuestionnaire(true);
						break;
					default:						
						break;
					}
				   }
				}
		}catch (Exception e) {
			logger.info("Exception in fetchAmendRenewalDetails:" + e);
		}
		return protocolRenewalDetail;
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
}
