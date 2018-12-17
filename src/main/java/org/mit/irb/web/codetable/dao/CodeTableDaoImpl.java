package org.mit.irb.web.codetable.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.dbengine.DBEngine;
import org.mit.irb.web.dbengine.Parameter;

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
				if(field.getVisible() == true){			
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
	public HashMap<String, Object> getChangedValue(CodeTableDatabus codeTableDatabus) {
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
			HashMap<String, Object> changedMap) {
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
						changedMap.put("UPDATE_TIMESTAMP", getCurrentTimestamp());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in generatePrimaryKey"+ e.getMessage());			
		}
		return changedMap;
	}
	
	public String getCurrentTimestamp() throws ParseException {
		long millis=System.currentTimeMillis();  
        java.sql.Date date=new java.sql.Date(millis); 
        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yy HH:mm:ss");
        String dateString = sdf.format(date);
		return dateString; 
	}

}
