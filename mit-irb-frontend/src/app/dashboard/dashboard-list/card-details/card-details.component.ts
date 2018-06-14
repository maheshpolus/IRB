import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent  {

  @Input() irbList: any = [];
  statusStyle: string;

  constructor(private _router: Router) { }

  openIrb( protocolNumber ) {
      this._router.navigate( ['/irb/irb-view/irbOverview'], {queryParams: {protocolNumber: protocolNumber}});
  }
}
