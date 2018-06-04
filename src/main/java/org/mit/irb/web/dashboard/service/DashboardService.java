package org.mit.irb.web.dashboard.service;

import org.mit.irb.web.common.pojo.DashboardProfile;
import org.springframework.stereotype.Service;

/*
 * Interface for dasboard services
*/

@Service
public interface DashboardService {
	
	/**
	 * @param personId
	 * @param personRoleType
	 * @return load snapshots in the dashboard
	 * @throws Exception
	 */
	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception;

	/**
	 * @param personId
	 * @param personRoleType
	 * @param dashboardType
	 * @param piName
	 * @param protocolNumber
	 * @param protocolTypeCode
	 * @param title
	 * @return load all the IRB protocols in the dashboard
	 */
	
	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboardType,
			String piName, String protocolNumber,String protocolTypeCode, String title);

	/**
	 * @param personId
	 * @param personRoleType
	 * @param avSummaryType
	 * @return load the details of snapshots in the dashboard
	 */
	public DashboardProfile getExpandedSnapShotView(String personId, String personRoleType, String avSummaryType);

	/**
	 * @return load the available protocolTypes to the dropdown for advance searching
	 */
	public DashboardProfile getDashboardProtocolType();
}
