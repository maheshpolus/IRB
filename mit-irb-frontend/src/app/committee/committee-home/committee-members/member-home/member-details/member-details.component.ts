import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommitteeSaveService } from '../../../../committee-save.service';
import { CompleterService, CompleterData } from 'ng2-completer';

import { PiElasticService } from '../../../../../common/service/pi-elastic.service';
import { KeyPressEvent } from '../../../../../common/directives/keyPressEvent.component';
import { CommitteCreateEditService } from '../../../../committee-create-edit.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css']
})
export class MemberDetailsComponent implements OnInit {

  memberId = null;
  personId = null;
  rolodexId = null;
  committeId = null;
  editRoleIndex = null;
  deletedRoleIndex = null;
  deleteExpertiseIndex = null;
  researchSearchString: string;
  personType = 'employee';
  elasticPlaceHolder: string;
  warningMessage: string;
  isExpertiseSearch = false;
  isExpertiseMandatoryFiled = true;
  isMandatoryFiled = true;
  isRoleMandatoryFiled = true;
  clearField: any = 'true';
  isNewMember = true;
  memberDetails: any = {};
  committeeMemberExpertise: any = {};
  committeeMemberRole: any = {};
  committeeMemberRoleEdit: any = {};
  committeeVo: any = {};
  options: any = {};
  userDTO: any = {};
  selectedPerson: any = {};
  memberShipTypes: any = [];
  memberRoleList: any = [];
  memberExpertiseList: any = [];
  expertiseSearchResult: any = [];
  roleTypes: CompleterData;
  expertiseTypes: CompleterData;
  constructor(private _activatedRoute: ActivatedRoute, private _elasticsearchService: PiElasticService, private _router: Router,
    private _committeeSaveService: CommitteeSaveService, private _spinner: NgxSpinnerService,
    private _completerService: CompleterService, public keyPressEvent: KeyPressEvent) {
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
    this.options.formatString = 'full_name | email_address | home_unit_name';
    this.options.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit_name: {},
      person_id: {}
    };
    this.elasticPlaceHolder = 'Search for an Employee Name';
  }

  ngOnInit() {
    this.userDTO = JSON.parse(localStorage.getItem('currentUser'));
    this._activatedRoute.queryParams.subscribe(params => {
      this.memberId = params['membershipId'];
      this.committeId = params['id'];
      if (this.memberId != null) {
        this.isNewMember = false;
      }
      this.personId = params['personId'];
      this.rolodexId = params['rolodexId'];
      this.getMemberDetails();
    });
  }
  getMemberDetails() {
    const requestObject = {
      commMembershipId: this.memberId, personId: this.personId, rolodexId: this.rolodexId,
      committeeId: this.committeId
    };
    this._spinner.show();
    this._committeeSaveService.getCommitteeMemberDetails(requestObject).subscribe((data: any) => {
      this._spinner.hide();
      this.committeeVo = data;
      this.roleTypes = this._completerService.local(data.membershipRoles, 'description', 'description');
      this.expertiseTypes = this._completerService.local(data.membershipRoles, 'description', 'description');
      this.memberShipTypes = data.committeeMembershipTypes;
      this.memberDetails = data.committeeMemberships;
      this.memberDetails.termStartDate = this.memberDetails.termStartDate != null ? new Date(this.memberDetails.termStartDate) : null;
      this.memberDetails.termEndDate = this.memberDetails.termEndDate != null ? new Date(this.memberDetails.termEndDate) : null;
      this.memberRoleList = data.committeeMemberships.committeeMemberRoles != null ? data.committeeMemberships.committeeMemberRoles : [];
      this.memberExpertiseList = data.committeeMemberships.committeeMemberExpertises != null ?
        data.committeeMemberships.committeeMemberExpertises : [];
      this.selectedPerson = this.memberDetails.nonEmployeeFlag === true ? this.memberDetails.rolodex : this.memberDetails.personDetails;
      this.memberDetails.membershipTypeCode = this.isNewMember ? '1' : this.memberDetails.membershipTypeCode;
    });
  }

  setMemberRole(event) {
    this.committeeMemberRole.membershipRoleCode = event.originalObject.membershipRoleCode;
    this.committeeMemberRole.membershipRoleDescription = event.originalObject.description;
  }

  getExpertiseList() {
    this.committeeMemberExpertise.researchAreaCode = null;
    this.researchSearchString = this.committeeMemberExpertise.researchAreaDescription;
    this._committeeSaveService.loadResearchAreas(this.researchSearchString).subscribe(
      (data: any) => {
        this.expertiseSearchResult = data.researchAreas;
      });
  }
  expertiseSelected(expertiseName) {
    this.expertiseSearchResult.forEach(expertise => {
      if (expertise.description === expertiseName) {
        this.committeeMemberExpertise.researchAreaCode = expertise.researchAreaCode;
        this.committeeMemberExpertise.researchAreaDescription = expertiseName;
      }
    });
    this.isExpertiseSearch = false;
  }
  changePersonType(type) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.options.defaultValue = '';
    if (type === 'employee') {
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
      this.elasticPlaceHolder = 'Search for an Employee Name';
      this.options.formatString = 'full_name | email_address | home_unit_name';
      this.options.fields = {
        full_name: {},
        first_name: {},
        user_name: {},
        email_address: {},
        home_unit_name: {},
        person_id: {}
      };

    } else {
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';
      this.options.formatString = 'full_name | email_address | title ';
      this.options.fields = {
        full_name: {},
        first_name: {},
        user_name: {},
        email_address: {},
        home_unit: {},
        title: {},
        rolodex_id: {}
      };
    }
  }
  selectPersonElasticResult(personDetails) {
    // this.selectedPerson = personDetails;
    if (this.options.type === 'person') {
      this.memberDetails.personId = personDetails.person_id;
      this.committeeVo.personId = personDetails.person_id;
      this.memberDetails.nonEmployeeFlag = false;
      this.selectedPerson = {
        fullName: personDetails.full_name, emailAddress: personDetails.email_address,
        directoryTitle: personDetails.job_title, phoneNumber: personDetails.phone_nbr,
        addressLine1: personDetails.addr_line_1, unitName: personDetails.home_unit_name
      };
    } else {
      this.committeeVo.rolodexId = personDetails.rolodex_id;
      this.memberDetails.rolodexId = personDetails.rolodex_id;
      this.memberDetails.nonEmployeeFlag = true;
      this.selectedPerson = {
        fullName: personDetails.full_name, emailAddress: personDetails.email_address,
        directoryTitle: personDetails.title, phoneNumber: personDetails.phone_number, addressLine1: personDetails.addr_line_1,
        organization: personDetails.organization
      };
    }
  }

  saveMemberDetails() {
    if (this.memberDetails.termStartDate == null || this.memberDetails.termEndDate == null ||
      ((this.committeeMemberRole.membershipRoleCode == null || this.committeeMemberRole.startDate == null ||
        this.committeeMemberRole.endDate == null) && this.isNewMember === true)) {
      this.isMandatoryFiled = false;
      this.warningMessage = 'Please fill mandatory fields marked <strong>*</strong>';
    } else if (this.memberDetails.termStartDate > this.memberDetails.termEndDate && this.isNewMember === true) {
      this.isMandatoryFiled = false;
      this.warningMessage = 'Term end date must be after term start date.';
    } else if (this.committeeMemberRole.startDate > this.committeeMemberRole.endDate && this.isNewMember === true) {
      this.isMandatoryFiled = false;
      this.warningMessage = 'Role end date must be after role start date.';
    } else if ((this.committeeMemberRole.startDate < this.memberDetails.termStartDate ||
      this.committeeMemberRole.startDate > this.memberDetails.termEndDate ||
      this.committeeMemberRole.endDate < this.memberDetails.termStartDate ||
      this.committeeMemberRole.endDate > this.memberDetails.termEndDate) && this.isNewMember === true) {
      this.isMandatoryFiled = false;
      this.warningMessage = 'Role start date and role end date must be between term start date and term end date.';
    } else {
      this.isMandatoryFiled = true;
      this.committeeVo.acType = 'U';
      if (this.isNewMember) {
        this.memberDetails.committeeMemberRoles[0] = this.committeeMemberRole;
      }
      this.memberDetails.personName = this.selectedPerson.fullName;
      this.memberDetails.updateTimeStamp = new Date().getTime();
      this.memberDetails.updateUser = this.userDTO.userName;
      this.committeeVo.committeeMemberships = this.memberDetails;
      this._spinner.show();
      this._committeeSaveService.updateCommitteMembers(this.committeeVo).subscribe((data: any) => {
        this._spinner.hide();
        this.memberDetails = data.committeeMemberships;
        this.memberDetails.termStartDate = this.memberDetails.termStartDate != null ? new Date(this.memberDetails.termStartDate) : null;
        this.memberDetails.termEndDate = this.memberDetails.termEndDate != null ? new Date(this.memberDetails.termEndDate) : null;
        this.committeeMemberRole.membershipRoleCode = null;
        this.committeeMemberRole.membershipRoleDescription = null;
        this.committeeMemberRole.startDate = null;
        this.committeeMemberRole.endDate = null;
        this._router.navigate(['irb/committee/committeeMembers/memberHome'],
          {
            queryParams: {
              'mode': this._activatedRoute.snapshot.queryParamMap.get('mode'), 'id': this.committeId,
              'membershipId': this.memberDetails.commMembershipId, 'personId': this.memberDetails.personDetails != null ?
                this.memberDetails.personDetails.personId : null,
              'rolodexId': this.memberDetails.rolodex != null ? this.memberDetails.rolodex.rolodexId : null
            }
          });
      });
    }
  }

  setMembershipType(typeCode) {
    this.memberShipTypes.forEach((type: any) => {
      if (typeCode === type.membershipTypeCode) {
        this.memberDetails.committeeMembershipType = {
          description: type.description, membershipTypeCode: typeCode
        };
      }
    });
  }

  addMemberRole() {
    if (this.committeeMemberRole.membershipRoleCode == null || this.committeeMemberRole.startDate == null ||
      this.committeeMemberRole.endDate == null) {
      this.isRoleMandatoryFiled = false;
      this.warningMessage = 'Please fill mandatory fields marked <strong>*</strong>';
    } else if (this.committeeMemberRole.startDate < this.memberDetails.termStartDate ||
      this.committeeMemberRole.startDate > this.memberDetails.termEndDate ||
      this.committeeMemberRole.endDate < this.memberDetails.termStartDate ||
      this.committeeMemberRole.endDate > this.memberDetails.termEndDate) {
      this.isRoleMandatoryFiled = false;
      this.warningMessage = 'Role start date and role end date must be between term start date and term end date.';
    } else if (this.committeeMemberRole.startDate > this.committeeMemberRole.endDate && this.isNewMember === true) {
      this.isMandatoryFiled = false;
      this.warningMessage = 'Role end date must be after role start date.';
    } else {
      this.isRoleMandatoryFiled = true;
      this.committeeMemberRole.acType = 'U';
      this.saveMemberRole(this.committeeMemberRole);
    }

  }

  saveMemberRole(memberRole) {
    // const requestObject = { commMembershipId: this.memberId, committeeId: this._activatedRoute.snapshot.queryParamMap.get('id'),
    // committeeMemberRole: memberRole};
    memberRole.updateTimestamp = new Date().getTime();
    memberRole.updateUser = this.userDTO.userName;
    this.committeeVo.committeeMemberRole = memberRole;
    this._spinner.show();
    this._committeeSaveService.updateMemberRole(this.committeeVo).subscribe((data: any) => {
      this._spinner.hide();
      this.memberRoleList = data.committeeMemberships.committeeMemberRoles != null ? data.committeeMemberships.committeeMemberRoles : [];
      this.committeeMemberRole.membershipRoleCode = null;
      this.committeeMemberRole.membershipRoleDescription = null;
      this.committeeMemberRole.startDate = null;
      this.committeeMemberRole.endDate = null;
      this.editRoleIndex = null;
      this.committeeMemberRoleEdit = {};
    });
  }
  deleteMemberRole() {
    this.memberRoleList[this.deletedRoleIndex].acType = 'D';
    this.saveMemberRole(this.memberRoleList[this.deletedRoleIndex]);
  }

  editMemberRole(role, index) {
    this.editRoleIndex = index;
    this.committeeMemberRoleEdit = role;
    this.committeeMemberRoleEdit.acType = 'U';
  }

  addExpertise() {
    if (this.committeeMemberExpertise.researchAreaCode == null) {
      this.isExpertiseMandatoryFiled = false;
      this.warningMessage = 'Please fill mandatory fields marked <strong>*</strong>';
    } else {
      this.isExpertiseMandatoryFiled = true;
      this.committeeMemberExpertise.acType = 'U';
      this.saveExpertise(this.committeeMemberExpertise);
    }
  }

  saveExpertise(expertise) {
    expertise.updateTimestamp = new Date().getTime();
    expertise.updateUser = this.userDTO.userName;
    this.committeeVo.committeeMemberExpertise = expertise;
    this._spinner.show();
    this._committeeSaveService.updateMemberExpertise(this.committeeVo).subscribe((data: any) => {
      this._spinner.hide();
      this.memberExpertiseList = data.committeeMemberships.committeeMemberExpertises != null ?
        data.committeeMemberships.committeeMemberExpertises : [];
      this.committeeMemberExpertise.researchAreaCode = null;
      this.committeeMemberExpertise.researchAreaDescription = null;
    });
  }

  deleteMemberExpertise() {
    this.memberExpertiseList[this.deleteExpertiseIndex].acType = 'D';
    this.saveExpertise(this.memberExpertiseList[this.deleteExpertiseIndex]);
  }

  checkCanDelete(index) {
    if (this.memberRoleList.length === 1) {
      document.getElementById('roleDeleteWarningBtn').click();

    } else {
      this.deletedRoleIndex = index;
      document.getElementById('deleteModalBtn').click();
    }
  }

}
