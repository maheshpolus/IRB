import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { TrainingDetailsComponent } from './training-details/training-details.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';

const routes: Routes = [
  { path: '', component: DashboardComponent, resolve: { irb: DashboardResolverService } },
  { path: 'person-detail', component: TrainingDetailsComponent, resolve: { irb: DashboardResolverService } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class PersonTrainingRoutingModule { }
