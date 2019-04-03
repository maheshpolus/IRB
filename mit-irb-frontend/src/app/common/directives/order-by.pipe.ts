import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'orderBy'
})
export class OrderByPipe implements PipeTransform {
    /**
     * @param  {Array<any>} records
     * @param  {any} args
     * returns the array sorted by args.property and in args.direction order
     */
    transform(records: Array<any>, args?: any): any {
        if (records != null && records !== undefined) {
            return records.sort(function (a, b) {
                a[args.property] = (a[args.property] == null ? '' : a[args.property].toString());
                b[args.property] = (b[args.property] == null ? '' : b[args.property].toString());
                if (a[args.property].toLowerCase() < b[args.property].toLowerCase()) {
                    return 1 * args.direction;
                } else if (a[args.property].toLowerCase() > b[args.property].toLowerCase()) {
                    return -1 * args.direction;
                } else {
                    return 0;
                }
            });
        }
    }
}
