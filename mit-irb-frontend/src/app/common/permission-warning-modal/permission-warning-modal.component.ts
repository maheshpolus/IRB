import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-permission-warning-modal',
  templateUrl: './permission-warning-modal.component.html',
  styleUrls: ['./permission-warning-modal.component.css']
})
export class PermissionWarningModalComponent implements OnInit {
@Input() alertMessage: string;
@Input() openProtocol: any;
@Input() protocolNumber: any;
  constructor(public activeModal: NgbActiveModal, private _router: Router,) { }

  ngOnInit() {
  }

  openProtocolViewMode() {
    this.activeModal.dismiss('Cross click');
    this._router.navigate( ['/irb/irb-view/irbOverview'], {queryParams: {protocolNumber: this.protocolNumber}});
  }
}
