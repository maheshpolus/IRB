import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable()
export class IrbViewService {

  constructor(private _http: HttpClient) { }

  getIrbHeaderDetails(params) {
      return this._http.post('/getDashboardSnapshot', params);
  }
}
