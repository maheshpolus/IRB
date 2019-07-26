import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';

import { CreateQuestionnaireService } from '../services/create.service';
import * as _ from 'lodash';
// import _.filter from 'lodash/filter';
import { Router, ActivatedRoute } from '@angular/router';
import { easeInOUt } from './../services/animations';

@Component({
  selector: 'app-questionnaire-list',
  templateUrl: './questionnaire-list.component.html',
  styleUrls: ['./questionnaire-list.component.css'],
  animations: [easeInOUt]
})
export class QuestionnaireListComponent implements OnInit {
  result: any = {
    questionnaireGroup: [],
    questionnaireList : []
  };
  selectedGroup     = null;
  questionnairelist = [];
  deBounceTimer: any;
  constructor( private _createQuestionnaireService: CreateQuestionnaireService,
               private _spinner: NgxSpinnerService,
               private _router: Router,
               private _activatedRoute: ActivatedRoute ) { }

  ngOnInit() {
    this._createQuestionnaireService.getQuestionnaireList().subscribe(
      data => {
        this.result = data;
        this.questionnairelist = this.result.questionnaireList;
      });
  }
  /**
   * changes the list of questionnaire according to the  selected group
   */
  getquestionnairelist() {
    if (this.selectedGroup === 'null') {
        this.questionnairelist = this.result.questionnaireList;
    } else {
        this.questionnairelist = _.filter( this.result.questionnaireList, {'QUEST_GROUP_TYPE_CODE': this.selectedGroup});
    }
  }
  navigateToCreate(questionnaireId, viewMode) {
    this._spinner.show();
    this._router.navigate(['../create'],
    { queryParams: { id: questionnaireId , viewmode: viewMode} , relativeTo: this._activatedRoute});
  }

  changeSortOrder(index, type) {
     const nextValue = type === 'positive' ?  index - 1 : index + 1;
      const sortOrder = this.result.questionnaireList[index].SORT_ORDER;
      this.result.questionnaireList[index].SORT_ORDER =  this.result.questionnaireList[nextValue].SORT_ORDER;
      this.result.questionnaireList[nextValue].SORT_ORDER = sortOrder;
      [this.result.questionnaireList[index] , this.result.questionnaireList[nextValue] ] =
              [this.result.questionnaireList[nextValue] , this.result.questionnaireList[index] ];
      this.updateQuestionnarireSortOrder();
    }

    updateQuestionnarireSortOrder() {
      clearTimeout(this.deBounceTimer);
      this.deBounceTimer = setTimeout(() => {
        this._createQuestionnaireService.updateQuestionnarireSortOrder(this.result).subscribe(data => {
          this.result = data;
          this.questionnairelist = this.result.questionnaireList;
        });
      }, 1000);
    }
}
