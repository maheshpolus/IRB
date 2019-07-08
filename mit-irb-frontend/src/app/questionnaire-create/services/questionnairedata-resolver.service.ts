import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot , Router  } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class QuestionnaireDataResolverService implements Resolve<any> {
    constructor(private _http: HttpClient) {}
    requestObject = {
        'questionnaire_id': null
    };
    resolve(route: ActivatedRouteSnapshot) {
        if (route.queryParams.id) {
            this.requestObject.questionnaire_id = route.queryParams.id;
            return this._http.post( '/irb/editQuestionnaire', this.requestObject);
        } else {
            return this._http.get('/irb/createQuestionnaire');
        }
    }
}
