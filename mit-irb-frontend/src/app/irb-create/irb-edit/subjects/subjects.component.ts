import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { IrbCreateService } from '../../irb-create.service';
import { SharedDataService } from '../../../common/service/shared-data.service';

@Component({
  selector: 'app-subjects',
  templateUrl: './subjects.component.html',
  styleUrls: ['./subjects.component.css']
})
export class SubjectsComponent implements OnInit, OnDestroy {

  protocolNumber = null;
  protocolId = null;
  subjectEditIndex = null;
  commonVo: any = {};
  generalInfo: any = {};
  result: any = {};
  userDTO: any = {};
  protocolSubject: any = {};
  protocolSubjectTypes = [];
  protocolSubjectList = [];
  protocolSubjectAgeGroups = [];
  protocolSubjectAgeGroupCategory = [];
  isSubjectInfoEdit = false;
  isGeneralInfoSaved = false;
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false
  };

  private $subscription1: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      this.protocolSubject.ageGroupCode = null;
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

  loadEditDetails() {
    this.protocolSubjectTypes = this.commonVo.protocolSubjectTypes;
    this.protocolSubjectAgeGroups = this.commonVo.ageGroups;
    // Look up Data - End
    this.generalInfo = this.commonVo.generalInfo;
    this.protocolSubject = this.commonVo.protocolSubject;
    this.protocolSubjectList = this.commonVo.protocolSubjectList != null ? this.commonVo.protocolSubjectList : [];
  }

  setSubjectType(typeCode) {

    this.protocolSubjectAgeGroupCategory = [];
    this.protocolSubjectTypes.forEach(subjectType => {
      if (subjectType.vulnerableSubjectTypeCode === typeCode) {
        this.protocolSubject.protocolSubjectTypes = { vulnerableSubjectTypeCode: typeCode, description: subjectType.description };
      }
    });
    this.setAgeGroupCategory(typeCode);

  }
  setAgeGroup(ageGroupCode) {
    this.protocolSubjectAgeGroupCategory.forEach(ageGroupCategory => {
      if (ageGroupCategory.ageGroupCode.toString() === ageGroupCode) {
        this.protocolSubject.ageGroups = {
          ageGroupCode: ageGroupCode,
          ageGroup: ageGroupCategory.ageGroup

        };

      }
    });
  }
  setAgeGroupCategory(typeCode) {
    this.protocolSubjectAgeGroups.forEach(ageGroup => {
      if (ageGroup.protocolSubjectTypes.vulnerableSubjectTypeCode === typeCode) {
        this.protocolSubjectAgeGroupCategory.push(ageGroup);
      }
    });
  }

  addSubjectDetails(mode) {
    if (this.protocolSubject.vulnerableSubjectTypeCode != null && this.protocolSubject.vulnerableSubjectTypeCode !== undefined
      && this.protocolSubject.subjectCount != null && this.protocolSubject.subjectCount !== undefined
      && this.protocolSubject.ageGroupCode != null && this.protocolSubject.ageGroupCode !== undefined) {
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
    this.protocolSubject = Object.assign({}, item);
    this.setAgeGroupCategory(this.protocolSubject.vulnerableSubjectTypeCode);
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
      this.protocolSubject.protocolId = this.protocolId;
      this.protocolSubject.acType = 'U';
      this.commonVo.protocolSubject = this.protocolSubject;
    }
    this._irbCreateService.updateSubject(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.protocolSubject = {};
        this.protocolSubjectAgeGroupCategory = [];
        this.protocolSubjectList = this.result.protocolSubjectList;
        this.commonVo.protocolSubject = {};
        this.commonVo.protocolSubjectList = this.protocolSubjectList;
      });
  }

}
