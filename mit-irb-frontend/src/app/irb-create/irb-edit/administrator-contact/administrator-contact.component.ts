import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { NgxSpinnerService } from 'ngx-spinner';

import { PiElasticService } from '../../../common/service/pi-elastic.service';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { IrbCreateService } from '../../irb-create.service';

@Component({
  selector: 'app-administrator-contact',
  templateUrl: './administrator-contact.component.html',
  styleUrls: ['./administrator-contact.component.css']
})
export class AdministratorContactComponent implements OnInit {

  userDTO: any = {};
  commonVo: any = {};
  options: any = {};
  protocolAdminContactType: any = [];
  affiliationTypes: any = [];
  protocolAdminContact: any = {};
  irbPersonDetailedList: any = {};
  protocolAdminContactList = [];
  irbPersonDetailedTraining: any = [];
  trainingComments = [];
  trainingAttachments = [];
  selectedPerson = {};
  personType = 'employee';
  elasticPlaceHolder: string;
  trainingStatus: string;
  clearField: any = 'true';
  protocolId = null;
  protocolNumber = null;
  isGeneralInfoSaved = false;
  showPersonElasticBand = false;
  isAdminContactEdit = false;
  showDeletePopup = false;
  mandatoryFieldMissing = false;
  isAdminContactEditable = true;
  adminContactSelectedRow = null;
  attachmentIconValue = -1;
  commentIconValue = -1;
  private subscription1: ISubscription;

  constructor(private _elasticsearchService: PiElasticService,
    private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _spinner: NgxSpinnerService, private _irbCreateService: IrbCreateService) {

    this.protocolAdminContact.adminContactType = {};
    this.protocolAdminContact.nonEmployeeFlag = 'N';
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
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });

    this.subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.commonVo = commonVo;
        this.loadEditDetails();
      }
    });
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
  }


  /*loads the required lookup data and protocol admin contact list if any */
  loadEditDetails() {
    this.isAdminContactEditable = this.commonVo.protocolRenewalDetails != null ? this.commonVo.protocolRenewalDetails.pointOFContact : true;
    this.protocolAdminContactType = this.commonVo.protocolAdminContactType;
    this.affiliationTypes = this.commonVo.affiliationTypes;
    this.protocolAdminContactList = this.commonVo.protocolAdminContactList != null ? this.commonVo.protocolAdminContactList : [];
    this.protocolAdminContact = this.commonVo.protocolAdminContact;
    this.protocolAdminContact.adminContactType = {};

  }
  /**
   * @param  {} type Changes the type of elastic search
   */

  changePersonType(type) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.options.defaultValue = '';
    this.showPersonElasticBand = false;
    this.protocolAdminContact.personId = null;
    this.protocolAdminContact.personName = null;
    if (type === 'employee') {
      this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
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
    } else {
      this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
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
        rolodex_id: {},
        title: {}
      };
    }
  }


  /**
   * @param  {} personDetails - set the selected person details to return object
   */
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.showPersonElasticBand = personDetails != null ? true : false;
    if (personDetails != null) {
      this.protocolAdminContact.personId = this.options.type === 'person' ? personDetails.person_id : personDetails.rolodex_id;
      this.protocolAdminContact.personName = personDetails.full_name;
    } else {
      this.protocolAdminContact.personId = null;
      this.protocolAdminContact.personName = null;
    }
  }

  setPersonAffiliation(affiliation) {
    this.affiliationTypes.forEach(personAffiliation => {
      if (personAffiliation.affiliationTypeCode.toString() === affiliation) {
          this.protocolAdminContact.protocolAffiliationTypes = { affiliationTypeCode: affiliation,
            description: personAffiliation.description };
          }
    });

  }


  /**
   * @param  {} typeCode set Admin contact type code and description to return object
   */
  setAdminContactType(typeCode) {
    this.protocolAdminContactType.forEach(contactType => {
      if (contactType.adminContactTypeCode === parseInt(typeCode, 10)) {
        this.protocolAdminContact.adminContactType = { adminContactTypeCode: typeCode, description: contactType.description };
      }
    });
  }


  /**
   * @param  {} mode Adding new admin contact
   */
  addAdminContact(mode) {
    if (this.protocolAdminContact.personId != null && this.protocolAdminContact.adminContactTypeCode != null &&
      this.protocolAdminContact.adminContactTypeCode !== 'null' && this.protocolAdminContact.affiliationTypeCode != null
      && this.protocolAdminContact !== null) {
      this.saveAdminContact(mode);
      this.mandatoryFieldMissing = false;
    } else {
      this.mandatoryFieldMissing = true;
    }
  }

  /**
   * @param  {} item - contact object edited
   * @param  {} index - edited index
   */
  editAdminContact(item, index) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.options.defaultValue = item.personName;
    this.options.editHighlight = true;
    this.isAdminContactEdit = true;
    this.adminContactSelectedRow = index;
    this.protocolAdminContact = Object.assign({}, item);
    this.personType = this.protocolAdminContact.nonEmployeeFlag === 'N' ? 'employee' : 'non-employee';
  }


  /**
   * @param  {} index - index of the unit object deleted
   */
  deleteAdminContact(index) {
    this.commonVo.protocolAdminContact = this.protocolAdminContactList[index];
    this.commonVo.protocolAdminContact.acType = 'D';
    this.showDeletePopup = true;

  }
  /**
   * @param  {} mode  save call of admin contact add, edit delete
   */
  saveAdminContact(mode) {
    this.protocolAdminContact.acType = mode === 'DELETE' ? 'D' : 'U';
    this.protocolAdminContact.updateTimestamp = new Date();
    this.protocolAdminContact.updateUser = this.userDTO.userName;
    this.protocolAdminContact.protocolNumber = this.protocolNumber;
    this.protocolAdminContact.nonEmployeeFlag = this.personType === 'employee' ? 'N' : 'Y';
    if (mode !== 'DELETE') {
      this.commonVo.protocolAdminContact = this.protocolAdminContact;
    }
    this._spinner.show();
    this._irbCreateService.updateAdminContact(this.commonVo).subscribe((data: any) => {
      this.protocolAdminContactList = data.protocolAdminContactList;
      this._spinner.hide();
      this.protocolAdminContact = {};
      this.protocolAdminContact.adminContactTypeCode = null;
      this.protocolAdminContact.affiliationTypeCode = null;
      this.protocolAdminContact.adminContactType = {};
      this.commonVo.protocolAdminContact = this.protocolAdminContact;
      this.showPersonElasticBand = false;
      this.commonVo.protocolAdminContactList = this.protocolAdminContactList;
      // tslint:disable-next-line:no-construct
      this.clearField = new String('true');
      this.options.defaultValue = '';
      this.options.editHighlight = false;
      this.isAdminContactEdit = false;
      this.adminContactSelectedRow = null;
    });
  }

  loadPersonDetailedList(item) {
    this.attachmentIconValue = -1;
    this.trainingAttachments = [];
    this.commentIconValue = -1;
    this.trainingComments = [];
    this._spinner.show();
    const params = { protocolNumber: this.protocolNumber, avPersonId: item.personId };
    this._irbCreateService.getIrbPersonDetailedList(params).subscribe((data: any) => {
      this._spinner.hide();
        this.irbPersonDetailedList = data.irbViewProtocolMITKCPersonInfo;
        this.irbPersonDetailedTraining = data.irbViewProtocolMITKCPersonTrainingInfo;
        this.trainingStatus = data.trainingStatus;
    },
      error => {
        console.log('Error in method loadPersonDetailedList()', error);
      },
    );
  }

  showTrainingAttachments(index) {
    if (this.attachmentIconValue === index) {
      this.attachmentIconValue = -1;
    } else {
      this.attachmentIconValue = index;
      this.trainingAttachments = this.irbPersonDetailedTraining[index].attachment;
  }
}

showTrainingComments(index) {
  if (this.commentIconValue === index) {
    this.commentIconValue = -1;
  } else {
    this.commentIconValue = index;
    this.trainingComments = this.irbPersonDetailedTraining[index].comments;
}
}
}
