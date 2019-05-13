import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
import { IrbCreateAttachmentComponent } from './irb-create-attachment/irb-create-attachment.component';
import { IrbCreateHistroyComponent } from './irb-create-histroy/irb-create-histroy.component';
import { IrbActionsComponent } from './irb-actions/irb-actions.component';
import { IrbProtocolComponent } from './irb-protocol/irb-protocol.component';
import { IrbQuestionnaireListComponent } from './irb-questionnaire-list/irb-questionnaire-list.component';
import { SubmissionDetailsComponent } from './irb-create-histroy/submission-details/submission-details.component';
import { IrbSummaryDetailsComponent } from './irb-summary-details/irb-summary-details.component';

const routes: Routes = [
  {
    path: '', component: IrbCreateHeaderComponent,
    children: [
      { path: '', redirectTo: 'irbHome', pathMatch: 'full' },
      { path: '', component: IrbEditComponent },
      { path: 'irbHome', component: IrbEditComponent, resolve: { irb: DashboardResolverService } },
      { path: 'irbCreateAttachment', component: IrbCreateAttachmentComponent, resolve: { irb: DashboardResolverService } },
      { path: 'irbHistory', component: IrbCreateHistroyComponent },
      { path: 'irbHistory/submission-detail', component: SubmissionDetailsComponent },
      { path: 'irbActions', component: IrbActionsComponent, resolve: { irb: DashboardResolverService } },
      { path: 'irbSummaryDetails', component: IrbSummaryDetailsComponent, resolve: { irb: DashboardResolverService } },
      { path: 'irbProtocol', component: IrbProtocolComponent,  resolve: { irb: DashboardResolverService } },
      { path: 'irbQuestionnaireList', component: IrbQuestionnaireListComponent },
      { path: 'irbQuestionnaireView', loadChildren: '../../app/questionnaire-view/view.module#ViewModule' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IrbCreateRoutingModule { }




