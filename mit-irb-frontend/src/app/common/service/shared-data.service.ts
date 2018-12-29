import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class SharedDataService {

  constructor() { }

  public generalInfo = new BehaviorSubject<any>({});
  generalInfoVariable = this.generalInfo.asObservable();

  public commonVo = new BehaviorSubject<any>({});
  CommonVoVariable = this.commonVo.asObservable();

  public currentTab = new BehaviorSubject<any>(null);
  currentTabVariable = this.currentTab.asObservable();

  public searchData: any = null;

  getGeneralInfo() {
    return this.generalInfo;
  }

 setGeneralInfo(generalInfo: any) {
    this.generalInfo.next(generalInfo);
  }

  getCommonVo() {
    return this.commonVo;
  }

 setCommonVo(commonVo: any) {
    this.commonVo.next(commonVo);
  }

  changeCurrentTab(currentTab: any) {
    this.currentTab.next(currentTab);
}

}
