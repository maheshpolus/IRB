import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-expanded-view',
  templateUrl: './expanded-view.component.html',
  styleUrls: ['./expanded-view.component.css']
})
export class ExpandedViewComponent implements OnInit {
  lastClickedTab = '';
  reverse = false;

  constructor() { }

  ngOnInit() {
  }
  sortBy(criteria) {
      this.reverse = !this.reverse;
      this.lastClickedTab = criteria;
  }
}
