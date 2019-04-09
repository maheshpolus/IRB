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
  editPersonLeadUnit = null;
  protocolId = null;
  protocolNumber = null;
  editIndex = null;
  userDTO: any = {};
  commonVo: any = {};
  generalInfo: any = {};
  personnelInfo: any = {};
  editPersonnelInfo: any = {};
  result: any = {};
  selectedPerson: any = {};
  personRoleTypes = [];
  protocolPersonLeadUnits = [];
  affiliationTypes = [];
  personalDataList = [];
  irbPersonDetailedTraining: any[] = [];
  message = '';
  personType = 'employee';
  personPlaceHolder = 'Search for an Employee Name';
  personalInfoSelectedRow: number;
  isElasticResultPerson = false;
  isPersonalInfoEdit = false;
  isGeneralInfoSaved = false;
  showPersonElasticBand = false;
  isObtainedConsent = false;
  isObtainedConsentEdit = false;
  showDeletePopup = false;
  warningMessage: string;
  alertMessage: string;
  irbPersonDetailedList: any;
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false,
    isPersonAdded: false, isPiExists: false, invalidPersonDetails: false
  };
  private $subscription1: ISubscription;

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
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
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
    // this.protocolPersonLeadUnits = this.commonVo.protocolPersonLeadUnits;
   // this.protocolPersonLeadUnitsCopy = this._completerService.local(this.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
    this.affiliationTypes = this.commonVo.affiliationTypes;
    this.generalInfo = Object.assign({}, this.commonVo.generalInfo);
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
        let email: string;
        // let title: string;
        let home_unit: string;
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
                if (hits_highlight[j] !== undefined && typeof (hits_highlight[j].first_name) !== undefined) {
                  personName = hits_highlight[j].first_name;
                }

                if (hits_highlight[j] !== undefined && typeof (hits_highlight[j].full_name) !== undefined) {
                  personName = hits_highlight[j].full_name;
                }
                if (hits_highlight[j] !== undefined && typeof (hits_source[j].email_address) !== undefined) {
                  email = hits_source[j].email_address;
                }
                if (hits_highlight[j] !== undefined && typeof (hits_source[j].home_unit) !== undefined) {
                  home_unit = hits_source[j].home_unit;
                }
                results.push({
                  label: personName + ' | ' + email + ' | ' + home_unit,
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
    // this.personnelInfo.personId = '';
  }


  /** assign values to requestObject on selecting a particular person from elastic search */
  selectedPersonResult(result) {
    this.showPersonElasticBand = true;
    this.selectedPerson = result.obj;
    this.personnelInfo.personName = '';
    this.personnelInfo.personName = result.obj.full_name;
    if (result.obj.person_id !== undefined) {
      this.personnelInfo.personId = result.obj.person_id;
      this.personnelInfo.nonEmployeeFlag = 'N';
    }
    if (result.obj.rolodex_id !== undefined) {
      this.personnelInfo.personId = result.obj.rolodex_id;
      this.personnelInfo.nonEmployeeFlag = 'Y';
    }
    this.personnelInfo.emailAddress = result.obj.email_address;
    this.personnelInfo.primaryTitle = result.obj.title;
    this.isElasticResultPerson = false;
  }

  changePersonType(type) {
    if (type === 'employee') {
      this.personPlaceHolder = 'Search for an Employee Name';
    } else {
      this.personPlaceHolder = 'Search for a Non-Employee Name';
    }
  }

  setPersonRole(roleId, mode) {
    if (roleId === 'PI') {
      this.personalDataList.forEach(personData => {
        if (personData.protocolPersonRoleId === 'PI') {
          this.invalidData.isPiExists = true;
        }
      });
    } else {
      this.invalidData.isPiExists = false;
      this.personRoleTypes.forEach(personeRole => {
        if (personeRole.protocolPersonRoleId === roleId) {
          if (mode === 'ADD') {
            this.personnelInfo.protocolPersonRoleTypes = { protocolPersonRoleId: roleId, description: personeRole.description };
          } else if (mode === 'EDIT') {
            this.editPersonnelInfo.protocolPersonRoleTypes = { protocolPersonRoleId: roleId, description: personeRole.description };
          }

        }
      });
    }
  }

  setPersonLeadUnit(leadUnitName) {
    if (this.personnelInfo.protocolLeadUnits != null) {
      if (this.personnelInfo.protocolLeadUnits[0].protocolUnitsId != null) {
        this.personnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.personnelInfo.protocolLeadUnits[0].unitNumber = personeUnit.unitNumber;
            this.personnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = {
               unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      } else {
        this.personnelInfo.protocolLeadUnits = null;
        this.personnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.personnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
            this.personnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = {
               unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      }
    } else {
      this.personnelInfo.protocolLeadUnits = null;
      this.personnelInfo.protocolPersonLeadUnits = null;
      this.protocolPersonLeadUnits.forEach(personeUnit => {
        if (personeUnit.unitName === leadUnitName) {
          this.personnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
          this.personnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
        }
      });
    }
  }

  setPersonLeadUnitEdit(leadUnitName) {
    if (this.editPersonnelInfo.protocolLeadUnits != null) {
      if (this.editPersonnelInfo.protocolLeadUnits[0].protocolUnitsId != null) {
        this.editPersonnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.editPersonnelInfo.protocolLeadUnits[0].unitNumber = personeUnit.unitNumber;
            // this.editPersonnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = {
            //                             unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
            this.editPersonnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      } else {
        this.editPersonnelInfo.protocolLeadUnits = null;
        this.editPersonnelInfo.protocolPersonLeadUnits = null;
        this.protocolPersonLeadUnits.forEach(personeUnit => {
          if (personeUnit.unitName === leadUnitName) {
            this.editPersonnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
            // this.editPersonnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = {
            //                         unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
            this.editPersonnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          }
        });
      }
    } else {
      this.editPersonnelInfo.protocolLeadUnits = null;
      this.editPersonnelInfo.protocolPersonLeadUnits = null;
      this.protocolPersonLeadUnits.forEach(personeUnit => {
        if (personeUnit.unitName === leadUnitName) {
          this.editPersonnelInfo.protocolLeadUnits = [{ unitNumber: personeUnit.unitNumber }];
          // this.editPersonnelInfo.protocolLeadUnits[0].protocolPersonLeadUnits = {
          //   unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
          this.editPersonnelInfo.protocolPersonLeadUnits = { unitNumber: personeUnit.unitNumber, unitName: leadUnitName };
        }
      });
    }
  }

  setPersonAffiliation(affiliation, mode) {
    this.affiliationTypes.forEach(personAffiliation => {
      if (personAffiliation.affiliationTypeCode.toString() === affiliation) {
        if (mode === 'ADD') {
          this.personnelInfo.protocolAffiliationTypes = { affiliationTypeCode: affiliation, description: personAffiliation.description };
        } else if (mode === 'EDIT') {
          this.editPersonnelInfo.protocolAffiliationTypes = {
             affiliationTypeCode: affiliation, description: personAffiliation.description };
        }

      }
    });

  }

  editPersonalDetails(selectedItem: any, index: number) {
    this.editIndex = index;
    this.personalInfoSelectedRow = index;
    this.isPersonalInfoEdit = true;
    this.editPersonnelInfo = Object.assign({}, selectedItem);
    this.isObtainedConsentEdit = this.editPersonnelInfo.isObtainedConsent === 'Y' ? true : false;
    // this.editPersonLeadUnit = selectedItem.protocolLeadUnits[0].protocolPersonLeadUnits.unitName;
  }
  addPersonalDetails(personnelInfo, mode) {
    if (!(personnelInfo.personId == null || personnelInfo.personId === undefined) &&
      !(personnelInfo.protocolPersonRoleId == null || personnelInfo.protocolPersonRoleId === undefined) &&
      !(personnelInfo.protocolAffiliationTypes == null || personnelInfo.protocolAffiliationTypes === undefined)
        && !this.invalidData.isPiExists) {
        this.invalidData.invalidPersonnelInfo = false;
        if (mode !== 'EDIT') {
        this.personalDataList.forEach(personData => {
          if (personData.personId === personnelInfo.personId) {
            this.invalidData.invalidPersonnelInfo = true;
            this.warningMessage = 'Person already added please choose a different person';
          }

        });
        personnelInfo.isObtainedConsent = this.isObtainedConsent === true  ? 'Y' : 'N';
        if (!this.invalidData.invalidPersonnelInfo) {
        this.showPersonElasticBand = false;
          this.savePersonalInfo(personnelInfo, mode);
        }
      } else {
          this.isPersonalInfoEdit = false;
          this.personalInfoSelectedRow = null;
          personnelInfo.isObtainedConsent = this.isObtainedConsentEdit === true  ? 'Y' : 'N';
          this.savePersonalInfo(personnelInfo, mode);
      }
    } else {
      this.invalidData.invalidPersonnelInfo = true;
      if (this.invalidData.isPiExists) {
        this.warningMessage = 'PI already exists please choose a different role';
      } else {
      this.warningMessage = 'Please fill all mandatory fields marked <strong>*</strong>';
      }
    }
  }

  savePersonalInfo(personnelInfo, mode) {
    if (mode !== 'DELETE') {
      personnelInfo.acType = 'U';
      personnelInfo.updateTimestamp = new Date();
      personnelInfo.updateUser = localStorage.getItem('userName');
      personnelInfo.sequenceNumber = 1;
      personnelInfo.protocolNumber = this.protocolNumber;
      // Setting Person Lead Unit Details
      // personnelInfo.protocolLeadUnits[0].protocolPersonId = this.personnelInfo.personId;
      // personnelInfo.protocolLeadUnits[0].protocolNumber = this.protocolNumber;
      // personnelInfo.protocolLeadUnits[0].person_id = this.userDTO.personID;
      // personnelInfo.protocolLeadUnits[0].updateTimestamp = new Date();
      // personnelInfo.protocolLeadUnits[0].updateUser = this.userDTO.userName;
      // personnelInfo.protocolLeadUnits[0].protocolId = this.protocolId;
      // personnelInfo.protocolLeadUnits[0].sequenceNumber = 1;
      // if (personnelInfo.protocolPersonRoleId === 'PI') {
      //   personnelInfo.protocolLeadUnits[0].leadUnitFlag = 'Y';
      // } else {
      //   personnelInfo.protocolLeadUnits[0].leadUnitFlag = 'N';
      // }
      // End setting Person Lead Unit Details
     // personnelInfo.protocolGeneralInfo = this.generalInfo;
      this.commonVo.personnelInfo = personnelInfo;
     // this.commonVo.generalInfo.personnelInfos[0] = personnelInfo; //added newly
    }
    this._irbCreateService.updateProtocolPersonInfo(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.personnelInfo = {};
        this.personnelInfo.affiliationTypeCode = null;
        this.personnelInfo.protocolPersonRoleId = null;
        this.isObtainedConsent = false;
        this.invalidData.isPersonAdded = false;
        this.editPersonnelInfo = {};
        this.personLeadUnit = null;
        this.personnelInfo.personId = null;
        this.editPersonLeadUnit = null;
        this.personalDataList = this.result.protocolPersonnelInfoList;
        this.generalInfo.personnelInfos = Object.assign([], this.result.protocolPersonnelInfoList);
        this._sharedDataService.setGeneralInfo(this.generalInfo);
      });
  }
  deletePersonalDetails(index) {
    this.commonVo.personnelInfo = this.personalDataList[index];
    this.commonVo.personnelInfo.acType = 'D';
  //  this.commonVo.personnelInfo.protocolGeneralInfo = this.generalInfo;
    this.showDeletePopup = true;
    if (this.commonVo.personnelInfo.protocolPersonRoleId === 'PI') {
      this.alertMessage = 'You are going to delete the PI. Are you sure you want to delete this item?';
    } else {
      this.alertMessage = 'Are you sure you want to delete this item?';
    }
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
