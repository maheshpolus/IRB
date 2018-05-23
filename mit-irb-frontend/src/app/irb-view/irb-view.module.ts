import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxSpinnerModule } from 'ngx-spinner';

import { IrbViewRoutingModule } from './irb-view-routing.module';
import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';

@NgModule({
  imports: [
    CommonModule,
    IrbViewRoutingModule,
    NgxSpinnerModule
  ],
  declarations: [IrbOverviewComponent, IrbHeaderDetailComponent, IrbQuestionaireComponent, IrbAttachmentsComponent, IrbHistoryComponent]
})
export class IrbViewModule { }
