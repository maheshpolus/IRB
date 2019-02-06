import { Component, OnInit, OnDestroy } from '@angular/core';
import {Router } from '@angular/router';

import { ScheduleConfigurationService } from '../../schedule-configuration.service';
import { Subscription } from 'rxjs/Subscription';

@Component( {
    selector: 'app-protocol-submitted',
    templateUrl: './protocol-submitted.component.html'
} )
export class ProtocolSubmittedComponent implements OnInit, OnDestroy {
    public result: any = {};
    outputPath: string;
    userName: string;
    public subscription: Subscription;
    constructor( private scheduleConf: ScheduleConfigurationService,  private _router: Router) { }

    ngOnInit() {
       this.subscription = this.scheduleConf.currentScheduleData.subscribe( data => {
            this.result = data;
            this.outputPath = 'http://192.168.1.76:8080/kc-dev';
            this.userName = localStorage.getItem( 'currentUser' );
        } );

    }
    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
    openProtocolView( protocolNumber ) {
        this._router.navigate( ['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber} } );
    }
}
