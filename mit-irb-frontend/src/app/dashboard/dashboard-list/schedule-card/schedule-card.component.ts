import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { SharedDataService } from '../../../common/service/shared-data.service';
import { PermissionWarningModalComponent } from '../../../common/permission-warning-modal/permission-warning-modal.component';

@Component({
  selector: 'app-schedule-card',
  templateUrl: './schedule-card.component.html',
  styleUrls: ['./schedule-card.component.css']
})
export class ScheduleCardComponent implements OnInit {

  @Input() irbList: any = [];
  @Input() userDTO: any;
  currentDate = new Date();
  constructor(private _router: Router, private modalService: NgbModal, private _sharedDataService: SharedDataService) { }

  ngOnInit() { }


  openPermissionWarningModal(alertMessage) {
    const modalRef = this.modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
    modalRef.componentInstance.alertMessage = alertMessage;
  }

  openIrb(scheduleId) {
      const requestObject = {
          acType: 'C', department: this.userDTO.unitNumber, personId: this.userDTO.personID, protocolId: null
      };
      this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
      const hasPermission = data.successCode;
      if (hasPermission) {
        this._router.navigate(['/irb/committee/schedule'], { queryParams: { 'scheduleId': scheduleId } });
      } else {
      const alertMessage = 'You donot have permission to Maintain Committee';
      this.openPermissionWarningModal(alertMessage);
      }
      });
      }
}
