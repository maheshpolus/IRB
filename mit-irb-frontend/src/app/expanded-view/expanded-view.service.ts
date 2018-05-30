import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable()
export class ExpandedViewService {

  constructor(private _http: HttpClient) { }
 
  getExpandedList(params) {
      return this._http.post('/mit-irb/getExpandedSnapShotView', params);
  }
}
