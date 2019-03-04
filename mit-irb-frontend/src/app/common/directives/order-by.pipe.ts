import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'orderBy'
})
export class OrderByPipe implements PipeTransform {

  transform(records: Array<any>, args?: any): any {
      return records.sort(function(a, b) {
            a[args.property] = (a[args.property] == null ? '' : a[args.property].toString());
            b[args.property] = (b[args.property] == null ? '' : b[args.property].toString());
            if (a[args.property].toLowerCase() < b[args.property].toLowerCase()) {
                return 1 * args.direction;
            } else if ( a[args.property].toLowerCase() > b[args.property].toLowerCase()) {
                return -1 * args.direction;
            } else {
                return 0;
            }
      });
  }
}
