package org.mit.irb.web.common.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.common.view.SnapshotData;

public class DashboardProfile {
	private List<IRBViews> irbViews;
	private List<SnapshotData> snapshotData;
	private ArrayList<HashMap<String, Object>> dashBoardDetailMap;
	
	public List<IRBViews> getIrbViews() {
		return irbViews;
	}
	public void setIrbViews(List<IRBViews> irbViews) {
		this.irbViews = irbViews;
	}
	public List<SnapshotData> getSnapshotData() {
		return snapshotData;
	}
	public void setSnapshotData(List<SnapshotData> snapshotData) {
		this.snapshotData = snapshotData;
	}
	public ArrayList<HashMap<String, Object>> getDashBoardDetailMap() {
		return dashBoardDetailMap;
	}
	public void setDashBoardDetailMap(ArrayList<HashMap<String, Object>> dashBoardDetailMap) {
		this.dashBoardDetailMap = dashBoardDetailMap;
	}
}
