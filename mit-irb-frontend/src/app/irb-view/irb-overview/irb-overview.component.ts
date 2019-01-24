import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { HttpClient } from '@angular/common/http';

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

    trainingCompleted: string;
    PROTOCOL_PERSONNEL_INFO: string;
    PROTOCOL_FUNDING_SOURCE_INFO: string;
    PROTOCOL_SUBJECT_INFO: string;
    PROTOCOL_ENGAGED_INSTITUTIONS_INFO: string;
    PROTOCOL_SPECIAL_REVIEW_INFO: string;



    requestObject = {
            protocolNumber: '',
            avPersonId: ''
    };

  constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute,
                private _spinner: NgxSpinnerService, private _http: HttpClient) { }

  /** sets requestObject and calls all the functions */
  ngOnInit() {
    this._http.get('/mit-irb/resources/string_config_json').subscribe(
        data => {
            const property_config: any = data;
            if (property_config) {
                this.PROTOCOL_PERSONNEL_INFO = property_config.PROTOCOL_PERSONNEL_INFO;
                this.PROTOCOL_FUNDING_SOURCE_INFO = property_config.PROTOCOL_FUNDING_SOURCE_INFO;
                this.PROTOCOL_SUBJECT_INFO = property_config.PROTOCOL_SUBJECT_INFO;
                this.PROTOCOL_ENGAGED_INSTITUTIONS_INFO = property_config.PROTOCOL_ENGAGED_INSTITUTIONS_INFO;
                this.PROTOCOL_SPECIAL_REVIEW_INFO = property_config.PROTOCOL_SPECIAL_REVIEW_INFO;
            }
        });
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.loadPersonalDetails();
      this.loadFundingDetails();
      this.loadCollaboratorsDetails();
      this.loadSubjectsDetails();
      this.loadSpecialReviewDetails();
  }

  /**calls service to load person details of protocol */
  loadPersonalDetails() {
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
               console.log( 'Error in method loadPersonalDetails()', error );
          },
      );
  }

  /**calls service to load funding details of protocol */
  loadFundingDetails() {
    this._spinner.show();
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
               console.log( 'Error in method loadFundingDetails()', error );
          },
          () => {
              this._spinner.hide();
          }
      );
  }

  /**calls service to load collaborators details of protocol */
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
      },
          error => {
               console.log( 'Error in method loadCollaboratorsDetails()', error );
          },
      );
  }

  /**calls service to load subjects details of protocol */
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
      },
          error => {
               console.log( 'Error in method loadSubjectsDetails()', error );
          },
      );
  }

  /**calls service to load special details of protocol */
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
               console.log( 'Error in method loadSpecialReviewDetails()', error );
          },
      );
  }

  /**calls service to load person details of protocol */
  loadPersonDetailedList() {
    this.irbPersonDetailedList = [];
    this.irbPersonDetailedTraining = [];
    this._irbViewService.getIrbPersonDetailedList( this.requestObject ).subscribe( data => {
        this.result = data || [];
        if ( this.result != null ) {
            this.irbPersonDetailedList = this.result.irbViewProtocolMITKCPersonInfo;
            this.irbPersonDetailedTraining = this.result.irbViewProtocolMITKCPersonTrainingInfo;
            this.trainingCompleted = this.result.trainingStatus;
        }
    },
        error => {
             console.log( 'Error in method loadPersonDetailedList()', error );
        },
    );
}
  /**show details of selected person in a popup
   * @@param personId -unique id of each person
  */
  showpersonData( personId ) {
      this.showpersonDataList = true;
      this.requestObject.avPersonId = personId;
      this.loadPersonDetailedList();
  }

  /**clear boolean after person details modal is closed */
  closeModal() {
      this.showpersonDataList = false;
  }
}
