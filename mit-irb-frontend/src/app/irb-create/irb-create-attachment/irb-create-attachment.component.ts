import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { IrbViewService } from '../../irb-view/irb-view.service';
import { ActivatedRoute } from '@angular/router';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';
import { subscribeOn } from 'rxjs/operator/subscribeOn';
import { ISubscription } from 'rxjs/Subscription';
declare var $: any;
@Component({
    selector: 'app-irb-create-attachment',
    templateUrl: './irb-create-attachment.component.html',
    styleUrls: ['./irb-create-attachment.component.css'],
    providers: [IrbCreateService]
})
export class IrbCreateAttachmentComponent implements OnInit {

    noIrbAttachments = false;
    editAttachment: any = [];
    irbAttachmentsList: any[] = [];
    attachmentTypes = [];
    result: any = {};
    sortOrder = '1';
    sortField = 'UPDATE_TIMESTAMP';
    direction: number;
    column: any;
    requestObject = {
        protocolNumber: '',
        protocolId: '',
        attachmentId: '',
        attachmentTypeCode: null,
        attachmentDescription: ''
    };

    irbAttachment: any = {};
    /* formdataJson object */
    protocolAttachments: any = {};
     /* formdataIRBJson object */
     irbAttachmentProtocol: any = {};


    /*Attachment variables */
    fil: FileList;
    uploadedFile: File[] = [];
    files: UploadFile[] = [];
    attachmentList: any[] = [];
    showAddAttachment = false;
    isMandatoryFilled = true;
    showPopup = false;
    tempSaveAttachment: any = {};
    isDuplicate = false;
    generalInfo: any;
    attachmentTypeDescription: string;
    private subscription1: ISubscription;


    constructor(private _irbViewService: IrbViewService,
         private _activatedRoute: ActivatedRoute,
         public changeRef: ChangeDetectorRef,
         private _irbCreateService: IrbCreateService, private _sharedDataService: SharedDataService) {}

    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
         this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get( 'protocolNumber' );
         this.requestObject.protocolId = this._activatedRoute.snapshot.queryParamMap.get( 'protocolId' );
         this.subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
            if (generalInfo !== undefined) {
              this.generalInfo = generalInfo;
            }
          });
       // this.requestObject.protocolNumber = '1405006366';
       this._irbCreateService.getAttachmentTypes(null).subscribe(data => {
           this.result = data;
           this.attachmentTypes = this.result.irbAttachementTypes;
       });
       console.log(this.attachmentTypes);
       this.loadIrbAttachmentList();
    }
    setAttachmentType(attachmentTypeCode) {
        this.attachmentTypes.forEach(attachmentType => {
            if ( attachmentType.typeCode == attachmentTypeCode) {
                this.attachmentTypeDescription = attachmentType.description;

            }
        });
    }

    /**calls service to load Attachment list in protocol*/
    loadIrbAttachmentList() {
        this._irbViewService.getIrbAttachmentList(this.requestObject).subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.irbViewProtocolAttachmentList == null || this.result.irbViewProtocolAttachmentList.length === 0) {
                    this.noIrbAttachments = true;
                } else {
                    this.irbAttachmentsList = this.result.irbViewProtocolAttachmentList;
                    this.sortBy();

                }
            }

        },
            error => {
                console.log('Error in method loadIrbAttachmentList()', error);
            },
        );
    }

    /**calls service to get attachment details and to download it
     * @param attachment - object of an attachment
     */
    downloadAttachment(attachment) {
        this.requestObject.attachmentId = attachment.FILE_ID;
        this._irbViewService.downloadIrbAttachment(attachment.FILE_ID).subscribe(data => {
            const a = document.createElement('a');
            const blob = new Blob([data], { type: data.type });
            a.href = URL.createObjectURL(blob);
            a.download = attachment.FILE_NAME;
            a.click();

        },
            error => console.log('Error downloading the file.'),
            () => console.log('OK'));
    }
    editAttachments(event: any, index, attachments) {
        event.preventDefault();
        this.tempSaveAttachment = attachments;
        this.editAttachment[index] = !this.editAttachment[index];
    }
    cancelEditedattachments(event: any, index, attachments) {
        event.preventDefault();
        this.editAttachment[index] = !this.editAttachment[index];
        attachments.Description = this.tempSaveAttachment.DESCRIPTION;
        attachments.ATTACHMENT_TYPE_CODE = this.tempSaveAttachment.ATTACHMENT_TYPE_CODE;
    }
    saveEditedattachments(event: any, index, attachments) {
        event.preventDefault();
        this.editAttachment[index] = !this.editAttachment[index];
        this.protocolAttachments.acType = 'U';
        this.irbAttachmentProtocol.protocolNumber =  this.requestObject.protocolNumber;
        // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
         this.irbAttachmentProtocol.sequenceNumber = 1;
         this.irbAttachmentProtocol.typeCode = this.requestObject.attachmentTypeCode;
         this.irbAttachmentProtocol.documentId = 1;
         this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
         this.irbAttachmentProtocol.updateTimestamp = new Date();
         this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
         this.irbAttachmentProtocol.attachmentVersion = 1;
         this.irbAttachmentProtocol.protocolGeneralInfo = this.generalInfo;
         this.irbAttachmentProtocol.statusCode = 2;
         this.irbAttachmentProtocol.attachementStatus = {
             statusCode: 2,
             description: 'Complete'
         };
         this.irbAttachmentProtocol.attachmentType = {
             typeCode: this.requestObject.attachmentTypeCode,
             description: this.attachmentTypeDescription,
         };
         this.irbAttachmentProtocol.protocolAttachment = {
            // protocolNumber: this.requestObject.protocolNumber,
            sequenceNumber: 1,
            updateTimestamp: new Date(),
             updateUser: localStorage.getItem('userName')

         };
        // this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
        // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
        // this.irbAttachmentProtocol.typeCD = attachments.attachmentTypeCode;
        // this.irbAttachmentProtocol.sequenceNumber = 1;
        // this.irbAttachmentProtocol.description = attachments.attachmentDescription;
        // this.irbAttachmentProtocol.updateTimestamp = new Date();
        // this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
        // this.irbAttachmentProtocol.attachmentVersion = 1;
        this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
            data => {
                this.irbAttachment = data;
                this.uploadedFile = [];

            }
        );
    }

    onChange(files: FileList) {
        this.fil = files;
        this.isDuplicate = false;
        if (this.uploadedFile.length === 0) {
            for (let index = 0; index < this.fil.length; index++) {
                this.uploadedFile.push(this.fil[index]);
            }
        } else {
            for (let index = 0; index < this.fil.length; index++) {
                this.isDuplicate = false;
                for (let j = 0; j < this.uploadedFile.length; j++) {
                    if (this.fil[index].name == this.uploadedFile[j].name) {
                        this.isDuplicate = true;
                    }
                }
                if (!this.isDuplicate) {
                    this.uploadedFile.push(this.fil[index]);
                }
            }
        }
        this.changeRef.detectChanges();
    }

    public dropped(event: UploadEvent) {
        this.files = event.files;
        for (const file of this.files) {
            this.attachmentList.push(file);
        }
        for (const file of event.files) {
            if (file.fileEntry.isFile) {
                const fileEntry = file.fileEntry as FileSystemFileEntry;
                fileEntry.file((info: File) => {
                    if (this.uploadedFile.length === 0) {
                        this.uploadedFile.push(info);
                    } else {
                        for (let i = 0; i < this.uploadedFile.length; i++) {
                            if (this.uploadedFile[i].name == info.name) {
                                this.isDuplicate = true;
                            }
                        }
                        if (!this.isDuplicate) {
                            this.uploadedFile.push(info);
                            this.isDuplicate = false;
                        }
                    }
                    this.changeRef.detectChanges();
                });
            }
        }
    }

    deleteFromUploadedFileList(item) {
        for (let i = 0; i < this.uploadedFile.length; i++) {
            if (this.uploadedFile[i].name == item.name) {
                this.uploadedFile.splice(i, 1);
                this.changeRef.detectChanges();
            }
            this.isDuplicate = false;
        }
    }
    showAddAttachmentPopUp(e) {
        e.preventDefault();
        this.showAddAttachment = true;
        this.uploadedFile = [];
        this.requestObject.attachmentTypeCode = '';
        this.requestObject.attachmentDescription = '';
        this.isMandatoryFilled = true;
        this.changeRef.detectChanges();
    }

    addAttachments() {
        if (this.requestObject.attachmentTypeCode == null
            || this.requestObject.attachmentDescription == '' || this.uploadedFile.length == 0) {
            this.isMandatoryFilled = false;
        } else {
            this.irbAttachmentProtocol.acType = 'U';
            this.irbAttachmentProtocol.protocolNumber =  this.requestObject.protocolNumber;
           // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
            this.irbAttachmentProtocol.sequenceNumber = 1;
            this.irbAttachmentProtocol.typeCode = this.requestObject.attachmentTypeCode;
            this.irbAttachmentProtocol.documentId = 1;
            this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
            this.irbAttachmentProtocol.updateTimestamp = new Date();
            this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
            this.irbAttachmentProtocol.createTimestamp = new Date();
            this.irbAttachmentProtocol.attachmentVersion = 1;
            this.irbAttachmentProtocol.protocolGeneralInfo = this.generalInfo;
            this.irbAttachmentProtocol.statusCode = 2;
            this.irbAttachmentProtocol.attachementStatus = {
                statusCode: 2,
                description: 'Complete'
            };
            this.irbAttachmentProtocol.attachmentType = {
                typeCode: this.requestObject.attachmentTypeCode,
                description: this.attachmentTypeDescription,
            };
            this.irbAttachmentProtocol.protocolAttachment = {
               // protocolNumber: this.requestObject.protocolNumber,
               sequenceNumber: 1,
               updateTimestamp: new Date(),
                updateUser: localStorage.getItem('userName')

            };
            // this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
            // this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
            // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
            // this.irbAttachmentProtocol.sequenceNumber = 1;
            // this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
            // this.irbAttachmentProtocol.updateTimestamp = new Date();
            // this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
            // this.irbAttachmentProtocol.createTimestamp = new Date();
            // this.irbAttachmentProtocol.attachmentVersion = 1;
            // this.irbAttachmentProtocol.typeCD = this.requestObject.attachmentTypeCode;
            // this.irbAttachmentProtocol.documentId = 1;
            // this.irbAttachmentProtocol.fileId = 146883;
            this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
                data => {
                    this.irbAttachment = data;
                    this.isMandatoryFilled = true;
                    this.requestObject.attachmentTypeCode = '';
                    this.requestObject.attachmentDescription = '';
                    this.uploadedFile = [];

                }
            );
        }
    }

    closeAttachments() {
        this.showAddAttachment = false;
        this.uploadedFile = [];
    }
    tempSave(event, attachment) {
        event.preventDefault();
        this.showPopup = true;
        this.tempSaveAttachment = attachment;
    }
    deleteAttachments(event) {
        event.preventDefault();
        this.showPopup = false;
        this.protocolAttachments.acType = 'D';
        this.irbAttachmentProtocol.protocolNumber =  this.requestObject.protocolNumber;
           // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
            this.irbAttachmentProtocol.sequenceNumber = 1;
            this.irbAttachmentProtocol.typeCode = this.requestObject.attachmentTypeCode;
            this.irbAttachmentProtocol.documentId = 1;
            this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
            this.irbAttachmentProtocol.updateTimestamp = new Date();
            this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
            this.irbAttachmentProtocol.attachmentVersion = 1;
            this.irbAttachmentProtocol.protocolGeneralInfo = this.generalInfo;
            this.irbAttachmentProtocol.statusCode = 2;
            this.irbAttachmentProtocol.attachementStatus = {
                statusCode: 2,
                description: 'Complete'
            };
            this.irbAttachmentProtocol.attachmentType = {
                typeCode: this.requestObject.attachmentTypeCode,
                description: this.attachmentTypeDescription,
            };
            this.irbAttachmentProtocol.protocolAttachment = {
               // protocolNumber: this.requestObject.protocolNumber,
               sequenceNumber: 1,
               updateTimestamp: new Date(),
                updateUser: localStorage.getItem('userName')

            };
        // this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
        // this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
        // this.irbAttachmentProtocol.typeCD = this.requestObject.attachmentTypeCode;
        // this.irbAttachmentProtocol.sequenceNumber = 1;
        // this.irbAttachmentProtocol.description = this.requestObject.attachmentDescription;
        // this.irbAttachmentProtocol.updateTimestamp = new Date();
        // this.irbAttachmentProtocol.updateUser = localStorage.getItem('userName');
        // this.irbAttachmentProtocol.attachmentVersion = 1;
        this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
            data => {
                this.irbAttachment = data;
                this.uploadedFile = [];

            }
        );
    }
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt(this.sortOrder, 10);
    }
    triggerAdd() {
        $('#addAttach').trigger('click');
    }

}
