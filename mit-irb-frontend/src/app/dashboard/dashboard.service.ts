import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class DashboardService {

  constructor(private _http: HttpClient) { }

  getSnapshots(params) {
      return this._http.post('/mit-irb/getDashboardSnapshot', params);
  }
  getIrbList(params) {
      return this._http.post('/mit-irb/getDashboardProtocolList', params);
  }
  getProtocolType() {
      return this._http.post('/mit-irb/getDashboardProtocolType', '');
  }
  getExemptProtocols(params) {
    return this._http.post('/mit-irb/getPersonExemptFormList', params);
  }
  loadCommitteeList() {
    return this._http.post('/mit-irb/loadCommitteeList', null);
}
loadCommitteeScheduleList() {
    return this._http.post('/mit-irb/loadCommitteeScheduleList', null);
}
}
