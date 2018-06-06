import { Component, OnInit } from '@angular/core';
import { DatePipe } from "@angular/common";

@Component({
  selector: 'app-exempt-questionaire',
  templateUrl: './exempt-questionaire.component.html',
  styleUrls: ['./exempt-questionaire.component.css']
})
export class ExemptQuestionaireComponent implements OnInit {
  sortOrder = '1';
  sortField = 'date';
  direction: number;
  column: any;
  questions = [{
      "questionId":"1",
      "question": "Does your study involve research?",
      "date": "12/12/2015"
  },
  {
      "questionId":"2",
      "question": "Does your research involve the use of human subject(s)?",
      "date": "06/12/2017"
  },
  {
      "questionId":"3",
      "question": "Does your research include prisoners as subjects?",
      "date": "12/10/2015"
  }]
    
  constructor(public datepipe: DatePipe) { }

  ngOnInit() {debugger; 
      this.sortBy();
  }
  sortBy() {
      this.questions.forEach((value,key)=>
      this.questions[key].date = this.datepipe.transform(this.questions[key].date, 'yyyy/MM/dd'))
      this.column = this.sortField;
      this.direction = parseInt( this.sortOrder, 10 );
  }
}
