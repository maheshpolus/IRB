import { Component, OnInit, HostListener } from '@angular/core';

@Component({
  selector: 'app-irb',
  templateUrl: './irb.component.html',
  styleUrls: ['./irb.component.css']
})
export class IrbComponent {

  constructor() { }
  @HostListener('window:scroll', ['$event'])
  scrollEvent() {
    const scrollHeight = window.pageYOffset;
    if (scrollHeight > 100) {
      document.getElementById('myBtn').style.display = 'block';
    } else {
      document.getElementById('myBtn').style.display = 'none';
    }
  }

  scrollToTop() {
   // document.getElementById('scrollToTop').scrollTop = 0;
 const id = document.getElementById('scrollToTop');
                if (id) {
                    id.scrollIntoView({behavior : 'smooth'});
                }
  }

}
