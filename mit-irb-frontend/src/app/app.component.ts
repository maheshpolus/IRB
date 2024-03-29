import { Component, ViewContainerRef } from '@angular/core';
import {Idle, DEFAULT_INTERRUPTSOURCES} from '@ng-idle/core';
import { Keepalive } from '@ng-idle/keepalive';
import { HttpClient } from '@angular/common/http';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

import { Router } from '@angular/router';
import { SharedDataService } from './common/service/shared-data.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
    message1: string;
    message: string;
    header: string;
    timeout = false;
     expire = false;
    lastPing?: Date = null;
    idleState = 'Not started.';
    SESSION_TIMEOUT_DEFAULT: any; // in seconds
    SESSION_TIMEOUT_WARNING: any; // in seconds

   constructor(private idle: Idle,
    private keepalive: Keepalive,
    private _router: Router,
    private _sharedDataService: SharedDataService, private _http: HttpClient, public toastr: ToastsManager, vcr: ViewContainerRef  ) {
        this.toastr.setRootViewContainerRef(vcr);
       this._http.get('/irb/resources/string_config_json').subscribe( data => {
           const property_config: any = data;
           if (property_config) {
            this.SESSION_TIMEOUT_DEFAULT = property_config.SESSION_TIMEOUT_DEFAULT;
            this.SESSION_TIMEOUT_WARNING = property_config.SESSION_TIMEOUT_WARNING;
               idle.setIdle(this.SESSION_TIMEOUT_DEFAULT);
               idle.setTimeout(this.SESSION_TIMEOUT_WARNING);
               idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
               // Session Timed out
               idle.onTimeout.subscribe(() => {
                   this.timeout = true;
                   this.expire = true;
                   this.header = 'Session Expired!';
                   this.message = 'You have been logged out of the application due to extended period of inactivity.';
                   // this._headerService.sessionExpired().subscribe(
                   //         data => {
                   //         } );
                   this.idle.stop();
               });
               idle.onTimeoutWarning.subscribe((countdown) => {
                   this.message = 'You will time out in ' + countdown + ' seconds!. Please continue working to retain session.';
               });
               // About to expire
               idle.onIdleStart.subscribe(() => {
                    this.timeout = true;
                    this.expire = false;
                    this.header = 'Session about to Expire';
                    this.message = 'Your session is about to expire. Please click ok to continue working.';
              });
               // Regain session
               idle.onIdleEnd.subscribe(() => this.idleState = 'No longer idle.');
             // sets the ping interval to 15 seconds
               keepalive.interval(15);
               keepalive.onPing.subscribe(() => this.lastPing = new Date());
               this.reset();
           }
           });

    }

    reset() {
      this.idle.watch();
    }

    closePopup() {
        this.timeout = false;
    }

    logout() {
        this.timeout = false;

        this._sharedDataService.changeCurrentTab(null);
        this._sharedDataService.searchData = null;
        this._sharedDataService.isAdvancesearch = false;
        this.idle.stop();
        this._router.navigate(['/logout']);
        // this._loginService.logout().subscribe(data => {
        //     this._sharedDataService.changeCurrentTab(null);
        //     this._sharedDataService.searchData = null;
        //     this._sharedDataService.isAdvancesearch = false;
        //     if (data === true) {
        //         sessionStorage.removeItem('ActivatedUser');
        //         this.idle.stop();
        //         this._router.navigate(['/login']);
        //     } else {
        //         this._router.navigate(['/logout']);
        //     }
        // });
    }
}
