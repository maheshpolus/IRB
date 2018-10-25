import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { DashboardResolverService } from '../common/service/dashboard-resolver.service';
import { IrbCreateAttachmentComponent } from './irb-create-attachment/irb-create-attachment.component';
import { IrbCreateHistroyComponent } from './irb-create-histroy/irb-create-histroy.component';
import { IrbActionsComponent } from './irb-actions/irb-actions.component';
import { IrbProtocolComponent } from './irb-protocol/irb-protocol.component';
const routes: Routes = [
  {
    path: '', component: IrbCreateHeaderComponent,
    children: [
      { path: '', redirectTo: 'irbHome', pathMatch: 'full' },
      { path: '', component: IrbEditComponent },
      { path: 'irbHome', component: IrbEditComponent, resolve: { irb: DashboardResolverService } },
      { path: 'irbCreateAttachment', component: IrbCreateAttachmentComponent },
      { path: 'irbHistory', component: IrbCreateHistroyComponent },
      { path: 'irbActions', component: IrbActionsComponent },
      { path: 'irbProtocol', component: IrbProtocolComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IrbCreateRoutingModule { }




