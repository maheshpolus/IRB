package org.mit.irb.web.codetable.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mit.irb.web.codetable.dto.CodeTableDatabus;
import org.mit.irb.web.codetable.service.CodeTableSerivce;

@Controller
public class CodeTableController {
	@Autowired CodeTableSerivce codeTableService;
	
	@RequestMapping(value = "/getCodeTableMetaData", method = RequestMethod.GET)
	public ResponseEntity<String> fetchCodeTableDetails(HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		CodeTableDatabus codeTableDatabus = new CodeTableDatabus();
		codeTableDatabus = codeTableService.getCodeTableDetail(request,codeTableDatabus);	
		String responseData = mapper.writeValueAsString(codeTableDatabus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/getCodeTable",method= RequestMethod.POST)
	public ResponseEntity<String> getTableDetail(@RequestBody CodeTableDatabus codeTableDatabus, HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		if(codeTableDatabus != null){			
			codeTableDatabus = codeTableService.getTableDetail(codeTableDatabus);			
		}
		String responseData = mapper.writeValueAsString(codeTableDatabus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/updateCodeTableRecord",method= RequestMethod.POST)
	public ResponseEntity<String> updateCodeTableRecord(@RequestBody CodeTableDatabus codeTableDatabus, HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		if(codeTableDatabus != null){			
			codeTableDatabus = codeTableService.updateCodeTableRecord(codeTableDatabus);			
		}
		String responseData = mapper.writeValueAsString(codeTableDatabus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/deleteCodeTableRecord",method= RequestMethod.POST)
	public ResponseEntity<String> deleteCodeTableRecord(@RequestBody CodeTableDatabus codeTableDatabus, HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		if(codeTableDatabus != null){			
			codeTableDatabus = codeTableService.deleteCodeTableRecord(codeTableDatabus); 			
		}
		String responseData = mapper.writeValueAsString(codeTableDatabus);
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value="/addCodeTableRecord",method= RequestMethod.POST)
	public ResponseEntity<String> addCodeTableRecord(@RequestBody CodeTableDatabus codeTableDatabus, HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		if(codeTableDatabus != null){
			if(codeTableDatabus.getUpdatedUser() == null){
				codeTableDatabus.setUpdatedUser("admin");
			}
			codeTableDatabus = codeTableService.addCodeTableRecord(codeTableDatabus); 			
		}
		String responseData = mapper.writeValueAsString(codeTableDatabus);
		return new ResponseEntity<String>(responseData, status);
	}
}
