package org.mit.irb.web.IRBProtocol.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBUtilDao {

	IRBUtilVO loadPersonTraining(IRBUtilVO vo);

	void updatePersonTraining(PersonTraining personTraining,Date sqldateAcknowledged, Date sqlfollowUpDate);

	IRBUtilVO loadTrainingList(String trainingName);
	
	ArrayList<HashMap<String, Object>> getPersonTraining(Integer personTrainingId);

	ArrayList<HashMap<String, Object>> getPersonTrainingComment(Integer personTrainingId, String acType);

	ArrayList<HashMap<String, Object>> getPersonTrainingAttachment(Integer personTrainingId, String acType);
	
	ResponseEntity<byte[]> downloadFileData(String fileDataId);

	void addTrainingComments(PersonTrainingComments personTrainingComments);

	void updateTrainingAttachments(MultipartFile[] files, PersonTrainingAttachment jsonObj, String fileDataId);

	Integer createPersonTrainingId();

	Boolean checkUserPermission(Integer protocolId, String username,String acType, String department);
}
