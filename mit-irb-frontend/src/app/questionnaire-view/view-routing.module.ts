import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ViewQuestionnaireComponent  } from './view-questionnaire/view-questionnaire.component';

const routes: Routes = [
  { path: '', redirectTo: 'view', pathMatch: 'full'},
  { path: 'view', component: ViewQuestionnaireComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewRoutingModule { }
