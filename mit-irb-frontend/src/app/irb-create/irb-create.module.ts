import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Ng2CompleterModule } from 'ng2-completer';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CKEditorModule } from 'ngx-ckeditor';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileDropModule } from 'ngx-file-drop';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { IrbCreateHeaderComponent } from './irb-create-header/irb-create-header.component';
import { IrbEditComponent } from './irb-edit/irb-edit.component';
import { IrbCreateRoutingModule } from '../irb-create/irb-create-routing.module';
import { IrbCreateAttachmentComponent } from './irb-create-attachment/irb-create-attachment.component';
import { IrbCreateHistroyComponent } from './irb-create-histroy/irb-create-histroy.component';
import { AppCommonModule } from '../common/common/common.module';
import { IrbActionsComponent } from './irb-actions/irb-actions.component';
import { CollaboratorsComponent } from './irb-edit/collaborators/collaborators.component';
import { SubjectsComponent } from './irb-edit/subjects/subjects.component';
import { FundingSourceComponent } from './irb-edit/funding-source/funding-source.component';
import { PersonnelInfoComponent } from './irb-edit/personnel-info/personnel-info.component';
import { GeneralDetailsComponent } from './irb-edit/general-details/general-details.component';
import { IrbProtocolComponent } from './irb-protocol/irb-protocol.component';
import { IrbQuestionnaireListComponent } from './irb-questionnaire-list/irb-questionnaire-list.component';
import { ProtocolUnitsComponent } from './irb-edit/protocol-units/protocol-units.component';
import { AdministratorContactComponent } from './irb-edit/administrator-contact/administrator-contact.component';
import { SubmissionDetailsComponent } from './irb-create-histroy/submission-details/submission-details.component';
import { IrbSummaryDetailsComponent } from './irb-summary-details/irb-summary-details.component';

@NgModule({
  imports: [
    CommonModule,
    IrbCreateRoutingModule,
    FormsModule,
    NgxSpinnerModule,
    ReactiveFormsModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    Ng2CompleterModule,
    FileDropModule,
    AppCommonModule,
    CKEditorModule,
    NgbModule.forRoot()
  ],
  declarations: [IrbCreateHeaderComponent, IrbEditComponent, IrbCreateAttachmentComponent,
    IrbCreateHistroyComponent, IrbActionsComponent, CollaboratorsComponent, SubjectsComponent,
    FundingSourceComponent, PersonnelInfoComponent, GeneralDetailsComponent, IrbProtocolComponent,
    IrbQuestionnaireListComponent,
    ProtocolUnitsComponent,
    AdministratorContactComponent, SubmissionDetailsComponent, IrbSummaryDetailsComponent],
    exports: [IrbActionsComponent, IrbSummaryDetailsComponent]
})
export class IrbCreateModule { }
