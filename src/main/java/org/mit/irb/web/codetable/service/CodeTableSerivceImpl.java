package org.mit.irb.web.codetable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public CodeTableDatabus updateCodeTableRecord(CodeTableDatabus codeTableDatabus) {
		try{
          	HashMap<String, Object> changedMap = codeTableDao.getChangedValue(codeTableDatabus);
          	int rowsUpdated = 0;
          	if(!changedMap.isEmpty()){
          		HashMap<String, Object> primaryKeyMap = codeTableDao.getPrimaryKeyValue(codeTableDatabus);			
    			String query = QueryBuilder.updateQuery(changedMap,primaryKeyMap,codeTableDatabus);		
    			rowsUpdated = dbEngine.executeUpdateSQL(new ArrayList<Parameter>(),query);
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
			HashMap<String, Object> changedMap = codeTableDao.getChangedValue(codeTableDatabus);
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
	public CodeTableDatabus addCodeTableRecord(CodeTableDatabus codeTableDatabus) {
		try{			
			HashMap<String, Object> changedMap = codeTableDao.getChangedValue(codeTableDatabus);
			changedMap = codeTableDao.generateMandatoryFields(codeTableDatabus,changedMap);
			String query = QueryBuilder.insertQuery(changedMap,codeTableDatabus);
			int rowsUpdated = dbEngine.executeUpdateSQL(new ArrayList<Parameter>(),query);
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

}
