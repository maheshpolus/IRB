import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AdminDashboardService {

  constructor(private _http: HttpClient) { }

  getAdminDashBoardPermissions(params) {
    return this._http.post('/irb/getAdminDashBoardPermissions', params);
  }

}
