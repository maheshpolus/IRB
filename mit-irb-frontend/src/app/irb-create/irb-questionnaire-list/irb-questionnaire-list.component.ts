import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';

import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-questionnaire-list',
  templateUrl: './irb-questionnaire-list.component.html',
  styleUrls: ['./irb-questionnaire-list.component.css']
})
export class IrbQuestionnaireListComponent implements OnInit, OnDestroy {
  protocolNumber = null;
  protocolId = null;
  applicableQuestionnaire = [];
  isQstnrEditable = true;
  private $subscription1: ISubscription;
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolNumber = params['protocolNumber'];
      this.protocolId = params['protocolId'];
    });
    this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.isQstnrEditable = commonVo.protocolRenewalDetails != null ? commonVo.protocolRenewalDetails.questionnaire : true;
      }
    });
    this.getQuetionnaireList();
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  getQuetionnaireList() {
    const requestObject = { 'module_item_key': this.protocolNumber, 'module_sub_item_key': 0,
                            'module_item_code': 7, 'module_sub_item_code': 0 };
    this._irbCreateService.getApplicableQuestionnaire(requestObject).subscribe(
      data => {
        const result: any = data;
        this.applicableQuestionnaire = result.applicableQuestionnaire != null ? result.applicableQuestionnaire : [];
      });
  }
}
