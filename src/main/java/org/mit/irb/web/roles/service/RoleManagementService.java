package org.mit.irb.web.roles.service;

import java.util.List;

import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.roles.vo.PersonSearchResult;
import org.mit.irb.web.roles.vo.RoleManagementVO;

public interface RoleManagementService {


	/**
	 * This method will fetch all assigned roles of a person
	 * @param String - Person Id
	 * @param String - Unit Number
	 * @return String - JSON String.
	 */
	String getAssignedRoleOfPerson(String personId,String unitNumber);
	
	/**
	 * This method will fetch all unassigned roles of a person
	 * @param String - Person Id
	 * @param String - Unit Number
	 * @return String - JSON String.
	 */
	String getUnAssignedRoleOfPerson(String personId,String unitNumber);
	
	/**
	 * This method return all the information of a Role
	 * @param Integer - Role Id
	 * @return String - JSON String.
	 */
	String getRoleInformation(Integer roleId);

	/**
	 * This method return assignee/update/delete role to a person
	 * @param RoleManagementVO - Role information VO
	 * @return String - JSON String.
	 */
	String savePersonRoles(RoleManagementVO vo);

	/**
	 * This method return fetchAllRoles
	 * @return String - JSON String.
	 */

	public String fetchAllRoles();

	/**
	 * This method return fetch Assigned Role based on criteria
	 * @param RoleManagementVO - Role information VO
	 * @return String - JSON String.
	 */

	public String getAssignedRole(RoleManagementVO vo);

	/**
	 * @param unitNumber
	 * @return
	 */
	String getUnitName(String unitNumber);

	List<Unit> getDepartmentList(String searchString);

	String getPersonDetailById(RoleManagementVO vo);

	String syncPersonRole();

	List<PersonSearchResult> findPerson(String searchString);
	
	
}
