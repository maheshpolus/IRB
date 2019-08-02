import { Component, OnInit, ChangeDetectionStrategy, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as _ from 'lodash';
// import _.forEach from 'lodash/forEach';
// import _.filter from 'lodash/filter';
import { CreateQuestionnaireService } from '../services/create.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ISubscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-create-main',
  templateUrl: './create-main.component.html',
  styleUrls: ['./create-main.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateMainComponent implements OnInit, OnDestroy {

  constructor(private _activatedRoute: ActivatedRoute,
              private _createQuestionnaireService: CreateQuestionnaireService,
              private _spinner: NgxSpinnerService,
              private _changeRef: ChangeDetectorRef) { }
  data: any = {};
  toast_message = 'Questionnaire Saved Sucessfully';
  QuestionnaireCommonValues: any = {
    lastQuestionId  : 1,
    lastGroupName   : 1,
    lastConditionId : 1,
    isQuestionEdited: false
  };
  currentTab = 'create';
  nodes: any = {
    nodes : []
  };
  errorList = [];
  groupLabels = {};
  isSaving = false;
  isViewmode = false;
  $goToQuestion: ISubscription;
  /**
   * takes the data from the resolver output,
   * updates the lastGroupName with max value to the current questionnaire group number;
   */
  ngOnInit() {
    this.data = this._activatedRoute.snapshot.data['Questionnaire'];
    this.QuestionnaireCommonValues.lastGroupName = this.data.questionnaire.maxGroupNumber + 1 || 1;
    this._spinner.hide();
    this.goToQuestion();
  }
  ngOnDestroy() {
    this.$goToQuestion.unsubscribe();
  }
  changeCurrentTab(selectedTab) {
    this.currentTab = selectedTab;
  }
  validateQuestionniare() {
    let isQuestionnaireValid = true;
    this.errorList.forEach((error: any) => {
      document.getElementById('tree' + error).classList.remove('highlight-error-node');
    });
    this.errorList = [];
    _.forEach(this.data.questionnaire.questions, (question) => {
       if ( question.QUESTION === '' || question.QUESTION == null ) {
         this.errorList.push(question.QUESTION_ID);
       }
    });
    _.forEach(this.data.questionnaire.options, (option) => {
      if ( option.OPTION_LABEL === '' || option.OPTION_LABEL == null ) {
        this.errorList.push(option.QUESTION_ID);
      }
    });
    _.forEach(this.data.questionnaire.conditions, (condition) => {
      if ( condition.CONDITION_VALUE === '' || condition.CONDITION_VALUE == null ) {
        this.errorList.push(condition.QUESTION_ID);
      }
    });
    if (this.errorList.length) {
      isQuestionnaireValid = false;
      this.errorList.forEach((error: any) => {
        document.getElementById('tree' + error).classList.add('highlight-error-node');
      });
    } else {
      this.saveQuestionniare();
    }
  }
  confirmSave() {
    if (!this.isSaving) {
      this.data.question_editted = this.QuestionnaireCommonValues.isQuestionEdited;
      if (this.data.header.IS_FINAL === true) {
        document.getElementById('saveQuestionniare').click();
      } else {
        this.saveQuestionniare();
      }
    }
  }
  /**
   * saves the questionnaire
   */
  saveQuestionniare() {
    if (!this.isSaving) {
      this.isSaving = true;
      document.getElementById('app-spinner').style.display = 'block';
      this.updateGroupLabel();
      this._createQuestionnaireService.saveQuestionnaireList(this.data).subscribe(
        data => {
          this.data = data;
          this._changeRef.markForCheck();
          const toastId = document.getElementById('toast-success');
          this.showToast(toastId);
          const  el = document.getElementById('app-spinner');
            if (el) {
             el.style.display = 'none';
            }
          this.isSaving = false;
        }, err => this.isSaving = false );
    }
  }
  /**
   * updates the group label of questionnaire with user updated values or group name itself
   */
  updateGroupLabel() {
    _.forEach(this.data.questionnaire.questions, (question) => {
      question.GROUP_LABEL = this.groupLabels[question.GROUP_NAME];
    });
  }

  /**
   * @param  {} toastId
   * enables a toast for sucessful save
   */
  showToast(toastId) {
    toastId.className = 'show';
    setTimeout(function () {
       toastId.className = toastId.className.replace('show', '');
    }, 2000);
  }
  updateGroupname() {
    _.forEach( this.data.questionnaire.questions , (question, key) => {
      this.groupLabels[question.GROUP_NAME] = question.GROUP_LABEL ||  question.GROUP_NAME;
    });
  }

  addNewQuestion(groupName) {
    this._createQuestionnaireService.addQuestionEvent.next(groupName);
  }
  goToQuestion() {
    this.$goToQuestion = this._createQuestionnaireService.updateSelectedQuestionId
       .subscribe( data => {
         if (this.currentTab !== 'create') {
           this.currentTab = 'create';
           setTimeout(() => {
             this._createQuestionnaireService.updateSelectedQuestionId.next(data);
           }, 500);
         }
       });
   }


}
