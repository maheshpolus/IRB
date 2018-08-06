import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { IrbCreateRoutingModule } from '../irb-create/irb-create-routing.module';
//import { IrbHistoryComponent } from '../irb-view/irb-history/irb-history.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    IrbCreateRoutingModule,
    FormsModule,  
  ],
  declarations: [IrbCreateHeaderComponent,IrbEditComponent]//IrbHistoryComponent
})
export class IrbCreateModule { }
