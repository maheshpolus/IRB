import { Component, OnInit, AfterViewInit, NgZone, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router, ActivatedRoute} from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { ISubscription } from 'rxjs/Subscription';

import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { IrbCreateService } from '../../irb-create.service';
import { PiElasticService } from '../../../common/service/pi-elastic.service';
import {SharedDataService} from '../../../common/service/shared-data.service';

@Component({
  selector: 'app-funding-source',
  templateUrl: './funding-source.component.html',
  styleUrls: ['./funding-source.component.css']
})
export class FundingSourceComponent implements OnInit, AfterViewInit, OnDestroy {

  userDTO: any = {};
  protocolNumber = null;
  protocolId = null;
  isGeneralInfoSaved = false;
  commonVo: any = {};
  generalInfo: any = {};
  sourceType: any = {};
  result: any = {};
  protocolFundingSourceTypes = [];
  fundingSource: any = {};
  isFundingInfoEdit = false;
  fundingEditIndex = null;
  protocolFundingSourceList = [];
  invalidData = {invalidGeneralInfo: false, invalidStartDate : false, invalidEndDate : false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false};

  private subscription1: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService) { }

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
  }

  ngOnDestroy() {
    if (this.subscription1) {
     this.subscription1.unsubscribe();
    }
   }

  ngAfterViewInit() {}

  loadEditDetails() {
    this.protocolFundingSourceTypes = this.commonVo.protocolFundingSourceTypes;
    this.generalInfo = this.commonVo.generalInfo;
    this.fundingSource = this.commonVo.fundingSource;
    this.protocolFundingSourceList = this.commonVo.protocolFundingSourceList != null ? this.commonVo.protocolFundingSourceList : [];
  }

  setFundingSourceType (typeCode) {
    this.sourceType.typeCode = typeCode;
    if (typeCode === '6') {
      this.sourceType.placeholder = 'Search Award';
    } else if (typeCode === '4') {
      this.sourceType.placeholder = 'Search Development Proposal';
    } else if (typeCode === '5') {
      this.sourceType.placeholder = 'Search Institute Proposal';
    } else if (typeCode === '1') {
      this.sourceType.placeholder = 'Search Sponsor';
    } else {
      this.sourceType.placeholder = null;
    }
    this.protocolFundingSourceTypes.forEach(SourceType => {
      if (SourceType.fundingSourceTypeCode === typeCode) {
        this.fundingSource.protocolFundingSourceTypes = {fundingSourceTypeCode: typeCode, description: SourceType.description};
      }
    });
  }

  addFundingDetails(mode) {
    if (this.fundingSource.fundingSourceTypeCode != null && this.fundingSource.fundingSourceTypeCode !== undefined &&
        this.fundingSource.fundingSource != null && this.fundingSource.fundingSource !== undefined) {
      this.invalidData.invalidFundingInfo = false;
      this.saveFundingDetails(mode);
      } else {
        this.invalidData.invalidFundingInfo = true;
      }
      if (mode === 'EDIT') {
        this.isFundingInfoEdit = false;
        this.fundingEditIndex = null;
      }
  }

  editFundingDetails(item, index) {
    this.fundingEditIndex = index;
    this.isFundingInfoEdit = true;
    this.fundingSource = Object.assign({}, item) ;
  }

  deleteFundingDetails(index) {
    this.commonVo.fundingSource = this.protocolFundingSourceList[index];
    this.commonVo.fundingSource.acType = 'D';
    this.saveFundingDetails('DELETE');
  }

  saveFundingDetails(mode) {
    if (mode !== 'DELETE') {
      this.fundingSource.updateTimestamp = new Date();
      this.fundingSource.updateUser = localStorage.getItem('userName');
      this.fundingSource.sequenceNumber = 1;
      this.fundingSource.protocolNumber = this.protocolNumber;
      this.fundingSource.protocolId = this.generalInfo.protocolId;
      this.fundingSource.acType = 'U';
      this.commonVo.fundingSource = this.fundingSource;
    }
      this._irbCreateService.updateFundingSource(this.commonVo).subscribe(
          data => {
             this.result = data;
             this.fundingSource = {};
             this.protocolFundingSourceList = this.result.protocolFundingSourceList;
          });
  }

}
