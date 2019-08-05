import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { NgxSpinnerService } from 'ngx-spinner';

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
  sequenceNumber = null;
  applicableQuestionnaire = [];
  isEditMode = false;
  isQstnrEditable = true;
  selectedQuestionnaire = null;
  private $subscription1: ISubscription;
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService, private _router: Router, private _spinner: NgxSpinnerService) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolNumber = params['protocolNumber'];
      this.protocolId = params['protocolId'];
      this.sequenceNumber = params['sequenceNumber'];
       const qnrId = params['qnrId'];
       const completed =  params['completed'];
      if (completed != null) {
        this.getQuetionnaireList(qnrId);
        this._router.navigate([], {
             queryParams: {
             completed: null
             },
             queryParamsHandling: 'merge',
           });
      }
    });
    const path = this._router.parseUrl(this._router.url).root.children['primary'].segments[1].path;
    if (path === 'irb-view') {
      this.isEditMode = false;
    } else {
      this.isEditMode = true;
    }
    this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.isQstnrEditable = commonVo.protocolRenewalDetails != null ? commonVo.protocolRenewalDetails.questionnaire : true;
      }
    });
    this.getQuetionnaireList(null);
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  getQuetionnaireList(qnrId) {
    let moduleSubItemCodeList: any = [];
    if (this.protocolNumber.includes('A')) {
      moduleSubItemCodeList = [0, 4];
    } else if (this.protocolNumber.includes('R')) {
      moduleSubItemCodeList = [0, 3];
    } else {
      moduleSubItemCodeList = [0];
    }
    const requestObject = { 'module_item_key': this.protocolNumber,
                            'module_sub_item_key': this.sequenceNumber,
                            'moduleSubItemCodeList': moduleSubItemCodeList,
                            'module_item_code': 7 };
    this._irbCreateService.getApplicableQuestionnaire(requestObject).subscribe(
      data => {
        this._spinner.show();
        const result: any = data;
        this._spinner.hide();
        this.applicableQuestionnaire = result.applicableQuestionnaire != null ? result.applicableQuestionnaire : [];
        if (this.applicableQuestionnaire.length > 0 &&  qnrId == null) {
          this.openEachQuestionaire(this.applicableQuestionnaire[0]);
        } else if (this.applicableQuestionnaire.length > 0 &&  qnrId != null) {
          const selectedQuestionnaire =
            this.applicableQuestionnaire.filter(questionnaire => questionnaire.QUESTIONNAIRE_ID.toString() === qnrId.toString());
          if (selectedQuestionnaire.length > 0) {
            this.openEachQuestionaire(selectedQuestionnaire[0]);
          }
        }
      });
  }


  openEachQuestionaire(questionnaire) {
    this.selectedQuestionnaire = questionnaire;
    let mode = 'view';
    if (!this.isEditMode) { // View mode url
      mode = 'view';
      this._router.navigate(['/irb/irb-view/irbQuestionnaireList/irbQuestionnaireView'], {
        queryParams: {
          protocolId: this.protocolId,
          sequenceNumber: this.sequenceNumber,
          ansHdrId: questionnaire.QUESTIONNAIRE_ANS_HEADER_ID,
          subItemCode: questionnaire.MODULE_SUB_ITEM_CODE,
          qnrId: questionnaire.QUESTIONNAIRE_ID,
          mode: mode
        },
        queryParamsHandling: 'merge',
      });
    } else if (this.isQstnrEditable && questionnaire.MODULE_SUB_ITEM_CODE === 0 ||
                questionnaire.MODULE_SUB_ITEM_CODE === 4 && this.protocolNumber.includes('A') ||
                questionnaire.MODULE_SUB_ITEM_CODE === 3 && this.protocolNumber.includes('R')) { // Edit mode url (open in edit mode)
                  mode = null;
                  this._router.navigate(['/irb/irb-create/irbQuestionnaireList/irbQuestionnaireView'], {
                    queryParams: {
                      protocolId: this.protocolId,
                      sequenceNumber: this.sequenceNumber,
                      ansHdrId: questionnaire.QUESTIONNAIRE_ANS_HEADER_ID,
                      subItemCode: questionnaire.MODULE_SUB_ITEM_CODE,
                      qnrId: questionnaire.QUESTIONNAIRE_ID,
                      mode: mode
                    },
                    queryParamsHandling: 'merge',
                  });

    } else {// Edit mode url (open in View mode)
      mode = 'view';
      this._router.navigate(['/irb/irb-create/irbQuestionnaireList/irbQuestionnaireView'], {
        queryParams: {
          protocolId: this.protocolId,
          sequenceNumber: this.sequenceNumber,
          ansHdrId: questionnaire.QUESTIONNAIRE_ANS_HEADER_ID,
          subItemCode: questionnaire.MODULE_SUB_ITEM_CODE,
          qnrId: questionnaire.QUESTIONNAIRE_ID,
          mode: mode
        },
        queryParamsHandling: 'merge',
      });
    }
  }
}
