import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

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

  constructor(private location: Location,
          private router: Router, private _activatedRoute: ActivatedRoute, private _irbViewService: IrbViewService) {}

  /** sets requestObject and call function to load header details */
  ngOnInit() {
      this.isExpanded = false;
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.loadHeaderDetails();
  }

  /**sets current tab value to identify which tabs has been clicked */
  loadHeaderDetails() {
            this._irbViewService.getIrbHeaderDetails( this.requestObject ).subscribe( data => {
              this.result = data || [];
              if ( this.result != null ) {
                  this.irbHeaderDetails = this.result.irbViewHeader;
              }
          },
              error => {
                   console.log( 'Error in method loadHeaderDetails()', error );
              },
          );
  }

  /**sets current tab value to identify which tabs has been clicked
   * @param current_tab - value of currently selected tab
   */
  show_current_tab( current_tab ) {
    this.currentTab = current_tab;
  }

  /**sets expand and collapse boolean for header details */
  toggle() {
      this.isExpanded = !this.isExpanded;
  }
  
  backClick(event) {
      event.preventDefault();
      // this.router.navigate(['/irb/dashboard']);
      this.location.back();
  }
}
