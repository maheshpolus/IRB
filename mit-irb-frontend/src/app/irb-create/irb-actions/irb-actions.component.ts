import { Component, OnInit, OnDestroy, ViewContainerRef, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
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
  generalInfo = {};
  userDTO: any = {};
  IRBActionsVO: any = {};
  commonVo: any = {};
  currentActionPerformed: any = {};
  moduleAvailableForAmendment: any = [];
  personnelInfoList = [];
  leadUnitList = [];
  personActionsList = [];
  submissionTypes = [];
  typeQualifier = [];
  reviewTypes = [];
  committees = [];
  scheduleDates = [];
  createOrViewPath: string;

  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  fil: FileList;
  attachmentList: any[] = [];

  private $subscription1: ISubscription;
  private $subscription2: ISubscription;

  invalidData = {
    noPiExists: true, noLeadUnit: true
  };


  constructor(private _activatedRoute: ActivatedRoute, private _router: Router, private _irbCreateService: IrbCreateService,
    private _sharedDataService: SharedDataService, public toastr: ToastsManager, vcr: ViewContainerRef,
    private _spinner: NgxSpinnerService, public changeRef: ChangeDetectorRef) {
      this.toastr.setRootViewContainerRef(vcr);  }

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
    this.IRBActionsVO.protocolNumber = this.protocolNumber;
    this.IRBActionsVO.protocolId = this.protocolId;
    this.IRBActionsVO.personID = this.userDTO.personID;
    this._spinner.show();
    this._irbCreateService.getAvailableActions(this.IRBActionsVO).subscribe(data => {
      this._spinner.hide();
      this.IRBActionsVO = data;
      this.personActionsList = this.IRBActionsVO.personActionsList;
      this.moduleAvailableForAmendment = this.IRBActionsVO.moduleAvailableForAmendment;
      if (this.personActionsList != null && this.personActionsList.length > 0) {
        this._spinner.show();
        setTimeout(() => { this.setActionIcons(); }, 1000);
      }
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
    if (action.ACTION_CODE === '101') {
      document.getElementById('submitModalBtn').click();
    } else if ( action.ACTION_CODE === '995' ||  action.ACTION_CODE === '303' ||
     action.ACTION_CODE === '120' || action.ACTION_CODE === '121') {
      document.getElementById('commentModalBtn').click();
    } else if (action.ACTION_CODE === '103' || action.ACTION_CODE === '105' || action.ACTION_CODE === '116' ||
     action.ACTION_CODE === '108' || action.ACTION_CODE === '115') {
      document.getElementById('commentAttachmentModalBtn').click();
    } else if (action.ACTION_CODE === '102' || action.ACTION_CODE === '114' || action.ACTION_CODE === '910') { // modify-910
      document.getElementById('commentCheckboxModalBtn').click();
    } else if (action.ACTION_CODE === '911') { // copy protocol
      document.getElementById('confirmModalBtn').click();
    }

  }

  // validateProtocol() {
  //   if (this.personnelInfoList.length > 0) {
  //     this.personnelInfoList.forEach(personnelInfo => {
  //       if (personnelInfo.protocolPersonRoleId === 'PI') {
  //         this.invalidData.noPiExists = false;
  //       }
  //     });
  //   } else {
  //     this.invalidData.noPiExists = true;
  //   }
  //   if (this.leadUnitList.length > 0) {
  //     this.leadUnitList.forEach(leadUnit => {
  //       if (leadUnit.unitTypeCode === '1') {
  //         this.invalidData.noLeadUnit = false;
  //       }
  //     });
  //   } else {
  //     this.invalidData.noLeadUnit = true;
  //   }
  //   if (this.invalidData.noLeadUnit === true || this.invalidData.noPiExists === true) {
  //     document.getElementById('validationModalBtn').click();
  //   }
  // }
  // openProtocolWithValidations() {
  //   this._router.navigate(['/irb/irb-create/irbHome'],
  //     { queryParams: { protocolNumber: this.protocolNumber, protocolId: this.protocolId, validated: 'true' } });
  // }

  performAction() {
   // if (this.currentActionPerformed.ACTION_CODE === '101') {
      // this.validateProtocol();
      //   if (this.invalidData.noLeadUnit === false || this.invalidData.noPiExists === false) {
      this.setActionVO();
      // }
   // }
    this._irbCreateService.performProtocolActions(this.IRBActionsVO, this.uploadedFile).subscribe(data => {
      console.log(data);
      this._router.navigate(['/irb/irb-view/irbActions'],
       { queryParams: { protocolNumber: this.protocolNumber, protocolId: this.protocolId} });
     // this.toastr.success('Action Performed successfully', null, { toastLife: 2000 });
    },
    error => {
      this.toastr.error('Failed to perform Action', null, { toastLife: 2000 });
      console.log('Error in perform action ', error);
    }
  );
  }

  setActionVO() {
    this.IRBActionsVO.acType = 'I';
    this.IRBActionsVO.actionTypeCode = this.currentActionPerformed.ACTION_CODE;
    this.IRBActionsVO.createUser = this.userDTO.userName;
    this.IRBActionsVO.updateUser = this.userDTO.userName;
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

}
