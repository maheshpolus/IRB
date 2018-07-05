/**
 * Login validator class for DB user
 */
package org.mit.irb.web.login.authentication;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import org.mit.irb.web.common.dto.*;

import org.mit.irb.web.common.utils.*;
import org.mit.irb.web.controller.BaseController;
import org.mit.irb.web.login.dao.LoginDao;
import org.mit.irb.web.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginValidator extends BaseController {
	@Autowired
	@Qualifier(value="loginService")
	LoginService loginService;

	@Autowired
	private LoginDao loginDao;
	
	protected static Logger logger = Logger.getLogger(LoginValidator.class.getName());
	Statement statement = null;
	ResultSet resultSet = null;
	DBEngine dbUtils = new DBEngine();

	public LoginValidator() {
	}
	
	// login credentials checking and return boolean value for DB user
	public Boolean loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isLoginSuccess = false;
		if ("USERID".equalsIgnoreCase(loginMode)) {
			isLoginSuccess = new UserIdAuthentication().Authenticate(userName, password);

		} else if ("TouchStone".equalsIgnoreCase(loginMode)) {
			isLoginSuccess = new TouchstoneAuthService().authenticate(request, request.getSession());

		}

		if (isLoginSuccess) {
			HttpSession session = request.getSession();
			PersonDTO personDTO = readPersonData(userName);
			session.setAttribute("personDTO" + session.getId(), personDTO);
			session.setAttribute("user" + session.getId(), personDTO.getFullName());
			// setting session to expiry in 30 mins
			int timeoutValue = Integer.parseInt(IRBProperties.getProperty("SESSION_TIMEOUT_DEFAULT"));
			session.setMaxInactiveInterval(timeoutValue * 60);
			return true;
		}
		return false;
	}

	// fetching person data
	public PersonDTO readPersonData(String userName) throws DBException, IOException {
		String procedureCall = null;
		PersonDTO personDTO = new PersonDTO();
		CallableStatement callableStatement = null;
		ResultSet resultSet = null;
		if (dbUtils != null) {
			Connection connection = dbUtils.beginTxn();
			try {
				try {
					procedureCall = "{call GET_AB_PERSON_DETAILS(?, ?)}";
					callableStatement = connection.prepareCall(procedureCall);

					callableStatement.setString(1, userName);
					callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

					// Call Stored Procedure
					callableStatement.executeUpdate();
					// get cursor and cast it to ResultSet
					resultSet = (ResultSet) callableStatement.getObject(2);

					while (resultSet.next()) {
						personDTO.setPersonID(resultSet.getString("prncpl_id"));
						personDTO.setFirstName(resultSet.getString("first_nm"));
						personDTO.setHasDual(resultSet.getString("middle_nm"));
						personDTO.setLastName(resultSet.getString("last_nm"));
						personDTO.setFullName(resultSet.getString("full_name"));
						personDTO.setEmail(resultSet.getString("email_addr"));
						personDTO.setUnitNumber(resultSet.getString("prmry_dept_cd"));
						personDTO.setUnitName(resultSet.getString("unit_name"));
						personDTO.setUserName(userName);
						Integer userType = checkUserType(personDTO.getPersonID(), "");
						personDTO.setUserRoleType(userType);
						try {
							String role = checkIRBUserRole(userName);
							personDTO.setRole(role);
							logger.info("in checkIRBUserRole" +role);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (SQLException e) {
					logger.error("Error in readPersonData method.", e);
				} finally {
					close(resultSet, callableStatement);
				}

			} finally {
				dbUtils.endTxn(connection);
			}
		}
		logger.info("personDto" +personDTO.getUnitName()+personDTO);
		return personDTO;
	}
	
	public Integer checkUserType(String personId, String unitNumber) throws DBException, IOException, SQLException {
		ArrayList<InParameter> inParam = new ArrayList<InParameter>();
		ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
		Integer userType = null;
		inParam.add(new InParameter("av_person_id", DBEngineConstants.TYPE_STRING, personId));
		inParam.add(new InParameter("av_unit_number", DBEngineConstants.TYPE_STRING, unitNumber));
		outParam.add(new OutParameter("has_right", DBEngineConstants.TYPE_INTEGER));
		ArrayList<HashMap<String, Object>> result = dbUtils.executeFunction(inParam, "fn_ost_get_user_role", outParam);
		if (result != null && !result.isEmpty()) {
			HashMap<String, Object> hmResult = result.get(0);
			userType = Integer.parseInt((String) hmResult.get("has_right"));
		}
		return userType;
	}
	
	public String checkIRBUserRole(String personId) throws Exception {
		String role = null;
		ArrayList<HashMap<String, Object>> result = null;
		result = checkIRBUserRoles(personId);
		for (HashMap<String, Object> map : result){
			for (Entry<String, Object> entry : map.entrySet()){
				if(entry.getKey().equals("IS_IRB_ADMIN") && entry.getValue().equals("Y")){
					role = "ADMIN";
					return role;
				} else if(entry.getKey().equals("IS_IRB_CHAIR") && entry.getValue().equals("Y")){
					role = "CHAIR";
					return role;
				} else if(entry.getKey().equals("IS_IRB_REVIEWER") && entry.getValue().equals("Y")){
					role = "REVIEWER";
					return role;
				} else{
					role = "PI";
				}
			}
		}
		return role;
	}
	
	public ArrayList<HashMap<String, Object>> checkIRBUserRoles(String personId) {
		ArrayList<HashMap<String, Object>> result = null;
		ArrayList<InParameter> inputParam = new ArrayList<>();
		ArrayList<OutParameter> outputParam = new ArrayList<>();
		inputParam.add(new InParameter("PERSON_ID", DBEngineConstants.TYPE_STRING, personId));
		inputParam.add(new InParameter("USER_ID ", DBEngineConstants.TYPE_STRING, personId));
		outputParam.add(new OutParameter("resultset", DBEngineConstants.TYPE_RESULTSET));
		try {
			result = dbUtils.executeProcedure(inputParam, "GET_IRB_PERSON_ROLE", outputParam);
		} catch (DBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public LoginDao getLoginDao() {
		return loginDao;
	}

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}
}
