import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { IrbCreateService } from '../irb-create.service';
import { ISubscription } from 'rxjs/Subscription';
import { SharedDataService } from '../../common/service/shared-data.service';
import { ToastsManager } from 'ng2-toastr';


@Component({
  selector: 'app-irb-summary-details',
  templateUrl: './irb-summary-details.component.html',
  styleUrls: ['./irb-summary-details.component.css'],
  providers: [IrbCreateService]
})
export class IrbSummaryDetailsComponent implements OnInit, OnDestroy {
  createOrViewPath: string;
  protocolStatus = null;
  protocolId = null;
  sequenceNumber: number;
  isAmendment  = false;
  moduleAvailableForAmendment: any = [];
  irbSummaryVo: any = {};
  userDTO: any = {};
  requestObject = {
    protocolNumber: null
  };

  private $subscription1: ISubscription;
  private $subscription2: ISubscription;

  constructor(private _activatedRoute: ActivatedRoute, private _irbCreateService: IrbCreateService,
    private _router: Router, private _sharedDataService: SharedDataService,
    private _spinner: NgxSpinnerService, public toastr: ToastsManager) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.requestObject.protocolNumber = params['protocolNumber'];
     // this.protocolId = params['protocolId'];
      this.isAmendment  = this.requestObject.protocolNumber.includes('A') ? true : false;
      this.getProtocolcolDetails();
      this.loadSummaryDetails();
    });
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
    if (this.$subscription2) {
      this.$subscription2.unsubscribe();
    }
  }

  getProtocolcolDetails() {
  this.createOrViewPath = this._router.parseUrl(this._router.url).root.children['primary'].segments[1].path;
    if (this.createOrViewPath === 'irb-create') {
      // this._spinner.show();
      this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe((commonVo: any) => {
        // this._spinner.hide();
        if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
          this.protocolStatus = commonVo.generalInfo.protocolStatus.protocolStatusCode;
          this.sequenceNumber = commonVo.generalInfo.sequenceNumber;
        }
      });
    } else {
      // this._spinner.show();
      this.$subscription2 = this._sharedDataService.viewProtocolDetailsVariable.subscribe((commonVo: any) => {
        //   this._spinner.hide();
        if (commonVo !== undefined && commonVo != null) {
          this.protocolStatus = commonVo.PROTOCOL_STATUS_CODE;
          this.sequenceNumber = commonVo.SEQUENCE_NUMBER;
        }
      });
    }
}

loadSummaryDetails() {
  // this._spinner.show();
  this._irbCreateService.getAmendRenwalSummary(this.requestObject).subscribe( data => {
  // this._spinner.hide();
    this.irbSummaryVo = data;
    this.moduleAvailableForAmendment = this.irbSummaryVo.moduleAvailableForAmendment;
  }

  );
}

saveAmendmentDetails() {
  this.irbSummaryVo.moduleAvailableForAmendment = this.moduleAvailableForAmendment;
  this.irbSummaryVo.protocolNumber = this.requestObject.protocolNumber;
  this.irbSummaryVo.protocolId = this.protocolId;
  this.irbSummaryVo.sequenceNumber = this.sequenceNumber;
  this.irbSummaryVo.updateUser = this.userDTO.userName;
  this._spinner.show();
  this._irbCreateService.updateAmendRenwalSummary(this.irbSummaryVo).subscribe(data => {
    this._spinner.hide();
    this.irbSummaryVo = data;
    this.moduleAvailableForAmendment = this.irbSummaryVo.moduleAvailableForAmendment;
    if (this.irbSummaryVo.successCode === true) {
      this.toastr.success('Saved Successfully', null, { toastLife: 2000 });
    } else {
      this.toastr.error('Failed to Save', null, { toastLife: 2000 });
    }
  });
}
}
