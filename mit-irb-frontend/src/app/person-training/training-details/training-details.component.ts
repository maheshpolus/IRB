import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { UploadEvent, UploadFile, FileSystemFileEntry } from 'ngx-file-drop';

import { PiElasticService } from '../../common/service/pi-elastic.service';

@Component({
  selector: 'app-training-details',
  templateUrl: './training-details.component.html',
  styleUrls: ['./training-details.component.css']
})
export class TrainingDetailsComponent implements OnInit {

  options: any = {};
  selectedPerson = {};
  elasticPlaceHolder: string;
  personType = 'employee';
  clearField: any = 'true';
  showPersonElasticBand = false;

  uploadedFile: File[] = [];
    files: UploadFile[] = [];
    fil: FileList;
    attachmentList: any[] = [];

  constructor(private _elasticsearchService: PiElasticService, public changeRef: ChangeDetectorRef) {
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.IRB_INDEX;
    this.options.type = 'person';
    this.options.size = 20;
    this.options.contextField = 'full_name';
    this.options.debounceTime = 500;
    this.options.theme = '#a31f34';
    this.options.width = '100%';
    this.options.fontSize = '16px';
    this.options.defaultValue = '';
    this.options.formatString = 'full_name | email_address | home_unit';
    this.options.fields = {
      full_name: {},
      first_name: {},
      user_name: {},
      email_address: {},
      home_unit: {}

    };
    this.elasticPlaceHolder = 'Search for an Employee Name';
  }

  ngOnInit() {
  }
  changePersonType(type) {
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.showPersonElasticBand = false;
    if (type === 'employee') {
      this.options.index = this._elasticsearchService.IRB_INDEX;
      this.options.type = 'person';
      this.elasticPlaceHolder = 'Search for an Employee Name';

    } else {
      this.options.index = this._elasticsearchService.NON_EMPLOYEE_INDEX;
      this.options.type = 'rolodex';
      this.elasticPlaceHolder = 'Search for an Non-Employee Name';
    }
  }
  selectPersonElasticResult(personDetails) {
    this.selectedPerson = personDetails;
    this.showPersonElasticBand = personDetails != null ? true : false;
  }

   /** Push the unique files choosen to uploaded file
     * @param files- files choosen
     */
    onChange(files: FileList) {
      this.fil = files;
      for (let index = 0; index < this.fil.length; index++) {
          this.uploadedFile.push(this.fil[index]);
      }
      this.changeRef.detectChanges();
  }

  /** Push the unique files dropped to uploaded file
  * @param files- files dropped
  */
  public dropped(event: UploadEvent) {
      this.files = event.files;
      for (const file of this.files) {
          this.attachmentList.push(file);
      }
      for (const file of event.files) {
          if (file.fileEntry.isFile) {
              const fileEntry = file.fileEntry as FileSystemFileEntry;
              fileEntry.file((info: File) => {
                  this.uploadedFile.push(info);
                  this.changeRef.detectChanges();
              });
          }
      }
  }
  /**Remove an item from the uploded file
   * @param item-item to be removed
   */
  deleteFromUploadedFileList(item) {
      for (let i = 0; i < this.uploadedFile.length; i++) {
          if (this.uploadedFile[i].name === item.name) {
              this.uploadedFile.splice(i, 1);
              this.changeRef.detectChanges();
          }
      }
  }
  triggerAdd() {
    document.getElementById('addAttach').click();
  }
  dismissAttachmentModal() {
    document.getElementById('cancelbtn').click();
  }

}
