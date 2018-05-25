import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';



@Injectable()
export class LoginService {

  constructor( private router: Router, private _http: HttpClient) { }

  login(params) {
    return this._http.post('/mit-irb/loginCheck', params);
    }
  logout() {
      return this._http.get('/mit-irb/logout');
  }
  getUserDetail() {
    this._http.get('/mit-irb/getUserDetails');
  }

}
