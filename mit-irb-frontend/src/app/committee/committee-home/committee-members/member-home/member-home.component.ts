import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-member-home',
  templateUrl: './member-home.component.html',
  styleUrls: ['./member-home.component.css']
})
export class MemberHomeComponent implements OnInit {

  tabSelected = 'MEMBER_DETAILS';
  committeeId = null;
  memberId = null;
  isNewMember = true;

  constructor( private _activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.memberId = params['membershipId'];
      this.committeeId = params['id'];
      if (this.memberId != null) {
        this.isNewMember = false;
      }
  });
}

}
