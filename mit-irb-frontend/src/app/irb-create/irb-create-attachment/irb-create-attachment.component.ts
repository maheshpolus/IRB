import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { NgxSpinnerService } from 'ngx-spinner';
import { ActivatedRoute } from '@angular/router';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';
import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';
import { IrbViewService } from '../../irb-view/irb-view.service';

declare var $: any;
@Component({
    selector: 'app-irb-create-attachment',
    templateUrl: './irb-create-attachment.component.html',
    styleUrls: ['./irb-create-attachment.component.css'],
    providers: [IrbCreateService]
})
export class IrbCreateAttachmentComponent implements OnInit, OnDestroy {

    userDTO: any = {};
    irbAttachment: any = {};
    versionAttachmentChoosen: any = {};
    irbAttachmentProtocol: any = {};
    tempSaveAttachment: any = {};
    tempEditAttachment: any = {};
    commonVo: any = {};
    protocolCollaboratorAttachmentsList: any[] = [];
    result: any = {};
    irbAttachmentsList: any[] = [];
    internalProtocolAtachmentList: any = [];
    previousProtocolAttachmentList: any = [];
    attachmentTypes = [];
    uploadedFile: File[] = [];
    files: UploadFile[] = [];
    attachmentList: any[] = [];
    isCollaboratorEdit = false;
    noIrbAttachments = false;
    isMandatoryFilled = true;
    showPopup = false;
    isAttachmentReplace = false;
    multipleFile = false;
    isAttachmentEditable = true;
    fil: FileList;
    generalInfo: any;
    attachmentTypeDescription: string;
    fileName: string;
    direction = 1;
    attachmentEditedRow: number;
    attachmentSelectedRow: number;
    column = 'updateTimestamp';
    tabSelected = 'STUDY';
    requestObject = {
        protocolNumber: '',
        protocolId: '',
        attachmentId: '',
        attachmentTypeCode: null,
        attachmentDescription: ''
    };
    private $subscription1: ISubscription;

    constructor(private _activatedRoute: ActivatedRoute,
        public changeRef: ChangeDetectorRef,
        private _irbCreateService: IrbCreateService, private _spinner: NgxSpinnerService,
        private _sharedDataService: SharedDataService) { }

    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
        this.userDTO = this._activatedRoute.snapshot.data['irb'];
        // const reqobj = { protocolNumber: this._activatedRoute.snapshot.queryParamMap.get('protocolNumber') };
        this._activatedRoute.queryParams.subscribe(params => {
            this.requestObject.protocolId = params['protocolId'];
            this.requestObject.protocolNumber = params['protocolNumber'];
          });
        this.$subscription1 = this._sharedDataService.commonVo.subscribe(commonVo => {
            if (commonVo !== undefined) {
                this.commonVo = commonVo;
                this.generalInfo = commonVo.generalInfo;
                this.isAttachmentEditable =
                this.commonVo.protocolRenewalDetails != null ? this.commonVo.protocolRenewalDetails.addModifyNoteAttachments : true;
            }
        });
        this._irbCreateService.getAttachmentTypes(null).subscribe(data => {
            this.result = data;
            this.attachmentTypes = this.result.irbAttachementTypes;
        });
        this.loadIrbAttachmentList();
    }

    ngOnDestroy() {
        if (this.$subscription1) {
            this.$subscription1.unsubscribe();
        }
    }

    /** To Set the Attachment Type Description
     * @param attachmentTypeCode - selected Attachment Type Code
     */
    setAttachmentType(attachmentTypeCode) {
        this.attachmentTypes.forEach(attachmentType => {
            if (attachmentType.typeCode === attachmentTypeCode) {
                this.attachmentTypeDescription = attachmentType.description;
                this.irbAttachmentProtocol.categoryCode = attachmentType.categoryCode;
            }
        });
    }

    /**calls service to load Attachment list in protocol*/
    loadIrbAttachmentList() {
        this.tabSelected = 'STUDY';
        this.column = 'updateTimestamp';
        this.attachmentSelectedRow = null;
        this.attachmentEditedRow = null;
        this._spinner.show();
        const reqobj = {protocolId: this.requestObject.protocolId, protocolNumber: this.requestObject.protocolNumber};
        this._irbCreateService.getIrbAttachmentList(reqobj).subscribe(data => {
            this.result = data || [];
            this._spinner.hide();
            if (this.result != null) {
              //  this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
                if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                    this.noIrbAttachments = true;
                } else {
                    this.generalInfo = this.result.protocolAttachmentList[0].protocolGeneralInfo;
                    this.irbAttachmentsList = this.result.protocolAttachmentList;
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

    downloadInternalAttachment( attachment ) {
        this._irbCreateService.downloadInternalProtocolAttachments( attachment.protocolCorrespondenceId ).subscribe( data => {
            const a = document.createElement( 'a' );
            const blob = new Blob( [data], { type: data.type } );
            a.href = URL.createObjectURL( blob );
            a.download = attachment.fileName;
            document.body.appendChild(a);
            a.click();

        },
            error => console.log( 'Error downloading the file.'),
            () => console.log( 'OK' ) );
    }
    /** To edit an Attachment
     * @param index-index of the edited attachment object
     * @param attachments-attachment object that is edited
     */
    editAttachments(attachments, index, isCollaboratorEdit) {
        // this.isCollaboratorEdit = isCollaboratorEdit;
        // if (this.isCollaboratorEdit) {
        // this.attachmentSelectedRowCollaborator = index;
        // this.fileName = attachments.protocolAttachments.fileName;
        // } else {
        this.attachmentEditedRow = index;
        this.attachmentSelectedRow = index;
        this.fileName = attachments.protocolAttachmentData.fileName;
        // }
        this.requestObject.attachmentTypeCode = attachments.attachmentType.typeCode;
        this.requestObject.attachmentDescription = attachments.description;
        this.tempEditAttachment = attachments;
        this.tempEditAttachment.acType = 'U';
    }

    saveEditedattachments() {
                // $('#attachmentModal').modal('toggle');
                if (this.tempEditAttachment.acType === 'U') {

                }
                this.attachmentEditedRow = null;
                this.attachmentSelectedRow = null;
                // this.attachmentSelectedRowCollaborator = null;
                this.isAttachmentReplace = false;
                this.tempEditAttachment.protocolId = this.requestObject.protocolId;
                this.tempEditAttachment.attachmentType = {
                    typeCode: this.requestObject.attachmentTypeCode,
                    description: this.attachmentTypeDescription == null ?
                        this.tempEditAttachment.attachmentType.description : this.attachmentTypeDescription,
                    updateTimestamp: this.tempEditAttachment.attachmentType.updateTimestamp,
                    updateUser: this.tempEditAttachment.attachmentType.updateUser
                };
                this.tempEditAttachment.typeCode = this.requestObject.attachmentTypeCode;
                this.tempEditAttachment.description = this.requestObject.attachmentDescription;
                this.tempEditAttachment.updateTimestamp = new Date();
                this.tempEditAttachment.updateUser = this.userDTO.userName;
                this._spinner.show();
                if (!this.isCollaboratorEdit) {
                this._irbCreateService.addattachment(this.tempEditAttachment, this.uploadedFile).subscribe(
                    data => {
                        this.result = data;
                        document.getElementById('closeAttachmentModal').click();
                        if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                            this.noIrbAttachments = true;
                        } else {
                            this.noIrbAttachments = false;
                        }
                        this.irbAttachmentsList = this.result.protocolAttachmentList;
                        this._spinner.hide();
                        this.irbAttachment = data;
                    });
                } else {
                this._irbCreateService.addCollaboratorAttachments(this.tempEditAttachment, this.uploadedFile).subscribe(
                    data => {
                     document.getElementById('closeAttachmentModal').click();
                      this.result = data;
                      this._spinner.hide();
                      this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
                      this.uploadedFile = [];
                      this.requestObject.attachmentDescription = '';
                      this.requestObject.attachmentTypeCode = null;
                      this.isMandatoryFilled = true;
                    });
                }
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

    /**calls the service to add the attachment
     */
    addAttachments() {
        if (this.requestObject.attachmentTypeCode == null
            || this.requestObject.attachmentDescription === '' || this.uploadedFile.length === 0) {
            this.isMandatoryFilled = false;
        } else {
            if (this.uploadedFile.length > 1) {
                this.multipleFile = true;
            } else {
                this.multipleFile = false;
                // $('#attachmentModal').modal('toggle');
                this.irbAttachmentProtocol.acType = 'I';
                this.irbAttachmentProtocol.isProtocolAttachment = 'Y';
                this.irbAttachmentProtocol.protocolNumber = this.requestObject.protocolNumber;
                this.irbAttachmentProtocol.protocolId = this.requestObject.protocolId;
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
                this.irbAttachmentProtocol.attachementStatus = {
                    statusCode: 2,
                    description: 'Complete'
                };
                this.irbAttachmentProtocol.attachmentType = {
                    typeCode: this.requestObject.attachmentTypeCode,
                    description: this.attachmentTypeDescription,
                };
                // this.irbAttachmentProtocol.protocolAttachmentData = {
                //     sequenceNumber: 1,
                //     updateTimestamp: new Date(),
                //     updateUser: this.userDTO.userName

                // };
                this._spinner.show();
                this._irbCreateService.addattachment(this.irbAttachmentProtocol, this.uploadedFile).subscribe(
                    data => {
                        this.result = data;
                document.getElementById('closeAttachmentModal').click();
                        if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                            this.noIrbAttachments = true;
                        } else {
                            this.noIrbAttachments = false;
                        }
                        this.irbAttachmentsList = this.result.protocolAttachmentList;
                        this._spinner.hide();
                        this.irbAttachment = data;
                        this.isMandatoryFilled = true;
                        this.requestObject.attachmentTypeCode = null;
                        this.requestObject.attachmentDescription = '';
                        this.uploadedFile = [];
                    });
            }
        }
    }

    /** Save the attachment before confirming delete
     * @param attachment- attachment object to be deleted
     */
    tempSave(event, attachment) {
        event.preventDefault();
        this.showPopup = true;
        this.tempSaveAttachment = attachment;
    }

    /**Calls the service to delete the attachment after confirmation
   */
    deleteAttachments() {
        this.showPopup = false;
        this.tempSaveAttachment.acType = 'D';
        this.tempSaveAttachment.protocolId = this.requestObject.protocolId;
        this._irbCreateService.addattachment(this.tempSaveAttachment, this.uploadedFile).subscribe(
            data => {
                this.result = data;
                if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                    this.noIrbAttachments = true;
                } else {
                    this.noIrbAttachments = false;
                }
                this.irbAttachmentsList = this.result.protocolAttachmentList;
                this.irbAttachment = data;
            });
    }
    deleteAttachmentsCollaborator() {
        this.tempSaveAttachment.acType = 'D';
        this._irbCreateService.addCollaboratorAttachments(this.tempSaveAttachment, this.uploadedFile).subscribe(
            data => {
                this.result = data;
                this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
                this.uploadedFile = [];
                this.requestObject.attachmentDescription = '';
            });
    }
    /**sort the Attachment list
     */
    sortBy() {
        this.direction = this.direction === 1 ? -1 : 1;
    }

    triggerAdd() {
        document.getElementById('addAttach').click();
       //  $('#addAttach').trigger('click');
    }

    clearAttachmentPopup() {
        this.requestObject.attachmentTypeCode = null;
        this.requestObject.attachmentDescription = null;
        this.attachmentEditedRow = null;
        this.attachmentSelectedRow = null;
       // this.attachmentSelectedRowCollaborator = null;
        this.isAttachmentReplace = false;
        this.uploadedFile = [];
        this.isMandatoryFilled = true;
        this.multipleFile = false;

    }
    replaceAttachment(attachments, index) {
        this.requestObject.attachmentTypeCode = attachments.attachmentType.typeCode;
        this.requestObject.attachmentDescription = attachments.description;
        this.isAttachmentReplace = true;
        this.tempEditAttachment = attachments;
        this.tempEditAttachment.acType = 'R';
       //  this.attachmentEditedRow = index;
       this.attachmentSelectedRow = index;
    }
    getInternalAttachment() {
        this.tabSelected = 'INTERNAL';
        this.column = 'updateTimeStamp';
        this._spinner.show();
        const reqobj = {protocolId: this.requestObject.protocolId};
        this._irbCreateService.loadInternalProtocolAttachments(reqobj).subscribe((data: any) => {
         this._spinner.hide();
            this.internalProtocolAtachmentList = data.internalProtocolAtachmentList != null ? data.internalProtocolAtachmentList : [];
        });
    }
    getVersion(attachments, index) {
        this.versionAttachmentChoosen = attachments;
        this.attachmentSelectedRow = index;
        this._spinner.show();
        this._irbCreateService.loadPreviousProtocolAttachments(attachments.documentId).subscribe((data: any) => {
            this._spinner.hide();
            this.previousProtocolAttachmentList = data.previousProtocolAttachmentList != null ? data.previousProtocolAttachmentList : [];
        });
    }
}

