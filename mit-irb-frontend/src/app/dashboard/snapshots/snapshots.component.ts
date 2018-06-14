import { Component, OnInit , Input} from '@angular/core';
import { Router } from '@angular/router';

import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'app-snapshots',
  templateUrl: './snapshots.component.html',
  styleUrls: ['./snapshots.component.css']
})

export class SnapshotsComponent implements OnInit {

    @Input() userDTO: any;
    result: any;
    snapshots: any = [];
    requestObject = {
            personId : '',
            personRoleType : ''
    };
    roleType: string;

  constructor(private _dashboardService: DashboardService, private _router: Router ) { }

  ngOnInit() {
      this.getSnapshotData();
      this.roleType = this.userDTO.role;
  }

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

  expandedView(summaryType) {
      this._router.navigate(['/irb/expanded-view'],
      {queryParams: {personId: this.userDTO.personID, personRole: this.roleType, summaryType: summaryType}});
  }

  goToQuestionaire() {debugger;
      this._router.navigate(['/irb/exempt-questionaire']);
  }
}
