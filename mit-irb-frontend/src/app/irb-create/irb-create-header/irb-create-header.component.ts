import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-irb-create-header',
  templateUrl: './irb-create-header.component.html',
  styleUrls: ['./irb-create-header.component.css']
})
export class IrbCreateHeaderComponent implements OnInit {
  currentTab = '';
  constructor() { }

  ngOnInit() {  
  }
 /**sets current tab value to identify which tabs has been clicked
   * @param current_tab - value of currently selected tab
   */
  show_current_tab( current_tab ) {
    debugger
    this.currentTab = current_tab;
  }
}
