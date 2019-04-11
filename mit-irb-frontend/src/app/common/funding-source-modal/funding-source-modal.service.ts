import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class FundingSourceModalService {

  constructor(private _http: HttpClient) { }

  loadFundingSourceDetails(exemptId) {
    const params = new HttpParams().set('exemptId', exemptId);
    return this._http.post('/mit-irb/getExemptProtocolFundingSource', params);

  }
  loadSponsorTypes(params) {
    return this._http.post('/mit-irb/loadSponsorTypes', params);
  }
  loadHomeUnits(unitSearchString: string) {
    const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
    return this._http.post('/mit-irb/loadHomeUnitsIRB', params);
  }
  updateExemptFundingSource(params) {
    return this._http.post('/mit-irb/updateExemptFundingSource', params);
  }

}
