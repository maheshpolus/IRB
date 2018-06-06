package org.mit.irb.web.dashboard.service.Impl;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.dashboard.dao.DashboardDao;
import org.mit.irb.web.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			String piName, String protocolNumber,String protocolTypeCode, String title) {
		DashboardProfile profile = dashboardDao.getDashboardProtocolList(personId, personRoleType, dashboardType, piName, protocolNumber, protocolTypeCode, title);
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
}
