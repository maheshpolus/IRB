import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ISubscription } from 'rxjs/Subscription';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';

import { IrbViewService } from '../irb-view.service';
import { SharedDataService } from '../../common/service/shared-data.service';

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
  editadminReviewer: any = {};
  adminReviewerComment: any = {};
  submissionVo: any = {};
  isEditIRBReviewer = false;
  IRBReviweverSelectedRow = null;

  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  fil: FileList;
  attachmentList: any[] = [];

  // Add Committee fields here only

  adminListDataSource: CompleterData;
  private $subscription: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _irbViewService: IrbViewService, private _completerService: CompleterService,
    private _sharedDataService: SharedDataService) { }

  ngOnInit() {
    this.getLookUpData();
    this.getSubmissionHistory();
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
      this.lookUpData = data || [];
      this.irbAdminsReviewerType = this.lookUpData.irbAdminsReviewerType != null ? this.lookUpData.irbAdminsReviewerType : [];
      this.irbAdminsList = this.lookUpData.irbAdminsList != null ? this.lookUpData.irbAdminsList : [];
      this.adminListDataSource = this._completerService.local(this.irbAdminsList, 'FULL_NAME,PERSON_ID', 'FULL_NAME');
      this.submissionRewiewTypeList = this.lookUpData.submissionRewiewTypeList != null ? this.lookUpData.submissionRewiewTypeList : [];
      this.submissionTypeList = this.lookUpData.submissionTypeList != null ? this.lookUpData.submissionTypeList : [];
      this.committeeList = this.lookUpData.committeeList != null ? this.lookUpData.committeeList : [];
      this.typeQualifierList = this.lookUpData.typeQualifierList != null ? this.lookUpData.typeQualifierList : [];
    });
  }

  getSubmissionHistory() {

  }

  getIRBAdminReviewers() {
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._irbViewService.getIRBAdminReviewers(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminsReviewers = result.irbAdminsReviewers;
    });
  }

  getIRBAdminReviewDetails() {
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._irbViewService.getIRBAdminReviewDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminCheckList = result.irbAdminCheckList;
      this.submissionVo.irbAdminCommentAttachment = result.irbAdminCommentAttachment;
    });
  }

  getSubmissionBasicDetails() {
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._irbViewService.getSubmissionBasicDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.submissionDetail = result.submissionDetail;
    });
  }

  setAdmin(event) {
    this.adminReviewer.personID = event.originalObject.PERSON_ID;
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
    this.saveOrUpdateAdminReviwever(this.adminReviewer);
  }

  editIRBReviewer(item, index) {debugger
    this.isEditIRBReviewer = true;
    this.IRBReviweverSelectedRow = index;
    this.editadminReviewer.acType = 'U';
    this.editadminReviewer.adminReviewerId = item.ADMIN_REVIEWER_ID;
    this.editadminReviewer.personID = item.PERSON_ID;
    this.editadminReviewer.personName = item.FULL_NAME;
    this.editadminReviewer.reviewTypeCode = item.REVIEW_TYPE_CODE;
    this.editadminReviewer.statusFlag = item.STATUS_FLAG;
    this.editadminReviewer.submissionId = item.SUBMISSION_ID;
    this.editadminReviewer.protocolId = item.PROTOCOL_ID;
    this.editadminReviewer.protocolNumber = item.PROTOCOL_NUMBER;
    this.editadminReviewer.sequenceNumber = item.SEQUENCE_NUMBER;
    this.editadminReviewer.submissionNumber = item.SUBMISSION_NUMBER;
    this.editadminReviewer.updateUser = this.userDTO.userName;
  }

  deleteIRBAdmin(item) {
    const reqstObj: any = {};
    reqstObj.adminReviewerId = item.ADMIN_REVIEWER_ID;
    reqstObj.submissionId = item.SUBMISSION_ID;
    reqstObj.acType = 'D';
    this.saveOrUpdateAdminReviwever(reqstObj);
  }

  markReviewComplete(item) {
    const reqstObj: any = {};
    reqstObj.acType = 'U';
    reqstObj.statusFlag = 'Y';
    reqstObj.adminReviewerId = item.ADMIN_REVIEWER_ID;
    reqstObj.submissionId = item.SUBMISSION_ID;
    reqstObj.sequenceNumber = item.SEQUENCE_NUMBER;
    reqstObj.personName = item.FULL_NAME;
    reqstObj.personID = item.PERSON_ID;
    reqstObj.reviewTypeCode = item.REVIEW_TYPE_CODE;
    reqstObj.protocolId = item.PROTOCOL_ID;
    reqstObj.protocolNumber = item.PROTOCOL_NUMBER;
    reqstObj.updateUser = this.userDTO.userName;
    this.saveOrUpdateAdminReviwever(reqstObj);
  }

  saveOrUpdateAdminReviwever(requestObj) {
    this._irbViewService.updateIRBAdminReviewer(requestObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminsReviewers = result.irbAdminsReviewers;
      this.adminReviewer = {};
      this.IRBReviweverSelectedRow = null;
      this.isEditIRBReviewer = false;
    });
  }


  addIRBReviewComments(mode) {debugger
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
      this.submissionVo.irbAdminCommentAttachment  = result.irbAdminCommentAttachment;
    });
  }


  onChange(files: FileList) {
    this.fil = files;
    for (let index = 0; index < this.fil.length; index++) {
      this.uploadedFile.push(this.fil[index]);
    }
   // this.changeRef.detectChanges();
  }

  /** Push the unique files dropped to uploaded file
  * @param files- files dropped
  */
  public dropped(event: UploadEvent) {
    this.files = event.files;
    for (const file of this.files) {
      this.attachmentList.push(file);
    }
    for (const file of event.files) {
      if (file.fileEntry.isFile) {
        const fileEntry = file.fileEntry as FileSystemFileEntry;
        fileEntry.file((info: File) => {
          this.uploadedFile.push(info);
        //  this.changeRef.detectChanges();
        });
      }
    }
  }

  /**Remove an item from the uploded file
   * @param item-item to be removed
   */
  deleteFromUploadedFileList(item) {
    for (let i = 0; i < this.uploadedFile.length; i++) {
      if (this.uploadedFile[i].name === item.name) {
        this.uploadedFile.splice(i, 1);
     //   this.changeRef.detectChanges();
      }
    }
  }

  /* Trigger the 'choose File' in attachment modal */
  triggerAdd() {
    document.getElementById('addAttach').click();
  }

  addAttachments(attachmentDescription) {debugger
    const reqstObj: any = {};
    reqstObj.acType = 'I';
    reqstObj.submissionId = this.headerDetails.SUBMISSION_ID;
    reqstObj.protocolId = this.headerDetails.PROTOCOL_ID;
    reqstObj.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    reqstObj.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    reqstObj.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    reqstObj.publicFLag = 'N';
    reqstObj.updateUser = this.userDTO.userName;
    reqstObj.comment = attachmentDescription;
    this._irbViewService.updateIRBAdminAttachments(reqstObj, this.uploadedFile).subscribe(data => {
       const result: any = data || [];
       this.submissionVo.irbAdminCommentAttachment  = result.irbAdminCommentAttachment;
    });
  }



  // Add Commitee methods here only
}
