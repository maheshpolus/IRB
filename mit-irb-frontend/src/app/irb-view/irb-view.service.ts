import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable()
export class IrbViewService {

  constructor(private _http: HttpClient) { }

  getIrbHeaderDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolDetails', params);
  }
  getIrbPersonalDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolPersons', params);
  }
  getIrbFundingDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolFundingSource', params);
  }
  getIrbCollaboratorsDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolLocation', params);
  }
  getIrbSubjectsDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolVulnerableSubject', params);
  }
  getIrbSpecialReviewDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolLocation', params);
  }
  getIrbPersonDetailedList(params) {
      return this._http.post('/mit-irb/getMITKCPersonInfo', params);
  }
}
