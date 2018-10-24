import { Component, OnInit, AfterViewInit, NgZone, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { ISubscription } from 'rxjs/Subscription';

import { IrbCreateService } from '../../irb-create.service';
import { PiElasticService } from '../../../common/service/pi-elastic.service';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { IrbViewService } from '../../../irb-view/irb-view.service';

@Component({
  selector: 'app-personnel-info',
  templateUrl: './personnel-info.component.html',
  styleUrls: ['./personnel-info.component.css']
})
export class PersonnelInfoComponent implements OnInit, AfterViewInit, OnDestroy {
  personSearchText: FormControl = new FormControl('');
  _results: Subject<Array<any>> = new Subject<Array<any>>();
  protected protocolPersonLeadUnitsCopy: CompleterData;
  personLeadUnit = null;
  protocolId = null;
  protocolNumber = null;
  editIndex = null;
  userDTO: any = {};
  commonVo: any = {};
  generalInfo: any = {};
  personnelInfo: any = {};
  result: any = {};
  personRoleTypes = [];
  protocolPersonLeadUnits = [];
  affiliationTypes = [];
  personalDataList = [];
  irbPersonDetailedTraining: any[] = [];
  message = '';
  personType = 'employee';
  personPlaceHolder = 'Search Employee Name';
  personalInfoSelectedRow: number;
  isElasticResultPerson = false;
  isPersonalInfoEdit = false;
  isGeneralInfoSaved = false;
  irbPersonDetailedList: any;
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false
  };
  private $subscription1: ISubscription;
  private $subscription2: ISubscription;

  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _ngZone: NgZone,
    private _elasticsearchService: PiElasticService,
    private _irbCreateService: IrbCreateService,
    private _irbViewService: IrbViewService,
    private _completerService: CompleterService) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });
    this.$subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.commonVo = commonVo;
        this.loadEditDetails();
      }
    });
    this.$subscription2 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
      if (generalInfo !== undefined) {
        this.generalInfo = generalInfo;
        if (this.generalInfo.personnelInfos != null && this.generalInfo.personnelInfos !== undefined) {
          this.personalDataList = this.generalInfo.personnelInfos;
          this.personnelInfo = {};
        }
      }
    });

  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
    if (this.$subscription2) {
      this.$subscription2.unsubscribe();
    }
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
    this.personRoleTypes = this.commonVo.personRoleTypes;
    this.protocolPersonLeadUnits = this.commonVo.protocolPersonLeadUnits;
    this.protocolPersonLeadUnitsCopy = this._completerService.local(this.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
    this.affiliationTypes = this.commonVo.affiliationTypes;
    this.generalInfo = this.commonVo.generalInfo;
    this.personnelInfo = this.commonVo.personnelInfo;
    this.personalDataList = this.commonVo.protocolPersonnelInfoList != null ? this.commonVo.protocolPersonnelInfoList : [];
  }

  /**fetches elastic search results
       * @param searchString - string enterd in the input field
       */
  getElasticSearchResults(searchString) {
    return new Promise<Array<String>>((resolve, reject) => {
      this._ngZone.runOutsideAngular(() => {
        let hits_source: Array<any> = [];
        let hits_highlight: Array<any> = [];
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

  /* show message if elastic search failed */
  handleError(): any {
    this.message = 'something went wrong';
  }

  clearSelectedPIdata() {
    this.personnelInfo.personId = '';
  }


  /** assign values to requestObject on selecting a particular person from elastic search */
  selectedPersonResult(result) {
    this.personnelInfo.personName = '';
    this.personnelInfo.personName = result.obj.full_name;
    if (result.obj.person_id !== undefined) {
      this.personnelInfo.personId = result.obj.person_id;
    }
    if (result.obj.rolodex_id !== undefined) {
      this.personnelInfo.personId = result.obj.rolodex_id;
    }
    this.isElasticResultPerson = false;
  }

  changePersonType(type) {
    if (type === 'employee') {
      this.personPlaceHolder = 'Search Employee Name';
    } else {
      this.personPlaceHolder = 'Search Non-Employee Name';
    }
  }

  setPersonRole(roleId) {
    this.personRoleTypes.forEach(personeRole => {
      if (personeRole.protocolPersonRoleId === roleId) {
        this.personnelInfo.protocolPersonRoleTypes = { protocolPersonRoleId: roleId, description: personeRole.description };
      }
    });
  }

  setPersonLeadUnit(leadUnitName) {
    if (this.personnelInfo.protocolLeadUnits != null) {
      if (this.personnelInfo.protocolLeadUnits[0].protocolUnitsId != null) {
        this.personnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.personnelInfo.protocolLeadUnits[0].unitNumber = personeUnit.unitNumber;
            this.personnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      } else {
        this.personnelInfo.protocolLeadUnits = null;
        this.personnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.personnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
            this.personnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      }
    } else {
      this.personnelInfo.protocolLeadUnits = null;
      this.personnelInfo.protocolPersonLeadUnits = null;
      this.protocolPersonLeadUnits.forEach(personeUnit => {
        if (personeUnit.unitName === leadUnitName) {
          this.personnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
          this.personnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
        }
      });
    }
  }

  setPersonAffiliation(affiliation) {
    this.affiliationTypes.forEach(personAffiliation => {
      if (personAffiliation.affiliationTypeCode.toString() === affiliation) {
        this.personnelInfo.protocolAffiliationTypes = { affiliationTypeCode: affiliation, description: personAffiliation.description };
      }
    });

  }

  editPersonalDetails(selectedItem: any, index: number) {
    this.editIndex = index;
    this.personalInfoSelectedRow = index;
    this.isPersonalInfoEdit = true;
    this.personnelInfo = Object.assign({}, selectedItem);
    this.personLeadUnit = selectedItem.protocolLeadUnits[0].protocolPersonLeadUnits.unitName;
  }

  addPersonalDetails(mode) {
    if (!(this.personnelInfo.personId == null || this.personnelInfo.personId === undefined) &&
      !(this.personnelInfo.protocolPersonRoleId == null || this.personnelInfo.protocolPersonRoleId === undefined) &&
      !(this.personnelInfo.primaryTitle == null || this.personnelInfo.primaryTitle === undefined) &&
      !(this.personnelInfo.protocolLeadUnits == null || this.personnelInfo.protocolLeadUnits === undefined) &&
      !(this.personnelInfo.protocolAffiliationTypes == null || this.personnelInfo.protocolAffiliationTypes === undefined)) {
      this.invalidData.invalidPersonnelInfo = false;
      this.savePersonalInfo(mode);
    } else {
      this.invalidData.invalidPersonnelInfo = true;
    }
    if (mode === 'EDIT') {
      this.isPersonalInfoEdit = false;
      this.personalInfoSelectedRow = null;
    }
  }

  savePersonalInfo(mode) {
    if (mode !== 'DELETE') {
      this.personnelInfo.acType = 'U';
      this.personnelInfo.updateTimestamp = new Date();
      this.personnelInfo.updateUser = localStorage.getItem('userName');
      this.personnelInfo.sequenceNumber = 1;
      this.personnelInfo.protocolNumber = this.protocolNumber;
      // Setting Person Lead Unit Details
      this.personnelInfo.protocolLeadUnits[0].protocolPersonId = this.personnelInfo.personId;
      this.personnelInfo.protocolLeadUnits[0].protocolNumber = this.protocolNumber;
      this.personnelInfo.protocolLeadUnits[0].person_id = this.userDTO.personID;
      this.personnelInfo.protocolLeadUnits[0].updateTimestamp = new Date();
      this.personnelInfo.protocolLeadUnits[0].updateUser = this.userDTO.userName;
      this.personnelInfo.protocolLeadUnits[0].protocolId = this.generalInfo.protocolId;
      this.personnelInfo.protocolLeadUnits[0].sequenceNumber = 1;
      if (this.personnelInfo.protocolPersonRoleId === 'PI') {
        this.personnelInfo.protocolLeadUnits[0].leadUnitFlag = 'Y';
      } else {
        this.personnelInfo.protocolLeadUnits[0].leadUnitFlag = 'N';
      }
      // End setting Person Lead Unit Details
      this.personnelInfo.protocolGeneralInfo = this.generalInfo;
      this.commonVo.personnelInfo = this.personnelInfo;
    }
    this._irbCreateService.updateProtocolPersonInfo(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.personnelInfo = {};
        this.personLeadUnit = null;
        this.personalDataList = this.result.protocolPersonnelInfoList;
      });
  }
  deletePersonalDetails(index) {
    this.commonVo.personnelInfo = this.personalDataList[index];
    this.commonVo.personnelInfo.acType = 'D';
    this.commonVo.personnelInfo.protocolGeneralInfo = this.generalInfo;
    this.savePersonalInfo('DELETE');
  }


  /*calls service to load person details of protocol */
  loadPersonDetailedList(item) {
    const params = { protocolNumber: this.protocolNumber, avPersonId: item.personId };
    this._irbViewService.getIrbPersonDetailedList(params).subscribe(data => {
      this.result = data || [];
      if (this.result != null) {
        this.irbPersonDetailedList = this.result.irbViewProtocolMITKCPersonInfo;
        this.irbPersonDetailedTraining = this.result.irbViewProtocolMITKCPersonTrainingInfo;
      }
    },
      error => {
        console.log('Error in method loadPersonDetailedList()', error);
      },
    );
  }

}
