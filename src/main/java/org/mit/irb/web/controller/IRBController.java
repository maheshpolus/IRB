package org.mit.irb.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mit.irb.web.IRBProtocol.service.IRBProtocolService;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.common.view.IRBViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class IRBController {

	@Autowired
	@Qualifier(value="irbProtocolService")
	IRBProtocolService irbProtocolService;
	
	@RequestMapping(value="/getIRBprotocolDetails",method= RequestMethod.POST)
	public ResponseEntity<String> getIRBprotocolDetails(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException{
		String protocolNumber = vo.getProtocol_number();
		IRBViewProfile irbViewProfile = irbProtocolService.getIRBProtocolDetails(protocolNumber);
		HttpStatus status = HttpStatus.OK;
		ObjectMapper mapper = new ObjectMapper();
		String  responseData = mapper.writeValueAsString(irbViewProfile);
		return new ResponseEntity<String>(responseData, status);
	}
}
