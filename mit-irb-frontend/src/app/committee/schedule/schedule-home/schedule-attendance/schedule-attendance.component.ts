import { Component, OnInit, OnDestroy, NgZone, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject} from 'rxjs';
import { ActivatedRoute } from '@angular/router';

import { ScheduleConfigurationService } from '../../schedule-configuration.service';
import { ScheduleAttendanceService } from './schedule-attendance.service';

import 'rxjs/add/operator/takeUntil';
import { CommitteeMemberNonEmployeeElasticService } from '../../../../common/service/committee-members-nonEmployee-elastic-search.service';
import { CommitteeMemberEmployeeElasticService } from '../../../../common/service/committee-members-employees-elastic-search.service';
import { PiElasticService } from '../../../../common/service/pi-elastic.service';

@Component( {
    selector: 'app-schedule-attendance',
    templateUrl: './schedule-attendance.component.html'
} )
export class ScheduleAttendanceComponent implements OnInit, OnDestroy {
    result: any = {};
    showCommentFlag = false;
    presentFlag = false;
    isAddGuest = false;
    attendanceIndex: number;
    commentsIndex: number;
    searchText: FormControl = new FormControl( '' );
    nonEmployeeFlag = false;
    message: string;
    showAddMember = false;
    public onDestroy$ = new Subject<void>();
    activeMembers = true;
    inactiveMembers: string;
    searchActive = false;
    iconClass = 'fa fa-search';
    selectedMember: any = {};

    updatingMemberObj: any = {};
    // scheduleId: number;
    // committeeId: string;
    attendanceShowFlag = false;
    editFlagEnabled = {};
    editIndex: number;
    tempAlternateFor: string;
    tempMemberPresent: boolean;
    tempComment: string;
    currentmember: string;
    showPopup: boolean;
    deletingMeberObj;
    commentFlgEnabled = {};
    placeHolderText = 'Search an employee';

    clearField: any = 'true';
    personType = 'employee';
    elasticPlaceHolder: string;
    committeeId = null;
    scheduleId = null;

    options: any = {};
    userDTO: any = {};
    guestMemberObj: any = {};
    selectedPerson: any = {};
    isPresent = null;

    showPersonElasticBand = false;
    showAllMembers = false;
    committeeMemberActiveList: any = [];
    alternateMember: any = [];
    guestMembers: any = [];

    constructor( private scheduleConfigurationService: ScheduleConfigurationService,
        public committeeMemberNonEmployeeElasticService: CommitteeMemberNonEmployeeElasticService,
        private _ngZone: NgZone, private _elasticsearchService: PiElasticService,
        public committeeMemberEmployeeElasticService: CommitteeMemberEmployeeElasticService,
        private scheduleAttendanceService: ScheduleAttendanceService,
        private activatedRoute: ActivatedRoute ) {

            this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
            this.options.index = this._elasticsearchService.IRB_INDEX;
            this.options.type = 'person';
            this.options.size = 20;
            this.options.contextField = 'full_name';
            this.options.debounceTime = 500;
            this.options.theme = '#a31f34';
            this.options.width = '100%';
            this.options.fontSize = '16px';
            this.options.defaultValue = '';
            this.options.formatString = 'full_name | email_address | home_unit_name';
            this.options.fields = {
              full_name: {},
              first_name: {},
              user_name: {},
              email_address: {},
              home_unit_name: {},
              person_id: {}
            };
            this.elasticPlaceHolder = 'Search for an Employee Name';
       // this.scheduleId = this.activatedRoute.snapshot.queryParams['scheduleId'];
       this.userDTO = JSON.parse(localStorage.getItem('currentUser'));
    }

    ngOnInit() {
        this.activatedRoute.queryParams.subscribe(params => {
            this.committeeId = params['committeeId'];
            this.scheduleId = params['scheduleId'];
          });
        this.scheduleConfigurationService.currentScheduleData.subscribe( data => {
            this.result = data;
        } );
    }
    ngOnDestroy() {
        this.onDestroy$.next();
        this.onDestroy$.complete();
    }


    addMember() {
        this.guestMemberObj.alternateFlag = false;
        this.guestMemberObj.alternateFor = '';
        this.guestMemberObj.comments = null;
        this.guestMemberObj.guestFlag = true;
        this.guestMemberObj.guestMemberActive = true;
        this.guestMemberObj.memberPresent = false;
        this.guestMemberObj.nonEmployeeFlag = this.nonEmployeeFlag;
        this.guestMemberObj.personId = this.selectedMember.id;
        this.guestMemberObj.personName = this.selectedMember.label;
        this.guestMemberObj.roleName = '';
        this.guestMemberObj.updateTimestamp = new Date().getTime();
        this.guestMemberObj.updateUser = this.userDTO.userName;
        this.scheduleAttendanceService.addGuestMember( this.guestMemberObj, this.scheduleId )
        .takeUntil(this.onDestroy$).subscribe( data => {
            this.result = data;
        } );
    }

    showComment( event: any, commentIndex: number ) {
        event.preventDefault();
        this.attendanceShowFlag = !this.attendanceShowFlag;
        this.showCommentFlag = !this.showCommentFlag;
        this.commentsIndex = commentIndex;
        if ((!this.commentFlgEnabled[commentIndex]) === true) {
            this.commentFlgEnabled[commentIndex] = true;
        }
    }

    editAttendanceData( event: any, index: number, memberObj ) {
        event.preventDefault();
        if ((!this.editFlagEnabled[index]) === true) {
            this.editFlagEnabled[index] = true;
        }
        this.tempAlternateFor = memberObj.alternateFor;
        this.tempComment = memberObj.comments;
        this.tempMemberPresent = memberObj.memberPresent;
    }

    saveAttendanceEditedData( event: any, index: number, memberObj ) {
        event.preventDefault();
        this.showCommentFlag = false;
        this.attendanceShowFlag = false;
        if ((!this.editFlagEnabled[index]) === false) {
            this.editFlagEnabled[index] = false;
        }
        if ((!this.commentFlgEnabled[index]) === false) {
            this.commentFlgEnabled[index] = false;
        }
        // this.committeeId = this.result.committeeSchedule.committeeId;
        this.updatingMemberObj.alternateFor = memberObj.alternateFor;
        this.updatingMemberObj.committeeScheduleAttendanceId = memberObj.committeeScheduleAttendanceId;
        this.updatingMemberObj.memberPresent = memberObj.memberPresent;
        this.updatingMemberObj.comments = memberObj.comments;
        this.updatingMemberObj.updateUser = this.userDTO.userName;
        this.updatingMemberObj.updateTimestamp = new Date().getTime();
        this.scheduleAttendanceService.updateMemberattendanceDate( this.committeeId, this.scheduleId, this.updatingMemberObj )
            .takeUntil(this.onDestroy$).subscribe( data => {
                this.result = data;
            } );
    }

    cancelEditAttenfance( event: any, index: number, memberObj ) {
        event.preventDefault();
        this.editFlagEnabled[index] = !this.editFlagEnabled[index];
        memberObj.alternateFor = this.tempAlternateFor;
        memberObj.comments = this.tempComment;
        memberObj.memberPresent = this.tempMemberPresent;
    }

    hideComment( event: any, commentIndex: number ) {
        this.showCommentFlag = false;
        this.commentsIndex = commentIndex;
    }

    markAttendance( event: any, memberObj, index ) {
        event.preventDefault();
        if ( memberObj.memberPresent === true ) {
            memberObj.memberPresent = false;
        } else {
            memberObj.memberPresent = true;
        }
        this.tempAlternateFor = memberObj.alternateFor;
        this.tempComment = memberObj.comments;
        this.tempMemberPresent = memberObj.memberPresent;

       // this.committeeId = this.result.committeeSchedule.committeeId;
        this.updatingMemberObj.alternateFor = memberObj.alternateFor;
        this.updatingMemberObj.committeeScheduleAttendanceId = memberObj.committeeScheduleAttendanceId;
        this.updatingMemberObj.memberPresent = memberObj.memberPresent;
        this.updatingMemberObj.comments = memberObj.comments;
        this.updatingMemberObj.updateUser = this.userDTO.userName;
        this.updatingMemberObj.updateTimestamp = new Date().getTime();
        this.scheduleAttendanceService.updateMemberattendanceDate( this.committeeId, this.scheduleId, this.updatingMemberObj )
            .takeUntil(this.onDestroy$).subscribe( data => {
                this.result = data;
            } );
    }

    showDeleteModal( event: any, memberObj ) {
        event.preventDefault();
        this.showPopup = true;
        this.deletingMeberObj = memberObj;
    }

    deleteAttendance( event: any ) {
        this.showPopup = false;
       // this.committeeId = this.result.committeeSchedule.committeeId;
        this.scheduleAttendanceService.deleteScheduleMemberAttendance(
            this.committeeId, this.scheduleId, this.deletingMeberObj.committeeScheduleAttendanceId )
            .takeUntil(this.onDestroy$).subscribe( data => {
                this.result = data;
            } );
    }

    showMemberDetails() {
        this.showAllMembers = true;
        const requestObject = {committeeId: this.committeeId, scheduleId: this.scheduleId};
        this.scheduleAttendanceService.getMeetingAttendenceList(requestObject).subscribe((data: any) => {
            this.committeeMemberActiveList = data.committeeMember;
            this.alternateMember = data.alternateMember;
            this.guestMembers = data.guestMembers;
        });
    }

    changePersonType(type) {
        // tslint:disable-next-line:no-construct
        this.clearField = new String('true');
        this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
        this.options.index = this._elasticsearchService.IRB_INDEX;
        this.options.defaultValue = '';
        if (type === 'employee') {
          this.options.index = this._elasticsearchService.IRB_INDEX;
          this.options.type = 'person';
          this.elasticPlaceHolder = 'Search for an Employee Name';
          this.options.formatString = 'full_name | email_address | home_unit_name';
          this.options.fields = {
            full_name: {},
            first_name: {},
            user_name: {},
            email_address: {},
            home_unit_name: {},
            person_id: {}
          };
        } else {
          this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
          this.options.type = 'rolodex';
          this.elasticPlaceHolder = 'Search for an Non-Employee Name';
          this.options.formatString = 'full_name | email_address | title ';
          this.options.fields = {
            full_name: {},
            first_name: {},
            user_name: {},
            email_address: {},
            home_unit: {},
            title: {},
            rolodex_id: {}
          };
        }
      }

      selectPersonElasticResult(personDetails) {
          this.showPersonElasticBand = true;
        if (this.options.type === 'person') {
          this.guestMemberObj.nonEmployeeFlag = false;
          this.selectedPerson = {
            fullName: personDetails.full_name, emailAddress: personDetails.email_address,
            directoryTitle: personDetails.job_title, phoneNumber: personDetails.phone_nbr,
            addressLine1: personDetails.addr_line_1, unitName: personDetails.home_unit_name
          };
        } else {
          this.guestMemberObj.nonEmployeeFlag = true;
          this.selectedPerson = {
            fullName: personDetails.full_name, emailAddress: personDetails.email_address,
            directoryTitle: personDetails.title, phoneNumber: personDetails.phone_number, addressLine1: personDetails.addr_line_1,
            organization: personDetails.organization
          };
        }
      }

      changeAttendence() {
          this.isPresent = false;
      }
}
