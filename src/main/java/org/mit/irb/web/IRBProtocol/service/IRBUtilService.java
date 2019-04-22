package org.mit.irb.web.IRBProtocol.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBUtilService {

	IRBUtilVO loadPersonTraining(IRBUtilVO vo);

	IRBUtilVO updatePersonTraining(PersonTraining personTraining, String user);

	IRBUtilVO loadTrainingList(String trainingName);

	/**
	 * @param personTrainingId
	 * @return person Training detail
	 */
	IRBUtilVO getPersonTrainingInfo(Integer personTrainingId);
	/**
	 * @param fileDataId
	 * @return download a file
	 */
	ResponseEntity<byte[]> downloadFileData(String fileDataId);

	ArrayList<HashMap<String, Object>> addTrainingAttachments(MultipartFile[] files, String formDataJson);

	IRBUtilVO addTrainingComments(PersonTrainingComments personTrainingComments, String updateUser);
}
