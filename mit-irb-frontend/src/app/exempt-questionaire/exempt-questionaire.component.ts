import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute} from '@angular/router';

import { ExemptQuestionaireService } from './exempt-questionaire.service';

@Component({
  selector: 'app-exempt-questionaire',
  templateUrl: './exempt-questionaire.component.html',
  styleUrls: ['./exempt-questionaire.component.css'],
  providers: [ExemptQuestionaireService]

})
export class ExemptQuestionaireComponent implements OnInit {

  showQuestionaire = false;
  showAlert = false;
  QuestionnaireCompletionFlag = 'Y';

  questionaire: any = [];
  mode: string;
  result: any;
  lastGROUP_NAME = null;
  requestObject = {
          exemptTitle: '',
          updateUser: '',
          personId: '',
          exemptQuestionnaireAnswerHeaderId: '',
          personName: '',
          exemptFormID: ''
  };
  saveQuestionnaireReqObject: any = {
    irbExemptForm: {},
    questionnaireDto: {},
    personDTO: {},
    questionnaireInfobean : {
      answerlist: [],
      QuestionnaireAnswerHeaderId: '',
      QuestionnaireCompletionFlag: '',
      isSubmit: ''
    }
  };
  alertMsg: string;
  userDTO: any = {};
  isViewMode: any;

  constructor( private _exemptQuestionaireService: ExemptQuestionaireService, private _activatedRoute: ActivatedRoute) { }

  ngOnInit() {
      this.userDTO = this._activatedRoute.snapshot.data['irb'];
      this.requestObject.updateUser = this.userDTO.userName;
      this.requestObject.personId = this.userDTO.personID;
      this.requestObject.personName = this.userDTO.fullName;
      /** check for view mode  and edit mode
       * @ isViemode = true -> viewmode
       * @ isViemode = false -> editmode
       */
      // this.mode = this._activatedRoute.snapshot.queryParamMap.get('mode');
      this.requestObject.exemptTitle = this._activatedRoute.snapshot.queryParamMap.get('title');
      this.requestObject.exemptFormID = this._activatedRoute.snapshot.queryParamMap.get('exemptFormID');
      this.requestObject.exemptQuestionnaireAnswerHeaderId = this._activatedRoute.snapshot.queryParamMap.get('exempHeaderId');
      this.isViewMode = this._activatedRoute.snapshot.queryParamMap.get('mode');
      if (this.isViewMode) {
        this.showQuestionaire = true;
        this.loadSavedQuestionaire();
      }
      if ( this.isViewMode == null || this.isViewMode === undefined || this.isViewMode === '1') {
        this.isViewMode = false;
      } else {
          this.isViewMode = true;
      }
  }

  loadQuestionaire() {
      this.showAlert = false;
      if ( this.requestObject.exemptTitle !== '') {
          this.showQuestionaire = true;
          this._exemptQuestionaireService.getQuestionaire( this.requestObject ).subscribe(
                  data => {
                      this.result = data || [];
                      if ( this.result != null ) {
                          this.questionaire = this.result.questionnaireDto;
                          this.saveQuestionnaireReqObject.irbExemptForm =  this.result.irbExemptForm;
                      }
                  },
                  error => {
                      console.log( 'Error in getQuestionaire', error );
                  },
          );
      } else {
          this.showAlert = true;
          this.alertMsg = 'Please provide Exempt title!';
      }
  }

  loadSavedQuestionaire() {
    this._exemptQuestionaireService.getSavedQuestionaire( this.requestObject ).subscribe(
      data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.questionaire = this.result.questionnaireDto;
              this.saveQuestionnaireReqObject.irbExemptForm =  this.result.irbExemptForm;
              this.updateExemptQuestionnaire();
          }
      },
      error => {
          console.log( 'Error in getSavedQuestionaire', error );
      },
    );
  }

  showChildQuestions(currentQuestion) {
    if (currentQuestion.acType == null) {
       currentQuestion.acType = 'I';
    } else if (currentQuestion.acType === 'D') {
       currentQuestion.acType = 'U';
    }
    if (currentQuestion.selectedAnswer == null || currentQuestion.selectedAnswer === undefined) {
       currentQuestion.selectedAnswer = null;
    }
    if (currentQuestion.hasCondition === 'Y') {
        this.questionaire.questionnaireConditions.forEach(condition => {
          switch (condition.CONDITION_TYPE) {
            case 'EQUALS':
            if (condition.QUESTION_ID === currentQuestion.questionId ) {
              this.questionaire.questionnaireQuestions.forEach(question => {
                if (condition.GROUP_NAME === question.groupName && condition.CONDITION_VALUE === currentQuestion.selectedAnswer) {
                  question.showQuestion = true;
                  if (question.acType == null) {
                    question.acType = 'I';
                  } else if (currentQuestion.acType === 'D') {
                    currentQuestion.acType = 'U';
                  }
                  this.lastGROUP_NAME = condition.GROUP_NAME;
                } else if (condition.GROUP_NAME === question.groupName &&
                    condition.CONDITION_VALUE !== currentQuestion.selectedAnswer) {
                    question.showQuestion = false;
                    question.selectedAnswer = null;
                    if (question.acType === 'U') {
                      question.acType = 'D';
                    } else if (question.acType === 'I') {
                      question.acType = null;
                    }
                    if (question.hasCondition === 'Y') {
                      this.hideChildQuestion(question);
                    }
                }
            });
          }
        }
      });
    }
  }

  hideChildQuestion(currentQuestion) {
      this.questionaire.questionnaireConditions.forEach(condition => {
        if (condition.QUESTION_ID === currentQuestion.questionId) {
          const childQuestion = condition;
            if ( childQuestion !== undefined) {
              this.questionaire.questionnaireQuestions.forEach(question => {
                if (childQuestion.GROUP_NAME === question.groupName) {
                  question.showQuestion = false;
                  question.selectedAnswer = null;
                  if (question.acType === 'U') {
                    question.acType = 'D';
                  } else if (question.acType === 'I') {
                    question.acType = null;
                  }
                  if (question.hasCondition === 'Y') {
                    this.hideChildQuestion(question);
                  }
                }
              });
            }
        }
      });
  }

  checkQuestionaireCompletion() {
    this.questionaire.questionnaireQuestions.forEach(question => {
      if (question.selectedAnswer == null && question.showQuestion === true) {
        this.QuestionnaireCompletionFlag = 'N';
      }
    });
  }

  saveQuestionaire() {
    this.showAlert = true;
    this.QuestionnaireCompletionFlag = 'Y';
    this.saveQuestionnaireReqObject.irbExemptForm.exemptTitle = this.requestObject.exemptTitle;
    this.saveQuestionnaireReqObject.questionnaireInfobean = {};
    this.alertMsg = '';
    this.result = {};
    this.checkQuestionaireCompletion();
    this.setCommonReqObject();
    this.saveQuestionnaireReqObject.questionnaireInfobean.isSubmit = 'N';
    this.saveQuestionnaireReqObject.questionnaireInfobean = JSON.stringify(this.saveQuestionnaireReqObject.questionnaireInfobean);
    this.saveQuestionnaireReqObject.personDTO.personID = this.userDTO.personID;
    this.saveQuestionnaireReqObject.personDTO.fullName = this.userDTO.fullName;
    this._exemptQuestionaireService.saveQuestionaire(this.saveQuestionnaireReqObject).subscribe(
            data => {
                  this.result = data;
                  if ( this.result != null ) {
                    if ( this.QuestionnaireCompletionFlag === 'Y') {
                      this.alertMsg = 'Questionaire is complete, you can proceed to submit!';
                  } else {
                    this.alertMsg = 'Questionaire saved successfully!';
                  }
              }
            },
            error => {
                console.log( 'Error in saveQuestionaire', error );
                this.alertMsg = 'Failed to save questionaire';
            },
    );
  }

  submitQuestionaire() {
    this.QuestionnaireCompletionFlag = 'Y';
    this.showAlert = true;
    this.alertMsg = '';
    this.checkQuestionaireCompletion();
    if ( this.QuestionnaireCompletionFlag === 'Y') {
      this.setCommonReqObject();
      this.saveQuestionnaireReqObject.questionnaireInfobean.isSubmit = 'Y';
      this.saveQuestionnaireReqObject.questionnaireInfobean = JSON.stringify(this.saveQuestionnaireReqObject.questionnaireInfobean);
      this.saveQuestionnaireReqObject.personDTO.personID = this.userDTO.personID;
      this.saveQuestionnaireReqObject.personDTO.fullName = this.userDTO.fullName;
      this._exemptQuestionaireService.saveQuestionaire(this.saveQuestionnaireReqObject).subscribe(
        data => {
              this.result = data;
              if ( this.result != null ) {
                  this.alertMsg = this.result;
                  this.isViewMode = true;
          }
        },
        error => {
            console.log( 'Error in submitQuestionaire', error );
            this.alertMsg = 'Failed to submit questionaire';
        },
      );
    } else {
      this.alertMsg = 'Cannot submit questionaire since it is not complete';
    }
  }

  setCommonReqObject() {
      this.saveQuestionnaireReqObject.questionnaireInfobean = {};
      this.saveQuestionnaireReqObject.questionnaireInfobean.QuestionnaireCompletionFlag = this.QuestionnaireCompletionFlag;
      this.saveQuestionnaireReqObject.questionnaireInfobean.answerlist = this.questionaire.questionnaireQuestions;
      if (this.questionaire.questionnaireQuestions != null || this.questionaire.questionnaireQuestions.length > 0) {
        this.saveQuestionnaireReqObject.questionnaireInfobean.QuestionnaireAnswerHeaderId =
                  this.questionaire.questionnaireQuestions[0].questionnaireAnswerHeaderId;
      }
  }

  updateExemptQuestionnaire() {
    this.questionaire.questionnaireQuestions.forEach(question => {
      if (question.selectedAnswer !== null) {
         question.showQuestion = true;
      }
    });
  }
}
