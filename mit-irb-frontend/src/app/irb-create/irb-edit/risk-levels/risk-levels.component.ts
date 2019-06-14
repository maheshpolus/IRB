import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { SharedDataService } from '../../../common/service/shared-data.service';
import { IrbCreateService } from '../../irb-create.service';

@Component({
  selector: 'app-risk-levels',
  templateUrl: './risk-levels.component.html',
  styleUrls: ['./risk-levels.component.css']
})
export class RiskLevelsComponent implements OnInit, OnDestroy {

  userDTO: any = {};
  commonVo: any = {};
  generalInfo: any = {};
  protocolRiskLevel: any = {};
  protocolFDARiskLevel: any = {};
  protocolNumber = null;
  protocolId = null;
  riskLevelType = [];
  fdaRiskLevelType = [];
  protocolRiskLevelList = [];
  protocolFDARiskLevelList = [];
  isGeneralInfoSaved = false;
  isRiskLevelEditable = false;

  private $subscription1: ISubscription;
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService, private _spinner: NgxSpinnerService,) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      this.isRiskLevelEditable = params['isAdminCorrection'] != null ? true : false;
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });

    this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.commonVo = commonVo;
        this.loadEditDetails();
      }
    });
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  loadEditDetails() {
    // Look up Data - Start
    this.riskLevelType = this.commonVo.riskLevelType;
    this.fdaRiskLevelType = this.commonVo.fdaRiskLevelType;
    // Look up Data - End
    this.generalInfo = this.commonVo.generalInfo != null ? this.commonVo.generalInfo : {};
    this.generalInfo.riskLvlDateAssigned = this.generalInfo.riskLvlDateAssigned != null ?
      this.GetFormattedDateFromString(this.generalInfo.riskLvlDateAssigned) : null;
      this.generalInfo.fdaRiskLvlDateAssigned = this.generalInfo.fdaRiskLvlDateAssigned != null ?
      this.GetFormattedDateFromString(this.generalInfo.fdaRiskLvlDateAssigned) : null;

    this.protocolRiskLevel = this.commonVo.protocolRiskLevel != null ? this.commonVo.protocolRiskLevel : {};
    this.protocolRiskLevelList = this.commonVo.protocolRiskLevelList != null ? this.commonVo.protocolRiskLevelList : [];

    this.protocolFDARiskLevel = this.commonVo.protocolFDARiskLevel != null ? this.commonVo.protocolFDARiskLevel : {};
    this.protocolFDARiskLevelList = this.commonVo.protocolFDARiskLevelList != null ? this.commonVo.protocolFDARiskLevelList : [];
  }


  saveGeneralInfo() {
    this.generalInfo.stringRiskLvlDate = this.generalInfo.riskLvlDateAssigned != null ?
        this.GetFormattedDate(this.generalInfo.riskLvlDateAssigned) : null;
    this.generalInfo.stringFDARiskLvlDate = this.generalInfo.fdaRiskLvlDateAssigned != null ?
        this.GetFormattedDate(this.generalInfo.fdaRiskLvlDateAssigned) : null;

    this.commonVo.generalInfo = this.generalInfo;
    this._spinner.show();
    this._irbCreateService.updateProtocolGeneralInfo(this.commonVo).subscribe(
      data => {
       const result: any = data;
        this._spinner.hide();
        this.generalInfo = result.generalInfo;
        this.commonVo.generalInfo = result.generalInfo;
        this._sharedDataService.setCommonVo(this.commonVo);
        this._sharedDataService.setGeneralInfo(this.generalInfo);
      });
  }


  /**
   * @param  {} currentDate - date to be conveted
   * convert the date to string with format mm-dd-yyyy
   */
  GetFormattedDate(currentDate) {
    const month = currentDate.getMonth() + 1;
    const day = currentDate.getDate();
    const year = currentDate.getFullYear();
    return month + '-' + day + '-' + year;
  }

  /**
   * @param  {} currentDate - input in format yyyy-mon-dd
   */
  GetFormattedDateFromString(currentDate) {
    const res = currentDate.split('-');
    const year = parseInt(res[0], 10);
    const month = parseInt(res[1], 10);
    const day = parseInt(res[2], 10);
    return new Date(year, month - 1, day);
}


}
