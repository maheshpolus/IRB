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
import { IrbViewModule } from './irb-view/irb-view.module';
import { AppCommonModule } from './common/common/common.module';

import { OrderByPipe } from './common/directives/order-by.pipe';
import { ExemptCardComponent } from './dashboard/dashboard-list/exempt-card/exempt-card.component';

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
        AppCommonModule
    ],
    providers: [AuthGuard, DashboardService,
                { provide: LocationStrategy, useClass: HashLocationStrategy },
                LoginService, DashboardResolverService, ElasticService, IrbViewService, ExpandedViewService],
    bootstrap: [AppComponent]
} )
export class AppModule { }
