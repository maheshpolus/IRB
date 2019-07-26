import { HttpClient } from '@angular/common/http';
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
    isScheduleClicked = false;
    alertMessage: string;
    FAQ_URL: string;
    GUIDELINES_URL: string;
    lockList = [];

    constructor(private _loginService: LoginService, private _router: Router,
        private _sharedDataService: SharedDataService, private _http: HttpClient) { }

    ngOnInit() {
        this._http.get('/mit-irb/resources/string_config_json').subscribe(
            data => {
                const property_config: any = data;
                if (property_config) {
                    this.FAQ_URL = property_config.FAQ_URL;
                    this.GUIDELINES_URL = property_config.GUIDELINES_URL;
                }
            });
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

        this._sharedDataService.changeCurrentTab(null);
        this._sharedDataService.searchData = null;
        this._sharedDataService.isAdvancesearch = false;
        this._router.navigate(['/logout']);

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


    loadProtocolLock() {
        const reqstObj = {personId: this.result.personID};
        this._sharedDataService.loadProtocolLock(reqstObj).subscribe(data => {
            const result: any = data;
            this.lockList = result.lockList != null ? result.lockList : [];
        });
    }

    RemoveLock(protocolNumber) {
        const reqstObj = { protocolNumber: protocolNumber, personId: this.result.personID };
        this._sharedDataService.releaseProtocolLock(reqstObj).subscribe(
            data => {
                const result: any = data;
                this.lockList = result.lockList != null ? result.lockList : [];
                console.log('Lock Released Successfully');
            });
    }
}
