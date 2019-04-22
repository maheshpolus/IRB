package org.mit.irb.web.IRBProtocol.dao.Impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.VO.IRBUtilVO;
import org.mit.irb.web.IRBProtocol.dao.IRBUtilDao;
import org.mit.irb.web.IRBProtocol.pojo.FileData;
import org.mit.irb.web.IRBProtocol.pojo.PersonTraining;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingAttachment;
import org.mit.irb.web.IRBProtocol.pojo.PersonTrainingComments;
import org.mit.irb.web.common.pojo.IRBExemptForm;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "irbUtilDao")
public class IRBUtilDaoImpl implements IRBUtilDao{
	DBEngine dbEngine;
	
	IRBUtilDaoImpl() {
		dbEngine = new DBEngine();
	}

	Logger logger = Logger.getLogger(IRBUtilDaoImpl.class.getName());

	@Override
	public IRBUtilVO loadPersonTraining(IRBUtilVO vo) {
		ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
		inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,vo.getSearchMode()));
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,vo.getPersonId()));								
		inputParam.add(new InParameter("AV_TRAINING_CODE", DBEngineConstants.TYPE_INTEGER,vo.getTrainingCode()));
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam,"GET_IRB_PERSON_TRAINING_LIST", outputParam);
			if (result != null && !result.isEmpty()) {
				vo.setPersonTrainingList(result);
			}
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
	public void updatePersonTraining(PersonTraining personTraining,String user){
		try{
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,personTraining.getPersonTrainingID()));			
			inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,personTraining.getPersonID()));				
			inputParam.add(new InParameter("AV_TRAINING_NUMBER", DBEngineConstants.TYPE_INTEGER,personTraining.getTrainingNumber()));
			inputParam.add(new InParameter("AV_TRAINING_CODE", DBEngineConstants.TYPE_STRING,personTraining.getTrainingCode()));
			inputParam.add(new InParameter("AV_DATE_ACKNOWLEDGED", DBEngineConstants.TYPE_DATE,personTraining.getDateAcknowledged()));		
			inputParam.add(new InParameter("AV_FOLLOWUP_DATE", DBEngineConstants.TYPE_DATE,personTraining.getFollowUpDate()));	
			inputParam.add(new InParameter("AV_COMMENTS", DBEngineConstants.TYPE_CLOB,personTraining.getDescription()));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,user));
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
	public ArrayList<HashMap<String, Object>> getPersonTrainingComment(Integer personTrainingId) {
		ArrayList<HashMap<String, Object>> result = null;
		try{
			ArrayList<InParameter> inputParam = new ArrayList<>();
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, personTrainingId));
			outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAING_COMMENTS", outputParam);
		}catch (Exception e) {
			logger.info("Exception in getPersonTraining:" + e);
		}	
		return result;
	}

	@Override
	public ArrayList<HashMap<String, Object>> getPersonTrainingAttachment(Integer personTrainingId) {
		ArrayList<HashMap<String, Object>> result = null;
		try{
			ArrayList<InParameter> inputParam = new ArrayList<>();
			ArrayList<OutParameter> outputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER, personTrainingId));
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
		inputParam.add(new InParameter("AV_SEARCH_TEXT", DBEngineConstants.TYPE_STRING, trainingName));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PERSON_TRAING_TCODE_SR", outputParam);
		} catch (DBException | IOException | SQLException e) {			
			e.printStackTrace();
		}
		vo.setTrainingDesc(result);
		return vo;
	}
	
	@Override
	public ResponseEntity<byte[]> downloadFileData(String fileDataId) {
		Integer attachmentsId = Integer.parseInt(fileDataId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FILE_DATA_ID", DBEngineConstants.TYPE_INTEGER, attachmentsId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_IRB_PERSON_TRAING_ATTMT_DL",
					outParam);
			if (result != null && !result.isEmpty()) {
				HashMap<String, Object> hmResult = result.get(0);
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) hmResult.get("FILE_DATA");
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
	public void updateTrainingAttachments(MultipartFile[] files,PersonTrainingAttachment jsonObj) {
		try {
			
			FileData fileData=new FileData();
			fileData.setFileData(files[0].getBytes());
			ArrayList<InParameter> inputParam = null;
			inputParam = new ArrayList<>();
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_STRING,jsonObj.getAcType() ));
			inputParam.add(new InParameter("AV_ATTACHMENT_ID", DBEngineConstants.TYPE_INTEGER,jsonObj.getAttachmentId()));
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,jsonObj.getPersonTrainingId()));
			inputParam.add(new InParameter("AV_DESCRIPTION", DBEngineConstants.TYPE_STRING,jsonObj.getDescription()));
			inputParam.add(new InParameter("AV_FILE_NAME", DBEngineConstants.TYPE_STRING,jsonObj.getFileName()));
			inputParam.add(new InParameter("AV_MIME_TYPE", DBEngineConstants.TYPE_STRING,jsonObj.getMimeType() ));
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,jsonObj.getUpdateUser()));
			inputParam.add(new InParameter("AV_DATA", DBEngineConstants.TYPE_BLOB,fileData.getFileData()));
			dbEngine.executeProcedure(inputParam, "UPD_IRB_PERSON_TRAING_ATTACMNT");
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in updateExemptprotocolAttachments method" + e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in updateExemptprotocolAttachments method" + e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in updateExemptprotocolAttachments method" + e);
		}
	}

	@Override
	public void addTrainingComments(PersonTrainingComments personTrainingComments, String updateUser) {
		try {
			ArrayList<InParameter> inputParam  = new ArrayList<InParameter>();
			inputParam.add(new InParameter("AV_TYPE", DBEngineConstants.TYPE_INTEGER,personTrainingComments.getAcType()));			
			inputParam.add(new InParameter("AV_COMMENT_ID", DBEngineConstants.TYPE_STRING,personTrainingComments.getCommentId()));	
			inputParam.add(new InParameter("AV_PERSON_TRAINING_ID", DBEngineConstants.TYPE_INTEGER,personTrainingComments.getPersonTrainingId()));			
			inputParam.add(new InParameter("AV_COMMENT_DESCRIPTION", DBEngineConstants.TYPE_STRING,personTrainingComments.getCommentDescription()));	
			inputParam.add(new InParameter("AV_UPDATE_USER", DBEngineConstants.TYPE_STRING,updateUser));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
