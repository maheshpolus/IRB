import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-exempt-card',
  templateUrl: './exempt-card.component.html',
  styleUrls: ['./exempt-card.component.css']
})
export class ExemptCardComponent {

  /**input from parent componet Exempt list */
  @Input() irbList: any = [];

  constructor(private _router: Router) { }
  /**Opens Expemt Exempt Component
   * @exemptId - unique id for exempt form
   * @mode - view or edit mode
   * @add question ID to route as query params
  */
  openExempt(exemptId, mode, exemptTitle, exemptFormID) {
    if (mode == null) {
      mode = 1;
    }
    this._router.navigate( ['/irb/exempt-questionaire'],
    {queryParams: {exempHeaderId: exemptId, mode: mode, title: exemptTitle, exemptFormID: exemptFormID}});
  }
}
