import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { ScheduleService } from '../../schedule.service';

@Component({
  selector: 'app-schedule-minutes',
  templateUrl: './schedule-minutes.component.html',
  styleUrls: ['./schedule-minutes.component.css']
})
export class ScheduleMinutesComponent implements OnInit {

  tabSelected = 'meeting';
  scheduleId = null;
  scheduleMinutes = [];
  // protocolMinutesList = [];
  entryType = [];
  committeeScheduleActItemsList = [];
  submittedProtocols = [];
  protocolContingencies = [];
  addNewMinutes = false;
  newCommitteeScheduleMinute: any = {};
  selectedProtocol: any = null;
  result: any;
  selectedProtocolIndex = null;
  isToDelete = false;
  deleteMinuteObj: any = {};
  editIndex = null;
  userDTO: any;

  constructor(private scheduleService: ScheduleService, private activatedRoute: ActivatedRoute,
    private _spinner: NgxSpinnerService) {
    this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get('scheduleId');
  }

  ngOnInit() {
    this.userDTO = JSON.parse(localStorage.getItem('currentUser'));
    this.getMinutes();
  }

  getMinutes() {
    const obj = { acType: this.tabSelected === 'all' ? 'A' : null, scheduleId: this.scheduleId };
    this._spinner.show();
    this.scheduleService.loadScheduleMeetingComments(obj).subscribe(data => {
      this._spinner.hide();
      this.result = data;
      this.scheduleMinutes = this.result.scheduleMinutes;
      this.committeeScheduleActItemsList = this.result.committeeScheduleActItemsList != null ?
        this.result.committeeScheduleActItemsList : [];
      this.entryType = this.result.minuteEntrytypes != null ? this.result.minuteEntrytypes : [];
      this.newCommitteeScheduleMinute = this.result.newCommitteeScheduleMinute != null ? this.result.newCommitteeScheduleMinute : {};
    });
  }

  getSubmittedProtocols(protocolNumber, submissionId) {
    const obj = { scheduleId: this.scheduleId, protocolNumber: protocolNumber, submissionId: submissionId };
    this._spinner.show();
    this.scheduleService.loadScheduleProtocolComments(obj).subscribe(data => {
      this._spinner.hide();
      const result: any = data;
      this.protocolContingencies = result.protocolContingencies != null ? result.protocolContingencies : [];
      this.submittedProtocols = result.submittedProtocols != null ? result.submittedProtocols : [];
      if (this.submittedProtocols != null && this.submittedProtocols.length > 0) {
        this.selectedProtocol = this.submittedProtocols[0];
        this.selectedProtocolIndex = 0;
        this.selectProtocol(this.selectedProtocol, 0);
      } else {
        this.selectedProtocol = null;
        this.scheduleMinutes = [];
      }
    });
  }

  changeMinuteType(code) {
    const selectedType = this.entryType.filter(x => x.minuteEntryTypecode.toString() === code);
      if (selectedType.length > 0) {
          this.newCommitteeScheduleMinute.minuteEntrytype = this.newCommitteeScheduleMinute.minuteEntrytype != null ?
            this.newCommitteeScheduleMinute.minuteEntrytype : {};
          this.newCommitteeScheduleMinute.minuteEntrytype.description = selectedType[0].description;
          this.newCommitteeScheduleMinute.minuteEntrytype.minuteEntryTypecode = selectedType[0].minuteEntryTypecode;
      } else {
          this.newCommitteeScheduleMinute.minuteEntrytype.description = null;
          this.newCommitteeScheduleMinute.minuteEntrytype.minuteEntryTypecode = null;
      }
  }
  selectOtherBussinessType(commScheduleActItemsId) {
    const selectedType = this.committeeScheduleActItemsList.filter(x => x.commScheduleActItemsId.toString() === commScheduleActItemsId);
    if (selectedType.length > 0) {
      this.newCommitteeScheduleMinute.minuteEntry = selectedType[0].itemDescription;
    } else {
      this.newCommitteeScheduleMinute.minuteEntry = null;
    }
  }

  selectContigencyItem(e, contigencyItem) {
    this.newCommitteeScheduleMinute.protocolContingencyCode = contigencyItem.protocolContingencyCode;
    this.newCommitteeScheduleMinute.minuteEntry = contigencyItem.description;
  }

  saveMinutes() {
    this.result.newCommitteeScheduleMinute = this.newCommitteeScheduleMinute;
    this.result.newCommitteeScheduleMinute.updateTimestamp = new Date();
    this.result.newCommitteeScheduleMinute.updateUser = this.userDTO.userName;
    this._spinner.show();
    this.scheduleService.saveMinuteData(this.result).subscribe(data => {
      this._spinner.hide();
      const result: any = data;
      this.newCommitteeScheduleMinute = {};
      this.scheduleMinutes = result.scheduleMinutes;
    });
  }


  saveProtocolMinutes() {
    this.newCommitteeScheduleMinute.minuteEntryTypeCode = 3;
    this.newCommitteeScheduleMinute.protocolNumber = this.selectedProtocol.protocolNumber;
    this.newCommitteeScheduleMinute.protocolId = this.selectedProtocol.protocolId;
    this.newCommitteeScheduleMinute.submissionId = this.selectedProtocol.submissionId;
    this.newCommitteeScheduleMinute.reviewerId = this.selectedProtocol.reviewerId;
    this.saveMinutes();
  }

  editMinuteItem(minute, index) {
    this.addNewMinutes = true;
    this.editIndex = index;
    this.newCommitteeScheduleMinute = minute;
  }

  updateMinuteItem() {
    this.result.newCommitteeScheduleMinute = this.newCommitteeScheduleMinute;
    this.result.scheduleId = this.scheduleId;
    this.result.newCommitteeScheduleMinute.updateTimestamp = new Date();
    this.result.newCommitteeScheduleMinute.updateUser = this.userDTO.userName;
    this._spinner.show();
    this.scheduleService.updateMinuteData(this.result).subscribe(data => {
      this._spinner.hide();
      const result: any = data || [];
      this.scheduleMinutes[this.editIndex] = result.newCommitteeScheduleMinute;
      this.newCommitteeScheduleMinute = {};
      this.addNewMinutes = false;
      this.editIndex = null;
    });
  }

  selectProtocol(protocol, index) {
    this.selectedProtocol = protocol;
    this.selectedProtocolIndex = index;
    const obj = {
      scheduleId: this.scheduleId, protocolNumber: protocol.protocolNumber,
      submissionId: protocol.submissionId
    };
    this._spinner.show();
    this.scheduleService.loadScheduleProtocolComments(obj).subscribe(data => {
      this._spinner.hide();
      const result: any = data;
      this.scheduleMinutes = result.scheduleMinutes != null ? result.scheduleMinutes : [];
    });
  }

  showDeleteModal(minuteItem) {
    this.isToDelete = true;
    this.deleteMinuteObj = minuteItem;
  }

  deleteMinuteItem() {
    const deleteRequestData: any = {};
    deleteRequestData.scheduleId = this.scheduleId;
    deleteRequestData.commScheduleMinuteId = this.deleteMinuteObj.commScheduleMinutesId;
    deleteRequestData.protocolNumber = this.deleteMinuteObj.protocolNumber;
    deleteRequestData.protocolId = this.deleteMinuteObj.protocolId;
    deleteRequestData.submissionId = this.deleteMinuteObj.submissionId;
    if (this.isToDelete === true) {
      this.isToDelete = false;
    }
    this._spinner.show();
    this.scheduleService.deleteMinuteData(deleteRequestData).subscribe(data => {
      this._spinner.hide();
      const result: any = data;
      this.scheduleMinutes = result.scheduleMinutes != null ? result.scheduleMinutes : [];
    });
  }
}
