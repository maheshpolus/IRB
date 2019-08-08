import { Component, OnInit, Input, ChangeDetectionStrategy, Output,
         ChangeDetectorRef, HostListener, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Constants } from '../../questionnaire.constants';
import * as _ from 'lodash';
import { CreateQuestionnaireService } from '../../services/create.service';
import { easeInOUt } from '../../services/animations';
import { ISubscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-create-questionnaire',
  templateUrl: './create-questionnaire.component.html',
  styleUrls: ['./create-questionnaire.component.css'],
  animations: [easeInOUt],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateQuestionnaireComponent implements OnInit, OnDestroy {

  constructor( private _activatedRoute: ActivatedRoute,
     private _createQuestionnaireService: CreateQuestionnaireService,
     private _changeRef: ChangeDetectorRef) {}
  @Input() questionnaire: any = {};
  @Input() commonValues: any  = {};
  @Input() nodes: any  = {};
  @Input() isFinal = false;
  selectedQuestionIndex = 0;
  toDeleteData   = {};
  editorConfig   = Constants.editorConfig;
  parentDetails  = '';
  isViewmode     = false;
  deleteTypeFlag = null;
  editIndex      = 0;
  debounceTimer: any;
  $updatEditIndex: ISubscription;
  $addQuestion: ISubscription;
  ckEditorConfig: {} = {
    height: '300px',
    toolbarCanCollapse: 1,
    removePlugins: 'sourcearea',
  };

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe( (data: any)  => {
      this.isViewmode   = (data.viewmode === 'true');
      this.editorConfig.editable = !this.isViewmode;
      if (data.id === undefined && this.questionnaire.questions.length === 0 ) {
        this.addNewQuestion('G0');
      }
    });
    this.addQuestionEvent();
    this.updateEditIndexEvent();
  }

  ngOnDestroy() {
    this.$addQuestion.unsubscribe();
    this.$updatEditIndex.unsubscribe();
    this.editIndex = null;
    this.scrollAnimate();
  }
  @HostListener('window:scroll', ['$event']) onWindowScroll() {
      this.scrollAnimate();
  }
  /**Animates the button as user scrolls
   */
  scrollAnimate() {
// tslint:disable-next-line: no-unused-expression
    this.debounceTimer ? clearTimeout(this.debounceTimer) : null;
    this.debounceTimer = setTimeout(() => {
      if (this.editIndex) {
        document.getElementById('floater').style.top =
        document.getElementById( this.editIndex.toString()).getBoundingClientRect().top + 'px' ;
      } else {
        const el =  document.getElementById('floater');
        if (el) {
          el.style.top = '100px';
        }
      }
    }, 500);
  }
  autoFocus() {
    setTimeout( () => {
      document.getElementById(this.editIndex.toString()).scrollIntoView({ behavior: 'smooth', block: 'start' });
      document.getElementById('question' + this.editIndex.toString()).focus();
      this.scrollAnimate();
     });
  }

  /**
   * @param  {} groupName
   * @param  {} parentId
   * Creates a new question basic features for the question is added here
   * like questionId, groupname etc and updates the new questionID
   */
  configureNewQuestion(groupName, parentId) {
    this.commonValues.isQuestionEdited = true;
    groupName === 'G0' ? Constants.newQuestion.SHOW_QUESTION = true : Constants.newQuestion.SHOW_QUESTION = false;
    Constants.newQuestion.GROUP_NAME  = groupName;
    Constants.newQuestion.QUESTION_ID = this.commonValues.lastQuestionId;
    Constants.newQuestion.PARENT_QUESTION_ID = parentId;
    this.addOption(this.commonValues.lastQuestionId, null);
    this.commonValues.lastQuestionId ++;
    this._changeRef.markForCheck();
    return Object.assign({}, Constants.newQuestion);
  }

  /**
   * subcribes to the event from add button Add new question is triggred on click. You should unsubcribe on ngOnDestroy
   * to avoid duplicate subcriptions
   */
  addQuestionEvent() {
    this.$addQuestion = this._createQuestionnaireService.addQuestionEvent
                        .subscribe( data => { this.addNewQuestion(data); });
  }
   /**
   * subcribes to the event from tree which emits the current selected question. You should unsubcribe on ngOnDestroy
   * to avoid duplicate subcriptions
   */
  updateEditIndexEvent() {
    this.$updatEditIndex = this._createQuestionnaireService.updateSelectedQuestionId.subscribe(
      (data: number) => {
        this.editIndex = data;
        this.autoFocus();
        this._changeRef.markForCheck();
    });
  }
  /**
   * @param  {} questionid
   * @param  {} optionLabel
   * updates the QuestionnaireOptions in Questionaire the basic details of option are added
   * the option number is calculated with QuestionnaireOptions array length and timeout is used to avoid errors
   */
  addOption(questionid, optionLabel) {
    this.commonValues.isQuestionEdited = true;
    let optionNumber      = 1;
    if (this.questionnaire.options.length > 0) {
      optionNumber = this.questionnaire.options[this.questionnaire.options.length - 1].QUESTION_OPTION_ID + 1;
    }
    Constants.newOption.QUESTION_ID        = questionid;
    Constants.newOption.QUESTION_OPTION_ID = optionNumber;
    Constants.newOption.OPTION_LABEL       = optionLabel;
    this.questionnaire.options.push(Object.assign({}, Constants.newOption));
    setTimeout(() => { document.getElementById('option' + optionNumber).focus(); });
  }
  /**
   * @param  {} questionId
   * @param  {} index
   * creates a new Condition for a selected question and pushed into QuestionnaireCondition array of questionnaire
   */
  addBranching(questionId, index) {
    this.commonValues.isQuestionEdited = true;
    this.questionnaire.questions[index].HAS_CONDITION = 'Y';
    Constants.newCondition.QUESTION_CONDITION_ID = this.commonValues.lastConditionId;
    Constants.newCondition.QUESTION_ID = questionId;
    Constants.newCondition.GROUP_NAME  = 'G' + this.commonValues.lastGroupName;
    this.commonValues.lastGroupName ++;
    this.commonValues.lastConditionId ++;
    this.questionnaire.conditions.push(Object.assign({}, Constants.newCondition));
  }
  /**
   * @param  {} index
   * @param  {} groupname
   * @param  {} parentId
   * Adds a new Child question to the selected question, child question is pushed just below the parent in array
   * A timeout is used to avoid error of navigating to question before creation
   */
  addConditionBasedQuestion(index, groupName, parentId) {
    this.questionnaire.questions.splice(index + 1, 0, this.configureNewQuestion(groupName, parentId));
    this._createQuestionnaireService.updateTree.next(
      {'parentId': parentId, 'questionId': this.commonValues.lastQuestionId, 'groupName': groupName});
    const id = (this.commonValues.lastQuestionId - 1);
    this.editIndex = id;
    this.autoFocus();
    this._createQuestionnaireService.updateSelectedQuestionId.next(this.editIndex);
  }
  /**
   * @param  {} groupName
   * Adds a base question(G0).A timeout is used to avoid error of navigating to question before creation
   */
  addNewQuestion(groupName) {
    this.questionnaire.questions.push( Object.assign({}, this.configureNewQuestion(groupName, null)));
    this._createQuestionnaireService.updateTree.next({'questionId': this.commonValues.lastQuestionId, 'groupName': groupName});
    this.editIndex = this.commonValues.lastQuestionId - 1;
    this.autoFocus();
    this._createQuestionnaireService.updateSelectedQuestionId.next(this.editIndex);
  }
  /**
   * @param  {} optionId
   * removes an option matching the optionNumber from QuestionnaireOptions
   */
  removeOption(option) {
    if (option.AC_TYPE === undefined) {
      this.questionnaire.deleteList.option.push(option.QUESTION_OPTION_ID);
      this.commonValues.isQuestionEdited = true;
    }
    _.remove( this.questionnaire.options, { 'QUESTION_OPTION_ID': option.QUESTION_OPTION_ID });
    const matchingCondtion = _.find( this.questionnaire.conditions,
                             { 'CONDITION_VALUE': option.OPTION_LABEL, 'QUESTION_ID': option.QUESTION_ID });
    if (matchingCondtion) {
      this.removeCondition(matchingCondtion);
    }
  }
  /**
   * @param  {} conditionId
   * removes a condition matching the conditionId
   */
  removeCondition(condition) {
    if (condition.AC_TYPE === undefined) {
      this.commonValues.isQuestionEdited = true;
      this.questionnaire.deleteList.condition.push(condition.QUESTION_CONDITION_ID);
    }
    const matchingQuestions: any = _.filter(this.questionnaire.questions, { 'GROUP_NAME': condition.GROUP_NAME });
    if (matchingQuestions) {
      _.forEach(matchingQuestions , (question) => { this.removeQuestion(question); });
    }
    _.remove( this.questionnaire.conditions, { 'QUESTION_CONDITION_ID': condition.QUESTION_CONDITION_ID });
  }
  /**
   * @param  {} questionid
   * Removes all options for a given questionid from QuestionnaireOptions
   */
  removeQuestionOptions(questionId) {
    _.remove( this.questionnaire.options, { 'QUESTION_ID': questionId });
  }
   /**
   * @param  {} questionId
   * removes all conditions for the a given questionId
   */
  removeQuestionConditions(questionId) {
    _.remove( this.questionnaire.conditions, { 'QUESTION_ID': questionId });
  }

  removeQuestion(question) {
    this.editIndex = null;
    if (question.AC_TYPE === undefined) {
      this.commonValues.isQuestionEdited = true;
     this.questionnaire.deleteList.question.push(question.QUESTION_ID);
    }
    _.remove( this.questionnaire.questions, { 'QUESTION_ID': question.QUESTION_ID });
    this.removeQuestionConditions(question.QUESTION_ID);
    this.removeQuestionOptions(question.QUESTION_ID);
    const childQuestionList: any = _.filter( this.questionnaire.questions, {'PARENT_QUESTION_ID': question.QUESTION_ID});
    _.forEach( childQuestionList, (childQuestion) => { this.removeQuestion(childQuestion); });
    this._createQuestionnaireService.updateTree.next({});
  }
  /**
   * @param  {} questionId
   * returns an array of options matching the given questionid
   */
  getOptions(questionId) {
    return _.filter( this.questionnaire.options, {'QUESTION_ID': questionId});
  }
  /**
   * @param  {} questionId
   * @param  {} questionType
   * for a given question looks for enabling braching .returns true if the length of matching
   * conditions is less than matching option for the given questionid or ceratin types which does not have options
   */
  enableBraching(questionId, questionType) {
    if (questionType === 'Text' || questionType === 'Textarea') {
      return true;
    } else {
      const optionLength    = _.filter( this.questionnaire.options, {'QUESTION_ID': questionId}).length;
      const conditionLength = _.filter( this.questionnaire.conditions, {'QUESTION_ID': questionId}).length + 1;
      if ( optionLength >= conditionLength || conditionLength === 0) {
        return true;
      } else {
        return false;
      }
    }
  }
  /**
   * @param  {} questionType
   * @param  {} questionId
   * updates the questionnaireOptions for change in questionType.removes the existing questions and updates with default options
   */
  changeQuestionType(questionType, questionId , index) {
    this.commonValues.isQuestionEdited = true;
    this.questionnaire.questions[index].HAS_CONDITION = null;
    const options = _.filter( this.questionnaire.options, {'QUESTION_ID': questionId});
    _.forEach( options, (option) => { this.removeOption(option); });
    if (questionType === 'Radio' || questionType === 'Checkbox') {
      this.addOption(questionId, null);
    }
    if (questionType === 'Y/N') {
      ['Yes', 'No'].forEach(element => {this.addOption(questionId, element); });
    }
    if (questionType === 'Y/N/NA') {
      ['Yes', 'No' , 'N/A'].forEach(element => { this.addOption(questionId, element); });
    }
  }
  /**
   * @param  {} deleteData - data to be deleted
   * @param  {} deleteTypeFlag - who intiated the delete
   * a single modal is used for all delete conformation. selected data to delete  and from where flag is set here
   */
  setDeleteData(deleteData, deleteTypeFlag) {
    this.toDeleteData   = deleteData;
    this.deleteTypeFlag = deleteTypeFlag;
  }
   /**
    *if the user confirms the delete and deletes accroding to the type(option,question or condition)
   */
  deleteAttribute() {
    if (this.deleteTypeFlag === 'question') {
      this.removeQuestion(this.toDeleteData);
    } else if ( this.deleteTypeFlag === 'condition') {
      this.removeCondition(this.toDeleteData);
    } else if ( this.deleteTypeFlag === 'option') {
      this.removeOption(this.toDeleteData);
    }
    this.toDeleteData   = {};
    this.deleteTypeFlag = null;
  }
  /**
   * @param  {} index
   * set actype to u if user changes
   */
  updateOptionACType(index) {
    this.commonValues.isQuestionEdited = true;
    if (this.questionnaire.options[index].AC_TYPE === undefined) {
      this.questionnaire.options[index].AC_TYPE = 'U';
    }
  }
  /**
   * @param  {} index
   * set actype to u if user changes
   */
  updateConditionACType(index) {
    this.commonValues.isQuestionEdited = true;
    if (this.questionnaire.conditions[index].AC_TYPE === undefined) {
      this.questionnaire.conditions[index].AC_TYPE = 'U';
    }
  }
  changeEditQuestion(questionId, id) {
    this.editIndex = questionId;
    this._createQuestionnaireService.updateSelectedQuestionId.next(questionId);
    this.scrollAnimate();
    setTimeout(() => {
      const el = document.getElementById(id).focus();
    }, 100);

  }
}
