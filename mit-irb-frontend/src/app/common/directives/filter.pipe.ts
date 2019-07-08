import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterStatus'
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], filterText: string[], loginPersonId: any, tabSelected: String): any[] {
    if (!items) {
      return [];
    }
    return items.filter((value) => {
      for (let i = 0; i < filterText.length; i++) {
        if (value.statusCode === filterText[i]) {
          if ((value.statusCode === '2' || value.statusCode === '5' || value.statusCode === '1') && tabSelected === 'PENDING') {
            if (value.statusCode === '2' && value.facultySponsorPersonId === loginPersonId) {
              return value;
            }
            if (value.statusCode === '5' && value.personId === loginPersonId) {
              return value;
            }
            if (value.statusCode === '1' && value.submittedOnce === 1 && value.personId === loginPersonId) {
              return value;
            }
          } else if (tabSelected === 'STUDIES' &&  (value.statusCode === '1' || value.statusCode === '5')) {
            if (value.personId === loginPersonId  && value.statusCode === '1') {
              return value;
            }
            if ( value.personId === loginPersonId  && value.statusCode === '5' ) {
              return value;
            }
          } else {
            return value;
          }
        }
      }
    });

  }
}
