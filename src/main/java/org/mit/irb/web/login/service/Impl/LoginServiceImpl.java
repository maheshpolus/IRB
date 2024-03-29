package org.mit.irb.web.login.service.Impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.login.dao.LoginDao;
import org.mit.irb.web.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginDao loginDao;
	
	private DBEngine dbEngine;
	protected static Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

	public LoginServiceImpl() {
		dbEngine = new DBEngine();
	}

	@Override
	public Boolean checkUserHasRight(String personId, String unitNumber) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
		boolean right = false;
		inParam.add(new InParameter("ls_person_id", DBEngineConstants.TYPE_STRING, personId));
		outParam.add(new OutParameter("has_right", DBEngineConstants.TYPE_INTEGER));
		ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction(inParam, "fn_osr_check_person_is_ca",
				outParam);
		if (result != null && !result.isEmpty()) {
			HashMap<String, Object> hmResult = result.get(0);
			if (Integer.parseInt((String) hmResult.get("has_right")) == 1) {
				right = true;
			}
		}
		return right;
	}

	@Override
	public String checkIRBUserRole(String personId) throws Exception {
		String role = null;
		logger.info("checkIRBUserRole ");
		ArrayList<HashMap<String, Object>> result = null;
		result = loginDao.checkIRBUserRole(personId);
		HashMap<String, Object> maps = result.get(0);
		if(maps.get("IS_IRB_ADMIN").equals("Y")){
			role = "ADMIN";
			return role;
		} else if(maps.get("IS_IRB_CHAIR").equals("Y")){
			role = "CHAIR";
			return role;
		} else if(maps.get("IS_IRB_DEPT_ADMIN").equals("Y")){
			role = "DEPT_ADMIN";
			return role;
		}else if(maps.get("IS_DEPT_VIEWER").equals("Y")){
				role = "DEPT_VIEWER";
				return role;
		} else if(maps.get("IS_IRB_REVIEWER").equals("Y")){
			role = "REVIEWER";
			return role;
		}else if(maps.get("IS_IRB_PI").equals("Y")){
			role = "PI";
			return role;		
		}else if(maps.get("IS_AGGREGATOR").equals("Y")){
			role = "AGGREGATOR";
			return role;
		}else if(maps.get("IS_PROTOCOL_VIEWER").equals("Y")){
			role = "VIEWER";
			return role;
		}
		
		return role;
	}

	@Override
	public PersonDTO getPersonDetails(String personID) throws Exception {
		PersonDTO personDTO = new PersonDTO();
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
		inParam.add(new InParameter("av_prncpl_id", DBEngineConstants.TYPE_STRING, personID));
		outParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		ArrayList<HashMap<String, Object>> personDetailsList = dbEngine.executeProcedure(inParam, "GET_PERSON_DETAILS",
				outParam);
		if (personDetailsList != null && !personDetailsList.isEmpty()) {
			if (personDetailsList.get(0).get("FULL_NAME") != null) {
				personDTO.setFullName(personDetailsList.get(0).get("FULL_NAME").toString());
			}
			if (personDetailsList.get(0).get("EMAIL_ADDR") != null) {
				personDTO.setEmail(personDetailsList.get(0).get("EMAIL_ADDR").toString());
			}
			if (personDetailsList.get(0).get("PHONE_NBR") != null) {
				personDTO.setPhoneNumber(personDetailsList.get(0).get("PHONE_NBR").toString());
			}
			if (personDetailsList.get(0).get("UNIT_NAME") != null) {
				personDTO.setUnitName(personDetailsList.get(0).get("UNIT_NAME").toString());
			}
			if (personDetailsList.get(0).get("PRNCPL_ID") != null) {
				personDTO.setPersonID(personDetailsList.get(0).get("PRNCPL_ID").toString());
			}
		}
		return personDTO;
	}
 
	@Override
	public Integer checkUserType(String personId, String unitNumber) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
		Integer userType = null;
		inParam.add(new InParameter("av_person_id", DBEngineConstants.TYPE_STRING, personId));
		inParam.add(new InParameter("av_unit_number", DBEngineConstants.TYPE_STRING, unitNumber));
		outParam.add(new OutParameter("has_right", DBEngineConstants.TYPE_INTEGER));
		ArrayList<HashMap<String, Object>> result = dbEngine.executeFunction(inParam, "fn_ost_get_user_role", outParam);
		if (result != null && !result.isEmpty()) {
			HashMap<String, Object> hmResult = result.get(0);
			userType = Integer.parseInt((String) hmResult.get("has_right"));
		}
		return userType;
	}
}
