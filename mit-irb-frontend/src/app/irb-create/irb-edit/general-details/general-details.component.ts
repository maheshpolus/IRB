import { Component, OnInit, AfterViewInit, NgZone, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { CompleterService, CompleterData } from 'ng2-completer';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { NgxSpinnerService } from 'ngx-spinner';
import { ISubscription } from 'rxjs/Subscription';


import { IrbCreateService } from '../../irb-create.service';
import { PiElasticService } from '../../../common/service/pi-elastic.service';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { KeyPressEvent } from '../../../common/directives/keyPressEvent.component';

@Component({
  selector: 'app-general-details',
  templateUrl: './general-details.component.html',
  styleUrls: ['./general-details.component.css']
})
export class GeneralDetailsComponent implements OnInit, AfterViewInit, OnDestroy {
  personSearchText: FormControl = new FormControl('');
  _results: Subject<Array<any>> = new Subject<Array<any>>();
  protocolNumber = null;
  protocolId = null;
  protected protocolPersonLeadUnitsCopy: CompleterData;
  requestObject = {};
  userDTO: any = {};
  result: any = {};
  commonVo: any = {};
  generalInfo: any = {};
  personnelInfo: any = {};
  ProtocolPI: any = {};
  protocolSubject: any = {};
  personalDataList = [];
  protocolType = [];
  unitSearchResult = [];
  protocolPersonLeadUnits = [];
  isGeneralInfoSaved = false;
  isElasticResultPerson = false;
  isLeadUnitSearch = false;
  isGeneralInfoEditable = true;
  message = '';
  personType = 'employee';
  warningMessage: string;
  searchString: string;
  remainingLength = 7500;
  private $subscription1: ISubscription;
  invalidData = { invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false };

  constructor(private _irbCreateService: IrbCreateService,
    private _ngZone: NgZone,
    private _completerService: CompleterService,
    private _elasticsearchService: PiElasticService,
    private _sharedDataService: SharedDataService,
    private _activatedRoute: ActivatedRoute,
    private _router: Router,
    private _spinner: NgxSpinnerService,
    public keyPressEvent: KeyPressEvent) { }

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
    this.requestObject = { protocolId: this.protocolId};
    // Look up data
    this.protocolType = this.commonVo.protocolType;
    this.protocolPersonLeadUnits = this.commonVo.protocolPersonLeadUnits;
    this.protocolPersonLeadUnitsCopy = this._completerService.local(this.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
    // Look up Data - End
    this.isGeneralInfoEditable = this.commonVo.protocolRenewalDetails != null ? this.commonVo.protocolRenewalDetails.generalInfo : true;
    this.generalInfo = this.commonVo.generalInfo;
    if (this.generalInfo.prtocolDescription != null) {
      this.remainingChar(this.generalInfo.prtocolDescription.length); // Calculating description length
    }
    this.generalInfo.protocolStartDate = this.commonVo.generalInfo.protocolStartDate != null ?
      this.GetFormattedDateFromString(this.commonVo.generalInfo.protocolStartDate) : null;
    this.commonVo.generalInfo.protocolEndDate = this.commonVo.generalInfo.protocolEndDate != null ?
      this.GetFormattedDateFromString(this.commonVo.generalInfo.protocolEndDate) : null;
    if (this.generalInfo.protocolId == null) {
      this.generalInfo.protocolStatusCode = 100;
      this.generalInfo.protocolStatus = { description: 'In Progress', protocolStatusCode: 100 };
    }
    this._sharedDataService.setGeneralInfo(Object.assign({}, this.generalInfo));
    this.personnelInfo = this.commonVo.personnelInfo;
    this.personalDataList = this.commonVo.protocolPersonnelInfoList != null ? this.commonVo.protocolPersonnelInfoList : [];
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
        this.generalInfo.protocolType = { protocolTypeCode: protocolTypeCode, description: type.description };
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

  remainingChar(currentLength) {
    const maxLength = document.getElementById('purposeOfStudy').getAttribute('maxLength');
    this.remainingLength = parseInt(maxLength, 10) - currentLength;
  }

  /** assign values to requestObject on selecting a particular PI from elastic search */
  selectedPiResult(result) {
    this.ProtocolPI.personName = '';
    this.ProtocolPI.personName = result.obj.full_name;
    if (result.obj.person_id !== undefined) {
      this.ProtocolPI.personId = result.obj.person_id;
    }
    this.isElasticResultPerson = false;
  }

  setPiLeadUnit(leadUnitName) {
    this.ProtocolPI.unitNumber = null;
    this.protocolPersonLeadUnits.forEach(personeUnit => {
      if (personeUnit.unitName === leadUnitName) {
        this.ProtocolPI.unitNumber = personeUnit.unitNumber;
        this.ProtocolPI.LeadUnitName = leadUnitName;
      }
    });
  }
  saveGeneralInfo() {
    if (this.validateGeneralInfo()) {
      if (!this.isGeneralInfoSaved) {
        this.isGeneralInfoSaved = true; // Change label of button from 'Proceed' to 'Save'
        this.generalInfo.personnelInfos = [];
        this.generalInfo.personnelInfos[0] = this.personnelInfo;
        this.generalInfo.personnelInfos[0].personName = this.ProtocolPI.personName;
        this.generalInfo.personnelInfos[0].personId = this.ProtocolPI.personId;
        this.generalInfo.personnelInfos[0].primaryTitle = 'Test';
        this.generalInfo.personnelInfos[0].protocolPersonRoleId = 'PI';
        this.generalInfo.personnelInfos[0].protocolPersonRoleTypes = { protocolPersonRoleId: 'PI', description: 'Principal Investigator' };
        this.generalInfo.personnelInfos[0].affiliationTypeCode = 2;
        this.generalInfo.personnelInfos[0].nonEmployeeFlag = 'N';
        this.generalInfo.personnelInfos[0].isObtainedConsent = 'N';
        this.generalInfo.personnelInfos[0].protocolAffiliationTypes = { affiliationTypeCode: 2, description: 'Non-Faculty' };
       // this.generalInfo.personnelInfos[0].protocolLeadUnits = [{ unitNumber: this.ProtocolPI.unitNumber }];
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].protocolPersonLeadUnits = {
        //   unitNumber: this.ProtocolPI.unitNumber,
        //   unitName: this.ProtocolPI.LeadUnitName
        // };
        // this.generalInfo.personnelInfos[0].protocolPersonLeadUnits = {
        //   unitNumber: this.ProtocolPI.unitNumber,
        //   unitName: this.ProtocolPI.LeadUnitName
        // };
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].protocolPersonId = this.ProtocolPI.personName;
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].person_id = this.userDTO.personID;
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].updateTimestamp = new Date();
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].updateUser = this.userDTO.userName;
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].sequenceNumber = 1;
        // this.generalInfo.personnelInfos[0].protocolLeadUnits[0].leadUnitFlag = 'Y';
        this.generalInfo.protocolUnits = [];
        this.generalInfo.protocolUnits[0] = this.commonVo.protocolLeadUnits;
        this.generalInfo.protocolUnits[0].unitTypeCode = '1';
        this.generalInfo.protocolUnits[0].unitType = { unitTypeCode: '1', description: 'Lead Unit' };
        this.generalInfo.protocolUnits[0].unitNumber = this.ProtocolPI.unitNumber;
        this.generalInfo.protocolUnits[0].unitName = this.ProtocolPI.LeadUnitName;
        this.generalInfo.protocolUnits[0].updateUser = this.userDTO.userName;
        this.generalInfo.protocolUnits[0].updateTimestamp = new Date();
      }

      // Formating dates and setting it to transient fileds
      this.generalInfo.aniticipatedStartDate = this.generalInfo.protocolStartDate != null ?
        this.GetFormattedDate(this.generalInfo.protocolStartDate) : null;
        this.generalInfo.aniticipatedEndDate = this.generalInfo.protocolEndDate != null ?
        this.GetFormattedDate(this.generalInfo.protocolEndDate) : null;

      this.generalInfo.updateTimestamp = new Date();
      this.generalInfo.updateUser = this.userDTO.userName;
      this.commonVo.generalInfo = this.generalInfo;
      this.requestObject = JSON.stringify(this.commonVo.generalInfo);
      this._spinner.show();
      this._irbCreateService.updateProtocolGeneralInfo(this.commonVo).subscribe(
        data => {
          this.result = data;
          this._spinner.hide();
          if (this.protocolNumber == null) {
            this.personalDataList = this.result.generalInfo.personnelInfos;
            this.personnelInfo = {};
          }
          this.protocolNumber = this.result.generalInfo.protocolNumber;
          this._router.navigate(['/irb/irb-create/irbHome'], {
            queryParams: {
              protocolNumber: this.result.generalInfo.protocolNumber,
              protocolId: this.result.generalInfo.protocolId
            }
          });
          this.commonVo.generalInfo = this.result.generalInfo;
          this.commonVo.protocolPersonnelInfoList = this.result.generalInfo.personnelInfos;
          this.commonVo.protocolLeadUnitsList = this.result.generalInfo.protocolUnits;
          this.commonVo.personnelInfo = {};
          this.commonVo.protocolLeadUnits = {};
          this._sharedDataService.setCommonVo(this.commonVo);
          this.generalInfo = this.result.generalInfo;
          this._sharedDataService.setGeneralInfo(this.generalInfo);
        });
    }
  }
  validateGeneralInfo() {
    if (this.generalInfo.protocolTypeCode !== null && this.generalInfo.protocolTypeCode !== 'null' &&
      this.generalInfo.protocolTitle !== null && this.generalInfo.protocolTitle !== '' &&
      this.invalidData.invalidStartDate === false && ((this.ProtocolPI.personId != null
      && this.ProtocolPI.personId !== undefined && this.ProtocolPI.unitNumber !== undefined
       && this.ProtocolPI.unitNumber != null) || this.isGeneralInfoSaved === true) && this.invalidData.invalidEndDate === false) {
      this.invalidData.invalidGeneralInfo = false;
      return true;
    } else {
      this.invalidData.invalidGeneralInfo = true;
      if (this.invalidData.invalidStartDate === true) {
        this.warningMessage = 'Start Date should be less than End Date';
      } else if (this.invalidData.invalidEndDate === true) {
        this.warningMessage = 'End Date should be greater than Start Date';
      } else {
        this.warningMessage = 'Please fill all mandatory fields marked <strong>*</strong>';
      }
      return false;
    }
  }

  /**
   * @param  {} currentDate - date to be conveted
   * convert the date to string with format mm-dd-yyyy
   */
  GetFormattedDate(currentDate) {
    const month = currentDate.getMonth() + 1;
    const day = currentDate.getDate();
    const year = currentDate.getFullYear();
    return month + '-' + day + '-' + year;
  }
  /**
   * @param  {} currentDate - input in format yyyy-mon-dd
   */
  GetFormattedDateFromString(currentDate) {
    if (typeof currentDate === 'string' || currentDate instanceof String) {
      const res = currentDate.split('-');
      const year = parseInt(res[0], 10);
      const month = parseInt(res[1], 10);
      const day = parseInt(res[2], 10);
      return new Date(year, month - 1, day);
    } else  {
      return currentDate;
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
        const results: Array<any> = [];
        let personName: string;
        let person_id: string;
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
                person_id = hits_source[j].person_id;
                test = hits_source[j];
                if (hits_highlight[j] !== undefined && hits_highlight[j].first_name !== undefined) {
                  personName = hits_highlight[j].first_name;
                }
                if (hits_highlight[j] !== undefined && hits_highlight[j].person_id !== undefined) {
                  person_id = hits_highlight[j].person_id;
                }
                if (hits_highlight[j] !== undefined && hits_highlight[j].full_name !== undefined) {
                  personName = hits_highlight[j].full_name;
                }
                results.push({
                  label: personName + ' | ' + person_id,
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

  getUnitList() {
    this.searchString = this.ProtocolPI.LeadUnitName;
    this._irbCreateService.loadHomeUnits(this.searchString).subscribe(
      (data: any) => {
        this.unitSearchResult = data.homeUnits;
      });
  }
  selectedUnit(unitName, unitNumber) {
    this.ProtocolPI.unitNumber = null;
    this.ProtocolPI.LeadUnitName = unitName;
    this.ProtocolPI.unitNumber = unitNumber;
    this.isLeadUnitSearch = false;
  }
}
