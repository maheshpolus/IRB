package org.mit.irb.web.IRBProtocol.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IRBUtilService {

	IRBUtilVO loadPersonTraining(IRBUtilVO vo);

	IRBUtilVO updatePersonTraining(IRBUtilVO vo);

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

	ArrayList<HashMap<String, Object>> addTrainingAttachments(MultipartFile[] files, String formDataJson, String fileDataId);

	IRBUtilVO addTrainingComments(PersonTrainingComments personTrainingComments);

	/**
	 * @param vo
	 * @return True if user has permission else false
	 */
	IRBPermissionVO checkUserPermission(IRBPermissionVO vo);
}
