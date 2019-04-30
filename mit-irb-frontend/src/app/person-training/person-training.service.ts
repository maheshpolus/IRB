import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';

@Injectable()
export class PersonTrainingService {
  formData = new FormData();

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
  getPersonTrainingInfo(params) {
    return this._http.post('/mit-irb/getPersonTrainingInfo', params);
  }
  downloadTrainingAttachment(fileId) {
    return this._http.get('/mit-irb/downloadFileData', {
        headers: new HttpHeaders().set('fileDataId', fileId.toString()),
        responseType: 'blob'
    });
}
addTrainingComments(params) {
  return this._http.post('/mit-irb/addTrainingComments', params);
  }

  addTrainingAttachments(personnelTrainingAttachments: Object, uploadedFile, fileDataId: string) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    this.formData.delete('fileDataId');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(personnelTrainingAttachments));
    this.formData.append('fileDataId', fileDataId);
    return this._http.post('/mit-irb/addTrainingAttachments', this.formData);
  }
  getUserTrainingRight(personID: string) {
    const params = new HttpParams().set('person_Id', personID);
    return this._http.post('/mit-irb/getUserTrainingRight', params);
  }

}
