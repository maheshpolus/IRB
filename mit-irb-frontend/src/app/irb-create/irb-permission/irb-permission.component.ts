import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';

import { PiElasticService } from '../../common/service/pi-elastic.service';
import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-permission',
  templateUrl: './irb-permission.component.html',
  styleUrls: ['./irb-permission.component.css']
})
export class IrbPermissionComponent implements OnInit, OnDestroy {

  userDTO: any = {};
  commonVo: any = {};
  permissionVo: any = {};
  options: any = {};
  elasticPlaceHolder: string;
  showPersonElasticBand = false;
  selectedPerson = {};
  protocolRolePerson: any = {};
  protocolId: null;
  protocolNumber: null;
  protocolRoles = [];
  protocolRolePersonList = [];
  clearField: any = 'true';
  private $subscription1: ISubscription;

  constructor(private _elasticsearchService: PiElasticService, private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute) {

    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.options.type = 'person';
    this.options.size = 20;
    this.options.contextField = 'full_name';
    this.options.debounceTime = 500;
    this.options.theme = '#a31f34';
    this.options.width = '100%';
    this.options.fontSize = '16px';
    this.options.defaultValue = '';
    this.options.formatString = 'full_name | email_address | home_unit | person_id';
    this.options.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit: {},
      person_id: {}
    };
    this.elasticPlaceHolder = 'Search for an Employee Name';
  }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
    });

    this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      this.commonVo = commonVo;
    });
    this.getProtocolPermissionDetails();
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  getProtocolPermissionDetails() {
    const reqstObj = { protocolId: this.protocolId };
    this._irbCreateService.getProtocolPermissionDetails(reqstObj).subscribe((data: any) => {
      this.permissionVo = data;
      this.protocolRoles = this.permissionVo.protocolRoles;
      this.protocolRolePerson = this.permissionVo.protocolRolePerson;
      this.protocolRolePersonList = this.permissionVo.protocolRolePersonList != null ? this.permissionVo.protocolRolePersonList : [];
    });
  }

  setRole(roleId) {
    const selectedRole = this.protocolRoles.filter(x => x.roleId.toString() === roleId);
    if (selectedRole.length > 0) {
       this.protocolRolePerson.role = {roleId : selectedRole[0].roleId, roleName: selectedRole[0].roleName};
    }
  }


  /**
   * @param  {} personDetails - set the selected person details to return object
   */
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.showPersonElasticBand = personDetails != null ? true : false;
    if (personDetails != null) {
      this.protocolRolePerson.personId = personDetails.person_id;
      this.protocolRolePerson.personName = personDetails.full_name;
    } else {
      this.protocolRolePerson.personId = null;
      this.protocolRolePerson.personName = null;
    }
  }


  addProtocolRolePerson() {
    this.protocolRolePerson.protocolId = this.protocolId;
    this.protocolRolePerson.protocolNumber = this.protocolNumber;
    this.protocolRolePerson.sequenceNumber = this.commonVo.generalInfo.sequenceNumber;
    this.protocolRolePerson.updateTimestamp = new Date();
    this.protocolRolePerson.updateUser = this.userDTO.userName;
    this.permissionVo.protocolRolePerson = this.protocolRolePerson;
    this.permissionVo.protocolRolePerson.acType = 'U';
    this._irbCreateService.updateProtocolPermission(this.permissionVo).subscribe((data: any) => {
      const result = data;
      this.protocolRolePerson = {};
      this.showPersonElasticBand = false;
      // tslint:disable-next-line:no-construct
      this.clearField = new String('true');
      this.options.defaultValue = '';
      this.protocolRolePersonList = result.protocolRolePersonList != null ? result.protocolRolePersonList : [];
    });
  }

  deletePermission(person) {
    person.acType = 'D';
    this.permissionVo.protocolRolePerson = person;
    this._irbCreateService.updateProtocolPermission(this.permissionVo).subscribe((data: any) => {
      const result = data;
      this.protocolRolePersonList = result.protocolRolePersonList != null ? result.protocolRolePersonList : [];
    });

  }
}
