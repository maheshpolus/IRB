import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { DashboardService } from '../dashboard/dashboard.service';

@Component({
  selector: 'app-committee-schedule-list',
  templateUrl: './committee-schedule-list.component.html',
  styleUrls: ['./committee-schedule-list.component.css']
})
export class CommitteeScheduleListComponent implements OnInit {
  irbListData: any = [];
  paginatedIrbListData: any = [];
  paginationData = {
    limit       : 10,
    page_number : 1,
  };
  isCommitteeList = false;
  count: number;
  committeeOrScheduleLabel: string;
  constructor(private _activatedRoute: ActivatedRoute,  private _spinner: NgxSpinnerService, private _router: Router,
    private _dashboardService: DashboardService) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.isCommitteeList = params['isCommitteeList'] === 'true' ? true : false;
      if (this.isCommitteeList) {
        this.committeeOrScheduleLabel = 'Committees';
        this.getCommitteeList();
      } else {
        this.committeeOrScheduleLabel = 'Schedules';
        this.getScheduleList();
      }
  });
  }

getCommitteeList() {
  this._spinner.show();
  this._dashboardService.loadCommitteeList().subscribe((data: any) => {
    this._spinner.hide();
    this.irbListData = data.committeList != null ? data.committeList : [];
    this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
    this.count = this.irbListData.length;
  });
}

getScheduleList() {
  this._spinner.show();
  this._dashboardService.loadCommitteeScheduleList().subscribe((data: any) => {
    this._spinner.hide();
    this.irbListData = data.committeSchedulList != null ? data.committeSchedulList : [];
    this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
    this.count = this.irbListData.length;
  });
}

  paginatedListPerPage(pageNumber) {
    this.paginatedIrbListData = this.irbListData.slice(pageNumber * this.paginationData.limit - this.paginationData.limit,
        pageNumber * this.paginationData.limit);
    // document.getElementById('scrollToTop').scrollTop = 0;
    const id = document.getElementById('scrollToTop');
            if (id) {
                id.scrollIntoView({behavior : 'smooth'});
            }
}

goToCommittee() {
  this._router.navigate(['/irb/committee'], {queryParams: {mode: 'create'}});
}

}
