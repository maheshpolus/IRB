import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class SharedDataService {

  constructor( private _http: HttpClient ) { }

  public generalInfo = new BehaviorSubject<any>({});
  generalInfoVariable = this.generalInfo.asObservable();

  public commonVo = new BehaviorSubject<any>({});
  CommonVoVariable = this.commonVo.asObservable();

  public currentTab = new BehaviorSubject<any>(null);
  currentTabVariable = this.currentTab.asObservable();

  public viewProtocolDetails = new BehaviorSubject<any>(null);
  viewProtocolDetailsVariable = this.viewProtocolDetails.asObservable();

  public searchData: any = null;
  public isAdvancesearch = false;
  public searchedStatus = null;
  public searchedSubmissionStatus = null;

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
getViewProtocolDetails() {
  return this.viewProtocolDetails;
}
setviewProtocolDetails(viewProtocolDetails: any) {
  this.viewProtocolDetails.next(viewProtocolDetails);
}
checkUserPermission(params) {
  return this._http.post('/irb/checkUserPermission', params);
}

}
