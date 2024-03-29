import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Location } from '@angular/common';

import { IrbViewService } from '../../../irb-view/irb-view.service';

@Component({
  selector: 'app-submission-details',
  templateUrl: './submission-details.component.html',
  styleUrls: ['./submission-details.component.css']
})
export class SubmissionDetailsComponent implements OnInit {

  requestObject = {
    protocolNumber: '',
  };
  result: any = {};
  protocolSubmissionReviewers = [];
  protocolReviewerComments = [];
  protocolRenewalDetail: any = {};
  protocolRenewalComments = '';
  submissionCheckListData = [];
  historyName = '';
  isammendment = false;
  isrenewal = false;

  constructor(private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute,
    private _spinner: NgxSpinnerService, private _location: Location) {
    this.historyName = this._activatedRoute.snapshot.queryParamMap.get('comment');
  }

  ngOnInit() {
    document.getElementById('scrollToTop').scrollTop = 0;
    this.getsubmissionDetails();
  }
  getsubmissionDetails() {
    this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('submissionProtocolNumber');
    this.isammendment = this.requestObject.protocolNumber.includes('A') ? true : false;
    this.isrenewal = this.requestObject.protocolNumber.includes('R') ? true : false;
    this._spinner.show();
    this._irbViewService.getProtocolSubmissionDetails(this.requestObject).subscribe(data => {
      this._spinner.hide();
      this.result = data || [];
      this.protocolSubmissionReviewers = this.result.protocolSubmissionReviewers != null ? this.result.protocolSubmissionReviewers : [];
      this.protocolReviewerComments = this.result.protocolReviewerComments != null ? this.result.protocolReviewerComments : [];
      this.protocolRenewalDetail = this.result.protocolRenewalDetail;
      this.protocolRenewalComments = this.result.protocolRenewalComments;
      this.submissionCheckListData = this.result.submissionCheckListData != null ? this.result.submissionCheckListData : [];

    },
      error => {
        console.log('Error in method loadHistoryList()', error);
      },
    );
  }

  backToHistory() {
    this._location.back();
  }
}
