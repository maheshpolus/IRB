import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import {Location} from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { NgxSpinnerService } from 'ngx-spinner';

import { PiElasticService } from '../../common/service/pi-elastic.service';
import { PersonTrainingService } from '../person-training.service';

@Component({
  selector: 'app-training-details',
  templateUrl: './training-details.component.html',
  styleUrls: ['./training-details.component.css']
})
export class TrainingDetailsComponent implements OnInit {

  options: any = {};
  selectedPerson = {};
  personnelTrainingInfo: any = {};
  userDTO: any = {};
  IRBUtilVO: any = {};
  elasticPlaceHolder: string;
  fileDataId = null;
  defaultValue: string;
  mode: string;
  from: string;
  warningMessage: string;
  commentSelectedRow = null;
  personType = 'employee';
  clearField: any = 'true';
  showPersonElasticBand = false;
  noFileChoosen = false;
  isCommentEdit = false;
  isTrainingSearch = false;
  showPopup = false;
  isCommentDelete = false;
  showCommentDiv = false;
  multipleFile = false;
  trainingSearchResult = [];
  personnelTrainingComment = [];
  personnelTrainingAttachments = [];

  uploadedFile: File[] = [];
  files: UploadFile[] = [];
  fil: FileList;
  attachmentList: any[] = [];

  requestObject = {
    personTrainingId: null
  };
  personTraining = {
    personTrainingID: null,
    personID: null,
    activeFlag: null,
    description: null,
    dateAcknowledged: null,
    dateRequested: null,
    dateSubmitted: null,
    followUpDate: null,
    isEmployee: 'Y',
    score: null,
    trainingCode: null,
    trainingNumber: null,
    updateTimeStamp: null,
    updateUser: null,
    acType: null
  };
  personTrainingComments = {
    acType: '',
    commentId: null,
    personTrainingId: null,
    commentDescription: null,
    updateUser: null
  };
  personnelTrainingAttachment = {
    acType: null,
    attachmentId: null,
    personTrainingId: null,
    description: null,
    fileName: null,
    mimeType: null,
    updateUser: null
  };
  invalidData = {
    invalidPersonData: false, invalidCommentData: false, invalidAttachmentData: false, invalidAckDate: false, invalidExpDate: false
  };

  constructor(private _elasticsearchService: PiElasticService, private _location: Location,
     private _activatedRoute: ActivatedRoute, private _router: Router,
    private _personTrainingService: PersonTrainingService, private _spinner: NgxSpinnerService,
    public changeRef: ChangeDetectorRef) {
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
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.requestObject.personTrainingId = params['personTrainingId'];
      this.mode = params['mode'];
      this.from = params['from'];
      if (this.requestObject.personTrainingId != null) {
        this._spinner.show();
        this._personTrainingService.getPersonTrainingInfo(this.requestObject).subscribe(data => {
          this._spinner.hide();
          this.IRBUtilVO = data;
          this.personnelTrainingInfo = this.IRBUtilVO.personnelTrainingInfo == null ? {} : this.IRBUtilVO.personnelTrainingInfo;
          this.personnelTrainingComment = this.IRBUtilVO.personnelTrainingComment == null ? [] : this.IRBUtilVO.personnelTrainingComment;
          this.personnelTrainingAttachments = this.IRBUtilVO.personnelTrainingAttachments == null ?
            [] : this.IRBUtilVO.personnelTrainingAttachments;
          if (this.mode !== 'view') {
            this.loadEditDetails();
          }
        });
      }
    });
  }
/* load the training details*/
  loadEditDetails() {
    if (this.personnelTrainingInfo != null) {
      this.options.defaultValue = this.personnelTrainingInfo.FULL_NAME;
      this.defaultValue = this.personnelTrainingInfo.FULL_NAME;
      this.personType = this.personnelTrainingInfo.IS_EMPLOYEE === 'Y' ? 'employee' : 'non-employee';
      this.personnelTrainingInfo.DATE_ACKNOWLEDGED = this.personnelTrainingInfo.DATE_ACKNOWLEDGED != null ?
        new Date(this.personnelTrainingInfo.DATE_ACKNOWLEDGED) : null;
      this.personnelTrainingInfo.EXPIRATION_DATE = this.personnelTrainingInfo.DATE_ACKNOWLEDGED != null ?
        new Date(this.personnelTrainingInfo.EXPIRATION_DATE) : null;
      this.personTraining.personID = this.personnelTrainingInfo.PERSON_ID;
      this.personTraining.isEmployee = this.personnelTrainingInfo.IS_EMPLOYEE;
    }
  }
  /**
   * @param  {} type- Change the elastic based on type
   */
  changePersonType(type) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.showPersonElasticBand = false;
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.options.defaultValue = '';
    this.personTraining.personID = null;
    if (type === 'employee') {
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
      this.elasticPlaceHolder = 'Search for an Employee Name';
      this.personTraining.isEmployee = 'Y';

    } else {
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';
      this.personTraining.isEmployee = 'N';
    }
  }

  /**
   * @param  {} personDetails - person choosen from the elastic search
   */
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.showPersonElasticBand = personDetails != null ? true : false;
    if (personDetails != null) {
      this.personTraining.personID = this.options.type === 'person' ?
        personDetails.person_id : personDetails.rolodex_id;
    } else {
      this.personTraining.personID = null;
    }
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

  /* Trigger the 'choose File' in attachment modal */
  triggerAdd() {
    document.getElementById('addAttach').click();
  }

  /* Dismiss the attachment modal */
  dismissAttachmentModal() {
    this.uploadedFile = [];
    this.personnelTrainingAttachment.description = '';
    document.getElementById('cancelbtn').click();
  }

  /* get the training list on each key press */
  getTrainingList() {
    this._personTrainingService.loadTrainingList(this.personnelTrainingInfo.TRAINING).subscribe(
      (data: any) => {
        this.trainingSearchResult = data.trainingDesc;
      });
  }

  /**
   * @param  {} description - name of the training selecetd
   * @param  {} typeCode - type code of the training selected
   */
  selectedTraining(description, typeCode) {
    this.personnelTrainingInfo.TRAINING_CODE = typeCode;
    this.personnelTrainingInfo.TRAINING = description;
  }

  /* save the training basic details */
  savePersonTrainingDetails() {
    if (this.personnelTrainingInfo.TRAINING_CODE == null || this.personTraining.personID == null ||
      this.invalidData.invalidAckDate === true || this.invalidData.invalidExpDate === true) {
      this.invalidData.invalidPersonData = true;
      if (this.invalidData.invalidAckDate) {
        this.warningMessage = 'Acknowledgement Date should be less than Expiration Date';
      } else if (this.invalidData.invalidExpDate) {
        this.warningMessage = 'Expiration Date should be greater than Acknowledgement Date';
      } else {
        this.warningMessage = 'Please fill Mandatory fields marked <strong>*</strong>';
      }
    } else {
      this.personTraining.acType = this.requestObject.personTrainingId != null ? 'U' : 'I';
      this.personTraining.trainingCode = this.personnelTrainingInfo.TRAINING_CODE;
      this.personTraining.description = this.personnelTrainingInfo.TRAINING;
      this.personTraining.personTrainingID = this.requestObject.personTrainingId;
      this.personTraining.updateUser = this.userDTO.userName;
      this.IRBUtilVO.personTraining = this.personTraining;
      this.IRBUtilVO.dateAcknowledged = this.personnelTrainingInfo.DATE_ACKNOWLEDGED != null ?
        this.GetFormattedDate(this.personnelTrainingInfo.DATE_ACKNOWLEDGED) : null;
      this.IRBUtilVO.followUpDate = this.personnelTrainingInfo.EXPIRATION_DATE != null ?
        this.GetFormattedDate(this.personnelTrainingInfo.EXPIRATION_DATE) : null;
      this._spinner.show();
      this._personTrainingService.updatePersonTraining(this.IRBUtilVO).subscribe(data => {
        this._spinner.hide();
        this.IRBUtilVO = data;
        this.personnelTrainingInfo = this.IRBUtilVO.personnelTrainingInfo;
        this.invalidData.invalidPersonData = false;
        this.showPersonElasticBand = false;
        this.loadEditDetails();
        this._router.navigate(['/irb/training-maintenance/person-detail'],
          {
            queryParams: {
              personTrainingId: this.IRBUtilVO.personTraining.personTrainingID,
              mode: 'edit'
            }
          });

      });
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
   * @param  {} attachment - object of the attachment to be downloaded
   */
  downloadTrainingAttachment(attachment) {
    this._personTrainingService.downloadTrainingAttachment(attachment.FILE_DATA_ID).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = attachment.FILE_NAME;
      document.body.appendChild(a);
      a.click();

    },
      error => console.log('Error downloading the file.', error),
      () => console.log('OK'));
  }
  /**
   * @param  {} attachment - object of the attachment to be deleted
   */
  deleteTrainingAttachment(attachment) {
    this.fileDataId = attachment.FILE_DATA_ID;
    this.isCommentDelete = false;
    this.showPopup = true;
    this.personnelTrainingAttachment.acType = 'D';
    this.personnelTrainingAttachment.attachmentId = attachment.ATTACHMENT_ID;

  }

  /* add the training attachment */
  addTrainingAttachment() {
    if (this.uploadedFile.length === 0) {
      this.noFileChoosen = true;
    } else {
      this.noFileChoosen = false;
      if (this.uploadedFile.length > 1) {
        this.multipleFile = true;
      } else {
        this.personnelTrainingAttachment.acType = 'I';
        this.personnelTrainingAttachment.fileName = this.uploadedFile[0].name;
        this.personnelTrainingAttachment.mimeType = this.uploadedFile[0].type;
        this.saveAttachment();
      }
    }
  }

  /* Save the attachment */
  saveAttachment() {
    this.personnelTrainingAttachment.personTrainingId = this.requestObject.personTrainingId;
    this.personnelTrainingAttachment.updateUser = this.userDTO.userName;
    this._spinner.show();
    this._personTrainingService.addTrainingAttachments(this.personnelTrainingAttachment, this.uploadedFile,
      this.fileDataId).subscribe((data: any[]) => {
        this._spinner.hide();
        this.personnelTrainingAttachments = data;
        this.dismissAttachmentModal();
      });

  }
  /**
   * @param  {} comment - comment object edited
   * @param  {} index - index of the comment object edited
   */
  editTrainingComment(comment, index) {
    this.commentSelectedRow = index;
    this.showCommentDiv = true;
    this.isCommentEdit = true;
    this.personTrainingComments.acType = 'U';
    this.personTrainingComments.commentDescription = comment.COMMENT_DESCRIPTION;
    this.personTrainingComments.commentId = comment.COMMENT_ID;
  }
  /**
   * @param  {} comment- comment object deleted
   */
  deleteTrainingComment(comment) {
    this.showPopup = true;
    this.isCommentDelete = true;
    this.personTrainingComments.commentId = comment.COMMENT_ID;
    this.personTrainingComments.acType = 'D';

  }

  /* Add training comments  */
  addTrainingComment() {
    this.personTrainingComments.acType = 'I';
    this.saveTrainingComment();

  }

  /* save the training comment */
  saveTrainingComment() {
    if (this.personTrainingComments.commentDescription == null && this.personTrainingComments.acType !== 'D') {
      this.invalidData.invalidCommentData = true;
    } else {
      this.personTrainingComments.personTrainingId = this.requestObject.personTrainingId;
      this.personTrainingComments.updateUser = this.userDTO.userName;
      this.IRBUtilVO.personTrainingComments = this.personTrainingComments;
      this._spinner.show();
      this._personTrainingService.addTrainingComments(this.IRBUtilVO).subscribe(data => {
        this._spinner.hide();
        this.IRBUtilVO = data;
        this.personnelTrainingComment = this.IRBUtilVO.personnelTrainingComment;
        this.personTrainingComments.commentDescription = null;
        this.invalidData.invalidCommentData = false;
        this.isCommentEdit = false;
        this.commentSelectedRow = null;
        this.showCommentDiv = false;
      });
    }
  }

  /* validation for the acknowledgement date and expiration dates */
  validateAckDate() {
    if (this.personnelTrainingInfo.EXPIRATION_DATE != null || this.personnelTrainingInfo.EXPIRATION_DATE !== undefined) {
      if (Date.parse(this.personnelTrainingInfo.DATE_ACKNOWLEDGED) >= Date.parse(this.personnelTrainingInfo.EXPIRATION_DATE)) {
        this.invalidData.invalidAckDate = true;
      } else {
        this.invalidData.invalidAckDate = false;
        this.invalidData.invalidExpDate = false;
      }
    }
  }

    /* validation for the acknowledgement date and expiration dates */
  validateExpDate() {
    if (this.personnelTrainingInfo.DATE_ACKNOWLEDGED != null || this.personnelTrainingInfo.DATE_ACKNOWLEDGED !== undefined) {
      if (Date.parse(this.personnelTrainingInfo.DATE_ACKNOWLEDGED) >= Date.parse(this.personnelTrainingInfo.EXPIRATION_DATE)) {
        this.invalidData.invalidExpDate = true;
      } else {
        this.invalidData.invalidAckDate = false;
        this.invalidData.invalidExpDate = false;
      }
    }
  }
  /* link back to te protocol from which it is taken */
  returnToProtocol() {
    this._location.back();
  }

  exitToDashboard() {
    this._router.navigate(['/irb/training-maintenance']);
  }
}


