package org.mit.irb.web.login.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.utils.DBException;

/**
 * Authenticate user using on Touchstone Authentication  
 * 
 */
public class TouchstoneAuthService{
    protected static Logger logger = Logger.getLogger(TouchstoneAuthService.class.getName());
    
    
	/**
	 * It fetches the user email from request,and create substring from email(
	 * from starting position to "@") and return true if the userId is in person table.
	 * 
	 */
    public static boolean authenticate(HttpServletRequest request,HttpSession session) throws DBException, IOException   {
        String remoteUser = request.getRemoteUser();        
        if(remoteUser == null){
        	remoteUser = (request.getUserPrincipal() != null ? request.getUserPrincipal().getName(): null);
        }
        String kerbEmail = remoteUser;
        String userId = null;
        logger.debug( "Starting Touchstone validation...");
        logger.debug( "Getting user from Request (email)===>"+kerbEmail);
        logger.info("In Touchstone ,Getting user from Request (email)===>"+kerbEmail);
        if(kerbEmail!=null) {
            userId = kerbEmail.substring(0,kerbEmail.lastIndexOf("@"));
            LoginValidator validator = new LoginValidator();
            PersonDTO pdto = validator.readPersonData(userId);        
            if(pdto.getPersonID() !=null){
				session.setAttribute("personDTO"+session.getId(), pdto);
				session.setAttribute("user"+session.getId(), pdto.getFullName());
				logger.debug( "Login success...");
            	 return true;
            }
            return false;
           
        } else {           
            return false ;
        }
    }
}
