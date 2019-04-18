package org.mit.irb.web.IRBProtocol.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBUtilDao {

	IRBUtilVO loadPersonTraining(IRBUtilVO vo);

	void updatePersonTraining(PersonTraining personTraining, String user);

	IRBUtilVO loadTrainingList(String trainingName);
	
	ArrayList<HashMap<String, Object>> getPersonTraining(Integer personTrainingId);

	ArrayList<HashMap<String, Object>> getPersonTrainingComment(Integer personTrainingId);

	ArrayList<HashMap<String, Object>> getPersonTrainingAttachment(Integer personTrainingId);
	
	ResponseEntity<byte[]> downloadFileData(String fileDataId);

	void addTrainingComments(PersonTrainingComments personTrainingComments, String updateUser);

	void updateTrainingAttachments(MultipartFile[] files, PersonTrainingAttachment jsonObj);
}
