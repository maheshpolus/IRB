import { Component, OnInit, Input, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../../../common/service/shared-data.service';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit, AfterViewInit {
    //isIrbList = false;

  @Input() irbList: any = [];
  statusStyle: string;

  constructor(private router: Router, private sharedData: SharedDataService) {
    //  this.isIrbList = false;
  }

  ngOnInit() {
  }
  ngAfterViewInit(){/*
      if( this.irbList == null || this.irbList.length == 0){
          this.isIrbList = true;
      }
      console.log("irblistaterview"+this.isIrbList);
      */
  }

  openIrb( protocolNumber ) {
      this.router.navigate( ['/irb/irb-view/irbOverview'] , {queryParams: {protocolNumber: protocolNumber}});
  }
}
