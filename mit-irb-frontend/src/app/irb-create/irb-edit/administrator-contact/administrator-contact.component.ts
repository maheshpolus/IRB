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
  protocolAdminContact: any = {};
  protocolAdminContactList = [];
  selectedPerson = {};
  personType = 'employee';
  elasticPlaceHolder: string;
  clearField: any = 'true';
  protocolId = null;
  protocolNumber = null;
  isGeneralInfoSaved = false;
  showPersonElasticBand = false;
  isAdminContactEdit = false;
  showDeletePopup = false;
  mandatoryFieldMissing = false;
  adminContactSelectedRow = null;
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
    this.options.formatString = 'full_name | email_address | home_unit';
    this.options.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit: {}

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
    this.protocolAdminContactType = this.commonVo.protocolAdminContactType;
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
      this.elasticPlaceHolder = 'Search for an Employee Name';
    } else {
      this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';
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
      this.protocolAdminContact.adminContactTypeCode !== 'null') {
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
  deleteProtocolUnit(index) {
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
}
