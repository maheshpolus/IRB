import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterStatus'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], filterText: string[],loginPersonId: any,tabSelected: String): any[] {
    if(!items) return [];
    return items.filter((value) => {
      for (let i = 0; i < filterText.length; i++) {
        if(value.statusCode == filterText[i]){
            if((value.statusCode == 2 || value.statusCode == 5 || value.statusCode == 1) && tabSelected == "PENDING" ){
              if(value.facultySponsorPersonId == loginPersonId)
                return value;
            }else if(value.statusCode == 1 && tabSelected == "PENDING"){
              if(value.submittedOnce == 1 ) 
                return value;
            }
            else{
              return value;}
          }
      }
 
  });


   }
}
