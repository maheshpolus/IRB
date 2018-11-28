import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-schedule-card',
  templateUrl: './schedule-card.component.html',
  styleUrls: ['./schedule-card.component.css']
})
export class ScheduleCardComponent implements OnInit {

  @Input() irbList: any = [];
  currentDate = new Date();
  constructor(private _router: Router) { }

  ngOnInit() { }

  openIrb(scheduleId) {
    this._router.navigate(['/irb/committee/schedule'], { queryParams: { 'scheduleId': scheduleId } });
  }
}
