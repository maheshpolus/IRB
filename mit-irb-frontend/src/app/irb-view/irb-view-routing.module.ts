import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbCreateAttachmentComponent } from '../irb-create/irb-create-attachment/irb-create-attachment.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
import { SubmissionDetailsComponent } from './irb-history/submission-details/submission-details.component';
import { IrbActionsComponent } from '../irb-create/irb-actions/irb-actions.component';
import { IrbSummaryDetailsComponent } from '../irb-create/irb-summary-details/irb-summary-details.component';
import { IrbSubmissionDetailComponent } from './irb-submission-detail/irb-submission-detail.component';
import { IrbPermissionComponent } from '../irb-create/irb-permission/irb-permission.component';
import { IrbQuestionnaireListComponent } from '../irb-create/irb-questionnaire-list/irb-questionnaire-list.component';
import { IrbProtocolComponent } from './irb-protocol/irb-protocol.component';

const routes: Routes = [
                        {path: '', component: IrbHeaderDetailComponent, resolve: { irb: DashboardResolverService },
                        children: [
                          {path: '', redirectTo: 'IrbOverviewComponent', pathMatch: 'full'},
                          {path: 'irbOverview', component: IrbOverviewComponent},
                          {path: 'irbQuestionaire', component: IrbQuestionaireComponent},
                          // {path: 'irbAttatchments', component: IrbAttachmentsComponent},
                          { path: 'irbAttatchments', component: IrbCreateAttachmentComponent,
                          resolve: { irb: DashboardResolverService } },
                          {path: 'irbHistory', component: IrbHistoryComponent},
                          {path: 'irbProtocol', component: IrbProtocolComponent},
                          {path: 'irbHistory/submission-detail', component: SubmissionDetailsComponent},
                          { path: 'irbPermission', component: IrbPermissionComponent , resolve: { irb: DashboardResolverService }},
                          {path: 'irbActions', component: IrbActionsComponent, resolve: { irb: DashboardResolverService }},
                          { path: 'irbSummaryDetails', component: IrbSummaryDetailsComponent, resolve: { irb: DashboardResolverService } },
                          {path: 'irbSubmissionDetails', component: IrbSubmissionDetailComponent,
                          resolve: { irb: DashboardResolverService }},
                          { path: 'irbQuestionnaireList', component: IrbQuestionnaireListComponent,
                    children: [{ path: 'irbQuestionnaireView', loadChildren: '../../app/questionnaire-view/view.module#ViewModule' }] },
                          { path: 'irbQuestionnaireView', loadChildren: '../../app/questionnaire-view/view.module#ViewModule' }
                          ]}
                        ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IrbViewRoutingModule { }
