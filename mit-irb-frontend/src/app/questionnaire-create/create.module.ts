import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { QuestionnaireDataResolverService } from './services/questionnairedata-resolver.service';
import { CreateRoutingModule } from './create-routing.module';
import { CreateQuestionnaireComponent } from './create-main/create-questionnaire/create-questionnaire.component';
import { QuestionnaireTreeComponent } from './create-main/questionnaire-tree/questionnaire-tree.component';
import { PreviewQuestionnaireComponent } from './create-main/preview-questionnaire/preview-questionnaire.component';
import { MainRouterComponent } from './main-router/main-router.component';
import { QuestionnaireListComponent } from './questionnaire-list/questionnaire-list.component';
import { CreateMainComponent } from './create-main/create-main.component';
import { CreateQuestionnaireService } from './services/create.service';
import { AutoGrowDirective } from './services/autoGrow.directive';

import { CKEditorModule } from 'ngx-ckeditor';
import { NgxSpinnerModule } from 'ngx-spinner';
import { BasicDetailsComponent } from './create-main/basic-details/basic-details.component';

@NgModule({
  imports: [
    CommonModule,
    CreateRoutingModule,
    FormsModule,
    CKEditorModule,
    NgxSpinnerModule
  ],
  declarations: [ CreateQuestionnaireComponent,
                  QuestionnaireTreeComponent,
                  PreviewQuestionnaireComponent,
                  MainRouterComponent,
                  QuestionnaireListComponent,
                  CreateMainComponent,
                  BasicDetailsComponent,
                  AutoGrowDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [QuestionnaireDataResolverService,
              CreateQuestionnaireService]
})
export class CreateModule { }
