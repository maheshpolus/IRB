import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
@Injectable()
export class ViewQuestionnaireService {

  constructor(private http: HttpClient) { }

  getjson() {
    return this.http.get('../../assets/dummy2.json');
  }
  getQuestionnaire(data) {
    return this.http.post('/mit-irb/getQuestionnaire', data );
  }
  saveQuestionnaire(data) {
     return this.http.post('mit-irb/saveQuestionnaireModule', data);
  }
}
