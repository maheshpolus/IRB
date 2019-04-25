import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { NgxSpinnerService } from 'ngx-spinner';

import { IrbCreateService } from '../../irb-create.service';
import { KeyPressEvent } from '../../../common/directives/keyPressEvent.component';
import { SharedDataService } from '../../../common/service/shared-data.service';


@Component({
  selector: 'app-protocol-units',
  templateUrl: './protocol-units.component.html',
  styleUrls: ['./protocol-units.component.css']
})
export class ProtocolUnitsComponent implements OnInit, OnDestroy {

  userDTO: any = {};
  commonVo: any = {};
  protocolUnit: any = {};
  protocolId = null;
  protocolNumber = null;
  protocolUnitSelectedRow = null;
  isGeneralInfoSaved = false;
  isProtocolUnitSearch = false;
  isProtocolUnitEdit = false;
  showDeletePopup = false;
  searchString: string;
  alertMessage: string;
  unitSearchResult = [];
  protocolUnitList = [];
  protocolUnitTypes = [];
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false,
    invalidUnitData: false, isLeadUnitExist: false
  };

  private $subscription1: ISubscription;

  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService,
    private _spinner: NgxSpinnerService,
    public keyPressEvent: KeyPressEvent) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
      this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
        if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
          this.commonVo = commonVo;
          this.loadEditDetails();
        }
      });
    });
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  /*loads the required lookup data and protocol unit list if any */
  loadEditDetails() {
    this.protocolUnit = this.commonVo.protocolLeadUnits;
    this.protocolUnitList = this.commonVo.protocolLeadUnitsList != null ? this.commonVo.protocolLeadUnitsList : [];
    this.protocolUnitTypes = this.commonVo.protocolUnitType;

  }

  /**
   * @param  {} mode - Specifies the action, whether is EDIT or ADD
   */
  addProtocolUnit(mode) {
    if (this.protocolUnit.unitName != null && this.protocolUnit.unitName !== undefined &&
      this.protocolUnit.unitNumber != null && this.protocolUnit.unitNumber !== undefined
      && this.protocolUnit.unitTypeCode != null && this.protocolUnit.unitTypeCode !== undefined && 
      this.protocolUnit.unitTypeCode !== 'null' &&
      this.invalidData.isLeadUnitExist === false) {
      this.invalidData.invalidUnitData = false;
      this.saveProtocolUnit(mode);
      if (mode === 'EDIT') {
        this.isProtocolUnitEdit = false;
        this.protocolUnitSelectedRow = null;
      }
    } else {
      this.invalidData.invalidUnitData = true;
      if (this.invalidData.isLeadUnitExist === true) {
        this.alertMessage = 'Lead Unit already exists please choose a different type';
      } else {
        this.alertMessage = 'Please fill all mandatory fields marked <strong>*</strong>';
      }
    }
  }

  /**
   * @param  {} item - unit object edited
   * @param  {} index - edited index
   */
  editProtocolUnit(item, index) {
    this.isProtocolUnitEdit = true;
    this.protocolUnitSelectedRow = index;
    this.protocolUnit = Object.assign({}, item);

  }
  /**
   * @param  {} index - index of the unit object deleted
   */
  deleteProtocolUnit(index) {
    this.protocolUnit.acType = 'D';
    this.commonVo.protocolLeadUnits = this.protocolUnitList[index];
    this.showDeletePopup = true;

  }


  /**
     * @param  {} mode - Specifies the action, whether is EDIT or ADD or DELETE
     */
  saveProtocolUnit(mode) {
    if (mode !== 'DELETE') {
      this.protocolUnit.acType = 'U';
      this.protocolUnit.updateTimestamp = new Date();
      this.protocolUnit.updateUser = this.userDTO.userName;
      this.protocolUnit.protocolNumber = this.protocolNumber;
      this.commonVo.protocolLeadUnits = this.protocolUnit;
    }
    this._spinner.show();
    this._irbCreateService.updateProtocolUnits(this.commonVo).subscribe((data: any) => {
      this.protocolUnitList = data.protocolLeadUnitsList;
      this._spinner.hide();
      this.protocolUnit = {};
      this.protocolUnit.unitTypeCode = null;
      this.commonVo.protocolLeadUnitsList = this.protocolUnitList;
      this.isProtocolUnitEdit = false;
    });

  }
  /* Get the homeunit list on each keypress  */
  getHomeUnitList() {
    this.searchString = this.protocolUnit.unitName;
    this._irbCreateService.loadHomeUnits(this.searchString).subscribe(
      (data: any) => {
        this.unitSearchResult = data.homeUnits;
      });
  }

  /**
   * @param  {} unitName - name of the unit selected
   * @param  {} unitNumber - number of the unit selected
   */
  selectedHomeUnit(unitName, unitNumber) {
    this.protocolUnit.unitName = unitName;
    this.protocolUnit.unitNumber = unitNumber;
    this.isProtocolUnitSearch = false;
  }


  /**
   * @param  {} unitTypeCode- code of the unitType selected
   */
  setProtocolUnitType(unitTypeCode) {
    this.invalidData.isLeadUnitExist = false;
    if (unitTypeCode === '1') {
      this.protocolUnitList.forEach(protocolUnit => {
        if (protocolUnit.unitTypeCode === unitTypeCode) {
          this.invalidData.isLeadUnitExist = true;
        }
      });

    }
    if (this.invalidData.isLeadUnitExist === false) {
      this.invalidData.isLeadUnitExist = false;
    this.protocolUnitTypes.forEach(protocolType => {
      if (protocolType.unitTypeCode === unitTypeCode) {
        this.protocolUnit.unitType = { unitTypeCode: unitTypeCode, description: protocolType.description };
      }
    });
  }
  }

}
