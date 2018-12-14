package org.mit.irb.web.dashboard.dao;

import org.mit.irb.web.common.pojo.DashboardProfile;

public interface DashboardDao {

	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception;

	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboardType, String pi_name, String protocolNumber,String protocolTypeCode, String title);

	public DashboardProfile getExpandedSnapShotView(String personId, String personRoleType, String avSummaryType);

	public DashboardProfile getDashboardProtocolType();
}
