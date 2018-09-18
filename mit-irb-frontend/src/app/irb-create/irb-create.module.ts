import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ng2CompleterModule } from 'ng2-completer';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { IrbCreateRoutingModule } from '../irb-create/irb-create-routing.module';
//import { IrbHistoryComponent } from '../irb-view/irb-history/irb-history.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';

@NgModule({
  imports: [
    CommonModule,
    IrbCreateRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    Ng2CompleterModule
  ],
  declarations: [IrbCreateHeaderComponent, IrbEditComponent]//IrbHistoryComponent
})
export class IrbCreateModule { }
