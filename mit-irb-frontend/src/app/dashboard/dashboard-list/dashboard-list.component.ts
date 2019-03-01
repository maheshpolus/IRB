import { Component, OnInit, Input, NgZone, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { ElasticService } from '../../common/service/elastic.service';
import { PiElasticService } from '../../common/service/pi-elastic.service';
import { DashboardService } from '../dashboard.service';
import { SharedDataService } from '../../common/service/shared-data.service';
import { KeyPressEvent } from '../../common/directives/keyPressEvent.component';

declare var $: any;
@Component({
    selector: 'app-dashboard-list',
    templateUrl: './dashboard-list.component.html',
    styleUrls: ['./dashboard-list.component.css']
})

export class DashboardListComponent implements OnInit, AfterViewInit {

    noProtocolList: boolean;
    noIrbList = false;
    isAdvancesearch = false;
    isAdvancsearchPerformed = false;

    @Input() userDTO: any;
    lastClickedTab = 'ALL';
    requestObject: any = {
        personId: '',
        personRoleType: '',
        protocolNumber: '',
        title: '',
        piName: '',
        protocolTypeCode: '',
        protocolStatusCode: '',
        dashboardType: '',
        determination: '',
        isAdvancedSearch: 'N',
        exemptFormStartDate: '',
        exemptFormEndDate: '',
        exemptFormfacultySponsorName: ''
    };
    paginationData = {
        limit       : 10,
        page_number : 1,
      };
    protocolCount: number;
    protocolTypeList = [];
    result: any;
    irbListData: any = [];
    paginatedIrbListData: any = [];
    exemptListData: any = [];
    roleType: string;
    protocolStatus = '';
    direction: number;
    column: any;
    sortOrder = '1';
    sortField = 'UPDATE_TIMESTAMP';
    protocolStatuses: any = [];
    protocolStatusesCopy: any = [];
    isCheckBoxChecked = {};

    /**elastic search variables */
    message = '';
    _results: Subject<Array<any>> = new Subject<Array<any>>();
    _piResults: Subject<Array<any>> = new Subject<Array<any>>();
    searchText: FormControl = new FormControl('');
    piSearchText: FormControl = new FormControl('');
    fsSearchText: FormControl = new FormControl('');
    searchTextModel: string;
    iconClass: any;
    IsElasticResult = false;
    IsElasticResultPI = false;
    IsElasticResultFS = false;
    protocol: string;
    protocolType: string;
    title: string;
    leadUnit: string;
    status: string;
    statusSearchText = '';
    piName: string;
    expiry_date: string;
    elasticResultTab = false;

    exemptParams: any = {
        personId: '',
        personRoleType: '',
        title: '',
        piName: '',
        determination: '',
        exemptFormStartDate: null,
        exemptFormEndDate: null,
        exemptFormfacultySponsorName: null
    };

    constructor(private _dashboardService: DashboardService,
        private _ngZone: NgZone,
        private _elasticsearchService: ElasticService,
        private _piElasticsearchService: PiElasticService,
        private _router: Router,
        private _sharedDataService: SharedDataService,
        public keyPressEvent: KeyPressEvent,
        private _spinner: NgxSpinnerService) { }

    /** load protocol list based on tab and load protocoltypes to show in advance search field */
    ngOnInit() {
        this.roleType = this.userDTO.role;
        this._dashboardService.getProtocolStatusList(null).subscribe(data => {
            this.result = data;
            if (this.result.dashBoardDetailMap != null || this.result.dashBoardDetailMap !== undefined ) {
            this.protocolStatuses = this.result.dashBoardDetailMap;
            this.protocolStatusesCopy = Object.assign([], this.protocolStatuses);
            }
        });
        this._sharedDataService.currentTab.subscribe(data => {
            if (data == null) {
                this.lastClickedTab = (this.roleType === 'ADMIN' || this.roleType === 'CHAIR') ? 'ALL' : 'ACTIVE';
            } else {
            this.lastClickedTab = data;
            }
        });
        if (this._sharedDataService.isAdvancesearch) {
            this.requestObject = this._sharedDataService.searchData !== null ?  this._sharedDataService.searchData : this.requestObject;
            if (this.requestObject !== null && this.requestObject.protocolStatusCode !== ''
                && this.requestObject.protocolStatusCode !== undefined) {
                this.protocolStatus =  this._sharedDataService.searchedStatus;
            }
            this.isAdvancesearch = true;
            this.isAdvancsearchPerformed = true;
        }
        if (this.lastClickedTab === 'EXEMPT') {
            this.getExemptListData(this.lastClickedTab);
        } else if (this.lastClickedTab === 'COMMITTEE' || this.lastClickedTab === 'SCHEDULE') {
            this.getCommitteeScheduleListData(this.lastClickedTab);
        } else {
            this.getIrbListData(this.lastClickedTab);
        }
        this.getIrbProtocolTypes();
    }

    /*logic for elastic search*/
    ngAfterViewInit() {
        this.searchText
            .valueChanges
            .map((text: any) => text ? text.trim() : '')
            .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
            .debounceTime(500)
            .distinctUntilChanged()
            .switchMap(searchString => {
                return new Promise<Array<String>>((resolve, reject) => {
                    this._ngZone.runOutsideAngular(() => {
                        let hits_source: Array<any> = [];
                        let hits_highlight: Array<any> = [];
                        const hits_out: Array<any> = [];
                        const results: Array<any> = [];
                        let protocol_number: string;
                        let title: string;
                        let protocol_type: string;
                        let personName: string;
                        // let lead_unit: string;
                        let exp_date: string;
                        let test;
                        this._elasticsearchService.irbSearch(searchString)
                            .then((searchResult) => {
                                this._ngZone.run(() => {
                                    hits_source = ((searchResult.hits || {}).hits || [])
                                        .map((hit) => hit._source);
                                    hits_highlight = ((searchResult.hits || {}).hits || [])
                                        .map((hit) => hit.highlight);

                                    hits_source.forEach((elmnt, j) => {
                                        protocol_number = hits_source[j].protocol_number;
                                        title = hits_source[j].title;
                                        // lead_unit = hits_source[j].lead_unit_name;
                                        exp_date = hits_source[j].expiration_date;
                                        protocol_type = hits_source[j].protocol_type;
                                        personName = hits_source[j].pi_name;
                                        status = hits_source[j].status;
                                        test = hits_source[j];

                                        if (typeof (hits_highlight[j].protocol_number) !== 'undefined') {
                                            protocol_number = hits_highlight[j].protocol_number;
                                        }
                                        if (typeof (hits_highlight[j].title) !== 'undefined') {
                                            title = hits_highlight[j].title;
                                        }
                                        /* if ( typeof ( hits_highlight[j].lead_unit_name ) !== 'undefined' ) {
                                             lead_unit = hits_highlight[j].lead_unit_name;
                                         }
                                         if ( typeof ( hits_highlight[j].lead_unit_number ) !== 'undefined' ) {
                                             unit_number = hits_highlight[j].lead_unit_number;
                                         }*/
                                        if (typeof (hits_highlight[j].protocol_type) !== 'undefined') {
                                            protocol_type = hits_highlight[j].protocol_type;
                                        }
                                        if (typeof (hits_highlight[j].pi_name) !== 'undefined') {
                                            personName = hits_highlight[j].pi_name;
                                        }
                                        if (typeof (hits_highlight[j].status) !== 'undefined') {
                                            status = hits_highlight[j].status;
                                        }
                                        results.push({
                                            label: protocol_number + '  :  ' + title
                                                /*+ '  |  ' + unit_number
                                                + '  |  ' + lead_unit*/
                                                + '  |  ' + protocol_type
                                                + '  |  ' + personName
                                                + '  |  ' + status,
                                            obj: test
                                        });
                                    });
                                    if (results.length > 0) {
                                        this.message = '';
                                    } else {
                                        if (this.searchTextModel && this.searchTextModel.trim()) {
                                            this.message = 'nothing was found';
                                        }
                                    }
                                    resolve(results);
                                });

                            })
                            .catch((error) => {
                                this._ngZone.run(() => {
                                    reject(error);
                                });
                            });
                    });
                });
            })
            .catch(this.handleError)
            .subscribe(this._results);
            this.piSearchText
            .valueChanges
            .map((text: any) => text ? text.trim() : '')
            .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
            .debounceTime(500)
            .distinctUntilChanged()
            .switchMap(searchString => {
                return this.getElasticSearchResults(searchString);
            })
            .catch(this.handleError)
            .subscribe(this._piResults);
            this.fsSearchText
            .valueChanges
            .map((text: any) => text ? text.trim() : '')
            .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
            .debounceTime(500)
            .distinctUntilChanged()
            .switchMap(searchString => {
                return this.getElasticSearchResults(searchString);
            })
            .catch(this.handleError)
            .subscribe(this._piResults);
    }
    
        getElasticSearchResults(searchString) {
        return new Promise<Array<String>>((resolve, reject) => {
            this._ngZone.runOutsideAngular(() => {
                let hits_source: Array<any> = [];
                let hits_highlight: Array<any> = [];
                const hits_out: Array<any> = [];
                const results: Array<any> = [];
                let personName: string;
                let test;
                this._piElasticsearchService.irbSearch(searchString)
                    .then((searchResult) => {
                        this._ngZone.run(() => {
                            hits_source = ((searchResult.hits || {}).hits || [])
                                .map((hit) => hit._source);
                            hits_highlight = ((searchResult.hits || {}).hits || [])
                                .map((hit) => hit.highlight);

                            hits_source.forEach((elmnt, j) => {
                                personName = hits_source[j].full_name;
                                test = hits_source[j];

                                if (typeof (hits_highlight[j].full_name) !== 'undefined') {
                                    personName = hits_highlight[j].full_name;
                                }
                                results.push({
                                    label: personName,
                                    obj: test
                                });
                            });
                            if (results.length > 0) {
                                this.message = '';
                            } else {
                                if (this.requestObject.piName && this.requestObject.piName.trim()) {
                                    this.message = 'nothing was found';
                                }
                            }
                            resolve(results);
                        });

                    })
                    .catch((error) => {
                        this._ngZone.run(() => {
                            reject(error);
                        });
                    });
            });
        });
    }

    

    /** handles error in elastic search */
    handleError(): any {
        this.message = 'something went wrong';
    }

    /** changes the icon in searchbox*/
    protocolSearchValueChange() {
        this.iconClass = this.searchTextModel ? 'fa fa-times fa-med' : 'fa fa-search fa-med';
    }

    /** clears the text entered for elastic search*/
    clearsearchBox(e) {
        e.preventDefault();
        this.searchTextModel = '';
    }

    /**
     * @param  {} id
     */
    onKeyEnterElastic(id) {
        $('#' + id + ' li.selected').trigger('click');
    }


    /**get advance search results
     * @param currentTab - value of current tab
     */
    getAdvanceSearch(currentTab) {
        this.isAdvancsearchPerformed = true;
        this.getIrbListData(currentTab);
    }

    /** calls service to load irb protocols in dashboard
     * @param currentTab - value of current tab
     */
    getIrbListData(currentTab) {
        this.paginationData.page_number = 1;
        this.irbListData = [];
        this.paginatedIrbListData = [];
        this.protocolCount = 0;
        this.noIrbList = false;
        this.searchTextModel = '';
        this.lastClickedTab = currentTab;
         if (!this.isAdvancesearch) {
            this.requestObject = { protocolTypeCode: '',
                                   determination: ''
                                    };
            this.requestObject.isAdvancedSearch = 'N';
        } else {
            this.requestObject.isAdvancedSearch = 'Y';
        }
        this._sharedDataService.changeCurrentTab(currentTab);
        this.requestObject.personId = this.userDTO.personID;
        this.requestObject.personRoleType = this.userDTO.role;
        this.requestObject.dashboardType = currentTab;
        this._sharedDataService.searchData = this.requestObject;
        if ((this.roleType === 'ADMIN' || this.roleType === 'CHAIR') && !this.isAdvancsearchPerformed && currentTab === 'ALL') {
            // No need to call service since no data will be shown for admin users by default
            // (only if advance serach is performed data will be shown in admin 'ALL' tab
        } else{
            this._spinner.show();
            this._dashboardService.getIrbList(this.requestObject).subscribe(data => {
                this.result = data || [];
                this._spinner.hide();
                if (this.result != null) {
                    if (this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length === 0) {
                        this.noIrbList = true;
                    } else {
                        this.irbListData = this.result.dashBoardDetailMap;
                        this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
                        this.protocolCount = this.irbListData.length;
                        this.sortBy();
                    }
                }
            },
                error => {
                    console.log('Error in method getIrbListData()', error);
                },
            );
        }

    }

    /** sets value of direction to implement sorting in tabs other than EXEMPT, COMMITEE, SCHEDULE*/
    updateSortOrder() {
        this.sortOrder = this.sortOrder === '1' ? '-1' : '1';
        this.sortBy();
    }

    /**
     * @param  {} array
     * @param  {} column
     * @param  {} direction
     * Sorts an input array with specified column in specified direction
     */
    sortAnArray(array, column, direction) {
        const sortedArray = array.sort(function(a, b) {
            if (a[column].toLowerCase() < b[column].toLowerCase()) {
                return 1 * direction;
            } else if ( a[column].toLowerCase() > b[column].toLowerCase()) {
                return -1 * direction;
            } else {
                return 0;
            }
        });
        return sortedArray;
    }

    /** sets value of column and direction to implement sorting */
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt(this.sortOrder, 10);
        this.irbListData = this.sortAnArray(this.irbListData, this.column, this.direction);
        this.protocolListPerPage(this.paginationData.page_number); // Paginate after sorting entire list
    }

    /*show or hide advance search fields*/
    showAdvanceSearch() {
        this.isAdvancesearch = !this.isAdvancesearch;
        this._sharedDataService.isAdvancesearch = this.isAdvancesearch;
        this.isAdvancsearchPerformed = false;
       // this._sharedDataService.searchData = null;
    }

    /** assigns values of selected result from elastic search results
     * @param result - elastic search selected result object
     */
    selectedResult(result) {
        this.protocol = result.obj.protocol_number;
        this.protocolType = result.obj.protocol_type;
        this.piName = result.obj.pi_name;
        this.expiry_date = result.obj.expiration_date;
        this.title = result.obj.title;
        this.status = result.obj.status;
        this.elasticResultTab = true;
        this.searchTextModel = '';
    }

    /** method to close elastic search result*/
    closeResultTab() {
        this.searchTextModel = '';
        this.elasticResultTab = false;
    }

    /** clear all fields in advance search and load orginal irb protocol list if none of the fields where empty*/
    clear() {
        if (this.requestObject.protocolNumber !== '' || this.requestObject.title !== '' ||
            this.requestObject.piName !== '' || this.requestObject.protocolTypeCode !== '' ||
            this.requestObject.protocolStatusCode !== '' ||
            this.requestObject.approvalDate !== '' || this.requestObject.expirationDate !== '') {
            this.requestObject.protocolNumber = '';
            this.requestObject.title = '';
            this.requestObject.piName = '';
            this.requestObject.isAdvancedSearch = 'N';
            this.requestObject.approvalDate = '';
            this.requestObject.expirationDate = '';
            this.requestObject.protocolTypeCode = '';
            this.requestObject.protocolStatusCode = '';
            this.protocolStatus = '';
            this.isCheckBoxChecked = {};
        // No need for backend call to clear data in ALL PROTOCOLS for Admins
        if ((this.roleType === 'ADMIN' || this.roleType === 'CHAIR') && this.lastClickedTab === 'ALL') {
                    this.irbListData = [];
                    this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
                    this.protocolCount = this.irbListData.length;
                    this.sortBy();
        } else {
            this.getIrbListData(this.lastClickedTab);
        }
        // this.getIrbListData(this.lastClickedTab);
        this.isAdvancsearchPerformed = false;
        this._sharedDataService.searchData = null;
    }
    }

    /**opens irb view module of a selected protocol
     * @param protocolNumber -unique identifier of a protocol
     */
    openIrb(protocolNumber) {
        const reqstobj = {'protocolNumber': protocolNumber, 'personId': this.userDTO.personID};
        this._dashboardService.checkingPersonsRightToViewProtocol(reqstobj).subscribe(data => {
            const response: any = data;
            if (response.userHasRightToViewProtocol === 1) {
                this._router.navigate(['/irb/irb-view/irbOverview'], { queryParams: { protocolNumber: protocolNumber } });
            } else {
                document.getElementById('openWarningModalButton').click();
            }
        });
    }

    /**fetch protocolTypes to show in dropdown*/
    getIrbProtocolTypes() {
        this._dashboardService.getProtocolType().subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length === 0) {
                    this.noProtocolList = true;
                } else {
                    this.protocolTypeList = this.result.dashBoardDetailMap;
                }
            }
        },
            error => {
                console.log('Error in method getIrbProtocolTypes()', error);
            },
        );
    }

    /**fetch exempt protocols
     * @param currentTab - value of current tab
    */
    getExemptListData(currentTab) {
        this.exemptListData = [];
        this.noIrbList = false;
        this.lastClickedTab = currentTab;
        if (!this.isAdvancesearch) {
            this.requestObject = { protocolTypeCode: '',
                                   determination: '' };
            this.exemptParams = {};
        }
        this._sharedDataService.changeCurrentTab(currentTab);
        this._sharedDataService.searchData = this.requestObject;
        this.exemptParams.personId = this.userDTO.personID;
        this.exemptParams.personRoleType =
            (this.userDTO.role === 'CHAIR' || this.userDTO.role === 'ADMIN') ? 'IRB_ADMIN' : this.userDTO.role;
        if (this.userDTO.jobTitle == null && this.exemptParams.personRoleType === 'PI') {
            this.exemptParams.personRoleType = 'STUDENT';
        }
        this.exemptParams.piName = this.requestObject.piName;
        this.exemptParams.title = this.requestObject.title;
        this.exemptParams.determination = this.requestObject.determination;
        if (this.requestObject.exemptFormStartDate != null && this.requestObject.exemptFormStartDate !== '') {
            this.exemptParams.exemptFormStartDate = this.GetFormattedDate(this.requestObject.exemptFormStartDate);
        }
        if (this.requestObject.exemptFormEndDate != null && this.requestObject.exemptFormEndDate !== '') {
            this.exemptParams.exemptFormEndDate = this.GetFormattedDate(this.requestObject.exemptFormEndDate);
        }
        this.exemptParams.exemptFormfacultySponsorName = this.requestObject.exemptFormfacultySponsorName;
        this._spinner.show();
        this._dashboardService.getExemptProtocols(this.exemptParams).subscribe(data => {
            this.result = data || [];
            this._spinner.hide();
            if (this.result != null) {
                if (this.result.irbExemptFormList == null || this.result.irbExemptFormList.length === 0) {
                    this.noIrbList = true;
                } else {
                    this.exemptListData = this.result.irbExemptFormList;
                }
            }
        },
            error => {
                console.log('Error in method getExemptListData()', error);
            },
        );
    }

    getCommitteeScheduleListData(tab) {
        this.noIrbList = false;
        this.irbListData = [];
        this.paginatedIrbListData = [];
        this.protocolCount = null;
        this.lastClickedTab = tab;
        this._sharedDataService.changeCurrentTab(tab);
        if (tab === 'COMMITTEE') {
            this._spinner.show();
            this._dashboardService.loadCommitteeList().subscribe(data => {
                this.result = data || [];
                this._spinner.hide();
                if (this.result != null) {
                    if (this.result.committeList == null || this.result.committeList.length === 0) {
                        this.noIrbList = true;
                    } else {
                       this.irbListData = this.result.committeList;
                        // this.column = 'updatedDate';
                        // this.direction = parseInt(this.sortOrder, 10);
                        this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
                        this.protocolCount = this.irbListData.length;
                    }
                }
            },
                error => {
                    console.log('Error in method getExemptListData()', error);
                },
            );
        } else if (tab === 'SCHEDULE') {
            this._spinner.show();
            this._dashboardService.loadCommitteeScheduleList().subscribe(data => {
                this.result = data || [];
                this._spinner.hide();
                if (this.result != null) {
                    if (this.result.committeSchedulList == null || this.result.committeSchedulList.length === 0) {
                        this.noIrbList = true;
                    } else {
                        const currentDate = new Date();
                        currentDate.setHours(0, 0, 0, 0);
                        this.irbListData = this.result.committeSchedulList;
                        this.paginatedIrbListData = this.irbListData.slice(0, this.paginationData.limit);
                        // this.column = 'updatedDate';
                        // this.direction = parseInt(this.sortOrder, 10);
                        this.protocolCount = this.irbListData.length;
                        this.irbListData.forEach(element => {
                            element.SCHEDULED_DATE = new Date(element.SCHEDULED_DATE);
                            element.SCHEDULED_DATE.setHours(0, 0, 0, 0);
                            if (currentDate <= element.SCHEDULED_DATE) {
                                element.isActive = true;
                            } else {
                                element.isActive = false;
                            }
                        });

                    }
                }
            },
                error => {
                    console.log('Error in method getExemptListData()', error);
                },
            );
        }
    }

    /** clear all fields in advance search of exempt studies*/
    clearExemptSearch() {
        this.requestObject.determination = '';
        this.requestObject.title = '';
        this.requestObject.piName = '';
        this.requestObject.exemptFormfacultySponsorName = null;
        this.requestObject.exemptFormStartDate = null;
        this.requestObject.exemptFormEndDate = null;
        this.exemptParams.determination = '';
        this.exemptParams.title = '';
        this.exemptParams.piName = '';
        this.exemptParams.exemptFormfacultySponsorName = null;
        this.exemptParams.exemptFormStartDate = null;
        this.exemptParams.exemptFormEndDate = null;
        this.getExemptListData('EXEMPT');
        this._sharedDataService.searchData = null;
    }


    /**
     * @param  {} currentDate
     * Formats date in mm-dd-yyy format
     */
    GetFormattedDate(currentDate) {
        const month = currentDate.getMonth() + 1;
        const day = currentDate.getDate();
        const year = currentDate.getFullYear();
        return month + '-' + day + '-' + year;
    }

    /**
     * @param  {} pageNumber
     * paginate an array
     */
    protocolListPerPage(pageNumber) {
        this.paginatedIrbListData = this.irbListData.slice(pageNumber * this.paginationData.limit - this.paginationData.limit,
            pageNumber * this.paginationData.limit);
        document.getElementById('scrollToTop').scrollTop = 0;
    }
    openStatusModal() {
        const statuses = this.protocolStatus.split(',');
        statuses.forEach(status => {
            this.isCheckBoxChecked[status] = true;
        });
        document.getElementById('openStatusModalButton').click();
    }
    statusSearchChange() {
        if (!this.statusSearchText) {
            this.protocolStatusesCopy = Object.assign([], this.protocolStatuses);
        }
        this.statusSearchText = this.statusSearchText.toLowerCase();
        this.protocolStatusesCopy = this.protocolStatuses.filter( status => {
            return status.PROTOCOL_STATUS.toLowerCase().includes(this.statusSearchText);
          });
    }
    setProtocolStatus() {
        this.protocolStatus = '';
        this.requestObject.protocolStatusCode = '';
        this.protocolStatusesCopy = Object.assign([], this.protocolStatuses);
        this.protocolStatuses.forEach(protocolStatus => {
            if (this.isCheckBoxChecked[protocolStatus.PROTOCOL_STATUS] === true) {
                this.protocolStatus = this.protocolStatus === '' ?
                protocolStatus.PROTOCOL_STATUS : this.protocolStatus + ',' + protocolStatus.PROTOCOL_STATUS;
                this._sharedDataService.searchedStatus = this.protocolStatus;
                this.requestObject.protocolStatusCode = this.requestObject.protocolStatusCode === '' ?
                protocolStatus.PROTOCOL_STATUS_CODE : this.requestObject.protocolStatusCode + ',' + protocolStatus.PROTOCOL_STATUS_CODE;
            }
        });
        this.getAdvanceSearch(this.lastClickedTab);
        this.statusSearchText = '';
    }
    clearStatusSearch(event) {
        event.preventDefault();
        this.statusSearchText = '';
        this.protocolStatusesCopy = Object.assign([], this.protocolStatuses);
    }
    clearStatus() {
        this.isCheckBoxChecked = {};
        this.protocolStatus = '';
        this._sharedDataService.searchedStatus = '';
        this.requestObject.protocolStatusCode = '';
        this._sharedDataService.searchData = this.requestObject;

    }
    selectedPiResult(result) {

         this.requestObject.piName = result.obj.full_name;
         this.IsElasticResultPI = false;
     }
     selectedFsResult(result) {
         this.requestObject.exemptFormfacultySponsorName = result.obj.full_name;
         this.IsElasticResultFS = false;
     }
}

