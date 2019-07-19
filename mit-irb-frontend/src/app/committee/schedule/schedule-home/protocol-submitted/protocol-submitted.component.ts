import { Component, OnInit } from '@angular/core';
import {Router, ActivatedRoute } from '@angular/router';
import { ScheduleHomeService } from '../schedule-home.service';
import { Subscription } from 'rxjs/Subscription';

@Component( {
    selector: 'app-protocol-submitted',
    templateUrl: './protocol-submitted.component.html'
} )
export class ProtocolSubmittedComponent implements OnInit {
    public result: any = {};
    outputPath: string;
    userName: string;
    scheduleId = null;
    protocolList = [];
    sortOrder = '1';
    column = 'UPDATE_TIMESTAMP';
    sortField = 'UPDATE_TIMESTAMP';
    direction: number;

    public subscription: Subscription;
    constructor(private _router: Router, private activatedRoute: ActivatedRoute,
        private _scheduleHomeService: ScheduleHomeService) {
            this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get( 'scheduleId' );
         }

    ngOnInit() {
    this.getProtocolSubmitted();
    }



    getProtocolSubmitted() {
        const obj = {scheduleId: this.scheduleId};
        this._scheduleHomeService.loadScheduledProtocols(obj).subscribe( data => {
            this.result = data;
            this.protocolList = this.result.submittedProtocolsList != null ? this.result.submittedProtocolsList : [];
        });
    }


    /** sets value of direction to implement sorting in tabs other than EXEMPT, COMMITEE, SCHEDULE*/
    updateSortOrder() {
        this.sortOrder = this.sortOrder === '1' ? '-1' : '1';
        this.sortBy();
    }

    /** sets value of column and direction to implement sorting */
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt(this.sortOrder, 10);
    }

    openProtocolView( protocolNumber ) {
        this._router.navigate( ['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber} } );
    }
}
