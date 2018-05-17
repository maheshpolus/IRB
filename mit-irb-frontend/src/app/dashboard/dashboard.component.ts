import { Component, OnInit } from '@angular/core';
import { DashboardService } from './dashboard.service';
import { Router, ActivatedRoute} from '@angular/router';

@Component( {
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
} )
export class DashboardComponent implements OnInit {
    userDTO = {};
    constructor(private _activatedRoute: ActivatedRoute) { }

    ngOnInit() {
        this.userDTO = this._activatedRoute.snapshot.data['irb'];
    }
}
