package org.mit.irb.web.IRBProtocol.daoImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.dao.IRBProtocolDao;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.view.ServiceAttachments;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="iRBProtocolDao")
@Transactional
public class IRBProtocolDaoImpl implements IRBProtocolDao{

	DBEngine dbEngine;
	
	IRBProtocolDaoImpl(){
		dbEngine = new DBEngine();
	}
	
	Logger logger = Logger.getLogger(IRBProtocolDaoImpl.class.getName());
	@Override
	public IRBViewProfile getIRBProtocolDetails(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_DETAILS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewHeader(result.get(0));
			
		}
		System.out.println("irbview : "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolPersons(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_PERSONS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolPersons(result);
		}
		System.out.println("irbview Persons: "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolFundingSource(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_FUNDING_SRC", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolFundingsource(result);
		}
		System.out.println("irbview PROTOCOL_FUNDING_SRC: "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolLocation(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_LOCATION", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolLocation(result);
		}
		System.out.println("irbview IRBprotocolLocation: "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolVulnerableSubject(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_VULNBLE_SUBJT", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolVulnerableSubject(result);
		}
		logger.info("irbview IRB_PROTOCOL_VULNBLE_SUBJT: "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getIRBprotocolSpecialReview(String protocolNumber) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_SPECIAL_REVW", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolSpecialReview(result);
		}
		System.out.println("irbview IRBprotocolSpecialReview: "+ result);
		return irbViewProfile;
	}

	@Override
	public IRBViewProfile getMITKCPersonInfo(String avPersonId) {
		IRBViewProfile irbViewProfile = new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		ArrayList<HashMap<String, Object>> resultTraing = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_MITKC_PERSON_INFO", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolMITKCPersonInfo(result.get(0));
			resultTraing = getMITKCPersonTraingInfo(avPersonId);
			irbViewProfile.setIrbviewProtocolMITKCPersonTrainingInfo(resultTraing);
		}
		return irbViewProfile;
	}

	public ArrayList<HashMap<String, Object>> getMITKCPersonTraingInfo(String avPersonId) {
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING, avPersonId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_MITKC_PERSON_TRAINING_INFO", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
		}
		return result;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(String attachmentId) {
		Integer attachmentsId = Integer.parseInt(attachmentId);
		ResponseEntity<byte[]> attachmentData = null;
		try {
			ArrayList<InParameter> inParam = new ArrayList<>();
			ArrayList<OutParameter> outParam = new ArrayList<>();
			inParam.add(new InParameter("AV_FIL_ID", DBEngineConstants.TYPE_INTEGER, attachmentsId));
			outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
			ArrayList<HashMap<String, Object>> result = dbEngine.executeProcedure(inParam, "GET_MITKC_ATTACHMENT_FILE",
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
		}
		return attachmentData;
	}

	@Override
	public IRBViewProfile getAttachmentsList(String protocolnumber) {
		IRBViewProfile irbViewProfile= new IRBViewProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolnumber));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dbEngine.executeProcedure(inputParam, "GET_IRB_PROTOCOL_ATTACHMENT", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			irbViewProfile.setIrbviewProtocolAttachmentList(result);
		}
		return irbViewProfile;
	}
}
