import { Component, OnInit } from '@angular/core';
import { IrbViewService } from "../irb-view.service";

@Component({
  selector: 'app-irb-history',
  templateUrl: './irb-history.component.html',
  styleUrls: ['./irb-history.component.css']
})
export class IrbHistoryComponent implements OnInit {
    noHistoryDetails: boolean;

    irbHistoryDetails = [
                         {
                             "ACTION_DATE":"20/12/2009",
                             "update_user_name":"Nicole",
                             "PROTOCOL_ACTION":"Submitted"
                        },
                        {
                            "ACTION_DATE":"20/12/2009",
                            "update_user_name":"Sid",
                            "PROTOCOL_ACTION":"Submitted"
                       },
                       {
                           "ACTION_DATE":"20/12/2009",
                           "update_user_name":"Fed",
                           "PROTOCOL_ACTION":"Progress"
                      }];
    noHistoryList = false;
  
    irbHistoryList = [
                       {
                           "PROTOCOL_ID":"35435",
                           "ACTION_ID": "454",
                           "NEXT_GROUP_ACTION_ID":"564646",
                           "PREVIOUS_GROUP_ACTION_ID":"5675",
                           "COMMENTS":"final protocol"
                       },
                       {
                           "PROTOCOL_ID":"35444",
                           "ACTION_ID": "459",
                           "NEXT_GROUP_ACTION_ID":"564645",
                           "PREVIOUS_GROUP_ACTION_ID":"565",
                           "COMMENTS":"Initial protocol"
                       },
                       {
                           "PROTOCOL_ID":"35447",
                           "ACTION_ID": "458",
                           "NEXT_GROUP_ACTION_ID":"56477",
                           "PREVIOUS_GROUP_ACTION_ID":"567",
                           "COMMENTS":"submiotetd protocol"
                       }];
  
  
  isExpanded: boolean[] = [];
  requestObject = {    
          protocol_number: '',
          protocol_id : '',
          action_id : '',
          next_group_action_id : '',
          previous_group_action_id : ''
  };
  result: any;
  indexVal : number;
  constructor( private _irbViewService: IrbViewService ) { }

  ngOnInit() {
      this.loadHistoryList();
      this.isExpanded.length = this.irbHistoryList.length;
  }
  
  loadHistoryList(){
        this._irbViewService.getProtocolHistotyGroupList( this.requestObject ).subscribe( data => {
            this.result = data || [];
            if ( this.result != null ) {
                if (this.result.irbviewProtocolPersons == null || this.result.irbviewProtocolPersons.length == 0) {
                    this.noHistoryList = true;
                }
                else{
                   // this.irbHistoryList = this.result.irbviewProtocolPersons;
                }
            }
            
        },
            error => {
                 console.log( error );
            },
        );
    }
  
  loadHistoryDetails(){
      this._irbViewService.getProtocolHistotyGroupDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolPersons == null || this.result.irbviewProtocolPersons.length == 0) {
                  this.noHistoryDetails = true;
              }
              else{
                 // this.irbHistoryDetails = this.result.irbviewProtocolPersons;
              }
          }
          
      },
          error => {
               console.log( error );
          },
      );
  }

  toggle(index) { 
  for( this.indexVal = 0; this.indexVal<this.isExpanded.length; this.indexVal++){
          if(this.indexVal==index) {
              this.isExpanded[this.indexVal] = !this.isExpanded[this.indexVal];
          }
          else {
              this.isExpanded[this.indexVal] = false;
          }
      }
  this.requestObject.protocol_id = this.irbHistoryList[index].PROTOCOL_ID;
  this.requestObject.action_id = this.irbHistoryList[index].ACTION_ID;
  this.requestObject.next_group_action_id = this.irbHistoryList[index].NEXT_GROUP_ACTION_ID;
  this.requestObject.previous_group_action_id = this.irbHistoryList[index].PREVIOUS_GROUP_ACTION_ID;
  this.loadHistoryDetails();
  }
}
