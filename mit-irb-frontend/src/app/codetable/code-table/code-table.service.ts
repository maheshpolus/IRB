import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()

export class CodeTableService {

    constructor(private _http: HttpClient) { }

    /**
     * @param  {} toastId
     * to show toaster messages
     */
    showToast(toastId) {
        toastId.className = 'show';
        setTimeout(function () {
        toastId.className = toastId.className.replace('show', '');
        }, 1000);
    }

    getCodetableValues(codetable) {
        return this._http.post('/irb/getCodeTable', codetable);
    }

    getUpdatedTableValues(updatedCodetable, attachmentColumnName) {
        const formData = new FormData();
        if (attachmentColumnName) {
            const attachment = updatedCodetable.tableData[0][attachmentColumnName];
            updatedCodetable.tableData[0][attachmentColumnName] = 'changed';
            formData.append('files', attachment);
        }
        formData.append('formDataJson', JSON.stringify(updatedCodetable));
        return this._http.post( '/irb/updateCodeTableRecord', formData);
    }

    removeSelectedData(removeData) {
        return this._http.post( '/irb/deleteCodeTableRecord', removeData);
    }

    addNewCodeTableData(newCodeTableData, attachmentColumnName) {
        const formData = new FormData();
        if (attachmentColumnName) {
            const attachment = newCodeTableData.tableData[0][attachmentColumnName];
            newCodeTableData.tableData[0][attachmentColumnName] = 'changed';
            formData.append('files', attachment);
        }
        formData.append('formDataJson', JSON.stringify(newCodeTableData));
        return this._http.post( '/irb/addCodeTableRecord', formData);
    }
    downloadAttachment(downloadData) {
        return this._http.post( '/irb/downloadAttachment', downloadData , {responseType: 'blob'});
    }
}
