import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

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
  userDTO: any = {};
  previousUrl: string;

  requestObject = {
          protocolNumber: ''
  };

  constructor(private router: Router, private _activatedRoute: ActivatedRoute, private _irbViewService: IrbViewService) {}

  /** sets requestObject and call function to load header details */
  ngOnInit() {
      this.userDTO = this._activatedRoute.snapshot.data['irb'];
      this.isExpanded = false;
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.previousUrl = this._activatedRoute.snapshot.queryParamMap.get('from');
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
      if (this.previousUrl != null) {
          this.router.navigate(['/irb/expanded-view'],
                              {queryParams: {personId: this.userDTO.personID, personRole: this.userDTO.role, summaryType: this.previousUrl}});
      } else {
        this.router.navigate(['/irb/dashboard']);
      }
      // event.preventDefault();
      // this.router.navigate(['/irb/dashboard']);
     // this.location.back();
  }
}
