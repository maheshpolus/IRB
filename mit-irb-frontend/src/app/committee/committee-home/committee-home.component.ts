import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

import { CommitteeSaveService } from '../committee-save.service';
import { CompleterService, CompleterData, CompleterItem } from 'ng2-completer';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/takeUntil';
import { CommitteeConfigurationService } from '../../common/service/committee-configuration.service';
import { KeyPressEvent } from '../../common/directives/keyPressEvent.component';

declare var $: any;
@Component({
    selector: 'app-committee-home',
    templateUrl: './committee-home.component.html',
    providers: [CommitteeSaveService]
})

export class CommitteeHomeComponent implements OnInit, OnDestroy {
    addResearch = false;
    editDetails = false;
    editResearch = false;
    isAreaReseachSearch = false;
    deleteResearch = false;
    isHomeUnitSearch = false;
    showSaveAreaOfResearch = false;
    isScheduleFieldsFilled = true;
    reviewType: string;
    description: string;
    minMembers: string;
    advSubDays: string;
    unitSearchString: string;
    errorFlag: boolean;
    error = '';
    addArea = '';
    areaOfReasearch: any[] = [];
    homeUnitSearchResult: any = [];
    areaResearchSearchResult: any = [];
    previousEditscheduleId: any;
    editClass: string;
    editAreaClass: string;
    maxProtocols: string;
    mode: string;
    public areaInput: any = {};
    slNo = 0;
    public researchArea: any = {};
    public dataServiceArea: any = [];
    userDTO: any;


    Id: string;
    Name: string;
    Type: string;
    Unit: string;
    unitName: string;
    editFlag: boolean;
    modeFlag: string;
    committeeTypeList: any[];
    areaList: any = [];
    scheduleStatus: any[];
    committeeData: any = {};
    result: any = {};
    resultTemp: any = {};
    showPopup = false;
    deleteConfirmation = false;
    areaOfResearchToDelete: any = {};
    duplicateIdFlag = false;
    emptyAreaOfResearch = false;
    duplicateAreaOfResearch = false;
    isAddClicked = false;
    deleteMsg = '';
    alertMsg = '';
    name: string;
    unit: string;
    unitname: string;
    addResearchArea: string;

    showGenerateSchedule = false;
    homeUnitData: any = {};
    scheduleData: any = {};
    sendScheduleRequestData: any;
    optionDay = 'XDAY';
    currentScheduleTab = 'DAILY';
    editSchedule = {};
    editScheduleClass = 'committeeBoxNotEditable';
    showConflictDates = false;

    dayOptions = [
        { name: 'Sunday', value: 'Sunday', checked: false },
        { name: 'Monday', value: 'Monday', checked: false },
        { name: 'Tuesday', value: 'Tuesday', checked: false },
        { name: 'Wednesday', value: 'Wednesday', checked: false },
        { name: 'Thursday', value: 'Thursday', checked: false },
        { name: 'Friday', value: 'Friday', checked: false },
        { name: 'Saturday', value: 'Saturday', checked: false }
    ];
    selectedMonthWeek = [
        { name: 'First', value: 'first' },
        { name: 'Second', value: 'second' },
        { name: 'Third', value: 'third' },
        { name: 'Fourth', value: 'fourth' },
        { name: 'Last', value: 'last' }
    ];
    yearMonthOptions = [
        { name: 'January', value: 'JANUARY' },
        { name: 'February', value: 'FEBRUARY' },
        { name: 'March', value: 'MARCH' },
        { name: 'April', value: 'APRIL' },
        { name: 'May', value: 'MAY' },
        { name: 'June', value: 'JUNE' },
        { name: 'July', value: 'JULY' },
        { name: 'August', value: 'AUGUST' },
        { name: 'September', value: 'SEPTEMBER' },
        { name: 'October', value: 'OCTOBER' },
        { name: 'November', value: 'NOVEMBER' },
        { name: 'December', value: 'DECEMBER' }
    ];

    monthOptionDay = 'XDAYANDXMONTH';
    selectedDayOfWeek: string;
    day = 1;
    option1Month = 1;
    yearOption = 'XDAY';
    isTodelete = false;
    scheduledDate: string;
    scheduleId: string;
    committeeId: string;
    isConflictDates = false;
    isMandatoryFilled = true;
    conflictDates: any = [];
    homeUnitList: any = [];
    filterStartDate: string;
    filerEndDate: string;
    today: any;
    scheduleValidationMessage: string;
    researchSearchString: string;
    filterValidationMessage: string;
    isDatePrevious = true;
    isStartDateBeforeToday = false;
    dateTime: { time: string, meridiem: string };
    displayTime: any = {};
    isMandatoryFilterFilled = true;
    isFilterDatePrevious = false;
    showDetails = false;
    isFilterClicked = false;
    listDate: string;
    listStatus: string;
    listTime: string;
    listPlace: string;
    isScheduleListItemEditMode = false;
    saveCommitteeFlag = false;
    isAreaOfResearchEditMode = false;
    scheduleTime: any;
    isEditDetailsModalOpen = false;
    isScheduleEditWarningModalOpen = false;
    public dataServiceHomeUnit: CompleterData;
    public onDestroy$ = new Subject<void>();


    constructor(public route: ActivatedRoute,
        private datePipe: DatePipe,
        public router: Router,
        private completerService: CompleterService,
        public keyPressEvent: KeyPressEvent,
        public committeeSaveService: CommitteeSaveService,
        private committeeConfigurationService: CommitteeConfigurationService,
        public toastr: ToastsManager) {
        this.committeeConfigurationService.currentMode.takeUntil(this.onDestroy$).subscribe(data => {
            this.mode = data;
        }, error => { }, () => {
        });
        this.resultTemp = {};
        this.resultTemp.committee = {};
        this.loadTempData();
        this.initialLoadChild();
    }

    loadTempData() {
        this.committeeConfigurationService.currentCommitteeData.takeUntil(this.onDestroy$).subscribe(data => {
            this.resultTemp = data;
            if (this.resultTemp.committee !== undefined) {
                this.name = this.resultTemp.committee.committeeName;
                this.unit = this.resultTemp.committee.homeUnitNumber;
                this.unitname = this.resultTemp.committee.homeUnitName;
                this.Id = this.resultTemp.committee.committeeId;
                this.Name = this.resultTemp.committee.committeeName;
                this.Type = this.resultTemp.committee.committeeType != null ? this.resultTemp.committee.committeeType.description : null;
                this.Unit = this.resultTemp.committee.homeUnitNumber;
                this.unitName = this.resultTemp.committee.homeUnitName;
                this.committeeTypeList = this.resultTemp.committeeTypeList;
                if (this.resultTemp.homeUnits != null || this.resultTemp.homeUnits !== undefined) {
                    this.homeUnitList = this.resultTemp.homeUnits;
                    this.dataServiceHomeUnit = this.completerService.local(this.homeUnitList, 'unitName', 'unitName');
                }
                // this.homeUnitList = this.resultTemp.homeUnits;
                this.committeeConfigurationService.currentAreaOfResearch.subscribe(data2 => {
                    this.areaList = data2;
                });
                if (this.resultTemp.scheduleStatus !== null) {
                    this.scheduleStatus = this.resultTemp.scheduleStatus;
                }
            }
        });
    }

    ngOnInit() {
        this.committeeConfigurationService.changeActivatedtab('committee_home');
        this.userDTO = JSON.parse(localStorage.getItem('currentUser'));

    }

    ngOnDestroy() {
        this.onDestroy$.next();
        this.onDestroy$.complete();
    }

    initialLoadChild() {
        this.committeeConfigurationService.currentCommitteeData.takeUntil(this.onDestroy$).subscribe(data => {
            this.result = data;
            if (this.result.committee !== undefined) {
                if (this.result.committee == null || this.result.committee === undefined) {
                    this.result.committee = {};
                }
                if (this.result.committee.committeeSchedules == null || this.result.committee.committeeSchedules === undefined) {
                    this.result.committee.committeeSchedules = [];
                }
                if (this.result.scheduleData == null || this.result.scheduleData === undefined) {
                    this.result.scheduleData = {};
                    this.result.scheduleData.time = {};
                    this.result.scheduleData.dailySchedule = {};
                    this.result.scheduleData.datesInConflict = [];
                    this.result.scheduleData.weeklySchedule = {};
                    this.result.scheduleData.monthlySchedule = {};
                    this.result.scheduleData.yearlySchedule = {};
                    this.result.committee.committeeSchedules.scheduleStatus = {};
                    this.result.scheduleData.recurrenceType = 'DAILY';
                    if (this.optionDay === 'XDAY') {
                        this.result.scheduleData.dailySchedule.day = 1;
                    }
                }

                if (this.mode === 'view') {
                    this.errorFlag = false;
                    this.editDetails = false;
                    this.committeeConfigurationService.changeEditFlag(this.editDetails);
                    this.Id = this.result.committee.committeeId;
                    this.Name = this.result.committee.committeeName;
                    this.unitName = this.result.committee.homeUnitName;
                    this.Unit = this.result.committee.homeUnitNumber;
                    this.editClass = 'committeeBoxNotEditable';
                    this.editAreaClass = 'committeeBoxNotEditable';
                    this.reviewType = this.result.committee.reviewTypeDescription;
                    this.description = this.result.committee.description;
                    this.minMembers = this.result.committee.minimumMembersRequired;
                    this.advSubDays = this.result.committee.advSubmissionDaysReq;
                    this.maxProtocols = this.result.committee.maxProtocols;
                    this.showDetails = true;
                    this.saveCommitteeFlag = true;
                } else {
                    this.editClass = 'scheduleBoxes';
                    this.editAreaClass = 'scheduleBoxes';
                    this.result.committee.reviewTypeDescription = this.result.committee.reviewTypeDescription == null ?
                        '' : this.result.committee.reviewTypeDescription;
                    this.editDetails = true;
                    this.Id = this.result.committee.committeeId;
                    this.showDetails = false;
                }
            }
        });
    }

    changeCommitteeType(committeeTypeCode) {
        const selectedType = this.committeeTypeList.filter(x => x.committeeTypeCode.toString() === committeeTypeCode);
        if (selectedType.length > 0) {
            this.result.committee.committeeType = this.result.committee.committeeType != null ? this.result.committee.committeeType : {};
            this.result.committee.committeeType.description = selectedType[0].description;
            this.result.committee.committeeType.committeeTypeCode = selectedType[0].committeeTypeCode;
        } else {
            this.result.committee.committeeType.description = null;
            this.result.committee.committeeType.committeeTypeCode = null;
        }
    }

    showaddAreaOfResearch() {
        if (this.editDetails === true) {
            this.isEditDetailsModalOpen = true;
        }
        if (this.saveCommitteeFlag) {
            this.addResearch = !this.addResearch;
            this.editResearch = !this.editResearch;
            if (this.editResearch) {
                this.editAreaClass = 'scheduleBoxes';
            }
        }
    }

    closeEditDetailsModalOpen() {
        this.isEditDetailsModalOpen = false;
        this.showGenerateSchedule = false;
        this.deleteConfirmation = false;
    }

    onHomeSelect(selected: CompleterItem) {
        if (selected) {
            if (selected.originalObject != null && selected.originalObject !== undefined) {
                this.result.committee.homeUnitNumber = selected.originalObject.unitNumber;
            } else {
                this.result.committee.homeUnitNumber = null;
            }
        }
    }
    showEditDetails() {
        this.editDetails = !this.editDetails;
        this.saveCommitteeFlag = false;
        if (this.editDetails) {
            this.editClass = 'scheduleBoxes';
        }
        this.committeeConfigurationService.changeEditFlag(this.editDetails);
    }

    saveDetails(dataObject) {
        if (dataObject !== undefined) {
            this.result = dataObject.result;
            this.committeeConfigurationService.changeCommmitteeData(this.result);
        }
        if ((this.result.committee.committeeId == null ||
            this.result.committee.committeeName === null || this.result.committee.committeeName === '' ||
            this.result.committee.committeeName === undefined ||
            this.result.committee.committeeTypeCode === null || this.result.committee.committeeTypeCode === '')) {
            this.errorFlag = true;
            this.error = '*Please fill all the mandatory fields marked';
        } else {
            this.isEditDetailsModalOpen = false;
            this.showGenerateSchedule = false;
            this.deleteConfirmation = false;
            this.error = '';
            this.errorFlag = false;
            this.showDetails = true;
            this.committeeConfigurationService.changeCommmitteeData(this.result);
            this.committeeConfigurationService.currentCommitteeData.takeUntil(this.onDestroy$).subscribe(data => {
                this.result = data;
            });
            if (this.mode === 'create') {
                this.result.updateType = 'SAVE';
                this.result.committee.committeeType = {};
                this.result.committee.committeeType.committeeTypeCode = '1';
                this.result.committee.committeeType.description = 'IRB';
                this.result.committee.createUser = this.userDTO.userName;
                this.result.committee.createTimestamp = new Date().getTime();
                this.result.committee.updateUser = this.userDTO.userName;
                this.result.committee.updateTimestamp = new Date().getTime();
            } else if (this.mode === 'view') {
                this.result.updateType = 'UPDATE';
                this.result.committee.updateUser = this.userDTO.userName;
                this.result.committee.updateTimestamp = new Date().getTime();
            }
            this.result.currentUser = this.userDTO.userName;
            if (this.editDetails === false) {
                this.editClass = 'committeeBoxNotEditable';
            }
            this.committeeSaveService.saveCommitteeData(this.result).takeUntil(this.onDestroy$).subscribe(data => {
                this.result = data || [];
                if (this.result.status === true) {
                if (this.result != null) {
                    this.toastr.success('Committee saved successfully', null, { toastLife: 2000 });
                    this.committeeConfigurationService.changeCommmitteeData(this.result);
                    this.saveCommitteeFlag = true;
                    if (this.mode === 'view') {
                        this.initialLoadChild();
                    } else {
                        this.editDetails = !this.editDetails;
                        this.committeeConfigurationService.changeEditFlag(this.editDetails);
                        this.mode = 'view';
                        this.initialLoadChild();
                        this.committeeConfigurationService.changeMode(this.mode);
                        // Change the url in view mode
                        this.router.navigate(['irb/committee/committeeHome'], { queryParams: { 'mode': this.mode, 'id': this.Id } });
                    }
                }
                this.committeeConfigurationService.currentactivatedTab.subscribe(data2 => {
                    if (data2 === 'committee_members') {
                        this.router.navigate(['irb/committee/committeeMembers'], { queryParams: { 'mode': this.mode, 'id': this.Id } });
                    }
                });
            } else {
                this.toastr.error(this.result.message, null, { toastLife: 2000 });
            }
            });
        }
    }

    duplicateId() {
        this.clear();
    }

    cancelEditDetails() {
        this.errorFlag = false;
        this.isEditDetailsModalOpen = false;
        this.showGenerateSchedule = false;
        if (this.mode === 'view') {
            this.editDetails = !this.editDetails;
            this.committeeConfigurationService.changeEditFlag(this.editDetails);
            if (!this.editDetails) {
                this.editClass = 'committeeBoxNotEditable';
            }
            this.saveCommitteeFlag = true;
            this.result.committee.committeeId = this.Id;
            this.result.committee.committeeType.description = 'IRB';
            this.result.committee.committeeName = this.name;
            this.result.committee.homeUnitName = this.unitname;
            this.result.committee.homeUnitNumber = this.unit;
            this.result.committee.reviewTypeDescription = this.reviewType;
            this.result.committee.description = this.description;
            this.result.committee.minimumMembersRequired = this.minMembers;
            this.result.committee.advSubmissionDaysReq = this.advSubDays;
            this.result.committee.maxProtocols = this.maxProtocols;
        } else {
            this.result.committee.committeeType.description = '';
            this.result.committee.committeeName = '';
            this.result.committee.homeUnitName = '';
            this.result.committee.reviewTypeDescription = '';
            this.result.committee.description = '';
            this.result.committee.minimumMembersRequired = '';
            this.result.committee.advSubmissionDaysReq = '';
            this.result.committee.maxProtocols = '';
            this.router.navigate(['/irb/committee'], { queryParams: { mode: 'create' } });
        }
    }

    addAreaOfResearch(Object) {
        this.showSaveAreaOfResearch = true;
        this.addResearchArea = '0';
        this.editAreaClass = 'committeeBoxNotEditable';
        for (let i = 0; i < this.result.committee.researchAreas.length; i++) {
            if (Object.researchAreaCode === this.result.committee.researchAreas[i].researchAreaCode) {
                this.addResearchArea = '1';
            }
        }
        if (this.addResearchArea === '1') {
            // this.showPopup = true;
            this.duplicateAreaOfResearch = true;
            this.alertMsg = 'Cannot add  Research area since it is already there in the committee!';
        } else if (Object.researchAreaDescription === '' || Object.researchAreaDescription === undefined ||
        Object.researchAreaCode == null || Object.researchAreaCode === undefined) {
            // this.showPopup = true;
            this.emptyAreaOfResearch = true;
            this.alertMsg = 'Please select an Area of research to add!';
        } else {
            this.duplicateAreaOfResearch = false;
            this.emptyAreaOfResearch = false;
            this.committeeSaveService.saveResearchAreaCommitteeData(this.result.committee.committeeId, Object)
                .takeUntil(this.onDestroy$)
                .subscribe(data => {
                    let temp: any;
                    temp = data;
                    // this.result = data || [];
                    this.result.committee = temp.committee;
                    // this.result.committee.updateTimestamp = data.updateTimestamp;
                    this.committeeConfigurationService.changeCommmitteeData(this.result);
                });
            this.initialLoadChild();
            this.deleteResearch = false;
        }
        this.areaInput = {};
    }

    showEditResearch() {
        this.editResearch = !this.editResearch;
        if (this.editResearch) {
            this.editAreaClass = 'scheduleBoxes';
        }
    }

    areaChangeFunction(researchAreaDescription) {
    }

    onAreaSelect() {
        this.areaList._data.forEach((value, index) => {
            if (value.description === this.areaInput.researchAreaDescription) {
                this.areaInput.researchAreaCode = value.researchAreaCode;
                this.areaInput.researchAreaDescription = value.description;
                this.areaInput.updateUser = this.userDTO.userName;
                this.areaInput.updateTimestamp = new Date().getTime();
            }
        });
    }

    deleteAreaOfResearchConfirmation($event, Object) {
        event.preventDefault();
        if (this.editDetails === true) {
            this.isEditDetailsModalOpen = true;
        } else {
            this.showPopup = true;
            this.deleteConfirmation = true;
            this.areaOfResearchToDelete = Object;
            this.deleteMsg = 'Are you sure you want to delete this area of research?';
        }
    }

    clear() {
        this.showPopup = false;
        this.deleteConfirmation = false;
        this.duplicateIdFlag = false;
        this.deleteMsg = '';
        this.alertMsg = '';
        this.duplicateAreaOfResearch = false;
        this.emptyAreaOfResearch = false;
    }

    deleteAreaOfResearch(Object) {

        if (this.result.committee.researchAreas.length != null && Object.commResearchAreasId !== undefined) {
            this.committeeSaveService.deleteAreaOfResearch(Object.commResearchAreasId, this.result.committee.committeeId)
                .takeUntil(this.onDestroy$).subscribe(data => {
                    // this.result = data || [];
                    let temp: any;
                    temp = data;
                    this.result.committee = temp.committee || [];
                    this.result.message = data.message;
                    this.result.status = data.status;
                    // this.result.committee.updateTimestamp = data.updateTimestamp;
                    this.committeeConfigurationService.changeCommmitteeData(this.result);
                });
        } else if (Object.commResearchAreasId === undefined || Object.commResearchAreasId == null) {
            this.result.committee.researchAreas.forEach((value, index) => {
                if (value.researchAreaCode === Object.researchAreaCode) {
                    this.result.committee.researchAreas.splice(index, 1);
                }
            });
        }
        this.clear();
        this.initialLoadChild();
        this.deleteResearch = true;
    }

    showSchedulePopUp() {
        if (this.editDetails === true) {
            this.isEditDetailsModalOpen = true;
        } else {
            if (this.showGenerateSchedule === false) {
                this.showGenerateSchedule = true;
            }
            const q = new Date();
            const m = q.getMonth();
            const d = q.getDate();
            const y = q.getFullYear();
            this.today = new Date(y, m, d);
            this.result.scheduleData = {};
            this.result.scheduleData.time = {};
            this.result.scheduleData.dailySchedule = {};
            this.result.scheduleData.weeklySchedule = {};
            this.result.scheduleData.monthlySchedule = {};
            this.result.scheduleData.yearlySchedule = {};
            this.result.scheduleData.scheduleStartDate = this.today;
            // this.result.scheduleData.dailySchedule.scheduleEndDate = this.today;
            // this.result.scheduleData.weeklySchedule.scheduleEndDate = this.today;
            // this.result.scheduleData.monthlySchedule.scheduleEndDate = this.today;
            // this.result.scheduleData.yearlySchedule.scheduleEndDate = this.today;
            this.result.scheduleData.recurrenceType = 'MONTHLY';
            this.result.scheduleData.dailySchedule.day = 1;
            this.displayTime = null;
            this.isDatePrevious = false;
            this.isMandatoryFilled = true;
            this.isStartDateBeforeToday = false;
            this.monthOptionDay = 'XDAYANDXMONTH';
                this.result.scheduleData.monthlySchedule.day = 1;
                this.result.scheduleData.monthlySchedule.option1Month = 1;
                this.result.scheduleData.monthlySchedule.selectedMonthsWeek = '';
                this.result.scheduleData.monthlySchedule.selectedDayOfWeek = '';
                this.result.scheduleData.monthlySchedule.option2Month = null;
        }
    }

    showTab(recurrenceType) {
        this.result.scheduleData.recurrenceType = recurrenceType;
        switch (this.result.scheduleData.recurrenceType) {
            case 'WEEKLY': this.result.scheduleData.weeklySchedule.week = 1; break;
            case 'MONTHLY': this.monthOptionDay = 'XDAYANDXMONTH';
                this.result.scheduleData.monthlySchedule.day = 1;
                this.result.scheduleData.monthlySchedule.option1Month = 1;
                this.result.scheduleData.monthlySchedule.selectedMonthsWeek = '';
                this.result.scheduleData.monthlySchedule.selectedDayOfWeek = '';
                this.result.scheduleData.monthlySchedule.option2Month = null;
                break;
            case 'YEARLY': this.yearOption = 'XDAY';
                this.result.scheduleData.yearlySchedule.day = 1;
                this.result.scheduleData.yearlySchedule.option1Year = 1;
                this.result.scheduleData.yearlySchedule.option2Year = null;
                break;
        }
    }

    sentDayOption() {
        setTimeout(() => {
            if (this.optionDay === 'XDAY') {
                this.result.scheduleData.dailySchedule.day = 1;
            } else {
                this.result.scheduleData.dailySchedule.day = '';
            }
        }, 100);
    }

    sentMonthOption() {
        setTimeout(() => {
            if (this.monthOptionDay === 'XDAYANDXMONTH') {
                this.result.scheduleData.monthlySchedule.day = 1;
                this.result.scheduleData.monthlySchedule.option1Month = 1;
                this.result.scheduleData.monthlySchedule.selectedMonthsWeek = '';
                this.result.scheduleData.monthlySchedule.selectedDayOfWeek = '';
                this.result.scheduleData.monthlySchedule.option2Month = null;
            } else {
                this.result.scheduleData.monthlySchedule.day = null;
                this.result.scheduleData.monthlySchedule.option1Month = null;
                this.result.scheduleData.monthlySchedule.option2Month = 1;
            }
        }, 100);
    }

    sentYearOption() {
        setTimeout(() => {
            if (this.yearOption === 'XDAY') {
                this.result.scheduleData.yearlySchedule.day = 1;
                this.result.scheduleData.yearlySchedule.option1Year = 1;
                this.result.scheduleData.yearlySchedule.option2Year = null;
            } else {
                this.result.scheduleData.yearlySchedule.day = null;
                this.result.scheduleData.yearlySchedule.option1Year = null;
                this.result.scheduleData.yearlySchedule.option2Year = 1;
            }
        }, 100);
    }

    generateSchedule(e) {
        e.preventDefault();
        this.result.scheduleData.time.time = this.datePipe.transform(this.displayTime, 'hh:mm');
        this.result.scheduleData.time.meridiem = this.datePipe.transform(this.displayTime, 'aa');
        this.sendScheduleRequestData = {};
        this.sendScheduleRequestData.scheduleData = {};
        this.sendScheduleRequestData.scheduleData.weeklySchedule = {};
        this.sendScheduleRequestData.scheduleData.monthlySchedule = {};
        this.sendScheduleRequestData.scheduleData.yearlySchedule = {};
        this.sendScheduleRequestData.currentUser = this.userDTO.userName;
        this.sendScheduleRequestData.committee = this.result.committee;
        if (this.result.scheduleData.scheduleStartDate < this.today) {
            this.isStartDateBeforeToday = true;
            this.scheduleValidationMessage = '* Please ensure start date is today or upcomming days';
        } else {
            this.isStartDateBeforeToday = false;
        }
        switch (this.result.scheduleData.recurrenceType) {
            case 'DAILY': this.result.scheduleData.dailySchedule.dayOption = this.optionDay;
                this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
                if (this.result.scheduleData.scheduleStartDate > this.result.scheduleData.dailySchedule.scheduleEndDate) {
                    this.isDatePrevious = true;
                    this.scheduleValidationMessage = '* Please ensure the end date is after the start date';
                } else {
                    this.isDatePrevious = false;
                }
                if (this.result.scheduleData.scheduleStartDate == null ||
                    this.result.scheduleData.dailySchedule.scheduleEndDate == null ||
                    this.displayTime == null || this.result.scheduleData.place == null ||
                    this.result.scheduleData.place === '' || this.result.scheduleData.place === undefined) {
                    this.isMandatoryFilled = false;
                    this.scheduleValidationMessage = '* Please fill the mandatory fields.';
                } else {
                    this.isMandatoryFilled = true;
                }
                break;
            case 'WEEKLY': this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
                this.sendScheduleRequestData.scheduleData.weeklySchedule.daysOfWeek = this.selectedOptions;
                if (this.result.scheduleData.scheduleStartDate > this.result.scheduleData.weeklySchedule.scheduleEndDate) {
                    this.isDatePrevious = true;
                    this.scheduleValidationMessage = '* Please ensure the end date is after the start date';
                } else {
                    this.isDatePrevious = false;
                }
                if (this.result.scheduleData.scheduleStartDate == null ||
                    this.result.scheduleData.weeklySchedule.scheduleEndDate == null ||
                    this.displayTime == null || this.result.scheduleData.place == null) {
                    this.isMandatoryFilled = false;
                    this.scheduleValidationMessage = '* Please fill the mandatory fields.';
                } else {
                    this.isMandatoryFilled = true;
                }
                break;
            case 'MONTHLY': this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
                this.sendScheduleRequestData.scheduleData.monthlySchedule.monthOption = this.monthOptionDay;
                if (this.result.scheduleData.scheduleStartDate > this.result.scheduleData.monthlySchedule.scheduleEndDate) {
                    this.isDatePrevious = true;
                    this.scheduleValidationMessage = '* Please ensure the end date is after the start date';
                } else {
                    this.isDatePrevious = false;
                }
                if (this.result.scheduleData.scheduleStartDate == null ||
                    this.result.scheduleData.monthlySchedule.scheduleEndDate == null ||
                    this.displayTime == null || this.result.scheduleData.place == null) {
                    this.isMandatoryFilled = false;
                    this.scheduleValidationMessage = '* Please fill the mandatory fields.';
                } else {
                    this.isMandatoryFilled = true;
                }
                break;
            case 'YEARLY': this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
                this.sendScheduleRequestData.scheduleData.yearlySchedule.yearOption = this.yearOption;
                if (this.result.scheduleData.scheduleStartDate > this.result.scheduleData.yearlySchedule.scheduleEndDate) {
                    this.isDatePrevious = true;
                    this.scheduleValidationMessage = '* Please ensure the end date is after the start date';
                } else {
                    this.isDatePrevious = false;
                }
                if (this.result.scheduleData.scheduleStartDate == null ||
                    this.result.scheduleData.yearlySchedule.scheduleEndDate == null ||
                    this.displayTime == null || this.result.scheduleData.place == null) {
                    this.isMandatoryFilled = false;
                    this.scheduleValidationMessage = '* Please fill the mandatory fields.';
                } else {
                    this.isMandatoryFilled = true;
                }
                break;
            case 'NEVER': this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
                this.isDatePrevious = false;
                if (this.result.scheduleData.scheduleStartDate == null ||
                    this.displayTime == null || this.result.scheduleData.place == null) {
                    this.isMandatoryFilled = false;
                    this.scheduleValidationMessage = '* Please fill the mandatory fields.';
                } else {
                    this.isMandatoryFilled = true;
                }
                break;
        }

        if (this.isDatePrevious === false && this.isStartDateBeforeToday === false && this.isMandatoryFilled === true) {
            $('#exampleModalCenter').modal('toggle');
            this.committeeSaveService.saveScheduleData(this.sendScheduleRequestData).takeUntil(this.onDestroy$).subscribe(data => {
                this.result = data || [];
                this.filterStartDate = this.result.scheduleData.filterStartDate;
                this.conflictDates = this.result.scheduleData.datesInConflict;
                if (this.conflictDates != null && this.conflictDates.length > 0) {
                    this.isConflictDates = true;
                    document.getElementById('skippedSchedulesButton').click();
                }
                this.filerEndDate = this.result.scheduleData.filerEndDate;
                this.result.scheduleData = {};
                this.result.scheduleData.time = {};
                this.result.scheduleData.dailySchedule = {};
                this.result.scheduleData.weeklySchedule = {};
                this.result.scheduleData.monthlySchedule = {};
                this.result.scheduleData.yearlySchedule = {};
                this.result.scheduleData.filterStartDate = this.filterStartDate;
                this.result.scheduleData.filerEndDate = this.filerEndDate;
                this.showGenerateSchedule = false;
                this.committeeConfigurationService.changeCommmitteeData(this.result);
            });
            this.initialLoadChild();
        }
    }

    editScheduleData(e, date, status, place, time, i, scheduleId) {
        e.preventDefault();
        if (this.isScheduleListItemEditMode === true) {
            this.alertMsg = 'You are editing a schedule data with serial number : ' + (this.previousEditscheduleId);
            this.isScheduleEditWarningModalOpen = true;
        } else {
            this.alertMsg = '';
            this.isScheduleEditWarningModalOpen = false;
            this.isScheduleListItemEditMode = true;
            this.scheduleTime = new Date(time);
            this.editSchedule[i] = !this.editSchedule[i];
            this.listDate = date;
            this.previousEditscheduleId = scheduleId;
            this.listStatus = status;
            this.listPlace = place;
            this.listTime = time;
        }
    }

    showDeleteModal(e, scheduleObject, committeeId, scheduledDate) {
        e.preventDefault();
        if (this.isScheduleListItemEditMode === true) {
            this.alertMsg = 'You are editing a schedule data with serial number : ' + (scheduleObject.scheduleId);
            this.isScheduleEditWarningModalOpen = true;
        } else {
            this.alertMsg = '';
            this.isScheduleEditWarningModalOpen = false;
            this.scheduledDate = scheduledDate;
            this.scheduleId = scheduleObject.scheduleId;
            this.sendScheduleRequestData = {};
        scheduleObject.viewTime = {};
        scheduleObject.viewTime.time = this.datePipe.transform(this.scheduleTime, 'hh:mm');
        scheduleObject.viewTime.meridiem = this.datePipe.transform(this.scheduleTime, 'aa');
        scheduleObject.scheduleStatus.updateTimestamp = new Date();
        scheduleObject.scheduleStatus.updateUser = this.userDTO.userName;
        this.scheduleStatus.forEach((value, index) => {
            if (value.description === scheduleObject.scheduleStatus.description) {
                value.updateTimestamp = new Date();
                value.updateUser = this.userDTO.userName;
                scheduleObject.scheduleStatusCode = value.scheduleStatusCode;
                scheduleObject.scheduleStatus = value;
                // this.result.committee.committeeSchedules[i].scheduleStatus.description = value.description;
            }
        });
        this.sendScheduleRequestData.committeeSchedule = scheduleObject;
            this.committeeId = committeeId;
            if (this.isTodelete === false) {
                this.isTodelete = true;
            }
        }
    }

    closeConflictDateModal() {
        if (this.isConflictDates === true) {
            this.isConflictDates = false;
        }
    }

    deleteScheduleData() {
       // this.sendScheduleRequestData = {};
        this.sendScheduleRequestData.scheduleId = this.scheduleId;
        this.sendScheduleRequestData.committeeId = this.committeeId;
        if (this.isTodelete === true) {
            this.isTodelete = false;
        }
        this.committeeSaveService.deleteScheduleData(this.sendScheduleRequestData).takeUntil(this.onDestroy$).subscribe(data => {
            let temp: any;
            temp = data || [];
            // this.result = data || [];
            this.result.committee.committeeSchedules = temp.committee.committeeSchedules;
             this.committeeConfigurationService.changeCommmitteeData(this.result);
        });
        this.initialLoadChild();
    }

    cancelDelete() {
        this.clear();
    }

    updateScheduleData(e, i, scheduleObject) {
        e.preventDefault();
         if (scheduleObject.scheduledDate == null || scheduleObject.place === '' ||
         scheduleObject.place == null || this.scheduleTime == null) {
            this.isScheduleFieldsFilled = false;
         } else {
            this.isScheduleFieldsFilled = true;
        this.editSchedule[i] = !this.editSchedule[i];
        this.sendScheduleRequestData = {};
        scheduleObject.viewTime = {};
        scheduleObject.viewTime.time = this.datePipe.transform(this.scheduleTime, 'hh:mm');
        scheduleObject.viewTime.meridiem = this.datePipe.transform(this.scheduleTime, 'aa');
        scheduleObject.scheduleStatus.updateTimestamp = new Date();
        scheduleObject.scheduleStatus.updateUser = this.userDTO.userName;
        this.scheduleStatus.forEach((value, index) => {
            if (value.description === scheduleObject.scheduleStatus.description) {
                value.updateTimestamp = new Date();
                value.updateUser = this.userDTO.userName;
                scheduleObject.scheduleStatusCode = value.scheduleStatusCode;
                scheduleObject.scheduleStatus = value;
                this.result.committee.committeeSchedules[i].scheduleStatus.description = value.description;
            }
        });
        this.sendScheduleRequestData.committeeSchedule = scheduleObject;
        this.sendScheduleRequestData.committeeId = this.result.committee.committeeId;
        this.sendScheduleRequestData.advSubmissionDaysReq = this.result.committee.advSubmissionDaysReq;
        this.committeeSaveService.updateScheduleData(this.sendScheduleRequestData).takeUntil(this.onDestroy$).subscribe(data => {
            // this.result = data || [];
            let temp: any;
            temp = data;
            this.result.committee.committeeSchedules = temp.committee.committeeSchedules;
            this.result.message = temp.message;
            this.result.status = temp.status;
            if (this.result.status === false) {
                document.getElementById('skippedSchedulesButton').click();
            }
           this.committeeConfigurationService.changeCommmitteeData(this.result);
        });
       this.initialLoadChild();
        this.isScheduleListItemEditMode = false;
    }
    }

    cancelEditSchedule(e, i) {
        e.preventDefault();
        this.editSchedule[i] = !this.editSchedule[i];
        this.isScheduleListItemEditMode = false;
        this.result.committee.committeeSchedules[i].scheduledDate = this.listDate;
        this.result.committee.committeeSchedules[i].scheduleStatus.description = this.listStatus;
        this.result.committee.committeeSchedules[i].time = this.listTime;
        this.result.committee.committeeSchedules[i].place = this.listPlace;
    }

    get selectedOptions() {
        return this.dayOptions
            .filter(option => option.checked)
            .map(option => option.value);
    }

    filterSchedule() {
        if (this.result.scheduleData.filterStartDate > this.result.scheduleData.filerEndDate) {
            this.isFilterDatePrevious = true;
            this.filterValidationMessage = '* Please ensure that the To : Date is greater than or equal to the From : Date.';
        } else {
            this.isFilterDatePrevious = false;
        }
        if (this.result.scheduleData.filterStartDate == null || this.result.scheduleData.filerEndDate == null) {
            this.isMandatoryFilterFilled = false;
            this.filterValidationMessage = '* Please enter the necessary dates to apply filter.';
        } else {
            this.isMandatoryFilterFilled = true;
        }
        if (this.isMandatoryFilterFilled === true && this.isFilterDatePrevious === false) {
            this.sendScheduleRequestData = {};
            if (this.result.scheduleData == null || this.result.scheduleData === undefined) {
                this.sendScheduleRequestData.scheduleData = {};
            } else {
                this.sendScheduleRequestData.scheduleData = this.result.scheduleData;
            }
            this.sendScheduleRequestData.committee = this.result.committee;
            this.committeeSaveService.filterScheduleData(this.sendScheduleRequestData).takeUntil(this.onDestroy$).subscribe(data => {
                this.result = data || [];
                this.committeeConfigurationService.changeCommmitteeData(this.result);
            });
            this.initialLoadChild();
        }
    }

    resetFilterSchedule() {
        if (this.isFilterDatePrevious === true || this.isMandatoryFilterFilled === false) {
            this.isFilterDatePrevious = false;
            this.isMandatoryFilterFilled = true;
            this.result.scheduleData.filterStartDate = null;
            this.result.scheduleData.filerEndDate = null;
        } else if (this.result.scheduleData.filterStartDate == null || this.result.scheduleData.filerEndDate == null) {
            this.isFilterDatePrevious = false;
            this.isMandatoryFilterFilled = true;
        } else {
            this.isFilterDatePrevious = false;
            this.isMandatoryFilterFilled = true;
            this.sendScheduleRequestData = {};
            this.sendScheduleRequestData.scheduleData = {};
            this.sendScheduleRequestData.committee = this.result.committee;
            this.committeeSaveService.resetFilterSchedule(this.sendScheduleRequestData).takeUntil(this.onDestroy$).subscribe(data => {
                this.result = data || [];
                this.committeeConfigurationService.changeCommmitteeData(this.result);
                this.result.scheduleData.filterStartDate = null;
                this.result.scheduleData.filerEndDate = null;
            });
            this.initialLoadChild();
        }
    }

    cancelEditScheduleItem() {
        this.isScheduleEditWarningModalOpen = !this.isScheduleEditWarningModalOpen;
        this.isTodelete = false;
    }

    loadSchedules(event: any, scheduleId) {
        event.preventDefault();
        this.router.navigate(['/irb/committee/schedule'], { queryParams: { 'scheduleId': scheduleId } });
    }
    expandAreaOfResearch() {
        this.isAddClicked = !this.isAddClicked;
        this.emptyAreaOfResearch = false;
        this.duplicateAreaOfResearch = false;
    }
    getHomeUnitList() {
        this.unitSearchString = this.result.committee.homeUnitName;
        this.result.committee.homeUnitNumber = null;
        this.committeeSaveService.loadHomeUnits(this.unitSearchString).subscribe(
            (data: any) => {
              this.homeUnitSearchResult = data.homeUnits;
            });
    }
    selectedHomeUnit(homeUnitName, homeUnitNumber) {
            this.result.committee.homeUnitNumber = homeUnitNumber;
            this.result.committee.homeUnitName = homeUnitName;
            this.isHomeUnitSearch = false;
      }
      getAreaResearchList() {
        this.areaInput.researchAreaCode = null;
        this.researchSearchString = this.areaInput.researchAreaDescription;
        this.committeeSaveService.loadResearchAreas(this.researchSearchString).subscribe(
            (data: any) => {
              this.areaResearchSearchResult = data.researchAreas;
            });
      }
      selectedResearchArea(researchAreaDescription, researchAreaCode) {
                this.areaInput.researchAreaCode = researchAreaCode;
                this.areaInput.researchAreaDescription = researchAreaDescription;
      }
}
