import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable( )

export class SearchService {

    constructor(private _http: HttpClient) { }

    getTableProperties() {
        return this._http.get('/mit-irb/getCodeTableMetaData');
    }
}
