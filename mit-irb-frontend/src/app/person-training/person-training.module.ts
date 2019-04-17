import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PersonTrainingRoutingModule } from './person-training-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { TrainingDetailsComponent } from './training-details/training-details.component';

@NgModule({
  imports: [
    CommonModule,
    PersonTrainingRoutingModule
  ],
  declarations: [DashboardComponent, TrainingDetailsComponent]
})
export class PersonTrainingModule { }
