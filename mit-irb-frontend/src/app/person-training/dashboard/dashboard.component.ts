import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

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
  paginatedTrainingList = [];
  clearField: any = 'true';
  isTrainingSearch = false;
  showPopup = false;
  requestObject = {
    searchMode: 'L',
    trainingCode: null,
    personId: '',
  };
  paginationData = {
    limit: 10,
    page_number: 1,
  };
  trainingCount: number;

  constructor(private _elasticsearchService: PiElasticService, private _personTrainingService: PersonTrainingService,
    private _activatedRoute: ActivatedRoute, private _router: Router, private _spinner: NgxSpinnerService) {
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
    this.loadTrainingList();
  }

  /* load the training list */
  loadTrainingList() {
    this._spinner.show();
    this.trainingCount = 0;
    this._personTrainingService.loadPersonTrainingList(this.requestObject).subscribe(data => {
      this._spinner.hide();
      this.IRBUtilVO = data;
      this.personTrainingList = this.IRBUtilVO.personTrainingList == null ? [] : this.IRBUtilVO.personTrainingList;
      this.trainingCount = this.personTrainingList.length;
      this.paginatedTrainingList = this.personTrainingList.slice(0, this.paginationData.limit);
    });
  }

  /**
   * @param  {} type- Change the elastic based on type
   */
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

  /**
   * @param  {} personDetails - person choosen from the elastic search
   */
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    if (this.selectedPerson != null) {
      this.requestObject.personId = this.options.type === 'person' ? personDetails.person_id : personDetails.rolodex_id;
    } else {
      this.requestObject.personId = null;
    }

  }

   /* get the training list on each key press */
  getTrainingList() {
    this._personTrainingService.loadTrainingList(this.trainingName).subscribe(
      (data: any) => {
        this.trainingSearchResult = data.trainingDesc;
      });
  }

  /**
   * @param  {} description - name of the training selecetd
   * @param  {} typeCode - type code of the training selected
   */
  selectedTraining(description, typeCode) {
    this.requestObject.trainingCode = typeCode;
    this.trainingName = description;
  }

  /* load the training details on advanced search */
  getAdvanceSearchResult() {
    this.requestObject.searchMode = 'A';
    this.loadTrainingList();

  }

  /* Clear the advanced search parameters */
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

  /**
   * @param  {} trainingDetail - training object viewed or edited
   * @param  {} mode- species whether in edit or view mode
   */
  showTrainingDetails(trainingDetail, mode) {
    if (mode === 'VIEW') {
      this._router.navigate(['/irb/training-maintenance/person-detail'],
        {
          queryParams: {
            personTrainingId: trainingDetail.PERSON_TRAINING_ID,
            mode: 'view'
          }
        });

    } else {
      this._router.navigate(['/irb/training-maintenance/person-detail'],
        {
          queryParams: {
            personTrainingId: trainingDetail.PERSON_TRAINING_ID,
            mode: 'edit'
          }
        });
    }

  }

  /**
   * @param  {} trainingDetail - training object to be deleted
   */
  deleteTrainingDetails(trainingDetail) {
    this.personDetail = trainingDetail;
    this.showPopup = true;

  }

  /* delete the training if it is confirmed */
  confirmDelete() {

    this.IRBUtilVO.personTraining = {
      acType: 'D',
      personTrainingID: this.personDetail.PERSON_TRAINING_ID, updateUser: this.userDTO.userName
    };
    this._spinner.show();
    this._personTrainingService.updatePersonTraining(this.IRBUtilVO).subscribe((data: any) => {
      this._spinner.hide();
      this.IRBUtilVO = data;
      this.personTrainingList = this.IRBUtilVO.personTrainingList;
      this.paginatedTrainingList = this.personTrainingList.slice(this.paginationData.page_number * this.paginationData.limit 
        - this.paginationData.limit, this.paginationData.page_number * this.paginationData.limit);
    });
  }

  /**
   * @param  {} pageNumber - page selected to paginate
   */
  trainingListPerPage(pageNumber) {
    this.paginatedTrainingList = this.personTrainingList.slice(pageNumber * this.paginationData.limit - this.paginationData.limit,
      pageNumber * this.paginationData.limit);
    document.getElementById('topOfTrainingList').scrollTop = 0;
  }

}

