import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { LoginService } from '../../login/login.service';
import { SharedDataService } from '../service/shared-data.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})

export class HeaderComponent implements OnInit {
    requestObject = {
        userName: sessionStorage.getItem('ActivatedUser')
    };

    result: any;
    showMenu = true;

    constructor(private _loginService: LoginService, private _router: Router, private _sharedDataService: SharedDataService) { }

    ngOnInit() {
        this._loginService.getUserDetail(this.requestObject).subscribe(data => {
            this.result = data || [];
            localStorage.setItem('userName', this.result.userName);
        },
            error => {
                console.log('Error in method getUserDetail()', error);
            },
        );
    }

    logout() {
        this._loginService.logout().subscribe(data => {
           // sessionStorage.removeItem('ActivatedUser');
            if (data === true) {
                this._sharedDataService.changeCurrentTab(null);
                this._sharedDataService.searchData = null;
                this._router.navigate(['/logout']);
            } else {
                this._router.navigate(['/irb/dashboard']);
            }
        });
    }
    expandMenu(e: any) {
        e.preventDefault();
        this.showMenu = false;
        const element = document.getElementById('myMenu');
        element.classList.remove('slideMenu');
        element.classList.add('hideMenu');
    }
    hideMenu(e: any) {
        e.preventDefault();
        const element = document.getElementById('myMenu');
        element.classList.remove('hideMenu');
        element.classList.add('slideMenu');
        this.showMenu = true;
    }

}
