import { Component, OnInit, OnDestroy, ViewChild, ContentChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { CommitteeHomeComponent } from './committee-home/committee-home.component';

import { CommitteCreateEditService } from './committee-create-edit.service';
import { CommitteeSaveService } from './committee-save.service';
import { CompleterService, CompleterData } from 'ng2-completer';
import { CommitteeMembersComponent } from './committee-members/committee-members.component';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/takeUntil';
import { CommitteeConfigurationService } from '../common/service/committee-configuration.service';
@Component( {
    selector: 'app-committee',
    templateUrl: './committee.component.html',
    providers: [CommitteCreateEditService, CommitteeSaveService, CommitteeHomeComponent, CommitteeMembersComponent]
} )

export class CommitteeComponent implements OnInit, OnDestroy {

    userDTO: any = {};
    currentTab = 'committee_home';
    schedule = false;
    mode: string;
    class: string;
    id: string;
    name: string;
    type: string;
    homeUnit: string;
    lastUpdated: string;
    homeUnits: any = [];
    reviewTypes: any = [];
    scheduleStatus: any = [];
    result: any = {};
    homeUnitInput: any = [];
    editDetails = false;
    homeUnitName: string;
    editFlag = false;
    constantClass: string;
    areaList: any = [];
    showPopup = false;
    middleOfEdit = false;
    middleOfSave = false;
    alertMsgNotSaved = '';
    alertMsgMiddleOfEdit = '';
    public dataServiceHomeUnit: CompleterData;
    isOnEditMembers = false;
    isCreateNewCommittee = false;
    memberData: any = [];
    alertMsgMemberMiddleOfEdit: string;
    public onDestroy$ = new Subject<void>();
    activatedRoute;
    @ContentChild( CommitteeMembersComponent )
    @ContentChild( CommitteeHomeComponent )
    private committeeHomeObj: CommitteeHomeComponent;

    constructor( public route: ActivatedRoute,
        private _spinner: NgxSpinnerService,
        public router: Router,
        public committeCreateService: CommitteCreateEditService,
        private completerService: CompleterService,
        public committeeSaveService: CommitteeSaveService,
        public committeeConfigurationService: CommitteeConfigurationService ) {
        this.result.committee = {};
        this.result.committee.committeeType = {};
        this.mode = this.route.snapshot.queryParamMap.get( 'mode' );
        this.id = this.route.snapshot.queryParamMap.get( 'id' );
        const test =  this.route.snapshot.firstChild.url[0].path;
        this.currentTab = (test === 'committeeHome' ? 'committee_home' : 'committee_members');
    }
    ngOnDestroy() {
        this.onDestroy$.next();
        this.onDestroy$.complete();
    }


    ngOnInit() {
        this.userDTO = this.route.snapshot.data['irb'];
        localStorage.setItem('currentUser', this.userDTO.userName);
        this.initLoadParent();
        this.committeeConfigurationService.currentCommitteeData.takeUntil( this.onDestroy$ ).subscribe( data => {
            this.result = data;
        });
        this.route.queryParams.subscribe(params => {
            this.id = params['id'];
        if (this.id == null || this.id === undefined) {
            this.isCreateNewCommittee = true;
          } else {
            this.isCreateNewCommittee = false;
          }
        });

    }

    onActivate( componentRef ) {
        this.activatedRoute = componentRef;
    }

    initLoadParent() {
        this._spinner.show();
        if ( this.mode === 'create' ) {
            this.editFlag = true;
            this.committeCreateService.getCommitteeData( '1' )
                .takeUntil( this.onDestroy$ ).subscribe( data => {
                    this._spinner.hide();
                    this.result = data || [];
                    if ( this.result != null ) {
                        this.homeUnits = this.result.homeUnits;
                        this.committeeConfigurationService.changeCommmitteeData( this.result );
                        this.homeUnitInput.unitName = this.result.committee.homeUnitName;
                        this.reviewTypes = this.result.reviewTypes;
                        this.scheduleStatus = this.result.scheduleStatus;
                        this.areaList = this.completerService.local( this.result.researchAreas, 'description', 'description' );
                        this.committeeConfigurationService.changeAreaOfResearch( this.areaList );
                        this.dataServiceHomeUnit = this.completerService.local( this.homeUnits, 'unitName', 'unitName' );
                    }
                } );
            this.committeeConfigurationService.changeMode( this.mode );
            this.editDetails = true;
            this.class = 'scheduleBoxes';
            this.constantClass = 'scheduleBoxes';
            this.homeUnitInput.unitName = '';
        } else if ( this.mode === 'view' ) {
            this.committeCreateService.loadCommittee( this.id )
                .takeUntil( this.onDestroy$ ).subscribe( data => {
                    this.result = data || [];
                    this._spinner.hide();
                    this.loadScheduleList();
                    if ( this.result != null ) {
                        this.committeeConfigurationService.changeCommmitteeData( this.result );
                        const ts = new Date( this.result.committee.updateTimestamp );
                        let month = String( ts.getMonth() + 1 );
                        let day = String( ts.getDate() );
                        const year = String( ts.getFullYear() );
                        if ( month.length < 2 ) {month = '0' + month; }
                        if ( day.length < 2 ) {day = '0' + day; }
                     //   this.lastUpdated = `${day}/${month}/${year}` + ' by ' + this.result.committee.updateUser;
                        this.lastUpdated = this.result.committee.updateUser;
                        this.homeUnits = this.result.homeUnits;
                        this.committeeConfigurationService.changeCommmitteeData( this.result );
                        this.homeUnitInput.unitName = this.result.committee.homeUnitName;
                        this.reviewTypes = this.result.reviewTypes;
                        this.scheduleStatus = this.result.scheduleStatus;
                        this.areaList = this.completerService.local( this.result.researchAreas, 'description', 'description' );
                        this.committeeConfigurationService.changeAreaOfResearch( this.areaList );
                        this.dataServiceHomeUnit = this.completerService.local( this.homeUnits, 'unitName', 'unitName' );
                    }
                } );
            this.committeeConfigurationService.changeMode( this.mode );
            this.editDetails = false;
            this.class = 'committeeBoxNotEditable';
            this.constantClass = 'committeeBoxNotEditable';
            this.committeeConfigurationService.currentEditFlag.takeUntil( this.onDestroy$ ).subscribe( data => {
                this.editFlag = data;
                if ( this.editFlag ) {
                    this.class = 'scheduleBoxes';
                } else {
                    this.class = 'committeeBoxNotEditable';
                }
            } );
        }

    }

    loadScheduleList() {
        this.committeCreateService.loadScheduleDetailsById( this.id )
                .takeUntil( this.onDestroy$ ).subscribe( data => {
                    const schedules: any = data;
                    if (this.result.committee != null) {
                        this.result.committee.committeeSchedules = schedules.committee.committeeSchedules || [];
                    }
                });
    }

    show_current_tab( e: any, current_tab ) {
        e.preventDefault();
        this.committeeConfigurationService.changeActivatedtab( current_tab );
        this.clear();
        if ( current_tab === 'committee_members' ) {
            // this.committeeConfigurationService.currentEditFlag.subscribe( data => {
            //     this.editFlag = data;
            // } );
            // if ( this.editFlag ) {
            //     this.showPopup = true;
            //     if ( this.mode === 'view' ) {
            //         this.middleOfEdit = true;
            //         this.alertMsgMiddleOfEdit = 'You are in the middle of editing Committee Details, Do you want to stay on the page..?';
            //     } else if ( this.mode === 'create' ) {
            //         this.middleOfSave = true;
            //         this.alertMsgNotSaved = 'You have to save the committee to proceed!';
            //     }
            // } else {
                this.currentTab = current_tab;
               // this.router.navigate( ['irb/committee/committeeMembers'],
               // { queryParams: { 'mode': this.mode, 'id': this.id } } );
                this.router.navigate( ['irb/committee/committeeMembers'],
                { queryParams: { 'mode': this.route.snapshot.queryParamMap.get( 'mode' ), 'id': this.id } } );
            // }

        } else if ( current_tab === 'committee_home' ) {
            // this.committeeConfigurationService.currentMemberEditFlag.takeUntil( this.onDestroy$ ).subscribe( data => {
            //     this.isOnEditMembers = data;

            // } );
            // if ( this.isOnEditMembers ) {
            //     this.showPopup = true;
            //     this.committeeConfigurationService.currentMemberData.takeUntil( this.onDestroy$ ).subscribe( data => {
            //         this.memberData = data;
            //     } );
            //     this.alertMsgMemberMiddleOfEdit = 'You are in the middle of editing a Member Details, Do you want to stay on the page..?';
            // } else {
                this.currentTab = current_tab;
               // this.router.navigate( ['irb/committee/committeeHome'],
              //  { queryParams: { 'mode': this.mode, 'id': this.id } } );
                this.router.navigate( ['irb/committee/committeeHome'],
                { queryParams: { 'mode': this.route.snapshot.queryParamMap.get( 'mode' ), 'id': this.id } } );
            // }
        }
    }

    saveAndContinue( data ) {
        this.editFlag = !this.editFlag;
        this.class = 'committeeBoxNotEditable';
        this.currentTab = 'committee_members';
        this.clear();
        this.activatedRoute.saveDetails( data );
    }

    clear() {
        this.showPopup = false;
        this.middleOfEdit = false;
        this.alertMsgMiddleOfEdit = '';
        this.middleOfSave = false;
        this.alertMsgNotSaved = '';
    }

    homeChangeFunction( unitName ) {
        this.homeUnits.forEach(( value, index ) => {
            if ( value.unitName === unitName ) {
                this.result.committee.homeUnitNumber = value.unitNumber;
            }
        } );
    }

    onHomeSelect() {
        this.homeUnits.forEach(( value, index ) => {
            if ( value.unitName === this.result.committee.homeUnitName ) {
                this.result.committee.homeUnitNumber = value.unitNumber;
            }
        } );
    }

    recieveFlag( $event ) {
        this.editFlag = $event;
        if ( this.editFlag ) {
            this.class = 'scheduleBoxes';
        } else {
            this.class = 'committeeBoxNotEditable';
        }
    }

    recievemode( $event ) {
        this.mode = $event;
        this.committeeConfigurationService.currentCommitteeData.takeUntil( this.onDestroy$ ).subscribe( data => {
            this.result = data;
            this.id = this.result.committee.committeeId;
        } );

        if ( this.mode === 'view' ) {
            this.initLoadParent();
            this.editDetails = false;
            this.class = 'committeeBoxNotEditable';
            this.constantClass = 'committeeBoxNotEditable';
        }
    }

    recieveEditmembersFlag( $event ) {
        this.isOnEditMembers = $event;
    }

    saveMemberAndContinue( Object ) {
        this.isOnEditMembers = false;
        this.committeeConfigurationService.changeEditMemberFlag( this.isOnEditMembers );
        this.showPopup = false;
        this.class = 'committeeBoxNotEditable';
        this.currentTab = 'committee_home';
        this.clear();
        this.activatedRoute.saveDetails( Object );
        this.router.navigate( ['irb/committee/committeeHome'], { queryParams: { 'mode': this.mode, 'id': this.id } } );
    }

    stayOnPage() {

    }

    stayOnCommittee() {
    }
    backClick() {
        this.router.navigate(['/irb/committee-schedule-list'],
        {queryParams: { isCommitteeList: true}});
    }
}
