import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {  Subject } from 'rxjs/Subject';

@Injectable()
export class CreateQuestionnaireService {

  constructor(private http: HttpClient) { }

  addQuestionEvent = new Subject();
  updateTree  = new Subject();
  updateSelectedQuestionId = new Subject();
  getQuestionnaireList() {
    return this.http.post('/irb/showAllQuestionnaire', {});
  }
  saveQuestionnaireList(data) {
     return this.http.post('/irb/configureQuestionnaire', data);
  }
}
