package org.mit.irb.web.roles.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.collections4.ListUtils;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mit.irb.web.committee.pojo.Unit;
import org.mit.irb.web.committee.view.PersonDetailsView;
import org.mit.irb.web.common.constants.KeyConstants;
import org.mit.irb.web.roles.pojo.PersonRoles;
import org.mit.irb.web.roles.pojo.Rights;
import org.mit.irb.web.roles.pojo.Role;
import org.mit.irb.web.roles.pojo.RoleRights;
import org.mit.irb.web.roles.vo.PersonSearchResult;
import org.mit.irb.web.roles.vo.RoleManagementVO;
import org.mit.irb.web.roles.vo.RolesView;

@Transactional
@Service(value = "rolesManagement")
public class RolesManagementDaoImpl implements RolesManagementDao{

	protected static Logger logger = Logger.getLogger(RolesManagementDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public List<PersonRoles> getAssignedRoleOfPerson(String personId, String unitNumber) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<PersonRoles> query = builder.createQuery(PersonRoles.class);
		Root<PersonRoles> personRole = query.from(PersonRoles.class);
		Predicate predicate1 = builder.equal(personRole.get("personId"), personId);
		Predicate predicate2 = builder.equal(personRole.get("unitNumber"), unitNumber);
		query.where(builder.and(predicate1,predicate2));				
		List<PersonRoles> PersonRoles = session.createQuery(query).list();
		return PersonRoles;
		
	}
 
	@Override
	public List<Role> getUnAssignedRoleOfPerson(String personId, String unitNumber) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Role> outerQuery = builder.createQuery(Role.class);
		Root<Role> role = outerQuery.from(Role.class);	
		Subquery<PersonRoles> subQuery = outerQuery.subquery(PersonRoles.class);
		Root<PersonRoles> personRole = subQuery.from(PersonRoles.class);			
		Predicate predicate1 = builder.equal(personRole.get("personId"), personId);
		Predicate predicate2 = builder.equal(personRole.get("unitNumber"), unitNumber);	
		subQuery.select(personRole.get("roleId")).where(builder.and(predicate1,predicate2));			
		outerQuery.select(role).where(builder.in(role.get("roleId")).value(subQuery).not());
		List<Role> roles = session.createQuery(outerQuery).list();
		return roles;
	}

	@Override
	public Role getRoleInformation(Integer roleId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Role> query = builder.createQuery(Role.class);
		Root<Role> role = query.from(Role.class);
		Predicate predicate1 = builder.equal(role.get("roleId"), roleId);		
		query.where(builder.and(predicate1));				
		List<Role> roleList = session.createQuery(query).list();
		return roleList.get(0);
	}

	@Override
	public List<Rights> getAllRightsForRole(Integer roleId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Rights> outerQuery = builder.createQuery(Rights.class);
		Root<Rights> right = outerQuery.from(Rights.class);	
		Subquery<RoleRights> subQuery = outerQuery.subquery(RoleRights.class);
		Root<RoleRights> roleRight = subQuery.from(RoleRights.class);			
		Predicate predicate1 = builder.equal(roleRight.get("roleId"), roleId);		
		subQuery.select(roleRight.get("rightId")).where(builder.and(predicate1));			
		outerQuery.select(right).where(builder.in(right.get("rightId")).value(subQuery));
		List<Rights> rights = session.createQuery(outerQuery).list();
		return rights;	
	}

	@Override
	public Integer insertPersonRole(PersonRoles personRoles) {
		Serializable id = hibernateTemplate.save(personRoles);
		return Integer.parseInt(id.toString());
	}

	@Override
	public void updatePersonRole(PersonRoles personRoles) {
		hibernateTemplate.update(personRoles);		
	}

	@Override
	public void deletePersonRole(PersonRoles personRoles) {
		hibernateTemplate.delete(personRoles);
	}

	@Override
	public List<Role> fetchAllRoles() {
		return hibernateTemplate.loadAll(Role.class);
	}

	
	@Override
	public String getAssignedRole(RoleManagementVO vo) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query roleList = null;
		if (vo.getUnitNumber() != null && vo.getRoleId() != null) {
			roleList = session.createSQLQuery(KeyConstants.ROLE_WITH_UNIT_AND_ROLE);
			roleList.setParameter("unit_number", vo.getUnitNumber());
			roleList.setParameter("role_id", vo.getRoleId());
		} else if (vo.getUnitNumber() != null && vo.getRoleId() == null) {
			roleList = session.createSQLQuery(KeyConstants.ROLE_WITH_UNIT);
			roleList.setParameter("unit_number", vo.getUnitNumber());
		} else if (vo.getRoleId() != null && vo.getUnitNumber() == null) {
			roleList = session.createSQLQuery(KeyConstants.ROLE_WITH_ROLE);
			roleList.setParameter("role_id", vo.getRoleId());
		} else if(vo.getPersonId() != null && vo.getRoleId() == null && vo.getRoleId() == null) {
			roleList = session.createSQLQuery(KeyConstants.ROLES_OF_PERSON);
			roleList.setParameter("person_id", vo.getPersonId());
		} else {
			roleList = session.createSQLQuery(KeyConstants.FETCH_ALL_ROLES);
		}
		List<Object[]> roles = roleList.getResultList();
		List<RolesView> rolesViews = new ArrayList<RolesView>();
		for (Object[] role : roles) {
			RolesView rolesView = new RolesView();
			rolesView.setPersonId(role[0].toString());
			rolesView.setFullName(role[1].toString());
			rolesView.setEmail(role[2].toString());
			rolesView.setUserName(role[3].toString());
			rolesView.setUnitName(role[4].toString());
			rolesView.setUnitNumber(role[5].toString());
			rolesViews.add(rolesView);
		}
		return convertObjectToJSON(rolesViews);
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
		String unitName = "";
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			String hqlQuery = "select u.unitName from Unit u where u.unitNumber='"+unitNumber+"'";
			Query query =  session.createQuery(hqlQuery);
			unitName = (String) query.getSingleResult();
		}catch(Exception e) {				
		}
		return unitName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> getDepartmentList(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		Disjunction or = Restrictions.disjunction();
		or.add(Restrictions.like("unitName", "%" + searchString + "%").ignoreCase());
		or.add(Restrictions.like("unitNumber", "%" + searchString + "%").ignoreCase());
		criteria.add(or);
		criteria.addOrder(Order.asc("unitNumber"));
		criteria.setMaxResults(25);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Unit> units = criteria.list();
		List<Unit>  unitList = new ArrayList<>();
		if(units !=null && !units.isEmpty()) {
			for(Unit unitObject : units) {
				Unit unitObj = new Unit();
				unitObj.setUnitNumber(unitObject.getUnitNumber());
				unitObj.setUnitName(unitObject.getUnitName());
				unitList.add(unitObj);
			}
		}
		return unitList;	
	}

	@Override
	public PersonDetailsView getPersonDetailById(String personId) {
		PersonDetailsView person = hibernateTemplate.get(PersonDetailsView.class, personId);
		return person;
	}

	@Override
	public Object syncPersonRole() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		/*SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet resultSet = null;*/
		String result = "data refreshed";
		try {
			/*if (oracledb.equalsIgnoreCase("N")) {
				statement = connection.prepareCall("{call REFRESH_PERSON_ROLE_RT_MV ()}");
				statement.execute();
				resultSet = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "REFRESH_PERSON_ROLE_RT_MV ";
				String functionCall = "{call " + procedureName + "(?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.execute();
				resultSet = (ResultSet) statement.getObject(1);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<PersonSearchResult> findPerson(String searchString) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		final String likeCriteria = "%" + searchString.toUpperCase() + "%";
		@SuppressWarnings("unchecked")
		org.hibernate.query.Query<PersonSearchResult> query = session.createQuery(
				"SELECT NEW com.polus.fibicomp.person.vo.PersonSearchResult(t.personId, t.fullName) " + "FROM Person t "
						+ "WHERE UPPER(t.personId) like :likeCriteria OR UPPER(t.fullName) like :likeCriteria");
		query.setParameter("likeCriteria", likeCriteria);
		return ListUtils.emptyIfNull(query.setMaxResults(25).list());
	}
}
