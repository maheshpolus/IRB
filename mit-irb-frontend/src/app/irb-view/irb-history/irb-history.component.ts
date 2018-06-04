import { Component, OnInit } from '@angular/core';
import { IrbViewService } from '../irb-view.service';
import { ActivatedRoute } from '@angular/router';

@Component( {
    selector: 'app-irb-history',
    templateUrl: './irb-history.component.html',
    styleUrls: ['./irb-history.component.css']
} )

export class IrbHistoryComponent implements OnInit {

    noHistoryDetails: boolean;
    noHistoryList = false;
    isExpanded: boolean[] = [];

    irbHistoryDetails = [];
    irbHistoryList = [];
    result: any;
    indexVal: number;

    requestObject = {
        protocolNumber: '',
        protocolId: '',
        actionId: '',
        nextGroupActionId: '',
        previousGroupActionId: ''
    };

    constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute ) { }

    ngOnInit() {
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get( 'protocolNumber' );
        this.loadHistoryList();
    }

    loadHistoryList() {
        this._irbViewService.getProtocolHistotyGroupList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbviewProtocolHistoryGroupList == null || this.result.irbviewProtocolHistoryGroupList.length === 0 ) {
                    this.noHistoryList = true;
                } else {
                    this.irbHistoryList = this.result.irbviewProtocolHistoryGroupList;
                    this.isExpanded.length = this.irbHistoryList.length;
                }
            }

        },
            error => {
                console.log( "Error in method loadHistoryList()", error );
            },
        );
    }

    loadHistoryDetails() {
        this.irbHistoryDetails = [];
        this._irbViewService.getProtocolHistotyGroupDetails( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbviewProtocolHistoryGroupDetails == null ||
                     this.result.irbviewProtocolHistoryGroupDetails.length === 0 ) {
                    this.noHistoryDetails = true;
                } else {
                    this.irbHistoryDetails = this.result.irbviewProtocolHistoryGroupDetails;
                }
            }

        },
            error => {
                console.log( "Error in method loadHistoryDetails()", error );
            },
        );
    }

    toggle( index ) {
        for ( this.indexVal = 0; this.indexVal < this.isExpanded.length; this.indexVal++ ) {
            if ( this.indexVal === index ) {
                this.isExpanded[this.indexVal] = !this.isExpanded[this.indexVal];
            } else {
                this.isExpanded[this.indexVal] = false;
            }
        }
        this.requestObject.protocolId = this.irbHistoryList[index].PROTOCOL_ID;
        this.requestObject.actionId = this.irbHistoryList[index].ACTION_ID;
        this.requestObject.nextGroupActionId = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
        this.requestObject.previousGroupActionId = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
        this.loadHistoryDetails();
    }
}
