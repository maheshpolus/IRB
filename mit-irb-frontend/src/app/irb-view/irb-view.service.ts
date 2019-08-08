import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable()
export class IrbViewService {
  formData = new FormData();
  constructor(private _http: HttpClient) { }

  getIrbHeaderDetails(params) {
      return this._http.post('/irb/getIRBprotocolDetails', params);
  }
  getIrbPersonalDetails(params) {
      return this._http.post('/irb/getIRBprotocolPersons', params);
  }
  getIrbFundingDetails(params) {
      return this._http.post('/irb/getIRBprotocolFundingSource', params);
  }
  getIrbCollaboratorsDetails(params) {
      return this._http.post('/irb/getIRBprotocolLocation', params);
  }
  getIrbSubjectsDetails(params) {
      return this._http.post('/irb/getIRBprotocolVulnerableSubject', params);
  }
  getIrbSpecialReviewDetails(params) {
      return this._http.post('/irb/getIRBprotocolSpecialReview', params);
  }
  getIrbPersonDetailedList(params) {
      return this._http.post('/irb/getMITKCPersonInfo', params);
  }
//   getIrbAttachmentList(params) {
//       return this._http.post('/irb/getAttachmentList', params);
//   }
  downloadIrbAttachment(attachmentId) {
      return this._http.get('/irb/downloadAttachment', {
          headers: new HttpHeaders().set('attachmentId', attachmentId.toString()),
          responseType: 'blob'
      });
  }
  getProtocolHistotyGroupList(params) {
      return this._http.post('/irb/getProtocolHistotyGroupList', params);
  }
  getProtocolHistotyGroupDetails(params) {
      return this._http.post('/irb/getProtocolHistotyGroupDetails', params);
  }
  loadProtocolHistoryCorrespondanceLetter(actionId) {
      return this._http.get( '/irb/loadProtocolHistoryCorrespondanceLetter', {
          headers: new HttpHeaders().set('protocolActionId', actionId.toString()),
          responseType: 'blob'
      } );
  }
  loadProtocolHistoryActionComments(params) {
    return this._http.post('/irb/loadProtocolHistoryActionComments', params);
}
loadProtocolHistoryGroupComments(params) {
    return this._http.post('/irb/loadProtocolHistoryGroupComments', params);
}
getIRBprotocolUnits(params) {
    return this._http.post('/irb/getIRBprotocolUnits', params);
}
getIRBprotocolAdminContact(params) {
    return this._http.post('/irb/getIRBprotocolAdminContact', params);
}
getIRBprotocolCollaboratorDetails(params) {
    return this._http.post('/irb/getIRBprotocolCollaboratorDetails', params);
}
downloadCollaboratorFileData(attachmentId) {
    return this._http.get('/irb/downloadCollaboratorFileData', {
        headers: new HttpHeaders().set('fileDataId', attachmentId.toString()),
        responseType: 'blob'
    });
}
downloadTrainingAttachment(fileId) {
    return this._http.get('/irb/downloadFileData', {
        headers: new HttpHeaders().set('fileDataId', fileId.toString()),
        responseType: 'blob'
    });
}
getProtocolSubmissionDetails(params) {
    return this._http.post('/irb/getProtocolSubmissionDetails', params);
}

getSubmissionLookups(params) {
    return this._http.post('/irb/getSubmissionLookups', params);
}

updateIRBAdminReviewer(params) {
    return this._http.post('/irb/updateIRBAdminReviewer', params);
}

getIRBAdminReviewers(params) {
    return this._http.post('/irb/getIRBAdminReviewers', params);
}

getIRBAdminReviewDetails(params) {
    return this._http.post('/irb/getIRBAdminReviewDetails', params);
}

getSubmissionBasicDetails(params) {
    return this._http.post('/irb/getSubmissionBasicDetails', params);
}

updateIRBAdminComment(params) {
    return this._http.post('/irb/updateIRBAdminComment', params);
}

updateIRBAdminAttachments(reqstObject: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(reqstObject));
    return this._http.post('/irb/updateIRBAdminAttachments', this.formData);
  }

  getSubmissionHistory(params) {
    return this._http.post('/irb/getSubmissionHistory', params);
}

updateIRBAdminCheckList(params) {
    return this._http.post('/irb/updateIRBAdminCheckList', params);
}

downloadAdminRevAttachment(attachmentId) {
    return this._http.get('/irb/downloadAdminRevAttachment', {
        headers: new HttpHeaders().set('attachmentId', attachmentId.toString()),
        responseType: 'blob'
    });
}

getCommitteeScheduledDates(committeeId) {
    const params = new HttpParams().set('committeeId', committeeId);
      return this._http.post('/irb/getCommitteeScheduledDates', params);
  }

  updateBasicSubmissionDetail(params) {
    return this._http.post('/irb/updateBasicSubmissionDetail', params);
  }
  loadCommitteeMembers(params) {
    return this._http.post('/irb/loadCommitteeMembers', params);
  }
  updateCommitteeReviewers(params) {
        return this._http.post('/irb/updateCommitteeReviewers', params);
  }
  updateCommitteeVotingDetail(params) {
    return this._http.post('/irb/updateCommitteeVotingDetail', params);
  }
  updateCommitteeReviewerAttachments(reqstObject: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(reqstObject));
    return this._http.post('/irb/updateCommitteeReviewerAttachments', this.formData);
  }
loadCommitteeReviewerDetails(params) {
    return this._http.post('/irb/loadCommitteeReviewerDetails', params);
}

getPastSubmission(params) {
    return this._http.post('/irb/getPastSubmission', params);
}
updateCommitteeReviewerComments(params) {
    return this._http.post('/irb/updateCommitteeReviewerComments', params);
}
downloadCommitteeFileData(attachmentId) {
    return this._http.get('/irb/downloadCommitteeFileData', {
        headers: new HttpHeaders().set('fileDataId', attachmentId.toString()),
        responseType: 'blob'
    });
}
getIrbAttachmentList(params) {
  return this._http.post('/irb/loadIRBProtocolAttachments', params);
}
loadInternalProtocolAttachments(params) {
    return this._http.post('/irb/loadInternalProtocolAttachments', params);
  }
  loadPreviousProtocolAttachments(documentId) {
    const params = new HttpParams().set('documentId', documentId);
      return this._http.post('/irb/loadPreviousProtocolAttachments', params);
  }
  downloadInternalProtocolAttachments(attachmentId) {
    return this._http.get('/irb/downloadInternalProtocolAttachments', {
        headers: new HttpHeaders().set('documentId', attachmentId.toString()),
        responseType: 'blob'
    });
}
getIRBprotocolScienificData(params) {
    return this._http.post('/irb/getIRBprotocolScienificData', params);
  }

checkSubmissionLock(params) {
    return this._http.post('/irb/checkSubmissionLock', params);
  }
}
