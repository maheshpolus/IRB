package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "irbUtilService")
public class IRBUtilServiceImpl implements IRBUtilService {
	
	@Autowired
	IRBUtilDao irbUtilDao;
	
	protected static Logger logger = Logger.getLogger(IRBUtilServiceImpl.class.getName());

	@Override
	public IRBUtilVO loadPersonTraining(IRBUtilVO vo) {		
		vo = irbUtilDao.loadPersonTraining(vo);
		return vo;
	}

	@Override
	public IRBUtilVO updatePersonTraining(IRBUtilVO vo) {			
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date dateAcknowledged = null;
		java.sql.Date sqldateAcknowledged = null;
		java.util.Date  followUpDate = null;
		java.sql.Date sqlfollowUpDate = null;
		try{
			if (vo.getDateAcknowledged()!= null) {
				dateAcknowledged = sdf1.parse(vo.getDateAcknowledged());
				sqldateAcknowledged = new java.sql.Date(dateAcknowledged.getTime());				
			}
			if (vo.getFollowUpDate()!= null) {
				followUpDate = sdf1.parse(vo.getFollowUpDate());
				sqlfollowUpDate = new java.sql.Date(followUpDate.getTime());				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}		
		switch (vo.getPersonTraining().getAcType()) {
		case "I":
			vo.getPersonTraining().setPersonTrainingID(irbUtilDao.createPersonTrainingId());
			irbUtilDao.updatePersonTraining(vo.getPersonTraining(),sqldateAcknowledged,sqlfollowUpDate);
			ArrayList<HashMap<String, Object>> personTrainingListI = irbUtilDao.getPersonTraining(vo.getPersonTraining().getPersonTrainingID());
			HashMap<String, Object> personTrainingI=personTrainingListI.get(0);
			vo.setPersonnelTrainingInfo(personTrainingI);
			break;
		case "U":
			irbUtilDao.updatePersonTraining(vo.getPersonTraining(),sqldateAcknowledged,sqlfollowUpDate);
			ArrayList<HashMap<String, Object>> personTrainingListU = irbUtilDao.getPersonTraining(vo.getPersonTraining().getPersonTrainingID());
			HashMap<String, Object> personTrainingU=personTrainingListU.get(0);
			vo.setPersonnelTrainingInfo(personTrainingU);
			break;
		case "D":	
			irbUtilDao.updatePersonTraining(vo.getPersonTraining(),sqldateAcknowledged,sqlfollowUpDate);
			vo = irbUtilDao.loadPersonTraining(vo);		
			break;
		}		
		return vo;		
	}

	@Override
	public IRBUtilVO loadTrainingList(String trainingName) {
		IRBUtilVO vo = irbUtilDao.loadTrainingList(trainingName);
		return vo;
	}
	
	@Override
	public IRBUtilVO getPersonTrainingInfo(Integer personTrainingId) {
		IRBUtilVO irbUtilVO = new IRBUtilVO();
		try {
			ArrayList<HashMap<String, Object>> personTrainingList = irbUtilDao.getPersonTraining(personTrainingId);
			HashMap<String, Object> personTraining=personTrainingList.get(0);
			irbUtilVO.setPersonnelTrainingInfo(personTraining);	
			//acType 1 for personTraining and 2 for protocolPersons
			String acType="1";
			ArrayList<HashMap<String, Object>> personTrainingComment = irbUtilDao.getPersonTrainingComment(personTrainingId,acType);
			irbUtilVO.setPersonnelTrainingComment(personTrainingComment);
			ArrayList<HashMap<String, Object>> personTrainingAttachment = irbUtilDao.getPersonTrainingAttachment(personTrainingId,acType);
			irbUtilVO.setPersonnelTrainingAttachments(personTrainingAttachment);
		} catch (Exception e) {
			logger.info("Exception in getPersonTrainingInfo:" + e);
		} 
		return irbUtilVO;
	}
	
	@Override
	public ResponseEntity<byte[]> downloadFileData(String fileDataId) {
		ResponseEntity<byte[]> attachments = irbUtilDao.downloadFileData(fileDataId);
		return attachments;
	}

	@Override
	public ArrayList<HashMap<String, Object>> addTrainingAttachments(MultipartFile[] files, String formDataJson,String fileDataId) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			PersonTrainingAttachment personTrainingAttachment = mapper.readValue(formDataJson, PersonTrainingAttachment.class);
			irbUtilDao.updateTrainingAttachments(files,personTrainingAttachment,fileDataId);		
			result = irbUtilDao.getPersonTrainingAttachment(personTrainingAttachment.getPersonTrainingId(),"1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IRBUtilVO addTrainingComments(PersonTrainingComments personTrainingComments) {
		IRBUtilVO vo = new IRBUtilVO();
		irbUtilDao.addTrainingComments(personTrainingComments);
		ArrayList<HashMap<String, Object>> personTrainingComment = irbUtilDao.getPersonTrainingComment(personTrainingComments.getPersonTrainingId(),"1");
		vo.setPersonnelTrainingComment(personTrainingComment);	
		return vo;
	}

	@Override
	public IRBPermissionVO checkUserPermission(IRBPermissionVO vo) {
		try{
			Boolean hasPermission = null;			
			hasPermission = irbUtilDao.checkUserPermission(vo.getProtocolId() ,vo.getDepartment(),vo.getPersonId(),vo.getAcType());						
			if(!hasPermission){
				vo.setSuccessCode(hasPermission);
				vo.setSuccessMessage("User do not have permission");
			}else{
				vo.setSuccessCode(true);
				vo.setSuccessMessage("User has permission");
			}
		} catch (Exception e) {
			logger.info("Exception in checkUserPermission:" + e);
		}
		return vo;
	}	
}
