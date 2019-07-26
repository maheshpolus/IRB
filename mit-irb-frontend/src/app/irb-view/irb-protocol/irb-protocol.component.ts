import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IrbViewService } from '../irb-view.service';

@Component({
  selector: 'app-irb-protocol',
  templateUrl: './irb-protocol.component.html',
  styleUrls: ['./irb-protocol.component.css']
})
export class IrbProtocolComponent implements OnInit {

  requestObject = {
    protocolNumber: null
  };
  scienceOfProtocol: any = {};

  constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute ) { }

  ngOnInit() {
    this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
    this.getScienceOfProtocol();
  }

  getScienceOfProtocol() {
    this._irbViewService.getIRBprotocolScienificData(this.requestObject).subscribe((data: any) => {
    this.scienceOfProtocol = data.irbViewScienceOfProtocol;
    });

  }

}
