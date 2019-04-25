import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { FileDropModule } from 'ngx-file-drop';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { PersonTrainingRoutingModule } from './person-training-routing.module';
import { AppCommonModule } from '../common/common/common.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TrainingDetailsComponent } from './training-details/training-details.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    AppCommonModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    PersonTrainingRoutingModule,
    NgxSpinnerModule,
    FileDropModule,
    NgbModule
  ],
  declarations: [DashboardComponent, TrainingDetailsComponent]
})
export class PersonTrainingModule { }
