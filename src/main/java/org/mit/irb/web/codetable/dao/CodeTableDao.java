package org.mit.irb.web.codetable.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.dto.Fields;
import org.mit.irb.web.dbengine.Parameter;
import org.springframework.web.multipart.MultipartFile;

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
	HashMap<String, Object> getChangedValue(MultipartFile[] files,CodeTableDatabus codeTableDatabus);

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
	 * @param files 
	 * @return generate Primary key of table
	 */
	HashMap<String, Object> generateMandatoryFields(CodeTableDatabus codeTableDatabus, HashMap<String, Object> changedMap, MultipartFile[] files);

	/**
	 * @param fields
	 * @return get attachment column
	 */
	String getAttachmentColumn(List<Fields> fields);

	/**
	 * @param dataList
	 * @return get content type of a attachment
	 */
	String getContentType(ArrayList<HashMap<String, Object>> dataList);

	/**
	 * @param files
	 * @param changedMap
	 * @param codeTableDatabus
	 * @return
	 */
	ArrayList<Parameter> setInsertParamValues(MultipartFile[] files, HashMap<String, Object> changedMap,
			CodeTableDatabus codeTableDatabus);

	/**
	 * @param files
	 * @param changedMap
	 * @param primaryKeyMap
	 * @param codeTableDatabus
	 * @return
	 */
	ArrayList<Parameter> setUpdateParamValues(MultipartFile[] files, HashMap<String, Object> changedMap,
			HashMap<String, Object> primaryKeyMap, CodeTableDatabus codeTableDatabus);
}
