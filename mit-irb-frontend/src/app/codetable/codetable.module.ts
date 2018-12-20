import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { SearchComponent } from './search/search.component';
import { CodeTableComponent } from './code-table/code-table.component';
import { SearchService } from './search/search.service';
import { CodeTableService } from './code-table/code-table.service';
import { FilterPipe } from './code-table/filter.pipe';

import { Ng2CompleterModule } from 'ng2-completer';
import { CodetableRoutingModule } from './/codetable-routing.module';

@NgModule({
  imports: [CommonModule,
            Ng2CompleterModule,
            HttpClientModule,
            FormsModule,
            CodetableRoutingModule
          ],
  declarations: [ SearchComponent,
                  CodeTableComponent,
                  FilterPipe,
                ],
  providers   : [ SearchService,
                  CodeTableService
                ],
  // exports: [ CodeTableComponent,
  //            SearchComponent
  //          ]
})
export class CodetableModule { }
