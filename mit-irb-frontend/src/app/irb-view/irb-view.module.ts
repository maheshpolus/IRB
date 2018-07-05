import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxSpinnerModule } from 'ngx-spinner';

import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';
import { FormsModule } from '@angular/forms';
import { AppModule } from '../app.module';
import { AppCommonModule } from '../common/common/common.module';

import { IrbViewRoutingModule } from './irb-view-routing.module';

@NgModule({
  imports: [
    CommonModule,
    IrbViewRoutingModule,
    NgxSpinnerModule,
    FormsModule,
    AppCommonModule
  ],
  declarations: [IrbOverviewComponent, IrbHeaderDetailComponent, IrbQuestionaireComponent, IrbAttachmentsComponent, IrbHistoryComponent]
})
export class IrbViewModule {}
