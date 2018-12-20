import { Directive, Output, HostListener, EventEmitter, Input , OnInit } from '@angular/core';

@Directive({
  selector: '[appDragNdrop]'
})
export class DragNdropDirective {

  constructor() { }
  @Output() filesDropEvent: EventEmitter<any> = new EventEmitter();
  // @Output() dropHandler: EventEmitter<any> = new EventEmitter<any>();

  @HostListener('dragover') onDragOver() {
      return false;
  }
  @HostListener('dragenter' ['$event']) onDragStart(e) {
    e.dataTransfer.setData('text', e.target.id);
    return false;
}
  @HostListener('drop', ['$event']) handleDrop(e) {
      e.preventDefault();
      this.handleInputChange(e);
      console.log(e.dataTransfer.files[0]);
      this.filesDropEvent.emit(e.dataTransfer.files[0]);
  }
  handleInputChange(e) {
  }
}
