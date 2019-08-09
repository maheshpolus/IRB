import { Directive, HostListener, ElementRef } from '@angular/core';

@Directive({
  selector: '[appClickNdrag]'
})
export class ClickNdragDirective {
  element: HTMLElement;
  isMouseDown = false;
  offset = [];
  constructor(public elmnt: ElementRef) {
    this.element = elmnt.nativeElement;
  }
  @HostListener('mousedown', ['$event'])
   public onListenerTriggered(event: any): void {
     if (event.target.className === 'sticky-header') {
      this.isMouseDown = true;
      this.offset.push(this.element.offsetLeft - event.clientX);
      this.offset.push(this.element.offsetTop - event.clientY);
     }
   }
   @HostListener('mousemove', ['$event'])
   public onListenermove(event: any): void {
    event.preventDefault();
    if (this.isMouseDown ) {
      this.element.style.left = this.offset[0] + event.clientX + 'px';
      this.element.style.top  = this.offset[1] + event.clientY + 'px';
    }
   }
   @HostListener('mouseup', ['$event'])
   public onListenerEnd(event: any): void {
    this.isMouseDown = false;
   }
   @HostListener('mouseleave', ['$event'])
   public onListenerLeave(event: any): void {
    this.isMouseDown = false;
   }
}
