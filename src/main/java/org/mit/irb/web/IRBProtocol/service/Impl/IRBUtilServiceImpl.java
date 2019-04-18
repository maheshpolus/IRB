package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolInitLoadDao;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.common.pojo.IRBExemptForm;
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
	public IRBUtilVO updatePersonTraining(PersonTraining personTraining,String user) {	
		IRBUtilVO vo =new IRBUtilVO();
		irbUtilDao.updatePersonTraining(personTraining,user);
		vo.setPersonnelTrainingInfo(irbUtilDao.getPersonTraining(personTraining.getPersonTrainingID()));
		return vo;
		
	}

	@Override
	public IRBUtilVO loadTrainingList(String trainingName) {
		IRBUtilVO vo=irbUtilDao.loadTrainingList(trainingName);
		return vo;
	}
	
	@Override
	public IRBUtilVO getPersonTrainingInfo(Integer personTrainingId) {
		IRBUtilVO irbUtilVO = new IRBUtilVO();
		try {
			ArrayList<HashMap<String, Object>> personTraining = irbUtilDao.getPersonTraining(personTrainingId);
			irbUtilVO.setPersonnelTrainingInfo(personTraining);
			ArrayList<HashMap<String, Object>> personTrainingComment = irbUtilDao.getPersonTrainingComment(personTrainingId);
			irbUtilVO.setPersonnelTrainingComment(personTrainingComment);
			ArrayList<HashMap<String, Object>> personTrainingAttachment = irbUtilDao.getPersonTrainingAttachment(personTrainingId);
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
	public ArrayList<HashMap<String, Object>> addTrainingAttachments(MultipartFile[] files, String formDataJson) {
		ArrayList<HashMap<String, Object>> result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			PersonTrainingAttachment jsonObj = mapper.readValue(formDataJson, PersonTrainingAttachment.class);
			irbUtilDao.updateTrainingAttachments(files,jsonObj);		
			result =irbUtilDao.getPersonTrainingAttachment(jsonObj.getPersonTrainingId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public IRBUtilVO addTrainingComments(PersonTrainingComments personTrainingComments, String updateUser) {
		IRBUtilVO vo = new IRBUtilVO();
		irbUtilDao.addTrainingComments(personTrainingComments,updateUser);
		vo.setPersonnelTrainingComment(irbUtilDao.getPersonTrainingComment(personTrainingComments.getPersonTrainingId()));
		return vo;
	}	
}
