import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';

import { ViewRoutingModule } from './view-routing.module';
import { ViewQuestionnaireService } from './view.service';
import { ViewQuestionnaireComponent } from './view-questionnaire/view-questionnaire.component';

import { DragNdropDirective } from './file-drop/drag-ndrop.directive';
import { FileDropComponent } from './file-drop/file-drop.component';

@NgModule({
  imports: [
    CommonModule,
    ViewRoutingModule,
    FormsModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
  ],
  providers: [ViewQuestionnaireService],
  declarations: [ViewQuestionnaireComponent, DragNdropDirective, FileDropComponent]
})
export class ViewModule { }
