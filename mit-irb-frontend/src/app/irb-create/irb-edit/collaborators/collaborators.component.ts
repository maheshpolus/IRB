import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';

import { IrbCreateService } from '../../irb-create.service';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { IrbViewService } from '../../../irb-view/irb-view.service';


declare var $: any;
@Component({
  selector: 'app-collaborators',
  templateUrl: './collaborators.component.html',
  styleUrls: ['./collaborators.component.css']
})
export class CollaboratorsComponent implements OnInit, OnDestroy {

  userDTO: any = {};
  commonVo: any = {};
  generalInfo: any = {};
  result: any = {};
  protocolCollaborator: any = {};
  tempSaveCollaboratorPerson: any = {};
  protocolCollaboratorSelected: any = {};
  protocolCollaboratorAttachments: any = {};
  tempSaveAttachment: any = {};
  protocolCollaboratorAttachmentsList: any = [];
  protocolCollaboratorPersons: any = [];
  selectedPerson: any = {};
  protocolCollaboratorList = [];
  personsSelected = [];
  personalDataList = [];
  personalDataListCopy = [];
  attachmentTypes = [];
  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  attachmentList: any[] = [];
  isPersonSelected: boolean[] = [];
  isGeneralInfoSaved = false;
  isCollaboratorInfoEdit = false;
  isShowAddPerson = false;
  isShowAddAttachment = false;
  showPopup = false;
  protocolNumber = null;
  protocolId = null;
  collaboratorEditIndex = null;
  collaboratorName = null;
  fil: FileList;
  iconValue = -1;
  attachmentTypeDescription: string;
  protected collaboratorNames: CompleterData;
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false
  };
  requestObject = {
    attachmentTypeCode: null,
    attachmentDescription: ''
  };
  private $subscription1: ISubscription;
  private $subscription2: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    public changeRef: ChangeDetectorRef,
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
          this.personalDataListCopy = Object.assign([], this.generalInfo.personnelInfos);
          this.isPersonSelected.length = this.generalInfo.personnelInfos.length;
        }
      }
    });
    this._irbCreateService.getAttachmentTypes(null).subscribe(data => {
      this.result = data;
      this.attachmentTypes = this.result.irbAttachementTypes;
    });
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
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
        this.protocolCollaborator.collaboratorNames = { organizationId: collaborator.organizationId, organizationName: collaboratorName };
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
    this.protocolCollaborator = Object.assign({}, item);
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
        this.commonVo.protocolCollaborator = {};
        this.commonVo.protocolCollaboratorList = this.protocolCollaboratorList;
      });
  }

  setCollaboratorPersonDetails(item) {
    this.isShowAddPerson = false;
    this.isShowAddAttachment = false;
    this.protocolCollaboratorSelected = Object.assign({}, item);
    this.loadCollaboratorPersonsAndAttachment(this.protocolCollaboratorSelected.protocolLocationId);
      }
  loadCollaboratorPersonsAndAttachment(CollaboratorId) {
    this.commonVo.collaboratorId = CollaboratorId;
    this._irbCreateService.loadCollaboratorPersonsAndAttachments(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList == null ?
          null : this.result.protocolCollaboratorAttachmentsList;
        this.protocolCollaboratorPersons = this.result.protocolCollaboratorPersons == null ? null : this.result.protocolCollaboratorPersons;
        if (this.protocolCollaboratorPersons != null || this.protocolCollaboratorPersons !== undefined) {
          this.removePersonFromPersonInfo();
        }
      });
  }
  // Persons Popup Methods

  addCollaboratorPerson() {
    for (let index = 0; index < this.isPersonSelected.length; index++) {
      if (this.isPersonSelected[index] === true) {
        this.isPersonSelected[index] = !this.isPersonSelected[index];
        this.personsSelected.push({
          collaboratorId: this.protocolCollaboratorSelected.protocolLocationId,
          personId: this.personalDataList[index].protocolPersonId,
          protocolId: this.protocolCollaborator.protocolId,
          protocolNumber: this.protocolCollaborator.protocolNumber,
          sequenceNumber: 1,
          updateTimestamp: new Date(),
          updateUser: localStorage.getItem('userName'),
          acType: 'U'
        });

        // this.personsSelected.push(this.personalDataList[index]);
      }
    }
    this.commonVo.protocolCollaboratorPersons = this.personsSelected;
    this._irbCreateService.addCollaboratorPersons(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorPersons = this.result.protocolCollaboratorPersons == null ?
          null : this.result.protocolCollaboratorPersons;
          if (this.protocolCollaboratorPersons != null || this.protocolCollaboratorPersons !== undefined) {
            this.removePersonFromPersonInfo();
          }
        this.personsSelected = [];
      });

  }
  removePersonFromPersonInfo() {
    for (let index = 0; index < this.personalDataList.length; index++) {
      this.protocolCollaboratorPersons.forEach(protocolCollaboratorPerson => {
        if (protocolCollaboratorPerson.personnelInfo.protocolPersonId === this.personalDataList[index].protocolPersonId  ) {
          this.personalDataList.splice(index, 1);
        }
      });

    }
  }
  deletePerson(protocolCollaboratorPerson) {
    this.personalDataListCopy.forEach(personalData => {
      if (protocolCollaboratorPerson.personId === personalData.protocolPersonId) {
        this.personalDataList.push(personalData);
      }
    });
    // this.personalDataList.push(protocolCollaboratorPerson);
    protocolCollaboratorPerson.acType = 'D';
    this.personsSelected.push(protocolCollaboratorPerson);
    this.commonVo.protocolCollaboratorPersons = this.personsSelected;
    this._irbCreateService.addCollaboratorPersons(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorPersons = this.result.protocolCollaboratorPersons == null ?
          null : this.result.protocolCollaboratorPersons;
        this.personsSelected = [];
      });
  }
  // attachment popup methods
  triggerAdd() {
    $('#addAttach').trigger('click');
  }
  /** Push the unique files choosen to uploaded file
   * @param files- files choosen
   */
  onChange(files: FileList) {
    this.fil = files;
    for (let index = 0; index < this.fil.length; index++) {
      this.uploadedFile.push(this.fil[index]);
    }
    this.changeRef.detectChanges();
  }

  /** Push the unique files dropped to uploaded file
  * @param files- files dropped
  */
  public dropped(event: UploadEvent) {
    this.files = event.files;
    for (const file of this.files) {
      this.attachmentList.push(file);
    }
    for (const file of event.files) {
      if (file.fileEntry.isFile) {
        const fileEntry = file.fileEntry as FileSystemFileEntry;
        fileEntry.file((info: File) => {
          this.uploadedFile.push(info);
          this.changeRef.detectChanges();
        });
      }
    }
  }
  /**Remove an item from the uploded file
   * @param item-item to be removed
   */
  deleteFromUploadedFileList(item) {
    for (let i = 0; i < this.uploadedFile.length; i++) {
      if (this.uploadedFile[i].name === item.name) {
        this.uploadedFile.splice(i, 1);
        this.changeRef.detectChanges();
      }
    }
  }
  setAttachmentType(attachmentTypeCode) {
    this.attachmentTypes.forEach(attachmentType => {
      if (attachmentType.typeCode === attachmentTypeCode) {
        this.attachmentTypeDescription = attachmentType.description;
      }
    });
  }
  addAttachments() {
    this.protocolCollaboratorAttachments = {
      collaboratorId: this.protocolCollaboratorSelected.protocolLocationId,
      protocolId: this.protocolCollaboratorSelected.protocolId,
      protocolNumber: this.protocolCollaboratorSelected.protocolNumber,
      sequenceNumber: 1,
      description: this.requestObject.attachmentDescription,
      updateTimestamp: new Date(),
      createDate: new Date(),
      updateUser: localStorage.getItem('userName'),
      acType: 'I'

    };
    this.protocolCollaboratorAttachments.protocolAttachments = {
      sequenceNumber: 1,
      updateTimestamp: new Date(),
      updateUser: localStorage.getItem('userName')

    };
    this.protocolCollaboratorAttachments.attachmentType = {
      typeCode: this.requestObject.attachmentTypeCode,
      description: this.attachmentTypeDescription
    };
    this.protocolCollaboratorAttachments.typeCode = this.requestObject.attachmentTypeCode;
    this._irbCreateService.addCollaboratorAttachments(this.protocolCollaboratorAttachments, this.uploadedFile).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
        this.uploadedFile = [];
        this.requestObject.attachmentDescription = '';
        this.requestObject.attachmentTypeCode = '';
      });
  }
  tempSave(event, attachment) {
    event.preventDefault();
    this.showPopup = true;
    this.tempSaveAttachment = attachment;
  }
  downloadAttachment(attachment) {
    this._irbViewService.downloadIrbAttachment(attachment.protocolAttachments.fileId).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = attachment.protocolAttachments.fileName;
      a.click();

    },
      error => console.log('Error downloading the file.', error),
      () => console.log('OK'));
  }
  deleteAttachments() {
    this.tempSaveAttachment.acType = 'D';
    this._irbCreateService.addCollaboratorAttachments(this.tempSaveAttachment, this.uploadedFile).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
        this.uploadedFile = [];
        this.requestObject.attachmentDescription = '';
      });
  }
  toggle(item, index) {
    this.protocolCollaboratorAttachmentsList = null;
    this.protocolCollaboratorPersons = null;
    if (this.iconValue === index) {
      this.iconValue = -1;
    } else {
      this.iconValue = index;
      this.loadCollaboratorPersonsAndAttachment(item.protocolLocationId);
    }

  }

}
