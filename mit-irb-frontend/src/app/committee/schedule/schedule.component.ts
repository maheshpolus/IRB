import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { ScheduleService } from '../schedule/schedule.service';
import { ScheduleConfigurationService } from './schedule-configuration.service';

@Component( {
    selector: 'app-schedule-component',
    templateUrl: './schedule.component.html'
} )
export class ScheduleComponent implements OnInit, OnDestroy {

    currentTab = 'schedule_home';
    scheduleId: number;
    result: any = {};
    public loadScheduleDataSub: Subscription;

    constructor( private scheduleService: ScheduleService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private scheduleConfigurationService: ScheduleConfigurationService ) {
    }

    ngOnInit() {
        this.scheduleId = this.activatedRoute.snapshot.queryParams['scheduleId'];
        this.loadScheduleDataSub = this.scheduleService.loadScheduleData( this.scheduleId ).
            subscribe( data => {
                this.result = data;
                if (this.result !== null) {
                    this.scheduleConfigurationService.changeScheduleData( this.result );
                }
            } );
    }
    ngOnDestroy() {
        this.loadScheduleDataSub.unsubscribe();
    }

    show_current_tab( e: any, current_tab ) {
        e.preventDefault();
        this.currentTab = current_tab;
    }
}
