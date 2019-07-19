import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { RoleMaintainanceService } from './role-maintainance.service';
import * as _ from 'lodash';
import { PiElasticService } from '../common/service/pi-elastic.service';

@Component({
  selector: 'app-role-maintainance',
  templateUrl: './role-maintainance.component.html',
  styleUrls: ['./role-maintainance.component.css']
})
export class RoleMaintainanceComponent implements OnInit {
  
  userDTO: any;
  unitId: any;
  personId: any;
  person: any = {};
  elasticSearchOptions: any = {};
  personDetails: any = {};
  clearField;
  personName = '';
  roleVisibleId: number;
  isPerson: boolean;
  unAssignedList: any;
  draggedElement = null;
  assignedList: any;
  roleDetails: any = {
    personId: '',
    unitNumber: ''
  };
  viewPerson: any = [];
  isViewMode = true;
  tempLeadUnits: any[];
  tempAssignedList: any;
  tempUnAssignedList: any;
  checkall: boolean;
  allcheck: boolean;
  assignedDetails: any = {
    personId: '',
    unitNumber: '',
    updateUser: '',
    roles: []
  };
  roleOverview: any;
  unitName: any;
  toast_message: any;
  debounceTimer: any;
  searchResults: any = [];
  isSearchPersonActive = false;
  isSearchRoleActive = false;
  unitResults: any = [];
  roleName: any;
  roleResults: any = [];
  tempRoleResultList: any;
  personRoleName = '';
  personUnitName = '';
  isUnitSearchActive = false;
  roleSearchObject = {
    unitNumber: null,
    roleId: null,
    personId: null
  };
  personRoleUnitList: any;
  personRoleLists: any;
  isRoleList = true;
  isRoleUnitDisable = false;

  constructor(private _router: ActivatedRoute, private _elasticsearchService: PiElasticService,
    private _roleService: RoleMaintainanceService, ) { }

  ngOnInit() {
    this.userDTO = JSON.parse(localStorage.getItem('currentUser'));
    this._router.queryParams.subscribe(params => {
      this.unitId = this.roleDetails.unitNumber = this.assignedDetails.unitNumber = '000001';
      this.roleSearchObject.unitNumber = this.unitId;
      this.personId = params['personId'];
    });
    if (this.personId) {
      this._roleService.getPersonData(this.personId).subscribe((data: any) => {
        if (data) {
          this.person = data.person;
          this.isPerson = true;
          this.isRoleList = false;
          this.personDetails.personName = this.person.fullName;
          this.personName = this.person.fullName;
          this.elasticSearchOptions.defaultValue = this.personName;
          this.elasticSearchOptions = Object.assign({}, this.elasticSearchOptions);
          this.personDetails.personId = this.roleDetails.personId = this.assignedDetails.personId = this.person.personId;
          this.personDetails.user_name = this.person.principalName;
          this.personDetails.email_id = this.person.emailAddress;
          if (this.person.unit) {
            this.personDetails.unit_name = this.person.unit.unitName;
            this.personDetails.unit_number = this.person.unit.unitNumber;
          }
          this.assignedAndUnAssigned();
        }
      });
    }
    this.assignedDetails.updateUser = this.userDTO.userName;
    this.elasticSearchOptions.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.elasticSearchOptions.index = this._elasticsearchService.IRB_INDEX;
    this.elasticSearchOptions.type = 'person';
    this.elasticSearchOptions.size = 20;
    this.elasticSearchOptions.contextField = 'full_name';
    this.elasticSearchOptions.debounceTime = 500;
    this.elasticSearchOptions.formatString = 'full_name | email_address | home_unit | person_id';
    this.elasticSearchOptions.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit: {},
      person_id: {}
    };
    this._roleService.getUnitName(this.unitId).subscribe(
      (data: any) => {
        this.unitName = data;
        this.personUnitName = this.unitName;
        (document.getElementsByClassName('app-elastic-search')[0] as HTMLElement).focus();
      });
    this.roleUnitSearch();
    this._roleService.fetchRoles().subscribe(data => {
      this.roleResults = data;
    });
  }

  /**
   * Get assigned and Unassigned list of a selected person
   */
  assignedAndUnAssigned() {
    this._roleService.getUnAssignedList(this.roleDetails).subscribe(
      (data: any) => {
        this.unAssignedList = data;
        this.tempUnAssignedList = this.unAssignedList;
      });
    this._roleService.getAssignedList(this.roleDetails).subscribe(
      (data: any) => {
        this.assignedList = data;
        this.tempAssignedList = this.assignedList;
      });
  }
  /**
   * @param  {} value
   * @param  {} index
   * @param  {} administrator
   * select a result from elastic search for persons
   */
  selectUserElasticResult(result) {
    if (result === null) {
      this.isPerson = false;
      this.isRoleList = true;
      this.roleSearchObject.personId = null;
    } else {
      this.personUnitName = null;
      this.roleSearchObject.unitNumber = null;
      this.personRoleName = null;
      this.roleSearchObject.roleId = null;
      this.isRoleUnitDisable = true;
      this.personDetails.personName = result.full_name;
      this.personDetails.personId = this.roleSearchObject.personId =
      this.roleDetails.personId = this.assignedDetails.personId = result.person_id;
      this.personDetails.user_name = result.user_name;
      this.personDetails.email_id = result.email_address;
      this.personDetails.unit_name = result.unit_name;
      this.personDetails.unit_number = result.home_unit;
      this.assignedAndUnAssigned();
    }
  }

  /**
   * move selected list from assigned to unassigned and vice versa
   */
  moveunAssignedList() {
    const SELECTEDLIST = _.remove(this.tempUnAssignedList, { 'selected': true });
    _.remove(this.unAssignedList, { 'selected': true });
    SELECTEDLIST.forEach((e: any) => {
      delete e.selected;
      e.acType = 'I';
    });
    this.assignedDetails.roles = SELECTEDLIST;
    this._roleService.assignRoles(this.assignedDetails).subscribe(
      (data: any) => {
        this.tempAssignedList = this.assignedList.concat(data);
        this.assignedList = this.tempAssignedList;
      });
  }
  moveassignedList() {
    const SELECTEDLIST = _.remove(this.tempAssignedList, { 'selected': true });
    _.remove(this.assignedList, { 'selected': true });
    SELECTEDLIST.forEach((e: any) => {
      delete e.selected;
      e.acType = 'D';
    });
    this.assignedDetails.roles = SELECTEDLIST;
    this._roleService.assignRoles(this.assignedDetails).subscribe(
      (data: any) => {
        this.tempUnAssignedList = this.unAssignedList.concat(data);
        this.unAssignedList = this.tempUnAssignedList;
      });
  }

  /**
   * @param  {} list
  * Save assigned roles of a selected person
  */
  setDescentType(list) {
    this.assignedDetails.roles = [];
    list.acType = 'U';
    (list.descentFlag === 'N') ? list.descentFlag = 'Y' : list.descentFlag = 'N';
    // list.descentFlag = 'Y';
    this.assignedDetails.roles.push(list);
    this._roleService.assignRoles(this.assignedDetails).subscribe(
      (data: any) => {
      });
  }
  /**
   * @param  {} searchText
   * filters the assigned and unassigned list with respect to the input entered
   */
  filterAssignedUnits(searchText) {
    this.tempAssignedList = this.assignedList;
    if (searchText !== '') {
      this.tempAssignedList = this.tempAssignedList.filter(v => {
        return v.roleName.toLowerCase().includes(searchText.toLowerCase())
          || v.roleName.toLowerCase().includes(searchText.toLowerCase());
      });
    } else {
      this.tempAssignedList = this.assignedList;
    }
  }
  filterUnassignedUnits(searchText) {
    this.tempUnAssignedList = this.unAssignedList;
    if (searchText !== '') {
      this.tempUnAssignedList = this.tempUnAssignedList.filter(v => {
        return v.roleName.toLowerCase().includes(searchText.toLowerCase())
          || v.roleName.toLowerCase().includes(searchText.toLowerCase());
      });
    } else {
      this.tempUnAssignedList = this.unAssignedList;
    }
  }
  /**
   * @param  {} check
   * Select all functionality of assigned and unassigned list
   */
  selectAllUnAssigned(check) {
    this.tempUnAssignedList.forEach(element => {
      element.selected = check;
    });
  }
  selectAllAssigned(check) {
    this.tempAssignedList.forEach(element => {
      element.selected = check;
    });
  }
  /**
   * @param  {} roleId
   * View details including rights of a selected role
   */
  viewRoleDetails(roleId) {
    this._roleService.viewRoleOverview(roleId).subscribe(
      (data: any) => {
        this.roleOverview = data;
      });
  }
  syncRoles() {
    this._roleService.syncRoles().subscribe(
      (data: any) => {
        if (data) {
          const toastId = document.getElementById('toast-success');
          this.toast_message = 'Successfully synced the roles';
          toastId.className = 'show';
          setTimeout(() => {
            toastId.className = toastId.className.replace('show', '');
          }, 2000);
        }
      });
  }
  /**
* Get search data for home unit list
*/
  fetchUnit(selectedString) {
     // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.roleSearchObject.personId = null;
    if (selectedString !== '') {
      clearTimeout(this.debounceTimer);
      this.debounceTimer = setTimeout(() => {
        this._roleService.fetchKeyUnit(selectedString).subscribe(data => {
          this.unitResults = data;
        });
      }, 500);
    } else {
      this.unitResults = [];
    }
  }
  roleUnitSearch() {
    this.personRoleUnitList = [];
    this.viewPerson = [];
    this._roleService.getAssignedRoleLists(this.roleSearchObject).subscribe(data => {
      this.personRoleUnitList = data;
      this.personRoleUnitList.forEach(element => {
        this.viewPerson.push(false);
      });
    });
    this.isRoleUnitDisable = false;
  }

  findRoles(personRoleName) {
    this.roleSearchObject.personId = null;
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.isSearchRoleActive = true;
    this.roleSearchObject.roleId = null;
    this.tempRoleResultList = this.roleResults;
    if (personRoleName !== '') {
      this.tempRoleResultList = this.tempRoleResultList.filter(v => {
        return v.roleName.toLowerCase().includes(personRoleName.toLowerCase())
          || v.roleName.toLowerCase().includes(personRoleName.toLowerCase());
      });
    } else {
      this.tempRoleResultList = this.roleResults;
    }
  }
  viewPersonRoles(personId, unitNumber) {
    this.roleVisibleId = personId;
    this.personRoleLists = [];
    this.roleDetails.personId = personId;
    this._roleService.getAssignedList(this.roleDetails).subscribe(
      (data: any) => {
        this.personRoleLists = data;
      });
  }
  setPersonIndex(i) {
    const temp = this.viewPerson[i];
    this.viewPerson = [];
    temp === undefined ? this.viewPerson[i] = true : this.viewPerson[i] = !temp;
}
editPersonRoles(person) {
  this.roleDetails.personId = this.personDetails.personId = this.assignedDetails.personId = person.personId;
  this.roleDetails.unitNumber = person.unitNumber;
  this.personDetails.personName = person.fullName;
  this.personDetails.user_name = person.userName;
  this.personDetails.email_id = person.email;
  this.personDetails.unit_name = person.unitName;
  this.personDetails.unit_number = person.unitNumber;
  this._roleService.getUnAssignedList(this.roleDetails).subscribe(
    (data: any) => {
      this.unAssignedList = data;
      this.tempUnAssignedList = this.unAssignedList;
    });
  this._roleService.getAssignedList(this.roleDetails).subscribe(
    (data: any) => {
      this.assignedList = data;
      this.tempAssignedList = this.assignedList;
    });
}
emptyValidationKeyup(event, object, fieldaname) {
  if (event.keyCode >= 46 && event.keyCode <= 90 || event.keyCode === 8) {
    object[fieldaname] = null;
  }
}
}
