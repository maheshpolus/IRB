import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ExpandedViewService } from './expanded-view.service';
import { SharedDataService } from '../common/service/shared-data.service';
import { PermissionWarningModalComponent } from '../common/permission-warning-modal/permission-warning-modal.component';


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
    userDTO: any = {};

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
        private _spinner: NgxSpinnerService, private _router: Router, private modalService: NgbModal, private _sharedDataService: SharedDataService ) { }

    /** sets page heading and calls function to load snapshot detailed data*/
    ngOnInit() {
    	this.userDTO = this._activatedRoute.snapshot.data['irb'];
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
      /**
   * @param  {} protocolNumber
   * @param  {} protocolId
   * open the IRB protocol in edit mode
   */
  EditIrb (protocolNumber, protocolId) {
    const requestObject = {
      acType: 'E', department: this.userDTO.unitNumber, personId: this.userDTO.personID, protocolId: protocolId
  };
  this._sharedDataService.checkUserPermission(requestObject).subscribe((data: any) => {
  const hasPermission = data.successCode;
  if (hasPermission) {
     this._router.navigate( ['/irb/irb-create'], {queryParams: {protocolNumber: protocolNumber, protocolId: protocolId}});
} else {
  const alertMessage = 'You do not have permission to edit this protocol';
  this.openPermissionWarningModal(alertMessage);
}
});
  }
  
   openPermissionWarningModal(alertMessage) {
     const modalRef = this.modalService.open(PermissionWarningModalComponent, { backdrop : 'static'});
     modalRef.componentInstance.alertMessage = alertMessage;
   }
}
