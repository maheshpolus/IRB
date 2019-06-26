import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { AssignModalComponent } from '../../../common/assign-modal/assign-modal.component';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { PermissionWarningModalComponent } from '../../../common/permission-warning-modal/permission-warning-modal.component';
@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent  {

  @Input() irbList: any = [];
  @Input() userDTO: any;
  @Input() currentTab: string;

  constructor(private _router: Router,  private modalService: NgbModal, private _sharedDataService: SharedDataService) {
  }

  /**Opens Irb Component
   * @protocolNumber - unique id for protocol
  */
  openIrb( protocolNumber ) {
      this._router.navigate( ['/irb/irb-view/irbOverview'], {queryParams: {protocolNumber: protocolNumber}});
  }
  /**
   * @param  {} protocolNumber
   * @param  {} protocolId
   * open the IRB protocol in edit mode
   */
  EditIrb (protocolNumber, protocolId) {
    const requestObject = {
      acType: 'E', department: this.userDTO.unitNumber, personId: this.userDTO.personID, protocolId: protocolId
  };
  this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
  const hasPermission = data.successCode;
  if (hasPermission) {
     this._router.navigate( ['/irb/irb-create'], {queryParams: {protocolNumber: protocolNumber, protocolId: protocolId}});
} else {
  const alertMessage = 'You do not have permission to edit this protocol';
  this.openPermissionWarningModal(alertMessage);
}
});
  }

  openAssignPopUp(mode, protocolDetails, index) {
    const modalRef = this.modalService.open(AssignModalComponent, {
      size: 'lg',
      windowClass: 'assignAdminModal', backdrop: 'static',
    });
    modalRef.componentInstance.protocoldetails =  {
      PROTOCOL_NUMBER: protocolDetails.PROTOCOL_NUMBER,
      PROTOCOL_ID: protocolDetails.PROTOCOL_ID,
      SUBMISSION_ID: protocolDetails.SUBMISSION_ID,
      SUBMISSION_NUMBER: protocolDetails.SUBMISSION_NUMBER,
      SEQUENCE_NUMBER: protocolDetails.SEQUENCE_NUMBER
    };
    modalRef.componentInstance.mode = mode;
    modalRef.componentInstance.userDTO = this.userDTO;
    modalRef.componentInstance.passEntry.subscribe((receivedEntry: any) => {
      // this.irbHeaderDetails = receivedEntry;
      this.irbList[index].ASSIGNEE_PERSON = receivedEntry.ASSIGNEE_PERSON;
      this.irbList[index].ASSIGNEE_PERSON_ID = receivedEntry.ASSIGNEE_PERSON_ID;
      this.irbList[index].SUBMISSION_STATUS = receivedEntry.SUBMISSION_STATUS;
      this.irbList[index].SUBMISSION_STATUS_CODE = receivedEntry.SUBMISSION_STATUS_CODE;
      });
  }

 openPermissionWarningModal(alertMessage) {
     const modalRef = this.modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
     modalRef.componentInstance.alertMessage = alertMessage;
   }
}

