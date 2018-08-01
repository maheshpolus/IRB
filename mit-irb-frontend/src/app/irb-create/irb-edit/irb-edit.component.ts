import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '../../../../node_modules/@angular/forms';

@Component({
  selector: 'app-irb-edit',
  templateUrl: './irb-edit.component.html',
  styleUrls: ['./irb-edit.component.css']
})


export class IrbEditComponent implements OnInit {

  personalInfo = new PersonalData();
  personalDataList: PersonalData[];
  userRoles: any[] = [
    { name: "One", value: "one" },
    { name: "Two", value: "two" },
    { name: "Three", value: "three" }
  ]
  //default: string = 'One';

  //createForm: FormGroup;

  personalInfoSelectedRow:number;
  isPersonalInfoEdit = false;
  constructor() {
  //   this.createForm = new FormGroup({
  //     customRoleSelect: new FormControl(null)
  // });
  // this.createForm.controls['customRoleSelect'].setValue(this.default, {onlySelf: true});
    this.personalDataList = [
      { id: "3", name: "test", role: "two", title: "noTitle", leadUnit: "unit", affiliate: "no" },
      { id: "4", name: 'raj', role: 'one', title: 'test', leadUnit: '5', affiliate: 'test' },
      { id: "5", name: 'testNamw', role: 'two', title: 'testtitle', leadUnit: '7', affiliate: 'testTitle' }
    ]
  }
  ngOnInit() {
  }
  editPersonalDetails(selectedItem: any,index :number) {
  this.personalInfoSelectedRow = index;
    this.isPersonalInfoEdit = true;
    this.personalInfo = selectedItem;
  }
  addPersonalDetails() {
    debugger
    this.personalDataList.push({
      id: "0",
      name: this.personalInfo.name,
      role: this.personalInfo.role,
      title: this.personalInfo.title,
      leadUnit: this.personalInfo.leadUnit,
      affiliate: this.personalInfo.affiliate
    });
  }
  deletePersonalDetails(index) {
    this.personalInfoSelectedRow = null;
    this.isPersonalInfoEdit=false;
    this.personalDataList.splice(index, 1);
  }
}
export class PersonalData {
  id: string
  name: string;
  role: string;
  title: string;
  leadUnit: string;
  affiliate: string;
}
