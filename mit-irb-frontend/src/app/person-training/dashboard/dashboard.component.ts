import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { PiElasticService } from '../../common/service/pi-elastic.service';
import { PersonTrainingService } from '../person-training.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  personType = 'employee';
  elasticPlaceHolder: string;
  trainingName: string;
  options: any = {};
  userDTO: any = {};
  selectedPerson: any = {};
  personDetail: any = {};
  IRBUtilVO: any = {};
  trainingSearchResult = [];
  personTrainingList = [];
  clearField: any = 'true';
  isTrainingSearch = false;
  showPopup = false;
  requestObject = {
    searchMode: 'L',
    trainingCode: null,
    personId: '',
  };

  constructor(private _elasticsearchService: PiElasticService, private _personTrainingService: PersonTrainingService,
  private _activatedRoute: ActivatedRoute, private _router: Router) {
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.options.type = 'person';
    this.options.size = 20;
    this.options.contextField = 'full_name';
    this.options.debounceTime = 500;
    this.options.theme = '#a31f34';
    this.options.width = '100%';
    this.options.fontSize = '16px';
    this.options.defaultValue = '';
    this.options.formatString = 'full_name';
    this.options.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit: {}

    };
    this.elasticPlaceHolder = 'Search for an Employee Name';
  }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    console.log(this.userDTO);
    this.loadTrainingList();
  }
  loadTrainingList() {
    this._personTrainingService.loadPersonTrainingList(this.requestObject).subscribe( data => {
      this.IRBUtilVO = data;
      this.personTrainingList = this.IRBUtilVO.personTrainingList;
    });
  }
  changePersonType(personType) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    if (personType === 'employee') {
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
      this.elasticPlaceHolder = 'Search for an Employee Name';

    } else {
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';

  }
}
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.requestObject.personId = this.selectedPerson.person_id;

  }
  getTrainingList() {
    this._personTrainingService.loadTrainingList(this.trainingName).subscribe(
      (data: any) => {
        this.trainingSearchResult = data.trainingDesc;
      });
  }
  selectedTraining(description, typeCode) {
   this.requestObject.trainingCode = typeCode;
   this.trainingName = description;
  }

  getAdvanceSearchResult() {
    this.requestObject.searchMode = 'A';
    this.loadTrainingList();

  }
  clearAdvancedSearch() {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.selectedPerson = {};
    this.requestObject.trainingCode = null;
    this.requestObject.searchMode = 'L';
    this.requestObject.personId = '';
    this.trainingName = '';
    this.loadTrainingList();
  }
  showTrainingDetails(trainingDetail, mode) {
    if ( mode === 'VIEW') {
      this._router.navigate( ['/irb/training-maintenance/person-detail'],
      { queryParams: { personTrainingId: trainingDetail.PERSON_TRAINING_ID,
        mode: 'view' } } );

    } else {
      this._router.navigate( ['/irb/training-maintenance/person-detail'],
      { queryParams: { personTrainingId: trainingDetail.PERSON_TRAINING_ID,
        mode: 'edit' } } );
    }

  }
  deleteTrainingDetails(trainingDetail) {
    this.personDetail = trainingDetail;
    this.showPopup = true;

}
confirmDelete() {
 this.IRBUtilVO.personTraining.PERSON_TRAINING_ID = this.personDetail.PERSON_TRAINING_ID;
 this.IRBUtilVO.personTraining.UPDATE_USER = this.userDTO.userName;
 this._personTrainingService.updatePersonTraining(this.IRBUtilVO).subscribe((data: any) => {
  this.IRBUtilVO = data;
  this.personTrainingList = this.IRBUtilVO.personTrainingList;
 });
  }

}
