import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';

import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-questionnaire-list',
  templateUrl: './irb-questionnaire-list.component.html',
  styleUrls: ['./irb-questionnaire-list.component.css'],
  providers: [IrbCreateService]
})
export class IrbQuestionnaireListComponent implements OnInit, OnDestroy {
  protocolNumber = null;
  protocolId = null;
  applicableQuestionnaire = [];
  isQstnrEditable = true;
  private $subscription1: ISubscription;
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService, private _router: Router) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolNumber = params['protocolNumber'];
      this.protocolId = params['protocolId'];
    });
    const path = this._router.parseUrl(this._router.url).root.children['primary'].segments[1].path;
    if (path === 'irb-view') {
      this.isQstnrEditable = false;
    }
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
    let moduleSubItemCodeList: any = [];
    if (this.protocolNumber.includes('A')) {
      moduleSubItemCodeList = [2, 4];
    } else if (this.protocolNumber.includes('R')) {
      moduleSubItemCodeList = [2, 3];
    } else {
      moduleSubItemCodeList = [2];
    }
    const requestObject = { 'module_item_key': this.protocolNumber, 'moduleSubItemCodeList': moduleSubItemCodeList,
                            'module_item_code': 7, 'module_sub_item_code': 0 };
    this._irbCreateService.getApplicableQuestionnaire(requestObject).subscribe(
      data => {
        const result: any = data;
        this.applicableQuestionnaire = result.applicableQuestionnaire != null ? result.applicableQuestionnaire : [];
      });
  }
}
