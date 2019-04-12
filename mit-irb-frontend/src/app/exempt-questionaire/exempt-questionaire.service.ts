import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable()
export class ExemptQuestionaireService {
formData = new FormData();
constructor(private _http: HttpClient) { }

  getQuestionaire( params ) {
      return this._http.post('/mit-irb/savePersonExemptForm', params);
  }
  saveQuestionaire( params ) {
      return this._http.post('/mit-irb/saveQuestionnaire', params);
  }
  getSavedQuestionaire( params ) {
      return this._http.post('/mit-irb/getPersonExemptForm', params);
  }
  getPIUnit() {
    return this._http.post('/mit-irb/getLeadunitAutoCompleteList', '');
  }
  evaluatedQuestionaire( params ) {
    return this._http.post('/mit-irb/getEvaluateMessage', params);
  }
  addExemptProtocolChecklist(exemptForm: Object, uploadedFile) {
    this.formData.delete( 'files' );
        this.formData.delete( 'formDataJson' );
        for ( let i = 0; i < uploadedFile.length; i++ ) {
            this.formData.append( 'files', uploadedFile[i] );
        }
        this.formData.append( 'formDataJson', JSON.stringify( exemptForm ) );
    return this._http.post('/mit-irb/addExemptProtocolAttachments', this.formData);
  }

showExemptProtocolChecklist(params) {
    return this._http.post('/mit-irb/getExemptProtocolAttachmentList', params);
}

downloadExemptProtocolChecklist(checklistId) {
    return this._http.get('/mit-irb/downloadExemptProtocolAttachments', {
        headers: new HttpHeaders().set('checkListId', checklistId.toString()),
        responseType: 'blob'
    });
}

  getActivityLogByExemptFormID( params ) {
    return this._http.post('/mit-irb/getExemptProtocolActivityLogs', params);
  }
  approveOrDisapproveAction(params) {
    return this._http.post('/mit-irb/approveOrDisapproveExemptProtocols', params);
  }
  generateCorrespondence(exemptFormId, personID) {
      const jsonObject = {
          'commonVo': null,
          'exemptFormId': exemptFormId,
          'personId': personID
        };
      return this._http.get('/mit-irb/generateCorrespondence', {
          headers: new HttpHeaders().set('jsonObject', JSON.stringify(jsonObject)),
          responseType: 'blob'
      });
    }
    loadHomeUnits(unitSearchString: string) {
        const params = new HttpParams().set('homeUnitSearchString', unitSearchString);
        return this._http.post('/mit-irb/loadHomeUnits', params);
      }
}
