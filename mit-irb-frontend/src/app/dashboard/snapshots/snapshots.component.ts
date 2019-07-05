import { Component, OnInit , Input, Output, EventEmitter} from '@angular/core';
import { Router } from '@angular/router';

import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-snapshots',
  templateUrl: './snapshots.component.html',
  styleUrls: ['./snapshots.component.css']
})

export class SnapshotsComponent implements OnInit {

    @Input() userDTO: any;
    @Output() currentTab = new EventEmitter<Event>();
    @Output() isUnAssignedClicked = new EventEmitter<Event>();
    result: any = {};
    snapshots: any = [];
    requestObject = {
            personId : '',
            personRoleType : ''
    };
    roleType: string;

  constructor(private _dashboardService: DashboardService, private _router: Router) { }

  /** calls function to load snapshot details */
  ngOnInit() {
      this.getSnapshotData();
      this.roleType = this.userDTO.role;
  }

  /** call service to load snapshot data in dashboard */
  getSnapshotData() {
    this.requestObject.personId = this.userDTO.personID;
    this.requestObject.personRoleType = this.userDTO.role;
    this._dashboardService.getSnapshots( this.requestObject ).subscribe(
            data => {
                this.result = data || [];
                if ( this.result != null ) {
                    this.snapshots = this.result.snapshotData;
                }
            },
            error => {
                console.log( 'Error in getSnapshotData', error );
            },
    );
  }

  /** open expanded view of a snapshot when a snapshot is clicked
   * @param summaryType - type of snapshot
  */
  expandedView(summaryType) {
      this._router.navigate(['/irb/expanded-view'],
      {queryParams: {personId: this.userDTO.personID, personRole: this.roleType, summaryType: summaryType}});
  }

  /** route to exempt questionaire component when the link is clicked */
  goToQuestionaire() {
      this._router.navigate(['/irb/exempt-questionaire']);
  }
   /** route to Create Protocol module when the link is clicked */
   goToCreateProtocol() {
    this._router.navigate(['/irb/irb-create']);
}

// openPermissionWarningModal(alertMessage) {
//   const modalRef = this.modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
//   modalRef.componentInstance.alertMessage = alertMessage;
// }
// goToCommittee() {
//     const requestObject = {
//         acType: 'C', department: this.userDTO.unitNumber, personId: this.userDTO.personID, protocolId: null
//     };
//     this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
//     const hasPermission = data.successCode;
//     if (hasPermission) {
//         this._router.navigate(['/irb/committee'], {queryParams: {mode: 'create'}});
//     } else {
//     const alertMessage = 'You donot have permission to Maintain Committee';
//     this.openPermissionWarningModal(alertMessage);
//     }
//     });
 // }
    openNewSubmission(tab, isUnAssignedClicked, event) {
        event.stopPropagation();
        const obj: any = {tab: tab, isUnAssignedClicked: isUnAssignedClicked};
        this.currentTab.emit(obj);
         //   this.isUnAssignedClicked.emit(isUnAssignedClicked);
    }
}

