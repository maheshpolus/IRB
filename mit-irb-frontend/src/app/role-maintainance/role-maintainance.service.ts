import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
// import { CommonService } from '../common/services/common.service';

@Injectable()
export class RoleMaintainanceService {

  constructor( private _http: HttpClient) {}

  getUnAssignedList(role) {
    return this._http.post('/irb/unassignedRoleOfPerson', role );
  }
  getAssignedList(role) {
    return this._http.post('/irb/assignedRoleOfPerson', role );
  }
  getUnitName(unitId) {
    return this._http.post('/irb/getUnitName', {'unitNumber': unitId});
  }
  assignRoles(assignedList) {
    return this._http.post('/irb/savePersonRoles', assignedList);
  }
  viewRoleOverview(roleId) {
    return this._http.post('/irb/roleInformation',  {'roleId': roleId});
  }
  syncRoles() {
    return this._http.get('/irb/syncPersonRole');
  }
  getPersonData(personId) {
    return this._http.post('/irb/getPersonDetailById', {'personId': personId});
  }
  fetchPersons( searchString) {
    return this._http.get('/irb/findPersons' + '?searchString=' + searchString);
  }
  fetchKeyUnit( searchString) {
    return this._http.get('/irb/findDepartment' + '?searchString=' + searchString);
  }
  fetchRoles() {
    return this._http.post('/irb/fetchAllRoles', { });
  }
  getAssignedRoleLists(params) {
    return this._http.post('/irb/getAssignedRole', params);
  }
}
