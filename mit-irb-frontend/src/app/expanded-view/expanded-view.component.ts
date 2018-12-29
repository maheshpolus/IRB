import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ExpandedViewService } from './expanded-view.service';

@Component( {
    selector: 'app-expanded-view',
    templateUrl: './expanded-view.component.html',
    styleUrls: ['./expanded-view.component.css']
} )

export class ExpandedViewComponent implements OnInit {

    noData = false;

    pageHeader: string;
    expandedList: any;
    exemptCardDetails: any;
    result: any;
    lastClickedTab = '';

    requestObject = {
        personId: '',
        personRoleType: '',
        avSummaryType: ''
    };

    constructor( private _expandedViewService: ExpandedViewService, private _activatedRoute: ActivatedRoute, private _router: Router ) { }

    /** sets page heading and calls function to load snapshot detailed data*/
    ngOnInit() {
        this.requestObject.personId = this._activatedRoute.snapshot.queryParamMap.get( 'personId' );
        this.requestObject.personRoleType = this._activatedRoute.snapshot.queryParamMap.get( 'personRole' );
        this.requestObject.avSummaryType = this._activatedRoute.snapshot.queryParamMap.get( 'summaryType' );
        if ( this.requestObject.avSummaryType === 'AMMEND_RENEW') {
            this.pageHeader = 'Ammend/Renewals in Progress';
        } else if ( this.requestObject.avSummaryType === 'REVISION_REQ') {
            if ( this.requestObject.personRoleType === 'PI') {
                this.pageHeader = 'Pending Protocols';
            } else {
                this.pageHeader = 'Pending Responses';
            }
        } else if ( this.requestObject.avSummaryType === 'AWAITING_RESP') {
            this.pageHeader = 'Awaiting IRB Response';
        }
        this.loadExpandedData();
    }

    /** calls service to load details of selected snapshot*/
    loadExpandedData() {
        this._expandedViewService.getExpandedList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length === 0 ) {
                    this.noData = true;
                } else {
                    this.expandedList = this.result.dashBoardDetailMap;
                }
            }  if (this.result.dashboardExemptCardDetails != null && this.result.dashboardExemptCardDetails.length !== 0) {
                this.exemptCardDetails = this.result.dashboardExemptCardDetails;
            }

        },
            error => {
                console.log( 'Error in method loadExpandedData()', error );
            },
        );
    }

    /** opens irb view module
     * @param protocolNumber - unique identifier of a protocol
    */
    openIrbView( protocolNumber ) {
        this._router.navigate( ['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber } } );
    }
    openExempt( exemptId, mode, exemptTitle, exemptFormID ) {
        this._router.navigate( ['/irb/exempt-questionaire'],
            { queryParams: { exempHeaderId: exemptId, mode: mode, title: exemptTitle, exemptFormID: exemptFormID } } );
    }
}
