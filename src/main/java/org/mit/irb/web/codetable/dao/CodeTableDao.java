package org.mit.irb.web.codetable.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.dbengine.Parameter;

public interface CodeTableDao {
	
	 /**
	 * @param fields
	 * @return Get visible columns for table
	 */
	String getColumnList(List<Fields> fields);
	
	/**
	 * @param codeTableDatabus
	 * @return Get changed values
	 */
	HashMap<String, Object> getChangedValue(CodeTableDatabus codeTableDatabus);

	/**
	 * @param codeTableDatabus
	 * @return Get primary key and its value
	 */
	HashMap<String, Object> getPrimaryKeyValue(CodeTableDatabus codeTableDatabus);

	/**
	 * @param primaryKeyList
	 * @param updatedMap 
	 * @param fieldList 
	 * @param changedMap 
	 * @param inParam
	 * @return set input variables
	 */
	ArrayList<Parameter> getInputVariable(List<String> primaryKeyList, HashMap<String, Object> updatedMap, List<Fields> fieldList, HashMap<String, String> changedMap, ArrayList<Parameter> inParam);

	/**
	 * @param codeTableDatabus
	 * @param changedMap
	 * @return generate Primary key of table
	 */
	HashMap<String, Object> generateMandatoryFields(CodeTableDatabus codeTableDatabus, HashMap<String, Object> changedMap);
}
