import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SharedDataService } from '../../../common/service/shared-data.service';
import { PermissionWarningModalComponent } from '../../../common/permission-warning-modal/permission-warning-modal.component';

@Component({
  selector: 'app-committee-card',
  templateUrl: './committee-card.component.html',
  styleUrls: ['./committee-card.component.css']
})
export class CommitteeCardComponent implements OnInit {

  @Input() irbList: any = [];
  @Input() userDTO: any;
  constructor(private _router: Router, private modalService: NgbModal, private _sharedDataService: SharedDataService) { }


  ngOnInit() {
  }
  /**
   * @param  {} committeId
   * Opens the committe
   */
  openIrb(committeId) {
    const requestObject = {
      acType: 'C', department: this.userDTO.unitNumber, personId: this.userDTO.personID, protocolId: null
  };
  this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
  const hasPermission = data.successCode;
  if (hasPermission) {
    this._router.navigate(['/irb/committee'], { queryParams: { mode: 'view', id:  committeId} });
} else {
  const alertMessage = 'You donot have permission to Maintain Committee';
  this.openPermissionWarningModal(alertMessage);
}
});
  }

  openPermissionWarningModal(alertMessage) {
    const modalRef = this.modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
    modalRef.componentInstance.alertMessage = alertMessage;
  }
}
