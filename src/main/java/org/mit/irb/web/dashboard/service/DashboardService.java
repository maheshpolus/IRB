package org.mit.irb.web.dashboard.service;

import org.mit.irb.web.common.pojo.DashboardProfile;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {

	public DashboardProfile getSnapshotData(String personId, String personRoleType);

	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboard_type,
			String lead_unit_number, String protocol_number,String protocol_type_code, String title);
}
