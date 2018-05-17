import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from '../../../common/service/shared-data.service';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent implements OnInit {

  @Input() irbList: any = [];
  statusStyle: string;

  constructor(private router: Router, private sharedData: SharedDataService) { }

  ngOnInit() {}

  openIrb( protocolNumber ) {
      this.router.navigate( ['/irb/irb-view/irbOverview'] , {queryParams: {protocolNumber: protocolNumber}});
  }
}
