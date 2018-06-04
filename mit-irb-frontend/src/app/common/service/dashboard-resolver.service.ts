import { Injectable } from '@angular/core';
import { Resolve, ActivatedRoute, Router  } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/delay';

@Injectable()
export class DashboardResolverService implements Resolve<any> {
  constructor(private _http: HttpClient ) {}

  resolve() {
    return this._http.get('/mit-irb/getUserDetails');
    }
}
