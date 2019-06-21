package org.mit.irb.web.roles.dao;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.committee.view.PersonDetailsView;
import org.mit.irb.web.roles.pojo.PersonRoles;
import org.mit.irb.web.roles.pojo.Rights;
import org.mit.irb.web.roles.pojo.Role;
import org.mit.irb.web.roles.vo.PersonSearchResult;
import org.mit.irb.web.roles.vo.RoleManagementVO;

@Transactional
@Service
public interface RolesManagementDao {

	
	List<PersonRoles> getAssignedRoleOfPerson(String personId,String unitNumber);
	
	List<Role> getUnAssignedRoleOfPerson(String personId,String unitNumber);
	
	Role getRoleInformation(Integer roleId);
	
	List<Rights> getAllRightsForRole(Integer roleId);
	
	Integer insertPersonRole(PersonRoles personRoles);
	
	void updatePersonRole(PersonRoles personRoles);
	
	void deletePersonRole(PersonRoles personRoles);

	public List<Role> fetchAllRoles();

	public String getAssignedRole(RoleManagementVO vo);

	String getUnitName(String unitNumber);

	List<Unit> getDepartmentList(String searchString);

	PersonDetailsView getPersonDetailById(String personId);

	Object syncPersonRole();

	List<PersonSearchResult> findPerson(String searchString);

}


