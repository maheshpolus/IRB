import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { SharedDataService } from '../../common/service/shared-data.service';

@Component({
  selector: 'app-irb-create-header',
  templateUrl: './irb-create-header.component.html',
  styleUrls: ['./irb-create-header.component.css']
})
export class IrbCreateHeaderComponent implements OnInit, OnDestroy {
  currentTab = '';
  generalInfo: any;
  private subscription1: ISubscription;
  constructor(private _sharedDataService: SharedDataService) { }

  ngOnInit() {
    this.subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
      if (generalInfo !== undefined) {
        this.generalInfo = generalInfo;
      }
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
    this.currentTab = current_tab;
  }
}
