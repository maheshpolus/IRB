import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { HttpClient } from '@angular/common/http';
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
}
