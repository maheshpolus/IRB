import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import {ScheduleService} from '../../schedule.service';

@Component({
  selector: 'app-schedule-minutes',
  templateUrl: './schedule-minutes.component.html',
  styleUrls: ['./schedule-minutes.component.css']
})
export class ScheduleMinutesComponent implements OnInit {

  tabSelected =  'MEETING_COMMENTS';
  scheduleId = null;
  scheduleMinutes = [];
  
  constructor(private scheduleService: ScheduleService, private activatedRoute: ActivatedRoute) {
    this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get( 'scheduleId' );
  }

  ngOnInit() {
    this.getMinutes();
  }

  getMinutes() {
    const obj = {acType : null, scheduleId: this.scheduleId};
    this.scheduleService.loadScheduleMeetingComments(obj).subscribe( data => {
      const result = data;
      this.scheduleMinutes = result.scheduleMinutes;
    });
  }

}
