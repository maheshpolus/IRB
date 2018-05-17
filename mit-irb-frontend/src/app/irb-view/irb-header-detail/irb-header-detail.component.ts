import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-irb-header-detail',
  templateUrl: './irb-header-detail.component.html',
  styleUrls: ['./irb-header-detail.component.css']
})
export class IrbHeaderDetailComponent implements OnInit {
  currentTab = 'irb_overView';
  
  constructor(private router: Router, private route: ActivatedRoute ) { }

  ngOnInit() {
      console.log(this.route.snapshot.queryParamMap.get('protocolNumber'));
  }

  show_current_tab( e: any, current_tab ) {
      e.preventDefault();
      this.currentTab = current_tab;
      console.log(this.currentTab);
  }
}
