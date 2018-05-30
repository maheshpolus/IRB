import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { IrbViewService } from "../irb-view.service";
import { NgxSpinnerService } from 'ngx-spinner';

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
          protocol_number: ''
  };
  
  constructor(private router: Router, private route: ActivatedRoute, private _irbViewService: IrbViewService,
        private spinner: NgxSpinnerService ) {
            this.spinner.show();
         }

  ngOnInit() {debugger;
      this.isExpanded = false;
      console.log("in onit", this.currentTab);
      this.requestObject.protocol_number = this.route.snapshot.queryParamMap.get('protocolNumber');
      this.loadHeaderDetails();
  }

  show_current_tab( e: any, current_tab ) {
      e.preventDefault();
      this.currentTab = current_tab;
  }
  loadHeaderDetails(){
          this._irbViewService.getIrbHeaderDetails( this.requestObject ).subscribe( data => {
              this.result = data || [];
              if ( this.result != null ) {
                  this.irbHeaderDetails = this.result.irbviewHeader;
              }
          },
              error => {
                   console.log( error );
              },
          );
  }
  toggle(){
      console.log("in toggle", this.isExpanded);
      this.isExpanded = !this.isExpanded;
  }
}
