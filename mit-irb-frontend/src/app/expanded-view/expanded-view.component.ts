import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

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
    sortFieldProtocol = 'UPDATE_TIMESTAMP';
    sortOrderProtocol = '1';
    columnProtocol = 'UPDATE_TIMESTAMP';
    directionProtocol: number;

    sortFieldExempt = 'UPDATE_TIMESTAMP';
    sortOrderExempt = '1';
    columnExempt = 'UPDATE_TIMESTAMP';
    directionExempt: number;

    constructor( private _expandedViewService: ExpandedViewService, private _activatedRoute: ActivatedRoute,
        private _spinner: NgxSpinnerService, private _router: Router ) { }

    /** sets page heading and calls function to load snapshot detailed data*/
    ngOnInit() {
        this.requestObject.personId = this._activatedRoute.snapshot.queryParamMap.get( 'personId' );
        this.requestObject.personRoleType = this._activatedRoute.snapshot.queryParamMap.get( 'personRole' );
        this.requestObject.avSummaryType = this._activatedRoute.snapshot.queryParamMap.get( 'summaryType' );
        if ( this.requestObject.avSummaryType === 'AMMEND_RENEW') {
            this.pageHeader = 'Amendments/Renewals in Progress';
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
        this._spinner.show();
        this._expandedViewService.getExpandedList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            this._spinner.hide();
            if ( this.result != null ) {
                if ( this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length === 0 ) {
                    this.noData = true;
                } else {
                    this.expandedList = this.result.dashBoardDetailMap;
                    this.sortByProtocol();
                }
            }  if (this.result.dashboardExemptCardDetails != null && this.result.dashboardExemptCardDetails.length !== 0) {
                this.exemptCardDetails = this.result.dashboardExemptCardDetails;
                this.sortByExempt();
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
        this._router.navigate( ['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber,
                                                                from: this.requestObject.avSummaryType } } );
    }
    openExempt( exemptId, mode, exemptTitle, exemptFormID ) {
        this._router.navigate( ['/irb/exempt-questionaire'],
            { queryParams: { exempHeaderId: exemptId, mode: mode, title: exemptTitle, exemptFormID: exemptFormID,
                             from: this.requestObject.avSummaryType } } );
    }


    /** sets value of direction to implement sorting in tab protocol*/
    updateSortOrderProtocol() {
        this.sortOrderProtocol = this.sortOrderProtocol === '1' ? '-1' : '1';
        this.sortByProtocol();
    }

    sortByProtocol() {
        this.columnProtocol = this.sortFieldProtocol;
        this.directionProtocol = parseInt(this.sortOrderProtocol, 10);
    }

    /** sets value of direction to implement sorting in tab Exempt*/
    updateSortOrderExempt() {
        this.sortOrderExempt = this.sortOrderExempt === '1' ? '-1' : '1';
        this.sortByExempt();
    }

    sortByExempt() {
        this.columnExempt = this.sortFieldExempt;
        this.directionExempt = parseInt(this.sortOrderExempt, 10);
    }
}
