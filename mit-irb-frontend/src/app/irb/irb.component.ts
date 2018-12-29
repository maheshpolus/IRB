import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-irb',
  templateUrl: './irb.component.html',
  styleUrls: ['./irb.component.css']
})
export class IrbComponent {

  constructor() { }
  @HostListener('scroll', ['$event'])
  scrollEvent() {
    const scrollHeight = document.getElementById('scrollToTop').scrollTop;
    if (scrollHeight > 100) {
      document.getElementById('myBtn').style.display = 'block';
    } else {
      document.getElementById('myBtn').style.display = 'none';
    }
  }

  scrollToTop() {
   document.getElementById('scrollToTop').scrollTop = 0;
  }

}
