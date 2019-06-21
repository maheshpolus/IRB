import { NgModule } from '@angular/core';
import { RoleMaintainanceComponent } from './role-maintainance.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AppCommonModule } from '../common/common/common.module';
// import { SharedModule } from '../shared/shared.module';
import { RoleMaintainanceService } from './role-maintainance.service';
import { FormsModule } from '@angular/forms';


@NgModule({
  imports: [
    CommonModule,
   // SharedModule,
   AppCommonModule,
    FormsModule,
    RouterModule.forChild([{ path: '', component: RoleMaintainanceComponent }])
  ],
  declarations: [RoleMaintainanceComponent],
  providers: [RoleMaintainanceService]

})
export class RoleMaintainanceModule { }
