import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable()
export class IrbCreateService {
  formData = new FormData();

  constructor(private _http: HttpClient) { }

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

  saveScienceOfProtocol(params) {
    return this._http.post('/mit-irb/saveScienceOfProtocol', params);
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
  addCollaboratorAttachments(protocolCollaboratorAttachments: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(protocolCollaboratorAttachments));
    return this._http.post('/mit-irb/addCollaboratorAttachments', this.formData);
  }
  loadCollaboratorPersonsAndAttachments(params) {
    return this._http.post('/mit-irb/loadCollaboratorPersonsAndAttachments', params);
  }
  addCollaboratorPersons(params) {
    return this._http.post('/mit-irb/addCollaboratorPersons', params);
  }
  loadSponsorTypes(params) {
    return this._http.post('/mit-irb/loadSponsorTypes', params);
  }
  getApplicableQuestionnaire(params) {
    // return this._http.post('/mit-irb/getApplicableQuestionnaire', params);
    return this._http.post('/mit-irb/getApplicableQuestionnaire', params);
  }
  loadHomeUnits(unitSearchString: string) {
    const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
    return this._http.post('/mit-irb/loadHomeUnits', params);
  }
}
