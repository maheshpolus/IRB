package org.mit.irb.web.correspondence.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.IRBProtocol.dao.IRBExemptProtocolDao;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.IRBViewProfile;
import org.mit.irb.web.correspondence.service.CorrespondenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CorrespondenceController {
	@Autowired CorrespondenceService correspondenceService;
	@Autowired IRBExemptProtocolDao irbExemptProtocolDao;
	protected static Logger logger = Logger.getLogger(CorrespondenceController.class.getName());

	
	@RequestMapping(value = "/generateCorrespondence", method = RequestMethod.GET)
	public ResponseEntity<byte[]> generateCorrespondence(HttpServletResponse response,
			@RequestHeader("jsonObject") String jsonObject) throws Exception{
		CommonVO commonvoData = getPersonExemptForm(jsonObject);
		return correspondenceService.generateCorrespondence(response,commonvoData);
	}


	@RequestMapping(value = "/generateActionCorrespondence", method = RequestMethod.GET)
	public ResponseEntity<byte[]> generateActionCorrespondence(HttpServletResponse response,
			@RequestHeader("jsonObject") String jsonObject) throws Exception{
		CommonVO commonvoData = getPersonExemptForm(jsonObject);
		return correspondenceService.generateActionCorrespondence(response,commonvoData);
	}
	
	private CommonVO getPersonExemptForm(String jsonObject) throws Exception {
		CommonVO commonvo = new CommonVO();
		String exemptFormId = null;
		String personId = null;
		JSONObject questionnaireJsnobject = new JSONObject(jsonObject);
		if(!questionnaireJsnobject.get("commonVo").equals(null)){
			String commonVo = String.valueOf(questionnaireJsnobject.get("commonVo"));
			ObjectMapper mapper = new ObjectMapper();
			commonvo = mapper.readValue(commonVo, CommonVO.class);
		}else{
			exemptFormId = String.valueOf(questionnaireJsnobject.get("exemptFormId"));
			personId = String.valueOf(questionnaireJsnobject.get("personId"));
			IRBViewProfile irbViewProfile = irbExemptProtocolDao.getPersonExemptForm(Integer.parseInt(exemptFormId), personId);
			commonvo.setIrbExemptForm(irbViewProfile.getIrbExemptFormList().get(0));
		}
		return commonvo;
	}
}
