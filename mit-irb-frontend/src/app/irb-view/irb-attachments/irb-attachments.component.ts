import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { IrbViewService } from '../irb-view.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component( {
    selector: 'app-irb-attachments',
    templateUrl: './irb-attachments.component.html',
    styleUrls: ['./irb-attachments.component.css']
} )
export class IrbAttachmentsComponent implements OnInit {

    noIrbAttachments = false;

    irbAttachmentsList: any[] = [];
    internalProtocolAtachmentList: any[] = [];
    previousProtocolAttachmentList: any[] = [];
    result: any;
    attachmentSelectedRow: number;
    versionAttachmentChoosen: any = {};
    direction = 1;
    column = 'updateTimestamp';
    tabSelected = 'STUDY';

    requestObject = {
            protocolNumber: '',
            attachmentId: '',
            protocolId: ''
    };

   PROTOCOL_ATTACHMENTS_INFO: string;

    constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute, private _http: HttpClient,
        private _spinner: NgxSpinnerService ) { }
    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
        this._http.get('/mit-irb/resources/string_config_json').subscribe(
            data => {
                const property_config: any = data;
                if (property_config) {
                    this.PROTOCOL_ATTACHMENTS_INFO = property_config.PROTOCOL_ATTACHMENTS_INFO;
                }
            });
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get( 'protocolNumber' );
        this.requestObject.protocolId = this._activatedRoute.snapshot.queryParamMap.get( 'protocolId' );
        this.loadIrbAttachmentList();
    }

    /**calls service to load Attachment list in protocol*/
    // loadIrbAttachmentList() {
    //     this._irbViewService.getIrbAttachmentList( this.requestObject ).subscribe( data => {
    //         this.result = data || [];
    //         if ( this.result != null ) {
    //             if ( this.result.irbViewProtocolAttachmentList == null || this.result.irbViewProtocolAttachmentList.length === 0 ) {
    //                 this.noIrbAttachments = true;
    //             } else {
    //                 this.irbAttachmentsList = this.result.irbViewProtocolAttachmentList;
    //             }
    //         }

    //     },
    //         error => {
    //             console.log( 'Error in method loadIrbAttachmentList()', error );
    //         },
    //     );
    // }
    loadIrbAttachmentList() {
        this.tabSelected = 'STUDY';
        this.column = 'updateTimestamp';
        this.attachmentSelectedRow = null;
        this._spinner.show();
        const reqobj = {protocolId: this.requestObject.protocolId, protocolNumber: this.requestObject.protocolNumber};
        this._irbViewService.getIrbAttachmentList(reqobj).subscribe(data => {
            this.result = data || [];
            this._spinner.hide();
            if (this.result != null) {
              //  this.protocolCollaboratorAttachmentsList = this.result.protocolCollaboratorAttachmentsList;
                if (this.result.protocolAttachmentList == null || this.result.protocolAttachmentList.length === 0) {
                    this.noIrbAttachments = true;
                } else {
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
    downloadAttachment( attachment ) {
        this._irbViewService.downloadIrbAttachment( attachment.protocolAttachmentData.fileId ).subscribe( data => {
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

    downloadInternalAttachment( attachment ) {
        this._irbViewService.downloadInternalProtocolAttachments( attachment.protocolCorrespondenceId ).subscribe( data => {
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

    /**set column and direction to sort attachments */
    sortBy() {
        this.direction = this.direction === 1 ? -1 : 1;
    }

    getInternalAttachment() {
        this.tabSelected = 'INTERNAL';
        this.column = 'updateTimeStamp';
        const reqobj = {protocolId: this.requestObject.protocolId};
        this._irbViewService.loadInternalProtocolAttachments(reqobj).subscribe((data: any) => {
          this.internalProtocolAtachmentList = data.internalProtocolAtachmentList != null ? data.internalProtocolAtachmentList : [];
        });
    }

    getVersion(attachments, index) {
        this.attachmentSelectedRow = index;
        this.versionAttachmentChoosen = attachments;
        this._irbViewService.loadPreviousProtocolAttachments(attachments.documentId).subscribe((data: any) => {
            this.previousProtocolAttachmentList = data.previousProtocolAttachmentList != null ? data.previousProtocolAttachmentList : [];
        });
    }
}
