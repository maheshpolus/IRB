import { Component, Input, OnChanges } from '@angular/core';
import { Router } from '@angular/router';


@Component( {
    selector: 'app-exempt-card',
    templateUrl: './exempt-card.component.html',
    styleUrls: ['./exempt-card.component.css']
} )
export class ExemptCardComponent implements  OnChanges  {

    /**input from parent componet Exempt list */
    @Input() exemptList: any = []; // Full list of exemted protocols irrespective of statuses
    @Input() userDTO: any;
    @Input() showBtn: boolean;

    statusCode: string[];
    tabSelected = 'STUDIES';
    filteredExemptList: any = []; // List of exempt protocols filtered based on statuses
    paginatedExemptList: any = []; // List of exempt protocols paginated from 'filteredExemptList'
    paginationData = {
        limit       : 10,
        page_number : 1,
      };
      protocolCount = 10;


    constructor( private _router: Router ) { }


    ngOnChanges(changes) {
        this.showData( this.userDTO.role, this.tabSelected );
    }

    /**Opens Expemt Exempt Component
     * @exemptId - unique id for exempt form
     * @mode - view or edit mode
     * @add question ID to route as query params
    */
    openExempt( exemptId, mode, exemptTitle, exemptFormID ) {
        if ( mode == null ) {
            mode = 1;
        }
        this._router.navigate( ['/irb/exempt-questionaire'],
            { queryParams: { exempHeaderId: exemptId, mode: mode, title: exemptTitle, exemptFormID: exemptFormID } } );
    }

    public showData( userRole, tabClicked ) {
        this.paginationData.page_number = 1;
        this.tabSelected = tabClicked;
        this.statusCode = this.getStatusCode( userRole, tabClicked );
        this.filteredExemptList = this.filterExemptFullList(this.statusCode, this.userDTO.personID, this.tabSelected);
        this.paginatedExemptList = this.filteredExemptList.slice(0, this.paginationData.limit);
        this.protocolCount = this.filteredExemptList.length;
    }

    getStatusCode( role, tabClicked ) {
        if ( (role === 'PI' || role === 'DEPT_ADMIN') && this.userDTO.jobTitle !== null && tabClicked === 'PENDING' ) {
            return ['1', '2', '5'];
        } else if (( role === 'PI' || role === 'DEPT_ADMIN') && this.userDTO.jobTitle !== null && tabClicked === 'STUDIES' ) {
            return ['1', '2', '3', '4', '5'];
        } else if ( ( role === 'ADMIN' || role === 'CHAIR' ) && tabClicked === 'STUDIES' ) {
            return ['1'];
        } else if ( ( role === 'ADMIN' || role === 'CHAIR' ) && tabClicked === 'PENDING' ) {
            return ['3', '5', '2'];
        } else if ( ( role === 'ADMIN' || role === 'CHAIR' ) && tabClicked === 'SUBMITTED' ) {
            return ['4'];
        } else if ( (role === 'PI' || role === 'DEPT_ADMIN') && this.userDTO.jobTitle == null && tabClicked === 'STUDIES' ) {
            return ['1', '2', '3', '4', '5'];
        } else if ( (role === 'PI' || role === 'DEPT_ADMIN') && this.userDTO.jobTitle == null && tabClicked === 'PENDING' ) {
            return ['5'];
        }
    }


    /**
     * @param  {} statusCodes
     * @param  {} personID
     * @param  {} tabSelected
     * Filters the full Exempt protocol list based on STATUES and Role of USERS
     * returns the filtered list
     */
    filterExemptFullList(statusCodes, personID, tabSelected) {
        let filteredArray = [];
        if (!this.exemptList) {
            filteredArray = [];
        } else {
            filteredArray = this.exemptList.filter((value) => {
                for (let i = 0; i < statusCodes.length; i++) {
                    if (value.statusCode === statusCodes[i]) {
                        if ((value.statusCode === '2' || value.statusCode === '5' || value.statusCode === '1')
                            && tabSelected === 'PENDING') {
                            if (value.statusCode === '2' && value.facultySponsorPersonId === personID) {
                                return value;
                            }
                            if (value.statusCode === '5' && value.personId === personID) {
                                return value;
                            }
                            if (value.statusCode === '1' && value.submittedOnce === 1 && value.personId === personID) {
                                return value;
                            }
                        } else if (tabSelected === 'STUDIES' && (value.statusCode === '1' || value.statusCode === '5')) {
                            if (value.personId === personID && value.statusCode === '1') {
                                return value;
                            }
                            if (value.personId === personID && value.statusCode === '5') {
                                return value;
                            }
                        } else {
                            return value;
                        }
                    }
                }
            });
        }
        return filteredArray;

    }

    /**
     * @param  {} pageNumber
     * paginate an array
     */
    paginateExempList(pageNumber) {
        this.paginatedExemptList = this.filteredExemptList.slice(pageNumber * this.paginationData.limit - this.paginationData.limit,
            pageNumber * this.paginationData.limit);
        document.getElementById('scrollToTop').scrollTop = 0;
    }
}
