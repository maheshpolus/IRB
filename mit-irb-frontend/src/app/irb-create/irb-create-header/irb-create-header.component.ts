import { Component, OnInit, OnDestroy } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedDataService } from '../../common/service/shared-data.service';
import { IrbCreateService } from '../irb-create.service';

@Component({
  selector: 'app-irb-create-header',
  templateUrl: './irb-create-header.component.html',
  styleUrls: ['./irb-create-header.component.css'],
  providers: [IrbCreateService, SharedDataService]
})
export class IrbCreateHeaderComponent implements OnInit, OnDestroy {
  commonVo: any = {};
  generalInfo: any = {};
  protocolNumber = null;
  protocolId = null;
  piName = null;
  isExpanded: boolean;
  isCreateNewProtocol = false;
  private $subscription1: ISubscription;
  constructor(private _sharedDataService: SharedDataService,
    private _router: Router,
    private _activatedRoute: ActivatedRoute,
    private _irbCreateService: IrbCreateService,
    private _spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.$subscription1 = this._sharedDataService.generalInfoVariable.subscribe(generalInfo => {
      if (generalInfo !== undefined) {
        this.generalInfo = generalInfo;
        if (this.generalInfo.personnelInfos != null) {
          this.generalInfo.personnelInfos.forEach(element => {
            if (element.protocolPersonRoleId === 'PI') {
              this.piName = element.personName;
            }
          });
        }
      }
    });
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId == null || this.protocolId === undefined) {
        this.isCreateNewProtocol = true;
      } else {
        this.isCreateNewProtocol = false;
      }
    });

    this.getIRBProtocol();
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  getIRBProtocol() {
    const requestObject = { protocolId: this.protocolId };
    this._spinner.show();
    this._irbCreateService.getEditDetails(requestObject).subscribe(
      data => {
        this._spinner.hide();
        this.commonVo = data;
        this.generalInfo = this.commonVo.generalInfo;
        this._sharedDataService.setCommonVo(this.commonVo);
        if (this.commonVo.generalInfo.personnelInfos != null) {
          this.commonVo.generalInfo.personnelInfos.forEach(element => {
            if (element.protocolPersonRoleId === 'PI') {
              this.piName = element.personName;
            }
          });
        }
      });
  }

  /**sets current tab value to identify which tabs has been clicked
    * @param current_tab - value of currently selected tab
    */
  show_current_tab(current_tab) {
    if (this.protocolNumber !== null && this.protocolId !== null) {
      this._router.navigate(['/irb/irb-create/' + current_tab],
        { queryParams: { protocolNumber: this.protocolNumber, protocolId: this.protocolId } });
    }
  }
  toggle() {
    this.isExpanded = !this.isExpanded;
  }
}
