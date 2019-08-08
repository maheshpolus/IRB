package org.mit.irb.web.IRBProtocol.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.pojo.Lock;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.roles.pojo.PersonRoles;
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

	Boolean checkUserPermission(String protocolNumber, String username,String acType, String department);

	/**
	 * @param moduleItemKey
	 * @return lock object for given protocol number
	 */
	List<Lock> fetchProtocolLockData(String moduleItemKey);

	/**
	 * @param lock
	 * @return 
	 */
	Lock createProtocolLock(Lock lock);

	/**
	 * @param protocolNumber
	 */
	void releaseProtocolLock(String protocolNumber);

	ArrayList<HashMap<String, Object>> fetchUserPermission(String personId);

	/**
	 * @param personId
	 * @return
	 */
	List<PersonRoles> fetchUserRoles(String personId);

	/**
	 * @param personId
	 * @return
	 */
	List<Lock> fetchAdminUserLockList(String personId);

	/**
	 * @param personId
	 * @return
	 */
	List<Lock> fetchPIUserLockList(String personId);

	Integer generateLockId();
}
