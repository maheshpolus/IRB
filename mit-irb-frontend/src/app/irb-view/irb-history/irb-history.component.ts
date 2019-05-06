import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';

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
    sortField = '';
    sortOrder = 1;

    requestObject = {
        protocolNumber: '',
        protocolId: '',
        actionId: '',
        nextGroupActionId: '',
        previousGroupActionId: ''
    };

    PROTOCOL_HISTORY_INFO: string;

    constructor(private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute, private _http: HttpClient,
        private router: Router, private _spinner: NgxSpinnerService) { }

    /** sets requestObject and calls functions to load history list */
    ngOnInit() {
        this._http.get('/mit-irb/resources/string_config_json').subscribe(
            data => {
                const property_config: any = data;
                if (property_config) {
                    this.PROTOCOL_HISTORY_INFO = property_config.PROTOCOL_HISTORY_INFO;
                }
            });
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
        this.loadHistoryList();
    }

    /**calls service to get history of protocols created*/
    loadHistoryList() {
        this._spinner.show();
        this._irbViewService.getProtocolHistotyGroupList(this.requestObject).subscribe(data => {
            this._spinner.hide();
            this.result = data || [];
            if (this.result != null) {
                if (this.result.irbViewProtocolHistoryGroupList == null || this.result.irbViewProtocolHistoryGroupList.length === 0) {
                    this.noHistoryList = true;
                } else {
                    this.irbHistoryList = this.result.irbViewProtocolHistoryGroupList;
                    this.isExpanded.length = this.irbHistoryList.length;
                    this.isExpanded.fill(true);
                }
            }

        },
            error => {
                console.log('Error in method loadHistoryList()', error);
            },
        );
    }

    /**calls service to get details of a selected history */
    loadHistoryDetails(index) {
        // this.irbHistoryDetails = [];
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
        // for (this.indexVal = 0; this.indexVal < this.isExpanded.length; this.indexVal++) {
        //     if (this.indexVal === index) {
        //         this.isExpanded[this.indexVal] = !this.isExpanded[this.indexVal];
        //     }
        //     if (this.isExpanded[this.indexVal] === true) {
        //         this.requestObject.protocolId = this.irbHistoryList[index].PROTOCOL_ID;
        //         this.requestObject.actionId = this.irbHistoryList[index].ACTION_ID;
        //         this.requestObject.nextGroupActionId = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
        //         this.requestObject.previousGroupActionId = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
        //         this.loadHistoryDetails(index);
        //     }
        // }
        this.isExpanded[index] = !this.isExpanded[index];
        // if (this.isExpanded[index] === true) {
        //     this.requestObject.protocolId = this.irbHistoryList[index].PROTOCOL_ID;
        //     this.requestObject.actionId = this.irbHistoryList[index].ACTION_ID;
        //     this.requestObject.nextGroupActionId = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
        //     this.requestObject.previousGroupActionId = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
        //     this.loadHistoryDetails(index);
        // }

    }


    viewSubmissionDetails(submissionProtocolNumber, comment) {
        this.router.navigate(['/irb/irb-view/irbHistory/submission-detail'],
            {
                queryParams: {
                    protocolNumber: this._activatedRoute.snapshot.queryParamMap.get('protocolNumber'),
                    submissionProtocolNumber: submissionProtocolNumber, comment: comment
                }
            });
    }


test() {
    this.isExpanded.fill(true);
}
test1() {
this.isExpanded.fill(false);

}

    // Download correspondance letter
    downloadCorrespondanceLetter(actionId, fileName) {
        this._irbViewService.loadProtocolHistoryCorrespondanceLetter(actionId).subscribe(data => {
            const a = document.createElement('a');
            const blob = new Blob([data], { type: data.type });
            a.href = URL.createObjectURL(blob);
            a.download = fileName;
            document.body.appendChild(a);
            a.click();

        },
            error => console.log('Error downloading the file.'),
            () => console.log('OK'));
    }
    // Fetching review comments
    getReviewComments(protocolActionId, protocolActionTypecode) {
        this.reviewComments = [];
        const reqObj = {
            'protocolNumber': this.requestObject.protocolNumber,
            'protocolActionId': protocolActionId, 'protocolActionTypecode': protocolActionTypecode
        };
        this._irbViewService.loadProtocolHistoryActionComments(reqObj).subscribe(
            data => {
                const response: any = data;
                this.reviewComments = response.irbProtocolHistoryActionComments;
            });
    }

    updateSortOrder() {
        this.sortOrder = this.sortOrder === 1 ? -1 : 1;
    }
}
