import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { CommitteeSaveService } from '../../../../committee-save.service';

@Component({
  selector: 'app-training-details',
  templateUrl: './training-details.component.html',
  styleUrls: ['./training-details.component.css']
})
export class TrainingDetailsComponent implements OnInit {

  attachmentIconValue = null;
  commentIconValue = null;
  personId = null;
  rolodexId = null;
  trainingAttachments: any = [];
  trainingComments: any = [];
  irbPersonDetailedList: any = [];
  irbPersonDetailedTraining: any = [];
  constructor(private _committeeSaveService: CommitteeSaveService, private _activatedRoute: ActivatedRoute,
    private _spinner: NgxSpinnerService) { }

  ngOnInit() {
    this._activatedRoute.queryParams.subscribe(params => {
      this.personId = params['personId'];
      this.rolodexId = params['rolodexId'];
      this.loadPersonDetailedList();
    });
  }


  loadPersonDetailedList() {
    this.attachmentIconValue = -1;
    this.trainingAttachments = [];
    this.commentIconValue = -1;
    this.trainingComments = [];
    const params = { avPersonId: this.personId != null ? this.personId : this.rolodexId };
    this._spinner.show();
    this._committeeSaveService.getIrbPersonDetailedList(params).subscribe((data: any) => {
      this._spinner.hide();
      this.irbPersonDetailedList = data.irbViewProtocolMITKCPersonInfo;
      this.irbPersonDetailedTraining = data.irbViewProtocolMITKCPersonTrainingInfo != null ?
        data.irbViewProtocolMITKCPersonTrainingInfo : [];
    });
  }

  showTrainingAttachments(index) {
    if (this.attachmentIconValue === index) {
      this.attachmentIconValue = -1;
    } else {
      this.attachmentIconValue = index;
      this.trainingAttachments = this.irbPersonDetailedTraining[index].attachment;
    }
  }

  showTrainingComments(index) {
    if (this.commentIconValue === index) {
      this.commentIconValue = -1;
    } else {
      this.commentIconValue = index;
      this.trainingComments = this.irbPersonDetailedTraining[index].comments;
    }
  }

  downloadAttachment(attachment) {
    this._committeeSaveService.downloadTrainingAttachment(attachment.FILE_DATA_ID).subscribe(data => {
      const a = document.createElement('a');
      const blob = new Blob([data], { type: data.type });
      a.href = URL.createObjectURL(blob);
      a.download = attachment.FILE_NAME;
      document.body.appendChild(a);
      a.click();

    },
      error => console.log('Error downloading the file.', error),
      () => console.log('OK'));
  }

}
