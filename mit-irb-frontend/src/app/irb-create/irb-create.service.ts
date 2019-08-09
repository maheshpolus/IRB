import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class IrbCreateService {
  formData = new FormData();
  showSectionComment = new Subject();

  constructor(private _http: HttpClient) { }

  getEditDetails(params) {
    return this._http.post('/irb/createIRBProtocol', params);
  }

  updateProtocolGeneralInfo(params) {
    return this._http.post('/irb/updateProtocolGeneralInfo', params);
  }

  updateProtocolPersonInfo(params) {
    return this._http.post('/irb/updateProtocolPersonInfo', params);
  }

  getIrbAttachmentList(params) {
    return this._http.post('/irb/loadIRBProtocolAttachments', params);
  }

  saveScienceOfProtocol(params) {
    return this._http.post('/irb/saveScienceOfProtocol', params);
  }

  addattachment(irbAttachmentProtocol: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(irbAttachmentProtocol));
    return this._http.post('/irb/addProtocolAttachments', this.formData);
  }

  getAttachmentTypes(params) {
    return this._http.post('/irb/loadAttachmentType', params);
  }

  updateFundingSource(params) {
    return this._http.post('/irb/updateFundingSource', params);
  }

  updateSubject(params) {
    return this._http.post('/irb/updateSubject', params);
  }

  updateCollaborator(params) {
    return this._http.post('/irb/updateCollaborator', params);
  }
  addCollaboratorAttachments(protocolCollaboratorAttachments: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(protocolCollaboratorAttachments));
    return this._http.post('/irb/addCollaboratorAttachments', this.formData);
  }
  loadCollaboratorPersonsAndAttachments(params) {
    return this._http.post('/irb/loadCollaboratorPersonsAndAttachments', params);
  }
  addCollaboratorPersons(params) {
    return this._http.post('/irb/addCollaboratorPersons', params);
  }
  loadSponsorTypes(params) {
    return this._http.post('/irb/loadSponsorTypes', params);
  }
  getApplicableQuestionnaire(params) {
    // return this._http.post('/irb/getApplicableQuestionnaire', params);
    return this._http.post('/irb/getApplicableQuestionnaire', params);
  }
  loadHomeUnits(unitSearchString: string) {
    const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
    return this._http.post('/irb/loadHomeUnits', params);
  }
  updateProtocolUnits(params) {
    return this._http.post('/irb/updateUnitDetails', params);
  }
  updateAdminContact(params) {
    return this._http.post('/irb/updateAdminContact', params);
  }
  getUserTrainingRight(personID: string) {
    const params = new HttpParams().set('person_Id', personID);
    return this._http.post('/irb/getUserTrainingRight', params);
  }
  downloadTrainingAttachment(fileId) {
    return this._http.get('/irb/downloadFileData', {
        headers: new HttpHeaders().set('fileDataId', fileId.toString()),
        responseType: 'blob'
    });
}

getAvailableActions(params) {
return this._http.post('/irb/getActionList', params);
}
performProtocolActions(iRBActionsVo: Object, uploadedFile) {
  this.formData.delete('files');
  this.formData.delete('formDataJson');
  for (let i = 0; i < uploadedFile.length; i++) {
    this.formData.append('files', uploadedFile[i]);
  }
  this.formData.append('formDataJson', JSON.stringify(iRBActionsVo));
  return this._http.post('/irb/performProtocolActions', this.formData);
}

getAmendRenwalSummary(param) {
  return this._http.post('/irb/getAmendRenwalSummary', param);
}

updateAmendRenwalSummary(param) {
  return this._http.post('/irb/updateAmendRenwalSummary', param);
}

getOrganizationList(searchString) {
  const params = new HttpParams().set('collaboratorSearchString', searchString);
    return this._http.post('/irb/loadCollaborators', params);
}
getActionLookup(param) {
  return this._http.post('/irb/getActionLookup', param);
}
getCommitteeScheduledDates(committeeId) {
  const params = new HttpParams().set('committeeId', committeeId);
    return this._http.post('/irb/getCommitteeScheduledDates', params);
}
loadInternalProtocolAttachments(params) {
  return this._http.post('/irb/loadInternalProtocolAttachments', params);
}
loadPreviousProtocolAttachments(documentId) {
  const params = new HttpParams().set('documentId', documentId);
    return this._http.post('/irb/loadPreviousProtocolAttachments', params);
}
downloadIrbAttachment(attachmentId) {
  return this._http.get('/irb/downloadAttachment', {
      headers: new HttpHeaders().set('attachmentId', attachmentId.toString()),
      responseType: 'blob'
  });
}
downloadInternalProtocolAttachments(attachmentId) {
  return this._http.get('/irb/downloadInternalProtocolAttachments', {
      headers: new HttpHeaders().set('documentId', attachmentId.toString()),
      responseType: 'blob'
  });
}
getProtocolPermissionDetails(param) {
    return this._http.post('/irb/getProtocolPermissionDetails', param);
  }
  updateProtocolPermission(param) {
    return this._http.post('/irb/updateProtocolPermission', param);
  }
  loadCollaboratorAttachmentType() {
    return this._http.get('/irb/loadCollaboratorAttachmentType');
  }
  saveOrUpdateInternalProtocolAttachments(internalAttachments: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(internalAttachments));
    return this._http.post('/irb/saveOrUpdateInternalProtocolAttachments', this.formData);
  }
  getIrbPersonDetailedList(params) {
    return this._http.post('/irb/getMITKCPersonInfo', params);
}
}
