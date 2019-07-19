import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { AdminDashboardService } from './admin-dashboard.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  constructor( private _activatedRoute: ActivatedRoute, private _spinner: NgxSpinnerService,
     private _adminDashboardService: AdminDashboardService ) { }
  hasPermissions: any = {};
  userDTO: any = {};

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    localStorage.setItem('currentUser', JSON.stringify(this.userDTO));
    this.getUserPermissions();
  }

  getUserPermissions() {
    this._spinner.show();
    const requestObject = {personId: this.userDTO.personID};
    this._adminDashboardService.getAdminDashBoardPermissions(requestObject).subscribe( (data: any) => {
    this._spinner.hide();
      this.hasPermissions = data.adminDashBoardPermissions;
    });

  }
}
