import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
import { SubmissionDetailsComponent } from './irb-history/submission-details/submission-details.component';
import { IrbActionsComponent } from '../irb-create/irb-actions/irb-actions.component';

const routes: Routes = [
                        {path: '', component: IrbHeaderDetailComponent, resolve: { irb: DashboardResolverService },
                        children: [
                          {path: '', redirectTo: 'IrbOverviewComponent', pathMatch: 'full'},
                          {path: 'irbOverview', component: IrbOverviewComponent},
                          {path: 'irbQuestionaire', component: IrbQuestionaireComponent},
                          {path: 'irbAttatchments', component: IrbAttachmentsComponent},
                          {path: 'irbHistory', component: IrbHistoryComponent},
                          {path: 'irbHistory/submission-detail', component: SubmissionDetailsComponent},
                          {path: 'irbActions', component: IrbActionsComponent},
                          ]}
                        ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IrbViewRoutingModule { }
