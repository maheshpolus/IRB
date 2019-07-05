package org.mit.irb.web.codetable.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.apache.log4j.Logger;

import org.mit.irb.web.codetable.dto.CodeTable;
import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.correspondence.dto.CorrespondenceDataBus;

public class QueryBuilder {
	protected static Logger logger = Logger.getLogger(QueryBuilder.class.getName());
	
	public static String selectQuery(String dbTableName, String columnList) {
		String query  = "SELECT "+columnList+" FROM "+dbTableName +" ORDER BY UPDATE_TIMESTAMP DESC";
		return query;
	}

	public static String updateQuery(HashMap<String, Object> changedMap, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) throws Exception {
		String sqlScript = null;
		String part1 = "UPDATE "+codeTableDatabus.getCodetable().getDatabase_table_name()+ " SET ";
		String part2 = getModifiedParamSql(changedMap,codeTableDatabus);
		String part3 = getPrimaryKeyParamSql(primaryKeyMap,codeTableDatabus);
		sqlScript = part1 + part2 + part3;
		return sqlScript;
	}

	private static String getPrimaryKeyParamSql(HashMap<String, Object> primaryKeyMap,CodeTableDatabus codeTableDatabus) {
		String part3 = null;
		Set<String> primaryKeySet = primaryKeyMap.keySet();
		for(String key : primaryKeySet){
			int lastElement = primaryKeySet.size();
			part3 = key + " = ";
			part3 = part3 +"<<"+key+">>";
			int index = new ArrayList<>(primaryKeySet).indexOf(key);
			if(lastElement  != index+1){
				part3 = part3 +" , ";
			}
		}
		return part3;
	}
	private static String getPrimaryKeySql(HashMap<String, Object> primaryKeyMap,CodeTableDatabus codeTableDatabus) {
		String part3 = null;
		Set<String> primaryKeySet = primaryKeyMap.keySet();
		for(String key : primaryKeySet){
			int lastElement = primaryKeySet.size();
			String datatype = getColumnDataType(codeTableDatabus.getCodetable(),key);
			part3 = key + " = ";
			if(datatype.equalsIgnoreCase("String") || datatype.equalsIgnoreCase("Date")){
				part3 = part3 +"\'"+primaryKeyMap.get(key)+"\'";
			}else{
				part3 = part3 +primaryKeyMap.get(key);
			}
			int index = new ArrayList<>(primaryKeySet).indexOf(key);
			if(lastElement  != index+1){
				part3 = part3 +" , ";
			}
		}
		return part3;
	}


	private static String getModifiedParamSql(HashMap<String, Object> changedMap, CodeTableDatabus codeTableDatabus) {
		String part2 = "";
		Set<String> changedColumnSet = changedMap.keySet();
		for(String changedColumn: changedColumnSet){
			int lastElement = changedColumnSet.size();
			part2 = part2 + changedColumn + " = " ;
			part2 = part2 +"<<"+changedColumn+">>";
			int index = new ArrayList<>(changedColumnSet).indexOf(changedColumn);
			if(lastElement  != index+1){
				part2 = part2 + ",";
			}
		}
		part2 = part2+" WHERE ";
		return part2;
	}

	public static String getColumnDataType(CodeTable codetable,String changedColumn) {
		String dataType = null;
		for(Fields field : codetable.getFields()){
			if(field.getColumn_name().equalsIgnoreCase(changedColumn)){
				dataType =field.getData_type();
				break;
			}
		}
		return dataType;
	}

	public static String deleteQuery(HashMap<String, Object> changedMap, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) {
		String sqlScript = null;
		String part1 = "DELETE FROM "+codeTableDatabus.getCodetable().getDatabase_table_name()+ " WHERE ";
		String part2 = getPrimaryKeySql(primaryKeyMap,codeTableDatabus);
		sqlScript = part1 + part2;
		return sqlScript;
	}

	public static String selectMaxEntry(CodeTable codeTable, String primaryKey) {
		String sqlScript = "SELECT MAX(";
		String datatype = getColumnDataType(codeTable,primaryKey);	
		if(datatype.equalsIgnoreCase("String")){
			sqlScript = sqlScript +"to_number("+primaryKey+")";
		}else{
			sqlScript = sqlScript + primaryKey;
		}
		sqlScript = sqlScript+") as max_data FROM "+codeTable.getDatabase_table_name() ;
		return sqlScript;
	}

	public static String insertQuery(HashMap<String, Object> changedMap, CodeTableDatabus codeTableDatabus) {
		String sqlScript = null;
		String part1 = "INSERT INTO "+codeTableDatabus.getCodetable().getDatabase_table_name()+ " (";
		String part2 = getTableColumnQuery(codeTableDatabus.getCodetable());
		String part3 = getColumnValueForParams(codeTableDatabus.getCodetable());
		sqlScript = part1+part2+") VALUES ("+part3 +")";
		return sqlScript;
	}

	private static String getColumnValueForParams(CodeTable codetable) {
		String ColumnValueForParams = "";
			for(Fields field: codetable.getFields()){
				int lastElement = codetable.getFields().size();
				
				ColumnValueForParams = ColumnValueForParams +"<<"+field.getColumn_name()+">>";
				int index = codetable.getFields().indexOf(field);
				if(lastElement  != index+1){
					ColumnValueForParams = ColumnValueForParams + ",";
				}
			}

		return ColumnValueForParams;
	}

	private static String getTableColumnQuery(CodeTable codetable) {
		String tablecolumns ="";
		for(Fields fields: codetable.getFields()){
			int lastElement = codetable.getFields().size();
			tablecolumns = tablecolumns + fields.getColumn_name();
			int index = codetable.getFields().indexOf(fields);
			if(lastElement  != index+1){
				tablecolumns = tablecolumns + ",";
			}
		}
		return tablecolumns;
	}

	public static String selectQueryForAttachment(String attachmentColumn, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) {
		String query = null;
		String part1  = "SELECT "+attachmentColumn+" FROM "+codeTableDatabus.getCodetable().getDatabase_table_name()+ " WHERE ";
		String part2 = getPrimaryKeySql(primaryKeyMap,codeTableDatabus);
		query = part1 + part2 ;
		return query;
	}

	public static String selectQueryForProtocolDetails(CorrespondenceDataBus correspondenceDataBus) {
		String sqlScript = "";
		try{
			String part1 = "SELECT PROTOCOL_NUMBER,TITLE,EXPIRATION_DATE FROM PROTOCOL WHERE ";
			String part2 = "PROTOCOL_NUMBER = "+"\'"+correspondenceDataBus.getModuleItemKey()+"\'"+" AND "+"SEQUENCE_NUMBER = "+correspondenceDataBus.getSequenceNumber();
			sqlScript = part1+part2;
		}catch(Exception e){
			logger.error("Exception in selectQueryForProtocolDetails"+ e.getMessage());
		}
		return sqlScript;
	}

	public static String selectQueryForPIName(CorrespondenceDataBus correspondenceDataBus) {
		String sqlScript = "";
		try{
			String part1 = "SELECT FULL_NAME FROM PROTOCOL_PERSONS WHERE ";
			String part2 = "PROTOCOL_NUMBER = "+"\'"+correspondenceDataBus.getModuleItemKey()+"\'"+" AND "+"SEQUENCE_NUMBER = "+correspondenceDataBus.getSequenceNumber()+" AND ";
			String part3 = "PROTOCOL_PERSON_ROLE_ID = "+"\'"+"PI"+"\'";
			sqlScript = part1+part2+part3;
		}catch(Exception e){
			logger.error("Exception in selectQueryForPIName"+ e.getMessage());
		}
		return sqlScript;
	}

	public static String selectQueryForActionDate(CorrespondenceDataBus correspondenceDataBus) {
		String sqlScript = "";
		try{
			String part1 = "SELECT ACTION_DATE FROM PROTOCOL_ACTIONS WHERE ";
			String part2 = "PROTOCOL_NUMBER = "+"\'"+correspondenceDataBus.getModuleItemKey()+"\'"+" AND "+"SEQUENCE_NUMBER = "+correspondenceDataBus.getSequenceNumber()+" AND ";
			String part3 = "PROTOCOL_ACTION_TYPE_CODE = "+"\'"+correspondenceDataBus.getActionCode()+"\'";
			sqlScript = part1+part2+part3;
		}catch(Exception e){
			logger.error("Exception in selectQueryForPIName"+ e.getMessage());
		}
		return sqlScript;
	}

	public static String selectLetterTypeCode(CorrespondenceDataBus correspondenceDataBus) {
		String sqlScript = "";
		try{
			String part1 = "SELECT LETTER_TEMPLATE_TYPE_CODE FROM IRB_ACTION_LETTER_TEMPLATE WHERE ";
			String part2 = "ACTION_TYPE_CODE = "+ correspondenceDataBus.getActionCode();
			sqlScript = part1+part2;
		}catch(Exception e){
			logger.error("Exception in selectLetterTypeCode"+ e.getMessage());
		}
		return sqlScript;
	}

	public static String selectLetterTemplate(String templateTypeCode) {
		String sqlScript = "";
		try{
			String part1 = "SELECT CORRESPONDENCE_TEMPLATE FROM LETTER_TEMPLATE_TYPE WHERE ";
			String part2 = "LETTER_TEMPLATE_TYPE_CODE = " +"\'"+ templateTypeCode +"\'";
			sqlScript = part1+part2;
		}catch(Exception e){
			logger.error("Exception in selectLetterTypeCode"+ e.getMessage());
		}
		return sqlScript;
	}
	public static String selectQueryForMap(String dbTableName, String columnList) {
		String query  = "SELECT "+columnList+" AS RVALUE FROM "+dbTableName ;
		return query;
	}
}
