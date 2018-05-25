package org.mit.irb.web.dashboard.daoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.view.IRBViews;
import org.mit.irb.web.common.view.SnapshotData;
import org.mit.irb.web.dashboard.dao.DashboardDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value="dashboardDao")
public class DashboardDaoImpl implements DashboardDao{
	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());
	private DBEngine dBEngine;
	
	public DashboardDaoImpl(){ 
		dBEngine = new DBEngine();
	}
	
	@Override
	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY", outputParam);
			if (result != null && !result.isEmpty()) {
				if(personRoleType.equals("PI")){
					getPiSnaspshots(profile, result);
				}
				else if (personRoleType.equals("ADMIN")||personRoleType.equals("CHAIR")){
					getAdminSnaspshots(profile, result);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getSnapshots"+ e);
		}
		return profile;
	}

	public void getAdminSnaspshots(DashboardProfile profile, ArrayList<HashMap<String, Object>> result) {
		SnapshotData snapshotData = new SnapshotData();
		for(HashMap<String, Object> hmap: result){
			if(hmap.get("COMMITTEE_NAME_PREV") != null){
				snapshotData.setCommitte_name_prev(hmap.get("COMMITTEE_NAME_PREV").toString());
			} else{
				//snapshotData.setCommitte_name_prev("0");
			}
			if(hmap.get("SCHEDULED_DATE_PREV") != null){
				snapshotData.setScheduled_date_prev(hmap.get("SCHEDULED_DATE_PREV").toString());
			} else{
				//snapshotData.setScheduled_date_prev("0");
			}
			if(hmap.get("SCHEDULED_DAY_PREV") != null){
				snapshotData.setScheduled_day_prev(hmap.get("SCHEDULED_DAY_PREV").toString());
			} else{
				//snapshotData.setScheduled_day_prev("0");
			}
			if(hmap.get("COMMITTEE_NAME_NEXT") != null){
				snapshotData.setCommittee_name_next(hmap.get("COMMITTEE_NAME_NEXT").toString());
			} else{
				//snapshotData.setCommittee_name_next("0");
			}
			if(hmap.get("SCHEDULED_DATE_NEXT") != null){
				snapshotData.setScheduled_date_next(hmap.get("SCHEDULED_DATE_NEXT").toString());
			} else{
				//snapshotData.setScheduled_date_next("0");
			}
			if(hmap.get("SCHEDULED_DAY_NEXT") != null){
				snapshotData.setScheduled_day_next(hmap.get("SCHEDULED_DAY_NEXT").toString());
			} else{
				//snapshotData.setScheduled_day_next("0");
			}
			if(hmap.get("REVISION_REQ_COUNT") != null){
				snapshotData.setRevision_req_count(hmap.get("REVISION_REQ_COUNT").toString());
			} else{
				snapshotData.setRevision_req_count("0");
			}
		}
		profile.setSnapshotData(snapshotData);
	}

	public void getPiSnaspshots(DashboardProfile profile, ArrayList<HashMap<String, Object>> result) {
		SnapshotData snapshotData = new SnapshotData();
		for(HashMap<String, Object> hmap: result){
			if(hmap.get("AMMEND_RENEW_COUNT") != null){
				snapshotData.setAmmend_renew_count(hmap.get("AMMEND_RENEW_COUNT").toString());
			} else{
				snapshotData.setAmmend_renew_count("0");
			}
			if(hmap.get("REVISION_REQ_COUNT") != null){
				snapshotData.setRevision_req_count(hmap.get("REVISION_REQ_COUNT").toString());
			} else{
				snapshotData.setRevision_req_count("0");
			}
			if(hmap.get("AWAITING_RESP_COUNT") != null){
				snapshotData.setAwaiting_resp_count(hmap.get("AWAITING_RESP_COUNT").toString());
			} else{
				snapshotData.setAwaiting_resp_count("0");
			}
		}
		profile.setSnapshotData(snapshotData);
	}

	@Override
	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboard_type,
			String lead_unit_number, String protocol_number,String protocol_type_code, String title) {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		inputParam.add(new InParameter("DASHBOARD_TYPE", DBEngineConstants.TYPE_STRING, dashboard_type));
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocol_number));
		inputParam.add(new InParameter("TITLE", DBEngineConstants.TYPE_STRING, title));
		inputParam.add(new InParameter("LEAD_UNIT_NUMBER", DBEngineConstants.TYPE_STRING, lead_unit_number));
		inputParam.add(new InParameter("PROTOCOL_TYPE_CODE", DBEngineConstants.TYPE_STRING, protocol_type_code));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_DASHBOARD", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			profile.setDashBoardDetailMap(result);
		}
		return profile;
	}

	@Override
	public DashboardProfile getExpandedSnapShotView(String personId, String person_role_type, String av_summary_type) {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, person_role_type));
		inputParam.add(new InParameter("AV_SUMMARY_TYPE ", DBEngineConstants.TYPE_STRING, av_summary_type));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY_LIST", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (result != null && !result.isEmpty()) {
			profile.setDashBoardDetailMap(result);
		}
		return profile;
	}
}
