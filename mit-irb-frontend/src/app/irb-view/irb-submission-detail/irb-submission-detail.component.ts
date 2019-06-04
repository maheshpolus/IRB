import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ISubscription } from 'rxjs/Subscription';

import { IrbViewService } from '../irb-view.service';
import {SharedDataService} from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-submission-detail',
  templateUrl: './irb-submission-detail.component.html',
  styleUrls: ['./irb-submission-detail.component.css']
})
export class IrbSubmissionDetailComponent implements OnInit, OnDestroy {

  isExpanded = false;
  lookUpData: any;
  headerDetails: any;
  userDTO: any;
  irbAdminsReviewerType = [];
  irbAdminsList = [];
  submissionRewiewTypeList = [];
  submissionTypeList = [];
  committeeList = [];
  typeQualifierList = [];
  isCheckListChecked = [];
  showCheckListComment = [];
  adminReviewer: any = {};
  adminReviewerComment: any = {};
  submissionVo: any = {};
  isEditIRBReviewer = false;
  IRBReviweverSelectedRow = null;

// Add Committee fields here only

  adminListDataSource: CompleterData;
  private $subscription: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
  private _irbViewService: IrbViewService, private _completerService: CompleterService,
  private _sharedDataService: SharedDataService) { }

  ngOnInit() {
    this.getLookUpData();
    this.$subscription = this._sharedDataService.viewProtocolDetailsVariable.subscribe(data => {
      if (data !== undefined && data != null) {
        this.headerDetails = data;
        this.getIRBAdminReviewers();
        this.getIRBAdminReviewDetails();
        this.getSubmissionBasicDetails();
      }
    });
  }
  ngOnDestroy() {
    if (this.$subscription) {
      this.$subscription.unsubscribe();
    }
  }

  getLookUpData() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._irbViewService.getSubmissionLookups(null).subscribe(data => {
      this.lookUpData  = data || [];
      this.irbAdminsReviewerType = this.lookUpData.irbAdminsReviewerType != null ? this.lookUpData.irbAdminsReviewerType : [];
      this.irbAdminsList = this.lookUpData.irbAdminsList != null ? this.lookUpData.irbAdminsList : [];
      this.adminListDataSource = this._completerService.local(this.irbAdminsList, 'FULL_NAME,PERSON_ID', 'FULL_NAME');
      this.submissionRewiewTypeList = this.lookUpData.submissionRewiewTypeList != null ? this.lookUpData.submissionRewiewTypeList : [];
      this.submissionTypeList = this.lookUpData.submissionTypeList != null ? this.lookUpData.submissionTypeList : [];
      this.committeeList = this.lookUpData.committeeList != null ? this.lookUpData.committeeList : [];
      this.typeQualifierList = this.lookUpData.typeQualifierList != null ? this.lookUpData.typeQualifierList : [];
    });
  }

  getIRBAdminReviewers() {
    const reqstObj = {submissionId: this.headerDetails.SUBMISSION_ID};
    this._irbViewService.getIRBAdminReviewers(reqstObj).subscribe(data => {
      const result: any = data || [];
       this.submissionVo.irbAdminsReviewers  = result.irbAdminsReviewers;
    });
  }

  getIRBAdminReviewDetails() {
    const reqstObj = {submissionId: this.headerDetails.SUBMISSION_ID};
    this._irbViewService.getIRBAdminReviewDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminCheckList  = result.irbAdminCheckList;
      this.submissionVo.irbAdminCommentAttachment  = result.irbAdminCommentAttachment;
    });
  }

  getSubmissionBasicDetails() {
    const reqstObj = {submissionId: this.headerDetails.SUBMISSION_ID};
    this._irbViewService.getSubmissionBasicDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
       this.submissionVo.submissionDetail  = result.submissionDetail;
    });
  }

  setAdmin(event) {
    this.adminReviewer.personID  = event.originalObject.PERSON_ID;
  }

  addAdminReviwever(mode) {
    this.adminReviewer.acType = mode;
    this.adminReviewer.statusFlag = 'N';
    this.adminReviewer.submissionId = this.headerDetails.SUBMISSION_ID;
    this.adminReviewer.protocolId = this.headerDetails.PROTOCOL_ID;
    this.adminReviewer.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    this.adminReviewer.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    this.adminReviewer.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    this.adminReviewer.updateUser = this.userDTO.userName;
    this._irbViewService.updateIRBAdminReviewer(this.adminReviewer).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminsReviewers  = result.irbAdminsReviewers;
    });
  }

  editIRBReviewer(item, index) {
    this.isEditIRBReviewer = true;
    this.IRBReviweverSelectedRow = index;
    this.adminReviewer.personID  = item.PERSON_ID;
    this.adminReviewer.personName = item.FULL_NAME;
    this.adminReviewer.reviewTypeCode = item.REVIEW_TYPE_CODE;
  }


  addIRBReviewComments(mode) {
    this.adminReviewerComment.acType = mode;
    this.adminReviewerComment.submissionId = this.headerDetails.SUBMISSION_ID;
    this.adminReviewerComment.protocolId = this.headerDetails.PROTOCOL_ID;
    this.adminReviewerComment.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    this.adminReviewerComment.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    this.adminReviewerComment.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    this.adminReviewerComment.publicFLag = this.adminReviewerComment.publicFLag === true ? 'Y' : 'N';
    this.adminReviewerComment.updateUser = this.userDTO.userName;
    this._irbViewService.updateIRBAdminComment(this.adminReviewerComment).subscribe(data => {
      const result: any = data || [];
    //  this.submissionVo.irbAdminsReviewers  = result.irbAdminsReviewers;
    });
  }



  // Add Commitee methods here only
}
