import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';

@Injectable()
export class DashboardService {

  constructor(private _http: HttpClient) { }

  getSnapshots(params) {
      return this._http.post('/irb/getDashboardSnapshot', params);
  }
  getIrbList(params) {
      return this._http.post('/irb/getDashboardProtocolList', params);
  }
  getProtocolType() {
      return this._http.post('/irb/getDashboardProtocolType', '');
  }
  getExemptProtocols(params) {
    return this._http.post('/irb/getPersonExemptFormList', params);
  }
  loadCommitteeList() {
    return this._http.post('/irb/loadCommitteeList', null);
}
loadCommitteeScheduleList() {
    return this._http.post('/irb/loadCommitteeScheduleList', null);
}
checkingPersonsRightToViewProtocol(params) {
    return this._http.post('/irb/checkingPersonsRightToViewProtocol', params);
  }
  getProtocolStatusList(params) {
    return this._http.post('/irb/getDashboardProtocolStatus', params);
  }
  getDashboardProtocolSubmissionStatus(params) {
    return this._http.post('/irb/getDashboardProtocolSubmissionStatus', params);
  }
  generateCorrespondence(exemptFormId, personID) {
      const jsonObject = {
        'commonVo': null,
        'exemptFormId': exemptFormId,
        'personId': personID
      };
      return this._http.get('/irb/generateCorrespondence', {
          headers: new HttpHeaders().set('jsonObject', JSON.stringify(jsonObject)),
          responseType: 'blob'
      });
    }
    getIRBAdminList(params) {
        return this._http.post('/irb/getIRBAdminList', params);
      }
}
