import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';

import { DashboardService } from './dashboard.service';

@Component( {
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
} )

export class DashboardComponent implements OnInit {
    userDTO: any = {};
    constructor(private _activatedRoute: ActivatedRoute) {}

    ngOnInit() {
         this.userDTO = this._activatedRoute.snapshot.data['irb'];
    }
}
