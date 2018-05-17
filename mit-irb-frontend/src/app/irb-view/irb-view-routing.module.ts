import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';

const routes: Routes = [
                        {path: '', component: IrbHeaderDetailComponent, children: [
                          {path: '', redirectTo: 'IrbOverviewComponent', pathMatch: 'full'},
                          {path: 'irbOverview', component: IrbOverviewComponent},
                          {path: 'irbQuestionaire', component: IrbQuestionaireComponent},
                          {path: 'irbAttatchments', component: IrbAttachmentsComponent},
                          {path: 'irbHistory', component: IrbHistoryComponent}
                          ]}
                        ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IrbViewRoutingModule { }
