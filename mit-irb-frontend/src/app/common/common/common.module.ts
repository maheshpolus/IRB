import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderByPipe } from '../directives/order-by.pipe';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [OrderByPipe],
  exports: [OrderByPipe]
})
export class AppCommonModule { }
