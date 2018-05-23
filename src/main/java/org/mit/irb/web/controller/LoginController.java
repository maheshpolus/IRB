package org.mit.irb.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mit.irb.web.common.VO.CommonVO;
import org.mit.irb.web.common.dto.PersonDTO;
import org.mit.irb.web.common.pojo.DashboardProfile;
import org.mit.irb.web.common.utils.DBEngine;
import org.mit.irb.web.common.utils.DBEngineConstants;
import org.mit.irb.web.common.utils.DBException;
import org.mit.irb.web.common.utils.InParameter;
import org.mit.irb.web.common.utils.OutParameter;
import org.mit.irb.web.login.authentication.LoginValidator;
import org.mit.irb.web.login.authentication.TouchstoneAuthService;
import org.mit.irb.web.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class LoginController extends BaseController {
	
	@Autowired
	LoginValidator loginValidator;
	
	@Autowired
	@Qualifier(value="loginService")
	LoginService loginService;
	
	@Value("${LOGIN_MODE}")
	private String login_mode;
	
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> loginUser(@RequestBody CommonVO vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("Received request for login: ");
		HttpStatus status = HttpStatus.OK;
		String userName = vo.getUsername();
		String password = vo.getPassword();
		String loginMode = "USERID";
		Boolean loginCheck = false;
		String role = null;
		try {
			loginCheck = loginValidator.loginCheck(loginMode, userName, password, request, response);
			if (loginCheck) {
				role = loginService.checkIRBUserRole(userName);
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseData = null;
		if (role != null && !role.isEmpty()) {
			PersonDTO personDTO = loginValidator.readPersonData(userName);
			personDTO.setRole(role);
			responseData = mapper.writeValueAsString(personDTO);
		}
		return new ResponseEntity<String>(responseData, status);
	}

	@RequestMapping(value = {"/","/login"}, method = RequestMethod.GET)
	public String entryPage(HttpServletRequest request, HttpSession session) throws DBException, IOException {
		logger.info("Inside  EntryPage");
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO" + session.getId());
		if (personDTO != null) {
			logger.info("personDTO != null");
			return "index.html";

		} else {
			if ("TOUCHSTONE".equalsIgnoreCase(login_mode)) {
				Boolean isLoginSuccess = TouchstoneAuthService.authenticate(request, session);
				logger.info("in touchstone  isLoginSuccess " +isLoginSuccess);
				if (isLoginSuccess) {
					return "index.html";
				}
			}
		}
		return "index.html";
	}
	
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public ResponseEntity<String> getPersonDetails(HttpServletRequest request){
		String responseData = null;
		HttpStatus status = HttpStatus.OK;
		PersonDTO personDTO = null;
		HttpSession session = request.getSession();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(session != null){
				personDTO = (PersonDTO) session.getAttribute("personDTO"+session.getId());
			}
			responseData = mapper.writeValueAsString(personDTO);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<String>(responseData, status);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("Log Out");
		// invalidate the session if exists
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login";
	}
}
