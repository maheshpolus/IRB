import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class DashboardService {

  constructor(private _http: HttpClient) { }

  getSnapshots(params) {
      return this._http.post('getDashboardSnapshot', params);
  }
  getIrbList(params) {
      return this._http.post('getDashboardProtocolList', params);
  }
}
