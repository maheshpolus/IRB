import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { IrbViewService } from '../irb-view.service';

@Component({
  selector: 'app-irb-header-detail',
  templateUrl: './irb-header-detail.component.html',
  styleUrls: ['./irb-header-detail.component.css']
})

export class IrbHeaderDetailComponent implements OnInit {
  isExpanded: boolean;
  currentTab = '';
  irbHeaderDetails: any;
  result: any;

  requestObject = {
          protocolNumber: ''
  };

  constructor(private router: Router, private _activatedRoute: ActivatedRoute, private _irbViewService: IrbViewService,
        private spinner: NgxSpinnerService ) {
            this.spinner.show();
         }

  ngOnInit() {
      this.isExpanded = false;
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.loadHeaderDetails();
  }

  show_current_tab( e: any, current_tab ) {
      e.preventDefault();
      this.currentTab = current_tab;
  }
  loadHeaderDetails() {
          this._irbViewService.getIrbHeaderDetails( this.requestObject ).subscribe( data => {
              this.result = data || [];
              if ( this.result != null ) {
                  this.irbHeaderDetails = this.result.irbViewHeader;
              }
          },
              error => {
                   console.log( "Error in method loadHeaderDetails()", error );
              },
          );
  }
  toggle() {
      this.isExpanded = !this.isExpanded;
  }
}
