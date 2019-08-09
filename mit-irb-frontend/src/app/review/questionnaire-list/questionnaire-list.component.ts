import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { IrbCreateService } from '../../irb-create/irb-create.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-review-questionnaire-list',
  templateUrl: './questionnaire-list.component.html',
  styleUrls: ['./questionnaire-list.component.css'],
  providers: [IrbCreateService]
})
export class ReviewQuestionnaireListComponent implements OnInit, OnDestroy {

  protocolNumber = null;
  protocolId = null;
  applicableQuestionnaire = [];
  isQstnrEditable = true;
  isShowQuestionnaire = false;
  private $subscription1: ISubscription;
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService, private _router: Router) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolNumber = params['protocolNumber'];
      this.protocolId = params['protocolId'];
      this.isShowQuestionnaire = (params['mode'] === 'view') || false;
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
      moduleSubItemCodeList = [0, 4];
    } else if (this.protocolNumber.includes('R')) {
      moduleSubItemCodeList = [0, 3];
    } else {
      moduleSubItemCodeList = [0];
    }
    const requestObject = { 'module_item_key': this.protocolNumber, 'moduleSubItemCodeList': moduleSubItemCodeList,
                            'module_item_code': 7, 'module_sub_item_code': 0 };
    this._irbCreateService.getApplicableQuestionnaire(requestObject).subscribe(
      data => {
        const result: any = data;
        this.applicableQuestionnaire = result.applicableQuestionnaire != null ? result.applicableQuestionnaire : [];
      });
  }
  addRouteParams(questionnaire) {
    this._router.navigate( [],
      {
        relativeTo: this._activatedRoute,
        queryParams: { protocolNumber: this.protocolNumber, protocolId: this.protocolId,
          ansHdrId: questionnaire.QUESTIONNAIRE_ANS_HEADER_ID,
          qnrId: questionnaire.QUESTIONNAIRE_ID, mode: 'view'},
        queryParamsHandling: 'merge',
      });
  }
 }
