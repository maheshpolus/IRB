import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { IrbViewService } from '../irb-view.service';

@Component({
  selector: 'app-irb-overview',
  templateUrl: './irb-overview.component.html',
  styleUrls: ['./irb-overview.component.css']
})

export class IrbOverviewComponent implements OnInit {

    showpersonDataList = false;
    noIrbPersons = false;
    noIrbfunds = false;
    noIrbCollaborators = false;
    noIrbSpecialReviews = false;
    noIrbSubjects = false;
    isExpanded = true;

    irbPersonDetailedTraining = [];
    irbPersonsDetails: any[] = [];
    irbFundingDetails: any[] = [];
    irbCollaborators: any[] = [];
    irbSubjects: any[] = [];
    irbSpecialReview: any[] = [];
    irbPersonDetailedList = [];
    result: any;

    requestObject = {
            protocolNumber: '',
            avPersonId: ''
    };

  constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute,
                private _spinner: NgxSpinnerService) { }

  ngOnInit() {
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.loadPersonalDetails();
      this.loadFundingDetails();
      this.loadCollaboratorsDetails();
      this.loadSubjectsDetails();
      this.loadSpecialReviewDetails();
  }

  toggle() {
      this.isExpanded = !this.isExpanded;
  }

  loadPersonalDetails() {
    this._spinner.show();
      this._irbViewService.getIrbPersonalDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolPersons == null || this.result.irbviewProtocolPersons.length === 0) {
                  this.noIrbPersons = true;
              } else {
                  this.irbPersonsDetails = this.result.irbviewProtocolPersons;
              }
          }
      },
          error => {
               console.log( "Error in method loadPersonalDetails()", error );
          },
      );
  }

  loadFundingDetails() {
      this._irbViewService.getIrbFundingDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolFundingsource == null || this.result.irbviewProtocolFundingsource.length === 0) {
                  this.noIrbfunds = true;
              } else {
              this.irbFundingDetails = this.result.irbviewProtocolFundingsource;
              }
          }
      },
          error => {
               console.log( "Error in method loadFundingDetails()", error );
          },
      );
  }

  loadCollaboratorsDetails() {
      this._irbViewService.getIrbCollaboratorsDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolLocation == null || this.result.irbviewProtocolLocation.length === 0) {
                  this.noIrbCollaborators = true;
              } else {
                  this.irbCollaborators = this.result.irbviewProtocolLocation;
              }
          }
          this._spinner.hide();
      },
          error => {
               console.log( "Error in method loadCollaboratorsDetails()", error );
          },
      );
  }

  loadSubjectsDetails() {
      this._irbViewService.getIrbSubjectsDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolVulnerableSubject == null || this.result.irbviewProtocolVulnerableSubject.length === 0) {
                  this.noIrbSubjects = true;
              } else {
              this.irbSubjects = this.result.irbviewProtocolVulnerableSubject;
              }
          }
          this._spinner.hide();
      },
          error => {
               console.log( "Error in method loadSubjectsDetails()", error );
          },
      );
  }

  loadSpecialReviewDetails() {
      this._irbViewService.getIrbSpecialReviewDetails( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbviewProtocolSpecialReview == null || this.result.irbviewProtocolSpecialReview.length === 0) {
                  this.noIrbSpecialReviews = true;
              } else {
              this.irbSpecialReview = this.result.irbviewProtocolSpecialReview;
              }
          }
      },
          error => {
               console.log( "Error in method loadSpecialReviewDetails()", error );
          },
      );
  }

  showpersonData( personId ) {
      this.showpersonDataList = true;
      this.requestObject.avPersonId = personId;
      this.loadPersonDetailedList();
  }

  closeModal() {
      this.showpersonDataList = false;
  }

  loadPersonDetailedList() {
      this._irbViewService.getIrbPersonDetailedList( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.irbPersonDetailedList = this.result.irbviewProtocolMITKCPersonInfo;
              this.irbPersonDetailedTraining = this.result.irbviewProtocolMITKCPersonTrainingInfo;
          }
      },
          error => {
               console.log( "Error in method loadPersonDetailedList()", error );
          },
      );
  }
}
