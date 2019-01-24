import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';

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
    isUpArrow = true;
    isTimeStampSorting = true;
    direction = 1;
    column = 'UPDATE_TIMESTAMP';

    requestObject = {
            protocolNumber: '',
            attachmentId: ''
    };

   PROTOCOL_ATTACHMENTS_INFO: string;

    constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute, private _http: HttpClient ) { }
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
            document.body.appendChild(a);
            a.click();

        },
            error => console.log( 'Error downloading the file.'),
            () => console.log( 'OK' ) );
    }

    /**set column and direction to sort attachments */
    sortBy(column) {
        this.isUpArrow = !this.isUpArrow;
        this.isTimeStampSorting = column === 'UPDATE_TIMESTAMP' ? true : false;
        this.column = column;
        this.direction = this.direction === 1 ? -1 : 1;
    }
}
