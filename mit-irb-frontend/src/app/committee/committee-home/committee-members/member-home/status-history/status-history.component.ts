import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommitteeSaveService } from '../../../../committee-save.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-status-history',
  templateUrl: './status-history.component.html',
  styleUrls: ['./status-history.component.css']
})
export class StatusHistoryComponent implements OnInit {
  memberId = null;
  personId = null;
  rolodexId = null;
  termHistoryList: any = [];
  selectedPerson: any = {};
  isNonEmployee = false;

  constructor(private _activatedRoute: ActivatedRoute, private _spinner: NgxSpinnerService,
    private _committeeSaveService: CommitteeSaveService) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.memberId = params['membershipId'];
      this.personId = params['personId'];
      this.rolodexId = params['rolodexId'];
      this.getMemberTermHistory();
    });
  }

  getMemberTermHistory() {
    const requestObject = { commMembershipId: this.memberId, personId: this.personId, rolodexId: this.rolodexId };
    this._spinner.show();
    this._committeeSaveService.getMemberTermHistory(requestObject).subscribe((data: any) => {
      this._spinner.hide();
      this.termHistoryList = data.committeeMemberStatusChange != null ? data.committeeMemberStatusChange : [];
      this.isNonEmployee = data.personId == null ? true : false;
      this.selectedPerson = this.isNonEmployee ? data.nonEmployee : data.employee;
    });

  }

}
