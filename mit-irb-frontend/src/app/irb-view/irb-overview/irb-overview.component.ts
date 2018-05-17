import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-irb-overview',
  templateUrl: './irb-overview.component.html',
  styleUrls: ['./irb-overview.component.css']
})
export class IrbOverviewComponent implements OnInit {
  isExpanded = true;
  constructor() { }

  ngOnInit() {
  }
  toggle() {
      this.isExpanded = !this.isExpanded;
  }
}
