import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class FundingSourceModalService {

  constructor(private _http: HttpClient) { }

  loadFundingSourceDetails(exemptId) {
    const params = new HttpParams().set('exemptId', exemptId);
    return this._http.post('/irb/getExemptProtocolFundingSource', params);

  }
  loadSponsorTypes(params) {
    return this._http.post('/irb/loadSponsorTypes', params);
  }
  loadHomeUnits(unitSearchString: string) {
    const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
    return this._http.post('/irb/loadHomeUnitsIRB', params);
  }
  updateExemptFundingSource(params) {
    return this._http.post('/irb/updateExemptFundingSource', params);
  }

}
