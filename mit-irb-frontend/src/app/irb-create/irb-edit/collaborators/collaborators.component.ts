import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CompleterService, CompleterData } from 'ng2-completer';
import { ActivatedRoute } from '@angular/router';
import { ISubscription } from 'rxjs/Subscription';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { NgxSpinnerService } from 'ngx-spinner';

import { IrbCreateService } from '../../irb-create.service';
import { SharedDataService } from '../../../common/service/shared-data.service';
import { IrbViewService } from '../../../irb-view/irb-view.service';
import { KeyPressEvent } from '../../../common/directives/keyPressEvent.component';


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
  irbAttachmentProtocol: any = {};
  protocolCollaboratorSelected: any = {};
  protocolCollaboratorAttachments: any = {};
  tempSaveAttachment: any = {};
  protocolCollaboratorAttachmentsList: any = [];
  protocolCollaboratorPersons: any = [];
  organizationSearchResult: any = [];
  selectedPerson: any = {};
  protocolCollaboratorList = [];
  personsSelected = [];
  personalDataList = [];
  attachmentTypes = [];
  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  attachmentList: any[] = [];
  isPersonSelected: boolean[] = [];
  isGeneralInfoSaved = false;
  isCollaboratorInfoEdit = false;
  showDeletePopup = false;
  isShowAddPerson = false;
  isShowAddAttachment = false;
  showPopup = false;
  isCollaboratorEditable = true;
  isOrganizationSearch = false;
  protocolNumber = null;
  protocolId = null;
  collaboratorEditIndex = null;
  collaboratorName = null;
  fil: FileList;
  iconValue = -1;
  attachmentTypeDescription: string;
  collaboratorSearchString: string;
  warningMessage: string;
  protected collaboratorNames: CompleterData;
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false,
    invalidAttachmentInfo: false, invalidCollaboratorPersonInfo: false
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
    private _completerService: CompleterService,
    public keyPressEvent: KeyPressEvent,
    private _spinner: NgxSpinnerService
  ) { }

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
          this.personalDataList = Object.assign([], this.generalInfo.personnelInfos);
          this.isPersonSelected.length = this.generalInfo.personnelInfos.length;
          if (this.protocolCollaboratorSelected.protocolLocationId != null
            && this.protocolCollaboratorSelected.protocolLocationId !== undefined ) {
          this.loadCollaboratorPersonsAndAttachment(this.protocolCollaboratorSelected.protocolLocationId);
          }
        }
      }
    });
    this._irbCreateService.loadCollaboratorAttachmentType().subscribe(data => {
      this.result = data;
      this.attachmentTypes = this.result.irbAttachementTypes;
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

  loadEditDetails() {
    this.isCollaboratorEditable =
    this.commonVo.protocolRenewalDetails != null ? this.commonVo.protocolRenewalDetails.engangedInstitution : true;
    // this.collaboratorNames = this._completerService.
    //   local(this.commonVo.collaboratorNames, 'organizationName,organizationId', 'organizationName');
    this.generalInfo = this.commonVo.generalInfo;
    this.protocolCollaborator = this.commonVo.protocolCollaborator != null ? this.commonVo.protocolCollaborator : {};
    this.protocolCollaboratorList = this.commonVo.protocolCollaboratorList != null ? this.commonVo.protocolCollaboratorList : [];
  }

  // setCollaborator(collaboratorName) {
  //   this.protocolCollaborator.collaboratorNames = null;
  //   this.commonVo.collaboratorNames.forEach(collaborator => {
  //     if (collaborator.organizationName === collaboratorName) {
  //       this.protocolCollaborator.organizationId = collaborator.organizationId;
  //       this.protocolCollaborator.collaboratorNames =
  // { organizationId: collaborator.organizationId, organizationName: collaboratorName };
  //     }
  //   });
  // }

  validateApprovalDate() {
    if (this.protocolCollaborator.expirationDate !== null && this.protocolCollaborator.expirationDate !== undefined) {
      this.protocolCollaborator.expirationDate = new Date(this.protocolCollaborator.expirationDate);
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
      this.protocolCollaborator.approvalDate = new Date(this.protocolCollaborator.approvalDate);
      if (this.protocolCollaborator.expirationDate < this.protocolCollaborator.approvalDate) {
        this.invalidData.invalidExpirationDate = true;
      } else {
        this.invalidData.invalidExpirationDate = false;
        this.invalidData.invalidApprovalDate = false;
      }
    }
  }

  addCollaboratorDetails(mode) {
    if (this.protocolCollaborator.collaboratorNames != null && this.protocolCollaborator.collaboratorNames !== undefined
      && this.invalidData.invalidApprovalDate === false && this.invalidData.invalidExpirationDate === false) {
      this.invalidData.invalidCollaboratorInfo = false;
      this.saveCollaboratorDetails(mode);
    } else {
      this.invalidData.invalidCollaboratorInfo = true;
      if (this.invalidData.invalidApprovalDate) {
        this.warningMessage = 'Approval Date should be less than Expiration Date';
      } else if (this.invalidData.invalidExpirationDate) {
        this.warningMessage = 'Expiration Date should be greater than Approval Date';
      } else {
        this.warningMessage = 'Please fill all mandatory fields marked <strong>*</strong>';
      }
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
    this.protocolCollaborator.approvalDate = this.protocolCollaborator.approvalDate != null ?
      this.GetFormattedDateFromString(this.protocolCollaborator.approvalDate) : null;
    this.protocolCollaborator.expirationDate = this.protocolCollaborator.expirationDate != null ?
      this.GetFormattedDateFromString(this.protocolCollaborator.expirationDate) : null;
    this.collaboratorName = this.protocolCollaborator.collaboratorNames.organizationName;
  }

  deleteCollaboratorDetails(index) {
    this.commonVo.protocolCollaborator = this.protocolCollaboratorList[index];
    this.commonVo.protocolCollaborator.acType = 'D';
    this.commonVo.protocolCollaborator.updateTimestamp = new Date();
    this.commonVo.protocolCollaborator.protocolOrgTypeCode = 1;
    this.commonVo.protocolCollaborator.updateUser = this.userDTO.userName;
    this.showDeletePopup = true;
  }

  saveCollaboratorDetails(mode) {

    this.protocolCollaborator.stringApprovalDate = this.protocolCollaborator.approvalDate != null ?
    this.GetFormattedDate(this.protocolCollaborator.approvalDate) : null;
    this.protocolCollaborator.stringExpirationDate = this.protocolCollaborator.expirationDate != null ?
    this.GetFormattedDate(this.protocolCollaborator.expirationDate) : null;

    if (mode !== 'DELETE') {
      this.protocolCollaborator.updateTimestamp = new Date();
      this.protocolCollaborator.updateUser = this.userDTO.userName;
      this.protocolCollaborator.sequenceNumber = 1;
      this.protocolCollaborator.protocolOrgTypeCode = 1;
      this.protocolCollaborator.protocolNumber = this.protocolNumber;
      this.protocolCollaborator.protocolId = this.generalInfo.protocolId;
      this.protocolCollaborator.acType = 'U';
      this.commonVo.protocolCollaborator = this.protocolCollaborator;
    }
    this._spinner.show();
    this._irbCreateService.updateCollaborator(this.commonVo).subscribe(
      data => {
        this.result = data;
        this._spinner.hide();
        this.protocolCollaborator = {};
        this.collaboratorName = null;
        this.commonVo.protocolCollaborator = this.protocolCollaborator;
        this.protocolCollaboratorList = this.result.protocolCollaboratorList;
        this.commonVo.protocolCollaborator = {};
        this.commonVo.protocolCollaboratorList = this.protocolCollaboratorList;
      });
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

  setCollaboratorPersonDetails(item) {
    this.iconValue = -1;
    this.isShowAddPerson = false;
    this.isShowAddAttachment = false;
    this.protocolCollaboratorSelected = Object.assign({}, item);
    this.loadCollaboratorPersonsAndAttachment(this.protocolCollaboratorSelected.protocolLocationId);
  }
  loadCollaboratorPersonsAndAttachment(CollaboratorId) {
    this.commonVo.collaboratorId = CollaboratorId;
    this.personalDataList = Object.assign([], this.generalInfo.personnelInfos);
    this._spinner.show();
    this._irbCreateService.loadCollaboratorPersonsAndAttachments(this.commonVo).subscribe(
      data => {
        this.result = data;
        this._spinner.hide();
        this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList == null ?
          null : this.result.protocolCollaboratorAttachmentsList;
        this.protocolCollaboratorPersons = this.result.protocolCollaboratorPersons == null ? [] : this.result.protocolCollaboratorPersons;
        if (this.protocolCollaboratorPersons != null || this.protocolCollaboratorPersons.length > 0) {
          this.removePersonFromPersonInfo();
        }
      });
  }
  // Persons Popup Methods

  addCollaboratorPerson() {
    this.isPersonSelected.forEach((isPersonSelected, index) => {
      if (isPersonSelected === true) {
        this.personsSelected.push({
          collaboratorId: this.protocolCollaboratorSelected.protocolLocationId,
          personId: this.personalDataList[index].protocolPersonId,
          protocolId: this.protocolCollaborator.protocolId,
          protocolNumber: this.protocolCollaborator.protocolNumber,
          sequenceNumber: 1,
          updateTimestamp: new Date(),
          updateUser: this.userDTO.userName,
          acType: 'U'
        });
      }
    });
    if (this.personsSelected != null && this.personsSelected.length === 0) {
      this.invalidData.invalidCollaboratorPersonInfo = true;
    } else {
      this.invalidData.invalidCollaboratorPersonInfo = false;
      this.commonVo.protocolCollaboratorPersons = this.personsSelected;
      this._irbCreateService.addCollaboratorPersons(this.commonVo).subscribe(
        data => {
          this.result = data;
          this.protocolCollaboratorPersons = this.result.protocolCollaboratorPersons == null ?
            [] : this.result.protocolCollaboratorPersons;
          if (this.protocolCollaboratorPersons != null || this.protocolCollaboratorPersons.length > 0) {
            this.removePersonFromPersonInfo();
          }
          this.personsSelected = [];
          this.isPersonSelected = [];
        });
    }
  }
  removePersonFromPersonInfo() {
    this.protocolCollaboratorPersons.forEach(protocolCollaboratorPerson => {
      this.personalDataList.forEach((personData, index) => {
        if (protocolCollaboratorPerson.personnelInfo.protocolPersonId === personData.protocolPersonId) {
          this.personalDataList.splice(index, 1);
        }
      });

    });
  }
  deletePerson(protocolCollaboratorPerson) {
    protocolCollaboratorPerson.acType = 'D';
    this.personalDataList.push(protocolCollaboratorPerson.personnelInfo);
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
      // this.fil = files;
      // for (let index = 0; index < this.fil.length; index++) {
      //     this.uploadedFile.push(this.fil[index]);
      // }
      //  console.log(this.uploadedFile);
      // this.changeRef.detectChanges();
      this.uploadedFile = [];
      // this.uploadedFile.push(this.fil[0]);
      this.uploadedFile[0] = files[0];
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
                  this.uploadedFile = [];
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
            this.irbAttachmentProtocol.subCategoryCode = attachmentType.subCategoryCode;
        }
    });
}
  addAttachments() {
    if (this.requestObject.attachmentTypeCode == null
      || this.requestObject.attachmentDescription === '' || this.uploadedFile.length === 0) {
      this.invalidData.invalidAttachmentInfo = true;
    } else {
      this.invalidData.invalidAttachmentInfo = false;
      // $('#attachmentModal').modal('toggle');
      this.irbAttachmentProtocol.acType = 'I';
      this.irbAttachmentProtocol.protocolNumber = this.protocolNumber;
      this.irbAttachmentProtocol.protocolId = this.protocolId;
      this.irbAttachmentProtocol.sequenceNumber = 1;
      this.irbAttachmentProtocol.typeCode = this.requestObject.attachmentTypeCode;
      this.irbAttachmentProtocol.documentId = 1;
      this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
      this.irbAttachmentProtocol.updateTimestamp = new Date();
      this.irbAttachmentProtocol.updateUser = this.userDTO.userName;
      this.irbAttachmentProtocol.createTimestamp = new Date();
      this.irbAttachmentProtocol.attachmentVersion = 1;
      this.irbAttachmentProtocol.protocolGeneralInfo = this.generalInfo;
      this.irbAttachmentProtocol.statusCode = 2;
      this.irbAttachmentProtocol.isProtocolAttachment = 'N';
      this.irbAttachmentProtocol.attachementStatus = {
          statusCode: 2,
          description: 'Complete'
      };
      this.irbAttachmentProtocol.attachmentType = {
          typeCode: this.requestObject.attachmentTypeCode,
          description: this.attachmentTypeDescription,
         // subCategoryCode: this.irbAttachmentProtocol.subCategoryCode
      };
      this.irbAttachmentProtocol.attachmentSubCategory = {
        subCategoryCode: this.irbAttachmentProtocol.subCategoryCode,
        description: 'Enagaged Institutions'
      };
      this.irbAttachmentProtocol.protocolLocationId = this.protocolCollaboratorSelected.protocolLocationId;
      // this.irbAttachmentProtocol.protocolAttachmentData = {
      //     sequenceNumber: 1,
      //     updateTimestamp: new Date(),
      //     updateUser: this.userDTO.userName

      // };
      this._spinner.show();
      this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
          data => {
              this.result = data;
             this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList != null ?
             this.result.protocolCollaboratorAttachmentsList : [];
              this._spinner.hide();
              // this.irbAttachment = data;
             // this.isMandatoryFilled = true;
              this.requestObject.attachmentTypeCode = null;
              this.requestObject.attachmentDescription = '';
              this.uploadedFile = [];
          });
      // this.protocolCollaboratorAttachments = {
      //   collaboratorId: this.protocolCollaboratorSelected.protocolLocationId,
      //   protocolId: this.protocolCollaboratorSelected.protocolId,
      //   protocolNumber: this.protocolCollaboratorSelected.protocolNumber,
      //   sequenceNumber: 1,
      //   description: this.requestObject.attachmentDescription,
      //   updateTimestamp: new Date(),
      //   createDate: new Date(),
      //   updateUser: this.userDTO.userName,
      //   acType: 'I'

      // };
      // this.protocolCollaboratorAttachments.protocolAttachments = {
      //   sequenceNumber: 1,
      //   updateTimestamp: new Date(),
      //   updateUser: this.userDTO.userName

      // };
      // this.protocolCollaboratorAttachments.attachmentType = {
      //   typeCode: this.requestObject.attachmentTypeCode,
      //   description: this.attachmentTypeDescription
      // };
      // this.protocolCollaboratorAttachments.typeCode = this.requestObject.attachmentTypeCode;
      // this._irbCreateService.addCollaboratorAttachments(this.protocolCollaboratorAttachments, this.uploadedFile).subscribe(
      //   data => {
      //     this.result = data;
      //     this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
      //     this.uploadedFile = [];
      //     this.requestObject.attachmentDescription = '';
      //     this.requestObject.attachmentTypeCode = null;
      //   });
    }
  }
  tempSave(event, attachment) {
    event.preventDefault();
    this.showPopup = true;
    this.tempSaveAttachment = attachment;
  }
  downloadAttachment(attachment) {
    this._irbCreateService.downloadIrbAttachment(attachment.protocolAttachmentData.fileId).subscribe(data => {
        const a = document.createElement('a');
        const blob = new Blob([data], { type: data.type });
        a.href = URL.createObjectURL(blob);
        a.download = attachment.fileName;
        document.body.appendChild(a);
        a.click();

    },
        error => console.log('Error downloading the file.', error),
        () => console.log('OK'));
}
  deleteAttachments() {
    this.tempSaveAttachment.acType = 'D';
    this.tempSaveAttachment.protocolId = this.protocolId;
    this._irbCreateService.addattachment(this.tempSaveAttachment, this.uploadedFile).subscribe(
      data => {
        this.result = data;
        this.protocolCollaboratorAttachmentsList = this.result.protocolAttachmentList != null ? this.result.protocolAttachmentList : [];
        this.uploadedFile = [];
        this.requestObject.attachmentDescription = '';
      });
  }
  toggle(item, index) {
    this.protocolCollaboratorAttachmentsList = null;
    this.protocolCollaboratorPersons = null;
    if (this.iconValue === index) {
      this.iconValue = -1;
      this.protocolCollaboratorSelected = {};
    } else {
      this.iconValue = index;
      this.protocolCollaboratorSelected = Object.assign({}, item);
      this.loadCollaboratorPersonsAndAttachment(item.protocolLocationId);
    }

  }

  getOrganizationList() {
    this.collaboratorSearchString = this.collaboratorName;
    this._irbCreateService.getOrganizationList(this.collaboratorSearchString).subscribe(
      (data: any) => {
        this.organizationSearchResult = data.collaboratorList;
      });
  }

  selectedOrganization(organizationName, organizationId) {
    this.collaboratorName = organizationName;
    this.protocolCollaborator.organizationId = organizationId;
    this.protocolCollaborator.collaboratorNames = { organizationId: organizationId, organizationName: organizationName };
    this.isOrganizationSearch = false;
  }
}
