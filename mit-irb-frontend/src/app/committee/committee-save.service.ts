import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';

@Injectable()
export class CommitteeSaveService {

    constructor( private http: HttpClient) {

    }

    saveCommitteeData( committeeObj: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/saveCommittee', committeeObj )
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
        return this.http.post( '/mit-irb/saveAreaOfResearch', params )
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
        return this.http.post( '/mit-irb/deleteAreaOfResearch', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    saveScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/addSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    updateScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/updateCommitteeSchedule', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    deleteScheduleData( sendScheduleRequestData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/deleteSchedule', sendScheduleRequestData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    filterScheduleData( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/filterCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    resetFilterSchedule( scheduleData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/resetCommitteeScheduleDates', scheduleData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }
    loadHomeUnits(unitSearchString: string) {
        const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
        return this.http.post('/mit-irb/loadHomeUnits', params);
      }
      loadResearchAreas(researchSearchString: string) {
        const params = new HttpParams().set('researchSearchString', researchSearchString);
        return this.http.post('/mit-irb/loadResearchAreas', params);
      }
      getCommitteeMembersById(params) {
        return this.http.post('/mit-irb/loadCommitteeMemberById', params);
      }
      getCommitteeMemberDetails(params) {
        return this.http.post('/mit-irb/loadCommitteeMemberDetails', params);
      }
      updateCommitteMembers(params) {
        return this.http.post('/mit-irb/updateCommitteeMemberDetails', params);
      }
      getMemberTermHistory(params) {
        return this.http.post('/mit-irb/loadTerHistory', params);
      }
      updateMemberRole(params) {
        return this.http.post('/mit-irb/updateMemberRole', params);
      }
      updateMemberExpertise(params) {
        return this.http.post('/mit-irb/updateMemberExpertise', params);
      }
      getIrbPersonDetailedList(params) {
        return this.http.post('/mit-irb/getMITKCPersonInfo', params);
    }
    downloadTrainingAttachment(fileId) {
        return this.http.get('/mit-irb/downloadFileData', {
            headers: new HttpHeaders().set('fileDataId', fileId.toString()),
            responseType: 'blob'
        });
    }
}
