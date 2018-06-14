import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { IrbComponent } from './irb/irb.component';
import { Routes, RouterModule, PreloadAllModules } from '@angular/router';
import { ExpandedViewComponent } from './expanded-view/expanded-view.component';
import { AuthGuard } from './common/service/auth-guard.service';
import { DashboardResolverService } from './common/service/dashboard-resolver.service';
import { ExemptQuestionaireComponent } from './exempt-questionaire/exempt-questionaire.component';

/**remove the comment on can Activate once login service is completed
 * login service - should set a vatriable 'ActivatedUser' in browser session
 */
const appRoutes = [
     { path: '', redirectTo: 'irb/dashboard', pathMatch: 'full'},
   // { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    {
        path: 'irb', component: IrbComponent, // canActivate: [AuthGuard],
        children: [
            { path: 'dashboard', component: DashboardComponent,
                    resolve: { irb: DashboardResolverService }
            },
            { path: 'irb-view', loadChildren: 'app/irb-view/irb-view.module#IrbViewModule' },
            { path: 'expanded-view', component: ExpandedViewComponent },
            { path: 'exempt-questionaire', component: ExemptQuestionaireComponent,
                resolve: { irb: DashboardResolverService }
            }
        ]
    }
];
@NgModule( {
    imports: [
        CommonModule,
        RouterModule.forRoot( appRoutes )
    ],
    exports: [RouterModule]
} )
export class AppRoutingModule { }
