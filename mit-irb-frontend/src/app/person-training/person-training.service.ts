import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class PersonTrainingService {

  constructor(private _http: HttpClient) { }

  loadPersonTrainingList(params) {
    return this._http.post('/mit-irb/loadPersonTrainingList', params);
  }
  loadTrainingList(trainingName: string) {
    const params = new HttpParams().set('trainingName', trainingName);
    return this._http.post('/mit-irb/loadTrainingList', params);
  }
  updatePersonTraining(params) {
    return this._http.post('/mit-irb/updatePersonTraining', params);
  }
}
