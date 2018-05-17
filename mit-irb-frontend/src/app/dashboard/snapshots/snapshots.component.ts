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
            personId: '900002368',
            person_role_type : 'pi',

    };
    roleType = 'PI';

  constructor(private _dashboardService: DashboardService, private router: Router,
              private _activatedRoute: ActivatedRoute ) { }

  ngOnInit() {
      this.getSnapshotData();
  }

  getSnapshotData() {
   // this.requestObject.personId = this.userDTO.personId;
      
      this._dashboardService.getSnapshots( this.requestObject ).subscribe(
              data => {
                  this.result = data || [];
                  if ( this.result != null ) {
                      this.snapshots = this.result.dashBoardDetailMap;
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
