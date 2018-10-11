import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Http, HttpModule } from '@angular/http';
import { Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';

import { HttpClient } from "@angular/common/http";
@Injectable()
export class ScheduleService {
    constructor( private http: HttpClient ) {

    }
    
    public loadScheduleData( scheduleId: number ){
        var params = {
                scheduleId:scheduleId
        };
            return this.http.post( '/mit-irb/loadScheduleById', params )
                .catch( error => {
                    console.error( error.message || error );
                    return Observable.throw( error.message || error )
                } );
    }
}
