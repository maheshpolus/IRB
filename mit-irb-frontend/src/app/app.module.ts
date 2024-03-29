import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { enableProdMode } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { Ng2CompleterModule } from 'ng2-completer';
import { NgxSpinnerModule } from 'ngx-spinner';
import { FileDropModule } from 'ngx-file-drop';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastModule } from 'ng2-toastr';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { AppCommonModule } from './common/common/common.module';
import { NgIdleKeepaliveModule } from "@ng-idle/keepalive";

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HeaderComponent } from './common/header/header.component';
import { FooterComponent } from './common/footer/footer.component';
import { SnapshotsComponent } from './dashboard/snapshots/snapshots.component';
import { DashboardListComponent } from './dashboard/dashboard-list/dashboard-list.component';
import { CardDetailsComponent } from './dashboard/dashboard-list/card-details/card-details.component';
import { IrbComponent } from './irb/irb.component';
import { ExpandedViewComponent } from './expanded-view/expanded-view.component';
import { ExemptQuestionaireComponent } from './exempt-questionaire/exempt-questionaire.component';
import { ExemptCardComponent } from './dashboard/dashboard-list/exempt-card/exempt-card.component';
import { LogoutComponent } from "./login/logout.component";

import { AuthGuard } from './common/service/auth-guard.service';
import { ElasticService } from './common/service/elastic.service';
import { DashboardService } from './dashboard/dashboard.service';
import { ExpandedViewService } from './expanded-view/expanded-view.service';
import { LoginService } from './login/login.service';
import { DashboardResolverService } from './common/service/dashboard-resolver.service';
import { IrbViewService } from './irb-view/irb-view.service';
import { PiElasticService } from './common/service/pi-elastic.service';
import { SharedDataService } from './common/service/shared-data.service';
import { HeaderService } from './common/service/header.service';
import { FilterPipe } from './common/directives/filter.pipe';
import { KeyPressEvent } from './common/directives/keyPressEvent.component';
import { CommitteeCardComponent } from './dashboard/dashboard-list/committee-card/committee-card.component';
import { ScheduleCardComponent } from './dashboard/dashboard-list/schedule-card/schedule-card.component';
import { FundingSourceModalComponent } from './common/funding-source-modal/funding-source-modal.component';
import { FundingSourceModalService } from './common/funding-source-modal/funding-source-modal.service';
import { AssignModalServiceService } from './common/assign-modal/assign-modal.service';
import { PersonTrainingService } from './person-training/person-training.service';
import { AssignModalComponent } from './common/assign-modal/assign-modal.component';
import { PermissionWarningModalComponent } from './common/permission-warning-modal/permission-warning-modal.component';
import { CommitteeScheduleListComponent } from './committee-schedule-list/committee-schedule-list.component';

enableProdMode();

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        DashboardComponent,
        HeaderComponent,
        FooterComponent,
        SnapshotsComponent,
        DashboardListComponent,
        CardDetailsComponent,
        IrbComponent,
        ExpandedViewComponent,
        ExemptQuestionaireComponent,
        ExemptCardComponent,
        FilterPipe,
        CommitteeCardComponent,
        ScheduleCardComponent,
        LogoutComponent,
        FundingSourceModalComponent,
        AssignModalComponent,
        PermissionWarningModalComponent,
        CommitteeScheduleListComponent

    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        AppCommonModule,
        Ng2CompleterModule,
        NgxSpinnerModule,
        FileDropModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
        BrowserAnimationsModule,
        NgIdleKeepaliveModule.forRoot(),
        ToastModule.forRoot(),
        NgbModule.forRoot()
    ],
    providers: [AuthGuard, DashboardService,
        { provide: LocationStrategy, useClass: HashLocationStrategy },
        LoginService, DashboardResolverService, ElasticService, IrbViewService, ExpandedViewService, PiElasticService,
        SharedDataService, FundingSourceModalService, AssignModalServiceService, PersonTrainingService,
        KeyPressEvent, HeaderService],
        entryComponents: [FundingSourceModalComponent, AssignModalComponent, PermissionWarningModalComponent],
    bootstrap: [AppComponent]
})
export class AppModule { }
