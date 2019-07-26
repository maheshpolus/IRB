import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { HttpClient, HttpHeaders } from '@angular/common/http';
@Injectable()
export class ScheduleService {
    constructor( private http: HttpClient ) {

    }

    public loadScheduleData( params ) {
            return this.http.post( '/mit-irb/loadScheduleById', params )
                .catch( error => {
                    console.error( error.message || error );
                    return Observable.throw( error.message || error );
                } );
    }
    public loadScheduleMeetingComments( params ) {
        return this.http.post( '/mit-irb/loadScheduleMeetingComments', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
}
  public loadAttachmentTypeData(scheduleId) {
    return this.http.post('/mit-irb/loadMeetingAttachmentById', {'scheduleId': scheduleId}).catch(error => {
      console.error(error.message || error);
      return Observable.throw(error.message || error);
    });
  }
  createAgendaForSchedule(params) {
  return this.http.post( '/mit-irb/createAgendaForSchedule', params );
  }
  downloadLatestAgenda(scheduleId) {
    return this.http.get('/mit-irb/downloadScheduleAgenda', {
        headers: new HttpHeaders().set('scheduleId', scheduleId.toString()),
        responseType: 'blob'
    });
}

  saveMinuteData( minuteData: Object ): Observable<JSON> {
      return this.http.post( '/mit-irb/addCommitteeScheduleMinute', minuteData )
          .catch( error => {
              console.error( error.message || error );
              return Observable.throw( error.message || error );
          } );
  }

  loadScheduleProtocolComments( params ): Observable<JSON> {
      return this.http.post( '/mit-irb/loadScheduleProtocolComments', params )
          .catch( error => {
              console.error( error.message || error );
              return Observable.throw( error.message || error );
          } );
  }

  deleteMinuteData( minuteData: Object ): Observable<JSON> {
      return this.http.post( '/mit-irb/deleteScheduleMinute', minuteData )
          .catch( error => {
              console.error( error.message || error );
              return Observable.throw( error.message || error );
          } );
  }

  loadAllScheduleAgenda(scheduleId) {
    return this.http.post('/mit-irb/loadAllScheduleAgenda', {scheduleId: scheduleId})
      .catch( error => {
        console.error( error.message || error );
        return Observable.throw( error.message || error );
      } );
  }

  loadAllScheduleMinutes(scheduleId) {
    return this.http.post('/mit-irb/loadAllScheduleMinutes', {scheduleId: scheduleId})
      .catch( error => {
        console.error( error.message || error );
        return Observable.throw( error.message || error );
      } );
  }

  downloadAgendaAttachment(scheduleAgendaId) {
    return this.http.get('/mit-irb/downloadScheduleAgendaById', {
      headers: new HttpHeaders().set('scheduleAgendaId', scheduleAgendaId.toString()),
      responseType: 'blob'
    });
  }

  createMinutesForSchedule(params) {
    return this.http.post( '/mit-irb/createMinuteForSchedule', params );
  }

  downloadLatestMinutes(scheduleId) {
    return this.http.get('/mit-irb/downloadScheduleMinute', {
      headers: new HttpHeaders().set('scheduleId', scheduleId.toString()),
      responseType: 'blob'
    });
  }

  updateMinuteData(updatedata: Object): Observable<JSON> {
    return this.http.post( '/mit-irb/updateCommitteeScheduleMinute', updatedata )
    .catch( error => {
        console.error( error.message || error );
        return Observable.throw( error.message || error );
    } );
  }

  downloadMinuteAttachment(scheduleMinuteDocId) {
    return this.http.get('/mit-irb/downloadScheduleMinuteById', {
      headers: new HttpHeaders().set('scheduleMinuteDocId', scheduleMinuteDocId.toString()),
      responseType: 'blob'
    });
  }
}
