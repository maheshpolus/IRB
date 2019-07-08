import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { HttpClient } from '@angular/common/http';
import { ISubscription } from 'rxjs/Subscription';

import { IrbViewService } from '../irb-view.service';
import { SharedDataService } from '../../common/service/shared-data.service';
@Component({
  selector: 'app-irb-overview',
  templateUrl: './irb-overview.component.html',
  styleUrls: ['./irb-overview.component.css']
})

export class IrbOverviewComponent implements OnInit, OnDestroy {

    showpersonDataList = false;
    noIrbPersons = false;
    noIrbfunds = false;
    noIrbCollaborators = false;
    noIrbSpecialReviews = false;
    noIrbSubjects = false;
    isExpanded = true;
    noProtocolUnits = false;
    noAdminContacts = false;

    irbPersonDetailedTraining: any[] = [];
    irbPersonsDetails: any[] = [];
    irbFundingDetails: any[] = [];
    irbCollaborators: any[] = [];
    irbSubjects: any[] = [];
    irbSpecialReview: any[] = [];
    irbProtocolUnitList: any = [];
    irbAdminContactList: any = [];
    irbViewProtocolCollaboratorAttachments: any = [];
    irbViewProtocolCollaboratorPersons: any = [];
    trainingAttachments: any = [];
    trainingComments: any = [];
    irbPersonDetailedList: any;
    result: any;
    irbHeaderDetails: any = {};

    trainingCompleted: string;
    PROTOCOL_PERSONNEL_INFO: string;
    PROTOCOL_FUNDING_SOURCE_INFO: string;
    PROTOCOL_SUBJECT_INFO: string;
    PROTOCOL_ENGAGED_INSTITUTIONS_INFO: string;
    PROTOCOL_SPECIAL_REVIEW_INFO: string;

    attachmentIconValue: number;
    commentIconValue: number;

    protocolCollaboratorSelected: any = {};

    requestObject = {
            protocolNumber: '',
            avPersonId: ''
    };
    private $subscription1: ISubscription;

  constructor( private _irbViewService: IrbViewService, private _activatedRoute: ActivatedRoute,
                private _spinner: NgxSpinnerService, private _http: HttpClient, private _sharedDataService: SharedDataService) { }

  /** sets requestObject and calls all the functions */
  ngOnInit() {
    this._http.get('/irb/resources/string_config_json').subscribe(
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
        this.$subscription1 = this._sharedDataService.viewProtocolDetailsVariable.subscribe(data => {
            if (data !== undefined && data != null) {
                this.irbHeaderDetails = data;
            }
        });
      this.requestObject.protocolNumber = this._activatedRoute.snapshot.queryParamMap.get('protocolNumber');
      this.loadPersonalDetails();
      this.loadFundingDetails();
      this.loadCollaboratorsDetails();
      this.loadSubjectsDetails();
      this.loadSpecialReviewDetails();
      this.loadAdminstrativeContactDetails();
      this.loadProtocolUnitDetails();
  }

  ngOnDestroy() {
    if (this.$subscription1) {
      this.$subscription1.unsubscribe();
    }
}

  /**calls service to load person details of protocol */
  loadPersonalDetails() {
      this._spinner.show();
    this._irbViewService.getIrbPersonalDetails( this.requestObject ).subscribe( data => {
        this._spinner.hide();
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
  loadProtocolUnitDetails() {
    this._irbViewService.getIRBprotocolUnits( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbViewProtocolUnits == null || this.result.irbViewProtocolUnits.length === 0) {
                  this.noProtocolUnits  = true;
              } else {
                  this.irbProtocolUnitList = this.result.irbViewProtocolUnits;
              }
          }
      },
          error => {
               console.log( 'Error in method loadPersonalDetails()', error );
          },
      );
  }

  loadAdminstrativeContactDetails() {
    this._irbViewService.getIRBprotocolAdminContact( this.requestObject ).subscribe( data => {
          this.result = data || [];
          if ( this.result != null ) {
              if (this.result.irbViewProtocolAdminContact == null || this.result.irbViewProtocolAdminContact.length === 0) {
                  this.noAdminContacts = true;
              } else {
                  this.irbAdminContactList = this.result.irbViewProtocolAdminContact;
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
    // this._spinner.show();
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
            //   this._spinner.hide();
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
    this.trainingAttachments = [];
    this.trainingComments = [];
    this.attachmentIconValue = -1;
    this.commentIconValue = -1;
    this._spinner.show();
    this._irbViewService.getIrbPersonDetailedList( this.requestObject ).subscribe( data => {
        this._spinner.hide();
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
  openCollaboratordetails(collaborator) {
      this.protocolCollaboratorSelected = collaborator;
      this.result.protocolLocationId = collaborator.PROTOCOL_LOCATION_ID;
      this._irbViewService.getIRBprotocolCollaboratorDetails(this.result).subscribe(data => {
        this.result = data || [];
        if (this.result != null) {
            this.irbViewProtocolCollaboratorAttachments = this.result.irbViewProtocolCollaboratorAttachments != null ?
            this.result.irbViewProtocolCollaboratorAttachments : [];
            this.irbViewProtocolCollaboratorPersons = this.result.irbViewProtocolCollaboratorPersons != null ?
            this.result.irbViewProtocolCollaboratorPersons : [];
        }
      });
  }
  downloadCollaboratorAttachment(attachment) {
    this._irbViewService.downloadIrbAttachment(attachment.FILE_ID).subscribe( data => {
        const a = document.createElement( 'a' );
            const blob = new Blob( [data], { type: data.type } );
            a.href = URL.createObjectURL( blob );
            a.download = attachment.FILE_NAME;
            document.body.appendChild(a);
            a.click();
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
  this._irbViewService.downloadTrainingAttachment(attachment.FILE_DATA_ID).subscribe(data => {
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
