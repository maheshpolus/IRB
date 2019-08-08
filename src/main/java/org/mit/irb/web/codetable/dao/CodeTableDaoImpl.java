package org.mit.irb.web.codetable.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.sql.Timestamp;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.dbengine.DBEngine;
import org.mit.irb.web.dbengine.Parameter;
import org.mit.irb.web.dbengine.DBEngineConstants;

@Service(value = "codeTableDao")
public class CodeTableDaoImpl implements CodeTableDao{
	protected static Logger logger = Logger.getLogger(CodeTableDaoImpl.class.getName());
	private DBEngine dbEngine;
	
	public CodeTableDaoImpl(){
		dbEngine = new DBEngine();
	}
	@Override
	public String getColumnList(List<Fields> fields) {
		String commaSeparatedField = null;
		try{
			List<String> columnList = new ArrayList<String>();
			for(Fields field :fields){
				if(field.getVisible() == true && !field.getData_type().equalsIgnoreCase("Blob") && !field.getData_type().equalsIgnoreCase("Clob")){			
					columnList.add(field.getColumn_name());
				}		
			}
			commaSeparatedField = String.join(",", columnList);
		}catch(Exception e){
			logger.error("Exception in getColumnList"+ e.getMessage());
		}
		return commaSeparatedField;
	}

	@Override
	public HashMap<String, Object> getChangedValue(MultipartFile[] files,CodeTableDatabus codeTableDatabus) {
		HashMap<String, Object> modifiedData = new HashMap<String, Object>();
		try{
			for(Fields field: codeTableDatabus.getCodetable().getFields()){
				String changedColumn = null;
				String changedColumnValue = null;
				if(field.getValue_changed() != null && field.getValue_changed() == true){
					changedColumn = field.getColumn_name();
				}
				if(changedColumn != null){
					for (HashMap<String, Object> hmResult : codeTableDatabus.getTableData()) {			
							if (hmResult.get(changedColumn) != null) {
								changedColumnValue = hmResult.get(changedColumn).toString();
							}
						}
						if(changedColumnValue.equalsIgnoreCase("")){
							changedColumnValue =null;
						}
						modifiedData.put(changedColumn, changedColumnValue);
					}
				}				
		} catch (Exception e) {
			logger.error("Exception in getChangedValue"+ e.getMessage());			
		}
		return modifiedData;
	}
	
	@Override
	public HashMap<String, Object> getPrimaryKeyValue(CodeTableDatabus codeTableDatabus) {
		HashMap<String, Object> primaryKeyValues = new HashMap<String, Object>();
		try{
			for(String primaryKey : codeTableDatabus.getCodetable().getPrimary_key()){
				Object primaryKeyValue = null;
				for (HashMap<String, Object> hmResult : codeTableDatabus.getTableData()) {	
					if (hmResult.get(primaryKey) != null) {
						primaryKeyValue = hmResult.get(primaryKey);
					}
				}
				primaryKeyValues.put(primaryKey, primaryKeyValue);
			}
		} catch (Exception e) {
			logger.error("Exception in getPrimaryKeyValue"+ e.getMessage());			
		}
		return primaryKeyValues;
	}

	@Override
	public ArrayList<Parameter> getInputVariable(List<String> primaryKeyList, HashMap<String, Object> updatedMap, List<Fields> fieldList,
			HashMap<String, String> changedMap, ArrayList<Parameter> inParam) {
		try{			
			for(String primaryKey :primaryKeyList ){
				for(Fields fields :fieldList){
					if(fields.getColumn_name().equalsIgnoreCase(primaryKey)){
						inParam.add(new Parameter(primaryKey,fields.getData_type(),updatedMap.get(primaryKey)));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getInputVariable"+ e.getMessage());			
		}
		return inParam;
	}

	@Override
	public HashMap<String, Object> generateMandatoryFields(CodeTableDatabus codeTableDatabus,
			HashMap<String, Object> changedMap, MultipartFile[] files) {
		try{
			for(Fields field : codeTableDatabus.getCodetable().getFields()){
				for(String primaryKey :codeTableDatabus.getCodetable().getPrimary_key()){
						if(field.getColumn_name().equalsIgnoreCase(primaryKey)){
							if(field.getIs_editable() != true){
								String query = QueryBuilder.selectMaxEntry(codeTableDatabus.getCodetable(),primaryKey);	
								ArrayList<HashMap<String, Object>> dataList = dbEngine.executeQuerySQL(new ArrayList<Parameter>(),query);
								Integer max = 0;
								if(dataList.get(0).get("MAX_DATA") != null){
								    max =  Integer.parseInt(dataList.get(0).get("MAX_DATA").toString());
									max++;								
								}else{
									max++;				
								}
								changedMap.put(primaryKey, max.toString());
							}
						}else if(field.getColumn_name().equalsIgnoreCase("OBJ_ID")){
							UUID uuid = UUID.randomUUID();
	      					changedMap.put("OBJ_ID", uuid.toString());
						}else if(field.getColumn_name().equalsIgnoreCase("UPDATE_USER")){
							changedMap.put("UPDATE_USER", codeTableDatabus.getUpdatedUser());
						}else if(field.getColumn_name().equalsIgnoreCase("VER_NBR")){
							changedMap.put("VER_NBR", 1);
						}else if(field.getColumn_name().equalsIgnoreCase("UPDATE_TIMESTAMP")){
							changedMap.put("UPDATE_TIMESTAMP", codeTableDatabus.getTableData().get(0).get("UPDATE_TIMESTAMP"));
						}else if(field.getColumn_name().equalsIgnoreCase("CONTENT_TYPE")){
						changedMap.put("CONTENT_TYPE",  files[0].getContentType());
					}		
				}
			}
		} catch (Exception e) {
			logger.error("Exception in generatePrimaryKey"+ e.getMessage());			
		}
		return changedMap;
	}
	
	@Override
	public String getAttachmentColumn(List<Fields> fields) {
		String commaSeparatedField = null;
		try{
			List<String> columnList = new ArrayList<String>();
			for(Fields field :fields){	
				columnList.add(field.getColumn_name());	
			}
			commaSeparatedField = String.join(",", columnList);
		}catch(Exception e){
			logger.error("Exception in getAttachmentColumn"+ e.getMessage());
		}
		return commaSeparatedField;
	}
	@Override
	public String getContentType(ArrayList<HashMap<String, Object>> dataList) {
		String contentType = null;
		try{
			String extentionType = getExtentionType(dataList);
			if(extentionType.equalsIgnoreCase(".jpg")){
				contentType ="image/jpeg";
			}else if(extentionType.equalsIgnoreCase(".pdf")){
				contentType ="application/pdf";
			}else if(extentionType.equalsIgnoreCase(".png")){
				contentType ="image/png";
			}else if(extentionType.equalsIgnoreCase(".txt")){
				contentType ="text/plain";
			}else if(extentionType.equalsIgnoreCase(".docx")){
				contentType ="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			}else if(extentionType.equalsIgnoreCase(".sql")){
				contentType ="application/octet-stream";
			}else if(extentionType.equalsIgnoreCase(".eml")){
				contentType ="message/rfc822";
			}else if(extentionType.equalsIgnoreCase(".xlsx")){
				contentType ="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			}else if(extentionType.equalsIgnoreCase(".zip")){
				contentType ="application/x-zip-compressed";
			}else if(extentionType.equalsIgnoreCase(".doc")){
				contentType ="application/msword";
			}else if(extentionType.equalsIgnoreCase(".ts")){
				contentType ="video/vnd.dlna.mpeg-tts";
			}else if(extentionType.equalsIgnoreCase(".ico")){
				contentType ="image/x-icon";
			}else if(extentionType.equalsIgnoreCase(".sig") || extentionType.equalsIgnoreCase(".json")){
				contentType ="application/octet-stream";
			}else if(extentionType.equalsIgnoreCase(".js")){
				contentType ="application/javascript";
			}else if(extentionType.equalsIgnoreCase(".mp3")){
				contentType ="audio/mp3";
			}
		}catch(Exception e){
			logger.error("Exception in getContentType"+ e.getMessage());
		}
		return contentType;
	}
	private String getExtentionType(ArrayList<HashMap<String, Object>> dataList) {
		String extentionType = null;
		try{
			if(dataList.get(0).get("CONTENT_TYPE") == null){
				String filename = dataList.get(0).get("FILE_NAME").toString();
				if(filename.contains(".")){
					int index = filename.lastIndexOf('.');
					extentionType = filename.substring(index,filename.length());			
				}
			}else{
				extentionType = dataList.get(0).get("CONTENT_TYPE").toString();
			}
		}catch(Exception e){
			logger.error("Exception in getExtentionType"+ e.getMessage());
		}
		return extentionType;
	}
	@Override
	public ArrayList<Parameter> setInsertParamValues(MultipartFile[] files, HashMap<String, Object> changedMap,
			CodeTableDatabus codeTableDatabus) {
		ArrayList<Parameter> inputParam = new ArrayList<>();
		try{
			for(Fields fields: codeTableDatabus.getCodetable().getFields()){
				if(fields.getData_type().equalsIgnoreCase("Blob") || fields.getData_type().equalsIgnoreCase("Clob")){
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(),files.length != 0 ? files[0].getBytes():new byte[0]));
				}else if (fields.getData_type().equalsIgnoreCase("INTEGER") ) {
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(),Integer.parseInt(changedMap.get(fields.getColumn_name()).toString())));
				}else if (fields.getData_type().equalsIgnoreCase("Date") ) {
					if(fields.getColumn_name().equalsIgnoreCase("UPDATE_TIMESTAMP")){
						inputParam.add(new Parameter(fields.getColumn_name(),DBEngineConstants.TYPE_TIMESTAMP,getCurrentTimestamp()));
					}else{
						Timestamp timestamp = getUpdatedDate(changedMap.get(fields.getColumn_name()).toString());
						inputParam.add(new Parameter(fields.getColumn_name(), DBEngineConstants.TYPE_TIMESTAMP,timestamp));
					}				
				}else{
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(), changedMap.get(fields.getColumn_name())));
				}			
			}
		}catch(Exception e){
			logger.error("Exception in setParamValues"+ e.getMessage());
		}
		return inputParam;
	}
	
	private static Timestamp  getCurrentTimestamp() {
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	private static Timestamp getUpdatedDate(String stringDate) {
		Timestamp timestampDate = null;
		try {
		    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		    java.util.Date date = dateFormat.parse(stringDate);
		    timestampDate = new Timestamp(date.getTime());
		} catch(Exception e) { 
		    System.out.println(e);
		}
		return timestampDate;
	}
	@Override
	public ArrayList<Parameter> setUpdateParamValues(MultipartFile[] files, HashMap<String, Object> changedMap,
			HashMap<String, Object> primaryKeyMap, CodeTableDatabus codeTableDatabus) {
		ArrayList<Parameter> inputParam = new ArrayList<>();
		Set<String> changedMapKeySet = changedMap.keySet();
		Set<String> primaryKeySet = primaryKeyMap.keySet();
		try{
			for(String key : changedMapKeySet){
				inputParam = updateInputParams(files,codeTableDatabus,key,changedMap,inputParam);
			}
			for(String primaryKey : primaryKeySet){
				inputParam = updateInputParams(files,codeTableDatabus,primaryKey,primaryKeyMap,inputParam);
			}
		}catch(Exception e){
			logger.error("Exception in setUpdateParamValues"+ e.getMessage());
		}
		return inputParam;
	}
	private ArrayList<Parameter> updateInputParams(MultipartFile[] files, CodeTableDatabus codeTableDatabus, String key,
			HashMap<String, Object> changedMap, ArrayList<Parameter> inputParam) throws Exception {
		for(Fields fields: codeTableDatabus.getCodetable().getFields()){
			if(fields.getColumn_name().equalsIgnoreCase(key)){
				if(fields.getData_type().equalsIgnoreCase("Blob") || fields.getData_type().equalsIgnoreCase("Clob")){
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(), files[0].getBytes()));
				}else if (fields.getData_type().equalsIgnoreCase("INTEGER") ) {
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(),Integer.parseInt(changedMap.get(fields.getColumn_name()).toString())));
				}else if (fields.getData_type().equalsIgnoreCase("Date") ) {
					if(fields.getColumn_name().equalsIgnoreCase("UPDATE_TIMESTAMP")){
						inputParam.add(new Parameter(fields.getColumn_name(),DBEngineConstants.TYPE_TIMESTAMP,getCurrentTimestamp()));
					}else{
						Timestamp timestamp = getUpdatedDate(changedMap.get(fields.getColumn_name()).toString());
						inputParam.add(new Parameter(fields.getColumn_name(), DBEngineConstants.TYPE_TIMESTAMP,timestamp));
					}	
				}else{
					inputParam.add(new Parameter(fields.getColumn_name(), fields.getData_type(), changedMap.get(fields.getColumn_name())));
				}				
			}
		}
		return inputParam;
	}
}
