import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { Router, ActivatedRoute} from '@angular/router';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-create-header',
  templateUrl: './irb-create-header.component.html',
  styleUrls: ['./irb-create-header.component.css']
})
export class IrbCreateHeaderComponent implements OnInit, OnDestroy {
  generalInfo = {};
  protocolNumber = null;
  protocolId = null;
  isExpanded: boolean;
  private subscription1: ISubscription;
  constructor(private _sharedDataService: SharedDataService,
    private _router: Router,
    private _activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
      if (generalInfo !== undefined) {
        this.generalInfo = generalInfo;
      }
    });
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
  });
  }

  ngOnDestroy() {
    if (this.subscription1) {
      this.subscription1.unsubscribe();
    }
  }

  /**sets current tab value to identify which tabs has been clicked
    * @param current_tab - value of currently selected tab
    */
  show_current_tab(current_tab) {
    if (this.protocolNumber !== null && this.protocolId !== null) {
      this._router.navigate( ['/irb/irb-create/' + current_tab],
     {queryParams: {protocolNumber: this.protocolNumber, protocolId: this.protocolId}});
    }
  }
  toggle() {
      this.isExpanded = !this.isExpanded;
    }
}
