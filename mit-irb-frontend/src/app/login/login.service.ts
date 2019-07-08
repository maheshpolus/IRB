import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class LoginService {

  constructor( private router: Router, private _http: HttpClient) { }

  login(params) {
      return this._http.post('/irb/loginCheck', params);
    }
  logout() {
      return this._http.get('/irb/logout');
  }
  getUserDetail(param) {
      return this._http.post('/irb/getUserDetails', param);
  }
}

