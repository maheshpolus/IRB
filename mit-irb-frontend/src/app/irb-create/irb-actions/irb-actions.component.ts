import { Component, OnInit, OnDestroy, ViewContainerRef, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { ToastsManager, Toast } from 'ng2-toastr/ng2-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';

import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-actions',
  templateUrl: './irb-actions.component.html',
  styleUrls: ['./irb-actions.component.css'],
  providers: [IrbCreateService]
})
export class IrbActionsComponent implements OnInit, OnDestroy {

  protocolNumber = null;
  protocolId = null;
  currentActionDate = null;
  generalInfo = {};
  userDTO: any = {};
  IRBActionsVO: any = {};
  IRBActionsResult: any = {};
  commonVo: any = {};
  currentActionPerformed: any = {};
  moduleAvailableForAmendment: any = [];
  cannedCommentList: any = [];
  isItemChecked: any = [];
  checkListSelectedCodes: any = [];
  expeditedCheckList: any = [];
  notifyTypeQualifier: any = [];
  commentList: any = [];
  scheduleDateList: any = [];
  committeeList: any = [];
  riskLevelType: any = [];
  fdaRiskLevelType = [];
  personnelInfoList = [];
  leadUnitList = [];
  personActionsList = [];
  submissionTypes = [];
  typeQualifier = [];
  reviewTypes = [];
  committees = [];
  scheduleDates = [];
  createOrViewPath: string;
  actionButtonName: string;
  selectedCommitteeId: string;
  tabSelected: string;
  selectedScheduleId: string;
  minuteFlag = true;
  letterFlag = true;
  publicFlag = false;

  riskLevelDetail = {
    riskLevelCode: null,
    fdaRiskLevelCode: null,
    riskLevelDateAssigned: null,
    fdariskLevelDateAssigned: null,
    riskLevelComment: null,
    fdaRiskLevelComment: null,
    submissionId: null,
    protocolId: null,
    protocolNumber: null,
    updateUser: null
  };

  commentObject = {
    comments: '',
    flag: 'Y',
    letterFlag: 'Y',
    contingencyCode: ''
  };

  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  fil: FileList;
  attachmentList: any[] = [];

  private $subscription1: ISubscription;
  private $subscription2: ISubscription;

  invalidData = {
    noPiExists: true, noLeadUnit: true, invalidReviewComments: false
  };


  constructor(private _activatedRoute: ActivatedRoute, private _router: Router, private _irbCreateService: IrbCreateService,
    private _sharedDataService: SharedDataService, public toastr: ToastsManager, vcr: ViewContainerRef,
    private _spinner: NgxSpinnerService, public changeRef: ChangeDetectorRef) {
    // this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this.submissionTypes = [{ type: 'Initial Protocol Application for Approval' },
    { type: 'Continuing Review/Continuation without Amendment' },
    { type: 'Amendment' },
    { type: 'Response to Previous IRB Notification' },
    { type: 'Self Report of Non-Compliance' },
    { type: 'Complaint' }
    ];
    this.typeQualifier = [{ type: 'AE/UADE' },
    { type: 'Annual Report' },
    { type: 'Annual Scheduled by IRB' },
    { type: 'Contingent/Conditional Approval/Deffered Approval/Non Approval' },
    { type: 'DSMB Report' },
    { type: 'Deviation' },
    { type: 'Eligible Exceptions/Protocol Deviation' },
    ];
    this.reviewTypes = [{ type: 'Full' },
    { type: 'Expedited' },
    { type: 'Exempt' },
    { type: 'Committee' },
    { type: 'COUHES' },
    ];
    this.scheduleDates = [{ type: '10-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '11-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '12-06-2018, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '01-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '02-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '30-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    { type: '04-06-2019, [COUHES - Room # 15-34], 12.00 PM' },
    ];
    this.committees = [{ type: 'COUHES' },
    ];
    this.loadInitDetails();

  }

  loadInitDetails() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
    });
    this.createOrViewPath = this._router.parseUrl(this._router.url).root.children['primary'].segments[1].path;
    if (this.createOrViewPath === 'irb-create') {
      this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
        if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
          this.commonVo = commonVo;
          // this.personnelInfoList = this.commonVo.generalInfo.personnelInfos == null ? [] : this.commonVo.generalInfo.personnelInfos;
          // this.leadUnitList = this.commonVo.protocolLeadUnitsList == null ? [] : this.commonVo.protocolLeadUnitsList;
          this.IRBActionsVO.protocolStatus = this.commonVo.generalInfo.protocolStatus.protocolStatusCode;
          this.IRBActionsVO.submissionStatus = this.commonVo.generalInfo.protocolSubmissionStatuses == null ? null :
            this.commonVo.generalInfo.protocolSubmissionStatuses.submissionStatusCode;
          this.IRBActionsVO.sequenceNumber = this.commonVo.generalInfo.sequenceNumber;
          this.IRBActionsVO.submissionNumber = this.commonVo.generalInfo.protocolSubmissionStatuses == null ? null :
            this.commonVo.generalInfo.protocolSubmissionStatuses.submissionNumber;
          this.getAvailableActions();
        }
      });
    } else {
      this.$subscription2 = this._sharedDataService.viewProtocolDetailsVariable.subscribe(commonVo => {
        if (commonVo !== undefined && commonVo != null) {
          this.commonVo = commonVo;
          this.IRBActionsVO.protocolStatus = this.commonVo.PROTOCOL_STATUS_CODE;
          this.IRBActionsVO.submissionStatus = this.commonVo.SUBMISSION_STATUS_CODE;
          this.IRBActionsVO.sequenceNumber = this.commonVo.SEQUENCE_NUMBER;
          this.IRBActionsVO.submissionNumber = this.commonVo.SUBMISSION_NUMBER;
          this.IRBActionsVO.submissionId = this.commonVo.SUBMISSION_ID;
          this.IRBActionsVO.protocolNumber = this.protocolNumber;
          this.IRBActionsVO.protocolId = this.protocolId;
          this.getAvailableActions();
        }
      });
    }
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
    if (this.$subscription2) {
      this.$subscription2.unsubscribe();
    }
  }

  getAvailableActions() {
    this.getActionLookup();
    this.IRBActionsVO.protocolNumber = this.protocolNumber;
    this.IRBActionsVO.protocolId = this.protocolId;
    this.IRBActionsVO.personID = this.userDTO.personID;
    this._spinner.show();
    this._irbCreateService.getAvailableActions(this.IRBActionsVO).subscribe(data => {
      this._spinner.hide();
      this.IRBActionsVO = data;
      this.personActionsList = this.IRBActionsVO.personActionsList;
      // this.moduleAvailableForAmendment = this.IRBActionsVO.moduleAvailableForAmendment;
      if (this.personActionsList != null && this.personActionsList.length > 0) {
        this._spinner.show();
        setTimeout(() => { this.setActionIcons(); }, 1000);
      }
    });
  }
  getActionLookup() {
    this._irbCreateService.getActionLookup(this.IRBActionsVO).subscribe((data: any) => {
      this.moduleAvailableForAmendment = data.moduleAvailableForAmendment != null ? data.moduleAvailableForAmendment : [];
      this.notifyTypeQualifier = data.notifyTypeQualifier != null ? data.notifyTypeQualifier : [];
      this.expeditedCheckList = data.expeditedApprovalCheckList != null ? data.expeditedApprovalCheckList : [];
      this.riskLevelType = data.riskLevelType != null ? data.riskLevelType : [];
      this.fdaRiskLevelType = data.fdaRiskLevelType != null ? data.fdaRiskLevelType : [];
      this.committeeList = data.committeeList != null ? data.committeeList : [];
      this.selectedCommitteeId = this.committeeList[0].COMMITTEE_ID;
      this.scheduleDateList = data.scheduleDates != null ? data.scheduleDates : [];
      this.selectedScheduleId = this.scheduleDateList.length > 0 ? this.scheduleDateList[0].SCHEDULE_ID : null;
      this.cannedCommentList = data.expeditedCannedComments != null ? data.expeditedCannedComments : [];
    });
  }

  setActionIcons() {
    this._spinner.hide();
    this.personActionsList.forEach((personAction, index) => {
      document.getElementById('icon' + index).className = personAction.ICON_CLASS_NAME;
    });

  }
  openActionDetails(action) {
    this.currentActionPerformed = action;
    this.uploadedFile = [];
    this.commentList = [];
    if (action.ACTION_CODE === '101') { // Submit
      document.getElementById('submitModalBtn').click();
    } else if (action.ACTION_CODE === '992' || action.ACTION_CODE === '303' ||
      action.ACTION_CODE === '213' || action.ACTION_CODE === '300'
      || action.ACTION_CODE === '113' || action.ACTION_CODE === '119' ||
      action.ACTION_CODE === '211' || action.ACTION_CODE === '212' || action.ACTION_CODE === '910' || action.ACTION_CODE === '113') {
      if (action.ACTION_CODE === '213') {
        this.actionButtonName = 'Return';
      } else if (action.ACTION_CODE === '300') {
        this.actionButtonName = 'Close';
      } else {
        this.actionButtonName = 'Save';
      }
      // Delete Protocol, Withdraw
      // return to PI, Close, disapprove, irb-Acknowledgment, data-Analysis-only, reopen enrollment, closed for enrollment
      // terminated, suspended
      if (action.ACTION_CODE === '213' || action.ACTION_CODE === '300' ||
        action.ACTION_CODE === '211' || action.ACTION_CODE === '212'
        || action.ACTION_CODE === '201' ||
        action.ACTION_CODE === '910' || action.ACTION_CODE === '113' || action.ACTION_CODE === '119') {
        this.IRBActionsVO.actionDate = new Date();
      }
      document.getElementById('commentModalBtn').click();
    } else if (action.ACTION_CODE === '114' || action.ACTION_CODE === '105' || action.ACTION_CODE === '116' ||
      action.ACTION_CODE === '108' || action.ACTION_CODE === '115') {
      // Rqst Data Analysis, Rqst to close, notify irb, rqst close enrollment,rqst reopen enrollment, make admin corrections
      document.getElementById('commentAttachmentModalBtn').click();
    } else if (action.ACTION_CODE === '102' || action.ACTION_CODE === '103') {
      // create renewal,Create amendment,modify-amedment
      document.getElementById('commentCheckboxModalBtn').click();
    } else if (action.ACTION_CODE === '911') { // copy protocol
      document.getElementById('confirmModalBtn').click();
    } else if (action.ACTION_CODE === '206') {
      this.IRBActionsVO.actionDate = new Date();
      // grant exemption, approved, response approval <-- Approval date
      this.IRBActionsVO.approvalDate = new Date();
      this.actionButtonName = 'Assign';
      // In Agenda, Grant Exemption, IRB review not required, Approved, SMR, SRR, response approval
      document.getElementById('commentDatesModalBtn').click();
    } else if (action.ACTION_CODE === '200' || action.ACTION_CODE === '205' ||
      action.ACTION_CODE === '204' || action.ACTION_CODE === '202' || action.ACTION_CODE === '301' || action.ACTION_CODE === '302' ||
      action.ACTION_CODE === '203' || action.ACTION_CODE === '304' || action.ACTION_CODE === '209' || action.ACTION_CODE === '201' ||
      action.ACTION_CODE === '210' || action.ACTION_CODE === '208' || action.ACTION_CODE === '207') {
      setTimeout(() => {
        const id = document.getElementById('actionScreen');
        if (id) {
          id.scrollIntoView({ behavior: 'smooth' });
        }
      }, 300);
      this.IRBActionsVO.actionDate = new Date();
      this.tabSelected = 'COMMENTS';
      if (action.ACTION_CODE === '205' || action.ACTION_CODE === '204' || action.ACTION_CODE === '208') {
        this.IRBActionsVO.expirationDate = new Date();
        const todayDate = new Date();
        this.IRBActionsVO.expirationDate.setYear(todayDate.getFullYear() + 1);
        this.IRBActionsVO.expirationDate = new Date(this.IRBActionsVO.expirationDate);
        this.IRBActionsVO.approvalDate = new Date();
      }
      if (action.ACTION_CODE === '210') {
        this.IRBActionsVO.decisionDate = new Date();
      }
      // if (action.ACTION_CODE === '200' || action.ACTION_CODE === '205' || action.ACTION_CODE === '204' || action.ACTION_CODE === '304' ||
      //  action.ACTION_CODE === '203' || action.ACTION_CODE === '202' ||  action.ACTION_CODE === '209' || action.ACTION_CODE === '201' ||
      //   action.ACTION_CODE === '301' || action.ACTION_CODE === '302' || action.ACTION_CODE === '210') {
      //   this.tabSelected = 'COMMENTS';
      // }
    }
    // else if (action.ACTION_CODE === '205') {
    //   document.getElementById('expeditedApprovalModalBtn').click();
    // }


  }

  cancelActionPerform() {
    const id = document.getElementById('actionListPanel');
    if (id) {
      id.scrollIntoView({ behavior: 'smooth' });
    }
    this.currentActionPerformed = {};
  }

  performAction() {
    if (this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '205' ||
    this.currentActionPerformed.ACTION_CODE === '204' ||
    this.currentActionPerformed.ACTION_CODE === '304' || this.currentActionPerformed.ACTION_CODE === '203' ||
    this.currentActionPerformed.ACTION_CODE === '202' || this.currentActionPerformed.ACTION_CODE === '208') {
      if (this.commentObject.comments != null || this.commentObject.comments !== '') {
        this.addReviewComments(this.commentObject);
      }
    }
    if (this.invalidData.invalidReviewComments === false) {
    this.setActionVO();
    this._spinner.show();
    this._irbCreateService.performProtocolActions(this.IRBActionsVO, this.uploadedFile).subscribe(data => {
      this._spinner.hide();
      this.IRBActionsResult = data;
      if (this.IRBActionsResult.successCode === true) {
        this.toastr.success(this.IRBActionsResult.successMessage, null, { toastLife: 2000 });
        // Submit Action,Rqst Data Analysis, Rqst to close, notify irb, rqst close enrollment,rqst reopen enrollment
        // admin actions data analysis only, reopen entrollment, close, terminated, suspended, defered
        if (this.currentActionPerformed.ACTION_CODE === '101' || this.currentActionPerformed.ACTION_CODE === '114' ||
          this.currentActionPerformed.ACTION_CODE === '105' || this.currentActionPerformed.ACTION_CODE === '116' ||
          this.currentActionPerformed.ACTION_CODE === '108' || this.currentActionPerformed.ACTION_CODE === '115' ||
          this.currentActionPerformed.ACTION_CODE === '211' || this.currentActionPerformed.ACTION_CODE === '212' ||
          this.currentActionPerformed.ACTION_CODE === '207' || this.currentActionPerformed.ACTION_CODE === '301' ||
          this.currentActionPerformed.ACTION_CODE === '302' || this.currentActionPerformed.ACTION_CODE === '201' ||
          this.currentActionPerformed.ACTION_CODE === '300' || this.currentActionPerformed.ACTION_CODE === '213' ||
          this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '119' ||
          this.currentActionPerformed.ACTION_CODE === '205' || this.currentActionPerformed.ACTION_CODE === '204' ||
          this.currentActionPerformed.ACTION_CODE === '304' || this.currentActionPerformed.ACTION_CODE === '910'
          || this.currentActionPerformed.ACTION_CODE === '209' || this.currentActionPerformed.ACTION_CODE === '210' ||
          this.currentActionPerformed.ACTION_CODE === '202' || this.currentActionPerformed.ACTION_CODE === '203' ||
          this.currentActionPerformed.ACTION_CODE === '208') {
          // this._router.navigate(['/irb/dashboard']);
          this._router.navigate(['/irb/irb-view/irbOverview'],
            { queryParams: { protocolNumber: this.protocolNumber } });
        }

        // create amendment, create renewal, withdrawn, copy protocol
        if (this.currentActionPerformed.ACTION_CODE === '103' || this.currentActionPerformed.ACTION_CODE === '102' ||
          this.currentActionPerformed.ACTION_CODE === '303' || this.currentActionPerformed.ACTION_CODE === '911') {
         // this._router.navigate(['/irb/dashboard']);
          this._router.navigate(['/irb/irb-create/irbHome'],
            { queryParams: { protocolNumber: this.IRBActionsResult.protocolNumber, protocolId: this.IRBActionsResult.protocolId } });
        }
        // Administartive correction
        if (this.currentActionPerformed.ACTION_CODE === '113') {
            this._router.navigate(['/irb/irb-create/irbHome'],
            { queryParams: { protocolNumber: this.IRBActionsResult.protocolNumber,
              protocolId: this.IRBActionsResult.protocolId, isAdminCorrection : true } });
          }

        // delete protocol
        if (this.currentActionPerformed.ACTION_CODE === '992') {
          this._router.navigate(['/irb/dashboard']);
        }
      } else {
        this.toastr.error('Failed to perform Action', null, { toastLife: 2000 });
      }
    },
      error => {
        this.toastr.error('Failed to perform Action', null, { toastLife: 2000 });
        console.log('Error in perform action ', error);
      }
    );
  }
}

  setActionVO() {
    this.IRBActionsVO.acType = 'I';
    this.IRBActionsVO.actionTypeCode = this.currentActionPerformed.ACTION_CODE;
    this.IRBActionsVO.createUser = this.userDTO.userName;
    this.IRBActionsVO.updateUser = this.userDTO.userName;
    this.IRBActionsVO.personAction = this.currentActionPerformed;
    if (this.currentActionPerformed.ACTION_CODE === '213' || this.currentActionPerformed.ACTION_CODE === '300' ||
      this.currentActionPerformed.ACTION_CODE === '304' || this.currentActionPerformed.ACTION_CODE === '209' ||
      this.currentActionPerformed.ACTION_CODE === '211' || this.currentActionPerformed.ACTION_CODE === '212' ||
      this.currentActionPerformed.ACTION_CODE === '207' || this.currentActionPerformed.ACTION_CODE === '301' ||
      this.currentActionPerformed.ACTION_CODE === '302' || this.currentActionPerformed.ACTION_CODE === '201' ||
      this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '206' ||
      this.currentActionPerformed.ACTION_CODE === '210' || this.currentActionPerformed.ACTION_CODE === '204'
      || this.currentActionPerformed.ACTION_CODE === '202' || this.currentActionPerformed.ACTION_CODE === '203' ||
      this.currentActionPerformed.ACTION_CODE === '113' || this.currentActionPerformed.ACTION_CODE === '910' ||
      this.currentActionPerformed.ACTION_CODE === '208' || this.currentActionPerformed.ACTION_CODE === '119') {
      this.IRBActionsVO.actionDate = this.IRBActionsVO.actionDate != null ?
        this.GetFormattedDate(this.IRBActionsVO.actionDate) : null;
    }
    if (this.currentActionPerformed.ACTION_CODE === '206' || this.currentActionPerformed.ACTION_CODE === '208' ||
      this.currentActionPerformed.ACTION_CODE === '204' || this.currentActionPerformed.ACTION_CODE === '205') {
      this.IRBActionsVO.approvalDate = this.IRBActionsVO.approvalDate != null ?
        this.GetFormattedDate(this.IRBActionsVO.approvalDate) : null;
    }
    if (this.currentActionPerformed.ACTION_CODE === '210') {
      this.IRBActionsVO.decisionDate = this.IRBActionsVO.decisionDate != null ?
        this.GetFormattedDate(this.IRBActionsVO.decisionDate) : null;
    }
    if (this.currentActionPerformed.ACTION_CODE === '204' || this.currentActionPerformed.ACTION_CODE === '208' ||
      this.currentActionPerformed.ACTION_CODE === '205') {
      this.IRBActionsVO.expirationDate = this.IRBActionsVO.expirationDate != null ?
        this.GetFormattedDate(this.IRBActionsVO.expirationDate) : null;
    }
    if (this.currentActionPerformed.ACTION_CODE === '212' || this.currentActionPerformed.ACTION_CODE === '200' ||
      this.currentActionPerformed.ACTION_CODE === '205' || this.currentActionPerformed.ACTION_CODE === '208') {
      this.IRBActionsVO.selectedCommitteeId = this.selectedCommitteeId;
    }
    if (this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '205' ||
      this.currentActionPerformed.ACTION_CODE === '208') {
      this.IRBActionsVO.selectedScheduleId = this.selectedScheduleId;
    }
    if (this.currentActionPerformed.ACTION_CODE === '205') {
      this.IRBActionsVO.expeditedCheckListSelectedCode = this.checkListSelectedCodes;
    }
    if (this.currentActionPerformed.ACTION_CODE === '205' || this.currentActionPerformed.ACTION_CODE === '204' ||
      this.currentActionPerformed.ACTION_CODE === '203' || this.currentActionPerformed.ACTION_CODE === '202' ||
       this.currentActionPerformed.ACTION_CODE === '208' ||
      this.currentActionPerformed.ACTION_CODE === '304') {
        // this.IRBActionsVO.riskLevelList = this.riskLevelList;
        // this.IRBActionsVO.fdaRiskLevelList = this.fdaRiskLevelList;
        this.IRBActionsVO.riskLevelDetail = this.riskLevelDetail;
        this.IRBActionsVO.riskLevelDetail.riskLevelDateAssigned = this.riskLevelDetail.riskLevelDateAssigned != null ?
           this.GetFormattedDate(this.riskLevelDetail.riskLevelDateAssigned) : null;
        this.IRBActionsVO.riskLevelDetail.fdariskLevelDateAssigned = this.riskLevelDetail.fdariskLevelDateAssigned != null ?
           this.GetFormattedDate(this.riskLevelDetail.fdariskLevelDateAssigned) : null;
    }
    if (this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '205' ||
      this.currentActionPerformed.ACTION_CODE === '204' || this.currentActionPerformed.ACTION_CODE === '202' ||
      this.currentActionPerformed.ACTION_CODE === '301' || this.currentActionPerformed.ACTION_CODE === '302' ||
      this.currentActionPerformed.ACTION_CODE === '203' || this.currentActionPerformed.ACTION_CODE === '304' ||
      this.currentActionPerformed.ACTION_CODE === '209' || this.currentActionPerformed.ACTION_CODE === '201' ||
      this.currentActionPerformed.ACTION_CODE === '210' || this.currentActionPerformed.ACTION_CODE === '208' ||
      this.currentActionPerformed.ACTION_CODE === '207') {
      this.IRBActionsVO.irbActionsReviewerComments = this.commentList;
    }
    if (this.currentActionPerformed.ACTION_CODE === '103') {
      this.IRBActionsVO.moduleAvailableForAmendment = this.moduleAvailableForAmendment;
    }
   if (this.currentActionPerformed.ACTION_CODE === '200' || this.currentActionPerformed.ACTION_CODE === '209' ||
   this.currentActionPerformed.ACTION_CODE === '201' || this.currentActionPerformed.ACTION_CODE === '301' ||
   this.currentActionPerformed.ACTION_CODE === '302' || this.currentActionPerformed.ACTION_CODE === '210') {
    this.IRBActionsVO.publicFlag = this.publicFlag === true ? 'Y' : 'N';
   }
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



  onChange(files: FileList) {
    this.fil = files;
    for (let index = 0; index < this.fil.length; index++) {
      this.uploadedFile.push(this.fil[index]);
    }
    this.changeRef.detectChanges();
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
          this.changeRef.detectChanges();
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
        this.changeRef.detectChanges();
      }
    }
  }

  /* Trigger the 'choose File' in attachment modal */
  triggerAdd() {
    document.getElementById('addAttach').click();
  }

  openDeleteWarning() {
    document.getElementById('confirmModalBtn').click();
  }

  setSelectedCheckList(checklistId, isSelected) {
    if (isSelected) {
      this.checkListSelectedCodes.push(checklistId);
    } else {
      const index = this.checkListSelectedCodes.findIndex(item => item === checklistId);
      this.checkListSelectedCodes.splice(index, 1);
    }
  }
  getScheduleDate(committeId) {
    this._spinner.show();
    this._irbCreateService.getCommitteeScheduledDates(committeId).subscribe((data: any) => {
      this._spinner.hide();
      this.scheduleDateList = data.scheduleDates != null ? data.scheduleDates : [];
      if (this.scheduleDateList.length > 0) {
        this.selectedScheduleId = this.scheduleDateList[0].SCHEDULE_ID;
      }
    });
  }

  addReviewComments(commentObject) {
    if (this.minuteFlag === false && this.letterFlag === false) {
      this.invalidData.invalidReviewComments = true;
    } else {
    this.invalidData.invalidReviewComments = false;
    commentObject.flag = this.minuteFlag === true ? 'Y' : 'N';
    commentObject.letterFlag = this.letterFlag === true ? 'Y' : 'N';
    this.commentList.push(commentObject);
    this.commentObject = {
      comments: '',
      flag: 'Y',
      letterFlag: 'Y',
      contingencyCode: ''
    };
    this.letterFlag = true;
    this.minuteFlag = true;
  }
}
  deleteReviewComments(index) {
    this.commentList.splice(index, 1);
  }
}
