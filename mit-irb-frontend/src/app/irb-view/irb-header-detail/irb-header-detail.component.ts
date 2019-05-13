import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { IrbViewService } from '../irb-view.service';
import { SharedDataService } from '../../common/service/shared-data.service';

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

  PROTOCOL_BASIC_INFO: string;

  requestObject = {
          protocolNumber: ''
  };

  constructor(private router: Router, private _activatedRoute: ActivatedRoute,
    private _irbViewService: IrbViewService, private _http: HttpClient, private _sharedDataService: SharedDataService) {
        this.router.events.subscribe((evt: any) => {
            if (evt instanceof NavigationEnd) {
              if (evt.url === '/irb/irb-view/irbOverview?protocolNumber=' + this.requestObject.protocolNumber) {
                this.loadHeaderDetails();
              }
            }
            if (evt.snapshot != null && evt.snapshot.params != null  && evt.snapshot.params.queryParams != null) {
              this.requestObject.protocolNumber = evt.snapshot.params.queryParams.protocolNumber;
            }
        });
    }


  /** sets requestObject and call function to load header details */
  ngOnInit() {
    this._http.get('/mit-irb/resources/string_config_json').subscribe(
        data => {
            const property_config: any = data;
            if (property_config) {
                this.PROTOCOL_BASIC_INFO = property_config.PROTOCOL_BASIC_INFO;
            }
        });
      this.userDTO = this._activatedRoute.snapshot.data['irb'];
      this.isExpanded = false;
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.previousUrl = this._activatedRoute.snapshot.queryParamMap.get('from');
      this.loadHeaderDetails();
      if (this.isAmmendmentOrRenewal()) {
        this.show_current_tab('irbSummaryDetails');
        this.router.navigate(['/irb/irb-view/irbSummaryDetails'],
        { queryParams: { protocolNumber: this.requestObject.protocolNumber } });
      }
  }

  /**sets current tab value to identify which tabs has been clicked */
  loadHeaderDetails() {
            this._irbViewService.getIrbHeaderDetails( this.requestObject ).subscribe( data => {
              this.result = data || [];
              if ( this.result != null ) {
                  this.irbHeaderDetails = this.result.irbViewHeader;
                  this._sharedDataService.setviewProtocolDetails(this.irbHeaderDetails);
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
                              {queryParams: {personId: this.userDTO.personID,
                                personRole: this.userDTO.role, summaryType: this.previousUrl}});
      } else {
        this.router.navigate(['/irb/dashboard']);
      }
      // event.preventDefault();
      // this.router.navigate(['/irb/dashboard']);
     // this.location.back();
  }

  isAmmendmentOrRenewal() {
    const isammendmentOrRenewal = this.requestObject.protocolNumber.includes('A') ||
    this.requestObject.protocolNumber.includes('R') ? true : false;
    return isammendmentOrRenewal;
  }
}
