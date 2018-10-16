import { Component, OnInit, AfterViewInit, NgZone, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { CompleterService, CompleterData } from 'ng2-completer';
import { Router, ActivatedRoute} from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { NgxSpinnerService } from 'ngx-spinner';
import { ISubscription } from 'rxjs/Subscription';

import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { IrbCreateService } from '../../irb-create.service';
import { PiElasticService } from '../../../common/service/pi-elastic.service';
import {SharedDataService} from '../../../common/service/shared-data.service';
import { IrbViewService } from '../../../irb-view/irb-view.service';

@Component({
  selector: 'app-collaborators',
  templateUrl: './collaborators.component.html',
  styleUrls: ['./collaborators.component.css']
})
export class CollaboratorsComponent implements OnInit, OnDestroy {

  userDTO: any = {};
  protocolNumber = null;
  protocolId = null;
  isGeneralInfoSaved = false;
  commonVo: any = {};
  generalInfo: any = {};
  protocolCollaborator: any = {};
  isCollaboratorInfoEdit = false;
  collaboratorEditIndex = null;
  collaboratorName = null;
  protocolCollaboratorList = [];
  result: any = {};
  protected collaboratorNames: CompleterData;
  invalidData = {invalidGeneralInfo: false, invalidStartDate : false, invalidEndDate : false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false};

  private subscription1: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService,
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

    this.subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.commonVo = commonVo;
        this.loadEditDetails();
      }
    });
  }

  ngOnDestroy() {
    if (this.subscription1) {
     this.subscription1.unsubscribe();
    }
  }

  loadEditDetails() {
      this.collaboratorNames = this._completerService.
                                  local(this.commonVo.collaboratorNames, 'organizationName,organizationId', 'organizationName');
      this.generalInfo = this.commonVo.generalInfo;
      this.protocolCollaborator = this.commonVo.protocolCollaborator != null ? this.commonVo.protocolCollaborator : {};
      this.protocolCollaboratorList = this.commonVo.protocolCollaboratorList != null ? this.commonVo.protocolCollaboratorList : [];
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
