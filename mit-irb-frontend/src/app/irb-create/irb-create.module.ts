import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ng2CompleterModule } from 'ng2-completer';
import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { IrbCreateRoutingModule } from '../irb-create/irb-create-routing.module';
//import { IrbHistoryComponent } from '../irb-view/irb-history/irb-history.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileDropModule } from 'ngx-file-drop';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { IrbCreateAttachmentComponent } from './irb-create-attachment/irb-create-attachment.component';
import { IrbCreateHistroyComponent } from './irb-create-histroy/irb-create-histroy.component';
import { AppCommonModule } from '../common/common/common.module';
import { IrbActionsComponent } from './irb-actions/irb-actions.component';

@NgModule({
  imports: [
    CommonModule,
    IrbCreateRoutingModule,
    FormsModule,  
	 ReactiveFormsModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
  Ng2CompleterModule,
    FileDropModule,
    AppCommonModule
  ],
  declarations: [IrbCreateHeaderComponent,IrbEditComponent, IrbCreateAttachmentComponent, IrbCreateHistroyComponent, IrbActionsComponent]//IrbHistoryComponent
})
export class IrbCreateModule { }
