import { Component, OnInit, OnDestroy, ViewContainerRef } from '@angular/core';
import { ISubscription } from 'rxjs/Subscription';
import { ActivatedRoute } from '@angular/router';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';
import { IrbCreateService } from '../irb-create.service';
import { SharedDataService } from '../../common/service/shared-data.service';

declare var $: any;

@Component({
  selector: 'app-irb-protocol',
  templateUrl: './irb-protocol.component.html',
  styleUrls: ['./irb-protocol.component.css']
})

export class IrbProtocolComponent implements OnInit, OnDestroy {

  isProtocolScienceEmpty = false;
  userDTO: any = {};
  requestObject: any = {};
  commonVo: any = {};
  scientificId: any;
  protocolScience: string;
  ckEditorConfig: {} = {
    height: '300px',
    toolbarCanCollapse: 1,
    removePlugins: 'sourcearea',
  };
  private $subscription1: ISubscription;

  constructor(private _irbCreateService: IrbCreateService, private _sharedDataService: SharedDataService,
    private _activatedRoute: ActivatedRoute, public toastr: ToastsManager, vcr: ViewContainerRef) {
    this.toastr.setRootViewContainerRef(vcr);
  }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.requestObject.protocolId = params['protocolId'];
      this.requestObject.protocolNumber = params['protocolNumber'];
    });
    // this.userDTO = this._activatedRoute.snapshot.data['irb'];

    this.$subscription1 = this._sharedDataService.commonVo.subscribe(commonVo => {
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
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
  }

  saveProtocolScience() {
    if (($('#editor1 iframe').contents().find('body').text()).toString() === '') {
      this.isProtocolScienceEmpty = true;
    } else {
      this.isProtocolScienceEmpty = false;
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
        this.toastr.success('Protocol saved successfully', null, { toastLife: 2000 });
        if (this.commonVo.scienceOfProtocol != null) {
          this.protocolScience = this.commonVo.scienceOfProtocol.description;
        }
      },
        error => {
          this.toastr.error('Failed to save Protocol', null, { toastLife: 2000 });
          console.log('Error in saveScienceOfProtocol ', error);
        }
      );
    }
  }

}
