import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterStatus'
})
export class FilterPipe implements PipeTransform {debugger;
  transform(items: any[], filterText: string[],loginPersonId: any,tabSelected: String): any[] {
    if(!items) return [];
    return items.filter((value) => {
      for (let i = 0; i < filterText.length; i++) {
        if(value.statusCode == filterText[i]){
            if((value.statusCode == 2 || value.statusCode == 5 || value.statusCode == 3) && tabSelected == "PENDING" ){
              if(value.facultySponsorPersonId == loginPersonId)
                return value;
            } else{
              return value;}
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
