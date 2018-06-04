import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import 'rxjs/Rx';

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

    ngOnInit() {
        this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get( 'protocolNumber' );
        this.loadIrbAttachmentList();
    }

    loadIrbAttachmentList() {
        this._irbViewService.getIrbAttachmentList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if ( this.result.irbviewProtocolAttachmentList == null || this.result.irbviewProtocolAttachmentList.length === 0 ) {
                    this.noIrbAttachments = true;
                } else {
                    this.irbAttachmentsList = this.result.irbviewProtocolAttachmentList;
                    this.sortBy();
                }
            }

        },
            error => {
                console.log( "Error in method loadIrbAttachmentList()", error );
            },
        );
    }

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

    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt( this.sortOrder, 10 );
    }
}
