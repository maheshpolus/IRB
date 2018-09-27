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
  protocolNumber = null;
  protocolId = null;
  personalInfo = {};
  personalDataList = [];
  personalInfoSelectedRow: number;
  protected protocolPersonLeadUnitsCopy: CompleterData;
  isPersonalInfoEdit = false;
  isFundingInfoEdit = false;
  isSubjectInfoEdit = false;
  isCollaboratorInfoEdit = false;
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
  protocolCollaboratorList = [];
  generalInfo: any = {};
  personnelInfo: any = {};
  fundingSource: any = {};
  protocolSubject: any = {};
  protocolCollaborator: any = {};
  personLeadUnit = null;
  collaboratorName = null;
  IsElasticResultPerson = false;
  personType = 'employee';
  personPlaceHolder = 'Search Employee Name';
  requestObject = {};
  userDTO: any = {};
  editIndex = null;
  fundingEditIndex = null;
  collaboratorEditIndex = null;
  subjectEditIndex = null;
  invalidData = {invalidGeneralInfo: false, invalidStartDate : false, invalidEndDate : false,
                 invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
                 invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false};
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
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
  });
  this._sharedDataService.setGeneralInfo({});
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
    this.requestObject = { protocolId: this.protocolId};
    this._irbCreateService.getEditDetails(this.requestObject).subscribe(
      data => {
        this.commonVo = data;
        // Look up Data - start
        this.protocolType = this.commonVo.protocolType;
        this.personRoleTypes = this.commonVo.personRoleTypes;
        this.protocolPersonLeadUnits = this.commonVo.protocolPersonLeadUnits;
        this.protocolPersonLeadUnitsCopy = this._completerService.local(this.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
        this.collaboratorNames = this._completerService.
                                  local(this.commonVo.collaboratorNames, 'organizationName,organizationId', 'organizationName');
        this.affiliationTypes = this.commonVo.affiliationTypes;
        this.protocolFundingSourceTypes = this.commonVo.protocolFundingSourceTypes;
        this.protocolSubjectTypes = this.commonVo.protocolSubjectTypes;
        // Look up Data - End
        this.generalInfo = this.commonVo.generalInfo;
        this.generalInfo.protocolStartDate =
        this.commonVo.generalInfo.protocolStartDate != null ? new Date(this.commonVo.generalInfo.protocolStartDate) : null;
        this.commonVo.generalInfo.protocolEndDate =
        this.commonVo.generalInfo.protocolEndDate != null ? new Date(this.commonVo.generalInfo.protocolEndDate) : null;
        if (this.generalInfo.protocolId == null) {
          this.generalInfo.protocolStatusCode = 100;
          this.generalInfo.protocolStatus = {description : 'In Progress', protocolStatusCode: 100};
        }
       // this.generalInfo.attachmentProtocols = [];
        this._sharedDataService.setGeneralInfo( Object.assign({}, this.generalInfo));
        this.personnelInfo = this.commonVo.personnelInfo;
        this.fundingSource = this.commonVo.fundingSource;
        this.protocolSubject = this.commonVo.protocolSubject;
        this.protocolCollaborator = this.commonVo.protocolCollaborator != null ? this.commonVo.protocolCollaborator : {};
        this.personalDataList = this.commonVo.protocolPersonnelInfoList != null ? this.commonVo.protocolPersonnelInfoList : [];
        this.protocolFundingSourceList = this.commonVo.protocolFundingSourceList != null ? this.commonVo.protocolFundingSourceList : [];
        this.protocolSubjectList = this.commonVo.protocolSubjectList != null ? this.commonVo.protocolSubjectList : [];
        this.protocolCollaboratorList = this.commonVo.protocolCollaboratorList != null ? this.commonVo.protocolCollaboratorList : [];
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
           this.protocolNumber = this.result.generalInfo.protocolNumber;
           this._router.navigate( ['/irb/irb-create/irbHome'], {queryParams: {protocolNumber: this.result.generalInfo.protocolNumber,
                                                                protocolId: this.result.generalInfo.protocolId}});
           this.commonVo.generalInfo = this.result.generalInfo;
           this.generalInfo = this.result.generalInfo;
           this._sharedDataService.setGeneralInfo(this.generalInfo);
        });
    }
  }
 validateGeneralInfo() {
    if (this.generalInfo.protocolTypeCode !== null && this.generalInfo.protocolTypeCode !== 'null' &&
        this.generalInfo.protocolTitle !== null && this.generalInfo.protocolTitle !== '' &&
        this.invalidData.invalidStartDate === false &&
        this.invalidData.invalidEndDate === false) {
        this.invalidData.invalidGeneralInfo = false;
        return true;
    } else {
      this.invalidData.invalidGeneralInfo = true;
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

  setPersonRole(roleId) {
    this.personRoleTypes.forEach(personeRole => {
      if (personeRole.protocolPersonRoleId === roleId) {
        this.personnelInfo.protocolPersonRoleTypes = {protocolPersonRoleId: roleId, description: personeRole.description};
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
           this.personnelInfo.protocolPersonLeadUnits = {unitNumber: personeUnit.unitNumber, unitName: leadUnitName};
          }
       });
      } else {
        this.personnelInfo.protocolLeadUnits = null;
        this.personnelInfo.protocolPersonLeadUnits = null;
    this.protocolPersonLeadUnits.forEach(personeUnit => {
         if (personeUnit.unitName === leadUnitName) {
           this.personnelInfo.protocolLeadUnits = [{unitNumber: personeUnit.unitNumber}];
           this.personnelInfo.protocolPersonLeadUnits = {unitNumber: personeUnit.unitNumber, unitName: leadUnitName};
          }
       });
      }
    }  else {
      this.personnelInfo.protocolLeadUnits = null;
        this.personnelInfo.protocolPersonLeadUnits = null;
      this.protocolPersonLeadUnits.forEach(personeUnit => {
         if (personeUnit.unitName === leadUnitName) {
           this.personnelInfo.protocolLeadUnits = [{unitNumber: personeUnit.unitNumber}];
           this.personnelInfo.protocolPersonLeadUnits = {unitNumber: personeUnit.unitNumber, unitName: leadUnitName};
          }
       });
    }
  }

  setPersonAffiliation(affiliation) {
    this.affiliationTypes.forEach(personAffiliation => {
      if (personAffiliation.affiliationTypeCode == affiliation) {
        this.personnelInfo.protocolAffiliationTypes = {affiliationTypeCode: affiliation, description: personAffiliation.description};
      }
    });

  }

  editPersonalDetails(selectedItem: any, index: number) {
    this.editIndex = index;
    this.personalInfoSelectedRow = index;
    this.isPersonalInfoEdit = true;
    this.personnelInfo = Object.assign({}, selectedItem) ;
    this.personLeadUnit = selectedItem.protocolLeadUnits[0].protocolPersonLeadUnits.unitName;
  }
  addPersonalDetails(mode) {
    if (!(this.personnelInfo.personId == null || this.personnelInfo.personId === undefined)  &&
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
    // this.personnelInfo.protocolId = this.generalInfo.protocolId;
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
    // this.commonVo.protocolLeadUnits = this.personnelInfo.protocolLeadUnits;
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
    // this.personalInfoSelectedRow = null;
    // this.isPersonalInfoEdit = false;
    // this.personalDataList.splice(index, 1);

    this.commonVo.personnelInfo = this.personalDataList[index];
    this.commonVo.personnelInfo.acType = 'D';
    this.commonVo.personnelInfo.protocolGeneralInfo = this.generalInfo;
    this.savePersonalInfo('DELETE');
  }


  /**calls service to load person details of protocol */
  loadPersonDetailedList(item) {
    const params = {protocolNumber: this.protocolNumber, avPersonId: item.personId};
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

setFundingSourceType (typeCode) {
  this.protocolFundingSourceTypes.forEach(SourceType => {
    if (SourceType.fundingSourceTypeCode === typeCode) {
      this.fundingSource.protocolFundingSourceTypes = {fundingSourceTypeCode: typeCode, description: SourceType.description};
    }
  });
}

addFundingDetails(mode) {
  if (this.fundingSource.fundingSourceTypeCode != null && this.fundingSource.fundingSourceTypeCode !== undefined) {
    this.invalidData.invalidFundingInfo = false;
    this.saveFundingDetails(mode);
    } else {
      this.invalidData.invalidFundingInfo = true;
    }
    if (mode === 'EDIT') {
      this.isFundingInfoEdit = false;
      this.fundingEditIndex = null;
    }
}

editFundingDetails(item, index) {
  this.fundingEditIndex = index;
  this.isFundingInfoEdit = true;
  this.fundingSource = Object.assign({}, item) ;
}

deleteFundingDetails(index) {
  this.commonVo.fundingSource = this.protocolFundingSourceList[index];
  this.commonVo.fundingSource.acType = 'D';
  this.saveFundingDetails('DELETE');
}

saveFundingDetails(mode) {
  if (mode !== 'DELETE') {
    this.fundingSource.updateTimestamp = new Date();
    this.fundingSource.updateUser = localStorage.getItem('userName');
    this.fundingSource.sequenceNumber = 1;
    this.fundingSource.protocolNumber = this.protocolNumber;
    this.fundingSource.protocolId = this.generalInfo.protocolId;
    this.fundingSource.acType = 'U';
    this.commonVo.fundingSource = this.fundingSource;
  }
    this._irbCreateService.updateFundingSource(this.commonVo).subscribe(
        data => {
           this.result = data;
           this.fundingSource = {};
           this.protocolFundingSourceList = this.result.protocolFundingSourceList;
        });
}

setSubjectType(typeCode) {
  this.protocolSubjectTypes.forEach(subjectType => {
    if (subjectType.vulnerableSubjectTypeCode === typeCode) {
      this.protocolSubject.protocolSubjectTypes = {vulnerableSubjectTypeCode: typeCode, description: subjectType.description};
    }
  });
}

addSubjectDetails(mode) {
  if (this.protocolSubject.vulnerableSubjectTypeCode != null && this.protocolSubject.vulnerableSubjectTypeCode !== undefined
        && this.protocolSubject.subjectCount != null && this.protocolSubject.subjectCount !== undefined) {
          this.invalidData.invalidSubjectInfo = false;
          this.saveSubjectDetails(mode);
    } else {
      this.invalidData.invalidSubjectInfo = true;
    }
    if (mode === 'EDIT') {
      this.isSubjectInfoEdit = false;
      this.subjectEditIndex = null;
    }
}

editSubjectDetails(item, index) {
  this.subjectEditIndex = index;
  this.isSubjectInfoEdit = true;
  this.protocolSubject = Object.assign({}, item) ;
}

deleteSubjectDetails(index) {
  this.commonVo.protocolSubject = this.protocolSubjectList[index];
  this.commonVo.protocolSubject.acType = 'D';
  this.saveSubjectDetails('DELETE');
}

saveSubjectDetails(mode) {
  if (mode !== 'DELETE') {
    this.protocolSubject.updateTimestamp = new Date();
    this.protocolSubject.updateUser = localStorage.getItem('userName');
    this.protocolSubject.sequenceNumber = 1;
    this.protocolSubject.protocolNumber = this.protocolNumber;
    this.protocolSubject.protocolId = this.generalInfo.protocolId;
    this.protocolSubject.acType = 'U';
    this.commonVo.protocolSubject = this.protocolSubject;
  }
  this._irbCreateService.updateSubject(this.commonVo).subscribe(
    data => {
       this.result = data;
       this.protocolSubject = {};
       this.protocolSubjectList = this.result.protocolSubjectList;
       // this.commonVo.personnelInfo = this.result.personnelInfo;
       // this.personnelInfo = this.result.personnelInfo;
    });
}

setCollaborator(collaboratorName) {
  this.protocolCollaborator.collaboratorNames = null;
  this.commonVo.collaboratorNames.forEach(collaborator => {
    if (collaborator.organizationName === collaboratorName) {
      this.protocolCollaborator.organizationId = collaborator.organizationId;
      this.protocolCollaborator.collaboratorNames = {organizationId: collaborator.organizationId, organizationName: collaboratorName};
    }
  });
}

validateApprovalDate() {
  if (this.protocolCollaborator.expirationDate !== null && this.protocolCollaborator.expirationDate !== undefined) {
    if (this.protocolCollaborator.expirationDate < this.protocolCollaborator.approvalDate) {
      this.invalidData.invalidApprovalDate = true;
  } else {
      this.invalidData.invalidApprovalDate = false;
      this.invalidData.invalidExpirationDate = false;
    }
  }
}

validateExpirationDate() {
  if (this.protocolCollaborator.approvalDate !== null && this.protocolCollaborator.approvalDate !== undefined) {
    if (this.protocolCollaborator.expirationDate < this.protocolCollaborator.approvalDate) {
      this.invalidData.invalidExpirationDate = true;
    } else {
      this.invalidData.invalidExpirationDate = false;
      this.invalidData.invalidApprovalDate = false;
    }
  }
}

addCollaboratorDetails(mode) {
  if (this.protocolCollaborator.collaboratorNames != null && this.protocolCollaborator.collaboratorNames !== undefined) {
    this.invalidData.invalidCollaboratorInfo = false;
    this.saveCollaboratorDetails(mode);
  } else {
    this.invalidData.invalidCollaboratorInfo = true;
  }
  if (mode === 'EDIT') {
    this.isCollaboratorInfoEdit = false;
    this.collaboratorEditIndex = null;
  }
}

editCollaboratorDetails(item, index) {
  this.collaboratorEditIndex = index;
  this.isCollaboratorInfoEdit = true;
  this.protocolCollaborator = Object.assign({}, item) ;
  this.collaboratorName = this.protocolCollaborator.collaboratorNames.organizationName;
}

deleteCollaboratorDetails(index) {
  this.commonVo.protocolCollaborator = this.protocolCollaboratorList[index];
  this.commonVo.protocolCollaborator.acType = 'D';
  this.commonVo.protocolCollaborator.updateTimestamp = new Date();
  this.commonVo.protocolCollaborator.protocolOrgTypeCode = 1;
  this.commonVo.protocolCollaborator.updateUser = localStorage.getItem('userName');
  this.saveCollaboratorDetails('DELETE');
}

saveCollaboratorDetails(mode) {
  if (mode !== 'DELETE') {
    this.protocolCollaborator.updateTimestamp = new Date();
    this.protocolCollaborator.updateUser = localStorage.getItem('userName');
    this.protocolCollaborator.sequenceNumber = 1;
    this.protocolCollaborator.protocolOrgTypeCode = 1;
    this.protocolCollaborator.protocolNumber = this.protocolNumber;
    this.protocolCollaborator.protocolId = this.generalInfo.protocolId;
    this.protocolCollaborator.acType = 'U';
    this.commonVo.protocolCollaborator = this.protocolCollaborator;
  }
  this._irbCreateService.updateCollaborator(this.commonVo).subscribe(
    data => {
       this.result = data;
       this.protocolCollaborator = {};
       this.collaboratorName = null;
       this.protocolCollaboratorList = this.result.protocolCollaboratorList;
    });
}

}

