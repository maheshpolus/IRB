import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CompleterService, CompleterData } from 'ng2-completer';

import {AssignModalServiceService} from './assign-modal.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-assign-modal',
  templateUrl: './assign-modal.component.html',
  styleUrls: ['./assign-modal.component.css']
})
export class AssignModalComponent implements OnInit {

  @Input() userDTO: any;
  @Input() protocoldetails: any;
  @Input() mode: any;
  @Output() passEntry: EventEmitter<any> = new EventEmitter();
  adminList = [];
  isAdmintSearch = false;
  adminListDataSource: CompleterData;
  requestObj: any =  {};
  constructor(public activeModal: NgbActiveModal, private assignModalServiceService: AssignModalServiceService,
    private _completerService: CompleterService, private _spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.getIRBAdminList();
  }

  getIRBAdminList() {
    this._spinner.show();
    this.assignModalServiceService.getIRBAdminList(null).subscribe(
      (data: any) => {
        this._spinner.hide();
        const result = data;
        this.adminList = result.irbAdminsList;
        this.adminListDataSource = this._completerService.local(this.adminList, 'FULL_NAME,PERSON_ID', 'FULL_NAME');
      });
  }
  setAdmin(event) {
    this.requestObj.acType = this.mode;
    this.requestObj.personID  = event.originalObject.PERSON_ID;
    this.requestObj.personName  = event.originalObject.FULL_NAME;
    this.requestObj.protocolNumber  = this.protocoldetails.PROTOCOL_NUMBER;
    this.requestObj.protocolId  = this.protocoldetails.PROTOCOL_ID;
    this.requestObj.submissionId  = this.protocoldetails.SUBMISSION_ID;
    this.requestObj.submissionNumber  = this.protocoldetails.SUBMISSION_NUMBER;
    this.requestObj.sequenceNumber  = this.protocoldetails.SEQUENCE_NUMBER;
    this.requestObj.updateUser  = this.userDTO.userName;
  }
  assignMeAsAdmin() {
    this.requestObj.acType = this.mode;
    this.requestObj.personID  = this.userDTO.personID;
    this.requestObj.personName  = this.userDTO.fullName;
    this.requestObj.protocolNumber  = this.protocoldetails.PROTOCOL_NUMBER;
    this.requestObj.protocolId  = this.protocoldetails.PROTOCOL_ID;
    this.requestObj.submissionId  = this.protocoldetails.SUBMISSION_ID;
    this.requestObj.submissionNumber  = this.protocoldetails.SUBMISSION_NUMBER;
    this.requestObj.sequenceNumber  = this.protocoldetails.SEQUENCE_NUMBER;
    this.requestObj.updateUser  = this.userDTO.userName;
    this.updateIrbAdmin();
  }

  updateIrbAdmin() {
    if (this.requestObj.personID != null) {
      this._spinner.show();
      this.assignModalServiceService.updateIRBAdmin(this.requestObj).subscribe(
        (data: any) => {
          this._spinner.hide();
          const result = data;
          this.passEntry.emit(result.irbViewHeader);
          this.activeModal.dismiss();
        });
    }
  }
}
