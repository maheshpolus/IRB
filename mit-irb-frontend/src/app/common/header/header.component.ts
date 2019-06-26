import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { LoginService } from '../../login/login.service';
import { SharedDataService } from '../service/shared-data.service';
import { PermissionWarningModalComponent} from '../../common/permission-warning-modal/permission-warning-modal.component';

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
    alertMessage: string;
    FAQ_URL: string;
    GUIDELINES_URL: string;

    constructor(private _loginService: LoginService, private _router: Router,
        private _sharedDataService: SharedDataService, private _http: HttpClient,  private _modalService: NgbModal) { }

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

        // this._loginService.logout().subscribe(data => {
        //     this._sharedDataService.changeCurrentTab(null);
        //     this._sharedDataService.searchData = null;
        //     this._sharedDataService.isAdvancesearch = false;
        //     if (data === true) {
        //         sessionStorage.removeItem('ActivatedUser');
        //         this._router.navigate(['/login']);
        //     } else {
        //         this._router.navigate(['/logout']);
        //     }
        // });
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
    // goToQuestionnaire() {
    //     const hasPermission = this.checkUserPermission('Q');
    //     if (hasPermission !== undefined) {
    //     if (hasPermission) {
    //     this._router.navigate(['/irb/configure-questionnaire']);
    //     } else {
    //         this.alertMessage = 'You donot have Permission to Maintain Questionnaire';
    //         // document.getElementById('noPermissionModalBtn').click();
    //         this.openPermissionWarningModal(this.alertMessage);
    //     }
    // }
    // }
    // goToCodeTable() {
    //    // const hasPermission = this.checkUserPermission('CT');
    //     if (this.checkUserPermission('CT')) {
    //     this._router.navigate(['/irb/code-table']);
    //     } else {
    //         this.alertMessage = 'You donot have Permission to Maintain Code Tables';
    //        // document.getElementById('noPermissionModalBtn').click();
    //        this.openPermissionWarningModal(this.alertMessage);
    //     }
    // }
    // goToTrainingMaintainence() {
    //    // const hasPermission = this.checkUserPermission('T');
    //     if (this.checkUserPermission('T')) {
    //     this._router.navigate(['/irb/training-maintenance']);
    //     } else {
    //         this.alertMessage = 'You donot have Permission to Maintain Person training';
    //         // document.getElementById('noPermissionModalBtn').click();
    //         this.openPermissionWarningModal(this.alertMessage);
    //     }
    // }
    // checkUserPermission(Type) {debugger;
    //    let hasPermission;
    //     const requestObject = {
    //         acType: Type, department: this.result.unitNumber, personId: this.result.personID
    //     };
    //     this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
    //     hasPermission = data.successCode;
    //     return hasPermission;
    //     });
    // }

    checkPermissionToGotoLink(acType) {
        const requestObject = {
                    acType: acType, department: this.result.unitNumber, personId: this.result.personID, protocolId: null
                };
                this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
                const  hasPermission = data.successCode;
                if (hasPermission === true) {
                    if (acType === 'Q') {
                        this._router.navigate(['/irb/configure-questionnaire']);
                    } else if (acType === 'CT') {
                        this._router.navigate(['/irb/code-table']);
                    } else if ( acType === 'T') {
                        this._router.navigate(['/irb/training-maintenance']);

                    } else if ( acType === 'R') {
                        this._router.navigate(['/irb/role-maintainance']);

                    }
                } else {
                    if (acType === 'Q') {
                        this.alertMessage = 'You do not have Permission to Maintain Questionnaire';
                    } else if (acType === 'CT') {
                        this.alertMessage = 'You do not have Permission to Maintain Code Tables';
                    } else if ( acType === 'T') {
                        this.alertMessage = 'You do not have Permission to Maintain Person training';
                    }  else if ( acType === 'R') {
                        this.alertMessage = 'You do not have Permission to Maintain Roles';
                    }
                    this.openPermissionWarningModal(this.alertMessage);
                }
                });
    }

    openPermissionWarningModal(alertMessage) {
        const modalRef = this._modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
        modalRef.componentInstance.alertMessage = alertMessage;
      }
}
