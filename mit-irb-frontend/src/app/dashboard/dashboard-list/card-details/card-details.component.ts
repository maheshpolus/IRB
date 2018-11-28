import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.css']
})
export class CardDetailsComponent  {

  @Input() irbList: any = [];

  constructor(private _router: Router) {
   }

  /**Opens Irb Component
   * @protocolNumber - unique id for protocol
  */
  openIrb( protocolNumber ) {
      this._router.navigate( ['/irb/irb-view/irbOverview'], {queryParams: {protocolNumber: protocolNumber}});
  }

  EditIrb (protocolNumber, protocolId) {
    this._router.navigate( ['/irb/irb-create'], {queryParams: {protocolNumber: protocolNumber, protocolId: protocolId}});
  }
}
