import { Component, OnInit, AfterViewInit, NgZone } from '@angular/core';
import { FormControl } from '@angular/forms';
import { CompleterService, CompleterData } from 'ng2-completer';
import { Router, ActivatedRoute} from '@angular/router';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import { IrbCreateService } from '../irb-create.service';
import { PiElasticService } from '../../common/service/pi-elastic.service';
import {SharedDataService} from '../../common/service/shared-data.service';
import { IrbViewService } from '../../irb-view/irb-view.service';

@Component({
  selector: 'app-irb-edit',
  templateUrl: './irb-edit.component.html',
  styleUrls: ['./irb-edit.component.css'],
  providers: [IrbCreateService]
})


export class IrbEditComponent implements OnInit, AfterViewInit {
  personSearchText: FormControl = new FormControl('');
  message = '';
  _results: Subject<Array<any>> = new Subject<Array<any>>();
  personalInfo = {};
  personalDataList = [];
  personalInfoSelectedRow: number;
  protected protocolPersonLeadUnitsCopy: CompleterData;
  isPersonalInfoEdit = false;
  isGeneralInfoSaved = false;
  result: any = {};
  commonVo: any = {};
  protocolType = [];
  affiliationTypes = [];
  protected collaboratorNames: CompleterData;
  protocolFundingSourceTypes = [];
  protocolPersonLeadUnits = [];
  protocolSubjectTypes = [];
  personRoleTypes = [];
  protocolFundingSourceList = [];
  protocolSubjectList = [];
  generalInfo: any = {};
  personnelInfo: any = {};
  fundingSource: any = {};
  protocolSubject: any = {};
  protocolCollaborator: any = {};
  IsElasticResultPerson = false;
  personType = 'employee';
  personPlaceHolder = 'Search Employee Name';
  requestObject = {};
  userDTO: any = {};
  editIndex = null;
  invalidData = {invalidStartDate : false, invalidEndDate : false};
  irbPersonDetailedList: any;
  irbPersonDetailedTraining: any[] = [];
  constructor(private _irbCreateService: IrbCreateService,
     private _ngZone: NgZone,
     private _completerService: CompleterService,
     private _elasticsearchService: PiElasticService,
     private _sharedDataService: SharedDataService,
     private _activatedRoute: ActivatedRoute,
     private _router: Router,
     private _irbViewService: IrbViewService) {}
  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this.loadEditDetails();
  }

  ngAfterViewInit() {
    this.personSearchText
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
  }
  loadEditDetails() {
    this._irbCreateService.getEditDetails(null).subscribe(
      data => {
        this.commonVo = data;
        // Look up Data - start
        this.protocolType = this.commonVo.protocolType;
        this.protocolPersonLeadUnits = this.commonVo.protocolPersonLeadUnits;
        this.personRoleTypes = this.commonVo.personRoleTypes;
        this.protocolPersonLeadUnitsCopy = this._completerService.local(this.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
        this.collaboratorNames = this._completerService.
                                  local(this.commonVo.collaboratorNames, 'organizationName,organizationId', 'organizationName');
        this.affiliationTypes = this.commonVo.affiliationTypes;
        this.protocolFundingSourceTypes = this.commonVo.protocolFundingSourceTypes;
        this.protocolSubjectTypes = this.commonVo.protocolSubjectTypes;
        // Look up Data - End
        this.generalInfo = this.commonVo.generalInfo;
        if (this.generalInfo.protocolId == null) {
          this.generalInfo.protocolStatusCode = 100;
          this.generalInfo.protocolStatus = {description : 'In Progress', protocolStatusCode: 100};
        }
        this.personnelInfo = this.commonVo.personnelInfo;
        this.fundingSource = this.commonVo.fundingSource;
        this.protocolSubject = this.commonVo.protocolSubject;
        // this.protocolCollaborator = this.commonVo.protocolCollaborator;
      });
  }

  validateStartDate() {
    if (this.generalInfo.protocolEndDate !== null && this.generalInfo.protocolEndDate !== undefined) {
      if (this.generalInfo.protocolEndDate < this.generalInfo.protocolStartDate) {
        this.invalidData.invalidStartDate = true;
    } else {
        this.invalidData.invalidStartDate = false;
        this.invalidData.invalidEndDate = false;
      }
    }
  }

  protocolTypeChange(protocolTypeCode) {
    this.protocolType.forEach(type => {
      if (type.protocolTypeCode === protocolTypeCode) {
        this.generalInfo.protocolType = {protocolTypeCode: protocolTypeCode, description: type.description};
      }
    });
  }

  validateEndDate() {
    if (this.generalInfo.protocolStartDate !== null && this.generalInfo.protocolStartDate !== undefined) {
      if (this.generalInfo.protocolEndDate < this.generalInfo.protocolStartDate) {
        this.invalidData.invalidEndDate = true;
      } else {
        this.invalidData.invalidEndDate = false;
        this.invalidData.invalidStartDate = false;
      }
    }
  }

  saveGeneralInfo() {
    if (this.validateGeneralInfo()) {
      if (!this.isGeneralInfoSaved) { // Change label of button from 'Proceed' to 'Save'
      this.isGeneralInfoSaved = true;
    }
    this.generalInfo.updateTimestamp = new Date();
    this.generalInfo.updateUser = localStorage.getItem('userName');
    this.commonVo.generalInfo = this.generalInfo;
      this.requestObject = JSON.stringify(this.commonVo.generalInfo);
      this._irbCreateService.updateProtocolGeneralInfo(this.commonVo).subscribe(
        data => {
           this.result = data;
           this._router.navigate( ['/irb/irb-create/irbHome'], {queryParams: {protocolNumber: this.result.generalInfo.protocolNumber}});
           this.commonVo.generalInfo = this.result.generalInfo;
           this.generalInfo = this.result.generalInfo;
           this._sharedDataService.setGeneralInfo(this.generalInfo);
        });
    }
  }
 validateGeneralInfo() {
    if (this.generalInfo.protocolTypeCode !== null &&
        this.generalInfo.protocolTitle !== null &&
        this.invalidData.invalidStartDate === false &&
        this.invalidData.invalidEndDate === false) {
          return true;
    } else {
      return false;
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
        this._elasticsearchService.personSearch(searchString, this.personType)
          .then((searchResult) => {
            this._ngZone.run(() => {
              hits_source = ((searchResult.hits || {}).hits || [])
                .map((hit) => hit._source);
              hits_highlight = ((searchResult.hits || {}).hits || [])
                .map((hit) => hit.highlight);

              hits_source.forEach((elmnt, j) => {
                personName = hits_source[j].full_name;
                test = hits_source[j];
                if (typeof (hits_highlight[j].first_name) !== 'undefined') {
                  personName = hits_highlight[j].first_name;
                }

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
                if (this.personnelInfo.personName && this.personnelInfo.personName.trim()) {
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

  clearSelectedPIdata() {
    this.personnelInfo.personId = '';
  }


  /** assign values to requestObject on selecting a particular person from elastic search */
  selectedPiResult(result) {
    this.personnelInfo.personName = '';
    this.personnelInfo.personName = result.obj.full_name;
    if (result.obj.person_id !== undefined) {
      this.personnelInfo.personId = result.obj.person_id;
    }
    if (result.obj.rolodex_id !== undefined) {
      this.personnelInfo.personId = result.obj.rolodex_id;
    }
    this.IsElasticResultPerson = false;
  }

  changePersonType(type) {
    if (type === 'employee') {
      this.personPlaceHolder = 'Search Employee Name';
    } else {
      this.personPlaceHolder = 'Search Non-Employee Name';
    }
  }

  setPersonRole() {
    // this.personRoleTypes.forEach(personeRole => {
    //   if (personeRole.protocolPersonRoleId === this.personnelInfo.protocolPersonRoleId) {}
    // });
  }

  editPersonalDetails(selectedItem: any, index: number) {
    this.editIndex = index;
    this.personalInfoSelectedRow = index;
    this.isPersonalInfoEdit = true;
    this.personnelInfo = Object.assign({}, selectedItem) ;
  }
  addPersonalDetails(mode) {
    if (!(this.personnelInfo.personName == null || this.personnelInfo.personName === undefined)  &&
        !(this.personnelInfo.protocolPersonRoleId == null || this.personnelInfo.protocolPersonRoleId === undefined) &&
        !(this.personnelInfo.primaryTitle == null || this.personnelInfo.primaryTitle === undefined) &&
        !(this.personnelInfo.leadUnit == null || this.personnelInfo.leadUnit === undefined) &&
        !(this.personnelInfo.affiliationTypeCode == null || this.personnelInfo.affiliationTypeCode === undefined)) {
        if (mode === 'ADD') {
          this.personalDataList.push({
            id: '0',
            personName: this.personnelInfo.personName,
            protocolPersonRoleId: this.personnelInfo.protocolPersonRoleId,
            primaryTitle: this.personnelInfo.primaryTitle,
            leadUnit: this.personnelInfo.leadUnit,
            affiliationTypeCode: this.personnelInfo.affiliationTypeCode,
            acType: 'U'
          });
          this.personnelInfo.acType = 'U';
        } else if (mode === 'EDIT') {
          this.personnelInfo.acType = 'U';
          this.personalDataList[this.editIndex] = Object.assign({}, this.personnelInfo) ;
        }
        this.savePersonalInfo();
      }
  }

 savePersonalInfo() {
    this.personnelInfo.updateTimestamp = new Date();
    this.personnelInfo.updateUser = localStorage.getItem('userName');
    this.commonVo.personnelInfo = this.personnelInfo;
      this._irbCreateService.updateProtocolPersonInfo(this.commonVo).subscribe(
        data => {
           this.result = data;
           this.commonVo.personnelInfo = this.result.personnelInfo;
           this.personnelInfo = this.result.personnelInfo;
        });
  }
  deletePersonalDetails(index) {
    this.personalInfoSelectedRow = null;
    this.isPersonalInfoEdit = false;
    this.personalDataList.splice(index, 1);
  }


  /**calls service to load person details of protocol */
  loadPersonDetailedList() {
    const params = {protocolNumber: '1004003828', avPersonId: '920366745'};
    this._irbViewService.getIrbPersonDetailedList( params ).subscribe( data => {
         this.result = data || [];
         if ( this.result != null ) {
            this.irbPersonDetailedList = this.result.irbViewProtocolMITKCPersonInfo;
             this.irbPersonDetailedTraining = this.result.irbViewProtocolMITKCPersonTrainingInfo;
         }
    },
        error => {
             console.log( 'Error in method loadPersonDetailedList()', error );
        },
    );
}

addFundingDetails(mode) {
  if (mode === 'ADD') {
    this.protocolFundingSourceList.push({
      fundingSourceTypeCode: this.fundingSource.fundingSourceTypeCode,
      protocolFundingSourceId: this.fundingSource.protocolFundingSourceId,
      protocolFundingDescription: this.fundingSource.protocolFundingSourceId,
      acType: 'I'
    });
    this.fundingSource = {};
  }
}

addSubjectDetails(mode) {
  if (mode === 'ADD') {
    this.protocolSubjectList.push({
      vulnerableSubjectTypeCode: this.protocolSubject.vulnerableSubjectTypeCode,
      subjectCount: this.protocolSubject.subjectCount,
      acType: 'I'
    });
    this.protocolSubject = {};
  }
}
}

