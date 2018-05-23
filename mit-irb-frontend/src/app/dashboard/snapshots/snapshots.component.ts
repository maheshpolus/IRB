import { Component, OnInit , Input} from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';
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
            person_role_type : ''
    };
    roleType: string;

  constructor(private _dashboardService: DashboardService, private router: Router,
              private _activatedRoute: ActivatedRoute ) { }

  ngOnInit() {
      this.getSnapshotData();
      this.roleType = this.userDTO.role;
  }

  getSnapshotData() {
    this.requestObject.personId = this.userDTO.personID;
    this.requestObject.person_role_type = this.userDTO.role;
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
  expandedView() {
      this.router.navigate(['/irb/expanded-view']);
  }
}
