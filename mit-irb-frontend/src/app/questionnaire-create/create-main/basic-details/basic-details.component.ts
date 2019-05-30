import { Component, OnInit, Input } from '@angular/core';
import { Constants } from '../../questionnaire.constants';
import * as _ from 'lodash';

@Component({
  selector: 'app-basic-details',
  templateUrl: './basic-details.component.html',
  styleUrls: ['./basic-details.component.css']
})
export class BasicDetailsComponent implements OnInit {

  constructor() { }
  @Input() data: any;
  newUsage = Object.assign({}, Constants.newUsage);
  UsageEditIndex = null;

  ngOnInit() {
  }
  getModuleName(code) {
    const label = Constants.moduleList.find(list => {
       return (list.value === parseInt(code, 10)); });
       return label ? label.label : '';
  }
  getSubmoduleItemName(code) {
    const label = Constants.subModuleList.find(list => {
       return (list.subModuleId ===  parseInt(code, 10)); });
       return label ? label.label : '';
  }
  /**
  * creates new usage item with current questionnaire id as its questionnaire id
  */
  addNewUsage() {
    if (this.newUsage.MODULE_ITEM_CODE){
      Constants.newUsage.QUESTIONNAIRE_ID = this.data.header.QUESTIONNAIRE_ID;
      this.data.usage.push(Object.assign({}, this.newUsage));
      this.newUsage = Object.assign({}, Constants.newUsage);
    }
  }
  findSubmodulelist(moduleId) {
    return _.filter( Constants.subModuleList , {'moduleId': parseInt(moduleId, 10)});
  }
  /**
   * @param  {} index
   * updates the usage actype as D
  */
  removeUsage(index) {
    this.data.usage[index].AC_TYPE = 'D';
  }
  /**
   * @param  {} data
   * @param  {} index
   * set edit usage index and populates the current data in edit field
   */
  editUsage(data, index) {
    this.UsageEditIndex = index;
    this.newUsage = Object.assign({}, data);
  }
  /** removes the edited changes*/
  cancelUsageEdit() {
    this.newUsage = Object.assign({}, Constants.newUsage);
    this.UsageEditIndex = null;
  }
  /** updates the usage array with current changes
   */
  updateUsage() {
    this.data.usage.splice(this.UsageEditIndex, 1, this.newUsage);
    this.cancelUsageEdit();
  }
}
