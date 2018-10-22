import { Component, OnInit, AfterViewInit, NgZone, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { ISubscription } from 'rxjs/Subscription';
import { CompleterService, CompleterData } from 'ng2-completer';
import { HttpClient } from '@angular/common/http';

import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';

import { IrbCreateService } from '../../irb-create.service';
import { ElasticService } from '../../../common/service/elastic.service';
import { SharedDataService } from '../../../common/service/shared-data.service';

@Component({
  selector: 'app-funding-source',
  templateUrl: './funding-source.component.html',
  styleUrls: ['./funding-source.component.css']
})
export class FundingSourceComponent implements OnInit, AfterViewInit, OnDestroy {

  elasticSearchText: FormControl = new FormControl('');
  message = '';
  IsElasticResultPerson = false;
  _results: Subject<Array<any>> = new Subject<Array<any>>();
  userDTO: any = {};
  protocolNumber = null;
  protocolId = null;
  isGeneralInfoSaved = false;
  showElasticBand = false;
  commonVo: any = {};
  generalInfo: any = {};
  sourceType: any = {};
  result: any = {};
  sponsorDetails: any = {};
  protocolFundingSourceTypes = [];
  sponsorList: CompleterData;
  protocolPersonLeadUnits: CompleterData;
  fundingSource: any = {};
  isFundingInfoEdit = false;
  fundingEditIndex = null;
  protocolFundingSourceList = [];
  invalidData = {
    invalidGeneralInfo: false, invalidStartDate: false, invalidEndDate: false,
    invalidPersonnelInfo: false, invalidFundingInfo: false, invalidSubjectInfo: false,
    invalidCollaboratorInfo: false, invalidApprovalDate: false, invalidExpirationDate: false
  };

  private subscription1: ISubscription;
  constructor(private _activatedRoute: ActivatedRoute,
    private _sharedDataService: SharedDataService,
    private _irbCreateService: IrbCreateService,
    private _ngZone: NgZone,
    private _elasticsearchService: ElasticService,
    private _completerService: CompleterService,
    private _http: HttpClient) { }

  ngOnInit() {
    this.userDTO = this._activatedRoute.snapshot.data['irb'];
    this._activatedRoute.queryParams.subscribe(params => {
      this.protocolId = params['protocolId'];
      this.protocolNumber = params['protocolNumber'];
      if (this.protocolId != null && this.protocolNumber != null) {
        this.isGeneralInfoSaved = true;
      }
    });

    this.subscription1 = this._sharedDataService.CommonVoVariable.subscribe(commonVo => {
      if (commonVo !== undefined && commonVo.generalInfo !== undefined && commonVo.generalInfo !== null) {
        this.commonVo = commonVo;
        this.loadEditDetails();
      }
    });
  }

  ngOnDestroy() {
    if (this.subscription1) {
      this.subscription1.unsubscribe();
    }
  }

  ngAfterViewInit() {
    this.elasticSearchText
      .valueChanges
      .map((text: any) => text ? text.trim() : '')
      .do(searchString => searchString ? this.message = 'searching...' : this.message = '')
      .debounceTime(500)
      .distinctUntilChanged()
      .switchMap(searchString => {
        return this.getElasticSearchResults(searchString);
      })
      .catch(this.handleError)
      .subscribe(this._results);
  }

  loadEditDetails() {
    this.protocolFundingSourceTypes = this.commonVo.protocolFundingSourceTypes;
    this.generalInfo = this.commonVo.generalInfo;
    this.fundingSource = this.commonVo.fundingSource;
    this.protocolFundingSourceList = this.commonVo.protocolFundingSourceList != null ? this.commonVo.protocolFundingSourceList : [];
    this.sponsorList = this._completerService.local(
      this.commonVo.sponsorType, 'sponsorName', 'sponsorName');
    this.protocolPersonLeadUnits = this._completerService.local(
      this.commonVo.protocolPersonLeadUnits, 'unitName,unitNumber', 'unitName');
  }

  setFundingSourceType(typeCode) {
    this.sourceType.typeCode = typeCode;
    this.fundingSource.fundingSource = null;
    this.fundingSource.sourceName = null;
    if (typeCode === '6') {
      this.sourceType.placeholder = 'Search Award';
    } else if (typeCode === '4') {
      this.sourceType.placeholder = 'Search Development Proposal';
    } else if (typeCode === '5') {
      this.sourceType.placeholder = 'Search Institute Proposal';
    } else if (typeCode === '1') {
      this.sourceType.placeholder = 'Search Sponsor';
    } else if (typeCode === '2') {
      this.sourceType.placeholder = 'Search Department';
    } else {
      this.sourceType.placeholder = null;
    }
    this.protocolFundingSourceTypes.forEach(SourceType => {
      if (SourceType.fundingSourceTypeCode === typeCode) {
        this.fundingSource.protocolFundingSourceTypes = { fundingSourceTypeCode: typeCode, description: SourceType.description };
      }
    });
  }

  /**fetches elastic search results
       * @param searchString - string enterd in the input field
       */
  getElasticSearchResults(searchString) {
    return new Promise<Array<String>>((resolve, reject) => {
      this._ngZone.runOutsideAngular(() => {
        if (this.sourceType.typeCode === '6') {
          this.awardElasticSearch(resolve, reject, searchString);
        } else if (this.sourceType.typeCode === '4' || this.sourceType.typeCode === '5') {
          this.proposalElasticSearch(resolve, reject, searchString, this.sourceType.typeCode);
        }
      });
    });
  }

  awardElasticSearch(resolve, reject, searchString) {
    let hits_source: Array<any> = [];
    let hits_highlight: Array<any> = [];
    const results: Array<any> = [];
    let test;
    this._elasticsearchService.awardSearch(searchString)
      .then((searchResult) => {
        this._ngZone.run(() => {
          hits_source = ((searchResult.hits || {}).hits || [])
            .map((hit) => hit._source);
          hits_highlight = ((searchResult.hits || {}).hits || [])
            .map((hit) => hit.highlight);

          hits_source.forEach((elmnt, j) => {
            let awardNumber: string = hits_source[j].award_number;
            let title: string = hits_source[j].title;
            let account_number: string = hits_source[j].account_number;
            let sponsor_name: string = hits_source[j].sponsor_name;
            let sponsor_award_id: string = hits_source[j].sponsor_award_id;
            let pi_name: string = hits_source[j].pi_name;
            let lead_unit_name: string = hits_source[j].lead_unit_name;
            let lead_unit_number: string = hits_source[j].lead_unit_number;
            test = hits_source[j];

            if (typeof (hits_highlight[j].award_number) !== 'undefined') {
              awardNumber = hits_highlight[j].award_number;
            }
            if (typeof (hits_highlight[j].title) !== 'undefined') {
              title = hits_highlight[j].title;
            }
            if (typeof (hits_highlight[j].account_number) !== 'undefined') {
              account_number = hits_highlight[j].account_number;
            }
            if (typeof (hits_highlight[j].sponsor_name) !== 'undefined') {
              sponsor_name = hits_highlight[j].sponsor_name;
            }
            if (typeof (hits_highlight[j].sponsor_award_id) !== 'undefined') {
              sponsor_award_id = hits_highlight[j].sponsor_award_id;
            }
            if (typeof (hits_highlight[j].pi_name) !== 'undefined') {
              pi_name = hits_highlight[j].pi_name;
            }
            if (typeof (hits_highlight[j].lead_unit_name) !== 'undefined') {
              lead_unit_name = hits_highlight[j].lead_unit_name;
            }
            if (typeof (hits_highlight[j].lead_unit_number) !== 'undefined') {
              lead_unit_number = hits_highlight[j].lead_unit_number;
            }
            results.push({
              label: awardNumber + '  :  ' + account_number
                + '  |  ' + sponsor_name
                + '  |  ' + pi_name
                + '  |  ' + sponsor_award_id
                + '  |  ' + title + '  |  '
                + lead_unit_number + '  |  '
                + lead_unit_name,
              obj: test
            });
          });
          if (results.length > 0) {
            this.message = '';
          } else {
            // if (this.personnelInfo.personName && this.personnelInfo.personName.trim()) {
            //   this.message = 'nothing was found';
            // }
          }
          resolve(results);
        });

      })
      .catch((error) => {
        this._ngZone.run(() => {
          reject(error);
        });
      });
  }

  proposalElasticSearch(resolve, reject, searchString, typeCode) {
    let hits_source: Array<any> = [];
    let hits_highlight: Array<any> = [];
    const results: Array<any> = [];
    let test;
    this._elasticsearchService.searchIP(searchString, typeCode)
      .then((searchResult) => {
        this._ngZone.run(() => {

          hits_source = ((searchResult.hits || {}).hits || [])
            .map((hit) => hit._source);
          hits_highlight = ((searchResult.hits || {}).hits || [])
            .map((hit) => hit.highlight);
          hits_source.forEach((elmnt, j) => {
            let proposal_number = hits_source[j].proposal_number;
            const proposalNumberVal = proposal_number;
            let sponsor_name: string = hits_source[j].sponsor_name;
            let sponsor_proposal_id: string = hits_source[j].sponsor_award_id;
            let pi_name = hits_source[j].pi_name;
            let lead_unit_number = hits_source[j].lead_unit_number;
            let lead_unit_name = hits_source[j].lead_unit_name;
            let status = hits_source[j].status;
            let title = hits_source[j].title;
            test = hits_source[j];
            if (typeof (hits_highlight[j].proposal_number) !== 'undefined') {
              proposal_number = hits_highlight[j].proposal_number;
            }
            if (typeof (hits_highlight[j].sponsor_name) !== 'undefined') {
              sponsor_name = hits_highlight[j].sponsor_name;
            }
            if (typeof (hits_highlight[j].sponsor_award_id) !== 'undefined') {
              sponsor_proposal_id = hits_highlight[j].sponsor_award_id;
            }
            if (typeof (hits_highlight[j].pi_name) !== 'undefined') {
              pi_name = hits_highlight[j].pi_name;
            }
            if (typeof (hits_highlight[j].lead_unit_number) !== 'undefined') {
              lead_unit_number = hits_highlight[j].lead_unit_number;
            }
            if (typeof (hits_highlight[j].lead_unit_name) !== 'undefined') {
              lead_unit_name = hits_highlight[j].lead_unit_name;
            }
            if (typeof (hits_highlight[j].status) !== 'undefined') {
              status = hits_highlight[j].status;
            }
            if (typeof (hits_highlight[j].title) !== 'undefined') {
              title = hits_highlight[j].title;
            }
            if (typeof (hits_highlight[j].status) !== 'undefined') {
              status = hits_highlight[j].status;
            }

            results.push({
              label: proposal_number + ' | ' + sponsor_name + '  |  ' + sponsor_proposal_id + ' | ' + pi_name + ' | '
                + lead_unit_number + ' : ' + lead_unit_name + ' | ' + status + ' | ' + title,
              value: proposalNumberVal, obj: test
            });

          });

          if (results.length > 0) {
            this.message = '';
          } else {
            // if (this.seachTextModel && this.seachTextModel.trim()) {
            //   this.message = 'nothing was found';
            // }
          }
          resolve(results);
        });

      })
      .catch((error) => {
        this._ngZone.run(() => {
          reject(error);
        });
      });
  }


  /**show message if elastic search failed */
  handleError(): any {
    this.message = 'something went wrong';
  }

  clearData() {
    // this.personnelInfo.personId = '';
    // this.ProtocolPI.personId = '';
  }

  /** assign values to requestObject on selecting a particular person from elastic search */
  selectedFundingSource(result, typeCode) {
    this.showElasticBand = true;
    this.sponsorDetails = result.obj;
    this.IsElasticResultPerson = false;
    if (typeCode === '6') {
      this.fundingSource.fundingSource = result.obj.award_number;
      this.fundingSource.sourceName = result.obj.sponsor_name;
    } else if (typeCode === '4') {
      this.fundingSource.fundingSource = result.obj.proposal_number;
      this.fundingSource.sourceName = result.obj.sponsor;
    } else if (typeCode === '5') {
      this.fundingSource.fundingSource = result.obj.proposal_number;
      this.fundingSource.sourceName = result.obj.sponsor_name;
    }
  }

  selectedSponsor(fundingSource) {
    this.fundingSource.fundingSource = null;
    this.commonVo.sponsorType.forEach(sponsor => {
      if (sponsor.sponsorName === fundingSource) {
        this.fundingSource.fundingSource = sponsor.sponsorCode;
        // this.fundingSource.sourceName = sponsor.sponsorName;
      }
    });
  }

  selectedDepartment(fundingSource) {
    this.fundingSource.fundingSource = null;
    this.commonVo.protocolPersonLeadUnits.forEach(unit => {
      if (unit.unitName === fundingSource) {
        this.fundingSource.fundingSource = unit.unitNumber;
        // this.fundingSource.sourceName = sponsor.sponsorName;
      }
    });
  }

  addFundingDetails(mode) {
    if (this.fundingSource.fundingSourceTypeCode != null && this.fundingSource.fundingSourceTypeCode !== undefined &&
      this.fundingSource.fundingSource != null && this.fundingSource.fundingSource !== undefined) {
      this.invalidData.invalidFundingInfo = false;
      this.showElasticBand = false;
      this.saveFundingDetails(mode);
    } else {
      this.invalidData.invalidFundingInfo = true;
    }
    if (mode === 'EDIT') {
      this.isFundingInfoEdit = false;
      this.fundingEditIndex = null;
    }
  }

  editFundingDetails(item, index) {
    this.fundingEditIndex = index;
    this.isFundingInfoEdit = true;
    this.fundingSource = Object.assign({}, item);
    this.sourceType.typeCode = this.fundingSource.fundingSourceTypeCode;
    if (this.fundingSource.fundingSourceTypeCode === '6') {
      this.sourceType.placeholder = 'Search Award';
    } else if (this.fundingSource.fundingSourceTypeCode === '4') {
      this.sourceType.placeholder = 'Search Development Proposal';
    } else if (this.fundingSource.fundingSourceTypeCode === '5') {
      this.sourceType.placeholder = 'Search Institute Proposal';
    } else if (this.fundingSource.fundingSourceTypeCode === '1') {
      this.sourceType.placeholder = 'Search Sponsor';
    } else {
      this.sourceType.placeholder = null;
    }
  }

  openKcFromElastic(item) {
    const requestObj: any = {awardId: item.awardId, docId: item.document_number,
                            fundingSourceTypeCode: this.fundingSource.fundingSourceTypeCode};
    this.openKcLink(requestObj);
  }

  openKcLink(item) {
    let BASE_URL_KC = null;
    let URL_FOR_KC_FRAME = null;
    let URL_FOR_KC_AWARD = null;
    let URL_FOR_KC_IP = null;
    // LOCAL BUILD
    /*BASE_URL_KC = 'http://192.168.1.139:8080/kc-dev';
    URL_FOR_KC_FRAME = '/kc-krad/landingPage?viewId=Kc-Header-IframeView&href=';
    URL_FOR_KC_AWARD = '/awardHome.do?methodToCall=docHandler&placeHolderAwardId={awardId}&docId={docId}&command=displayDocSearchView&businessObjectClassName=org.kuali.kra.award.home.Award&docFormKey=88888888&__login_user={user}&kcComp=true';
    URL_FOR_KC_IP = '/institutionalProposalHome.do?viewDocument=true&docId={docId}&docTypeName=InstitutionalProposalDocument&methodToCall=docHandler&docOpenedfromIPSearch=true&command=displayDocSearchView&formKey=ed9b8b81-209b-4ec9-b2bb-4f401f3edcf0&__login_user={user}&kcComp=true';
    if (item.fundingSourceTypeCode === '6') {
      URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{awardId}', item.awardId);
      URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{docId}', item.docId);
      URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{user}', this.userDTO.userName);

      const url_KCAward = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_AWARD;
      const win = window.open('about:blank', '_blank');
      win.location.href = url_KCAward;
    } else if (item.fundingSourceTypeCode === '5' || item.fundingSourceTypeCode === '4') {
      URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{docId}', item.docId);
      URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{user}', this.userDTO.userName);

      const url_KCIp = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_IP;
      const win = window.open('about:blank', '_blank');
      win.location.href = url_KCIp;
    }*/
    // PRODUCTION BUILD
    this.get_externalLinks_config().subscribe(data => {
      const externalLinks_config: any = data;
      BASE_URL_KC = externalLinks_config.KC_BASE_URL;
      URL_FOR_KC_FRAME = externalLinks_config.URL_FOR_KC_FRAME;
      URL_FOR_KC_AWARD = externalLinks_config.URL_FOR_KC_AWARD;
      URL_FOR_KC_IP = externalLinks_config.URL_FOR_KC_IP;

      if (item.fundingSourceTypeCode === '6') {
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{awardId}', item.awardId);
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{docId}', item.docId);
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{user}', this.userDTO.userName);

        const url_KCAward = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_AWARD;
        const win = window.open('about:blank', '_blank');
        win.location.href = url_KCAward;
      } else if (item.fundingSourceTypeCode === '5' || item.fundingSourceTypeCode === '4') {
        URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{docId}', item.docId);
        URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{user}', this.userDTO.userName);

        const url_KCIp = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_IP;
        const win = window.open('about:blank', '_blank');
        win.location.href = url_KCIp;
      }

    });
  }

  get_externalLinks_config() {
    return this._http.get('mit-irb/resources/elastic_config_json');
  }

  deleteFundingDetails(index) {
    this.commonVo.fundingSource = this.protocolFundingSourceList[index];
    this.commonVo.fundingSource.acType = 'D';
    this.saveFundingDetails('DELETE');
  }

  saveFundingDetails(mode) {
    if (mode !== 'DELETE') {
      this.fundingSource.updateTimestamp = new Date();
      this.fundingSource.updateUser = localStorage.getItem('userName');
      this.fundingSource.sequenceNumber = 1;
      this.fundingSource.protocolNumber = this.protocolNumber;
      this.fundingSource.protocolId = this.generalInfo.protocolId;
      this.fundingSource.acType = 'U';
      this.commonVo.fundingSource = this.fundingSource;
    }
    this._irbCreateService.updateFundingSource(this.commonVo).subscribe(
      data => {
        this.result = data;
        this.fundingSource = {};
        this.protocolFundingSourceList = this.result.protocolFundingSourceList;
      });
  }

}
