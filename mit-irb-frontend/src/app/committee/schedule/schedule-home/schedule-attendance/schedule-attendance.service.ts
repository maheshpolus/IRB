import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { HttpClient } from '@angular/common/http';

@Injectable()
export class ScheduleAttendanceService {

    constructor( private http: HttpClient ) { }

    addGuestMember( guestMemberObj: Object, scheduleId: number ) {
        const params = {
            scheduleId: scheduleId,
            updatedAttendance: guestMemberObj
        };
        return this.http.post( '/irb/addOthersPresent', params )
            .catch( error => {
                console.log( error || error );
                return Observable.throw( error );
            } );
    }

    updateMemberattendanceDate( committeeId: string, scheduleId: number, memberObj: Object ) {
        const params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            updatedAttendance: memberObj
        };

        return this.http.post( '/irb/updateScheduleAttendance', params )
            .catch( error => {
                console.log( error );
                return Observable.throw( error );
            } );
    }

    deleteScheduleMemberAttendance( committeeId: string, scheduleId: number, commScheduleAttendanceId: number ) {
        const params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            commScheduleAttendanceId: commScheduleAttendanceId
        };
        return this.http.post( '/irb/deleteScheduleAttendance', params )
            .catch( error => {
                console.log( error );
                return Observable.throw( error );
            } );
    }
    getMeetingAttendenceList(params) {
        return this.http.post( '/irb/loadMeetingAttendence', params );
    }
    updateMeetingAttendence(params) {
        return this.http.post( '/irb/updateMeetingAttendence', params );
    }
    getAllActiveMembers(params) {
        return this.http.post( '/irb/showAllMeetingAttendence', params );
    }
}
