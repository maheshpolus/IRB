package org.mit.irb.web.roles.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.committee.view.PersonDetailsView;//com.polus.fibicomp.person.pojo.Person;
import org.mit.irb.web.roles.dao.RolesManagementDao;
import org.mit.irb.web.roles.pojo.PersonRoles;
import org.mit.irb.web.roles.pojo.Rights;
import org.mit.irb.web.roles.pojo.Role;
import org.mit.irb.web.roles.vo.PersonSearchResult;
import org.mit.irb.web.roles.vo.RoleManagementVO;
import org.mit.irb.web.roles.vo.RoleRightsVO;
import org.mit.irb.web.roles.vo.RoleVO;

@Transactional
@Service(value = "roleManagementService")
public class RoleManagementServiceImpl implements RoleManagementService{

	protected static Logger logger = Logger.getLogger(RoleManagementServiceImpl.class.getName());

	@Autowired
	private RolesManagementDao rolesManagementDao;
	
	@Override
	public String getAssignedRoleOfPerson(String personId, String unitNumber) {
		List<RoleVO> roleList = new ArrayList<>();		
		List<PersonRoles> personRolesList = rolesManagementDao.getAssignedRoleOfPerson(personId, unitNumber);	
		for(PersonRoles personRole : personRolesList) {
			RoleVO roleVO = new RoleVO();			
			roleVO.setPersonRoleId(personRole.getPersonRoleId());
			roleVO.setRoleId(personRole.getRoleId());
			roleVO.setRoleName(personRole.getRole().getRoleName());
			roleVO.setDescentFlag(personRole.getDescentFlag()); 
			roleList.add(roleVO);
		}
		String response = convertObjectToJSON(roleList);
		return response;
	}

	@Override
	public String getUnAssignedRoleOfPerson(String personId, String unitNumber) {
		List<RoleVO> roleList = new ArrayList<>();		
		List<Role> prolesList = rolesManagementDao.getUnAssignedRoleOfPerson(personId, unitNumber);	
		for(Role role : prolesList) {
			RoleVO roleVO = new RoleVO();			
			roleVO.setRoleId(role.getRoleId());
			roleVO.setRoleName(role.getRoleName());
			roleVO.setDescentFlag("N");
			roleList.add(roleVO);
		}	
		String response = convertObjectToJSON(roleList);
		return response;
	}

	@Override
	public String getRoleInformation(Integer roleId) {		
		Role roleInfo = rolesManagementDao.getRoleInformation(roleId);			
		List<Rights> rightsForRole = rolesManagementDao.getAllRightsForRole(roleId);		
		RoleRightsVO roleRightsVO = new RoleRightsVO();
		roleRightsVO.setRoleId(roleId);
		roleRightsVO.setRoleName(roleInfo.getRoleName());
		roleRightsVO.setRights(rightsForRole);
		String response = convertObjectToJSON(roleRightsVO);
		return response;		
	}

	@Override
	public String savePersonRoles(RoleManagementVO vo) {
		List<RoleVO> roleList = vo.getRoles();		
		//List<RoleVO> updatedList = new ArrayList<>();		
		for(RoleVO role : roleList) {			
			PersonRoles personRole =  new PersonRoles();
			personRole.setPersonId(vo.getPersonId());
			personRole.setUnitNumber(vo.getUnitNumber());
			personRole.setUpdateUser(vo.getUpdateUser());
			personRole.setUpdateTimeStamp(new Date());
			personRole.setDescentFlag(role.getDescentFlag());
			personRole.setRoleId(role.getRoleId());			
			if("I".equals(role.getAcType())) {				
				Integer id = rolesManagementDao.insertPersonRole(personRole);
				role.setPersonRoleId(id);
			}else if("U".equals(role.getAcType())) {
				personRole.setPersonRoleId(role.getPersonRoleId());
				rolesManagementDao.updatePersonRole(personRole);				
			}else if("D".equals(role.getAcType())) {
				personRole.setPersonRoleId(role.getPersonRoleId());
				rolesManagementDao.deletePersonRole(personRole);					
			}
			role.setAcType("U");
			//updatedList.add(role);						
		}		
		String response = convertObjectToJSON(roleList);
		return response;	
	}

	@Override
	public String fetchAllRoles() {
		List<Role> role = rolesManagementDao.fetchAllRoles();
		return convertObjectToJSON(role);
	}

	@Override
	public String getAssignedRole(RoleManagementVO vo) {
		return rolesManagementDao.getAssignedRole(vo);
	}
	
	public String convertObjectToJSON(Object object) {
		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			response = mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public String getUnitName(String unitNumber) {
		String unitName = rolesManagementDao.getUnitName(unitNumber);
		return convertObjectToJSON(unitName);
	}

	@Override
	public List<Unit> getDepartmentList(String searchString) {
		return rolesManagementDao.getDepartmentList(searchString);

	}

	@Override
	public String getPersonDetailById(RoleManagementVO vo) {
		String personId = vo.getPersonId();
		PersonDetailsView person = rolesManagementDao.getPersonDetailById(personId);
		vo.setPerson(person);
		String response = convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String syncPersonRole() {
		return convertObjectToJSON(rolesManagementDao.syncPersonRole());
	}

	@Override
	public List<PersonSearchResult> findPerson(String searchString) {
		return rolesManagementDao.findPerson(searchString);
	}
}
