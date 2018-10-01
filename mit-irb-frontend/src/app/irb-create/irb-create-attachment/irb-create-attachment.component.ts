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
    tempEditAttachment: any = {};
    isDuplicate = false;
    generalInfo: any;
    attachmentTypeDescription: string;
    private subscription1: ISubscription;


    constructor(private _irbViewService: IrbViewService,
        private _activatedRoute: ActivatedRoute,
        public changeRef: ChangeDetectorRef,
        private _irbCreateService: IrbCreateService, private _sharedDataService: SharedDataService) { }

    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
        const reqobj = { protocolNumber: this._activatedRoute.snapshot.queryParamMap.get('protocolNumber') };
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
        this.subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
            if (generalInfo !== undefined) {
                this.generalInfo = generalInfo;
            }
        });
        this._irbCreateService.getAttachmentTypes(null).subscribe(data => {
            this.result = data;
            this.attachmentTypes = this.result.irbAttachementTypes;
        });
        this.loadIrbAttachmentList(reqobj);
    }
    setAttachmentType(attachmentTypeCode) {
        this.attachmentTypes.forEach(attachmentType => {
            if (attachmentType.typeCode == attachmentTypeCode) {
                this.attachmentTypeDescription = attachmentType.description;

            }
        });
    }

    /**calls service to load Attachment list in protocol*/
    loadIrbAttachmentList(reqobj) {
        this._irbCreateService.getIrbAttachmentList(reqobj).subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                    this.noIrbAttachments = true;
                } else {
                    this.generalInfo = this.result.protocolAttachmentList[0].protocolGeneralInfo;
                    this.irbAttachmentsList = this.result.protocolAttachmentList;
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
        this._irbViewService.downloadIrbAttachment(attachment.protocolAttachment.fileId).subscribe(data => {
            const a = document.createElement('a');
            const blob = new Blob([data], { type: data.type });
            a.href = URL.createObjectURL(blob);
            a.download = attachment.protocolAttachment.fileName;
            a.click();

        },
            error => console.log('Error downloading the file.'),
            () => console.log('OK'));
    }
    
    editAttachments(event: any, index, attachments) {
        this.tempEditAttachment.description = attachments.description;
        this.editAttachment[index] = !this.editAttachment[index];
    }
    
    cancelEditedattachments(event: any, index, attachments) {
        event.preventDefault();
        this.editAttachment[index] = !this.editAttachment[index];
        attachments.description = this.tempEditAttachment.description;
        attachments.attachmentType.typeCode = this.tempEditAttachment.attachmentType.typeCode;
    }
    
    saveEditedattachments(event: any, index, attachments) {
        debugger
        event.preventDefault();
        this.editAttachment[index] = !this.editAttachment[index];
        attachments.acType = 'U';
        attachments.attachmentType = {
            typeCode: attachments.attachmentType.typeCode,
            description: this.attachmentTypeDescription,
            updateTimestamp: attachments.attachmentType.updateTimestamp,
            updateUser: attachments.attachmentType.updateUser
        };
        attachments.protocolAttachment = attachments.protocolAttachment;
        attachments.typeCode = attachments.attachmentType.typeCode;
        attachments.updateTimestamp = new Date();
        attachments.updateUser = localStorage.getItem('userName');
        this._irbCreateService.addattachment(attachments, this.uploadedFile).subscribe(
            data => {
                this.result = data;
                this.irbAttachmentsList = this.result.protocolAttachmentList;
                this.irbAttachment = data;
            });
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
            this.irbAttachmentProtocol.acType = 'I';
            this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
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
                sequenceNumber: 1,
                updateTimestamp: new Date(),
                updateUser: localStorage.getItem('userName')

            };
            this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
                data => {
                    this.result = data;
                    if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                        this.noIrbAttachments = true;
                    } else {
                        this.noIrbAttachments = false;
                    }
                    this.irbAttachmentsList = this.result.protocolAttachmentList;
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
        this.showPopup = true;
        this.tempSaveAttachment = attachment;
    }
    deleteAttachments(event) {
        this.showPopup = false;
        this.tempSaveAttachment.acType = 'D';
        this._irbCreateService.addattachment(this.tempSaveAttachment, this.uploadedFile).subscribe(
            data => {
                this.result = data;
                this.irbAttachmentsList = this.result.protocolAttachmentList;
                this.irbAttachment = data;
            });
    }
    
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt(this.sortOrder, 10);
    }
    
    triggerAdd() {
        $('#addAttach').trigger('click');
    }
}
