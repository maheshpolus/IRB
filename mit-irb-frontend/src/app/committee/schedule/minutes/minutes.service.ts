import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class MinutesService {
    constructor( private http: HttpClient ) {

    }

    saveMinuteData( minuteData: Object ): Observable<JSON> {
        return this.http.post( '/mit-irb/addCommitteeScheduleMinute', minuteData )
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

    updateMinuteData(updatedata: Object): Observable<JSON> {
        return this.http.post( '/mit-irb/updateCommitteeScheduleMinute', updatedata )
        .catch( error => {
            console.error( error.message || error );
            return Observable.throw( error.message || error );
        } );
    }
}
