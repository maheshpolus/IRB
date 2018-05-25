import { Component, OnInit } from '@angular/core';
import { IrbViewService } from "../irb-view.service";
import { ActivatedRoute } from "@angular/router";
import { FormsModule } from '@angular/forms'; 
import 'rxjs/Rx';

@Component({
  selector: 'app-irb-attachments',
  templateUrl: './irb-attachments.component.html',
  styleUrls: ['./irb-attachments.component.css']
})
export class IrbAttachmentsComponent implements OnInit {
    
    noIrbAttachments = false;

    irbAttachmentsList: any[] = [];
    result: any;
    sortOrder: string;
    direction: number;
    column: any;
    sortField = 'UPDATE_TIMESTAMP';
    requestObject = {    
            protocol_number: '',
            attachmentId: ''
    };

  constructor(private _irbViewService: IrbViewService, private route: ActivatedRoute,) { }

  ngOnInit() {debugger;
      this.requestObject.protocol_number =this.route.snapshot.queryParamMap.get('protocolNumber');
      this.loadIrbAttachmentList();
  }
  
  loadIrbAttachmentList(){debugger;
      this._irbViewService.getIrbAttachmentList( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolAttachmentList == null || this.result.irbviewProtocolAttachmentList.length == 0) {
                  this.noIrbAttachments = true;
              }
              else{
                  this.irbAttachmentsList = this.result.irbviewProtocolAttachmentList;
              }
          }
          
      },
          error => {
               console.log( error );
          },
      );
  }
  
  downloadAttachment(attachment_id){debugger;
      this.requestObject.attachmentId = attachment_id;
      this._irbViewService.downloadIrbAttachment( attachment_id ).subscribe(data => {
      var a = document.createElement( "a" );
      var blob = new Blob([data], { type: 'application/pdf' });
      a.href = URL.createObjectURL( blob );
      a.download = "attachments";
      a.click();

      },
              error => console.log("Error downloading the file."),
              () => console.info("OK"));
  }
  
  sortBy( ) {
      this.column = this.sortField;
      this.direction =  parseInt(this.sortOrder, 10);
  }
}
