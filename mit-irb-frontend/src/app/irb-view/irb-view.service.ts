import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable()
export class IrbViewService {
  formData = new FormData();
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

getSubmissionLookups(params) {
    return this._http.post('/mit-irb/getSubmissionLookups', params);
}

updateIRBAdminReviewer(params) {
    return this._http.post('/mit-irb/updateIRBAdminReviewer', params);
}

getIRBAdminReviewers(params) {
    return this._http.post('/mit-irb/getIRBAdminReviewers', params);
}

getIRBAdminReviewDetails(params) {
    return this._http.post('/mit-irb/getIRBAdminReviewDetails', params);
}

getSubmissionBasicDetails(params) {
    return this._http.post('/mit-irb/getSubmissionBasicDetails', params);
}

updateIRBAdminComment(params) {
    return this._http.post('/mit-irb/updateIRBAdminComment', params);
}

updateIRBAdminAttachments(reqstObject: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(reqstObject));
    return this._http.post('/mit-irb/updateIRBAdminAttachments', this.formData);
  }

  getSubmissionHistory(params) {
    return this._http.post('/mit-irb/getSubmissionHistory', params);
}

updateIRBAdminCheckList(params) {
    return this._http.post('/mit-irb/updateIRBAdminCheckList', params);
}

downloadAdminRevAttachment(attachmentId) {
    return this._http.get('/mit-irb/downloadAdminRevAttachment', {
        headers: new HttpHeaders().set('attachmentId', attachmentId.toString()),
        responseType: 'blob'
    });
}

getCommitteeScheduledDates(committeeId) {
    const params = new HttpParams().set('committeeId', committeeId);
      return this._http.post('/mit-irb/getCommitteeScheduledDates', params);
  }

  updateBasicSubmissionDetail(params) {
    return this._http.post('/mit-irb/updateBasicSubmissionDetail', params);
  }
  loadCommitteeMembers(params) {
    return this._http.post('/mit-irb/loadCommitteeMembers', params);
  }
  updateCommitteeReviewers(params) {
        return this._http.post('/mit-irb/updateCommitteeReviewers', params);
  }
  updateCommitteeVotingDetail(params) {
    return this._http.post('/mit-irb/updateCommitteeVotingDetail', params);
  }
  updateCommitteeReviewerAttachments(reqstObject: Object, uploadedFile) {
    this.formData.delete('files');
    this.formData.delete('formDataJson');
    for (let i = 0; i < uploadedFile.length; i++) {
      this.formData.append('files', uploadedFile[i]);
    }
    this.formData.append('formDataJson', JSON.stringify(reqstObject));
    return this._http.post('/mit-irb/updateCommitteeReviewerAttachments', this.formData);
  }
loadCommitteeReviewerDetails(params) {
    return this._http.post('/mit-irb/loadCommitteeReviewerDetails', params);
}

}
