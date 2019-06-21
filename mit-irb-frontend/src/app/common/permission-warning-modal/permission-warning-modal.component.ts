import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-permission-warning-modal',
  templateUrl: './permission-warning-modal.component.html',
  styleUrls: ['./permission-warning-modal.component.css']
})
export class PermissionWarningModalComponent implements OnInit {
@Input() alertMessage: string;
  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
