import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { Ng2CompleterModule } from 'ng2-completer';

import { IrbOverviewComponent } from './irb-overview/irb-overview.component';
import { IrbHeaderDetailComponent } from './irb-header-detail/irb-header-detail.component';
import { IrbQuestionaireComponent } from './irb-questionaire/irb-questionaire.component';
import { IrbAttachmentsComponent } from './irb-attachments/irb-attachments.component';
import { IrbHistoryComponent } from './irb-history/irb-history.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppCommonModule } from '../common/common/common.module';
import { IrbCreateModule } from '../irb-create/irb-create.module';

import { IrbViewRoutingModule } from './irb-view-routing.module';
import { SubmissionDetailsComponent } from './irb-history/submission-details/submission-details.component';
import { IrbSubmissionDetailComponent } from './irb-submission-detail/irb-submission-detail.component';

@NgModule({
  imports: [
    CommonModule,
    IrbViewRoutingModule,
    NgxSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    AppCommonModule,
    IrbCreateModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    Ng2CompleterModule,
    NgbModule.forRoot()
  ],
  declarations: [IrbOverviewComponent, IrbHeaderDetailComponent,
    IrbQuestionaireComponent, IrbAttachmentsComponent, IrbHistoryComponent, SubmissionDetailsComponent, IrbSubmissionDetailComponent]
})
export class IrbViewModule {}
