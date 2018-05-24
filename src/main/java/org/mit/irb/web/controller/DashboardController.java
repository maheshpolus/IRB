package org.mit.irb.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@Controller
public class DashboardController {

	@Autowired
	@Qualifier(value = "dashboardService")
	DashboardService dashboardService;

	@RequestMapping(value = "/getDashboardSnapshot", method = RequestMethod.POST)
	public ResponseEntity<String> getDashboardSnapShot(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		DashboardProfile profile = this.dashboardService.getSnapshotData(vo.getPersonId(), vo.getPerson_role_type());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = "/getExpandedSnapShotView", method = RequestMethod.POST)
	public ResponseEntity<String> getExpandedSnapShotView(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		DashboardProfile profile = this.dashboardService.getExpandedSnapShotView(vo.getPersonId(), vo.getPerson_role_type(), vo.getAv_summary_type());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/getDashboardProtocolList", method = RequestMethod.POST)
	public ResponseEntity<String> getDashboardProtocolList(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) {
		HttpStatus status = HttpStatus.OK;
		DashboardProfile profile = this.dashboardService.getDashboardProtocolList(vo.getPersonId(), vo.getPerson_role_type(),
				 vo.getDashboard_type(),vo.getLead_unit_number(), vo.getProtocol_number(), vo.getProtocol_type_code(), vo.getTitle());
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		try {
			responseData = mapper.writeValueAsString(profile);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(responseData, status);
	}
}
