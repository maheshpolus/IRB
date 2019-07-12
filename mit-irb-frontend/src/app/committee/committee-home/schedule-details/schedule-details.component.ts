import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { DatePipe } from '@angular/common';
import { ISubscription } from 'rxjs/Subscription';

import { CommitteCreateEditService } from '../../committee-create-edit.service';
import { CommitteeSaveService } from '../../committee-save.service';
import { CommitteeConfigurationService } from '../../../common/service/committee-configuration.service';

@Component({
  selector: 'app-schedule-details',
  templateUrl: './schedule-details.component.html',
  styleUrls: ['./schedule-details.component.css']
})
export class ScheduleDetailsComponent implements OnInit, OnDestroy {

  committeeId = null;
  committee: any = {};
  committeeSchedules = [];
  scheduleStatus = [];
  showAllClicked = false;


  editSchedule = [];
  scheduleTime = null;
  listDate = null;
  scheduleId = null;
  previousEditscheduleId = null;
  listStatus =  null;
  listPlace = null;
  listTime = null;
  isScheduleFieldsFilled = true;
  sendScheduleRequestData: any = {};

  private $subscription: ISubscription;


  constructor(private _activatedRoute: ActivatedRoute,
    public committeCreateService: CommitteCreateEditService,
    public committeeSaveService: CommitteeSaveService,
    private committeeConfigurationService: CommitteeConfigurationService,
    private _spinner: NgxSpinnerService,
    private datePipe: DatePipe) { }

  ngOnInit() {
      this.$subscription = this.committeeConfigurationService.currentCommitteeData.subscribe(committee => {
      const data: any = committee;
      this.committee = data.committee;
  });
    this._activatedRoute.queryParams.subscribe(params => {
      this.committeeId = params['id'];
      this.loadScheduleList('F');
    });
  }

  ngOnDestroy() {
    if (this.$subscription) {
      this.$subscription.unsubscribe();
    }
  }
  loadScheduleList(acType) {
    const reqstObj = {committeeId: this.committeeId, acType: acType};
    this._spinner.show();
    this.committeCreateService.loadScheduleDetailsById( reqstObj ).subscribe(
      data => {
                this._spinner.hide();
                const result: any = data;
                if (result.committee != null) {
                   // this.committee = result.committee;
                    this.committeeSchedules = result.committee.committeeSchedules || [];
                    this.scheduleStatus = result.scheduleStatus;
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
      this.committeeSchedules = temp.committee.committeeSchedules;
    //  this.result.message = temp.message;
    //  this.result.status = temp.status;
    //  if (this.result.status === false) {
    //      document.getElementById('skippedSchedulesButton').click();
    //  }
   //  this.committeeConfigurationService.changeCommmitteeData(this.result);
  });
// this.initialLoadChild();
//  this.isScheduleListItemEditMode = false;
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
  this.scheduleStatus.forEach((value, index) => {
      if (value.description === scheduleObject.scheduleStatus.description) {
          value.updateTimestamp = new Date();
          value.updateUser = localStorage.getItem('currentUser');
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

}
