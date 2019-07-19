import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { CommitteeSaveService } from '../../committee-save.service';
import { NgxSpinnerService } from 'ngx-spinner';


@Component({
  selector: 'app-committee-members',
  templateUrl: './committee-members.component.html',
  styleUrls: ['./committee-members.component.css']
})
export class CommitteeMembersComponent implements OnInit {

  committeId = null;
  committeMemberList: any = [];
  committeeMember: any = {};
  userDTO: any = {};
  filteredCommitteMemberList: any = [];
  isActiveFilter = true;
  isInactiveFilter = false;

  constructor(private _committeeSaveService: CommitteeSaveService, private _router: Router, private _spinner: NgxSpinnerService,
    private _activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.userDTO = JSON.parse(localStorage.getItem('currentUser'));
    this._activatedRoute.queryParams.subscribe(params => {
      this.committeId = params['id'];
    });
    this.getCommitteeMembers();
  }
  getCommitteeMembers() {
    this._spinner.show();
    const requestObject = { committeeId: this.committeId };
    this._committeeSaveService.getCommitteeMembersById(requestObject).subscribe((data: any) => {
      this._spinner.hide();
      this.committeMemberList = (data.committee != null && data.committee.committeeMemberships != null) ?
        data.committee.committeeMemberships : [];
      this.filterMemberList();
    });
  }

  filterMemberList() {
    if (this.isActiveFilter === true && this.isInactiveFilter === false) {
      this.filteredCommitteMemberList = this.committeMemberList.filter(member => member.active === true);

    } else if (this.isActiveFilter === false && this.isInactiveFilter === true) {
      this.filteredCommitteMemberList = this.committeMemberList.filter(member => member.active === false);
    } else {
      this.filteredCommitteMemberList = Object.assign([], this.committeMemberList);
    }
  }

  editMember(member) {
    this._router.navigate(['irb/committee/committeeMembers/memberHome'],
      {
        queryParams: {
          'mode': this._activatedRoute.snapshot.queryParamMap.get('mode'), 'id': this.committeId,
          'membershipId': member.commMembershipId, 'personId': member.personId, 'rolodexId': member.rolodexId
        }
      });
  }

  deleteCommitteeMember(member) {
    member.acType = 'D';
    this.committeeMember = member;
    member.committeeId = this.committeId;
    member.updateTimestamp = new Date().getTime();
    member.updateUser = this.userDTO.userName;
  }

  updateCommitteMember() {
    this._spinner.show();
    this._committeeSaveService.updateCommitteMembers(this.committeeMember).subscribe((data: any) => {
      this._spinner.hide();
      this.committeMemberList = (data.committee != null && data.committee.committeeMemberships != null) ?
        data.committee.committeeMemberships : [];
      this.filterMemberList();
    });
  }
}
