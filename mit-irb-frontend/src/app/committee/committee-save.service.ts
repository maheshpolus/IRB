import { Injectable } from '@angular/core';
import { Http, HttpModule } from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';
import { HttpClient } from "@angular/common/http";

@Injectable()
export class CommitteeSaveService {

    constructor( private http: HttpClient) {

    }

    saveCommitteeData( committeeObj: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/saveCommittee', committeeObj )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    saveResearchAreaCommitteeData( committeeId, committeeObj ) {
        var params = {
            committeeId: committeeId,
            committeeResearchArea: committeeObj
        };
        return this.http.post( '/mit-irb/saveAreaOfResearch', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    deleteAreaOfResearch( commResearchAreasId, committeeId ) {
        var params = {
            commResearchAreasId: commResearchAreasId,
            committeeId: committeeId
        };
        return this.http.post( '/mit-irb/deleteAreaOfResearch', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    saveScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/addSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    updateScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/updateCommitteeSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    deleteScheduleData( sendScheduleRequestData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/deleteSchedule', sendScheduleRequestData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    filterScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/filterCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }

    resetFilterSchedule( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/resetCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error )
            } );
    }
}
