import { Component, OnInit, Input, NgZone, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import { Router } from '@angular/router';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { ElasticService } from '../../common/service/elastic.service';
import { DashboardService } from '../dashboard.service';
import { SharedDataService } from '../../common/service/shared-data.service';


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
        dashboardType: '',
        determination: '',
        exemptFormStartDate: '',
        exemptFormEndDate: '',
        exemptFormfacultySponsorName: ''
    };
    protocolTypeList = [];
    result: any;
    irbListData: any = [];
    exemptListData: any = [];
    roleType: string;
    direction: number;
    column: any;
    sortOrder = '1';
    sortField = 'UPDATE_TIMESTAMP';

    /**elastic search variables */
    message = '';
    _results: Subject<Array<any>> = new Subject<Array<any>>();
    searchText: FormControl = new FormControl('');
    searchTextModel: string;
    iconClass: any;
    IsElasticResult = false;
    protocol: string;
    protocolType: string;
    title: string;
    leadUnit: string;
    status: string;
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
        private _router: Router,
        private _sharedDataService: SharedDataService) { }

    /** load protocol list based on tab and load protocoltypes to show in advance search field */
    ngOnInit() {
        this._sharedDataService.currentTab.subscribe(data => {
            this.lastClickedTab = data;
        });
        if (this.lastClickedTab === 'EXEMPT') {
            this.getExemptListData(this.lastClickedTab);
        } else {
            this.getIrbListData(this.lastClickedTab);
        }

        this.roleType = this.userDTO.role;
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
        this.irbListData = [];
        this.noIrbList = false;
        this.searchTextModel = '';
        this.lastClickedTab = currentTab;
        if (!this.isAdvancesearch) {
            this.requestObject = { protocolTypeCode: '' };
        }
        this._sharedDataService.changeCurrentTab(currentTab);
        this.requestObject.personId = this.userDTO.personID;
        this.requestObject.personRoleType = this.userDTO.role;
        this.requestObject.dashboardType = currentTab;
        this._dashboardService.getIrbList(this.requestObject).subscribe(data => {
            this.result = data || [];
            if (this.result != null) {
                if (this.result.dashBoardDetailMap == null || this.result.dashBoardDetailMap.length === 0) {
                    this.noIrbList = true;
                } else {
                    this.irbListData = this.result.dashBoardDetailMap;
                    this.sortBy();
                }
            }
        },
            error => {
                console.log('Error in method getIrbListData()', error);
            },
        );
    }

    /** sets value of direction to implement sorting */
    updateSortOrder() {
        this.sortOrder = this.sortOrder === '1' ? '-1' : '1';
        this.sortBy();
    }

    /** sets value of column and direction to implement sorting */
    sortBy() {
        this.column = this.sortField;
        this.direction = parseInt(this.sortOrder, 10);
    }

    /*show or hide advance search fields*/
    showAdvanceSearch() {
        this.isAdvancesearch = !this.isAdvancesearch;
        this.isAdvancsearchPerformed = false;
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
            this.requestObject.piName !== '' || this.requestObject.protocolTypeCode !== '') {
            this.requestObject.protocolNumber = '';
            this.requestObject.title = '';
            this.requestObject.piName = '';
            this.requestObject.protocolTypeCode = '';
            this.getIrbListData(this.lastClickedTab);
            this.isAdvancsearchPerformed = false;
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
            this.requestObject = { protocolTypeCode: '' };
            this.exemptParams = {};
        }
        this._sharedDataService.changeCurrentTab(currentTab);
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
        this._dashboardService.getExemptProtocols(this.exemptParams).subscribe(data => {
            this.result = data || [];
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
        this.lastClickedTab = tab;
        if (tab === 'COMMITTEE') {
            this._dashboardService.loadCommitteeList().subscribe(data => {
                this.result = data || [];
                if (this.result != null) {
                    if (this.result.committeList == null || this.result.committeList.length === 0) {
                        this.noIrbList = true;
                    } else {
                        this.irbListData = this.result.committeList;
                        this.column = 'committeeId';
                        this.direction = parseInt(this.sortOrder, 10);
                    }
                }
            },
                error => {
                    console.log('Error in method getExemptListData()', error);
                },
            );
        } else if (tab === 'SCHEDULE') {
            this._dashboardService.loadCommitteeScheduleList().subscribe(data => {
                this.result = data || [];
                if (this.result != null) {
                    if (this.result.committeSchedulList == null || this.result.committeSchedulList.length === 0) {
                        this.noIrbList = true;
                    } else {
                        const currentDate = new Date();
                        currentDate.setHours(0, 0, 0, 0);
                        this.irbListData = this.result.committeSchedulList;
                        this.irbListData.forEach(element => {
                            element.scheduledDate = new Date(element.scheduledDate);
                            element.scheduledDate.setHours(0, 0, 0, 0);
                            if (currentDate <= element.scheduledDate) {
                                element.isActive = true;
                            } else {
                                element.isActive = false;
                            }
                        });
                        this.column = 'committeeId';
                        this.direction = parseInt(this.sortOrder, 10);
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
    }
    GetFormattedDate(currentDate) {
        const month = currentDate.getMonth() + 1;
        const day = currentDate.getDate();
        const year = currentDate.getFullYear();
        return month + '-' + day + '-' + year;
    }
}
