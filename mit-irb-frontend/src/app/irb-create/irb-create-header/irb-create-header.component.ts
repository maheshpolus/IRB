import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedDataService } from '../../common/service/shared-data.service';
import { IrbCreateService } from '../irb-create.service';
import { AssignModalComponent } from '../../common/assign-modal/assign-modal.component';

@Component({
  selector: 'app-irb-create-header',
  templateUrl: './irb-create-header.component.html',
  styleUrls: ['./irb-create-header.component.css'],
  providers: [IrbCreateService, SharedDataService]
})
export class IrbCreateHeaderComponent implements OnInit, OnDestroy {
  protocolNumber = null;
  piName = null;
  protocolId = null;
  commonVo: any = {};
  generalInfo: any = {};
  userDTO: any = {};
  isExpanded: boolean;
  isCreateNewProtocol = false;
  private $subscription1: ISubscription;

  constructor(private _sharedDataService: SharedDataService,
    private _router: Router,
    private _activatedRoute: ActivatedRoute,
    private _irbCreateService: IrbCreateService,
    private _spinner: NgxSpinnerService,
    private modalService: NgbModal) {
      this._router.events.subscribe((evt: any) => {
        if (evt instanceof NavigationEnd) {
          if (evt.url === '/irb/irb-create/irbHome?protocolNumber=' + this.protocolNumber + '&protocolId=' + this.protocolId) {
            this.getIRBProtocol();
          }
        }
        if (evt.snapshot != null && evt.snapshot.params != null  && evt.snapshot.params.queryParams != null) {
          this.protocolId = evt.snapshot.params.queryParams.protocolId;
          this.protocolNumber = evt.snapshot.params.queryParams.protocolNumber;
        }
    });
     }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this.$subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
      if (generalInfo !== undefined) {
        this.generalInfo = generalInfo;
        if (this.generalInfo.personnelInfos != null && this.generalInfo.personnelInfos.length > 0) {
          this.generalInfo.personnelInfos.forEach(element => {
            if (element.protocolPersonRoleId === 'PI') {
              this.piName = element.personName;
            }
          });
        } else {
          this.piName = null;
        }
      }
    });
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId == null || this.protocolId === undefined) {
        this.isCreateNewProtocol = true;
      } else {
        this.isCreateNewProtocol = false;
      }
    });
    this.getIRBProtocol();
    if (this.isAmmendmentOrRenewal()) {
      this.show_current_tab('irbSummaryDetails');
    }
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  getIRBProtocol() {
    const requestObject = { protocolId: this.protocolId, protocolNumber: this.protocolNumber };
    this._spinner.show();
    this._irbCreateService.getEditDetails(requestObject).subscribe(
      data => {
        this._spinner.hide();
        this.commonVo = data;
        this.generalInfo = this.commonVo.generalInfo;
        this._sharedDataService.setCommonVo(Object.assign({}, this.commonVo));
        if (this.commonVo.generalInfo.personnelInfos != null && this.commonVo.generalInfo.personnelInfos.length > 0) {
          this.commonVo.generalInfo.personnelInfos.forEach(element => {
            if (element.protocolPersonRoleId === 'PI') {
              this.piName = element.personName;
            }
          });
        } else {
          this.piName = null;
        }
      });
  }

  /**sets current tab value to identify which tabs has been clicked
    * @param current_tab - value of currently selected tab
    */
  show_current_tab(current_tab) {
    if (this.protocolNumber !== null && this.protocolId !== null) {
      this._router.navigate(['/irb/irb-create/' + current_tab],
        { queryParamsHandling: 'merge', queryParams: { protocolNumber: this.protocolNumber, protocolId: this.protocolId } });
    }
  }
  toggle() {
    this.isExpanded = !this.isExpanded;
  }

  backClick(event) {
    event.preventDefault();
      this._router.navigate(['/irb/dashboard']);
    }

    isAmmendmentOrRenewal() {
      if (this.protocolNumber !== null && this.protocolNumber !== undefined ) {
      const isammendmentOrRenewal = this.protocolNumber.includes('A') || this.protocolNumber.includes('R') ? true : false;
      return isammendmentOrRenewal;
      } else {
        return false;
      }
    }

    openAssignPopUp(mode) {
      const modalRef = this.modalService.open(AssignModalComponent, {
        size: 'lg',
        windowClass: 'assignAdminModal', backdrop: 'static',
      });
      const protocoldetails: any = {};
      if (this.generalInfo != null && this.generalInfo.protocolSubmissionStatuses != null) {
        protocoldetails.ASSIGNEE_PERSON_ID = this.generalInfo.protocolSubmissionStatuses.assigneePersonId;
        protocoldetails.PROTOCOL_NUMBER = this.generalInfo.protocolNumber;
        protocoldetails.PROTOCOL_ID = this.generalInfo.protocolId;
        protocoldetails.SUBMISSION_ID = this.generalInfo.protocolSubmissionStatuses.submission_Id;
        protocoldetails.SUBMISSION_NUMBER = this.generalInfo.protocolSubmissionStatuses.submissionNumber;
        protocoldetails.SEQUENCE_NUMBER = this.generalInfo.protocolSubmissionStatuses.sequenceNumber;
         modalRef.componentInstance.protocoldetails = protocoldetails;
         modalRef.componentInstance.mode = mode;
         modalRef.componentInstance.userDTO = this.userDTO;
        modalRef.componentInstance.passEntry.subscribe((receivedEntry) => {
          this.generalInfo.protocolSubmissionStatuses.protocolSubmissionStatus.description = receivedEntry.SUBMISSION_STATUS;
          this.generalInfo.protocolSubmissionStatuses.protocolSubmissionStatus.protocolSubmissionStatusCode =
                                                                                  receivedEntry.SUBMISSION_STATUS_CODE;
          this.generalInfo.protocolSubmissionStatuses.submission_Id = receivedEntry.SUBMISSION_ID;
          this.generalInfo.protocolSubmissionStatuses.sequenceNumber =  receivedEntry.SEQUENCE_NUMBER;
          this.generalInfo.protocolSubmissionStatuses.assigneePersonName = receivedEntry.ASSIGNEE_PERSON;
          this.generalInfo.protocolSubmissionStatuses.assigneePersonId = receivedEntry.ASSIGNEE_PERSON_ID;
          this.generalInfo.protocolSubmissionStatuses.submissionNumber = receivedEntry.SUBMISSION_NUMBER;
          this.commonVo.generalInfo = this.generalInfo;
          this._sharedDataService.setCommonVo(Object.assign({}, this.commonVo));
          });
      }
      }
    }
