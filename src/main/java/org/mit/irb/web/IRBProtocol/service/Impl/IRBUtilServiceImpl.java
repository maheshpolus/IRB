package org.mit.irb.web.IRBProtocol.service.Impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.mit.irb.web.IRBProtocol.VO.IRBPermissionVO;
import org.mit.irb.web.IRBProtocol.VO.IRBProtocolVO;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.IRBProtocol.pojo.Lock;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.IRBProtocol.service.IRBUtilService;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.roles.pojo.PersonRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "irbUtilService") 
public class IRBUtilServiceImpl implements IRBUtilService {
	
	@Autowired
	IRBUtilDao irbUtilDao;
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	@Value("${system.timezone}")
	private String timezone;
	
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
			Boolean lockPresent = false;
			if(vo.getAcType().equalsIgnoreCase("E")){
				List<Lock> lockList = irbUtilDao.fetchProtocolLockData(vo.getProtocolNumber());
				if(!lockList.isEmpty()){
					lockPresent = true;
				}
			}
			if(lockPresent){
				vo.setSuccessCode(false);
				vo.setSuccessMessage("Protocol is locked by other user, Do you want to open in View Mode?");
			}else{
				hasPermission = irbUtilDao.checkUserPermission(vo.getProtocolNumber() ,vo.getDepartment(),vo.getPersonId(),vo.getAcType());						
				if(!hasPermission){
					vo.setSuccessCode(hasPermission);
					vo.setSuccessMessage("You do not have permission to edit this protocol");
				}else{
					vo.setSuccessCode(true);
					vo.setSuccessMessage("User has permission");
				}
			}
			
		} catch (Exception e) {
			logger.info("Exception in checkUserPermission:" + e);
		}
		return vo;
	}

	@Override
	public Date adjustTimezone(Date date) {		
		try {
			 SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy");
			 DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
		     df.setTimeZone(TimeZone.getTimeZone(timezone));		       
		     return newFormat.parse(df.format(date));		       
		}catch(Exception e) {
			logger.debug("Error in adusting adjustTimezone, input date is "+date+" . Error "+e.getMessage());
		}       
        return date;
	}

	@Override
	public IRBUtilVO checkLockPresent(IRBUtilVO vo) {
		Boolean lockPresent = false;
		try{
			List<Lock> lockList = irbUtilDao.fetchProtocolLockData(vo.getProtocolNumber());
			for(Lock lock :lockList){
					if(lock.getPersonId().equalsIgnoreCase(vo.getPersonId())){
						lockPresent = false;
					}else{
						lockPresent = true;
					}	
				}			
			vo.setLockPresent(lockPresent);	
		}catch(Exception e) {
			logger.debug("Error in checkLockPresent"+e.getMessage());
		}  
		return vo;
	}

	private Integer generateLockId() {
		Integer lockId = null;
		Query queryGeneral = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("SELECT NVL(MAX(LOCK_ID),0)+1 FROM Lock");
		if (!queryGeneral.list().isEmpty()) {
			lockId = Integer.parseInt(queryGeneral.list().get(0).toString());
		}
		return lockId;
	}
	
	@Override
	public IRBProtocolVO createLock(IRBProtocolVO irbProtocolVO) {
		try{
			Lock lock = new Lock();
			lock.setLockId(generateLockId());
			lock.setModuleCode(KeyConstants.PROTOCOL_MODULE_CODE);
			lock.setModuleItemKey(irbProtocolVO.getProtocolNumber());
			lock.setPersonId(irbProtocolVO.getPersonId());
			lock.setUpdateUser(irbProtocolVO.getUpdateUser());
			lock.setUpdateTimestamp(new Date());
			lock = irbUtilDao.createProtocolLock(lock);
		}catch(Exception e) {
			logger.debug("Error in createLock"+e.getMessage());
		}
		return irbProtocolVO;
	}

	@Override
	public IRBUtilVO releaseProtocolLock(IRBUtilVO vo) {
		try{
			irbUtilDao.releaseProtocolLock(vo.getProtocolNumber());
			vo = loadProtocolLock(vo);
		}catch(Exception e) {
			logger.debug("Error in releaseProtocolLock"+e.getMessage());
		}
		return vo;
	}

	@Override
	public IRBUtilVO loadProtocolLock(IRBUtilVO vo) {
		try{
			/*ArrayList<HashMap<String, Object>> userPermissionMap = irbUtilDao.fetchUserPermission(vo.getPersonId());
			for(HashMap<String, Object> userPermission : userPermissionMap){
				if(userPermission.get("PERM_NM").toString().equalsIgnoreCase("")){
					
				}
			}*/
			Boolean adminUser = false;
			List<PersonRoles> personRolesList = irbUtilDao.fetchUserRoles(vo.getPersonId());
			for(PersonRoles personRoles : personRolesList){
				if(personRoles.getRoleId() == 5){
					adminUser = true;
					break;
				}
			}
			List<Lock> lockList = null;
			if(adminUser){
				lockList = irbUtilDao.fetchAdminUserLockList(vo.getPersonId());
			}else{
				lockList = irbUtilDao.fetchPIUserLockList(vo.getPersonId());
			}
			vo.setLockList(lockList);
		}catch(Exception e) {
			logger.debug("Error in loadProtocolLock"+e.getMessage());
		}
		return vo;
	}	
}
