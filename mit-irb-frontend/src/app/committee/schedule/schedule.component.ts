import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Subscription } from 'rxjs/Subscription';
import {Location} from '@angular/common';

import { ScheduleService } from '../schedule/schedule.service';
import { ScheduleConfigurationService } from './schedule-configuration.service';

@Component( {
    selector: 'app-schedule-component',
    templateUrl: './schedule.component.html'
} )
export class ScheduleComponent implements OnInit, OnDestroy {

    agendaList: any = [];
    minutelist = [];
    modalTitle = '';
    modalPara = '';
    currentTab = 'schedule_home';
    scheduleId = null;
    committeeId = null;
    userDTO = JSON.parse(localStorage.getItem('currentUser'));
    result: any = {};
    public loadScheduleDataSub: Subscription;

    constructor( private scheduleService: ScheduleService,
        private _location: Location,
        private _spinner: NgxSpinnerService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private scheduleConfigurationService: ScheduleConfigurationService ) {
            this.scheduleId = this.activatedRoute.snapshot.queryParamMap.get( 'scheduleId' );
            this.committeeId = this.activatedRoute.snapshot.queryParamMap.get( 'committeeId' );
    }

    ngOnInit() {
        this.currentTab = 'scheduleHome';
        this._spinner.show();
        const params = {
            scheduleId: this.scheduleId,
            committeeId: this.committeeId
    };
        this.loadScheduleDataSub = this.scheduleService.loadScheduleData( params ).
            subscribe( data => {
                this.result = data;
                this.result.committeeSchedule.meetingDate = this.result.committeeSchedule.meetingDate != null ?
                    new Date(this.result.committeeSchedule.meetingDate) : null;
                this._spinner.hide();
                if (this.result !== null) {
                    this.scheduleConfigurationService.changeScheduleData( this.result );
                }
            });

    }
    ngOnDestroy() {
        this.loadScheduleDataSub.unsubscribe();
    }

    showModal(type) {
      if (type === 'agenda') {
        this.modalTitle = 'Agenda';
        this.modalPara = 'List of all generated agendas';
        this.scheduleService.loadAllScheduleAgenda(this.scheduleId).subscribe(
          data => {
            this.minutelist = [];
            this.agendaList = data.agendaList != null ? data.agendaList : [];
          }
        );
      } else {
        this.agendaList = [];
        this.modalTitle = 'Minutes';
        this.modalPara = 'List of all generated minutes';
        this.scheduleService.loadAllScheduleMinutes(this.scheduleId).subscribe(
            data => {
                this.agendaList = [];
                this.minutelist = data.minuteList != null ? data.minuteList : [];
            }
          );
      }
    }

    downloadAgendaAttachment(scheduleAgendaId) {
      this.scheduleService.downloadAgendaAttachment(scheduleAgendaId).subscribe(data => {
        // const a = document.createElement('a');
        // const blob = new Blob ([data], {type: 'application/pdf'});
        // a.href = URL.createObjectURL(blob);
        // a.download = 'Agenda' + '.pdf';
        // document.body.appendChild(a);
        // a.click();
        const a = document.createElement( 'a' );
        const blob = new Blob( [data], { type: data.type} );
        a.href = URL.createObjectURL( blob );
        a.download = 'Agenda.pdf';
        document.body.appendChild(a);
        a.click();
      },
        error => console.log('Error downloading the file.', error),
        () => console.log('OK')
      );
    }

    downloadMinuteAttachment(scheduleMinuteDocId) {
      this.scheduleService.downloadMinuteAttachment(scheduleMinuteDocId).subscribe(data => {
        const a = document.createElement('a');
        const blob = new Blob([data], { type: data.type });
        a.href = URL.createObjectURL(blob);
        a.download = 'Minutes.pdf';
        document.body.appendChild(a);
        a.click();
      },
        error => console.log('Error downloading the file.', error),
        () => console.log('OK')
      );
    }

    onActivate( componentRef ) {
        this.activatedRoute = componentRef;
    }
    show_current_tab( e: any, current_tab ) {
        e.preventDefault();
        this.currentTab = current_tab;
        this.router.navigate( ['irb/committee/schedule/' + this.currentTab],
                { queryParams: { 'scheduleId': this.scheduleId, 'committeeId': this.committeeId } } );
    }
    backClick() {
        this._location.back();
    }
    createAgendaForSchedule() {
        const requestObject = { scheduleId: this.scheduleId, committeeId: this.committeeId, updateUser: this.userDTO.userName };
        this.scheduleService.createAgendaForSchedule(requestObject).subscribe( data => {
            const result: any = data;
            this.result.agendaDetails = result.agendaDetails;
            this.downloadLatestAgenda();
        });
    }
    downloadLatestAgenda() {
        this.scheduleService.downloadLatestAgenda(this.scheduleId).subscribe(data => {
            const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = 'Agenda.pdf';
      document.body.appendChild(a);
      a.click();
      },
        error => console.log('Error downloading the file.', error),
        () => console.log('OK'));
    }

    createMinutesForSchedule() {
      const requestObject = { scheduleId: this.scheduleId, committeeId: this.committeeId, updateUser: this.userDTO.userName };
      this.scheduleService.createMinutesForSchedule(requestObject).subscribe(data => {
        const result: any = data;
        this.result.minuteDetails = result.minuteDetails;
        this.downloadLatestMinutes();
      });
    }

    downloadLatestMinutes() {
      this.scheduleService.downloadLatestMinutes(this.scheduleId).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = 'Minutes.pdf';
      document.body.appendChild(a);
      a.click();
      },
      error => console.log('Error downloading the file.', error),
      () => console.log('OK'));
    }
}
