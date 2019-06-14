import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ISubscription } from 'rxjs/Subscription';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

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
  showCheckListComment = [];
  irbAdminCheckListBackUp = [];
  irbAdminCommentAttachmentBackUp = [];
  adminReviewer: any = {};
  editadminReviewer: any = {};
  adminReviewerComment: any = {};
  selectedPastSubmission: any = {};
  submissionVo: any = {};
  isEditIRBReviewer = false;
  IRBReviweverSelectedRow = null;
  showCheckList = null;
  publicFLag = false;
  adminPanelValidation = false;
  tabSelected = 'IRBCOMMENTS';
  showReviewsOf = 'All Administrative Reviewers';
  validationTextadmin = null;
  viewMode = false;

  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  fil: FileList;
  attachmentList: any[] = [];

  // Add Committee fields here only
  scheduleList: any = [];
  committeeReviewTypes = [];
  invalidData = {
    invalidBasicDetails: false, invalidProtocolReviewer: false, invalidReviewer: false,
    invalidReviewComments: false
  };
  committeeMemberList: any = [];
  recomendedActionType: any = [];
  scheduleID: string;
  committeeID: string;
  protocolReviewer: any = {
    protocolReviewerId: null,
    personID: null,
    selectedRecommendedAction: null,
    committeeMemberOnlineReviewerId: null,
    committeeReviewerStatusCode: null,
    committeeMemberDueDate: null,
    committeeReviewerTypeCode: null,
    committeeMemberAssignedDate: null,
    protocolNumber: null,
    protocolId: null,
    submissionId: null,
    submissionNumber: null,
    sequenceNumber: null,
    updateUser: null,
    statusFlag: 'N'
  };
  protocolReviewerEdit: any = {};
  submissionDate = null;
  votingComments: any = {};
  protocolReviewComments: any = {};
  committeeReviewers: any = [];
  committeeReviewerCommentsandAttachment = [];
  isEditProtocolReviewer = false;
  minuteFlag = true;
  letterFlag = true;
  protocolReviewerSelectedRow = null;
  reviewedBy = null;
  showProtocolReviewsOf = 'All Protocol Reviewers';
  attachmentDescription = '';
  tabSelectedCommittee: string;
  warningMessage: string;
  adminListDataSource: CompleterData;
  committeeReviewerSource: CompleterData;

  private $subscription: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _irbViewService: IrbViewService, private _completerService: CompleterService,
    private _sharedDataService: SharedDataService, private _spinner: NgxSpinnerService, public toastr: ToastsManager) {
      this.adminReviewer.reviewTypeCode = null;
    }

  ngOnInit() {
    this.getLookUpData();
    this.$subscription = this._sharedDataService.viewProtocolDetailsVariable.subscribe(data => {
      if (data !== undefined && data != null) {
        this.headerDetails = Object.assign({}, data);
        this.getIRBAdminReviewers();
        this.getIRBAdminReviewDetails();
        this.getSubmissionBasicDetails();
        this.getSubmissionHistory();
        this.loadCommitteeReviewerDetails();
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
      this.typeQualifierList = this.lookUpData.typeQualifierList != null ? this.lookUpData.typeQualifierList : [];
      this.committeeReviewTypes = this.lookUpData.committeeReviewType != null ? this.lookUpData.committeeReviewType : [];
      this.recomendedActionType = this.lookUpData.recomendedActionType != null ? this.lookUpData.recomendedActionType : [];
    });
  }

  getSubmissionHistory() {
    const reqstObj = {
      protocolNumber: this.headerDetails.PROTOCOL_NUMBER,
      protocolId: this.headerDetails.PROTOCOL_ID,
      sequenceNumber: this.headerDetails.SEQUENCE_NUMBER
    };
    this._irbViewService.getSubmissionHistory(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.submissionHistory = result.submissionHistory;
    });
  }

  getPastSubmission() {
    const reqstObj = {
      protocolNumber: this.headerDetails.PROTOCOL_NUMBER,
      protocolId: this.headerDetails.PROTOCOL_ID,
      sequenceNumber: this.headerDetails.SEQUENCE_NUMBER
    };
    this._irbViewService.getPastSubmission(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.pastSubmission = result.pastSubmission != null ? result.pastSubmission : [];
    });
  }

  loadPastSubmissionById(submission) {
    document.getElementById('pastSubCloseButton').click();
    this.selectedPastSubmission = submission;
    this.headerDetails.SUBMISSION_ID = submission.SUBMISSION_ID;
    this.headerDetails.SUBMISSION_DATE = submission.SUBMISSION_DATE;
    this.headerDetails.SUBMITTED_USER = submission.SUBMITTED_USER;
    this.getIRBAdminReviewers();
    this.getIRBAdminReviewDetails();
    this.getSubmissionBasicDetails();
    this.getSubmissionHistory();
    this.loadCommitteeReviewerDetails();
    this.viewMode = true;
  }

  showCurrentSubmission() {
    this.$subscription = this._sharedDataService.viewProtocolDetailsVariable.subscribe(data => {
      if (data !== undefined && data != null) {
        this.headerDetails = Object.assign({}, data);
        this.getIRBAdminReviewers();
        this.getIRBAdminReviewDetails();
        this.getSubmissionBasicDetails();
        this.getSubmissionHistory();
        this.loadCommitteeReviewerDetails();
        this.viewMode = false;
      }
    });
  }

  getIRBAdminReviewers() {
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._irbViewService.getIRBAdminReviewers(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.submissionVo.irbAdminsReviewers = result.irbAdminsReviewers;
    });
  }

  getIRBAdminReviewDetails() {
    this.showReviewsOf = 'All Administrative Reviewers';
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._irbViewService.getIRBAdminReviewDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
      this.irbAdminCheckListBackUp = Object.assign([], result.irbAdminCheckList);
      this.irbAdminCommentAttachmentBackUp = Object.assign([], result.irbAdminCommentAttachment);
      this.submissionVo.irbAdminCheckList = result.irbAdminCheckList;
      this.submissionVo.irbAdminCommentAttachment = result.irbAdminCommentAttachment;
    });
  }

  getSubmissionBasicDetails() {
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID };
    this._spinner.show();
    this._irbViewService.getSubmissionBasicDetails(reqstObj).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      this.submissionVo.committeeMemberList = result.committeeMemberList;
      this.submissionVo.committeeList = result.committeeList;
      this.submissionVo.committeeName = result.committeeName;
      this.submissionVo.committeeReviewers = result.committeeReviewers;
      this.submissionVo.reviewTypeCode = result.reviewTypeCode;
      this.submissionVo.sceduleId = result.sceduleId;
      this.submissionVo.scheduleDates = result.scheduleDates != null ? result.scheduleDates : [];
      this.submissionVo.selectedDate = result.selectedDate;
      this.submissionVo.submissionDetail = result.submissionDetail;
      this.submissionVo.submissionId = result.submissionId;
      this.submissionVo.submissionQualifierCode = result.submissionQualifierCode;
      this.submissionVo.submissionReviewType = result.submissionReviewType;
      this.submissionVo.submissionType = result.submissionType;
      this.submissionVo.submissionTypeCode = result.submissionTypeCode;
      this.submissionVo.successCode = result.successCode;



      this.scheduleList = result.scheduleDates != null ? result.scheduleDates : [];
      this.committeeList = result.committeeList != null ? result.committeeList : [];
      this.committeeID = this.submissionVo.committeeId != null ? this.submissionVo.committeeId :
        (this.committeeList.length > 0 ? this.committeeList[0].COMMITTEE_ID : null);
      this.scheduleID = this.submissionVo.sceduleId != null ?
        this.submissionVo.sceduleId : (this.scheduleList.length > 0 ? this.scheduleList[0].SCHEDULE_ID : null);
      if (this.scheduleID != null) {
        this.loadCommitteeMembers(this.scheduleID.toString());
      }
      this.committeeReviewers = this.submissionVo.committeeReviewers != null ? this.submissionVo.committeeReviewers : [];
    });
  }

  setAdmin(event) {
    this.adminReviewer.personID = event.originalObject.PERSON_ID;
  }

  addAdminReviwever(mode) {
    this.adminPanelValidation = false;
    if (this.submissionVo.irbAdminsReviewers != null && this.submissionVo.irbAdminsReviewers.length > 0) {
      const exist = this.submissionVo.irbAdminsReviewers.filter(x => x.PERSON_ID === this.adminReviewer.personID);
      this.adminPanelValidation = exist.length === 0 ? false : true;
      this.validationTextadmin = 'Admin already added!';
    }
    if (!this.adminPanelValidation) {
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
  }

  editIRBReviewer(item, index) {
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
    this._spinner.show();
    this._irbViewService.updateIRBAdminReviewer(requestObj).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      this.submissionVo.irbAdminsReviewers = result.irbAdminsReviewers;
      this.adminReviewer = {};
      this.IRBReviweverSelectedRow = null;
      this.isEditIRBReviewer = false;
      this.submissionVo.successCode = result.successCode;
        this.submissionVo.successMessage = result.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Adminstrative Reviewer', null, { toastLife: 2000 });
        }
    });
  }


  addIRBReviewComments(mode) {
    this.adminReviewerComment.acType = mode;
    this.adminReviewerComment.submissionId = this.headerDetails.SUBMISSION_ID;
    this.adminReviewerComment.protocolId = this.headerDetails.PROTOCOL_ID;
    this.adminReviewerComment.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    this.adminReviewerComment.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    this.adminReviewerComment.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    this.adminReviewerComment.publicFLag = this.publicFLag === true ? 'Y' : 'N';
    this.adminReviewerComment.updateUser = this.userDTO.userName;
    this.adminReviewerComment.personID = this.userDTO.personID;
    this._spinner.show();
    this._irbViewService.updateIRBAdminComment(this.adminReviewerComment).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      this.irbAdminCommentAttachmentBackUp = Object.assign([], result.irbAdminCommentAttachment);
      this.submissionVo.irbAdminCommentAttachment = result.irbAdminCommentAttachment;
      this.showReviewsOf = 'All Administrative Reviewers';
      this.adminReviewerComment = {};
      this.publicFLag = false;
      this.submissionVo.successCode = result.successCode;
        this.submissionVo.successMessage = result.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Adminstrative Reviewer Comment', null, { toastLife: 2000 });
        }
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

  addAttachments(attachmentDescription) {
    const reqstObj: any = {};
    reqstObj.acType = 'I';
    reqstObj.submissionId = this.headerDetails.SUBMISSION_ID;
    reqstObj.protocolId = this.headerDetails.PROTOCOL_ID;
    reqstObj.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    reqstObj.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    reqstObj.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    reqstObj.publicFLag = this.publicFLag === true ? 'Y' : 'N';
    reqstObj.updateUser = this.userDTO.userName;
    reqstObj.personID = this.userDTO.personID;
    reqstObj.comment = attachmentDescription;
    this._spinner.show();
    this._irbViewService.updateIRBAdminAttachments(reqstObj, this.uploadedFile).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      document.getElementById('closeButton').click();
      this.submissionVo.irbAdminCommentAttachment = result.irbAdminCommentAttachment;
      this.showReviewsOf = 'All Administrative Reviewers';
      this.submissionVo.successCode = result.successCode;
        this.submissionVo.successMessage = result.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Attachments', null, { toastLife: 2000 });
        }
    });
  }

  downloadAdminRevAttachment(attachment) {
    this._spinner.show();
    this._irbViewService.downloadAdminRevAttachment(attachment.attachmentId).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = attachment.fileName;
      document.body.appendChild(a);
      a.click();
      this._spinner.hide();
    });
  }

  clearAttachmentPopup() {
    this.uploadedFile = [];
    this.publicFLag = false;
    this.attachmentDescription = '';
  }

  showReviewById(irbAdminsReviewer) {
    this.showReviewsOf = irbAdminsReviewer.FULL_NAME;
    this.submissionVo.irbAdminCommentAttachment =
      this.irbAdminCommentAttachmentBackUp.filter(x => x.personId === irbAdminsReviewer.PERSON_ID);
    // const id = document.getElementById('commentdiv');
    // if (id) {
    //   id.scrollIntoView({ behavior: 'smooth' });
    // }
  }

  showChecklistByStatus(status) {
    if (status === 'null') {
      this.getIRBAdminReviewDetails();
    } else {
      this.submissionVo.irbAdminCheckList = this.irbAdminCheckListBackUp.filter(x => x.statusFlag.toString() === status);
    }
  }

  ChangeChecklistTick(checkList) {
    if (checkList.adminCheckListId == null) {
      checkList.actype = 'I';
    } else if (checkList.adminCheckListId != null && checkList.statusFlag === true) {
      checkList.actype = 'U';
    } else if (checkList.adminCheckListId != null && checkList.statusFlag === false) {
      checkList.actype = 'D';
    }
  }

  ChangeChecklistComment(checkList) {
    if (checkList.adminCheckListId == null) {
      checkList.actype = 'I';
    } else {
      checkList.actype = 'U';
    }
  }

  updateIRBAdminCheckList() {
    const reqstObj: any = {};
    reqstObj.submissionId = this.headerDetails.SUBMISSION_ID;
    reqstObj.protocolId = this.headerDetails.PROTOCOL_ID;
    reqstObj.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    reqstObj.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    reqstObj.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    reqstObj.irbAdminCheckList = this.submissionVo.irbAdminCheckList;
    reqstObj.updateUser = this.userDTO.userName;
    this._spinner.show();
    this._irbViewService.updateIRBAdminCheckList(reqstObj).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      this.irbAdminCheckListBackUp = Object.assign([], result.irbAdminCheckList);
      this.submissionVo.irbAdminCheckList = result.irbAdminCheckList;
      this.submissionVo.successCode = result.successCode;
        this.submissionVo.successMessage = result.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Update Checklist', null, { toastLife: 2000 });
        }
    });
  }



  // Add Commitee methods here only
  getScheduleLookUp(committeId) {
    this._spinner.show();
    this._irbViewService.getCommitteeScheduledDates(committeId).subscribe((data: any) => {
      this._spinner.hide();
      this.scheduleList = data.scheduleDates != null ? data.scheduleDates : [];
      this.scheduleID = this.scheduleList.length > 0 ? this.scheduleList[0].SCHEDULE_ID : null;
    });
  }

  saveSubmissionBasicDetails() {
    if (this.submissionVo.submissionTypeCode == null) {
      this.invalidData.invalidBasicDetails = true;
    } else {
      this.invalidData.invalidBasicDetails = false;
      this.submissionVo.submissionId = this.headerDetails.SUBMISSION_ID;
      this.submissionVo.committeeId = this.committeeID;
      this.submissionVo.sceduleId = this.scheduleID;
      this._spinner.show();
      this._irbViewService.updateBasicSubmissionDetail(this.submissionVo).subscribe((data: any) => {
        // this.submissionVo = data;
        const result: any = data;
        this._spinner.hide();
        this.submissionVo.committeeMemberList = result.committeeMemberList;
        this.submissionVo.committeeList = result.committeeList;
        this.submissionVo.committeeName = result.committeeName;
        this.submissionVo.committeeReviewers = result.committeeReviewers;
        this.submissionVo.reviewTypeCode = result.reviewTypeCode;
        this.submissionVo.sceduleId = result.sceduleId;
        this.submissionVo.scheduleDates = result.scheduleDates;
        this.submissionVo.selectedDate = result.selectedDate;
        this.submissionVo.submissionDetail = result.submissionDetail;
        this.submissionVo.submissionId = result.submissionId;
        this.submissionVo.submissionQualifierCode = result.submissionQualifierCode;
        this.submissionVo.submissionReviewType = result.submissionReviewType;
        this.submissionVo.submissionType = result.submissionType;
        this.submissionVo.submissionTypeCode = result.submissionTypeCode;
        this.submissionVo.successCode = result.successCode;
        this.submissionVo.successMessage = result.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Submission Details', null, { toastLife: 2000 });
        }
      });
    }
  }
  loadCommitteeMembers(scheduleId) {
    let scheduleDate;
    this._spinner.show();
    this.scheduleList.forEach(schedule => {
      this._spinner.hide();
      if (schedule.SCHEDULE_ID.toString() === scheduleId) {
        scheduleDate = this.GetFormattedDate(new Date(schedule.SCHEDULED_DATE));
      }
    });
    const reqstObj = { selectedDate: scheduleDate, committeeId: this.committeeID };
    this._irbViewService.loadCommitteeMembers(reqstObj).subscribe((data: any) => {
      this.committeeMemberList = data.committeeMemberList != null ? data.committeeMemberList : [];
      this.committeeReviewerSource = this._completerService.local(this.committeeMemberList, 'PERSON_NAME,PERSON_ID', 'PERSON_NAME');
    });
  }

  addProtocolReviewer() {
    if (this.protocolReviewer.committeeReviewerTypeCode == null || this.protocolReviewer.committeeReviewerTypeCode === 'null' ||
    this.protocolReviewer.personID == null ) {
      this.invalidData.invalidProtocolReviewer = true;
      this.warningMessage = 'Please fill all mandatory fields marked <strong>*</strong>';
    } else {
      this.invalidData.invalidProtocolReviewer = false;
        const index = (this.committeeReviewers != null && this.committeeReviewers.length > 0) ?
        this.committeeReviewers.findIndex(item => item.PERSON_ID === this.protocolReviewer.personID) : -1;
        if (index !== -1) {
          this.invalidData.invalidReviewer = true;
          this.warningMessage = 'Protocol Reviewer Already Added!';
        } else {
      this.invalidData.invalidReviewer = false;
      this.protocolReviewer.protocolId = this.headerDetails.PROTOCOL_ID;
      this.protocolReviewer.submissionId = this.headerDetails.SUBMISSION_ID;
      this.protocolReviewer.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
      this.protocolReviewer.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
      this.protocolReviewer.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
      this.protocolReviewer.updateUser = this.userDTO.userName;
      this.saveProtocolReviewer(this.protocolReviewer, 'I');
      }
    }
  }

  editProtocolReviewer(reviewer, index) {
    this.isEditProtocolReviewer = true;
    this.protocolReviewerSelectedRow = index;
    this.protocolReviewerEdit.personID = reviewer.PERSON_ID;
    this.protocolReviewerEdit.personName = reviewer.FULL_NAME;
    this.protocolReviewerEdit.selectedRecommendedAction = reviewer.REVIEW_DETERM_RECOM_CD;
    this.protocolReviewerEdit.committeeReviewerTypeCode = reviewer.REVIEWER_TYPE_CODE;
    this.protocolReviewerEdit.committeeMemberDueDate = reviewer.DUE_DATE != null ? new Date(reviewer.DUE_DATE) : null;
    this.protocolReviewerEdit.committeeMemberAssignedDate = reviewer.ASSIGNED_DATE != null ? new Date(reviewer.ASSIGNED_DATE) : null;
    this.protocolReviewerEdit.statusFlag = reviewer.STATUS_FLAG === null ? 'N' : reviewer.STATUS_FLAG;
    this.protocolReviewerEdit.protocolId = this.headerDetails.PROTOCOL_ID;
    this.protocolReviewerEdit.submissionId = this.headerDetails.SUBMISSION_ID;
    this.protocolReviewerEdit.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    this.protocolReviewerEdit.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    this.protocolReviewerEdit.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    this.protocolReviewerEdit.updateUser = this.userDTO.userName;
    this.protocolReviewerEdit.protocolReviewerId = reviewer.PROTOCOL_REVIEWER_ID;
    this.protocolReviewerEdit.committeeMemberOnlineReviewerId = reviewer.PROTOCOL_ONLN_RVW_ID;
  }

  markProtocolReviewComplete(reviewer) {
    this.protocolReviewerEdit.personID = reviewer.PERSON_ID;
    this.protocolReviewerEdit.personName = reviewer.FULL_NAME;
    this.protocolReviewerEdit.selectedRecommendedAction = reviewer.REVIEW_DETERM_RECOM_CD;
    this.protocolReviewerEdit.committeeReviewerTypeCode = reviewer.REVIEWER_TYPE_CODE;
    this.protocolReviewerEdit.committeeMemberDueDate = reviewer.DUE_DATE != null ? new Date(reviewer.DUE_DATE) : null;
    this.protocolReviewerEdit.committeeMemberAssignedDate = reviewer.ASSIGNED_DATE != null ? new Date(reviewer.ASSIGNED_DATE) : null;
    this.protocolReviewerEdit.statusFlag = 'Y';
    this.protocolReviewerEdit.protocolId = this.headerDetails.PROTOCOL_ID;
    this.protocolReviewerEdit.submissionId = this.headerDetails.SUBMISSION_ID;
    this.protocolReviewerEdit.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    this.protocolReviewerEdit.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    this.protocolReviewerEdit.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    this.protocolReviewerEdit.updateUser = this.userDTO.userName;
    this.protocolReviewerEdit.protocolReviewerId = reviewer.PROTOCOL_REVIEWER_ID;
    this.protocolReviewerEdit.committeeMemberOnlineReviewerId = reviewer.PROTOCOL_ONLN_RVW_ID;
    this.saveProtocolReviewer(this.protocolReviewerEdit, 'U');
  }

  deleteProtocolReviewer(reviewer) {
    this.protocolReviewerEdit.submissionId = this.headerDetails.SUBMISSION_ID;
    this.protocolReviewerEdit.protocolReviewerId = reviewer.PROTOCOL_REVIEWER_ID;
    this.protocolReviewerEdit.committeeMemberOnlineReviewerId = reviewer.PROTOCOL_ONLN_RVW_ID;
    this.saveProtocolReviewer(this.protocolReviewerEdit, 'D');
  }
  saveProtocolReviewer(protocolReviewer, mode) {
    protocolReviewer.acType = mode;
    if (mode === 'U') {
      protocolReviewer.committeeMemberAssignedDate = protocolReviewer.committeeMemberAssignedDate != null ?
        this.GetFormattedDate(protocolReviewer.committeeMemberAssignedDate) : null;
      protocolReviewer.committeeMemberDueDate = protocolReviewer.committeeMemberDueDate != null ?
        this.GetFormattedDate(protocolReviewer.committeeMemberDueDate) : null;
    }
    this._spinner.show();
    this._irbViewService.updateCommitteeReviewers(protocolReviewer).subscribe((data: any) => {
      this._spinner.hide();
      this.committeeReviewers = data.committeeReviewers != null ? data.committeeReviewers : [];
      if (mode === 'I') {
        this.protocolReviewer.personID = null;
        this.protocolReviewer.personName = null;
        this.protocolReviewer.committeeReviewerTypeCode = null;
      } else {
        this.protocolReviewerEdit = {};
        this.protocolReviewerSelectedRow = null;
      }
      this.submissionVo.successCode = data.successCode;
        this.submissionVo.successMessage = data.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to save Protocol Reviewer', null, { toastLife: 2000 });
        }
    });
  }

  GetFormattedDate(currentDate) {
    const month = currentDate.getMonth() + 1;
    const day = currentDate.getDate();
    const year = currentDate.getFullYear();
    return month + '-' + day + '-' + year;
  }
  setCommitteePerson(event) {
    this.protocolReviewer.personID = event.originalObject.PERSON_ID;
  }
  saveVotingComments() {
    this.submissionVo.submissionId = this.headerDetails.SUBMISSION_ID;
    this.submissionVo.sceduleId = this.scheduleID;
    this.submissionVo.committeeId = this.committeeID;
    this.submissionVo.updateUser = this.userDTO.userName;
    this._spinner.show();
    this._irbViewService.updateCommitteeVotingDetail(this.submissionVo).subscribe((data: any) => {
      this._spinner.hide();
      this.submissionVo.comment = data.comment;
      this.submissionVo.abstainCount = data.abstainCount;
      this.submissionVo.noVotingCount = data.noVotingCount;
      this.submissionVo.yesVotingCount = data.yesVotingCount;
      this.submissionVo.successCode = data.successCode;
        this.submissionVo.successMessage = data.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Voting Details', null, { toastLife: 2000 });
        }
    });
  }

  addAttachmentCommittee(attachmentComment) {
    const reqstObj: any = {};
    reqstObj.acType = 'I';
    reqstObj.submissionId = this.headerDetails.SUBMISSION_ID;
    reqstObj.protocolId = this.headerDetails.PROTOCOL_ID;
    reqstObj.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
    reqstObj.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
    reqstObj.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
    reqstObj.personID = this.userDTO.personID;
    reqstObj.flag = this.publicFLag === true ? 'Y' : 'N';
    reqstObj.updateUser = this.reviewedBy;
    reqstObj.attachmentDescription = attachmentComment;
    this._spinner.show();
    this._irbViewService.updateCommitteeReviewerAttachments(reqstObj, this.uploadedFile).subscribe((data: any) => {
      this._spinner.hide();
      this.submissionVo.committeeReviewerCommentsandAttachment = data.committeeReviewerCommentsandAttachment != null ?
    data.committeeReviewerCommentsandAttachment : [];
    this.committeeReviewerCommentsandAttachment = Object.assign([], this.submissionVo.committeeReviewerCommentsandAttachment);
    this.reviewedBy = this.userDTO.userName;
      document.getElementById('closeButton').click();
      this.submissionVo.successCode = data.successCode;
        this.submissionVo.successMessage = data.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Attachment', null, { toastLife: 2000 });
        }
    });
  }

  loadCommitteeReviewerDetails() {
    this.showProtocolReviewsOf = 'All Protocol Reviewers';
    const reqstObj = { submissionId: this.headerDetails.SUBMISSION_ID, personID: this.userDTO.personID };
    this._irbViewService.loadCommitteeReviewerDetails(reqstObj).subscribe(data => {
      const result: any = data || [];
      // this.submissionVo.irbAdminsReviewers = result.irbAdminsReviewers;
      this.submissionVo.comment = result.comment;
      this.submissionVo.abstainCount = result.abstainCount;
      this.submissionVo.noVotingCount = result.noVotingCount;
      this.submissionVo.yesVotingCount = result.yesVotingCount;
      this.submissionVo.committeeReviewerCommentsandAttachment = result.committeeReviewerCommentsandAttachment != null ?
                                                                result.committeeReviewerCommentsandAttachment : [];
     this.committeeReviewerCommentsandAttachment = Object.assign([], this.submissionVo.committeeReviewerCommentsandAttachment);
    });
  }

  addProtocolReviewComments(mode) {
  if (this.minuteFlag === false && this.letterFlag === false) {
    this.invalidData.invalidReviewComments = true;
  } else {
    this.invalidData.invalidReviewComments = false;
  this.submissionVo.acType = mode;
  this.submissionVo.protocolId = this.headerDetails.PROTOCOL_ID;
  this.submissionVo.submissionId = this.headerDetails.SUBMISSION_ID;
  this.submissionVo.submissionNumber = this.headerDetails.SUBMISSION_NUMBER;
  this.submissionVo.sequenceNumber = this.headerDetails.SEQUENCE_NUMBER;
  this.submissionVo.protocolNumber = this.headerDetails.PROTOCOL_NUMBER;
  this.submissionVo.personID = this.userDTO.personID;
  this.submissionVo.updateUser = this.reviewedBy;
  this.protocolReviewComments.flag = this.minuteFlag === true ? 'Y' : 'N';
  this.protocolReviewComments.letterFlag  = this.letterFlag === true ? 'Y' : 'N';
  this.protocolReviewComments.commMinutesScheduleId = null;
  this.protocolReviewComments.contingencyCode = null;
  this.submissionVo.irbCommitteeReviewerComments = this.protocolReviewComments;
  this._spinner.show();
  this._irbViewService.updateCommitteeReviewerComments(this.submissionVo).subscribe((data: any) => {
    this._spinner.hide();
    this.submissionVo.committeeReviewerCommentsandAttachment = data.committeeReviewerCommentsandAttachment != null ?
    data.committeeReviewerCommentsandAttachment : [];
    this.committeeReviewerCommentsandAttachment = Object.assign([], this.submissionVo.committeeReviewerCommentsandAttachment);
    this.reviewedBy = this.userDTO.userName;
    this.submissionVo.successCode = data.successCode;
        this.submissionVo.successMessage = data.successMessage;
        if (this.submissionVo.successCode === true) {
          this.toastr.success(this.submissionVo.successMessage, null, { toastLife: 2000 });
        } else {
          this.toastr.error('Failed to Save Committee Review Comments', null, { toastLife: 2000 });
        }
    this.letterFlag = true;
    this.minuteFlag = true;
    this.protocolReviewComments.comments = '';
  });
}
  }
  downloadCommitteReviwerAttachment(attachment) {
    this._spinner.show();
    this._irbViewService.downloadCommitteeFileData(attachment.REVIEWER_ATTACHMENT_ID).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = attachment.FILE_NAME;
      document.body.appendChild(a);
      this._spinner.hide();
      a.click();
    });
  }

  showProtocolReviewerCommentsById(protocolReviewer) {
    this.showProtocolReviewsOf = protocolReviewer.FULL_NAME;
    this.submissionVo.committeeReviewerCommentsandAttachment =
      this.committeeReviewerCommentsandAttachment.filter(x => x.PERSON_ID === protocolReviewer.PERSON_ID);
  }

  expandProtocolReviwers() {
    this.isExpanded = !this.isExpanded;
    this.tabSelectedCommittee =  this.isExpanded === true ? 'PROTOCOL_COMMENTS' : '';
    this.reviewedBy = this.userDTO.userName;
  }
}
