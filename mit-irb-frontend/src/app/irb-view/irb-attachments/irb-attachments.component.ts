import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IrbViewService } from '../irb-view.service';

@Component( {
    selector: 'app-irb-attachments',
    templateUrl: './irb-attachments.component.html',
    styleUrls: ['./irb-attachments.component.css']
} )
export class IrbAttachmentsComponent implements OnInit {

    noIrbAttachments = false;

    irbAttachmentsList: any[] = [];
    result: any;
    sortOrder = '1';
    sortField = 'UPDATE_TIMESTAMP';
    direction: number;
    column: any;

    requestObject = {
            protocolNumber: '',
            attachmentId: ''
    };

    constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute, ) { }

    /** sets requestObject and call function to load attachment details */
    ngOnInit() {
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get( 'protocolNumber' );
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

    /**set column and direction to sort attachments */
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt( this.sortOrder, 10 );
    }
}
