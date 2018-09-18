import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class IrbCreateService {

  constructor( private router: Router, private _http: HttpClient) { }
  getEditDetails(params) {
     return this._http.post('/mit-irb/createIRBProtocol', params);
  }

  updateProtocolGeneralInfo(params) {
    return this._http.post('/mit-irb/updateProtocolGeneralInfo', params);
 }

 updateProtocolPersonInfo(params) {
  return this._http.post('/mit-irb/updateProtocolPersonInfo', params);
}

}
