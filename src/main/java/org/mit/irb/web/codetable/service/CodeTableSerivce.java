package org.mit.irb.web.codetable.service;

import javax.servlet.http.HttpServletRequest;

import org.mit.irb.web.codetable.dto.CodeTableDatabus;

public interface CodeTableSerivce {

	/**
	 * @param request
	 * @param codeTableDatabus 
	 * @return List of modules and their sub lookup tables.
	 * @throws Exception
	 */
	CodeTableDatabus getCodeTableDetail(HttpServletRequest request, CodeTableDatabus codeTableDatabus) throws Exception;

	/**
	 * @param codeTableDatabus
	 * @return rows of a table
	 * @throws Exception
	 */
	CodeTableDatabus getTableDetail(CodeTableDatabus codeTableDatabus) throws Exception;

	/**
	 * @param codeTableDatabus
	 * @return modify a row in a table
	 */
	CodeTableDatabus updateCodeTableRecord(CodeTableDatabus codeTableDatabus);

	/**
	 * @param codeTableDatabus
	 * @return delete a row from a table
	 */
	CodeTableDatabus deleteCodeTableRecord(CodeTableDatabus codeTableDatabus);

	/**
	 * @param codeTableDatabus
	 * @return create a new row in a table
	 */
	CodeTableDatabus addCodeTableRecord(CodeTableDatabus codeTableDatabus);
}
