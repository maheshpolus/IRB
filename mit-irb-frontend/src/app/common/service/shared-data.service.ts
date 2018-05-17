import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class SharedDataService {
    public irbList = new BehaviorSubject<any>( {} );
    irbListVariable = this.irbList.asObservable();

    constructor() { }

    getIrbList() {
        return this.irbList;
    }

    setIrbList( irbList: any ) {
        this.irbList.next( irbList );
    }
}
