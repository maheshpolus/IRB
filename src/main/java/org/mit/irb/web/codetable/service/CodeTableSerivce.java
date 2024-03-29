package org.mit.irb.web.codetable.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

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
	 * @param files 
	 * @return modify a row in a table
	 */
	CodeTableDatabus updateCodeTableRecord(CodeTableDatabus codeTableDatabus, MultipartFile[] files);

	/**
	 * @param codeTableDatabus
	 * @return delete a row from a table
	 */
	CodeTableDatabus deleteCodeTableRecord(CodeTableDatabus codeTableDatabus);

	/**
	 * @param codeTableDatabus
	 * @return create a new row in a table
	 */
	CodeTableDatabus addCodeTableRecord(MultipartFile[] files,CodeTableDatabus codeTableDatabus);

	/**
	 * @param codeTableDatabus
	 * @param response 
	 * @return
	 */
	ResponseEntity<byte[]> downloadAttachments(CodeTableDatabus codeTableDatabus, HttpServletResponse response);

	/**
	 * @param files
	 * @param codeTableDatabus
	 * @return add/update the attachments added.
	 * @throws Exception 
	 */
	/*String addModifyAttachmentField(MultipartFile[] files, CodeTableDatabus codeTableDatabus) throws Exception;*/
}
