package org.mit.irb.web.dashboard.daoImpl;

import java.io.IOException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value="dashboardDao")
public class DashboardDaoImpl implements DashboardDao{
	private DBEngine dBEngine;
	
	public DashboardDaoImpl(){ 
		dBEngine = new DBEngine();
	}
	
	@Override
	public DashboardProfile getSnapshotData(String personId, String personRoleType) {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY", outputParam);
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
		inputParam.add(new InParameter("PROTOCOL_TYPE_CODE", DBEngineConstants.TYPE_STRING, protocol_number));
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

}
