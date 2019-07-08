package org.mit.irb.web.login.authentication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.common.utils.DBEngineConstants;


public class UserIdAuthentication {
	protected static Logger logger = Logger.getLogger(UserIdAuthentication.class.getName());
	private DBEngine dbEngine;

    public UserIdAuthentication(){
    	dbEngine = new DBEngine();
    }
	public boolean Authenticate(String userName,String password) throws ClassNotFoundException, DBException, IOException, SQLException{	
		boolean isLoginSuccess = false;
		try{		
			ArrayList<InParameter> inParam = new ArrayList<InParameter>();	
			ArrayList<OutParameter> outParam = new ArrayList<OutParameter>();
			inParam.add(new InParameter("av_prncpl_id", DBEngineConstants.TYPE_STRING,userName));
			inParam.add(new InParameter("av_prncpl_pswd", DBEngineConstants.TYPE_STRING,password));
			outParam.add(new OutParameter("has_right",DBEngineConstants.TYPE_INTEGER));
			ArrayList<HashMap<String,Object>> result = 
					dbEngine.executeFunction(inParam,"fn_ab_verify_login",outParam);
			if(result != null && !result.isEmpty()){
				HashMap<String,Object> hmResult = result.get(0);
				if( Integer.parseInt((String)hmResult.get("has_right")) == 1){
					isLoginSuccess= true;
				}
			}			
		}
		catch(SQLException e){
			logger.debug("Sql Exception: " + e);			
		}	
		return isLoginSuccess;
	}	
}
