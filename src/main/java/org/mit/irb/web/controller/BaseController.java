package org.mit.irb.web.controller;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
/*import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;*/

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.utils.DBEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;


/**
 * The main controller file for all controllers, 
 * It deals with connection creation,close,result set management etc.. *
 */
public class BaseController{
	
	protected static Logger logger = Logger.getLogger(BaseController.class
			.getName());
	
	DBEngine dbUtils = new DBEngine();
	protected Connection connection = null;
	
	@Autowired
	ServletContext servletContext = null;
	
	public static void close(ResultSet resultSet,
			CallableStatement callableStatement, Connection connection) {
		if (resultSet != null) {
			try {
				resultSet.close();

			} catch (SQLException e) {
				logger.error("The result set cannot be closed.", e);
			}
		}
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.error("The statement cannot be closed.", e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("The data source connection cannot be closed.", e);
			}
		}
	}

	public static void close(CallableStatement callableStatement,
			Connection connection) {

		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.error("The statement cannot be closed.", e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("The data source connection cannot be closed.", e);
			}
		}
	}

	public static void close(ResultSet resultSet,
			CallableStatement callableStatement) {
		if (resultSet != null) {
			try {
				resultSet.close();

			} catch (SQLException e) {
				logger.error("The result set cannot be closed.", e);
			}
		}
		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.error("The statement cannot be closed.", e);
			}
		}
	}

	public static void close(CallableStatement callableStatement) {

		if (callableStatement != null) {
			try {
				callableStatement.close();
			} catch (SQLException e) {
				logger.error("The statement cannot be closed.", e);
			}
		}
	}
	
	protected void setRequestURL(HttpServletRequest request){
	 String reqURI = request.getRequestURI();
     reqURI = reqURI.substring(request.getContextPath().length());
     String reqQS = request.getQueryString();
     if(reqQS!=null && !reqQS.trim().equals("")){
         reqURI+=("?"+reqQS);
     }
     HttpSession session = request.getSession();
     session.setAttribute(KeyConstants.REQUESTED_URL+session.getId(),reqURI);			
		}
		
	protected String getRequestedURL(HttpServletRequest request){
			try{
				HttpSession session = request.getSession();
				String reqURI = (String) session.getAttribute(KeyConstants.REQUESTED_URL+session.getId());
				return reqURI;
			}catch(Exception e){
				return "/";
			}
		
	}	
	protected Model updatePersonInfo(HttpSession session, Model model) {
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO"+session.getId());
		if(personDTO != null){
			model.addAttribute("name", personDTO.getUserName());
			model.addAttribute("fullName", personDTO.getFullName());
			model.addAttribute("roleNumber", personDTO.getRoleNumber());
			model.addAttribute("createPermNo", personDTO.getCreateNo());
			model.addAttribute("personID", personDTO.getPersonID());
		}else{
			model = null;
		}
		return model;
		
	}
	
	protected boolean isPersonAlive(HttpSession session) {
		return session.getAttribute("personDTO"+session.getId()) != null;		 
	}
	
}