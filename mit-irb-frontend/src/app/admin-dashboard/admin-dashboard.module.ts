import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxSpinnerModule } from 'ngx-spinner';


import { AdminDashboardRoutingModule } from './admin-dashboard-routing.module';
import { AdminDashboardComponent } from './admin-dashboard.component';
@NgModule({
  imports: [
    CommonModule,
    AdminDashboardRoutingModule,
    NgxSpinnerModule,
  ],
  declarations: [AdminDashboardComponent]
})
export class AdminDashboardModule { }
