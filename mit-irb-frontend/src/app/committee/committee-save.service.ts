import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class CommitteeSaveService {

    constructor( private http: HttpClient) {

    }

    saveCommitteeData( committeeObj: Object ): Observable<JSON> {
        return this.http.post( '/irb/saveCommittee', committeeObj )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveResearchAreaCommitteeData( committeeId, committeeObj ) {
        const params = {
            committeeId: committeeId,
            committeeResearchArea: committeeObj
        };
        return this.http.post( '/irb/saveAreaOfResearch', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    deleteAreaOfResearch( commResearchAreasId, committeeId ) {
        const params = {
            commResearchAreasId: commResearchAreasId,
            committeeId: committeeId
        };
        return this.http.post( '/irb/deleteAreaOfResearch', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/irb/addSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    updateScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/irb/updateCommitteeSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    deleteScheduleData( sendScheduleRequestData: Object ): Observable<JSON> {
        return this.http.post( '/irb/deleteSchedule', sendScheduleRequestData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    filterScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/irb/filterCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    resetFilterSchedule( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/irb/resetCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }
    loadHomeUnits(unitSearchString: string) {
        const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
        return this.http.post('/irb/loadHomeUnits', params);
      }
      loadResearchAreas(researchSearchString: string) {
        const params = new HttpParams().set('researchSearchString', researchSearchString);
        return this.http.post('/irb/loadResearchAreas', params);
      }
}
