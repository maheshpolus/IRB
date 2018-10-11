import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';

@Component( {
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
} )

export class DashboardComponent implements OnInit {
    userDTO: any = {};
    constructor(private _activatedRoute: ActivatedRoute) {}

    /** get userDTO from resolver service */
    ngOnInit() {
         this.userDTO = this._activatedRoute.snapshot.data['irb'];
    }
}
