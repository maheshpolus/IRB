package org.mit.irb.web.dashboard.dao;

import org.mit.irb.web.common.pojo.DashboardProfile;

public interface DashboardDao {

	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception;

	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboard_type,
			String pi_name, String protocol_number,String protocol_type_code, String title);

	public DashboardProfile getExpandedSnapShotView(String personId, String person_role_type, String av_summary_type);

	public DashboardProfile getDashboardProtocolType();
}
