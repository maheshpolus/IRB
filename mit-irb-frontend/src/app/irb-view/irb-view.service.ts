import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class IrbViewService {

  constructor(private _http: HttpClient) { }

  getIrbHeaderDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolDetails', params);
  }
  getIrbPersonalDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolPersons', params);
  }
  getIrbFundingDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolFundingSource', params);
  }
  getIrbCollaboratorsDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolLocation', params);
  }
  getIrbSubjectsDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolVulnerableSubject', params);
  }
  getIrbSpecialReviewDetails(params) {
      return this._http.post('/mit-irb/getIRBprotocolSpecialReview', params);
  }
  getIrbPersonDetailedList(params) {
      return this._http.post('/mit-irb/getMITKCPersonInfo', params);
  }
  getIrbAttachmentList(params) {
      return this._http.post('/mit-irb/getAttachmentList', params);
  }
  downloadIrbAttachment(attachmentId) {
      return this._http.get('/mit-irb/downloadAttachment', {
          headers: new HttpHeaders().set('attachmentId', attachmentId.toString()),
          responseType: 'blob'
      });
  }
  getProtocolHistotyGroupList(params) {
      return this._http.post('/mit-irb/getProtocolHistotyGroupList', params);
  }
  getProtocolHistotyGroupDetails(params) {
      return this._http.post('/mit-irb/getProtocolHistotyGroupDetails', params);
  }
  loadProtocolHistoryCorrespondanceLetter(actionId) {
      return this._http.get( '/mit-irb/loadProtocolHistoryCorrespondanceLetter', {
          headers: new HttpHeaders().set('protocolActionId', actionId.toString()),
          responseType: 'blob'
      } );
  }
  loadProtocolHistoryActionComments(params) {
    return this._http.post('/mit-irb/loadProtocolHistoryActionComments', params);
}
loadProtocolHistoryGroupComments(params) {
    return this._http.post('/mit-irb/loadProtocolHistoryGroupComments', params);
}
getIRBprotocolUnits(params) {
    return this._http.post('/mit-irb/getIRBprotocolUnits', params);
}
getIRBprotocolAdminContact(params) {
    return this._http.post('/mit-irb/getIRBprotocolAdminContact', params);
}
getIRBprotocolCollaboratorDetails(params) {
    return this._http.post('/mit-irb/getIRBprotocolCollaboratorDetails', params);
}
downloadCollaboratorFileData(attachmentId) {
    return this._http.get('/mit-irb/downloadCollaboratorFileData', {
        headers: new HttpHeaders().set('fileDataId', attachmentId.toString()),
        responseType: 'blob'
    });
}
downloadTrainingAttachment(fileId) {
    return this._http.get('/mit-irb/downloadFileData', {
        headers: new HttpHeaders().set('fileDataId', fileId.toString()),
        responseType: 'blob'
    });
}
getProtocolSubmissionDetails(params) {
    return this._http.post('/mit-irb/getProtocolSubmissionDetails', params);
}

}
