package org.mit.irb.web.dashboard.service.Impl;

import java.text.ParseException;

import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.dashboard.dao.DashboardDao;
import org.mit.irb.web.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "dashboardService")
public class DashboardServiceImpl implements DashboardService {

	private DBEngine dBEngine;

	public DashboardServiceImpl(){ 
		dBEngine = new DBEngine();
	}

	@Autowired
	DashboardDao dashboardDao; 

	@Override
	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception {
		DashboardProfile profile = dashboardDao.getSnapshotData(personId, personRoleType);
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboardType,
			String piName, String protocolNumber,String protocolTypeCode, String title, String prtocolStatusCode,String approvalDate,String  expirationDate,
			String isAdvanceSearch,String fundingSource,String protocolSubmissionStatus,String adminPersonId) throws ParseException {
		DashboardProfile profile = dashboardDao.getDashboardProtocolList(personId, personRoleType, dashboardType,
				piName, protocolNumber, protocolTypeCode, title, prtocolStatusCode,
				approvalDate,expirationDate,isAdvanceSearch,fundingSource,protocolSubmissionStatus,adminPersonId);
		return profile;
	}

	@Override
	public DashboardProfile getExpandedSnapShotView(String personId, String personRoleType, String avSummaryType) {
		DashboardProfile profile = dashboardDao.getExpandedSnapShotView(personId, personRoleType, avSummaryType);
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolType() {
		DashboardProfile profile = dashboardDao.getDashboardProtocolType();
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolStatus() {
		DashboardProfile profile = dashboardDao.getDashboardProtocolStatus();
		return profile;
	}

	@Override
	public DashboardProfile getprotocolSubmissionStatus() {
		DashboardProfile profile = dashboardDao.getProtocolSubmissionStatus();
		return profile;
	}
}
