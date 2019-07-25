package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.IRBProtocol.pojo.Lock;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "irbUtilDao")
public class IRBUtilDaoImpl implements IRBUtilDao{
	DBEngine dbEngine;
	
	IRBUtilDaoImpl() {
		dbEngine = new DBEngine();
	}

	@Autowired
	HibernateTemplate hibernateTemplate;
	
	Logger logger = Logger.getLogger(IRBUtilDaoImpl.class.getName());

	@Override
	public IRBUtilVO loadPersonTraining(IRBUtilVO vo) {
		ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,"A"));
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,vo.getPersonId()));								
		inputParam.add(new InParameter("AV_TRAINING_CODE", DBEngineConstants.TYPE_INTEGER,vo.getTrainingCode()));
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam,"GET_IRB_PERSON_TRAINING_LIST", outputParam);			
			vo.setPersonTrainingList(result);		
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in loadPersonTraining:" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in loadPersonTraining:" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in loadPersonTraining:" + e);
		}
		return vo;
	}
	
	@Override
	public void updatePersonTraining(PersonTraining personTraining,Date sqldateAcknowledged, Date sqlfollowUpDate){
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,personTraining.getPersonTrainingID()));			
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,personTraining.getPersonID()));				
			inputParam.add(new InParameter("AV_TRAINING_NUMBER", DBEngineConstants.TYPE_INTEGER,null));
			inputParam.add(new InParameter("AV_TRAINING_CODE", DBEngineConstants.TYPE_INTEGER,personTraining.getTrainingCode()));
			inputParam.add(new InParameter("AV_DATE_ACKNOWLEDGED", DBEngineConstants.TYPE_DATE,sqldateAcknowledged));		
			inputParam.add(new InParameter("AV_FOLLOWUP_DATE", DBEngineConstants.TYPE_DATE,sqlfollowUpDate));	
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_STRING,personTraining.getDescription()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,personTraining.getUpdateUser()));
			inputParam.add(new InParameter("AV_IS_EMPLOYEE", DBEngineConstants.TYPE_STRING,personTraining.getIsEmployee()));		
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,personTraining.getAcType()));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PERSON_TRAINING");
		} catch (Exception e) {
			logger.error("Exception in method updatePersonTraining", e);
		}	
	}
	@Override
	public ArrayList<HashMap<String, Object>> getPersonTraining(Integer personTrainingId) {
		ArrayList<HashMap<String, Object>> result = null;
		try{
			ArrayList<InParameter> inputParam = new ArrayList<>();
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, personTrainingId));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAINING_DTLS", outputParam);
		}catch (Exception e) {
			logger.info("Exception in getPersonTraining:" + e);
		}	
		return result;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getPersonTrainingComment(Integer personTrainingId,String acType) {
		ArrayList<HashMap<String, Object>> result = null;
		try{
			ArrayList<InParameter> inputParam = new ArrayList<>();
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, personTrainingId));
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, ""));
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,acType));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAING_COMMENTS", outputParam);
		}catch (Exception e) {
			logger.info("Exception in getPersonTraining:" + e);
		}	
		return result;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getPersonTrainingAttachment(Integer personTrainingId,String acType) {
		ArrayList<HashMap<String, Object>> result = null;
		try{
			ArrayList<InParameter> inputParam = new ArrayList<>();
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, personTrainingId));
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, ""));
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,acType));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAING_ATTACMNT", outputParam);
		}catch (Exception e) {
			logger.info("Exception in getPersonTraining:" + e);
		}	
		return result;
	}
	
	@Override
	public IRBUtilVO loadTrainingList(String trainingName) {
		IRBUtilVO vo = new IRBUtilVO();
		ArrayList<HashMap<String, Object>> result = null;
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();	
		inputParam.add(new InParameter("AV_SEARCH_TEXT", DBEngineConstants.TYPE_STRING,trainingName));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		try {
			result = dbEngine.executeProcedure(inputParam,"GET_IRB_PERSON_TRAINING_CODES", outputParam);
		} catch (DBException | IOException | SQLException e) {			
			e.printStackTrace();
		}
		vo.setTrainingDesc(result);
		return vo;
	}
	
	@Override
	public ResponseEntity<byte[]> downloadFileData(String fileDataId) {
		//Integer attachmentsId = Integer.parseInt(fileDataId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FILE_DATA_ID", DBEngineConstants.TYPE_STRING, fileDataId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_PERSON_TRAING_ATTMT_DL",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("DATA");
				byte[] data = byteArrayOutputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.parseMediaType(hmResult.get("CONTENT_TYPE").toString()));
				String filename = hmResult.get("FILE_NAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in downloadFileData method:" + e);
		}
		return attachmentData;
	}

	@Override
	public void updateTrainingAttachments(MultipartFile[] files,PersonTrainingAttachment personTrainingAttachment,String fileDataId) {		
		try {
			ArrayList<InParameter> inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,personTrainingAttachment.getAcType() ));
			inputParam.add(new InParameter("AV_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,personTrainingAttachment.getAttachmentId()));
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,personTrainingAttachment.getPersonTrainingId()));
			inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,personTrainingAttachment.getDescription()));
			inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,personTrainingAttachment.getFileName()));
			inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING,personTrainingAttachment.getMimeType() ));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,personTrainingAttachment.getUpdateUser()));
			if(personTrainingAttachment.getAcType().equalsIgnoreCase("D")){
				inputParam.add(new InParameter("AV_DATA", DBEngineConstants.TYPE_BLOB,new byte[0]));
			}else{
				inputParam.add(new InParameter("AV_DATA", DBEngineConstants.TYPE_BLOB,files[0].getBytes()));
			}	
			inputParam.add(new InParameter("AV_FILE_DATA_ID", DBEngineConstants.TYPE_STRING,fileDataId));
			dbEngine.executeProcedure(inputParam,"UPD_IRB_PERSON_TRAING_ATTACMNT");		
		} catch (Exception e) {
			logger.info("Exception in updateTrainingAttachments method" + e);
		}
	}

	@Override
	public void addTrainingComments(PersonTrainingComments personTrainingComments) {
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<>();
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,personTrainingComments.getAcType()));			
			inputParam.add(new InParameter("AV_COMMENT_ID", DBEngineConstants.TYPE_INTEGER,personTrainingComments.getCommentId()));	
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,personTrainingComments.getPersonTrainingId()));			
			inputParam.add(new InParameter("AV_COMMENT_DESCRIPTION", DBEngineConstants.TYPE_STRING,personTrainingComments.getCommentDescription()));	
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,personTrainingComments.getUpdateUser()));
			dbEngine.executeProcedure(inputParam, "UPD_IRB_PERSON_TRAING_CMNT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer createPersonTrainingId() {
		Integer trainingId = null;
		try{
			ArrayList<OutParameter> outParam = new ArrayList<>();
			outParam.add(new OutParameter("training_id",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction("FN_IRB_GET_NEXT_PRSN_TRAING_ID",outParam);	
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				trainingId = Integer.parseInt((String)hmResult.get("training_id"));			
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return trainingId;
	}

	@Override
	public Boolean checkUserPermission(String protocolNumber,String department, String personId,String acType) {
		Boolean hasPermission = false;
		try{
			ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,personId));			
			inputParam.add(new InParameter("AV_UNIT_NUMBER", DBEngineConstants.TYPE_STRING,department));
			inputParam.add(new InParameter("AV_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING,protocolNumber));
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,acType));	
			outParam.add(new OutParameter("hasPermission",DBEngineConstants.TYPE_STRING));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction(inputParam,"FN_CHECK_USER_RIGHTS",outParam);	
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				hasPermission = hmResult.get("hasPermission").toString().equalsIgnoreCase("1") ? true : false;			
			}
		} catch (Exception e) {
			logger.info("Exception in checkUserPermission method" + e);
		}	
		return hasPermission;
	}

	@Override
	public List<Lock> fetchProtocolLockData(String moduleItemKey) {
		List<Lock> lockList =new ArrayList<Lock>();
		try{
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(Lock.class);
			criteria.add(Restrictions.eq("MODULE_ITEM_KEY", moduleItemKey));
			lockList = criteria.list();
		} catch (Exception e) {
			logger.info("Exception in fetchProtocolLockData method" + e);
		}		
		return lockList;
	}

	@Override
	public Lock createProtocolLock(Lock lock) {
		try{
			hibernateTemplate.saveOrUpdate(lock);
		} catch (Exception e) {
			logger.info("Exception in createProtocolLock method" + e);
		}
		
		return lock;	
	}
}
