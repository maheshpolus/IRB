import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReviewComponent } from './review.component';
import { ReviewRoutingModule } from './review-routing.module';
import { CommentBoxComponent } from './comment-box/comment-box.component';
import { AutoGrowDirective } from './autoGrow.directive';
import { FormsModule } from '@angular/forms';
import { ViewModule } from '../questionnaire-view/view.module';
import { ReviewQuestionnaireListComponent } from './questionnaire-list/questionnaire-list.component';
import { AttachmentsComponent } from './attachments/attachments.component';


@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReviewRoutingModule,
    ViewModule
  ],
  declarations: [ReviewComponent, CommentBoxComponent, AutoGrowDirective, AttachmentsComponent, ReviewQuestionnaireListComponent],
})
export class ReviewModule { }
