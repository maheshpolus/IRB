package  org.mit.irb.web.roles.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.roles.service.RoleManagementService;
import org.mit.irb.web.roles.vo.PersonSearchResult;
import org.mit.irb.web.roles.vo.RoleManagementVO;
import org.mit.irb.web.roles.vo.RoleVO;

@RestController
public class RoleManagementController {
	protected static Logger logger = Logger.getLogger(RoleManagementController.class.getName());

	@Autowired
	@Qualifier(value = "roleManagementService")
	private RoleManagementService roleManagementService;

	@RequestMapping(value = "/assignedRoleOfPerson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String assignedRoleOfPerson(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("getPersonId : " + vo.getPersonId());
		logger.info("getDepartmentId : " + vo.getUnitNumber());
		return roleManagementService.getAssignedRoleOfPerson(vo.getPersonId(),vo.getUnitNumber());
	}
	
	@RequestMapping(value = "/unassignedRoleOfPerson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String unassignedRoleOfPerson(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("getPersonId : " + vo.getPersonId());
		logger.info("getDepartmentId : " + vo.getUnitNumber());
		return roleManagementService.getUnAssignedRoleOfPerson(vo.getPersonId(),vo.getUnitNumber());
	}
	
	@RequestMapping(value = "/roleInformation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getRoleInformation(@RequestBody RoleVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("roleId : " + vo.getRoleId());		
		return roleManagementService.getRoleInformation(vo.getRoleId());
	}
	
	@RequestMapping(value = "/savePersonRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String savePersonRoles(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		return roleManagementService.savePersonRoles(vo);
	}

	@RequestMapping(value = "/fetchAllRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchAllRoles(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Fetch All Roles");	
		return roleManagementService.fetchAllRoles();
	}

	@RequestMapping(value = "/getAssignedRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getAssignedRole(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Fetch All Roles");	
		return roleManagementService.getAssignedRole(vo);
	}
	
	@RequestMapping(value = "/getUnitName", method = RequestMethod.POST)
	public String requestResearchSummaryData(@RequestBody RoleManagementVO vo, HttpServletRequest request) throws Exception {
		return  roleManagementService.getUnitName(vo.getUnitNumber());	 
	}
	
	@RequestMapping(value = "/findDepartment", method = RequestMethod.GET)
	public List<Unit> getDepartment(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for getDepartment");
		logger.info("searchString : " + searchString);
		return roleManagementService.getDepartmentList(searchString);
	}
	
	@RequestMapping(value = "/getPersonDetailById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getPersonDetailById(@RequestBody RoleManagementVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Request for getPersonDetailById");
		return roleManagementService.getPersonDetailById(vo);
	}
	
	@RequestMapping(value = "/syncPersonRole", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String syncPersonRole(HttpServletRequest request, HttpServletResponse response) {
		return roleManagementService.syncPersonRole();
	}
	
	@RequestMapping(value = "/findPersons", method = RequestMethod.GET)
	public List<PersonSearchResult> getNext(HttpServletRequest request, HttpServletResponse response, @RequestParam("searchString") String searchString) {
		logger.info("Requesting for findPersons");
		logger.info("searchString : " + searchString);
		return roleManagementService.findPerson(searchString);
	}
}
