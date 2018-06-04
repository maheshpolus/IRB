import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router} from '@angular/router';

import { LoginService } from '../../login/login.service';

@Component( {
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
} )

export class HeaderComponent implements OnInit, AfterViewInit {

    result: any;

    constructor( private _loginService: LoginService, private _router: Router ) { }

    ngOnInit() {
        this._loginService.getUserDetail().subscribe( data => {
            this.result = data || [];
        },
            error => {
                 console.log( "Error in method getUserDetail()", error );
            },
        );
    }

    ngAfterViewInit() {}

    logout() {
    this._loginService.logout().subscribe(data => {
                sessionStorage.removeItem('ActivatedUser');
                this._router.navigate( ['/login'] );
        });
    }
}
