import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-committee-card',
  templateUrl: './committee-card.component.html',
  styleUrls: ['./committee-card.component.css']
})
export class CommitteeCardComponent implements OnInit {

  @Input() irbList: any = [];
  constructor(private _router: Router) { }


  ngOnInit() {
  }
  openIrb(committeId) {
    this._router.navigate(['/irb/committee'], { queryParams: { mode: 'view', id:  committeId} });
  }
}
