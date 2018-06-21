import { Component, OnInit, Input, NgZone, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import { Router, ActivatedRoute} from '@angular/router';
import { CompleterService, CompleterData } from 'ng2-completer';

import { ExemptQuestionaireService } from './exempt-questionaire.service';
import { PiElasticService } from '../common/service/pi-elastic.service';

@Component({
  selector: 'app-exempt-questionaire',
  templateUrl: './exempt-questionaire.component.html',
  styleUrls: ['./exempt-questionaire.component.css'],
  providers: [ExemptQuestionaireService]

})
export class ExemptQuestionaireComponent implements OnInit, AfterViewInit {

  showQuestionaire = false;
  showAlert = true;
  QuestionnaireCompletionFlag = 'N';

  questionaire: any = [];
  units: any = [];
  unitInput: any = {
    UNIT_NUMBER: '',
    UNIT_NAME: ''
  };
  mode: string;
  result: any = {};
  helpMsg = '';
  lastGROUP_NAME = null;
  requestObject: any = {
    irbExemptForm: {
          exemptTitle: '',
          updateUser: '',
          personId: '',
          exemptQuestionnaireAnswerHeaderId: '',
          personName: '',
          exemptFormID: '',
          facultySponsorPersonId: '',
          unitNumber: '',
          unitName: ''
    },
    personDTO: {},
    questionnaireDto: {},
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
  isEditMode = false;
  isEvaluate = false;
  isChecked = false;
  errorQuestionId: any;
  /* elastic search variables */
  message = '';
  _results: Subject<Array<any>> = new Subject<Array<any>>();
  piSearchText: FormControl = new FormControl( '' );
  facultySearchText: FormControl = new FormControl( '' );
  IsElasticResultPI = false;
  IsElasticResultFaculty = false;
  piName: string;
  personId: string;
  elasticResultTab = false;
  protected dataService: CompleterData;

  constructor( private _exemptQuestionaireService: ExemptQuestionaireService, private _activatedRoute: ActivatedRoute,
    private _ngZone: NgZone, private _elasticsearchService: PiElasticService, private _completerService: CompleterService) { }

  ngOnInit() {
      this.userDTO = this._activatedRoute.snapshot.data['irb'];
      this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
      this.requestObject.irbExemptForm.personId = this.userDTO.personID;
      this.requestObject.irbExemptForm.personName = this.userDTO.fullName;
      this.requestObject.personDTO.personID = this.userDTO.personID;
      this.requestObject.personDTO.fullName = this.userDTO.fullName;
      this.requestObject.irbExemptForm.unitName = this.userDTO.unitName;
      this.requestObject.irbExemptForm.unitNumber = this.userDTO.unitNumber;
      /** check for view mode  and edit mode
       * @ isViemode = true -> viewmode
       * @ isViemode = false -> editmode
       */
      // this.mode = this._activatedRoute.snapshot.queryParamMap.get('mode');
      this.requestObject.irbExemptForm.exemptTitle = this._activatedRoute.snapshot.queryParamMap.get('title');
      this.requestObject.irbExemptForm.exemptFormID = this._activatedRoute.snapshot.queryParamMap.get('exemptFormID');
      this.requestObject.irbExemptForm.exemptQuestionnaireAnswerHeaderId = this._activatedRoute.snapshot.queryParamMap.get('exempHeaderId');
      this.isViewMode = this._activatedRoute.snapshot.queryParamMap.get('mode');
      if (this.isViewMode) {
        this.showQuestionaire = true;
        this.loadSavedQuestionaire();
      }
      if (this.isViewMode === '1') {
        this.isEditMode = true;
      }
      if ( this.isViewMode == null || this.isViewMode === undefined || this.isViewMode === '1') {
        this.isViewMode = false;
      } else {
          this.isViewMode = true;
      }
      this.getPiUnits();
  }

  /*logic for elastic search*/
  ngAfterViewInit() {
      this.piSearchText
          .valueChanges
          .map(( text: any ) => text ? text.trim() : '' )
          .do( searchString => searchString ? this.message = 'searching...' : this.message = '' )
          .debounceTime( 500 )
          .distinctUntilChanged()
          .switchMap( searchString => {
            return this.getElasticSearchResults(searchString);
          } )
          .catch( this.handleError )
          .subscribe( this._results );
      this.facultySearchText
      .valueChanges
      .map(( text: any ) => text ? text.trim() : '' )
      .do( searchString => searchString ? this.message = 'searching...' : this.message = '' )
      .debounceTime( 500 )
      .distinctUntilChanged()
      .switchMap( searchString => {
        return this.getElasticSearchResults(searchString);
      } )
      .catch( this.handleError )
      .subscribe( this._results );
  }
  getElasticSearchResults(searchString) {
  return new Promise<Array<String>>(( resolve, reject ) => {
    this._ngZone.runOutsideAngular(() => {
        let hits_source: Array<any> = [];
        let hits_highlight: Array<any> = [];
        const hits_out: Array<any> = [];
        const results: Array<any> = [];
        let personName: string;
        let test;
        this._elasticsearchService.irbSearch(searchString)
            .then(( searchResult ) => {
              this._ngZone.run(() => {
                hits_source = ( ( searchResult.hits || {} ).hits || [] )
                    .map(( hit ) => hit._source );
                hits_highlight = ( ( searchResult.hits || {} ).hits || [] )
                    .map(( hit ) => hit.highlight );

                hits_source.forEach(( elmnt, j ) => {
                    personName = hits_source[j].full_name;
                    test = hits_source[j];

                    if ( typeof ( hits_highlight[j].full_name ) !== 'undefined' ) {
                        personName = hits_highlight[j].full_name;
                    }
                    results.push( {
                        label: personName,
                        obj: test
                    } );
                } );
                if ( results.length > 0 ) {
                    this.message = '';
                } else {
                    if ( this.requestObject.irbExemptForm.personName && this.requestObject.irbExemptForm.personName.trim() ) {
                        this.message = 'nothing was found';
                    }
                }
                resolve( results );
            } );

            } )
            .catch(( error ) => {
                this._ngZone.run(() => {
                    reject( error );
                } );
            } );
    } );
  } );
 }

  handleError(): any {
    this.message = 'something went wrong';
  }

  getPiUnits() {
    this._exemptQuestionaireService.getPIUnit().subscribe(
      data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.units = this.result;
              this.dataService = this._completerService.local(this.units, 'UNIT_NAME', 'UNIT_NAME');
          }
      },
      error => {
          console.log( 'Error in getPiUnits', error );
      }
    );
  }

  unitChangeFunction( unitName ) { }

  onUnitSelect() {
    this.units.forEach(( value, index ) => {
      if ( value.UNIT_NAME === this.requestObject.irbExemptForm.unitName ) {
          this.requestObject.irbExemptForm.unitNumber = value.UNIT_NUMBER;
          this.requestObject.irbExemptForm.unitName = value.UNIT_NAME;
      }
    } );
  }

  selectedResult(result) {
    this.requestObject.irbExemptForm.personName = '';
    this.requestObject.irbExemptForm.personName = result.obj.full_name;
    this.requestObject.irbExemptForm.personId = result.obj.person_id;
    this.IsElasticResultPI = false;
  }

  selectedFacultyResult(result) {
    this.requestObject.irbExemptForm.facultySponsorPerson = '';
    this.requestObject.irbExemptForm.facultySponsorPerson = result.obj.full_name;
    this.requestObject.irbExemptForm.facultySponsorPersonId = result.obj.person_id;
    this.IsElasticResultFaculty = false;
  }

  loadQuestionaire() {
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
      this.showAlert = false;
      if ( (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
      (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null)) {
          this._exemptQuestionaireService.getQuestionaire( this.requestObject ).subscribe(
                  data => {
                      this.result = data || [];
                      if ( this.result != null ) {
                          this.questionaire = this.result.questionnaireDto;
                          this.requestObject.irbExemptForm =  this.result.irbExemptForm;
                      }
                  },
                  error => {
                      console.log( 'Error in getQuestionaire', error );
                  },
                  () => {
                    this.showAlert = true;
                    this.showQuestionaire = true;
                  }
          );
      } else {
          this.showAlert = true;
          this.alertMsg = 'Please provide the mandatory fields';
      }
  }

  loadSavedQuestionaire() {
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
    this._exemptQuestionaireService.getSavedQuestionaire( this.requestObject ).subscribe(
      data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.questionaire = this.result.questionnaireDto;
              this.requestObject.irbExemptForm = this.result.irbExemptForm;
              this.updateExemptQuestionnaire();
              this.QuestionnaireCompletionFlag = 'Y';
              this.checkQuestionaireCompletion();
          }
      },
      error => {
          console.log( 'Error in getSavedQuestionaire', error );
      },
    );
  }

  showChildQuestions(currentQuestion) {
    this.errorQuestionId = null;
    this.isEvaluate = false;
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
    this.QuestionnaireCompletionFlag = 'Y';
     this.checkQuestionaireCompletion();
     if (this.QuestionnaireCompletionFlag === 'Y') {
       this.saveQuestionaire();
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
      if (question.selectedAnswer === null && question.showQuestion === true) {
        this.QuestionnaireCompletionFlag = 'N';
      }
    });
  }

  saveQuestionaire() {
    this.isEvaluate = false;
    this.errorQuestionId = null;
    this.QuestionnaireCompletionFlag = 'Y';
    this.requestObject.questionnaireInfobean = {};
    this.alertMsg = '';
    this.result = {};
    this.checkQuestionaireCompletion();
    this.setCommonReqObject();
    this.requestObject.questionnaireInfobean.isSubmit = 'N';
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
    this.requestObject.personDTO.personID = this.userDTO.personID;
    this.requestObject.personDTO.fullName = this.userDTO.fullName;
    this._exemptQuestionaireService.saveQuestionaire(this.requestObject).subscribe(
      data => {
            this.result = data;
            if ( this.result != null ) {
              this.requestObject.irbExemptForm = this.result.irbExemptForm;
              if ( this.QuestionnaireCompletionFlag === 'Y') {
                this.alertMsg = 'Exempt questionnaire is complete.' +
                'Please click on Evaluate to evaluate your answers and to know the determination.' +
                'If you dont want to know determination please go ahead with submission by clicking Submit';
            } else {
              this.alertMsg = 'Questionaire saved successfully!';
            }
            if (this.result.hasOwnProperty('questionnaireDto')) {
              this.questionaire.questionnaireQuestions = this.result.questionnaireDto.questionnaireQuestions;
            }
        }
      },
      error => {
          console.log( 'Error in saveQuestionaire', error );
          this.alertMsg = 'Failed to save questionaire';
      },
      () => {
        this.openSaveModal();
        this.updateExemptQuestionnaire();
      }
    );
  }

  submitConfirmation() {
      this.isEvaluate = false;
      this.alertMsg = '';
      this.QuestionnaireCompletionFlag = 'Y';
      this.checkQuestionaireCompletion();
      if ( this.QuestionnaireCompletionFlag === 'Y') {
        this.alertMsg = 'As PI, I certify that the above information is complete and accurate to the best of my knowledge.' +
        'I understand that the determination of non-exempt status for the proposed research is based solely upon' +
        'the information provided in this questionnaire and I agree to complete a new questionnaire should any of these' +
        'fact change or be determined to be inaccurate or incomplete.';
      } else {
        this.alertMsg = 'Cannot submit questionaire since it is not complete';
    }
  }

  closeConfirmation() {
    this.isChecked = false;
  }

  submitQuestionaire() {
    this.isEvaluate = false;
    this.errorQuestionId = null;
    this.alertMsg = '';
      this.setCommonReqObject();
      this.requestObject.questionnaireInfobean.isSubmit = 'Y';
      this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
      this.requestObject.personDTO.personID = this.userDTO.personID;
      this.requestObject.personDTO.fullName = this.userDTO.fullName;
      this._exemptQuestionaireService.saveQuestionaire(this.requestObject).subscribe(
        data => {
              this.result = data;
              if ( this.result != null ) {
                  this.alertMsg = this.result.exemptMessage;
                  this.requestObject.irbExemptForm = this.result.irbExemptForm;
                  this.isViewMode = true;
          }
        },
        error => {
            console.log( 'Error in submitQuestionaire', error );
            this.alertMsg = 'Failed to submit questionaire';
        },
        () => {
          this.openSaveModal();
        }
      );
  }

  setCommonReqObject() {
      this.requestObject.questionnaireInfobean = {};
      this.requestObject.questionnaireInfobean.QuestionnaireCompletionFlag = this.QuestionnaireCompletionFlag;
      this.requestObject.questionnaireInfobean.answerlist = this.questionaire.questionnaireQuestions;
      if (this.questionaire.questionnaireQuestions != null || this.questionaire.questionnaireQuestions.length > 0) {
        this.requestObject.questionnaireInfobean.QuestionnaireAnswerHeaderId =
                  this.questionaire.questionnaireQuestions[0].questionnaireAnswerHeaderId;
      }
  }

  /**updates questionnaire
   * itratesover questions to make child questions true
   * no input params required
   */
  updateExemptQuestionnaire() {
    this.questionaire.questionnaireQuestions.forEach(question => {
      if (question.selectedAnswer !== null) {
         question.showQuestion = true;
      }
      if (question.selectedAnswer != null && question.hasCondition === 'Y') {
         this.questionaire.questionnaireConditions.forEach(condition => {
          if (condition.QUESTION_ID === question.questionId && condition.CONDITION_VALUE === question.selectedAnswer) {
            this.questionaire.questionnaireQuestions.forEach(childquestions => {
              if (childquestions.groupName  === condition.GROUP_NAME) {
                childquestions.showQuestion = true;
              }
            });
          }
        });
      }
    });
  }

  getHelpLink(helpMsg) {
      if (helpMsg == null) {
          this.helpMsg = 'No help message availabe!';
      } else {
          this.helpMsg = helpMsg;
      }
  }
  /**opening modal using hidden button
   * call this fn to trigeer modal open of save
  */
  openSaveModal() {
    this.showAlert = true;
    document.getElementById('openModalButton').click();
  }

  evaluateExemptQuestionnaire() {
    this.errorQuestionId = null;
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
    this._exemptQuestionaireService.evaluatedQuestionaire(this.requestObject).subscribe(
      data => {
            this.result = data;
            if (this.result.isExemptGranted !== 'Y') {
              this.alertMsg = this.result.exemptMessage + ' Please see the highlighted question to know the reason for not being exempt.';
              this.errorQuestionId =  this.result.questionId;
            } else {
              this.alertMsg = this.result.exemptMessage + ' You can proceed with submission.';
            }
      },
      error => {
          console.log( 'Error in saveQuestionaire', error );
          this.alertMsg = 'Failed to save questionaire';
       },
      () => {
        this.isEvaluate = true;
      }
  );
  }
}

