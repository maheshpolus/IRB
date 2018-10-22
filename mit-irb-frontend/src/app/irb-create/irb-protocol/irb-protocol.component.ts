import { Component, OnInit, OnDestroy, ViewContainerRef } from '@angular/core';
import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';
import { ISubscription } from 'rxjs/Subscription';
import { ActivatedRoute } from '@angular/router';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

@Component({
  selector: 'app-irb-protocol',
  templateUrl: './irb-protocol.component.html',
  styleUrls: ['./irb-protocol.component.css']
})
export class IrbProtocolComponent implements OnInit, OnDestroy {
  ckEditorConfig: {} = {
    height: '300px',
    toolbarCanCollapse: 1,
    removePlugins: 'sourcearea',
  };
  protocolScience: string;
  requestObject: any = {};
  scientificId: any;
  private subscription1: ISubscription;
  commonVo: any = {};


  constructor(private _irbCreateService: IrbCreateService, private _sharedDataService: SharedDataService,
    private _activatedRoute: ActivatedRoute, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.requestObject.protocolId = params['protocolId'];
      this.requestObject.protocolNumber = params['protocolNumber'];
    });

    this.subscription1 = this._sharedDataService.commonVo.subscribe(commonVo => {
      if (commonVo !== undefined) {
        this.commonVo = commonVo;
        if (this.commonVo.scienceOfProtocol != null) {
          this.protocolScience = this.commonVo.scienceOfProtocol.description;
          this.scientificId = this.commonVo.scienceOfProtocol.scientificId;

        }
      }
    });
  }
  ngOnDestroy() {
    if (this.subscription1) {
      this.subscription1.unsubscribe();
    }
  }

  saveProtocolScience() {
    this.commonVo.scienceOfProtocol = {
      scientificId: (this.scientificId == null || this.scientificId === undefined) ? null : this.scientificId,
      protocolId: this.requestObject.protocolId,
      protocolNumber: this.requestObject.protocolNumber,
      description: this.protocolScience,
      updateUser: localStorage.getItem('userName'),
      updatetimestamp: new Date(),
      sequenceNumber: 1

    };
    this._irbCreateService.saveScienceOfProtocol(this.commonVo).subscribe(data => {
      this.commonVo = data;
      if (this.commonVo.scienceOfProtocol != null) {
        this.toastr.success('Protocol saved successfully');
        this.protocolScience = this.commonVo.scienceOfProtocol.description;
      }
    });

  }

}
