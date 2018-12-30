import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()

export class HeaderService {
    urlParams: any = {};
    constructor(private _http: HttpClient) {}

    getConfiguredWidgetList(params) {
        this.urlParams.personWidget = params;
        return this._http.post('updatePersonWidget', this.urlParams);
    }

    getLoginList( ) {
        return this._http.get('loginDetails' );
    }

    getWidgetList() {
        return this._http.get('getWidgetList');
    }
    sessionExpired() {
        return this._http.get('SessionInvalidate');
    }
}
