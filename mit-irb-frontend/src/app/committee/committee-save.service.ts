import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';

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
      getCommitteeMembersById(params) {
        return this.http.post('/irb/loadCommitteeMemberById', params);
      }
      getCommitteeMemberDetails(params) {
        return this.http.post('/irb/loadCommitteeMemberDetails', params);
      }
      updateCommitteMembers(params) {
        return this.http.post('/irb/updateCommitteeMemberDetails', params);
      }
      getMemberTermHistory(params) {
        return this.http.post('/irb/loadTerHistory', params);
      }
      updateMemberRole(params) {
        return this.http.post('/irb/updateMemberRole', params);
      }
      updateMemberExpertise(params) {
        return this.http.post('/irb/updateMemberExpertise', params);
      }
      getIrbPersonDetailedList(params) {
        return this.http.post('/irb/getMITKCPersonInfo', params);
    }
    downloadTrainingAttachment(fileId) {
        return this.http.get('/irb/downloadFileData', {
            headers: new HttpHeaders().set('fileDataId', fileId.toString()),
            responseType: 'blob'
        });
    }
}
