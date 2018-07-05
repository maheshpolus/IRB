import { Component, OnInit, Input, NgZone, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import { ActivatedRoute} from '@angular/router';
import { HttpClient } from '@angular/common/http';


import { ExemptQuestionaireService } from './exempt-questionaire.service';
import { PiElasticService } from '../common/service/pi-elastic.service';
import { NgxSpinnerService } from "ngx-spinner";

@Component({
  selector: 'app-exempt-questionaire',
  templateUrl: './exempt-questionaire.component.html',
  styleUrls: ['./exempt-questionaire.component.css'],
  providers: [ExemptQuestionaireService]

})
export class ExemptQuestionaireComponent implements OnInit, AfterViewInit {

  showQuestionaire = false;
  showAlert = true;
  showWelcomeMessage = true;
  QuestionnaireCompletionFlag = 'N';
  showHelpMsg: any = [];
  questionaire: any = [];
  units: any = [];
  unitName: any = [];
  result: any = {};
  helpMsg: any = [];
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
          unitName: '',
          summary: ''
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
  errorQuestions: any = [];
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

  WELCOME_MESSAGE: string;
  ABOUT_MESSAGE: string;
  EXEMPT_MSG: string;
  NOT_EXEMPT_MSG: string;
  PI_DECLARATION: string;
  OTHER_MSG: string;
  isExempt: string;

  constructor( private _exemptQuestionaireService: ExemptQuestionaireService, private _activatedRoute: ActivatedRoute,
    private _ngZone: NgZone, private _elasticsearchService: PiElasticService, private _http: HttpClient,private _spinner: NgxSpinnerService,) {}

  /** sets requestObject and checks for mode */
  ngOnInit() {
    this._http.get("/mit-irb/resources/string_config_json").subscribe(
      data => {
           const property_config: any = data;
           if (property_config) {
               this.WELCOME_MESSAGE = property_config.WELCOME_MSG;
               this.ABOUT_MESSAGE = property_config.ABOUT_QUESTIONNAIRE_MSG;
               this.EXEMPT_MSG = property_config.EXEMPT_MSG;
               this.NOT_EXEMPT_MSG = property_config.NOT_EXEMPT_MSG;
               this.PI_DECLARATION = property_config.PI_DECLARATION;
               this.OTHER_MSG =property_config.OTHER_MSG;
          }
       }
   );
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

  /** logic for elastic search*/
  ngAfterViewInit() {
    /** elastic search of PI */
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

    /** elastic search of Faculty sponsor */
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
      console.log()
    /** show welcome message on create mode */
      if (this.isViewMode == false && this.isEditMode == false) {
        this.openWelcomeModal();
      }
  }

/**fetches elastic search results
 * @param searchString - string enterd in the input field
 */
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
  /**show message if elastic search failed */
  handleError(): any {
    this.message = 'something went wrong';
  }

  /** calls service to fetch Units of PI */
  getPiUnits() {
    this._exemptQuestionaireService.getPIUnit().subscribe(
      data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.units = this.result;
              this.units.forEach(( value, index ) => {
                    this.unitName[index] = value.UNIT_NAME;
              } );
              //this.dataService = this._completerService.local(this.units, 'UNIT_NAME', 'UNIT_NAME');
          }
      },
      error => {
          console.log( 'Error in getPiUnits', error );
      }
    );
  }

  unitChangeFunction( unitName ) { }

  /** assign values to requestObject on selecting a particular unit from suggestion box */
  onUnitSelect() {
    this.units.forEach(( value, index ) => {
      if ( value.UNIT_NAME === this.requestObject.irbExemptForm.unitName ) {
          this.requestObject.irbExemptForm.unitNumber = value.UNIT_NUMBER;
          this.requestObject.irbExemptForm.unitName = value.UNIT_NAME;
      }
    } );
  }

 /** assign values to requestObject on selecting a particular PI from elastic search */
  selectedPiResult(result) {
    this.requestObject.irbExemptForm.personName = '';
    this.requestObject.irbExemptForm.personName = result.obj.full_name;
    this.requestObject.irbExemptForm.personId = result.obj.person_id;
    this.IsElasticResultPI = false;
  }

 /** assign values to requestObject on selecting a particular Faculty from elastic search */
  selectedFacultyResult(result) {
    this.requestObject.irbExemptForm.facultySponsorPerson = '';
    this.requestObject.irbExemptForm.facultySponsorPerson = result.obj.full_name;
    this.requestObject.irbExemptForm.facultySponsorPersonId = result.obj.person_id;
    this.IsElasticResultFaculty = false;
  }

  /** function to load exempt questionaire once the title and pi and exempt is created */
  loadQuestionaire() {
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
      this.showAlert = false;
      if ( (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
      (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
      (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== '')) {
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

  /** function to load exempt questionaire which was saved once
   * loaded in edit or view mode only
  */
  loadSavedQuestionaire() {
    this._spinner.show();
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
    this._exemptQuestionaireService.getSavedQuestionaire( this.requestObject ).subscribe(
      data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.questionaire = this.result.questionnaireDto;
              this.requestObject.irbExemptForm = this.result.irbExemptForm;
              if(this.requestObject.irbExemptForm.statusCode == '2') {
                  this.isViewMode = true;
              }
              this.updateExemptQuestionnaire();
              this.QuestionnaireCompletionFlag = 'Y';
              this.checkQuestionaireCompletion();
          }
      },
      error => {
          console.log( 'Error in getSavedQuestionaire', error );
      },
      ()=> {
          this._spinner.hide();
      }
    );
  }

  /** logic to show child questions once a question is answered */
  showChildQuestions(currentQuestion) {
    this.errorQuestions = [];
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
      let lastGROUP_NAME ='';
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
                  lastGROUP_NAME = condition.GROUP_NAME;
                } else if (condition.GROUP_NAME === question.groupName &&
                    condition.CONDITION_VALUE !== currentQuestion.selectedAnswer && lastGROUP_NAME != condition.GROUP_NAME) {
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

  /** logic to hide child questions once a question is skipped */
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

  /**checks whether the questionaire is complete and sets the flag */
  checkQuestionaireCompletion() {
    this.questionaire.questionnaireQuestions.forEach(question => {
      if (question.selectedAnswer === null && question.showQuestion === true) {
        this.QuestionnaireCompletionFlag = 'N';
      }
    });
  }

  /** service call to save the exempt questionaire */
  saveQuestionaire() {
    this.isEvaluate = false;
    this.errorQuestions = [];
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
    if ( (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
        (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
        (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== '')) {
        this._spinner.show();
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
        this._spinner.hide();
        this.openSaveModal();
        this.updateExemptQuestionnaire();
      }
    );
    } else {
        this.openSaveModal();
        this.alertMsg = 'Please provide the mandatory fields';
    }
  }

  /** calls service to evaluate exempt questioanire */
  evaluateExemptQuestionnaire() {
    this.errorQuestions = [];
    this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
    if ( (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
        (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
        (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== '')) {
        this._spinner.show();
        this._exemptQuestionaireService.evaluatedQuestionaire(this.requestObject).subscribe(
         data => {
            this.result = data;
            this.isExempt = this.result.isExemptGranted;
            if (this.result.isExemptGranted === 'N') {
              this.alertMsg = this.NOT_EXEMPT_MSG + ' Please see the highlighted question(s)' +
              'to know the reason for not being exempt.';
              this.errorQuestions =  this.result.exemptQuestionList;
            } else if(this.result.isExemptGranted === 'Y') {
              this.alertMsg = this.EXEMPT_MSG + ' You can proceed with submission.';
            } else if(this.result.isExemptGranted === 'O') {
              this.alertMsg = this.OTHER_MSG;
            }
      },
      error => {
          console.log( 'Error in saveQuestionaire', error );
          this.alertMsg = 'Failed to save questionaire';
       },
      () => {
        this._spinner.hide();
        this.isEvaluate = true;
      }
  );
 } else {
     this.openSaveModal();
     this.alertMsg = 'Please provide the mandatory fields';
 }
}

  /** shows a popup to show the certify message before submitting */
  submitConfirmation() {
      this.isEvaluate = false;
      this.alertMsg = '';
      this.QuestionnaireCompletionFlag = 'Y';
      this.checkQuestionaireCompletion();
      if ( this.QuestionnaireCompletionFlag === 'Y') {
        this.alertMsg = this.PI_DECLARATION;
      } else {
        this.alertMsg = 'Cannot submit questionaire since it is not complete';
    }
  }

  /** sets the value of checkbox to false if the certify message is closed without agreeing*/
  closeConfirmation() {
    this.isChecked = false;
  }

  /** submit questionaire after completing and agreeing*/
  submitQuestionaire() {
    this.isEvaluate = false;
    this.errorQuestions = [];
    this.alertMsg = '';
      this.setCommonReqObject();
      this.requestObject.questionnaireInfobean.isSubmit = 'Y';
      this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
      this.requestObject.personDTO.personID = this.userDTO.personID;
      this.requestObject.personDTO.fullName = this.userDTO.fullName;
      if ( (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
      (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
      (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== '')) {
          this._spinner.show();
          this._exemptQuestionaireService.saveQuestionaire(this.requestObject).subscribe(
           data => {
              this.result = data;
              if ( this.result != null ) {
                  if (this.result.irbExemptForm.isExempt === 'N') {
                      this.alertMsg = this.NOT_EXEMPT_MSG;
                  } else if(this.result.irbExemptForm.isExempt === 'Y') {
                      this.alertMsg = this.EXEMPT_MSG;
                  } else if(this.result.irbExemptForm.isExempt === 'O') {
                      this.alertMsg = this.OTHER_MSG;
                  }
              this.requestObject.irbExemptForm = this.result.irbExemptForm;
              this.isViewMode = true;
             }
        },
        error => {
            console.log( 'Error in submitQuestionaire', error );
            this.alertMsg = 'Failed to submit questionaire';
        },
        () => {
          this._spinner.hide();
          this.openSaveModal();
        }
      );
      } else {
          this.openSaveModal();
          this.alertMsg = 'Please provide the mandatory fields';
      }
  }

  /** function which sets the requestObject values for both save and submit calls*/
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
   * iterates over questions to make child questions true
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

  /** assigns help link message of a question
   * sets no help message if help mesag is not available
   * @param helpMsg
   */
  getHelpLink(helpMsg, index) {
      this.showHelpMsg[index] = !this.showHelpMsg[index];
      if (helpMsg == null) {
          this.helpMsg[index] = 'No help message availabe!';
      } else {
          this.helpMsg[index] = helpMsg;
      }
  }

  /**opening modal using hidden button
   * call this fn to trigeer modal open of save
  */
  openSaveModal() {
    this.showAlert = true;
    document.getElementById('openModalButton').click();
  }

  /**opening modal using hidden button
   * call this fn to trigeer modal open to show welcome message
  */
 openWelcomeModal() {
  this.showWelcomeMessage = true;
  document.getElementById('openWelocomeModalButton').click();
  }

  /**evaluates error questions
   * @param - questionid
   * return true if questionid is in errorlist
   */
  showEvaluateMessageById(questionId) {
    let isError = false;
    this.errorQuestions.forEach(element => {
      if(element.QUESTION_ID == questionId) {
        isError = true;
      }
    });
    return isError;
  }

  /** close welcome message modal */
  closeWelcome() {
    this.showWelcomeMessage = false;
  }
}
