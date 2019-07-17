import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { DatePipe } from '@angular/common';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

import { CommitteCreateEditService } from '../../committee-create-edit.service';
import { CommitteeSaveService } from '../../committee-save.service';

declare var $: any;
@Component({
  selector: 'app-schedule-details',
  templateUrl: './schedule-details.component.html',
  styleUrls: ['./schedule-details.component.css']
})
export class ScheduleDetailsComponent implements OnInit {

  committeeId = null;
  committee: any = {};
  committeeSchedules = [];
  scheduleStatus = [];
  showAllClicked = false;
  result: any;


  editSchedule = [];
  scheduleTime = null;
  listDate = null;
  scheduleId = null;
  previousEditscheduleId = null;
  listStatus = null;
  listPlace = null;
  listTime = null;
  isScheduleFieldsFilled = true;
  isTodelete = false;
  showGenerateSchedule = false;
  sendScheduleRequestData: any = {};
  today: any;
  optionDay = 'XDAY';
  displayTime: any = {};
  isDatePrevious = true;
  isMandatoryFilled = true;
  isStartDateBeforeToday = false;
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
  yearOption = 'XDAY';
  scheduleValidationMessage: string;
  filterStartDate: string;
  conflictDates: any = [];
  isConflictDates = false;
  filerEndDate: string;

  // private $subscription: ISubscription;


  constructor(private _activatedRoute: ActivatedRoute,
    public committeCreateService: CommitteCreateEditService,
    public committeeSaveService: CommitteeSaveService,
    private _spinner: NgxSpinnerService,
    private datePipe: DatePipe,
    public toastr: ToastsManager,
    public router: Router) { }

  ngOnInit() {
    //     this.$subscription = this.committeeConfigurationService.currentCommitteeData.subscribe(committee => {
    //     const data: any = committee;
    //     this.committee = data.committee;
    // });
    this._activatedRoute.queryParams.subscribe(params => {
      this.committeeId = params['id'];
      this.loadScheduleList('F');
    });
  }

  // ngOnDestroy() {
  //   if (this.$subscription) {
  //     this.$subscription.unsubscribe();
  //   }
  // }
  loadScheduleList(acType) {
    const reqstObj = { committeeId: this.committeeId, acType: acType };
    this._spinner.show();
    this.committeCreateService.loadScheduleDetailsById(reqstObj).subscribe(
      data => {
        this._spinner.hide();
        this.result = data;
        if (this.result.committee != null) {
          this.committee = this.result.committee;
          this.committeeSchedules = this.result.committee.committeeSchedules || [];
          this.scheduleStatus = this.result.scheduleStatus;
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

        }
      });
  }


  showAll() {
    this.showAllClicked = !this.showAllClicked;
    if (this.showAllClicked === true) {
      this.loadScheduleList(null);
    } else {
      this.loadScheduleList('F');
    }
  }

  editScheduleData(e, date, status, place, time, i, scheduleId) {
    e.preventDefault();

    // this.isScheduleEditWarningModalOpen = false;
    //   this.isScheduleListItemEditMode = true;
    this.scheduleTime = new Date(time);
    this.editSchedule.fill(false);
    this.editSchedule[i] = true;
    this.listDate = date;
    this.previousEditscheduleId = scheduleId;
    this.listStatus = status;
    this.listPlace = place;
    this.listTime = time;
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
      scheduleObject.scheduleStatus.updateUser = localStorage.getItem('currentUser');
      this.scheduleStatus.forEach((value, index) => {
        if (value.description === scheduleObject.scheduleStatus.description) {
          value.updateTimestamp = new Date();
          value.updateUser = localStorage.getItem('currentUser');
          scheduleObject.scheduleStatusCode = value.scheduleStatusCode;
          scheduleObject.scheduleStatus = value;
          this.committeeSchedules[i].scheduleStatus.description = value.description;
        }
      });
      this.sendScheduleRequestData.committeeSchedule = scheduleObject;
      this.sendScheduleRequestData.committeeId = this.committeeId;
      this.sendScheduleRequestData.advSubmissionDaysReq = this.committee.advSubmissionDaysReq;
      this.committeeSaveService.updateScheduleData(this.sendScheduleRequestData).subscribe(
        data => {
          let temp: any;
          temp = data;
          if (temp.status === true) {
            this.toastr.success('Meeting updated successfully', null, { toastLife: 2000 });
            this.committeeSchedules[i] = temp.committeeSchedule;
            //  this.result.message = temp.message;
            //  this.result.status = temp.status;
            //  if (this.result.status === false) {
            //      document.getElementById('skippedSchedulesButton').click();
            //  }
            //  this.committeeConfigurationService.changeCommmitteeData(this.result);
          } else {
            this.toastr.error(temp.message, null, { toastLife: 2000 });
          }
        });
        if (this.showAllClicked === true) {
          this.loadScheduleList(null);
        } else {
          this.loadScheduleList('F');
        }
      //  this.isScheduleListItemEditMode = false;
    }
  }


  showSchedulePopUp() {
    // if (this.editDetails === true) {
    //     this.isEditDetailsModalOpen = true;
    // }
    this.showGenerateSchedule = true;
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

  get selectedOptions() {
    return this.dayOptions
      .filter(option => option.checked)
      .map(option => option.value);
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
    this.sendScheduleRequestData.currentUser = localStorage.getItem('currentUser');
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
      this.committeeSaveService.saveScheduleData(this.sendScheduleRequestData).subscribe(data => {
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
        this.committeeSchedules = this.result.committee.committeeSchedules;
        //   this.committeeConfigurationService.changeCommmitteeData(this.result);
      });
      //  this.initialLoadChild();
    }
  }

  closeConflictDateModal() {
    if (this.isConflictDates === true) {
        this.isConflictDates = false;
    }
}


  showDeleteModal(e, scheduleObject, committeeId, scheduledDate) {
    e.preventDefault();
    //  this.alertMsg = '';
    // this.isScheduleEditWarningModalOpen = false;
    //  this.scheduledDate = scheduledDate;
    this.scheduleId = scheduleObject.scheduleId;
    this.sendScheduleRequestData = {};
    scheduleObject.viewTime = {};
    scheduleObject.viewTime.time = this.datePipe.transform(this.scheduleTime, 'hh:mm');
    scheduleObject.viewTime.meridiem = this.datePipe.transform(this.scheduleTime, 'aa');
    scheduleObject.scheduleStatus.updateTimestamp = new Date();
    scheduleObject.scheduleStatus.updateUser = localStorage.getItem('currentUser');
    // this.scheduleStatus.forEach((value, index) => {
    //     if (value.description === scheduleObject.scheduleStatus.description) {
    //         value.updateTimestamp = new Date();
    //         value.updateUser = localStorage.getItem('currentUser');
    //         scheduleObject.scheduleStatusCode = value.scheduleStatusCode;
    //         scheduleObject.scheduleStatus = value;
    //         // this.result.committee.committeeSchedules[i].scheduleStatus.description = value.description;
    //     }
    // });
    this.sendScheduleRequestData.committeeSchedule = scheduleObject;
    this.committeeId = committeeId;
    if (this.isTodelete === false) {
      this.isTodelete = true;
    }
  }

  deleteScheduleData() {
    // this.sendScheduleRequestData = {};
    this.sendScheduleRequestData.scheduleId = this.scheduleId;
    this.sendScheduleRequestData.committeeId = this.committeeId;
    if (this.isTodelete === true) {
      this.isTodelete = false;
    }
    this.committeeSaveService.deleteScheduleData(this.sendScheduleRequestData).subscribe(
      data => {
        let temp: any;
        temp = data || [];
        this.committeeSchedules = temp.committee.committeeSchedules;
      });
  }

  loadSchedules(event: any, scheduleId) {debugger
    event.preventDefault();
    this.router.navigate(['/irb/committee/schedule'], { queryParams: { 'scheduleId': scheduleId,
                                          'committeeId': this.committee.committeeId } });
}

  // cancelDelete() {
  //    this.clear();
  // }

}
