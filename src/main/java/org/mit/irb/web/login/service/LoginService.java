package org.mit.irb.web.login.service;

import java.io.IOException;
import java.sql.SQLException;

import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.utils.DBException;

public interface LoginService {
	
	
	/**
	 * @param personId : person ID
	 * @param unitNumber : Home Unit number
	 * @return true if person is CA , false if reporter
	 * @throws DBException
	 * @throws IOException
	 * @throws SQLException
	 */
	Boolean checkUserHasRight(String personId,String unitNumber) throws DBException, IOException, SQLException;
	
	/**
	 * @param personId : person ID
	 * @param unitNumber : Home Unit number
	 * @return true if person is IRB Admin , false if requester
	 * @throws Exception
	 */
	String checkIRBUserRole(String personId) throws Exception;
	
	/**
	 * @param personID
	 * @return full details of a person 
	 * @throws Exception
	 */
	PersonDTO getPersonDetails(String personID) throws Exception;
	
	/**
	 * @param personId : person ID
	 * @param unitNumber : Home Unit number
	 * @return type of user like OSP admin,OST department admin or MIT employee
	 * If return = 1 then OSP Admin
	 * If return = 2 then OST Dept. Admin
	 * If return = 3 then MIT employee 
	 * @throws DBException
	 * @throws IOException
	 * @throws SQLException
	 */
	Integer checkUserType(String personId,String unitNumber) throws DBException, IOException, SQLException;
}
