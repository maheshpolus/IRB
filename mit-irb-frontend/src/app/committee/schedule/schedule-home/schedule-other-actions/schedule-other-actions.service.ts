import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ScheduleOtherActionsService {
    constructor( private http: HttpClient ) {

    }

    public addOtherActions( committeeId, scheduleId: number, committeeScheduleActItems: Object ): Observable<JSON> {
        const params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            committeeScheduleActItems: committeeScheduleActItems
        };
        return this.http.post( '/irb/addOtherActions', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    public deleteOtherActions( committeeId, scheduleId: number, commScheduleActItemsId ): Observable<JSON> {
        const params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            commScheduleActItemsId: commScheduleActItemsId
        };
        return this.http.post( '/irb/deleteOtherActions', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }
}
