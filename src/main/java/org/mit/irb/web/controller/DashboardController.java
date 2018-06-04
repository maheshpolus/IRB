package org.mit.irb.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.dashboard.service.DashboardService;
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

/*
 * Controller for getting dashboard details
*/

@Controller
public class DashboardController {

	@Autowired
	@Qualifier(value = "dashboardService")
	DashboardService dashboardService;

	protected static Logger logger = Logger.getLogger(DashboardController.class.getName());
	
	@RequestMapping(value = "/getDashboardSnapshot", method = RequestMethod.POST)
	public ResponseEntity<String> getDashboardSnapShot(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("-----------getDashboardSnapshot---------------");
		HttpStatus status = HttpStatus.OK;
		logger.info("-----------PersonId: ---"+vo.getPersonId());
		logger.info("-----------PersonRoleType: ---"+vo.getPersonRoleType());
		DashboardProfile profile = this.dashboardService.getSnapshotData(vo.getPersonId(), vo.getPersonRoleType());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
			logger.info("-----------ResponseData:---"+ responseData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.info("-----------Exception:---"+ e);
		}
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/getExpandedSnapShotView", method = RequestMethod.POST)
	public ResponseEntity<String> getExpandedSnapShotView(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("-----------getDashboardSnapshot---------------");
		HttpStatus status = HttpStatus.OK;
		logger.info("-----------PersonId: ---"+vo.getPersonId());
		logger.info("-----------PersonRoleType: ---"+vo.getPersonRoleType());
		logger.info("-----------AvSummaryType: ---"+vo.getAvSummaryType());
		DashboardProfile profile = this.dashboardService.getExpandedSnapShotView(vo.getPersonId(), vo.getPersonRoleType(), vo.getAvSummaryType());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
			logger.info("-----------ResponseData:---"+ responseData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getDashboardProtocolList", method = RequestMethod.POST)
	public ResponseEntity<String> getDashboardProtocolList(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("-----------getDashboardProtocolList---------------");
		HttpStatus status = HttpStatus.OK;
		logger.info("-----------PersonId: ---"+vo.getPersonId());
		logger.info("-----------PersonRoleType: ---"+vo.getPersonRoleType());
		logger.info("-----------DashboardType: ---"+vo.getDashboardType());
		logger.info("-----------PiName: ---"+vo.getPiName());
		logger.info("-----------ProtocolNumber: ---"+vo.getProtocolNumber());
		logger.info("-----------ProtocolTypeCode: ---"+vo.getProtocolTypeCode());
		logger.info("-----------Title: ---"+vo.getTitle());
		DashboardProfile profile = this.dashboardService.getDashboardProtocolList(vo.getPersonId(), vo.getPersonRoleType(),
				 vo.getDashboardType(),vo.getPiName(), vo.getProtocolNumber(), vo.getProtocolTypeCode(), vo.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
			logger.info("-----------ResponseData:---"+ responseData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.info("-----------Exception:---"+ e);
		}
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getDashboardProtocolType", method = RequestMethod.POST)
	public ResponseEntity<String> getDashboardProtocolType(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("-----------getDashboardProtocolType---------------");
		HttpStatus status = HttpStatus.OK;
		DashboardProfile profile = this.dashboardService.getDashboardProtocolType();
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
			logger.info("-----------ResponseData:---"+ responseData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.info("-----------Exception:---"+ e);
		}
		return new ResponseEntity<String>(responseData, status);
	}
}
