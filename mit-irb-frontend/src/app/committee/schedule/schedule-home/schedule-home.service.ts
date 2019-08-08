import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ScheduleHomeService {

  constructor( private http: HttpClient ) { }

  saveScheduleData( scheduleObj: Object ) {
      return this.http.post( '/irb/updateSchedule', scheduleObj )
          .catch( error => {
              console.error( error.message || error );
              return Observable.throw( error.message || error );
          } );
  }

  loadScheduledProtocols( params ) {
    return this.http.post( '/irb/loadScheduledProtocols', params )
    .catch( error => {
        console.error( error.message || error );
        return Observable.throw( error.message || error );
    } );
  }
}
