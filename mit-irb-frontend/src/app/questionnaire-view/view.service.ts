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
  saveQuestionnaire(data, filesArray) {
    const formData = new FormData();
    if (filesArray.length > 0) {
      filesArray.forEach(file => {
        formData.append(file.questionId + '', file.attachment);
      });
    }
    formData.append('formDataJson', JSON.stringify(data));
     return this.http.post('mit-irb/saveQuestionnaireModule', formData);
  }

  downloadAttachment(attachmentId) {
    return this.http.post( 'mit-irb/downloadQuesAttachment',
    {'questionnaire_ans_attachment_id': attachmentId }, {responseType: 'blob'});
  }
}
