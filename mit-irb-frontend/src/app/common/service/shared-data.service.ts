import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class SharedDataService {

  constructor() { }

  public generalInfo = new BehaviorSubject<any>({});
  generalInfoVariable = this.generalInfo.asObservable();

  getGeneralInfo() {
    return this.generalInfo;
  }
  
 setGeneralInfo(generalInfo: any) {
    this.generalInfo.next(generalInfo);
  }

}
