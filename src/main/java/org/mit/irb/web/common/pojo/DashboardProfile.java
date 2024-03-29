package org.mit.irb.web.common.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.committee.pojo.ProtocolSubmissionStatus;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.common.view.SnapshotData;

/**
 * 
 * Class for setting dashboard related data
 *
 */
public class DashboardProfile {
	private List<IRBViews> irbViews;
	private SnapshotData snapshotData;
	private ArrayList<HashMap<String, Object>> dashBoardDetailMap;
	private ArrayList<HashMap<String, Object>> dashboardExemptCardDetails;
	private List<ProtocolSubmissionStatus> protocolSubmissionStatusList;
	private  Boolean canCreateProtocol;
	
	public ArrayList<HashMap<String, Object>> getDashboardExemptCardDetails() {
		return dashboardExemptCardDetails;
	}

	public void setDashboardExemptCardDetails(ArrayList<HashMap<String, Object>> dashboardExemptCardDetails) {
		this.dashboardExemptCardDetails = dashboardExemptCardDetails;
	}

	public List<IRBViews> getIrbViews() {
		return irbViews;
	}

	public void setIrbViews(List<IRBViews> irbViews) {
		this.irbViews = irbViews;
	}

	public ArrayList<HashMap<String, Object>> getDashBoardDetailMap() {
		return dashBoardDetailMap;
	}

	public void setDashBoardDetailMap(ArrayList<HashMap<String, Object>> dashBoardDetailMap) {
		this.dashBoardDetailMap = dashBoardDetailMap;
	}

	public SnapshotData getSnapshotData() {
		return snapshotData;
	}

	public void setSnapshotData(SnapshotData snapshotData) {
		this.snapshotData = snapshotData;
	}

	public List<ProtocolSubmissionStatus> getProtocolSubmissionStatusList() {
		return protocolSubmissionStatusList;
	}

	public void setProtocolSubmissionStatusList(List<ProtocolSubmissionStatus> protocolSubmissionStatusList) {
		this.protocolSubmissionStatusList = protocolSubmissionStatusList;
	}

	public Boolean getCanCreateProtocol() {
		return canCreateProtocol;
	}

	public void setCanCreateProtocol(Boolean canCreateProtocol) {
		this.canCreateProtocol = canCreateProtocol;
	}
}
