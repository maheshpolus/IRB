import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ExemptQuestionaireService {
  formData = new FormData();
constructor(private _http: HttpClient) { }

  getQuestionaire( params ) {
    debugger
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
  addExemptProtocolChecklist(exemptForm: Object,uploadedFile){
    this.formData.delete( 'files' ); 
        this.formData.delete( 'formDataJson' );
        for ( var i = 0; i < uploadedFile.length; i++ ) {
            this.formData.append( 'files', uploadedFile[i] );
        }
        this.formData.append( 'formDataJson', JSON.stringify( exemptForm ) );
    return this._http.post('/mit-irb/addExemptProtocolAttachments', this.formData);
  }

showExemptProtocolChecklist(params){
    return this._http.post('/mit-irb/getExemptProtocolAttachmentList', params);
}

downloadExemptProtocolChecklist(checklistId){ 
    return this._http.get('/mit-irb/downloadExemptProtocolAttachments', { 
        headers: new HttpHeaders().set('checkListId', checklistId.toString()),
        responseType:'blob'
    });
}
  //stk
  getActivityLogByExemptFormID( params ) {
    return this._http.post('/mit-irb/getExemptProtocolActivityLogs', params);
  }
  approveOrDisapproveAction(params)
  {
    return this._http.post('/mit-irb/approveOrDisapproveExemptProtocols', params);
  }
}
