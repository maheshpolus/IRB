package org.mit.irb.web.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public @ResponseBody DashboardProfile getDashboardSnapShot(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DashboardProfile profile = this.dashboardService.getSnapshotData(vo.getPersonId(), vo.getPersonRoleType());
		return profile;
	}

	@RequestMapping(value = "/getExpandedSnapShotView", method = RequestMethod.POST)
	public @ResponseBody DashboardProfile getExpandedSnapShotView(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DashboardProfile profile = this.dashboardService.getExpandedSnapShotView(vo.getPersonId(), vo.getPersonRoleType(), vo.getAvSummaryType());
		return profile;
	}
	
	@RequestMapping(value = "/getDashboardProtocolList", method = RequestMethod.POST)
	public @ResponseBody DashboardProfile getDashboardProtocolList(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		DashboardProfile profile = this.dashboardService.getDashboardProtocolList(vo.getPersonId(), vo.getPersonRoleType(), vo.getDashboardType(),vo.getPiName(), vo.getProtocolNumber(),
				vo.getProtocolTypeCode(), vo.getTitle(),vo.getProtocolStatusCode(),vo.getApprovalDate(),
				vo.getExpirationDate(),vo.getIsAdvancedSearch(),
				vo.getFundingSource(),vo.getProtocolSubmissionStatus());
		return profile;
	}
	
	@RequestMapping(value = "/getDashboardProtocolType", method = RequestMethod.POST)
	public @ResponseBody DashboardProfile getDashboardProtocolType(HttpServletRequest request,
			HttpServletResponse response) {
		DashboardProfile profile = this.dashboardService.getDashboardProtocolType();
		return profile;
	}
	
	@RequestMapping(value = "/getDashboardProtocolStatus", method = RequestMethod.POST)
	public @ResponseBody DashboardProfile getDashboardProtocolStatus(HttpServletRequest request,
			HttpServletResponse response) {
		DashboardProfile profile = this.dashboardService.getDashboardProtocolStatus();
		return profile;
	}
	
	@RequestMapping(value = "/getDashboardProtocolSubmissionStatus", method = RequestMethod.POST)
	public @ResponseBody CommonVO getDashboardProtocolSubmissionStatus(HttpServletRequest request,
			HttpServletResponse response) {
		CommonVO vo=this.dashboardService.getprotocolSubmissionStatus();
		return vo;
	}
}
