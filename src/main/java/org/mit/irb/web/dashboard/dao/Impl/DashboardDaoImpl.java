package org.mit.irb.web.dashboard.dao.Impl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.view.SnapshotData;
import org.mit.irb.web.dashboard.dao.DashboardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Transactional
@Service(value="dashboardDao")
public class DashboardDaoImpl implements DashboardDao{
	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());
	private DBEngine dBEngine;
	
	
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	public DashboardDaoImpl(){ 
		dBEngine = new DBEngine();
	}
	
	@Override
	public DashboardProfile getSnapshotData(String personId, String personRoleType) throws Exception {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		ArrayList<InParameter> inputParams = new ArrayList<>();
		ArrayList<OutParameter> outputParams = new ArrayList<>();

		ArrayList<HashMap<String, Object>> permissionList = null;
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY", outputParam);
			if (result != null && !result.isEmpty()) {
				if(personRoleType.equals("PI") || personRoleType.equals("DEPT_ADMIN")){
					getPiSnaspshots(profile, result);
				}
				else if (personRoleType.equals("ADMIN")||personRoleType.equals("CHAIR")){
					getAdminSnaspshots(profile, result);
				}
			}		
			inputParams.add(new InParameter("AV_PERSON_ID", DBEngineConstants.TYPE_STRING,personId));	
			outputParams.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));		
			permissionList = dBEngine.executeProcedure(inputParams,"GET_IRB_PERSON_PERMISSION", outputParams);
			profile.setCanCreateProtocol(false);
			if(permissionList !=null && !permissionList.isEmpty()){			
				for(HashMap<String,Object> permName : permissionList){							
					if(permName.get("PERM_NM").toString().equals("Create ProtocolDocument")){
						profile.setCanCreateProtocol(true);
					}
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
				snapshotData.setCommitteNamePrev(hmap.get("COMMITTEE_NAME_PREV").toString());
			} 
			if(hmap.get("SCHEDULED_DATE_PREV") != null){
				snapshotData.setScheduledDatePrev(hmap.get("SCHEDULED_DATE_PREV").toString());
			} 
			if(hmap.get("SCHEDULED_DAY_PREV") != null){
				snapshotData.setScheduledDayPrev(hmap.get("SCHEDULED_DAY_PREV").toString());
			} 
			if(hmap.get("COMMITTEE_NAME_NEXT") != null){
				snapshotData.setCommitteeNameNext(hmap.get("COMMITTEE_NAME_NEXT").toString());
			} 
			if(hmap.get("SCHEDULED_DATE_NEXT") != null){
				snapshotData.setScheduledDateNext(hmap.get("SCHEDULED_DATE_NEXT").toString());
			} 
			if(hmap.get("SCHEDULED_DAY_NEXT") != null){
				snapshotData.setScheduledDayNext(hmap.get("SCHEDULED_DAY_NEXT").toString());
			}
			if(hmap.get("REVISION_REQ_COUNT") != null){
				snapshotData.setRevisionReqCount(hmap.get("REVISION_REQ_COUNT").toString());
			} else{
				snapshotData.setRevisionReqCount("0");
			}
		}
		profile.setSnapshotData(snapshotData);
	}

	public void getPiSnaspshots(DashboardProfile profile, ArrayList<HashMap<String, Object>> result) {
		SnapshotData snapshotData = new SnapshotData();
		for(HashMap<String, Object> hmap: result){
			if(hmap.get("AMMEND_RENEW_COUNT") != null){
				snapshotData.setAmmendRenewCount(hmap.get("AMMEND_RENEW_COUNT").toString());
			} else{
				snapshotData.setAmmendRenewCount("0");
			}
			if(hmap.get("REVISION_REQ_COUNT") != null){
				snapshotData.setRevisionReqCount(hmap.get("REVISION_REQ_COUNT").toString());
			} else{
				snapshotData.setRevisionReqCount("0");
			}
			if(hmap.get("AWAITING_RESP_COUNT") != null){
				snapshotData.setAwaitingRespCount(hmap.get("AWAITING_RESP_COUNT").toString());
			} else{
				snapshotData.setAwaitingRespCount("0");
			}
		}
		profile.setSnapshotData(snapshotData);
	}

	@Override
	public DashboardProfile getDashboardProtocolList(String personId, String personRoleType, String dashboard_type,
			String pi_name, String protocol_number,String protocol_type_code, String title,
			String prtocolStatusCode,String approvalDate,String expirationDate,String  isAdvanceSearch,
			String fundingSource,String protocolSubmissionStatus) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date approvalDates = null;
		java.sql.Date sqlApprovalDates = null;
		java.util.Date expirationDates = null;
		java.sql.Date sqlExpirationDates = null;
		if (approvalDate!= null) {
			approvalDates = sdf1.parse(approvalDate);
			sqlApprovalDates = new java.sql.Date(approvalDates.getTime());
			
		}
		if (expirationDate!= null) {
			expirationDates = sdf1.parse(expirationDate);
			sqlExpirationDates = new java.sql.Date(expirationDates.getTime());
			
		}
		DashboardProfile profile = new DashboardProfile();
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("PERSON_ROLE_TYPE", DBEngineConstants.TYPE_STRING, personRoleType));
		inputParam.add(new InParameter("DASHBOARD_TYPE", DBEngineConstants.TYPE_STRING, dashboard_type));
		inputParam.add(new InParameter("PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocol_number));
		inputParam.add(new InParameter("TITLE", DBEngineConstants.TYPE_STRING, title));
		inputParam.add(new InParameter("PI_NAME", DBEngineConstants.TYPE_STRING, pi_name));
		inputParam.add(new InParameter("PROTOCOL_TYPE_CODE", DBEngineConstants.TYPE_STRING, protocol_type_code));
		inputParam.add(new InParameter("AV_PROTOCOL_STATUS_CODE", DBEngineConstants.TYPE_STRING, prtocolStatusCode));
		inputParam.add(new InParameter("AV_IS_ADV_SEARCH", DBEngineConstants.TYPE_STRING, isAdvanceSearch));
		inputParam.add(new InParameter("AV_EXPIRATION_DATE", DBEngineConstants.TYPE_DATE, sqlExpirationDates));
		inputParam.add(new InParameter("AV_APPROVAL_DATE", DBEngineConstants.TYPE_DATE, sqlApprovalDates));
		inputParam.add(new InParameter("AV_FUNDING_SOURCE", DBEngineConstants.TYPE_STRING, fundingSource));
		inputParam.add(new InParameter("AV_PROTO_SUBMISSION_STATUS", DBEngineConstants.TYPE_STRING, protocolSubmissionStatus));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_DASHBOARD", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getDashboardProtocolList"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getDashboardProtocolList"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getDashboardProtocolList"+ e);
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
		ArrayList<HashMap<String, Object>> exemptCardResult = null;
		try {
			if(av_summary_type.equals("ADMIN_PENDING")) {	
				exemptCardResult = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY_LIST", outputParam);
			}
			else 
			{
			result = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY_LIST", outputParam);
			if(av_summary_type.equals("REVISION_REQ")){
				inputParam.remove(2);
				inputParam.add(new InParameter("AV_SUMMARY_TYPE ", DBEngineConstants.TYPE_STRING, "EXEMPT_REQ"));
				exemptCardResult = dBEngine.executeProcedure(inputParam, "GET_IRB_SUMMARY_LIST", outputParam);
			    }
			}
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getExpandedSnapShotView"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getExpandedSnapShotView"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getExpandedSnapShotView"+ e);
		}
		if (result != null && !result.isEmpty()) {
			profile.setDashBoardDetailMap(result);
		} if (exemptCardResult != null && !exemptCardResult.isEmpty()) {
			profile.setDashboardExemptCardDetails(exemptCardResult);
		}
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolType() {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure("GET_IRB_PROTOCOL_TYPE", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getDashboardProtocolType"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getDashboardProtocolType"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getDashboardProtocolType"+ e);
		}
		if (result != null && !result.isEmpty()) {
			profile.setDashBoardDetailMap(result);
		}
		return profile;
	}

	@Override
	public DashboardProfile getDashboardProtocolStatus() {
		DashboardProfile profile = new DashboardProfile();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> result = null;
		try {
			result = dBEngine.executeProcedure("GET_IRB_PROTOCOL_STATUS", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
			logger.info("DBException in getDashboardProtocolStatus"+ e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("IOException in getDashboardProtocolStatus"+ e);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQLException in getDashboardProtocolStatus"+ e);
		}
		if (result != null && !result.isEmpty()) {
			profile.setDashBoardDetailMap(result);
		}
		return profile;
	}

	@Override
	public DashboardProfile getProtocolSubmissionStatus() {
		DashboardProfile profile = new DashboardProfile();
		Query query =hibernateTemplate.getSessionFactory().getCurrentSession()
		  .createQuery("from ProtocolSubmissionStatus ");
		profile.setProtocolSubmissionStatusList(query.list());  
		return profile;
	}
}
