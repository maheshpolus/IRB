import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule , ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';

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

import { AuthGuard } from './common/service/auth-guard.service';
import { ElasticService } from './common/service/elastic.service';
import { DashboardService } from './dashboard/dashboard.service';
import { ExpandedViewService } from './expanded-view/expanded-view.service';
import { LoginService } from './login/login.service';
import { DashboardResolverService } from './common/service/dashboard-resolver.service';
import { IrbViewService } from './irb-view/irb-view.service';

import { AppRoutingModule } from './app-routing.module';
import { AppCommonModule } from './common/common/common.module';

import { ExemptCardComponent } from './dashboard/dashboard-list/exempt-card/exempt-card.component';
import { PiElasticService } from './common/service/pi-elastic.service';
import { Ng2CompleterModule } from "ng2-completer";

@NgModule( {
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
        ExemptCardComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        AppCommonModule,
        Ng2CompleterModule 
    ],
    providers: [AuthGuard, DashboardService,
                { provide: LocationStrategy, useClass: HashLocationStrategy },
                LoginService, DashboardResolverService, ElasticService, IrbViewService, ExpandedViewService, PiElasticService],
    bootstrap: [AppComponent]
} )
export class AppModule { }
