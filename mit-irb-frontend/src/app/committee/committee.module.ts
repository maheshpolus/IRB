import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Ng2PageScrollModule } from 'ng2-page-scroll';
import { Ng2CompleterModule } from 'ng2-completer';
import { NgxSpinnerModule } from 'ngx-spinner';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { FileDropModule } from 'ngx-file-drop';
import { RouterModule } from '@angular/router';

import { CommitteeHomeComponent } from './committee-home/committee-home.component';
import { CommitteeMembersComponent } from './committee-home/committee-members/committee-members.component';
import { ScheduleHomeComponent } from './schedule/schedule-home/schedule-home.component';
import { MinutesComponent } from './schedule/minutes/minutes.component';
import { CommitteeComponent } from './committee.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { ProtocolSubmittedComponent } from './schedule/schedule-home/protocol-submitted/protocol-submitted.component';
import { ScheduleAttendanceComponent } from './schedule/schedule-home/schedule-attendance/schedule-attendance.component';
import { ScheduleOtherActionsComponent } from './schedule/schedule-home/schedule-other-actions/schedule-other-actions.component';
import { ScheduleAttachmentsComponent } from './schedule/schedule-home/schedule-attachments/schedule-attachments.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ScheduleService } from './schedule/schedule.service';
import { ScheduleOtherActionsService } from './schedule/schedule-home/schedule-other-actions/schedule-other-actions.service';
import { ScheduleHomeService } from './schedule/schedule-home/schedule-home.service';
import { ScheduleAttachmentsService } from './schedule/schedule-home/schedule-attachments/schedule-attachments.service';
import { ScheduleAttendanceService } from './schedule/schedule-home/schedule-attendance/schedule-attendance.service';
import { MinutesService } from './schedule/minutes/minutes.service';
import { ScheduleConfigurationService } from './schedule/schedule-configuration.service';
import { CommitteeConfigurationService } from '../common/service/committee-configuration.service';
import { CommitteeMemberEmployeeElasticService } from '../common/service/committee-members-employees-elastic-search.service';
import { CommitteeMemberNonEmployeeElasticService } from '../common/service/committee-members-nonEmployee-elastic-search.service';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
import { ScheduleDetailsComponent } from './committee-home/schedule-details/schedule-details.component';
import { MemberHomeComponent } from './committee-home/committee-members/member-home/member-home.component';
import { MemberDetailsComponent } from './committee-home/committee-members/member-home/member-details/member-details.component';
import { StatusHistoryComponent } from './committee-home/committee-members/member-home/status-history/status-history.component';
import { AppCommonModule } from '../common/common/common.module';
import { TrainingDetailsComponent } from './committee-home/committee-members/member-home/training-details/training-details.component';
import { ScheduleMinutesComponent } from './schedule/schedule-home/schedule-minutes/schedule-minutes.component';

const routes = [
    {
        path: '', component: CommitteeComponent,
        children: [{ path: '', redirectTo: 'committeeHome', pathMatch: 'full' },
        { path: 'committeeHome', component: CommitteeHomeComponent },
        { path: 'committeeMembers', component: CommitteeMembersComponent},
        { path: 'committeeMembers/memberHome', component: MemberHomeComponent},
        { path: 'scheduleDetails', component: ScheduleDetailsComponent },
        { path: 'committeeMembers', component: CommitteeMembersComponent }]
    },
    { path: 'schedule', component: ScheduleComponent,
    children: [ { path: '', redirectTo: 'scheduleHome', pathMatch: 'full' },
                { path: 'scheduleHome', component: ScheduleHomeComponent },
                { path: 'minutes', component: ScheduleMinutesComponent },
                { path: 'minutes', component: MinutesComponent },
                { path: 'protocolSubmitted', component: ProtocolSubmittedComponent },
                { path: 'attendance', component: ScheduleAttendanceComponent },
                { path: 'otherAction', component: ScheduleOtherActionsComponent },
                { path: 'attachment', component: ScheduleAttachmentsComponent },
            ]
    }
];

@NgModule( {
    imports: [
        CommonModule,
        FormsModule,
        AppCommonModule,
        ReactiveFormsModule,
        Ng2PageScrollModule,
        NgxSpinnerModule,
        Ng2CompleterModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        FileDropModule,
        RouterModule.forChild( routes )
    ],
    declarations: [CommitteeHomeComponent,
        CommitteeMembersComponent,
        ScheduleHomeComponent,
        MinutesComponent,
        CommitteeComponent,
        ScheduleComponent,
        ProtocolSubmittedComponent,
        ScheduleAttendanceComponent,
        ScheduleOtherActionsComponent,
        ScheduleAttachmentsComponent,
        ScheduleDetailsComponent,
         MemberHomeComponent,
        MemberDetailsComponent,
        StatusHistoryComponent,
        TrainingDetailsComponent,
        ScheduleMinutesComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    providers: [CommitteeConfigurationService,
                CommitteeMemberEmployeeElasticService,
                CommitteeMemberNonEmployeeElasticService,
                DatePipe,
                ScheduleService,
                ScheduleConfigurationService,
                ScheduleOtherActionsService,
                ScheduleHomeService,
                ScheduleAttachmentsService,
                ScheduleAttendanceService,
                MinutesService]
} )
export class CommitteeModule { }
