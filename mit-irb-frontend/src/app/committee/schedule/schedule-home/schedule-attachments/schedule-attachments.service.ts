import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable()
export class ScheduleAttachmentsService {
    formData = new FormData();
    constructor( private http: HttpClient) {

    }

    public addAttachments( scheduleId, newCommitteeScheduleAttachment: Object, attachmentTypeCode, uploadedFile, Description, currentUser ):
     Observable<JSON> {
        this.formData.delete( 'file' );
        this.formData.delete( 'formDataJson' );
        for ( let i = 0; i < uploadedFile.length; i++ ) {
            this.formData.append( 'files', uploadedFile[i] );
        }
        const sendObject = {
            scheduleId: scheduleId,
            newCommitteeScheduleAttachment: newCommitteeScheduleAttachment
        };
        this.formData.append( 'formDataJson', JSON.stringify( sendObject ) );
        return this.http.post( '/mit-irb/addScheduleAttachment', this.formData )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    public deleteAttachments( scheduleId, committeeId, commScheduleAttachId ): Observable<JSON> {
        const params = {
            committeeId: committeeId,
            scheduleId: scheduleId,
            commScheduleAttachId: commScheduleAttachId
        };
        return this.http.post( '/mit-irb/deleteScheduleAttachment', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }

    downloadAttachment( commScheduleAttachId: string, mimeType ): Observable<any> {
        return this.http.get( '/mit-irb/downloadScheduleAttachment', {
            headers: new HttpHeaders().set('commScheduleAttachId', commScheduleAttachId.toString()),
            responseType: 'blob'
        } );
    }

    updateScheduleAttachments(committeeId: string, scheduleId: number, newCommitteeScheduleAttachment: Object): Observable<JSON> {
        const params = {
                committeeId: committeeId,
                scheduleId: scheduleId,
                newCommitteeScheduleAttachment: newCommitteeScheduleAttachment
        };
        return this.http.post( '/mit-irb/updateScheduleAttachment', params )
            .catch( error => {
                console.error( error.message || error );
                return Observable.throw( error.message || error );
            } );
    }
}
