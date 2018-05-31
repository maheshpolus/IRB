import { Component, OnInit } from '@angular/core';
import { IrbViewService } from "../irb-view.service";
import { ActivatedRoute } from "@angular/router";

@Component( {
    selector: 'app-irb-history',
    templateUrl: './irb-history.component.html',
    styleUrls: ['./irb-history.component.css']
} )
export class IrbHistoryComponent implements OnInit {
    noHistoryDetails: boolean;

    irbHistoryDetails = [];
    noHistoryList = false;

    irbHistoryList = [];


    isExpanded: boolean[] = [];
    requestObject = {
        protocol_number: '',
        protocol_id: '',
        action_id: '',
        next_group_action_id: '',
        previous_group_action_id: ''
    };
    result: any;
    indexVal: number;
    constructor( private _irbViewService: IrbViewService, private route: ActivatedRoute ) { }

    ngOnInit() {
        this.requestObject.protocol_number = this.route.snapshot.queryParamMap.get( 'protocolNumber' );
        this.loadHistoryList();
        this.isExpanded.length = this.irbHistoryList.length;
    }

    loadHistoryList() {
        this._irbViewService.getProtocolHistotyGroupList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbviewProtocolHistoryGroupList == null || this.result.irbviewProtocolHistoryGroupList.length == 0 ) {
                    this.noHistoryList = true;
                }
                else {
                    this.irbHistoryList = this.result.irbviewProtocolHistoryGroupList;
                }
            }

        },
            error => {
                console.log( error );
            },
        );
    }

    loadHistoryDetails() {
        this.irbHistoryDetails = [];
        this._irbViewService.getProtocolHistotyGroupDetails( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbviewProtocolHistoryGroupDetails == null || this.result.irbviewProtocolHistoryGroupDetails.length == 0 ) {
                    this.noHistoryDetails = true;
                }
                else {
                    this.irbHistoryDetails = this.result.irbviewProtocolHistoryGroupDetails;
                }
            }

        },
            error => {
                console.log( error );
            },
        );
    }

    toggle( index ) {
        for ( this.indexVal = 0; this.indexVal < this.isExpanded.length; this.indexVal++ ) {
            if ( this.indexVal == index ) {
                this.isExpanded[this.indexVal] = !this.isExpanded[this.indexVal];
            }
            else {
                this.isExpanded[this.indexVal] = false;
            }
        }
        this.requestObject.protocol_id = this.irbHistoryList[index].PROTOCOL_ID;
        this.requestObject.action_id = this.irbHistoryList[index].ACTION_ID;
        this.requestObject.next_group_action_id = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
        this.requestObject.previous_group_action_id = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
        this.loadHistoryDetails();
    }
}
