import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class AssignModalServiceService {

  constructor(private _http: HttpClient) { }

  getIRBAdminList(params) {
    return this._http.post('/mit-irb/getIRBAdminList', params);
  }

  updateIRBAdmin(params) {
    return this._http.post('/mit-irb/updateIRBAdmin', params);
  }
}
