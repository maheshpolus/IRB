import { Component, OnInit, OnDestroy } from '@angular/core';
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
    constructor( private scheduleConf: ScheduleConfigurationService) { }

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
}
