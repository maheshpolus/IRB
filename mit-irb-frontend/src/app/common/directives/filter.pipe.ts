import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterStatus'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], filterText: string[]): any[] {
    if(!items) return [];
    return items.filter((value) => {
      for (let i = 0; i < filterText.length; i++) {
        if(value.statusCode == filterText[i]){
              return value;
          }
      }
      //return value;
  });

 // return items.filter( item => { 
  
  // if(item.statusCode == filterText)
  //     return item
  
  //   });
   }
}
