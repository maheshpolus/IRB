import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';



@Injectable()
export class LoginService {

  constructor( private router: Router, private _http: HttpClient ) { }

  login(params) {
    return this._http.post('/mit-irb/loginCheck', params);
    }
  
  login1(params) {
      return this._http.post('loginCheck', params);
      }

  logout() {
      this.router.navigate( ['/login'] );
  }

}
