import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ExemptQuestionaireService {

constructor(private _http: HttpClient) { }

  getQuestionaire( params ) {
      return this._http.post('/mit-irb/savePersonExemptForm', params);
  }
  saveQuestionaire( params ) {
      return this._http.post('/mit-irb/saveQuestionnaire', params);
  }
  getSavedQuestionaire( params ) {
      return this._http.post('/mit-irb/getPersonExemptForm', params);
  }
  getPIUnit() {
    return this._http.post('/mit-irb/getLeadunitAutoCompleteList', '');
  }
  evaluatedQuestionaire( params ) {
    return this._http.post('/mit-irb/getEvaluateMessage', params);
  }
}
