package org.mit.irb.web.codetable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import java.io.ByteArrayOutputStream;

import org.springframework.http.MediaType;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.mit.irb.web.codetable.dao.CodeTableDao;
import org.mit.irb.web.codetable.dao.JSONParser;
import org.mit.irb.web.codetable.dao.QueryBuilder;
import org.mit.irb.web.codetable.dto.CodeTable;
import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.dbengine.DBEngine;
import org.mit.irb.web.dbengine.Parameter;

@Service(value = "codeTableSerivce")
public class CodeTableSerivceImpl implements CodeTableSerivce{
	protected static Logger logger = Logger.getLogger(CodeTableSerivceImpl.class.getName());
	private DBEngine dbEngine;
	
	@Autowired 
	private CodeTableDao codeTableDao;

	public CodeTableSerivceImpl() {
		dbEngine = new DBEngine();
	}

	@Override
	public CodeTableDatabus getCodeTableDetail(HttpServletRequest request, CodeTableDatabus codeTableDatabus) throws Exception {
		try{
			codeTableDatabus.setConfigFile(JSONParser.getJSONData(request));
			List<Fields> fields = new ArrayList<Fields>();
			Fields field = new Fields();
			fields.add(field);
			CodeTable codeTable = new CodeTable();
			codeTable.setFields(fields);
			codeTableDatabus.setCodetable(codeTable);
		}catch(Exception e){
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in getCodeTableDetail"+ e.getMessage());
		}
		return codeTableDatabus;
	}
	
	@Override
	public CodeTableDatabus getTableDetail(CodeTableDatabus codeTableDatabus) throws Exception {
		try{		
			String columnList = codeTableDao.getColumnList(codeTableDatabus.getCodetable().getFields());
			String query = QueryBuilder.selectQuery(codeTableDatabus.getCodetable().getDatabase_table_name(),columnList);	
			ArrayList<HashMap<String, Object>> dataList = dbEngine.executeQuerySQL(new ArrayList<Parameter>(),query);
			codeTableDatabus.setTableData(dataList);
		}catch(Exception e){
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in getTableDetail"+ e.getMessage());
		}
		return codeTableDatabus;
	}

	@Override
	public CodeTableDatabus updateCodeTableRecord(CodeTableDatabus codeTableDatabus, MultipartFile[] files) {
		try{
          	HashMap<String, Object> changedMap = codeTableDao.getChangedValue(files,codeTableDatabus);
          	int rowsUpdated = 0;
          	if(!changedMap.isEmpty()){ 
          		HashMap<String, Object> primaryKeyMap = codeTableDao.getPrimaryKeyValue(codeTableDatabus);	
          		ArrayList<Parameter> inputParam = codeTableDao.setUpdateParamValues(files,changedMap,primaryKeyMap,codeTableDatabus);
    			String query = QueryBuilder.updateQuery(changedMap,primaryKeyMap,codeTableDatabus);		
    			rowsUpdated = dbEngine.executeUpdateSQL(inputParam,query);
          	}else{
          		rowsUpdated = 1;
          	}
          	codeTableDatabus.setPromptMessage(rowsUpdated == 1 ?"Successfully updated code table" : null);
			codeTableDatabus.setPromptCode(1);
			
		} catch (Exception e) {
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in updateCodeTableRecord"+ e.getMessage());			
		}
		return codeTableDatabus;
	}

	@Override
	public CodeTableDatabus deleteCodeTableRecord(CodeTableDatabus codeTableDatabus) {
		try{
			HashMap<String, Object> changedMap = codeTableDao.getChangedValue(null,codeTableDatabus);
			HashMap<String, Object> primaryKeyMap = codeTableDao.getPrimaryKeyValue(codeTableDatabus);
			String query = QueryBuilder.deleteQuery(changedMap,primaryKeyMap,codeTableDatabus);
			int rowsUpdated = dbEngine.executeUpdateSQL(new ArrayList<Parameter>(),query);
			codeTableDatabus.setPromptMessage(rowsUpdated == 1 ?"Successfully deleted the row": "Error found") ;
			codeTableDatabus.setPromptCode(1);
		} catch (Exception e) {
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in deleteCodeTableRecord"+ e.getMessage());			
		}
		return codeTableDatabus;
	}

	@Override
	public CodeTableDatabus addCodeTableRecord(MultipartFile[] files, CodeTableDatabus codeTableDatabus) {
		try{			
			HashMap<String, Object> changedMap = codeTableDao.getChangedValue(files,codeTableDatabus);
			changedMap = codeTableDao.generateMandatoryFields(codeTableDatabus,changedMap,files);
			ArrayList<Parameter> inputParam = codeTableDao.setInsertParamValues(files,changedMap,codeTableDatabus);
			String query = QueryBuilder.insertQuery(changedMap,codeTableDatabus);
			int rowsUpdated = dbEngine.executeUpdateSQL(inputParam, query);
			codeTableDatabus.getTableData().clear(); 
			codeTableDatabus.getTableData().add(changedMap);
			codeTableDatabus.setPromptMessage(rowsUpdated == 1 ?"Successfully added a row": "Error found");
			codeTableDatabus.setPromptCode(1);
		} catch (Exception e) {
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in addCodeTableRecord"+ e.getMessage());			
		}
		return codeTableDatabus;
	}

	@Override
	public ResponseEntity<byte[]> downloadAttachments(CodeTableDatabus codeTableDatabus,HttpServletResponse response) {
		ResponseEntity<byte[]> attachmentData = null;
		try {
			HashMap<String, Object> primaryKeyMap = codeTableDao.getPrimaryKeyValue(codeTableDatabus);	
			String columnList = codeTableDao.getAttachmentColumn(codeTableDatabus.getCodetable().getFields());
			String query = QueryBuilder.selectQueryForAttachment(columnList,primaryKeyMap,codeTableDatabus);	
			ArrayList<HashMap<String, Object>> dataList = dbEngine.executeQuerySQL(new ArrayList<Parameter>(),query);
			if(dataList.get(0).get(codeTableDatabus.getSelectedColumnForDownload()) != null){
				HttpHeaders headers = new HttpHeaders();
				String contentType = dataList.get(0).get("CONTENT_TYPE") == null ? codeTableDao.getContentType(dataList) : dataList.get(0).get("CONTENT_TYPE").toString();
				byte[] data = getByteArray(dataList.get(0).get(codeTableDatabus.getSelectedColumnForDownload()),codeTableDatabus);
				headers.setContentType(MediaType.parseMediaType(contentType));
				String filename = dataList.get(0).get("FILE_NAME").toString();
				headers.setContentDispositionFormData(filename, filename);
				headers.setContentLength(data.length);
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				headers.setPragma("public");
				attachmentData = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			codeTableDatabus.setPromptCode(0);
			codeTableDatabus.setPromptMessage(e.getMessage());
			logger.error("Exception in downloadAttachments"+ e.getMessage());			
		}
		return attachmentData;
	}


	private byte[] getByteArray(Object byteObject,CodeTableDatabus codeTableDatabus) {
		byte[] byteData = null;
		try{
			String datatype = QueryBuilder.getColumnDataType(codeTableDatabus.getCodetable(),codeTableDatabus.getSelectedColumnForDownload());
			if(datatype.equalsIgnoreCase("Clob")){
				String byteString = byteObject.toString();
				byteData = byteString.getBytes();
			}else{
				ByteArrayOutputStream byteArrayOutputStream = null;
				byteArrayOutputStream = (ByteArrayOutputStream) byteObject;
				byteData = byteArrayOutputStream.toByteArray();
			}
		} catch (Exception e) {
			logger.error("Exception in getByteArray"+ e.getMessage());			
		}
		return byteData;
	}
}
