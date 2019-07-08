import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { CommonModule } from '@angular/common';
import { OrderByPipe } from '../directives/order-by.pipe';
import {AppElasticComponent} from './app-elastic/app-elastic.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  declarations: [OrderByPipe, AppElasticComponent],
  exports: [OrderByPipe, AppElasticComponent]
})
export class AppCommonModule { }
