import { Injectable } from '@angular/core';
import { Resolve, ActivatedRoute  } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { LoginService } from '../../login/login.service';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/delay';

@Injectable()
export class DashboardResolverService implements Resolve<Observable<any>> {
  constructor(private _http: HttpClient) {}

  resolve() {
    //  return 'aisnaiosmpamsoam';
     return this._http.get('getUserDetails').catch(() => {
        return Observable.of({
          'personID': '',
          'firstName': '',
          'lastName': '',
          'fullName': '',
          'email': '',
          'roleNumber': null,
          'userName': '',
          'createNo': 0,
          'hasDual': null,
          'unitNumber': '',
          'unitName': '',
          'phoneNumber': null,
          'userRoleType': '',
          'role': ''
        });
     });
  }
}
