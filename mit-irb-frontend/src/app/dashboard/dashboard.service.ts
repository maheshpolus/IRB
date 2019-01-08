import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';

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
checkingPersonsRightToViewProtocol(params) {
    return this._http.post('/mit-irb/checkingPersonsRightToViewProtocol', params);
  }
  getProtocolStatusList(params) {
    return this._http.post('/mit-irb/getDashboardProtocolStatus', params);
  }
  generateCorrespondence(exemptFormId, personID) {
      const jsonObject = {
        'commonVo': null,
        'exemptFormId': exemptFormId,
        'personId': personID
      };
      return this._http.get('/mit-irb/generateCorrespondence', {
          headers: new HttpHeaders().set('jsonObject', JSON.stringify(jsonObject)),
          responseType: 'blob'
      });
    }
}
