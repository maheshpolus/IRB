import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ScheduleConfigurationService } from '../../schedule-configuration.service';
import { ScheduleAttendanceService } from './schedule-attendance.service';
import { PiElasticService } from '../../../../common/service/pi-elastic.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'app-schedule-attendance',
    templateUrl: './schedule-attendance.component.html'
})
export class ScheduleAttendanceComponent implements OnInit {

    clearField: any = 'true';
    personType = 'employee';
    elasticPlaceHolder: string;
    message: string;
    committeeId = null;
    scheduleId = null;

    options: any = {};
    userDTO: any = {};
    guestMemberObj: any = {};
    selectedPerson: any = {};
    committeeSchedule: any = {};
    showPersonElasticBand = false;
    markAllPresent = false;
    committeeMemberActiveList: any = [];
    alternateMember: any = [];
    guestMembers: any = [];
    alternateFor: any = [];
    editCommitteeAttendenceIndex = null;
    editAlternateAttendenceIndex = null;
    editGuestAttendenceIndex = null;
    updatedAttendance = [];
    isAddGuest = false;

    constructor(private scheduleConfigurationService: ScheduleConfigurationService, private _elasticsearchService: PiElasticService,
        private scheduleAttendanceService: ScheduleAttendanceService,
        private activatedRoute: ActivatedRoute, private _spinner: NgxSpinnerService) {

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
            this.showMemberDetails();
        });
        this.scheduleConfigurationService.currentScheduleData.subscribe((data: any) => {
            this.committeeSchedule = data.committeeSchedule;
        });
    }

    showMemberDetails() {
        this._spinner.show();
        const requestObject = {scheduleId: this.scheduleId };
        this._spinner.hide();
        this.scheduleAttendanceService.getMeetingAttendenceList(requestObject).subscribe((data: any) => {
            this.committeeMemberActiveList = data.committeeMember != null ? data.committeeMember : [];
            this.alternateMember = data.alternateMember != null ? data.alternateMember : [];
            this.guestMembers = data.guestMembers != null ? data.guestMembers : [];
            this.getAlternateFor();
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
            this.guestMemberObj.personId = personDetails.person_id;
            this.selectedPerson = {
                fullName: personDetails.full_name, emailAddress: personDetails.email_address,
                directoryTitle: personDetails.job_title, phoneNumber: personDetails.phone_nbr,
                addressLine1: personDetails.addr_line_1, unitName: personDetails.home_unit_name
            };
        } else {
            this.guestMemberObj.nonEmployeeFlag = true;
            this.guestMemberObj.personId = personDetails.rolodex_id;
            this.selectedPerson = {
                fullName: personDetails.full_name, emailAddress: personDetails.email_address,
                directoryTitle: personDetails.title, phoneNumber: personDetails.phone_number, addressLine1: personDetails.addr_line_1,
                organization: personDetails.organization
            };
        }
    }
    getAlternateFor() {
        this.alternateFor = this.committeeMemberActiveList.filter(member => member.memberPresent === 'N');
    }

    editAttendence(member, index) {
        this.guestMemberObj = Object.assign({}, member);
        this.guestMemberObj.acType = 'U';
        if (this.guestMemberObj.alternateFlag === true) {
            this.editAlternateAttendenceIndex = index;
            this.guestMemberObj.memberPresent = 'Y';
            this.guestMemberObj.alternateFor = member.alternateFor != null ?
            member.alternateFor : this.alternateFor[0].personName;
        } else if ( this.guestMemberObj.guestFlag === true ) {
            this.editGuestAttendenceIndex = index;
        } else {
            this.editCommitteeAttendenceIndex = index;
        }
        this.guestMemberObj.updateTimestamp = new Date().getTime();
        this.guestMemberObj.updateUser = this.userDTO.userName;

    }

    saveEditAttendence() {
        this.updatedAttendance.push(this.guestMemberObj);
        this.saveAttendence(this.updatedAttendance);
    }

    markCommitteeAttendence(member) {
        this.guestMemberObj = Object.assign({}, member);
        this.guestMemberObj.acType = 'U';
        this.guestMemberObj.memberPresent = member.memberPresent === 'Y' ? 'N' : 'Y';
        this.guestMemberObj.updateTimestamp = new Date().getTime();
        this.guestMemberObj.updateUser = this.userDTO.userName;
        this.updatedAttendance.push(this.guestMemberObj);
        if (this.guestMemberObj.memberPresent === 'Y')  {
            const alternateMember = this.checkMemberIsAlternate();
            if (alternateMember.isAlternateMember) {
               this.markAlternateMemberAbsent(alternateMember.member);
            }
        }
        this.saveAttendence(this.updatedAttendance);
    }

    saveAttendence(attendenceList) {
        const requestObject = {
            committeeId: this.committeeId, scheduleId: this.scheduleId,
            committeeSchedule: this.committeeSchedule, updatedAttendance: attendenceList
        };
        this._spinner.show();
        this.scheduleAttendanceService.updateMeetingAttendence(requestObject).subscribe((data: any) => {
            this._spinner.hide();
            this.committeeMemberActiveList = data.committeeMember != null ? data.committeeMember : [];
            this.alternateMember = data.alternateMember != null ? data.alternateMember : [];
            this.guestMembers = data.guestMembers != null ? data.guestMembers : [];
            this.getAlternateFor();
            this.updatedAttendance = [];
            this.guestMemberObj = {};
            this.markAllPresent = false;
            this.editAlternateAttendenceIndex = null;
            this.editCommitteeAttendenceIndex = null;
            this.editGuestAttendenceIndex = null;
            this.guestMemberObj = {};
        });

    }
    markAllMembers() {
            this.updatedAttendance = [];
            const absentMembers = this.committeeMemberActiveList.filter(member => (member.memberPresent === 'N'
                || member.memberPresent == null));
            absentMembers.forEach(member => {
                this.guestMemberObj = {};
                this.guestMemberObj = member;
                this.guestMemberObj.acType = 'U';
                this.guestMemberObj.memberPresent = 'Y';
                this.guestMemberObj.updateTimestamp = new Date().getTime();
                this.guestMemberObj.updateUser = this.userDTO.userName;
                this.updatedAttendance.push(this.guestMemberObj);
                const alternateMember = this.checkMemberIsAlternate();
                if (alternateMember.isAlternateMember) {
                   this.markAlternateMemberAbsent(alternateMember.member);
                }
                this.saveAttendence(this.updatedAttendance);
            });
    }

    deleteGuestAttendence(member) {
        this.guestMemberObj = Object.assign({}, member);
        this.guestMemberObj.acType = 'D';
        this.updatedAttendance.push(this.guestMemberObj);
        this.saveAttendence(this.updatedAttendance);
    }
    addGuestAttendence() {
        this.guestMemberObj.acType = 'U';
        this.guestMemberObj.memberPresent = 'Y';
        this.guestMemberObj.personName = this.selectedPerson.fullName;
        this.guestMemberObj.guestFlag = true;
        this.guestMemberObj.alternateFlag = false;
        this.guestMemberObj.updateTimestamp = new Date().getTime();
        this.guestMemberObj.updateUser = this.userDTO.userName;
        this.updatedAttendance.push(this.guestMemberObj);
         // tslint:disable-next-line:no-construct
         this.clearField = new String('true');
         this.showPersonElasticBand = false;
        this.saveAttendence(this.updatedAttendance);
    }

    saveAlternateAttendence() {
        const isAlternateMember = this.checkMemberChoosenAsAlternate();
        if (isAlternateMember && this.guestMemberObj.memberPresent === 'Y') {
            this.message = 'Alternate for \'' + this.guestMemberObj.alternateFor + '\' is already choosen.' ;
            document.getElementById('warningModalBtn').click();
        } else {
        this.updatedAttendance.push(this.guestMemberObj);
        this.saveAttendence(this.updatedAttendance);
        }

    }

    markAlternateAttendence(member) {
        this.guestMemberObj = Object.assign({}, member);
        this.guestMemberObj.memberPresent = member.memberPresent === 'Y' ? 'N' : 'Y';
        this.guestMemberObj.alternateFor = this.guestMemberObj.memberPresent === 'N' ? null : this.guestMemberObj.alternateFor;
        this.guestMemberObj.acType = 'U';
            this.guestMemberObj.updateTimestamp = new Date().getTime();
            this.guestMemberObj.updateUser = this.userDTO.userName;
        if ((this.guestMemberObj.alternateFor == null || this.guestMemberObj.alternateFor === undefined) &&
        this.guestMemberObj.memberPresent === 'Y') {
                this.message = 'Please Choose the \'Alternate For\' before marking Attendence.';
                document.getElementById('warningModalBtn').click();
        } else {
            this.saveAlternateAttendence();
        }
    }

    checkMemberIsAlternate() {
        let alternateMember: any = {};
        this.alternateMember.forEach( person => {
            if (person.alternateFor === this.guestMemberObj.personName) {
                alternateMember = { isAlternateMember: true, member: person };
            }
        });
        return alternateMember;
    }
    checkMemberChoosenAsAlternate() {
        let isAlternateMember = false;
        this.alternateMember.forEach( person => {
            if (person.alternateFor === this.guestMemberObj.alternateFor && person.personId !== this.guestMemberObj.personId) {
                isAlternateMember = true;
            }
        });
        return isAlternateMember;
    }
    getAllActiveMembers() {
        const requestObject = { committeeId: this.committeeId, scheduleId: this.scheduleId};
        this._spinner.show();
        this.scheduleAttendanceService.getAllActiveMembers(requestObject).subscribe((data: any) => {
            this._spinner.hide();
            this.committeeMemberActiveList = data.committeeMember != null ? data.committeeMember : [];
            this.alternateMember = data.alternateMember != null ? data.alternateMember : [];
            this.guestMembers = data.guestMembers != null ? data.guestMembers : [];
            this.getAlternateFor();
        });
    }

    markAlternateMemberAbsent(member) {
        this.guestMemberObj = Object.assign({}, member);
        this.guestMemberObj.acType = 'U';
        this.guestMemberObj.memberPresent = 'N';
        this.guestMemberObj.alternateFor = null;
        this.updatedAttendance.push(this.guestMemberObj);
    }
}


