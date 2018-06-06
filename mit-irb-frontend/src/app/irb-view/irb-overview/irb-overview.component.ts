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

    irbPersonDetailedTraining: any[] = [];
    irbPersonsDetails: any[] = [];
    irbFundingDetails: any[] = [];
    irbCollaborators: any[] = [];
    irbSubjects: any[] = [];
    irbSpecialReview: any[] = [];
    irbPersonDetailedList: any;
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
              if (this.result.irbViewProtocolPersons == null || this.result.irbViewProtocolPersons.length === 0) {
                  this.noIrbPersons = true;
              } else {
                  this.irbPersonsDetails = this.result.irbViewProtocolPersons;
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
              if (this.result.irbViewProtocolFundingsource == null || this.result.irbViewProtocolFundingsource.length === 0) {
                  this.noIrbfunds = true;
              } else {
              this.irbFundingDetails = this.result.irbViewProtocolFundingsource;
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
              if (this.result.irbViewProtocolLocation == null || this.result.irbViewProtocolLocation.length === 0) {
                  this.noIrbCollaborators = true;
              } else {
                  this.irbCollaborators = this.result.irbViewProtocolLocation;
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
              if (this.result.irbViewProtocolVulnerableSubject == null || this.result.irbViewProtocolVulnerableSubject.length === 0) {
                  this.noIrbSubjects = true;
              } else {
              this.irbSubjects = this.result.irbViewProtocolVulnerableSubject;
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
              if (this.result.irbViewProtocolSpecialReview == null || this.result.irbViewProtocolSpecialReview.length === 0) {
                  this.noIrbSpecialReviews = true;
              } else {
              this.irbSpecialReview = this.result.irbViewProtocolSpecialReview;
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
      this.irbPersonDetailedList = [];
      this.irbPersonDetailedTraining = [];
      this._irbViewService.getIrbPersonDetailedList( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              this.irbPersonDetailedList = this.result.irbViewProtocolMITKCPersonInfo;
              this.irbPersonDetailedTraining = this.result.irbViewProtocolMITKCPersonTrainingInfo;
          }
      },
          error => {
               console.log( "Error in method loadPersonDetailedList()", error );
          },
      );
  }
}
