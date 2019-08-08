import { NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { IrbComponent } from './irb/irb.component';
import { RouterModule, PreloadAllModules } from '@angular/router';
import { ExpandedViewComponent } from './expanded-view/expanded-view.component';
import { DashboardResolverService } from './common/service/dashboard-resolver.service';
import { ExemptQuestionaireComponent } from './exempt-questionaire/exempt-questionaire.component';
import { LogoutComponent } from './login/logout.component';
import { CommitteeScheduleListComponent } from './committee-schedule-list/committee-schedule-list.component';

const appRoutes = [
   //{ path: '', redirectTo: 'irb/dashboard', pathMatch: 'full'},
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'logout', component: LogoutComponent },
    {
        path: 'irb', component: IrbComponent, // canActivate: [AuthGuard],
        children: [
            { path: 'dashboard', component: DashboardComponent,
                    resolve: { irb: DashboardResolverService }
            },
            { path: 'code-table', loadChildren: 'app/codetable/codetable.module#CodetableModule' },
            { path: 'admin-dashbord', loadChildren: 'app/admin-dashboard/admin-dashboard.module#AdminDashboardModule',  
            resolve: { irb: DashboardResolverService } },
            { path: 'training-maintenance', loadChildren: 'app/person-training/person-training.module#PersonTrainingModule' },
            { path: 'role-maintainance', loadChildren: 'app/role-maintainance/role-maintainance.module#RoleMaintainanceModule' },
            { path: 'irb-create', loadChildren: 'app/irb-create/irb-create.module#IrbCreateModule' },
            { path: 'configure-questionnaire', loadChildren: 'app/questionnaire-create/create.module#CreateModule' },
            { path: 'irb-view', loadChildren: 'app/irb-view/irb-view.module#IrbViewModule' },
            { path: 'committee', loadChildren: 'app/committee/committee.module#CommitteeModule' },
            { path: 'expanded-view', component: ExpandedViewComponent },
            { path: 'exempt-questionaire', component: ExemptQuestionaireComponent,
                    resolve: { irb: DashboardResolverService }
            },
            { path: 'committee-schedule-list', component: CommitteeScheduleListComponent
    }
        ]
    }
];
@NgModule( {
    imports: [
        CommonModule,
        RouterModule.forRoot( appRoutes , {preloadingStrategy: PreloadAllModules})
    ],
    exports: [RouterModule],
} )
export class AppRoutingModule { }
