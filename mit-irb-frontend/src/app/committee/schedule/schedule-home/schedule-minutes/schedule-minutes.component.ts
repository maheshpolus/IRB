import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
  submittedProtocols = [];
  protocolContingencies = [];
  addNewMinutes = false;
  newCommitteeScheduleMinute: any = {};
  selectedProtocol: any = null;
  result: any;
  selectedProtocolIndex = null;
  isToDelete = false;
  deleteMinuteObj: any = {};

  constructor(private scheduleService: ScheduleService, private activatedRoute: ActivatedRoute) {
    this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get('scheduleId');
  }

  ngOnInit() {
    this.getMinutes();
  }

  getMinutes() {
    const obj = { acType: this.tabSelected === 'all' ? 'A' : null, scheduleId: this.scheduleId };
    this.scheduleService.loadScheduleMeetingComments(obj).subscribe(data => {
      this.result = data;
      this.scheduleMinutes = this.result.scheduleMinutes;
      this.entryType = this.result.minuteEntrytypes != null ? this.result.minuteEntrytypes : [];
      this.newCommitteeScheduleMinute = this.result.newCommitteeScheduleMinute != null ? this.result.newCommitteeScheduleMinute : {};
    });
  }

  getSubmittedProtocols(protocolNumber, submissionId) {
    const obj = { scheduleId: this.scheduleId, protocolNumber: protocolNumber, submissionId: submissionId };
    this.scheduleService.loadScheduleProtocolComments(obj).subscribe(data => {
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


  selectContigencyItem( e, contigencyItem ) {
    this.newCommitteeScheduleMinute.protocolContingencyCode = contigencyItem.protocolContingencyCode;
    this.newCommitteeScheduleMinute.minuteEntry = contigencyItem.description;
}

  saveMinutes() {
    this.result.newCommitteeScheduleMinute = this.newCommitteeScheduleMinute;
    this.scheduleService.saveMinuteData(this.result).subscribe(data => {
      const result: any = data;
      this.newCommitteeScheduleMinute = {};
      this.scheduleMinutes = result.scheduleMinutes;
    });
  }


  saveProtocolMinutes () {
    this.newCommitteeScheduleMinute.minuteEntryTypeCode = 3;
    this.newCommitteeScheduleMinute.protocolNumber = this.selectedProtocol.protocolNumber;
    this.newCommitteeScheduleMinute.protocolId = this.selectedProtocol.protocolId;
    this.newCommitteeScheduleMinute.submissionId = this.selectedProtocol.submissionId;
    this.newCommitteeScheduleMinute.reviewerId = this.selectedProtocol.reviewerId;
    this.saveMinutes();
  }

  editMinuteItem(minute, i) {

  }

  selectProtocol(protocol, index) {
    this.selectedProtocol = protocol;
    this.selectedProtocolIndex = index;
    const obj = {
      scheduleId: this.scheduleId, protocolNumber: protocol.protocolNumber,
      submissionId: protocol.submissionId
    };
    this.scheduleService.loadScheduleProtocolComments(obj).subscribe(data => {
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
    if ( this.isToDelete === true ) {
        this.isToDelete = false;
    }
    this.scheduleService.deleteMinuteData( deleteRequestData ).subscribe( data => {
      const result: any = data;
      this.scheduleMinutes = result.scheduleMinutes != null ? result.scheduleMinutes : [];
    } );
}
}
