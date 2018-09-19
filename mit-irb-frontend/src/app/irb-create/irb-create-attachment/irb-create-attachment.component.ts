import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import {IrbViewService} from '../../irb-view/irb-view.service'
import { ActivatedRoute } from '@angular/router';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';

@Component({
  selector: 'app-irb-create-attachment',
  templateUrl: './irb-create-attachment.component.html',
  styleUrls: ['./irb-create-attachment.component.css']
})
export class IrbCreateAttachmentComponent implements OnInit {

  noIrbAttachments = false;
  editAttachment: any = [];
    irbAttachmentsList: any[] = [];
    sortOrder = '1';
    sortField = 'UPDATE_TIMESTAMP';
    direction: number;
    column: any;
    result: any;
    requestObject = {
            protocolNumber: '',
            attachmentId: '',
            attachmentType: 'Type',
            attachmentDescription: ''
    };
    /*Attachment variables */
    fil: FileList;
    uploadedFile: File[] = [];
    files: UploadFile[] = [];
    attachmentList: any[] = [];
    showAddAttachment: boolean = false;
    isMandatoryFilled: boolean = true;
    showPopup: boolean = false;
    tempSaveAttachment: any[] = [];
    isDuplicate: boolean = false;
    @ViewChild('file')
    clearFileVariable: ElementRef;
    
    constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute, public changeRef: ChangeDetectorRef ) { }

    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
        this.requestObject.protocolNumber = '1405006366'  ;
        this.loadIrbAttachmentList();
    }

    /**calls service to load Attachment list in protocol*/
    loadIrbAttachmentList() {
        this._irbViewService.getIrbAttachmentList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbViewProtocolAttachmentList == null || this.result.irbViewProtocolAttachmentList.length === 0 ) {
                    this.noIrbAttachments = true;
                } else {
                    this.irbAttachmentsList = this.result.irbViewProtocolAttachmentList;
                    this.sortBy();
                   
                }
            }

        },
            error => {
                console.log( 'Error in method loadIrbAttachmentList()', error );
            },
        );
    }

    /**calls service to get attachment details and to download it
     * @param attachment - object of an attachment
     */
    downloadAttachment( attachment ) {
        this.requestObject.attachmentId = attachment.FILE_ID;
        this._irbViewService.downloadIrbAttachment( attachment.FILE_ID ).subscribe( data => {
            const a = document.createElement( 'a' );
            const blob = new Blob( [data], { type: data.type } );
            a.href = URL.createObjectURL( blob );
            a.download = attachment.FILE_NAME;
            a.click();

        },
            error => console.log( 'Error downloading the file.'),
            () => console.log( 'OK' ) );
    }
    editAttachments(event: any, index, attachments) {
      event.preventDefault();
      this.editAttachment[index] = !this.editAttachment[index];
      
  }
  cancelEditedattachments(event: any,index, attachments)
  {
    event.preventDefault();
    this.editAttachment[index] = !this.editAttachment[index];
  }
  saveEditedattachments(event: any,index, attachments)
  {
    event.preventDefault();
    this.editAttachment[index] = !this.editAttachment[index];
  }
  
  onChange(files: FileList) {debugger
        this.fil = files;
        this.isDuplicate=false;
        if(this.uploadedFile.length === 0)
            for (var i = 0; i < this.fil.length; i++)
                this.uploadedFile.push(this.fil[i]);
        else{
            for (var i = 0; i < this.fil.length; i++) {
                     this.isDuplicate=false;
                    for (var j = 0; j < this.uploadedFile.length; j++){
                        if(this.fil[i].name == this.uploadedFile[j].name )
                        {
                            this.isDuplicate=true;
                            //this.clearFileVariable.nativeElement.value = "";
                        }
                    }
                    if(!this.isDuplicate)
                            this.uploadedFile.push(this.fil[i]);
                           
                        
                }
            }
        this.changeRef.detectChanges();
}

public dropped(event: UploadEvent) {
    this.files = event.files;
    for (let file of this.files) {
        this.attachmentList.push(file);
    }
    for (const file of event.files) {
        if (file.fileEntry.isFile) {
            const fileEntry = file.fileEntry as FileSystemFileEntry;
            fileEntry.file((info: File) => {
                if(this.uploadedFile.length === 0)
                    this.uploadedFile.push(info);
                else{
                    for(var i=0;i<this.uploadedFile.length;i++)
                        if(this.uploadedFile[i].name == info.name )
                             this.isDuplicate=true;
                     if(!this.isDuplicate){
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
    for (var i = 0; i < this.uploadedFile.length; i++) {
        if (this.uploadedFile[i].name == item.name) {
            this.uploadedFile.splice(i, 1);
            this.changeRef.detectChanges();
        }
        this.clearFileVariable.nativeElement.value = "";
        this.isDuplicate =  false;
    }
}
showAddAttachmentPopUp(e) {
  e.preventDefault();
  this.showAddAttachment = true;
  this.uploadedFile = [];
  this.requestObject.attachmentType= 'Type';
  this.requestObject.attachmentDescription="";
  this.isMandatoryFilled=true;
  this.changeRef.detectChanges();
}

addAttachments()
{
  if(this.requestObject.attachmentType == "Type" || this.requestObject.attachmentDescription == "" || this.uploadedFile.length == 0 )
    this.isMandatoryFilled = false;
  else{
    this.isMandatoryFilled=true;
    this.requestObject.attachmentType= 'Type';
    this.requestObject.attachmentDescription="";
    this.uploadedFile = [];
    this.clearFileVariable.nativeElement.value = "";
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
  event.preventDefault();
  this.showPopup = false;
}
sortBy() {
    this.column = this.sortField;
    this.direction = parseInt( this.sortOrder, 10 );
}


}
