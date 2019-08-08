import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ScheduleOtherActionsService {
    constructor( private http: HttpClient ) {

    }
    getOtherActions(params) {
        return this.http.post( '/irb/loadMeetingOtherActions', params );
    }
    updateOtherActions(params) {
        return this.http.post( '/irb/updateMeetingOtherActions', params );
    }
}
