import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IrbCreateService } from '../../irb-create.service';
import { KeyPressEvent } from '../../../common/directives/keyPressEvent.component';

@Component({
  selector: 'app-protocol-units',
  templateUrl: './protocol-units.component.html',
  styleUrls: ['./protocol-units.component.css']
})
export class ProtocolUnitsComponent implements OnInit {

  userDTO: any = {};
  protocolId = null;
  protocolNumber = null;
  isGeneralInfoSaved = false;
  isProtocolUnitSearch = false;
  searchString: string;
  protocolUnitName: string;
  UnitSearchResult = [];

  constructor(private _activatedRoute: ActivatedRoute, private _irbCreateService: IrbCreateService,
    public keyPressEvent: KeyPressEvent) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });
  }
  getHomeUnitList() {
    this.searchString = this.protocolUnitName;
    this._irbCreateService.loadHomeUnits(this.searchString).subscribe(
        (data: any) => {
          this.UnitSearchResult = data.homeUnits;
        });
}
selectedHomeUnit(homeUnitName) {
    this.UnitSearchResult.forEach(homeUnit => {
      if (homeUnit.unitName === homeUnitName) {
        // this.result.committee.homeUnitNumber = homeUnit.unitNumber;
        this.protocolUnitName = homeUnitName;
      }
    });
    this.isProtocolUnitSearch = false;
  }

}
