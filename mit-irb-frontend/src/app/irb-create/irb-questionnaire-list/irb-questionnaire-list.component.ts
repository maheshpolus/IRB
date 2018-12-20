import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { IrbCreateService } from '../irb-create.service';

@Component({
  selector: 'app-irb-questionnaire-list',
  templateUrl: './irb-questionnaire-list.component.html',
  styleUrls: ['./irb-questionnaire-list.component.css']
})
export class IrbQuestionnaireListComponent implements OnInit {
  protocolNumber = null;
  protocolId = null;
  applicableQuestionnaire = [];
  constructor(private _irbCreateService: IrbCreateService, private _activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolNumber = params['protocolNumber'];
      this.protocolId = params['protocolId'];
    });
    this.getQuetionnaireList();
  }

  getQuetionnaireList() {
    const requestObject = { 'module_item_key': this.protocolNumber, 'module_sub_item_key': 0,
                            'module_item_code': 7, 'module_sub_item_code': 0 };
    this._irbCreateService.getApplicableQuestionnaire(requestObject).subscribe(
      data => {
        const result: any = data;
        this.applicableQuestionnaire = result.applicableQuestionnaire != null ? result.applicableQuestionnaire : [];
      });
  }
}
