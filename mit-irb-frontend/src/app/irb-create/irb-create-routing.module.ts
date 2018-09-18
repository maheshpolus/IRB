import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
//import { IrbHistoryComponent } from '../irb-view/irb-history/irb-history.component';
  const routes: Routes = [
    {path: '', component: IrbCreateHeaderComponent,
    children: [      
     {path: '', redirectTo: 'irbHome', pathMatch: 'full'},
     {path: '', component: IrbEditComponent},
      {path: 'irbHome', component: IrbEditComponent, resolve: { irb: DashboardResolverService }},
      //{path: 'irbHistory', component: IrbHistoryComponent}
      ]}
    ];
  @NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
export class IrbCreateRoutingModule { }




