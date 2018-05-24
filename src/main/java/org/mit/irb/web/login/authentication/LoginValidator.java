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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import org.mit.irb.web.common.dto.*;

import org.mit.irb.web.common.utils.*;
import org.mit.irb.web.controller.BaseController;
import org.mit.irb.web.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginValidator extends BaseController{
@Autowired LoginService loginService;
	protected static Logger logger = Logger.getLogger(LoginValidator.class.getName());
	Statement statement = null;
	ResultSet resultSet = null;
	DBEngine dbUtils = new DBEngine();
	public LoginValidator(){
	}

	//login credentials checking and return boolean value for DB user 
	public Boolean loginCheck(String loginMode,String userName,String password,HttpServletRequest request,HttpServletResponse response) throws Exception {
		boolean isLoginSuccess = false;
		if("USERID".equalsIgnoreCase(loginMode)){			
			isLoginSuccess = new UserIdAuthentication().Authenticate(userName, password);
			
		}
		else if("TouchStone".equalsIgnoreCase(loginMode)){
			isLoginSuccess = new TouchstoneAuthService().authenticate(request, request.getSession());
			
		}		
		
		if(isLoginSuccess){
			HttpSession session = request.getSession();
			PersonDTO personDTO = readPersonData(userName);
			session.setAttribute("personDTO"+session.getId(), personDTO);
			session.setAttribute("user"+session.getId(), personDTO.getFullName());				
			//setting session to expiry in 30 mins
			int timeoutValue = Integer.parseInt(IRBProperties.getProperty("SESSION_TIMEOUT_DEFAULT"));
			session.setMaxInactiveInterval(timeoutValue*60);
			return true;
		}
		return false;
	}
	
	//fetching person data
		public PersonDTO readPersonData(String userName) throws DBException, IOException{
			String procedureCall = null;
			PersonDTO personDTO = new PersonDTO();
			CallableStatement callableStatement = null;
			ResultSet resultSet = null;
			if(dbUtils != null){
				Connection connection = dbUtils.beginTxn();
				try{
					try{
						procedureCall = "{call GET_AB_PERSON_DETAILS(?, ?)}";
						callableStatement = connection.prepareCall(procedureCall);

						callableStatement.setString(1, userName);
						callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

						//Call Stored Procedure
						callableStatement.executeUpdate();
						// get cursor and cast it to ResultSet
						resultSet = (ResultSet) callableStatement.getObject(2);

						while (resultSet.next()) {
							personDTO.setPersonID(resultSet.getString(1));
							personDTO.setFirstName(resultSet.getString(2));
							personDTO.setHasDual(resultSet.getString(3));
							personDTO.setLastName(resultSet.getString(4));
							personDTO.setFullName(resultSet.getString(5));
							personDTO.setEmail(resultSet.getString(6));
							personDTO.setUnitNumber(resultSet.getString(7));
							personDTO.setUnitName(resultSet.getString(8));
							personDTO.setPhoneNumber(resultSet.getString(9));
							personDTO.setUserName(userName);
							Integer userType = loginService.checkUserType(personDTO.getPersonID(),"");
							personDTO.setUserRoleType(userType);
							try {
								String role = loginService.checkIRBUserRole(userName);
								personDTO.setRole(role);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					} catch(SQLException e) {
						logger.error("Error in readPersonData method.", e);
					} 
					finally{
						close(resultSet, callableStatement);
					}
					
				}
				finally{
					dbUtils.endTxn(connection);
				}
			}
			return personDTO;
		}

}
