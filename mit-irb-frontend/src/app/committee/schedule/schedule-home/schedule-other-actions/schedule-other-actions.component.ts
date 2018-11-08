import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ScheduleConfigurationService } from '../../schedule-configuration.service';
import { ScheduleService } from '../../schedule.service';
import { ScheduleOtherActionsService } from './schedule-other-actions.service';
import { Subject } from "rxjs/Subject";
import 'rxjs/add/operator/takeUntil';

@Component( {
    selector: 'app-schedule-other-actions',
    templateUrl: './schedule-other-actions.component.html'
} )

export class ScheduleOtherActionsComponent implements OnInit {
    result: any = {};
    committeeScheduleActItemsObject: any = {};
    otherActionsDescription: string = '';
    scheduleId: number;
    tempOtherAction: any = {};
    showPopup: boolean = false;
    currentUser = localStorage.getItem( "currentUser" );
    isMandatoryFilled: boolean = true;
    mandatoryMessage: string;
    public onDestroy$ = new Subject<void>();

    constructor( public scheduleOtherActionsService: ScheduleOtherActionsService, public activatedRoute: ActivatedRoute, public scheduleService: ScheduleService, public scheduleConfigurationService: ScheduleConfigurationService ) { }

    ngOnInit() {
        this.scheduleId = this.activatedRoute.snapshot.queryParams['scheduleId'];
        this.scheduleConfigurationService.currentScheduleData.takeUntil(this.onDestroy$).subscribe( data => {
            this.result = data;
        } );
    }
    
    ngOnDestroy() {
        this.onDestroy$.next();
        this.onDestroy$.complete();
    }
    
    OtherActionsTypeChange( type ) {
        var d = new Date();
        var time = d.getTime();
        for ( let actionType of this.result.scheduleActItemTypes ) {
            if ( actionType.description == type ) {
                this.committeeScheduleActItemsObject.scheduleActItemTypecode = actionType.scheduleActItemTypecode;
                this.committeeScheduleActItemsObject.scheduleActItemTypeDescription = actionType.description;
                this.committeeScheduleActItemsObject.updateUser = this.currentUser;
                this.committeeScheduleActItemsObject.updateTimestamp = time;
                this.result.committeeScheduleActItems = this.committeeScheduleActItemsObject;
            }
        }
    }

    addOtherActions() {
        if ( this.committeeScheduleActItemsObject.scheduleActItemTypeDescription == null ) {
            var d = new Date();
            var time = d.getTime();
            for ( let actionType of this.result.scheduleActItemTypes ) {
                if ( actionType.description == 'Adverse Event' ) {
                    this.committeeScheduleActItemsObject.scheduleActItemTypecode = actionType.scheduleActItemTypecode;
                    this.committeeScheduleActItemsObject.scheduleActItemTypeDescription = actionType.description;
                    this.committeeScheduleActItemsObject.updateUser = this.currentUser;
                    this.committeeScheduleActItemsObject.updateTimestamp = time;
                    this.result.committeeScheduleActItems = this.committeeScheduleActItemsObject;
                }
            }
        }
        if ( this.otherActionsDescription.trim().length != 0 && this.otherActionsDescription != '' && this.otherActionsDescription != null ) {
            this.isMandatoryFilled = true;
            this.mandatoryMessage = '';
            this.committeeScheduleActItemsObject.itemDescription = this.otherActionsDescription;
            this.result.committeeScheduleActItems = this.committeeScheduleActItemsObject;
            this.scheduleOtherActionsService.addOtherActions( this.result.committee.committeeId, this.scheduleId, this.result.committeeScheduleActItems ).takeUntil(this.onDestroy$).subscribe( data => {
                var temp: any = {};
                temp = data;
                this.result.committeeSchedule.committeeScheduleActItems = temp.committeeSchedule.committeeScheduleActItems;
            } );
        } else {
            this.isMandatoryFilled = false;
            this.mandatoryMessage = '* Please fill mandatory fields';
        }
        this.committeeScheduleActItemsObject = {};
        this.otherActionsDescription = ' ';
    }

    deleteOtherActions(  ) {
        this.scheduleOtherActionsService.deleteOtherActions( this.result.committee.committeeId, this.scheduleId, this.tempOtherAction.commScheduleActItemsId ).takeUntil(this.onDestroy$).subscribe( data => {
            var temp: any = {};
            temp = data;
            this.result.committeeSchedule.committeeScheduleActItems = temp.committeeSchedule.committeeScheduleActItems;
        });
    }
    
    //save temporarily during modal pop up
    tempSave( event: any, otherAction ) {
        event.preventDefault();
        this.showPopup = true;
        this.tempOtherAction = otherAction;
    }
}
