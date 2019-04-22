import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { PiElasticService } from '../../../common/service/pi-elastic.service';

@Component({
  selector: 'app-administrator-contact',
  templateUrl: './administrator-contact.component.html',
  styleUrls: ['./administrator-contact.component.css']
})
export class AdministratorContactComponent implements OnInit {

  userDTO: any = {};
  commonVo: any = {};
  options: any = {};
  protocolUnit: any = {};
  selectedPerson = {};
  personType = 'employee';
  elasticPlaceHolder: string;
  clearField: any = 'true';
  protocolId = null;
  protocolNumber = null;
  isGeneralInfoSaved = false;
  showPersonElasticBand = false;
  isAdminContactEdit = false;

  constructor(private _elasticsearchService: PiElasticService, private _activatedRoute: ActivatedRoute) {
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
    this.options.formatString = 'full_name | email_address | home_unit';
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
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });
  }
  changePersonType(type) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.showPersonElasticBand = false;
    if (type === 'employee') {
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
      this.elasticPlaceHolder = 'Search for an Employee Name';
    //   this.options.contextField = 'full_name';
    //   this.options.formatString =
    //   'full_name | email_address | home_unit';
    //   this.options.fields = {
    //   full_name: {},
    //   first_name: {},
    //   user_name: {},
    //   email_address: {},
    //   home_unit: {}
    // };

    } else {
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';
    //   this.options.contextField = 'full_name';
    //   this.options.formatString =
    //   'full_name | email_address | home_unit';
    //   this.options.fields = {
    //   full_name: {},
    //   first_name: {},
    //   user_name: {},
    //   email_address: {},
    //   home_unit: {}
    // };
    }
  }
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.showPersonElasticBand = personDetails != null ? true : false;
  }

}
