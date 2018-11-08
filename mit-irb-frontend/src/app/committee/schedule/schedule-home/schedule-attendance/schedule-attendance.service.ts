import { Injectable } from '@angular/core';
import { Observable } from "rxjs/Observable";
import { Http } from "@angular/http";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { HttpClient } from "@angular/common/http";

@Injectable()
export class ScheduleAttendanceService {

    constructor( private http: HttpClient ) { }

    addGuestMember( guestMemberObj: Object, scheduleId: number ){
        var params = {
            scheduleId: scheduleId,
            updatedAttendance: guestMemberObj
        };
        return this.http.post( '/mit-irb/addOthersPresent', params )
            .catch( error => {
                console.log( error || error )
                return Observable.throw( error )
            } );
    }

    updateMemberattendanceDate( committeeId: string, scheduleId: number, memberObj: Object ){
        var params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            updatedAttendance: memberObj
        };

        return this.http.post( '/mit-irb/updateScheduleAttendance', params )
            .catch( error => {
                console.log( error );
                return Observable.throw( error )
            } );
    }

    deleteScheduleMemberAttendance( committeeId: string, scheduleId: number, commScheduleAttendanceId: number ) {
        var params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            commScheduleAttendanceId: commScheduleAttendanceId
        };
        return this.http.post( '/mit-irb/deleteScheduleAttendance', params )
            .catch( error => {
                console.log( error );
                return Observable.throw( error )
            } );
    }
}
