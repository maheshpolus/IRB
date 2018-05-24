package org.mit.irb.web.dashboard.dao;

import org.mit.irb.web.common.pojo.DashboardProfile;

public interface DashboardDao {

	public DashboardProfile getSnapshotData(String personId, String personRoleType);

	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboard_type,
			String lead_unit_number, String protocol_number,String protocol_type_code, String title);
}
