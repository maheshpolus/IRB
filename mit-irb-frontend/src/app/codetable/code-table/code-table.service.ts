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
        return this._http.post('/mit-irb/getCodeTable', codetable);
    }

    getUpdatedTableValues(updatedCodetable) {
        return this._http.post('/mit-irb/updateCodeTableRecord', updatedCodetable);
    }

    removeSelectedData(removeData) {
        return this._http.post('/mit-irb/deleteCodeTableRecord', removeData);
    }

    addNewCodeTableData(newCodeTableData) {
        return this._http.post('/mit-irb/addCodeTableRecord', newCodeTableData);
    }
}
