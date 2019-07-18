import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs/Subscription';
import {Location} from '@angular/common';

import { ScheduleService } from '../schedule/schedule.service';
import { ScheduleConfigurationService } from './schedule-configuration.service';

@Component( {
    selector: 'app-schedule-component',
    templateUrl: './schedule.component.html'
} )
export class ScheduleComponent implements OnInit, OnDestroy {

    currentTab = 'schedule_home';
    scheduleId = null;
    committeeId = null;
    result: any = {};
    public loadScheduleDataSub: Subscription;

    constructor( private scheduleService: ScheduleService,
        private _location: Location,
        private _spinner: NgxSpinnerService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private scheduleConfigurationService: ScheduleConfigurationService ) {
            this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get( 'scheduleId' );
            this.committeeId = this.activatedRoute.snapshot.queryParamMap.get( 'committeeId' );
    }

    ngOnInit() {
       this._spinner.show();
        const params = {
            scheduleId: this.scheduleId,
            committeeId: this.committeeId
    };
        this.loadScheduleDataSub = this.scheduleService.loadScheduleData( params ).
            subscribe( data => {
                this.result = data;
                this.result.committeeSchedule.meetingDate = this.result.committeeSchedule.meetingDate != null ?
                    new Date(this.result.committeeSchedule.meetingDate) : null;
                this._spinner.hide();
                if (this.result !== null) {
                    this.scheduleConfigurationService.changeScheduleData( this.result );
                }
            });

    }
    ngOnDestroy() {
        this.loadScheduleDataSub.unsubscribe();
    }

    onActivate( componentRef ) {
        this.activatedRoute = componentRef;
    }
    show_current_tab( e: any, current_tab ) {
        e.preventDefault();
        this.currentTab = current_tab;
        this.router.navigate( ['irb/committee/schedule/' + this.currentTab],
                { queryParams: { 'scheduleId': this.scheduleId, 'committeeId': this.committeeId } } );
    }
    backClick() {
        this._location.back();
    }
}
