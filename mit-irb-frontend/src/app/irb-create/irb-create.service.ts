import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class IrbCreateService {
  formData = new FormData();

  constructor(private router: Router, private _http: HttpClient) { }
  getEditDetails(params) {
    return this._http.post('/mit-irb/createIRBProtocol', params);
  }

  updateProtocolGeneralInfo(params) {
    return this._http.post('/mit-irb/updateProtocolGeneralInfo', params);
  }

  updateProtocolPersonInfo(params) {
    return this._http.post('/mit-irb/updateProtocolPersonInfo', params);
  }
  getIrbAttachmentList(params) {
    return this._http.post('/mit-irb/loadIRBProtocolAttachmentsByProtocolNumber', params);
  }

  addattachment(irbAttachmentProtocol: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(irbAttachmentProtocol));
    return this._http.post('/mit-irb/addProtocolAttachments', this.formData);
  }
  getAttachmentTypes(params) {
    return this._http.post('/mit-irb/loadAttachmentType', params);
  }

  updateFundingSource(params) {
    return this._http.post('/mit-irb/updateFundingSource', params);
  }
  updateSubject(params) {
    return this._http.post('/mit-irb/updateSubject', params);
  }
  updateCollaborator(params) {
    return this._http.post('/mit-irb/updateCollaborator', params);
  }
}
