import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
// import { CommonService } from '../common/services/common.service';

@Injectable()
export class RoleMaintainanceService {

  constructor( private _http: HttpClient) {}

  getUnAssignedList(role) {
    return this._http.post('/mit-irb/unassignedRoleOfPerson', role );
  }
  getAssignedList(role) {
    return this._http.post('/mit-irb/assignedRoleOfPerson', role );
  }
  getUnitName(unitId) {
    return this._http.post('/mit-irb/getUnitName', {'unitNumber': unitId});
  }
  assignRoles(assignedList) {
    return this._http.post('/mit-irb/savePersonRoles', assignedList);
  }
  viewRoleOverview(roleId) {
    return this._http.post('/mit-irb/roleInformation',  {'roleId': roleId});
  }
  syncRoles() {
    return this._http.get('/mit-irb/syncPersonRole');
  }
  getPersonData(personId) {
    return this._http.post('/mit-irb/getPersonDetailById', {'personId': personId});
  }
  fetchPersons( searchString) {
    return this._http.get('/mit-irb/findPersons' + '?searchString=' + searchString);
  }
  fetchKeyUnit( searchString) {
    return this._http.get('/mit-irb/findDepartment' + '?searchString=' + searchString);
  }
  fetchRoles() {
    return this._http.post('/mit-irb/fetchAllRoles', { });
  }
  getAssignedRoleLists(params) {
    return this._http.post('/mit-irb/getAssignedRole', params);
  }
}
