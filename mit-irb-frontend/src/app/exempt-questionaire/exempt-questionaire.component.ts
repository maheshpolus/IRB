import { Component, OnInit, NgZone, AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, ViewContainerRef } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import { ActivatedRoute, Router } from '@angular/router';
import {Location} from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { KeyPressEvent } from '../common/directives/keyPressEvent.component';



import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { ExemptQuestionaireService } from './exempt-questionaire.service';
import { PiElasticService } from '../common/service/pi-elastic.service';
import { SharedDataService } from '../common/service/shared-data.service';
declare var $: any;

@Component({
    selector: 'app-exempt-questionaire',
    templateUrl: './exempt-questionaire.component.html',
    styleUrls: ['./exempt-questionaire.component.css'],
    changeDetection: ChangeDetectionStrategy.Default,
    providers: [ExemptQuestionaireService]

})
export class ExemptQuestionaireComponent implements OnInit, AfterViewInit {
    readonly strInProgressStatusCode = '1';
    readonly strEnroutedToFacultyStatusCode = '2';
    readonly strEnroutedIrbOfficeStatusCode = '3';
    readonly strEnrouteToPiStatusCode = '5';
    readonly strSubmittedStatusCode = '4';

    readonly strCreatedActionType = '1';
    readonly strSubmitActionType = '2';
    readonly strApprovedFacultyActionType = '3';
    readonly strReturnedFacultyActionType = '4';
    readonly strReturnedIrbOfficeActionType = '5';
    readonly strApprovedIrbOfficeActionType = '6';
    readonly strReturnedByPIActionType = '7';
    readonly strApprovedByPIActionType = '8';

    todayDate = new Date();
    continueBtnClicked = false;
    showQuestionaire = false;
    showAlert = true;
    ApproveRejectHeader = '';
    isApprovedAlert = false;
    isQuestionaireSubmitted = false;
    isQuestionnaireContinueClicked = false;
    showMore = false;
    isActionByPi = false;
    showConfirmationDetails = true;
    isRejectedAlert = false;
    isReturnAlert = false;
    actionLogResult: any = [];
    ignoreWarning = false;
    showWarningAlert = true;
    showWelcomeMessage = true;
    QuestionnaireCompletionFlag = 'N';
    showHelpMsg: any = [];
    questionaire: any = [];
    units: any = [];
    unitName: any = [];
    result: any = {};
    helpMsg: any = [];
    lastGROUP_NAME = null;
    exemptAttachmentObject: any = [];
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
            summary: '',
            checkListDescription: '',
            checkListAcType: '',
            exemptProtocolStartDate: '',
            exemptProtocolEndDate: '',
            loggedInUserFacultySponsor: '',
            loggedInUserPI: '',
            comment: '',
            actionTypesCode: '',
            statusCode: '',
            notificationNumber: null
        },
        exemptProtocolStartDate: '',
        exemptProtocolEndDate: '',
        facultySponsorJobTitle: null,
        personJobTitle: null,
        personDTO: {},
        questionnaireDto: {},
        questionnaireInfobean: {
            answerlist: [],
            QuestionnaireAnswerHeaderId: '',
            QuestionnaireCompletionFlag: '',
            isSubmit: ''
        }
    };
    alertMsg: string;
    showEvaluateWarning: string;
    modalHeading: string;
    exemptQuestionList: any = [];
    userDTO: any = {};
    isViewMode: any;
    showApprovePopup = false;
    isPendingActionRequired = false;
    isEditMode = false;
    isEvaluate = false;
    isChecked = false;
    errorQuestions: any = [];

    /* elastic search variables */
    message = '';
    _results: Subject<Array<any>> = new Subject<Array<any>>();
    piSearchText: FormControl = new FormControl('');
    facultySearchText: FormControl = new FormControl('');
    IsElasticResultPI = false;
    IsElasticResultFaculty = false;
    piName: string;
    personId: string;
    elasticResultTab = false;

    NOFACULTY_WARNING_MESSAGE: string;
    WELCOME_MESSAGE: string;
    ABOUT_MESSAGE: string;
    EXEMPT_MSG: string;
    NOT_EXEMPT_MSG: string;
    PI_DECLARATION: string;
    PI_DECLARATION_COMPLETE: string;
    PI_DECLARATION_AGREE_POLICIES: string;
    PI_DECLARATION_MEMBER_RESPONSIBILITY: string;
    PI_DECLARATION_COMPREHENSIVE_REVIEW: string;
    PI_DECLARATION_INTRODUCTION: string;
    OTHER_MSG: string;
    NOT_EXEMPT_MSG_SUBMIT: string;
    EXEMPT_MSG_SUBMIT: string;
    NOFACULTY_MESSAGE: string;
    NOFACULTY_TITILE_MESSAGE: string;
    QUESTIONNAIRE_COMPLETE_MESSAGE: string;
    isExempt: string;

    /*checklist variables*/
    fil: FileList;
    files: UploadFile[] = [];
    uploadedFile: File[] = [];
    attachmentList: any[] = [];
    fileName: string;
    attachmentDescription: string;
    attachmentObject: any = {};
    showAttachment = true;
    editChecklist: any[] = [true];
    showAddAttachment = false;
    showPopup = false;
    tempSaveAttachment: any = {};
    tempEditObject: any = {};
    editScheduleattachment: any = {};
    attachments: any = {};
    isEmptyCheckListDescr = false;
    isDuplicate = false;
    disableButton = false;
    showContinueButton = false;
    invalidData = {
        invalidStartDate : false,
        invalidEndDate: false,
        invalidExemptData: false
    };
    piDeclaration = {
        isConfirmationChecked : false,
        isAgreePoliciesChecked : false,
        isMemberResponsibilityChecked : false,
        isComprehensiveReviewChecked : false
    };
    isStayOnSamePage = false;

    constructor(private _location: Location,
            private _exemptQuestionaireService: ExemptQuestionaireService, private _activatedRoute: ActivatedRoute,
        private _ngZone: NgZone, private _elasticsearchService: PiElasticService, private _http: HttpClient,
        private _spinner: NgxSpinnerService, public changeRef: ChangeDetectorRef,
        private _router: Router, private _sharedDataService: SharedDataService, public keyPressEvent: KeyPressEvent,
        public toastr: ToastsManager, vcr: ViewContainerRef) {
            this.todayDate.setDate(this.todayDate.getDate() - 1);
            this.toastr.setRootViewContainerRef(vcr);
        }

    /** sets requestObject and checks for mode */
    ngOnInit() {
    	   $(function () {
            $('[data-toggle="popover"]').popover();
            $('.popover-dismiss').popover({
                trigger: 'focus'
              });
          });
        this._http.get('/mit-irb/resources/string_config_json').subscribe(
            data => {
                const property_config: any = data;
                if (property_config) {
                    this.WELCOME_MESSAGE = property_config.WELCOME_MSG;
                    this.NOFACULTY_WARNING_MESSAGE = property_config.NOFACULTY_WARNING_MESSAGE;
                    this.ABOUT_MESSAGE = property_config.ABOUT_QUESTIONNAIRE_MSG;
                    this.EXEMPT_MSG = property_config.EXEMPT_MSG;
                    this.NOT_EXEMPT_MSG = property_config.NOT_EXEMPT_MSG;
                    this.PI_DECLARATION = property_config.PI_DECLARATION;
                    this.EXEMPT_MSG_SUBMIT = property_config.EXEMPT_MSG_SUBMIT;
                    this.NOT_EXEMPT_MSG_SUBMIT = property_config.NOT_EXEMPT_MSG_SUBMIT;
                    this.OTHER_MSG = property_config.OTHER_MSG;
                    this.NOFACULTY_MESSAGE = property_config.NOFACULTY_MESSAGE;
                    this.PI_DECLARATION_INTRODUCTION = property_config.PI_DECLARATION_INTRODUCTION;
                    this.NOFACULTY_TITILE_MESSAGE = property_config.NOFACULTY_TITILE_MESSAGE;
                    this.PI_DECLARATION_COMPLETE = property_config.PI_DECLARATION_COMPLETE;
                    this.PI_DECLARATION_AGREE_POLICIES = property_config.PI_DECLARATION_AGREE_POLICIES;
                    this.PI_DECLARATION_MEMBER_RESPONSIBILITY = property_config.PI_DECLARATION_MEMBER_RESPONSIBILITY;
                    this.PI_DECLARATION_COMPREHENSIVE_REVIEW = property_config.PI_DECLARATION_COMPREHENSIVE_REVIEW;
                    this.QUESTIONNAIRE_COMPLETE_MESSAGE = property_config.QUESTIONNAIRE_COMPLETE_MESSAGE;
                }
            }
        );
        this.userDTO = this._activatedRoute.snapshot.data['irb'];
        this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
        this.requestObject.irbExemptForm.personId = this.userDTO.personID;
        this.requestObject.irbExemptForm.personName = this.userDTO.fullName;
        this.requestObject.personDTO.personID = this.userDTO.personID;
        this.requestObject.personDTO.fullName = this.userDTO.fullName;
        this.requestObject.personDTO.role = this.userDTO.role;
        this.requestObject.personJobTitle = this.userDTO.jobTitle;
        this.requestObject.irbExemptForm.unitName = this.userDTO.unitName;
        this.requestObject.irbExemptForm.unitNumber = this.userDTO.unitNumber;
        this.requestObject.irbExemptForm.exemptTitle = this._activatedRoute.snapshot.queryParamMap.get('title');
        this.requestObject.irbExemptForm.exemptFormID = this._activatedRoute.snapshot.queryParamMap.get('exemptFormID');
        this.requestObject.irbExemptForm.exemptQuestionnaireAnswerHeaderId =
            this._activatedRoute.snapshot.queryParamMap.get('exempHeaderId');
        this.isViewMode = this._activatedRoute.snapshot.queryParamMap.get('mode');
        if (this.isViewMode) {
            this.showQuestionaire = true;
            this.loadSavedQuestionaire();
        }
        if (this.isViewMode === '5' && this.requestObject.personDTO.personID === this.requestObject.irbExemptForm.personId) {
            this.isActionByPi = true;
            this.continueBtnClicked = true;
        }
        if (this.isViewMode === '1' || this.isViewMode === '5') {
            this.isEditMode = true;
            this.continueBtnClicked = true;
        }
        if (this.isViewMode == null || this.isViewMode === undefined || this.isViewMode === '1' || this.isViewMode === '5') {
             if (this.isViewMode === '5' && this.userDTO.personID === this.requestObject.irbExemptForm.createdUser) {
                this.isViewMode = true;
            } else {
                this.isViewMode = false;
            }
        } else {
            this.isViewMode = true;
            this.continueBtnClicked = true;
        }
        this.getPiUnits();
    }

    ngAfterViewInit() {
        this.piSearchText
            .valueChanges
            .map((text: any) => text ? text.trim() : '')
            .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
            .debounceTime(500)
            .distinctUntilChanged()
            .switchMap(searchString => {
                return this.getElasticSearchResults(searchString);
            })
            .catch(this.handleError)
            .subscribe(this._results);
        this.facultySearchText
            .valueChanges
            .map((text: any) => text ? text.trim() : '')
            .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
            .debounceTime(500)
            .distinctUntilChanged()
            .switchMap(searchString => {
                return this.getElasticSearchResults(searchString);
            })
            .catch(this.handleError)
            .subscribe(this._results);
        if (this.isViewMode === false && this.isEditMode === false) {
            this.openWelcomeModal();
        }
    }

    /**fetches elastic search results
     * @param searchString - string enterd in the input field
     */
    getElasticSearchResults(searchString) {
        return new Promise<Array<String>>((resolve, reject) => {
            this._ngZone.runOutsideAngular(() => {
                let hits_source: Array<any> = [];
                let hits_highlight: Array<any> = [];
                const hits_out: Array<any> = [];
                const results: Array<any> = [];
                let personName: string;
                let test;
                this._elasticsearchService.irbSearch(searchString)
                    .then((searchResult) => {
                        this._ngZone.run(() => {
                            hits_source = ((searchResult.hits || {}).hits || [])
                                .map((hit) => hit._source);
                            hits_highlight = ((searchResult.hits || {}).hits || [])
                                .map((hit) => hit.highlight);

                            hits_source.forEach((elmnt, j) => {
                                personName = hits_source[j].full_name;
                                test = hits_source[j];

                                if (typeof (hits_highlight[j].full_name) !== 'undefined') {
                                    personName = hits_highlight[j].full_name;
                                }
                                results.push({
                                    label: personName,
                                    obj: test
                                });
                            });
                            if (results.length > 0) {
                                this.message = '';
                            } else {
                                if (this.requestObject.irbExemptForm.personName && this.requestObject.irbExemptForm.personName.trim()) {
                                    this.message = 'nothing was found';
                                }
                            }
                            resolve(results);
                        });

                    })
                    .catch((error) => {
                        this._ngZone.run(() => {
                            reject(error);
                        });
                    });
            });
        });
    }

    /**show message if elastic search failed */
    handleError(): any {
        this.message = 'something went wrong';
    }


    /**
     * @param  {} id
     */
    onKeyEnterElastic(id) {
        $('#' + id + ' li.selected').trigger('click');
    }

    /** calls service to fetch Units of PI */
    getPiUnits() {
        this._exemptQuestionaireService.getPIUnit().subscribe(
            data => {
                this.result = data || [];
                if (this.result != null) {
                    this.units = this.result;
                    this.units.forEach((value, index) => {
                        this.unitName[index] = value.UNIT_NAME;
                    });
                }
            },
            error => {
                console.log('Error in getPiUnits', error);
            }
        );
    }

    unitChangeFunction(unitName) { }

    /** assign values to requestObject on selecting a particular unit from suggestion box */
    onUnitSelect() {
        this.units.forEach((value, index) => {
            if (value.UNIT_NAME === this.requestObject.irbExemptForm.unitName) {
                this.requestObject.irbExemptForm.unitNumber = value.UNIT_NUMBER;
                this.requestObject.irbExemptForm.unitName = value.UNIT_NAME;
            }
        });
    }

    clearSelectedPIdata() {
        this.requestObject.irbExemptForm.personId = null;
        this.requestObject.personJobTitle = null;
    }

    /** assign values to requestObject on selecting a particular PI from elastic search */
    selectedPiResult(result) {
        this.requestObject.irbExemptForm.personName = '';
        this.requestObject.irbExemptForm.personName = result.obj.full_name;
        this.requestObject.irbExemptForm.personId = result.obj.person_id;
        if (result.obj.haspistatus === 0) {
            this.requestObject.personJobTitle = null;
        } else {
            this.requestObject.personJobTitle = result.obj.job_title;
        }
       // this.requestObject.personJobTitle = result.obj.job_title;
        this.IsElasticResultPI = false;
    }

    clearSelectedFacultydata() {
        this.requestObject.irbExemptForm.facultySponsorPersonId = null;
        this.requestObject.facultySponsorJobTitle = null;

    }

    /** assign values to requestObject on selecting a particular Faculty from elastic search */
    selectedFacultyResult(result) {
        this.requestObject.irbExemptForm.facultySponsorPerson = '';
        this.requestObject.irbExemptForm.facultySponsorPerson = result.obj.full_name;
        this.requestObject.irbExemptForm.facultySponsorPersonId = result.obj.person_id;
        if (result.obj.haspistatus === 0) {
            this.requestObject.facultySponsorJobTitle = null;
        } else {
            this.requestObject.facultySponsorJobTitle = result.obj.job_title;
        }
      //  this.requestObject.facultySponsorJobTitle = result.obj.job_title;
        this.IsElasticResultFaculty = false;
    }

    validateStartDate() {
        if (this.requestObject.exemptProtocolEndDate != null || this.requestObject.exemptProtocolEndDate !== undefined) {
        if (Date.parse(this.requestObject.exemptProtocolStartDate) >= Date.parse(this.requestObject.exemptProtocolEndDate)) {
       this.invalidData.invalidStartDate = true;
        } else {
            this.invalidData.invalidStartDate = false;
            this.invalidData.invalidEndDate = false;
        }
    }
}

    validateEndDate() {
        if (this.requestObject.exemptProtocolEndDate != null || this.requestObject.exemptProtocolEndDate !== undefined) {
        if (Date.parse(this.requestObject.exemptProtocolEndDate) <= Date.parse(this.requestObject.exemptProtocolStartDate)) {
            this.invalidData.invalidEndDate = true;
        } else {
            this.invalidData.invalidStartDate = false;
            this.invalidData.invalidEndDate = false;
        }
    }
 }

    /** function to load exempt questionaire once the title and pi and exempt is created */
    loadQuestionaire() {
        this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
        this.showAlert = false;
        if (this.requestObject.irbExemptForm.personId !== '' &&
            (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
            (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
            (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== null) &&
            (this.requestObject.exemptProtocolStartDate !== '' && this.requestObject.exemptProtocolStartDate !== null) &&
            (this.requestObject.exemptProtocolEndDate !== '' && this.requestObject.exemptProtocolEndDate !== null)) {
            if (Date.parse(this.requestObject.exemptProtocolStartDate) >= Date.parse(this.requestObject.exemptProtocolEndDate)) {
                this.showAlert = true;
                this.modalHeading = 'Alert';
                this.alertMsg = 'End date should be greater than start date';
            } else if (!this.ignoreWarning && (this.requestObject.irbExemptForm.facultySponsorPersonId === ''
                || this.requestObject.irbExemptForm.facultySponsorPersonId == null) && this.requestObject.personJobTitle == null) {
                this.modalHeading = 'Alert';
                this.alertMsg = this.NOFACULTY_MESSAGE;
                this.openWarningModal();
            } else if (!this.ignoreWarning && this.requestObject.facultySponsorJobTitle == null
                && this.requestObject.personJobTitle == null) {
                this.modalHeading = 'Alert';
                this.alertMsg = this.NOFACULTY_TITILE_MESSAGE;
                this.openWarningModal();
            } else {
                this.disableButton = true;
                this.showAlert = false;
                this.invalidData.invalidExemptData = false;
                this.continueBtnClicked = true;
                this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
                this.requestObject.irbExemptForm.actionTypesCode = this.strCreatedActionType;
                this.requestObject.irbExemptForm.statusCode = this.strInProgressStatusCode;
                this.ignoreWarning = false;
                this.requestObject.irbExemptForm.exemptProtocolStartDate =
                this.GetFormattedDate(this.requestObject.exemptProtocolStartDate);
                this.requestObject.irbExemptForm.exemptProtocolEndDate = this.GetFormattedDate(this.requestObject.exemptProtocolEndDate);
                this._exemptQuestionaireService.getQuestionaire(this.requestObject).subscribe(
                    data => {
                        this.result = data || [];
                        if (this.result != null) {
                            this.questionaire = this.result.questionnaireDto;
                            this.requestObject.irbExemptForm = this.result.irbExemptForm;
                        }
                    },
                    error => {
                        console.log('Error in getQuestionaire', error);
                    },
                    () => {
                        this.showAlert = true;
                        this.showQuestionaire = true;
                    }
                );
            }
        } else if (this.requestObject.irbExemptForm.personId === ''
            && this.requestObject.irbExemptForm.personName !== '' && this.requestObject.irbExemptForm.personName !== null) {
                this.showAlert = true;
            this.modalHeading = 'Alert';
            this.alertMsg = 'Please enter valid data';
        } else {
            this.invalidData.invalidExemptData = true;
        }
    }

    /** function to load exempt questionaire which was saved once  loaded in edit or view mode only */
    loadSavedQuestionaire() {
        this._spinner.show();
        this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
        this._exemptQuestionaireService.getSavedQuestionaire(this.requestObject).subscribe(
            data => {
                this.result = data || [];
                if (this.result != null) {

                    // Removed checking of faculty sponsor job titlein 1st if condition
                    // Removed (this.result.irbExemptForm.loggedInUserPI === true && this.result.irbExemptForm.pijobTitle != null
                    // && this.result.irbExemptForm.statusCode === '5')
                    if ((this.result.irbExemptForm.loggedInUserFacultySponsor === true && this.result.irbExemptForm.statusCode === '2') ||
                         (this.result.irbExemptForm.statusCode === '3' &&
                            (this.userDTO.role === 'CHAIR' || this.userDTO.role === 'ADMIN'))) {
                        this.isPendingActionRequired = true;
                    }
                    if (this.result.irbExemptForm.statusCode === '2' || this.result.irbExemptForm.statusCode === '3' ||
                     this.result.irbExemptForm.statusCode === '3' || this.result.irbExemptForm.statusCode === '4' ||
                      this.result.irbExemptForm.statusCode === '5') {
                        this.isQuestionaireSubmitted = true;
                    } else {
                        this.isQuestionaireSubmitted = false;
                    }

                    if (this.isActionByPi && this.result.irbExemptForm.loggedInUserPI === true) {
                        this.isActionByPi = true;
                    } else {
                        this.isActionByPi = false;
                    }
                    if ( this.result.irbExemptForm.exemptQuestionList != null) {
                        this.exemptQuestionList = this.result.irbExemptForm.exemptQuestionList;
                    }
                    this.questionaire = this.result.questionnaireDto;
                    this.requestObject.personJobTitle = this.result.irbExemptForm.pijobTitle;
                    this.requestObject.facultySponsorJobTitle = this.result.irbExemptForm.facultySponsorJobTitle;
                    this.requestObject.irbExemptForm = this.result.irbExemptForm;
                    if (this.requestObject.irbExemptForm.exemptProtocolStartDate != null
                        && this.requestObject.irbExemptForm.exemptProtocolStartDate !== '') {
                        this.requestObject.exemptProtocolStartDate =
                            this.GetFormattedDateFromString(this.requestObject.irbExemptForm.exemptProtocolStartDate);
                    }
                    if (this.requestObject.irbExemptForm.exemptProtocolEndDate != null
                        && this.requestObject.irbExemptForm.exemptProtocolEndDate !== '') {
                        this.requestObject.exemptProtocolEndDate
                            = this.GetFormattedDateFromString(this.requestObject.irbExemptForm.exemptProtocolEndDate);
                    }
                    if (this.requestObject.irbExemptForm.statusCode === '2' || this.requestObject.irbExemptForm.statusCode === '3') {
                        this.isViewMode = true;
                    }
                    if (this.requestObject.irbExemptForm.statusCode === '5'
                        && this.userDTO.personID === this.requestObject.irbExemptForm.createdUser) {
                        this.isViewMode = true;
                    }
                   this.updateExemptQuestionnaire();
                    this.QuestionnaireCompletionFlag = 'Y';
                    this.checkQuestionaireCompletion();
                }
            },
            error => {
                console.log('Error in getSavedQuestionaire', error);
            },
            () => {
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
            let lastGROUP_NAME = '';
            this.questionaire.questionnaireConditions.forEach(condition => {
                switch (condition.CONDITION_TYPE) {
                    case 'EQUALS':
                        if (condition.QUESTION_ID === currentQuestion.questionId) {
                            this.questionaire.questionnaireQuestions.forEach(question => {
                                if (condition.GROUP_NAME === question.groupName
                                    && condition.CONDITION_VALUE === currentQuestion.selectedAnswer) {
                                    question.showQuestion = true;
                                    if (question.acType == null) {
                                        question.acType = 'I';
                                    } else if (currentQuestion.acType === 'D') {
                                        currentQuestion.acType = 'U';
                                    }
                                    lastGROUP_NAME = condition.GROUP_NAME;
                                } else if (condition.GROUP_NAME === question.groupName &&
                                    condition.CONDITION_VALUE !== currentQuestion.selectedAnswer
                                    && lastGROUP_NAME !== condition.GROUP_NAME) {
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
                if (childQuestion !== undefined) {
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
                this.isQuestionnaireContinueClicked = false; // this flag is used to hide Submit Button if qstnr is not completed
            }
        });
    }

    /** service call to save the exempt questionaire */
    saveQuestionaire() {
        this.isEvaluate = false;
        this.errorQuestions = [];
        this.QuestionnaireCompletionFlag = 'Y';
        this.requestObject.questionnaireInfobean = {};
        this.modalHeading = '';
        this.alertMsg = '';
        this.result = {};
        this.checkQuestionaireCompletion();
        this.setCommonReqObject();
        this.requestObject.questionnaireInfobean.isSubmit = 'N';
        this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
        this.requestObject.personDTO.personID = this.userDTO.personID;
        this.requestObject.personDTO.fullName = this.userDTO.fullName;
        if ((this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
            (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
            (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== '') &&
            (this.requestObject.exemptProtocolStartDate !== '' && this.requestObject.exemptProtocolStartDate !== null) &&
            (this.requestObject.exemptProtocolEndDate !== '' && this.requestObject.exemptProtocolEndDate !== null)) {
            this.invalidData.invalidExemptData = false;
                this._spinner.show();
            this._exemptQuestionaireService.saveQuestionaire(this.requestObject).subscribe(
                data => {
                    this.result = data;
                    if (this.result != null) {
                        this.requestObject.irbExemptForm = this.result.irbExemptForm;
                        // if (this.QuestionnaireCompletionFlag === 'Y') {
                        //     this.modalHeading = 'Questionnaire Complete';
                        //     this.alertMsg = 'You have completed the exempt questionnaire. Please click on' +
                        //         ' Preview to assess your exempt determination status. Once you are ready to' +
                        //         ' proceed, click Submit to continue.';
                        // } else {
                        //     this.showContinueButton = true;
                        //     this.modalHeading = 'Questionnaire Complete';
                        //     this.alertMsg = 'Questionnaire saved successfully!';
                        // }
                        this.toastr.success('Questionnaire saved successfully!', null, { toastLife: 2000 });
                        if (this.result.hasOwnProperty('questionnaireDto')) {
                            this.questionaire.questionnaireQuestions = this.result.questionnaireDto.questionnaireQuestions;
                        }
                    }
                },
                error => {
                    console.log('Error in saveQuestionaire', error);
                    this.toastr.error('Failed to save questionaire', null, { toastLife: 2000 });
                    // this.modalHeading = 'Alert';
                    // this.alertMsg = 'Failed to save questionaire';
                },
                () => {
                    this._spinner.hide();
                    //  this.openSaveModal();
                    this.updateExemptQuestionnaire();
                }
            );
        } else {
            this.invalidData.invalidExemptData = true;
        }
    }

    /** calls service to evaluate exempt questioanire */
    evaluateExemptQuestionnaire() {
        this.isQuestionnaireContinueClicked = true;
        this.errorQuestions = [];
        this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
        if (this.requestObject.irbExemptForm.personId !== '' &&
            (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
            (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
            (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== null) &&
            (this.requestObject.exemptProtocolStartDate !== '' && this.requestObject.exemptProtocolStartDate !== null) &&
            (this.requestObject.exemptProtocolEndDate !== '' && this.requestObject.exemptProtocolEndDate !== null)) {

            this._spinner.show();
            this._exemptQuestionaireService.evaluatedQuestionaire(this.requestObject).subscribe(
                data => {
                    this.result = data;
                    this.isExempt = this.result.isExemptGranted;

                    if ((this.requestObject.irbExemptForm.facultySponsorPersonId === ''
                        || this.requestObject.irbExemptForm.facultySponsorPersonId == null) && this.requestObject.personJobTitle == null) {
                        if (this.result.isExemptGranted === 'N') {
                            this.alertMsg = this.NOT_EXEMPT_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_MESSAGE;
                            this.errorQuestions = this.result.exemptQuestionList;
                        } else if (this.result.isExemptGranted === 'Y') {
                            this.alertMsg = this.EXEMPT_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_WARNING_MESSAGE;
                        } else if (this.result.isExemptGranted === 'O') {
                            this.alertMsg = this.OTHER_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_MESSAGE;
                        }
                    } else if (this.requestObject.facultySponsorJobTitle == null && this.requestObject.personJobTitle == null) {
                        if (this.result.isExemptGranted === 'N') {
                            this.alertMsg = this.NOT_EXEMPT_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_WARNING_MESSAGE;
                            this.errorQuestions = this.result.exemptQuestionList;
                        } else if (this.result.isExemptGranted === 'Y') {
                            this.alertMsg = this.EXEMPT_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_WARNING_MESSAGE;
                        } else if (this.result.isExemptGranted === 'O') {
                            this.alertMsg = this.OTHER_MSG;
                            this.showEvaluateWarning = this.NOFACULTY_WARNING_MESSAGE;
                        }
                    } else if (this.result.isExemptGranted === 'N') {
                        this.showEvaluateWarning = '';
                        this.alertMsg = this.NOT_EXEMPT_MSG;
                        this.errorQuestions = this.result.exemptQuestionList;
                    } else if (this.result.isExemptGranted === 'Y') {
                        this.showEvaluateWarning = '';
                        this.alertMsg = this.EXEMPT_MSG;
                    } else if (this.result.isExemptGranted === 'O') {
                        this.showEvaluateWarning = '';
                        this.alertMsg = this.OTHER_MSG;
                    }
                },
                error => {
                    console.log('Error in evaluateExemptQuestionnaire', error);
                    this.alertMsg = 'Failed to evaluate questionaire';
                },
                () => {
                    this._spinner.hide();
                    this.isEvaluate = true;
                }
            );

        } else if (this.requestObject.irbExemptForm.personId === ''
            && this.requestObject.irbExemptForm.personName !== '' && this.requestObject.irbExemptForm.personName !== null) {
            this.showAlert = true;
            this.modalHeading = 'Alert';
            this.alertMsg = 'Please enter valid data';
        } else {
            this.openSaveModal();
            this.modalHeading = 'Alert';
            this.alertMsg = 'Please provide the mandatory fields';
        }
    }

    /** shows a popup to show the certify message before submitting */
    submitConfirmation() {
        this.isEvaluate = false;
        this.modalHeading = '';
        this.alertMsg = '';
        this.QuestionnaireCompletionFlag = 'Y';
        this.checkQuestionaireCompletion();
        if (this.QuestionnaireCompletionFlag === 'Y') {
            this.alertMsg = this.PI_DECLARATION;
        } else {
            this.alertMsg = 'Cannot submit questionaire since it is not complete';
        }
    }

    /** sets the value of checkbox to false if the certify message is closed without agreeing*/
      closeConfirmation() {
        this.piDeclaration.isAgreePoliciesChecked = false;
        this.piDeclaration.isComprehensiveReviewChecked = false;
        this.piDeclaration.isConfirmationChecked = false;
        this.piDeclaration.isMemberResponsibilityChecked = false;
        this.showConfirmationDetails = true;
    }

    /** submit questionaire after completing and agreeing*/
    submitQuestionaire() {
        this.isEvaluate = false;
        this.isQuestionaireSubmitted = true;
        this.errorQuestions = [];
        this.modalHeading = 'Alert';
        this.alertMsg = '';
        this.setCommonReqObject();

        if (this.requestObject.irbExemptForm.personId != null &&
            this.requestObject.irbExemptForm.personId !== '' && this.userDTO.personID !== this.requestObject.irbExemptForm.personId) {
            this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
            this.requestObject.irbExemptForm.statusCode = this.strEnrouteToPiStatusCode;
        } else if ((this.requestObject.personJobTitle == null || this.requestObject.personJobTitle === '')) {   // PI - NO PI Status


            // if (this.requestObject.facultySponsorJobTitle == null || this.requestObject.facultySponsorJobTitle === '') {
            //     this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
            //     this.requestObject.irbExemptForm.statusCode = this.strEnroutedIrbOfficeStatusCode;
            // }



            if (this.requestObject.irbExemptForm.facultySponsorPersonId == null ||
                this.requestObject.irbExemptForm.facultySponsorPersonId === '') {
                this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
                this.requestObject.irbExemptForm.statusCode = this.strEnroutedIrbOfficeStatusCode;
            } else {
                if (this.requestObject.facultySponsorJobTitle !== null && this.requestObject.facultySponsorJobTitle !== '') {
                if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                    this.requestObject.irbExemptForm.notificationNumber = 705;
                } else if (this.requestObject.irbExemptForm.isExempt === 'N') {
                    this.requestObject.irbExemptForm.notificationNumber = 709;
                } else if (this.requestObject.irbExemptForm.isExempt === 'O') {
                    this.requestObject.irbExemptForm.notificationNumber = 710;
                }
            }
                this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
                this.requestObject.irbExemptForm.statusCode = this.strEnroutedToFacultyStatusCode;
            }
        } else if ((this.requestObject.personJobTitle !== null || this.requestObject.personJobTitle !== '')) { // PI has - PI STATUS


            // if ((this.requestObject.facultySponsorJobTitle == null || this.requestObject.facultySponsorJobTitle === '')) {

            if ((this.requestObject.irbExemptForm.facultySponsorPersonId == null ||
                this.requestObject.irbExemptForm.facultySponsorPersonId === '')) {

                if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                    this.requestObject.irbExemptForm.notificationNumber = 701;
                } else if (this.requestObject.irbExemptForm.isExempt === 'N') {
                    this.requestObject.irbExemptForm.notificationNumber = 702;
                } else if (this.requestObject.irbExemptForm.isExempt === 'O') {
                    this.requestObject.irbExemptForm.notificationNumber = 710;
                }
                this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
                this.requestObject.irbExemptForm.statusCode = this.strSubmittedStatusCode;
            } else
            // if ((this.requestObject.facultySponsorJobTitle !== null || this.requestObject.facultySponsorJobTitle !== ''))
            {
                if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                    this.requestObject.irbExemptForm.notificationNumber = 705;
                } else if (this.requestObject.irbExemptForm.isExempt === 'N') {
                    this.requestObject.irbExemptForm.notificationNumber = 709;
                } else if (this.requestObject.irbExemptForm.isExempt === 'O') {
                    this.requestObject.irbExemptForm.notificationNumber = 710;
                }
                this.requestObject.irbExemptForm.actionTypesCode = this.strSubmitActionType;
                this.requestObject.irbExemptForm.statusCode = this.strEnroutedToFacultyStatusCode;
            }
        }
        this.requestObject.questionnaireInfobean.isSubmit = 'Y';
        this.requestObject.questionnaireInfobean = JSON.stringify(this.requestObject.questionnaireInfobean);
        this.requestObject.personDTO.personID = this.userDTO.personID;
        this.requestObject.personDTO.fullName = this.userDTO.fullName;
        this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
        if (this.requestObject.irbExemptForm.personId !== '' &&
            (this.requestObject.irbExemptForm.exemptTitle !== '' && this.requestObject.irbExemptForm.personName !== '') &&
            (this.requestObject.irbExemptForm.exemptTitle !== null && this.requestObject.irbExemptForm.personName !== null) &&
            (this.requestObject.irbExemptForm.summary !== '' && this.requestObject.irbExemptForm.summary !== null) &&
            (this.requestObject.exemptProtocolStartDate !== '' && this.requestObject.exemptProtocolStartDate !== null) &&
            (this.requestObject.exemptProtocolEndDate !== '' && this.requestObject.exemptProtocolEndDate !== null)) {
            if (!this.ignoreWarning && (this.requestObject.irbExemptForm.facultySponsorPersonId === ''
                || this.requestObject.irbExemptForm.facultySponsorPersonId == null) && this.requestObject.personJobTitle == null) {
                this.modalHeading = 'Alert';
                this.alertMsg = this.NOFACULTY_MESSAGE;
                this.openWarningModal();
            } else if (!this.ignoreWarning && this.requestObject.facultySponsorJobTitle == null
                && this.requestObject.personJobTitle == null) {
                this.modalHeading = 'Alert';
                this.alertMsg = this.NOFACULTY_TITILE_MESSAGE;
                this.openWarningModal();
            } else {
                this._spinner.show();
                this._exemptQuestionaireService.saveQuestionaire(this.requestObject).subscribe(
                    data => {
                        this.result = data;
                        if (this.result != null) {
                            if (this.result.irbExemptForm.isExempt === 'N') {
                                this.alertMsg = this.NOT_EXEMPT_MSG_SUBMIT;
                            } else if (this.result.irbExemptForm.isExempt === 'Y') {
                                this.alertMsg = this.EXEMPT_MSG_SUBMIT;
                            } else if (this.result.irbExemptForm.isExempt === 'O') {
                                this.alertMsg = this.OTHER_MSG;
                            }
                            this.requestObject.irbExemptForm = this.result.irbExemptForm;
                            this.exemptQuestionList = this.result.exemptQuestionList;
                            this.isViewMode = true;
                        }
                    },
                    error => {
                        console.log('Error in submitQuestionaire', error);
                        this.alertMsg = 'Failed to submit questionaire';
                    },
                    () => {
                        this._spinner.hide();
                        this.openSaveModal();
                    }
                );
            }
        } else {
            this.openSaveModal();
            this.modalHeading = 'Alert';
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
                            if (childquestions.groupName === condition.GROUP_NAME) {
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
            if (element.QUESTION_ID === questionId) {
                isError = true;
            }
        });
        return isError;
    }

    /** close welcome message modal */
    closeWelcome() {
        this.showWelcomeMessage = false;
    }

    onChange(files: FileList) {
        this.fil = files;
        this.isDuplicate = false;
        if (this.uploadedFile.length === 0) {
            for (let index = 0; index < this.fil.length; index++) {
                this.uploadedFile.push(this.fil[index]);
            }
        } else {
            for (let index = 0; index < this.fil.length; index++) {
                this.isDuplicate = false;
                for (let j = 0; j < this.uploadedFile.length; j++) {
                    if (this.fil[index].name === this.uploadedFile[j].name) {
                        this.isDuplicate = true;
                    }
                }
                if (!this.isDuplicate) {
                    this.uploadedFile.push(this.fil[index]);
                }
            }
        }
        this.changeRef.detectChanges();
    }

    public dropped(event: UploadEvent) {
        this.files = event.files;
        for (const file of this.files) {
            this.attachmentList.push(file);
        }
        for (const file of event.files) {
            if (file.fileEntry.isFile) {
                const fileEntry = file.fileEntry as FileSystemFileEntry;
                fileEntry.file((info: File) => {
                    if (this.uploadedFile.length === 0) {
                        this.uploadedFile.push(info);
                    } else {
                        for (let i = 0; i < this.uploadedFile.length; i++) {
                            if (this.uploadedFile[i].name === info.name) {
                                this.isDuplicate = true;
                            }
                        }
                        if (!this.isDuplicate) {
                            this.uploadedFile.push(info);
                            this.isDuplicate = false;
                        }
                    }
                    this.changeRef.detectChanges();
                });
            }
        }
    }

    deleteFromUploadedFileList(item) {
        for (let i = 0; i < this.uploadedFile.length; i++) {
            if (this.uploadedFile[i].name === item.name) {
                this.uploadedFile.splice(i, 1);
                this.changeRef.detectChanges();
            }
        }
    }

    showAddAttachmentPopUp(e) {
        e.preventDefault();
        this.showAddAttachment = true;
        this.uploadedFile = [];
        this.attachmentDescription = '';
        this.isEmptyCheckListDescr = false;
        this.changeRef.detectChanges();
        this._exemptQuestionaireService.showExemptProtocolChecklist(this.requestObject).subscribe(data => {
            let temp: any = {};
            temp = data;
            this.exemptAttachmentObject = temp;
        });
    }

    addAttachments(index) {
        if (this.attachmentDescription === null || this.attachmentDescription === undefined || this.attachmentDescription === '') {
            this.isEmptyCheckListDescr = true;
        } else {
            this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
            this.requestObject.irbExemptForm.checkListDescription = this.attachmentDescription;
            this.requestObject.irbExemptForm.checkListAcType = 'I';
            this._exemptQuestionaireService.addExemptProtocolChecklist(
                this.requestObject.irbExemptForm, this.uploadedFile).subscribe(data => {
                    this.uploadedFile = [];
                    this.attachmentDescription = '';
                    this.isEmptyCheckListDescr = false;
                    let temp: any = {};
                    temp = data;
                    this.exemptAttachmentObject = temp;
                },
                    error => {
                    });
        }
    }

    tempSave(event, attachment) {
        this.showPopup = true;
        this.tempSaveAttachment = attachment;
    }

    deleteAttachments(event) {
        event.preventDefault();
        this.showPopup = false;
        this.requestObject.irbExemptForm.checkListId = this.tempSaveAttachment.EXEMPT_FORM_CHECKLST_ID;
        this.requestObject.irbExemptForm.checkListAcType = 'D';
        this._exemptQuestionaireService.addExemptProtocolChecklist(this.requestObject.irbExemptForm, this.uploadedFile).subscribe(data => {
            this.uploadedFile = [];
            let temp: any = {};
            temp = data;
            this.exemptAttachmentObject = temp;
        },
            error => {
            });
    }

    downloadAttachements(event, attachments) {
        event.preventDefault();
        this._exemptQuestionaireService.downloadExemptProtocolChecklist(attachments.EXEMPT_FORM_CHECKLST_ID).subscribe(
            data => {
                const a = document.createElement('a');
                const blob = new Blob([data], { type: data.type });
                a.href = URL.createObjectURL(blob);
                a.download = attachments.FILENAME;
                a.click();
            });
        return false;
    }

    editAttachments(event: any, index, attachments) {
        event.preventDefault();
        this.tempEditObject.description = attachments.DESCRIPTION;
        this.editScheduleattachment[index] = !this.editScheduleattachment[index];
    }

    saveEditedattachments(event: any, index, attachments) {
        event.preventDefault();
        this.editScheduleattachment[index] = !this.editScheduleattachment[index];
        this.requestObject.irbExemptForm.checkListId = attachments.EXEMPT_FORM_CHECKLST_ID;
        this.requestObject.irbExemptForm.updateUser = this.userDTO.userName;
        this.requestObject.irbExemptForm.checkListDescription = attachments.DESCRIPTION;
        this.requestObject.irbExemptForm.checkListAcType = 'U';
        this._exemptQuestionaireService.addExemptProtocolChecklist(this.requestObject.irbExemptForm, this.uploadedFile).subscribe(data => {
            this.uploadedFile = [];
            let temp: any = {};
            temp = data;
            this.exemptAttachmentObject = temp;
        },
            error => {
            });
    }

    cancelEditedattachments(event: any, index, attachments) {
        event.preventDefault();
        this.editScheduleattachment[index] = !this.editScheduleattachment[index];
        attachments.DESCRIPTION = this.tempEditObject.description;
    }

    closeAttachments() {
        this.showAddAttachment = false;
        this.uploadedFile = [];
    }
    proceedWithWarning() {
        this.ignoreWarning = true;
        if (this.QuestionnaireCompletionFlag === 'N') {
            this.loadQuestionaire();
        } else {
            this.submitQuestionaire();
        }

    }

    /**opening warning modal using hidden button
     * call this fn to trigeer modal open of save
    */
    openWarningModal() {
        document.getElementById('openWarningModalButton').click();
    }
    /**opening when clicking action log  button
     *
     */
    openActionLogModal() {
        this.actionLogResult = [];
        this._exemptQuestionaireService.getActivityLogByExemptFormID(this.requestObject).subscribe(
            data => {
                this.actionLogResult = data || [];
            },
            error => {
                console.log('Error in getActivityLogById', error);
            },
            () => {
            }
        );
    }
    returnQuestionnaireClick() {
        this.ApproveRejectHeader = 'Return Exempt Study';
        this.requestObject.irbExemptForm.comment = '';
        this.isApprovedAlert = false;
        this.isRejectedAlert = false;
        this.isReturnAlert = true;
    }

    returnConfirmClick() {
        this.requestObject.irbExemptForm.statusCode = this.strInProgressStatusCode;
        this.requestObject.irbExemptForm.actionTypesCode = this.strReturnedByPIActionType;
        this.isViewMode = true;
        this.approveRejectAction(this.requestObject);
    }

    approveQuestionnireClick() {// condition added for showing confirmation popup for faculty sponsor
        if (this.userDTO.personID === this.requestObject.irbExemptForm.facultySponsorPersonId
            && this.requestObject.irbExemptForm.statusCode === '2') {
            document.getElementById('openConfirmModalButton').click();
        } else {
            document.getElementById('openApproveModalButton').click();

        }
        this.ApproveRejectHeader = 'Approve Exempt Study';
        this.requestObject.irbExemptForm.comment = '';
        this.isApprovedAlert = true;
        this.isRejectedAlert = false;
        this.isReturnAlert = false;
        this.showApprovePopup = true;
    }

    approveConfirmClick() {
      // this.requestObject.irbExemptForm.statusCode = this.strSubmittedStatusCode;
        if (this.requestObject.personDTO.role === 'PI' || this.requestObject.personDTO.role === 'DEPT_ADMIN') {
            if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                this.requestObject.irbExemptForm.notificationNumber = 706;
            }
            if (this.requestObject.irbExemptForm.isExempt === 'N') {
                this.requestObject.irbExemptForm.notificationNumber = 707;
            }
            if (this.requestObject.irbExemptForm.isExempt === 'O') {
                this.requestObject.irbExemptForm.notificationNumber = 710;
            }


            if (this.requestObject.personJobTitle == null || this.requestObject.personJobTitle === '') {
                if ((this.requestObject.facultySponsorJobTitle == null || this.requestObject.facultySponsorJobTitle === '')) {
                    this.requestObject.irbExemptForm.statusCode = this.strEnroutedIrbOfficeStatusCode;
                } else {
                    this.requestObject.irbExemptForm.statusCode = this.strSubmittedStatusCode;
                }
            } else {
                this.requestObject.irbExemptForm.statusCode = this.strSubmittedStatusCode;
            }


            this.requestObject.irbExemptForm.actionTypesCode = this.strApprovedFacultyActionType;
        } else if (this.requestObject.personDTO.role === 'CHAIR' || this.requestObject.personDTO.role === 'ADMIN') {
            if (this.requestObject.irbExemptForm.loggedInUserFacultySponsor === true) {
                if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                    this.requestObject.irbExemptForm.notificationNumber = 706;
                }
                if (this.requestObject.irbExemptForm.isExempt === 'N') {
                    this.requestObject.irbExemptForm.notificationNumber = 707;
                }
                if (this.requestObject.irbExemptForm.isExempt === 'O') {
                    this.requestObject.irbExemptForm.notificationNumber = 710;
                }
            }
            this.requestObject.irbExemptForm.statusCode = this.strSubmittedStatusCode;
            this.requestObject.irbExemptForm.actionTypesCode = this.strApprovedIrbOfficeActionType;
        }
        this.approveRejectAction(this.requestObject);
    }

    rejectQuestionnireClick() {
        this.ApproveRejectHeader = 'Reject Exempt Study';
        this.requestObject.irbExemptForm.comment = '';
        this.isApprovedAlert = false;
        this.isRejectedAlert = true;
        this.isReturnAlert = false;
    }

    rejectConfirmClick() {
        this.requestObject.irbExemptForm.statusCode = this.strInProgressStatusCode;
        if (this.requestObject.personDTO.role === 'PI' || this.requestObject.personDTO.role === 'DEPT_ADMIN') {
            if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                this.requestObject.irbExemptForm.notificationNumber = 704;
            }
            if (this.requestObject.irbExemptForm.isExempt === 'N') {
                this.requestObject.irbExemptForm.notificationNumber = 708;
            }
            if (this.requestObject.irbExemptForm.isExempt === 'O') {
                this.requestObject.irbExemptForm.notificationNumber = 710;
            }

            this.requestObject.irbExemptForm.actionTypesCode = this.strReturnedFacultyActionType;
        } else if (this.requestObject.personDTO.role === 'CHAIR' || this.requestObject.personDTO.role === 'ADMIN') {
            if (this.requestObject.irbExemptForm.loggedInUserFacultySponsor === true) {
                if (this.requestObject.irbExemptForm.isExempt === 'Y') {
                    this.requestObject.irbExemptForm.notificationNumber = 704;
                }
                if (this.requestObject.irbExemptForm.isExempt === 'N') {
                    this.requestObject.irbExemptForm.notificationNumber = 708;
                }
                if (this.requestObject.irbExemptForm.isExempt === 'O') {
                    this.requestObject.irbExemptForm.notificationNumber = 710;
                }
            }
            this.requestObject.irbExemptForm.actionTypesCode = this.strReturnedIrbOfficeActionType;
        }
        this.approveRejectAction(this.requestObject);
    }

    approveRejectAction(reqObjectVal) {
        this.isPendingActionRequired = false;
        reqObjectVal.irbExemptForm.updateUser = this.userDTO.userName;
        this._exemptQuestionaireService.approveOrDisapproveAction(reqObjectVal).subscribe(
            data => {
                this.result = data;
                this.requestObject.irbExemptForm.status = this.result.irbExemptForm.status;
                this.isApprovedAlert = false;
                this.isRejectedAlert = false;
            },
            error => {
                console.log('Error in getActivityLogById', error);
            }
        );
    }

    GetFormattedDate(currentDate) {
        const month = currentDate.getMonth() + 1;
        const day = currentDate.getDate();
        const year = currentDate.getFullYear();
        return month + '-' + day + '-' + year;
    }

    GetFormattedDateFromString(currentDate) {
        const res = currentDate.split('-');
        const month = parseInt(res[0], 10);
        const day = parseInt(res[1], 10);
        const year = parseInt(res[2], 10);
        return new Date(year, month - 1, day);
    }

    triggerAdd() {
        $('#addAttach').trigger('click');
    }

    continueWithDashboard() {
        this.showContinueButton = false;
        this._router.navigate(['/irb/dashboard']);
        $('#alertModal').modal('hide');
    }

    continueWithQstnr() {
        this.showContinueButton = false;
        $('#alertModal').modal('hide');
    }

    returnToExemptList(event) {
        // event.preventDefault();
        // this._sharedDataService.changeCurrentTab('EXEMPT');
        // this._router.navigate(['/irb/dashboard']);
        this._location.back();
    }
    returnToExemptList2(event) {
        // event.preventDefault();
        // this._sharedDataService.changeCurrentTab('EXEMPT');
        // this._router.navigate(['/irb/dashboard']);
        this.saveQuestionaire();
        this._location.back();
    }

    generateCorrespondence() {
        const obj = {irbExemptForm: this.requestObject.irbExemptForm};
        this._exemptQuestionaireService.generateCorrespondence(obj).subscribe( data => {
            const a = document.createElement( 'a' );
            const blob = new Blob( [data], { type: data.type} );
            a.href = URL.createObjectURL( blob );
            a.download = 'Exempt Determination - ' + this.requestObject.irbExemptForm.exemptFormID + '.pdf';
            document.body.appendChild(a);
            a.click();

        },
            error => console.log( 'Error downloading the file.'),
            () => console.log( 'OK' ) );
    }
      openApprovePopup() {
        $('#confirmModal').modal('hide');
        document.getElementById('openApproveModalButton').click();
        this.showApprovePopup = false;
    }
}
