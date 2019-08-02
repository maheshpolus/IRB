import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ScheduleConfigurationService } from '../../schedule-configuration.service';
import { ScheduleService } from '../../schedule.service';
import { ScheduleOtherActionsService } from './schedule-other-actions.service';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/takeUntil';
import { NgxSpinnerService } from 'ngx-spinner';

@Component( {
    selector: 'app-schedule-other-actions',
    templateUrl: './schedule-other-actions.component.html'
} )

export class ScheduleOtherActionsComponent implements OnInit {
    result: any = {};
    committeeScheduleActItemsObject: any = {};
    otherActionsDescription = '';
    scheduleId: number;
    isAddClicked = false;
    userDTO = JSON.parse(localStorage.getItem('currentUser'));
    isMandatoryFilled = true;
    deleteIndex = null;
    isAddOtherAction = false;
    otherActionTypes: any = [];
    committeeScheduleActItems: any = [];

    constructor( public scheduleOtherActionsService: ScheduleOtherActionsService,
        public activatedRoute: ActivatedRoute,
        public scheduleService: ScheduleService,
         private _spinner: NgxSpinnerService) { }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            this.scheduleId = params['scheduleId'];
          });
        this.getOtherActions();
    }

    getOtherActions() {
        const requestObject = { scheduleId: this.scheduleId };
        this._spinner.show();
        this.scheduleOtherActionsService.getOtherActions(requestObject).subscribe((data: any) => {
            this._spinner.hide();
            this.otherActionTypes = data.scheduleActItemTypes != null ? data.scheduleActItemTypes : [];
            this.committeeScheduleActItems = data.committeeScheduleActItemsList != null ? data.committeeScheduleActItemsList : [];
            this.committeeScheduleActItemsObject.scheduleActItemTypecode = '5';
            this.setActionItemType(this.committeeScheduleActItemsObject.scheduleActItemTypecode);
        });
    }
    setActionItemType(typeCode) {
        this.otherActionTypes.forEach(type => {
            if ( type.scheduleActItemTypecode.toString() === typeCode ) {
                this.committeeScheduleActItemsObject.scheduleActItemTypeDescription = type.description;
            }
        });
    }
    addOtherAction() {
        if (this.committeeScheduleActItemsObject.itemDescription === '' || this.committeeScheduleActItemsObject.itemDescription == null) {
            this.isMandatoryFilled = false;
        } else {
            this.isMandatoryFilled = true;
            this.saveOtherActions('U', this.committeeScheduleActItemsObject);
        }
    }

    saveOtherActions(mode, otherActions ) {
        otherActions.updateUser = this.userDTO.userName;
        const requestObject = {acType: mode, scheduleId: this.scheduleId, committeeScheduleActItems: otherActions};
        this._spinner.show();
        this.scheduleOtherActionsService.updateOtherActions(requestObject).subscribe((data: any) => {
            this._spinner.hide();
            this.committeeScheduleActItems = data.committeeScheduleActItemsList != null ? data.committeeScheduleActItemsList : [];
            this.committeeScheduleActItemsObject.scheduleActItemTypecode = '5';
            this.setActionItemType(this.committeeScheduleActItemsObject.scheduleActItemTypecode);
            this.committeeScheduleActItemsObject.itemDescription = null;

        });
    }

    deleteOtherActions(otherActionId) {
        const requestObject = {acType: 'D', scheduleId: this.scheduleId, commScheduleActItemsId: otherActionId};
        this._spinner.show();
        this.scheduleOtherActionsService.updateOtherActions(requestObject).subscribe((data: any) => {
            this._spinner.hide();
            this.committeeScheduleActItems = data.committeeScheduleActItemsList != null ? data.committeeScheduleActItemsList : [];
        });
    }
}
