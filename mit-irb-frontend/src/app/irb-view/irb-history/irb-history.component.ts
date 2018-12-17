import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IrbViewService } from '../irb-view.service';

@Component({
    selector: 'app-irb-history',
    templateUrl: './irb-history.component.html',
    styleUrls: ['./irb-history.component.css']
})

export class IrbHistoryComponent implements OnInit {

    noHistoryDetails: boolean;
    noHistoryList = false;
    isExpanded: boolean[] = [];

    irbHistoryDetails = [];
    irbHistoryList = [];
    result: any;
    indexVal: number;
    reviewComments = [];

    requestObject = {
        protocolNumber: '',
        protocolId: '',
        actionId: '',
        nextGroupActionId: '',
        previousGroupActionId: ''
    };

    constructor(private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute) { }

    /** sets requestObject and calls functions to load history list */
    ngOnInit() {
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
        this.loadHistoryList();
    }

    /**calls service to get history of protocols created*/
    loadHistoryList() {
        this._irbViewService.getProtocolHistotyGroupList(this.requestObject).subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.irbViewProtocolHistoryGroupList == null || this.result.irbViewProtocolHistoryGroupList.length === 0) {
                    this.noHistoryList = true;
                } else {
                    this.irbHistoryList = this.result.irbViewProtocolHistoryGroupList;
                    this.isExpanded.length = this.irbHistoryList.length;
                }
            }

        },
            error => {
                console.log('Error in method loadHistoryList()', error);
            },
        );
    }

    /**calls service to get details of a selected history */
    loadHistoryDetails() {
        this.irbHistoryDetails = [];
        this._irbViewService.getProtocolHistotyGroupDetails(this.requestObject).subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.irbViewProtocolHistoryGroupDetails == null ||
                    this.result.irbViewProtocolHistoryGroupDetails.length === 0) {
                    this.noHistoryDetails = true;
                } else {
                    this.irbHistoryDetails = this.result.irbViewProtocolHistoryGroupDetails;
                }
            }

        },
            error => {
                console.log('Error in method loadHistoryDetails()', error);
            },
        );
    }

    /**expands and collapse to show detailed history
     * @@param index - unique index of selected history entry
     */
    toggle(index) {
        for (this.indexVal = 0; this.indexVal < this.isExpanded.length; this.indexVal++) {
            if (this.indexVal === index) {
                this.isExpanded[this.indexVal] = !this.isExpanded[this.indexVal];
            } else {
                this.isExpanded[this.indexVal] = false;
            }
            if (this.isExpanded[this.indexVal] === true) {
                this.requestObject.protocolId = this.irbHistoryList[index].PROTOCOL_ID;
                this.requestObject.actionId = this.irbHistoryList[index].ACTION_ID;
                this.requestObject.nextGroupActionId = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
                this.requestObject.previousGroupActionId = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
                this.loadHistoryDetails();
            }
        }
    }
    // Download correspondance letter
    downloadCorrespondanceLetter(actionId, fileName) {
      this._irbViewService.loadProtocolHistoryCorrespondanceLetter( actionId ).subscribe( data => {
        const a = document.createElement( 'a' );
        const blob = new Blob( [data], { type: data.type } );
        a.href = URL.createObjectURL( blob );
        a.download = fileName;
        a.click();

    },
        error => console.log( 'Error downloading the file.'),
        () => console.log( 'OK' ) );
    }
    // Fetching review comments
    getReviewComments(protocolActionId, protocolActionTypecode) {
        this.reviewComments = [];
        const reqObj = { 'protocolNumber': this.requestObject.protocolNumber,
                         'protocolActionId': protocolActionId, 'protocolActionTypecode': protocolActionTypecode};
        this._irbViewService.loadProtocolHistoryActionComments(reqObj).subscribe(
            data => {
                const response: any = data;
                this.reviewComments = response.irbProtocolHistoryActionComments;
            });
    }
}
