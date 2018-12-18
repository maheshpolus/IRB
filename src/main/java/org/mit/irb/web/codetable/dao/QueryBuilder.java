package org.mit.irb.web.codetable.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.mit.irb.web.codetable.dto.CodeTable;
import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;

public class QueryBuilder {
	
	public static String selectQuery(String dbTableName, String columnList) {
		String query  = "SELECT "+columnList+" FROM "+dbTableName +" ORDER BY UPDATE_TIMESTAMP DESC";
		return query;
	}

	public static String updateQuery(HashMap<String, Object> changedMap, HashMap<String, Object> primaryKeyMap,
			CodeTableDatabus codeTableDatabus) throws Exception {
		String sqlScript = null;
		String part1 = "UPDATE "+codeTableDatabus.getCodetable().getDatabase_table_name()+ " SET ";
		String part2 = getChangedColumnSql(changedMap,codeTableDatabus);
		String part3 = getPrimaryKeySql(primaryKeyMap,codeTableDatabus);
		sqlScript = part1 + part2 + part3;
		return sqlScript;
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

	private static String getChangedColumnSql(HashMap<String, Object> changedMap,CodeTableDatabus codeTableDatabus) throws Exception {
		String part2 = "";
		Set<String> changedColumnSet = changedMap.keySet();
		for(String changedColumn: changedColumnSet){
			int lastElement = changedColumnSet.size();
			String datatype = getColumnDataType(codeTableDatabus.getCodetable(),changedColumn);
			part2 = part2 + changedColumn + " = " ;
			if(datatype.equalsIgnoreCase("String")){
				part2 = part2 +"\'"+changedMap.get(changedColumn)+"\'";
			}else if (datatype.equalsIgnoreCase("Date")) {
				if(changedColumn.equalsIgnoreCase("UPDATE_TIMESTAMP")){
					part2 = part2 +"to_date("+"\'"+getCurrentTimestamp()+"\'"+",'dd-mm-yy hh24:mi:ss')";
				}else{
					part2 = part2 +"\'"+changedMap.get(changedColumn)+"\'";
				}		
			}else{		
				part2 = part2 +changedMap.get(changedColumn);
			}
			int index = new ArrayList<>(changedColumnSet).indexOf(changedColumn);
			if(lastElement  != index+1){
				part2 = part2 + ",";
			}
		}
		part2 = part2+" WHERE ";
		return part2;
	}

	public static String getCurrentTimestamp() throws Exception {
		long millis=System.currentTimeMillis();  
        java.sql.Date date=new java.sql.Date(millis); 
        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yy HH:mm:ss");
        String dateString = sdf.format(date);
		return dateString; 
	}
	private static String getColumnDataType(CodeTable codetable,String changedColumn) {
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
		String part3 = getTableColumnValueQuery(codeTableDatabus.getCodetable(),changedMap);
		sqlScript = part1+part2+") VALUES ("+part3 +")";
		return sqlScript;
	}

	private static String getTableColumnValueQuery(CodeTable codetable, HashMap<String, Object> changedMap) {
		String tablecolumns = "";
		for(Fields fields: codetable.getFields()){
			int lastElement = codetable.getFields().size();
			String datatype = getColumnDataType(codetable,fields.getColumn_name());
			if(datatype.equalsIgnoreCase("String")){
				tablecolumns = tablecolumns +"\'"+changedMap.get(fields.getColumn_name())+"\'";
			}else if(datatype.equalsIgnoreCase("Date")) {
				if(fields.getColumn_name().equalsIgnoreCase("UPDATE_TIMESTAMP")){
					tablecolumns = tablecolumns +"to_date("+"\'"+changedMap.get(fields.getColumn_name())+"\'"+",'dd-mm-yy hh24:mi:ss')";
				}else{
					tablecolumns = tablecolumns +"\'"+changedMap.get(fields.getColumn_name())+"\'";
				}		
			}else{
				tablecolumns = tablecolumns  +changedMap.get(fields.getColumn_name());
			}
			int index = codetable.getFields().indexOf(fields);
			if(lastElement  != index+1){
				tablecolumns = tablecolumns + ",";
			}
		}
		return tablecolumns;
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
}
