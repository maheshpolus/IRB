package org.mit.irb.web.dashboard.dao;

import java.text.ParseException;

import org.mit.irb.web.common.pojo.DashboardProfile;

public interface DashboardDao {

	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception;

	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType ,String dashboardType, String pi_name,
			String protocolNumber,String protocolTypeCode, String title, String prtocolStatusCode,String approvalDate, String expirationDate,
			String  isAdvanceSearch,String fundingSource,String protocolSubmissionStatus, String adminPersonId) throws ParseException;

	public DashboardProfile getExpandedSnapShotView(String personId, String personRoleType, String avSummaryType);

	public DashboardProfile getDashboardProtocolType();

	public DashboardProfile getDashboardProtocolStatus();
	
	public DashboardProfile getProtocolSubmissionStatus();

	public DashboardProfile getAdminDashBoardPermissions(String personId, String leadUnitNumber);
}
