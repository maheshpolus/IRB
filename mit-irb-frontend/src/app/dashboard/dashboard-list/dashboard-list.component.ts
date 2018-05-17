import { Component, OnInit, Input, NgZone, AfterViewInit } from '@angular/core';
import { SharedDataService } from '../../common/service/shared-data.service';
import { ElasticService } from '../../common/service/elastic.service';
import { DashboardService } from '../dashboard.service';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

@Component( {
    selector: 'app-dashboard-list',
    templateUrl: './dashboard-list.component.html',
    styleUrls: ['./dashboard-list.component.css']
} )
export class DashboardListComponent implements OnInit, AfterViewInit {

    @Input() userDTO: any ;
    lastClickedTab = 'All My Protocols';
    requestObject = {     
            personId : '900002368',
            person_role_type : 'pi',
            protocol_number : '',
            title : '',
            lead_unit_number : '',
            protocol_type_code : '',
            dashboard_type : ''
    };
    result: any;
    irbListData: any = [];

   /* irbListDummy: any = [
        {
        STATUS: 'In Drafts',
        PROTOCOL_NUMBER: '231312',
        TITLE: 'new',
        PROTOCOL_TYPE: 'processed',
        SUBMISSION_STATUS: 'drafted',
        APPROVAL_DATE: '7453535',
        EXPIRATION_DATE: '35424',
        UPDATE_TIMESTAMP: '3535345'
        },
        {
            STATUS: 'In Progress',
            PROTOCOL_NUMBER: '131312',
            TITLE: 'old',
            PROTOCOL_TYPE: 'negoatiable',
            SUBMISSION_STATUS: 'progress',
            APPROVAL_DATE: '53535',
            EXPIRATION_DATE: '62424',
            UPDATE_TIMESTAMP: '6535345'
        },
        {
            STATUS: 'Active Open Enrollment',
            PROTOCOL_NUMBER: '451312',
            TITLE: 'processing',
            PROTOCOL_TYPE: 'fixed',
            SUBMISSION_STATUS: 'active',
            APPROVAL_DATE: '32343535',
            EXPIRATION_DATE: '33324',
            UPDATE_TIMESTAMP: '45635345'
        },
        {
            STATUS: 'Pending Action',
            PROTOCOL_NUMBER: '731312',
            TITLE: 'denied',
            PROTOCOL_TYPE: 'standard',
            SUBMISSION_STATUS: 'pending',
            APPROVAL_DATE: '53535',
            EXPIRATION_DATE: '324',
            UPDATE_TIMESTAMP: '735345'
        }
    ];*/

    /**elastic serach variables */
    message = '';
    _results: Subject<Array<any>> = new Subject<Array<any>>();
    searchText: FormControl = new FormControl( '' );
    seachTextModel: string;
    IsElasticResult = false;
    protocol: string;
    protocolType: string;
    title: string;
    leadUnit: string;
    status: string;
    piName: string;
    elasticResultTab = false;

    isAdvancesearch = false;
    roleType = 'PI';
    direction: number;
    column: any;
    sortOrder: string;
    sortField = 'UPDATE_TIMESTAMP';
    arrayOfKeys: any = [];

    constructor( private sharedData: SharedDataService,
        private _dashboardService: DashboardService,
        private _ngZone: NgZone,
        private _elasticsearchService: ElasticService ) { }

    ngOnInit() {
        this.getIrbListData('ALL');
    }

    ngAfterViewInit() {
        this.searchText
            .valueChanges
            .map(( text: any ) => text ? text.trim() : '' )
            .do( searchString => searchString ? this.message = 'searching...' : this.message = '' )
            .debounceTime( 500 )
            .distinctUntilChanged()
            .switchMap( searchString => {
                return new Promise<Array<String>>(( resolve, reject ) => {
                    this._ngZone.runOutsideAngular(() => {
                        let hits_source: Array<any> = [];
                        let hits_highlight: Array<any> = [];
                        const hits_out: Array<any> = [];
                        const results: Array<any> = [];
                        let protocol_number: string;
                        let title: string;
                        let protocol_type: string;
                        let personName: string;
                        let lead_unit: string;
                        let unit_number: string;
                        let test;
                        this._elasticsearchService.irbSearch(searchString)
                            .then(( searchResult ) => {
                              this._ngZone.run(() => {
                                hits_source = ( ( searchResult.hits || {} ).hits || [] )
                                    .map(( hit ) => hit._source );
                                hits_highlight = ( ( searchResult.hits || {} ).hits || [] )
                                    .map(( hit ) => hit.highlight );

                                hits_source.forEach(( elmnt, j ) => {
                                    protocol_number = hits_source[j].protocol_number;
                                    title = hits_source[j].title;
                                    lead_unit = hits_source[j].lead_unit_name;
                                    unit_number = hits_source[j].lead_unit_number;
                                    protocol_type = hits_source[j].protocol_type;
                                    personName = hits_source[j].pi_name;
                                    status = hits_source[j].status;
                                    test = hits_source[j];

                                    if ( typeof ( hits_highlight[j].protocol_number ) !== 'undefined' ) {
                                        protocol_number = hits_highlight[j].protocol_number;
                                    }
                                    if ( typeof ( hits_highlight[j].title ) !== 'undefined' ) {
                                        title = hits_highlight[j].title;
                                    }
                                    if ( typeof ( hits_highlight[j].lead_unit_name ) !== 'undefined' ) {
                                        lead_unit = hits_highlight[j].lead_unit_name;
                                    }
                                    if ( typeof ( hits_highlight[j].lead_unit_number ) !== 'undefined' ) {
                                        unit_number = hits_highlight[j].lead_unit_number;
                                    }
                                    if ( typeof ( hits_highlight[j].protocol_type ) !== 'undefined' ) {
                                        protocol_type = hits_highlight[j].protocol_type;
                                    }
                                    if ( typeof ( hits_highlight[j].pi_name ) !== 'undefined' ) {
                                        personName = hits_highlight[j].pi_name;
                                    }
                                    if ( typeof ( hits_highlight[j].status ) !== 'undefined' ) {
                                        status = hits_highlight[j].status;
                                    }
                                    results.push( {
                                        label: protocol_number + '  :  ' + title
                                        + '  |  ' + unit_number
                                        + '  |  ' + lead_unit
                                        + '  |  ' + protocol_type
                                        + '  |  ' + personName
                                        + '  |  ' + status,
                                        obj: test
                                    } );
                                } );
                                if ( results.length > 0 ) {
                                    this.message = '';
                                } else {
                                    if ( this.seachTextModel && this.seachTextModel.trim() ) {
                                        this.message = 'nothing was found';
                                    }
                                }
                                resolve( results );
                            } );

                            } )
                            .catch(( error ) => {
                                this._ngZone.run(() => {
                                    reject( error );
                                } );
                            } );
                    } );
                } );
            } )
            .catch( this.handleError )
            .subscribe( this._results );
      }
      handleError(): any {
        this.message = 'something went wrong';
      }

    getIrbListData( currentTab ) {
        this.seachTextModel = '';
        this.lastClickedTab = currentTab;
   /*     this.requestObject.personId = this.userDTO.personID;
        this.requestObject.person_role_type = this.userDTO.role;*/
        this.requestObject.dashboard_type = currentTab;
        this._dashboardService.getIrbList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                this.irbListData = this.result.dashBoardDetailMap || [];
                if( this.irbListData != null) {
                    this.arrayOfKeys = Object.keys( this.irbListData[0]) || [];   
                }
            }
        },
            error => {
                 console.log( error );
            },
        );
    }

    sortBy( ) {
        this.column = this.sortField;
        this.direction =  parseInt(this.sortOrder, 10);
    }

    showAdvanceSearch() {
        this.isAdvancesearch = !this.isAdvancesearch;
    }

    selectedResult(result) {
        console.log(result);
        this.protocol = result.obj.protocol_number;
        this.protocolType = result.obj.protocol_type;
        this.piName = result.obj.pi_name;
        this.leadUnit = result.obj.lead_unit_name;
        this.title = result.obj.title;
        this.status = result.obj.status;
        this.elasticResultTab = true;
        this.seachTextModel = '';
      }
      closeResultTab() {
          this.seachTextModel = '';
        this.elasticResultTab = false;
      }
}
