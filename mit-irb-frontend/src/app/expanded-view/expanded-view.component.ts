import { Component, OnInit } from '@angular/core';
import { ExpandedViewService } from "./expanded-view.service";
import { ActivatedRoute, Router } from "@angular/router";

@Component( {
    selector: 'app-expanded-view',
    templateUrl: './expanded-view.component.html',
    styleUrls: ['./expanded-view.component.css']
} )
export class ExpandedViewComponent implements OnInit {

    noData = false;
    pageHeader: string;

    expandedList: any;
    result: any;
    lastClickedTab = '';
    requestObject = {
        personId: '',
        person_role_type: '',
        av_summary_type: ''
    }

    constructor( private _expandedViewService: ExpandedViewService, private route: ActivatedRoute, private router: Router ) { }

    ngOnInit() {
        this.requestObject.personId = this.route.snapshot.queryParamMap.get( 'personId' );
        this.requestObject.person_role_type = this.route.snapshot.queryParamMap.get( 'personRole' );
        this.requestObject.av_summary_type = this.route.snapshot.queryParamMap.get( 'summaryType' );
        if( this.requestObject.av_summary_type == 'AMMEND_RENEW') {
            this.pageHeader = 'Ammend/Renewals in Progress';
        }
        else if( this.requestObject.av_summary_type == 'REVISION_REQ') {
            if( this.requestObject.person_role_type == 'PI') {
                this.pageHeader = 'Revision Requested';
            }
            else {
                this.pageHeader = 'Pending Responses';
            }
        }
        else if( this.requestObject.av_summary_type == 'AWAITING_RESP') {
            this.pageHeader = 'Awaiting IRB Response';
        }
        this.loadExpandedData();
    }

    loadExpandedData() {
        this._expandedViewService.getExpandedList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length == 0 ) {
                    this.noData = true;
                }
                else {
                    this.expandedList = this.result.dashBoardDetailMap;
                }
            }

        },
            error => {
                console.log( error );
            },
        );
    }
    openIrbView( protocolNumber ) {
        this.router.navigate( ['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber } } );
    }
}
