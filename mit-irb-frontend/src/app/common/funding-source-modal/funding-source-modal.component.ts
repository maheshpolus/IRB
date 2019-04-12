import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';

import { ElasticService } from '../../common/service/elastic.service';
import { FundingSourceModalService } from './funding-source-modal.service';
import { KeyPressEvent } from '../directives/keyPressEvent.component';

@Component({
  selector: 'app-funding-source-modal',
  templateUrl: './funding-source-modal.component.html',
  styleUrls: ['./funding-source-modal.component.css']
})
export class FundingSourceModalComponent implements OnInit {
  @Input() fundingSourceExemptStudy: any;
  @Input() userDTO: any;
  options: any = {};
  result: any = {};
  exemptFundingSource: any = {};
  sponsorDetails = {};
  fundingSourceTypes = [];
  fundingSourceList = [];
  sponsorSearchResult = [];
  homeUnitSearchResult = [];
  elasticPlaceHolder: string;
  clearField: any = 'true';
  unitSearchString: string;
  isSponsorFundingSearch = false;
  isHomeUnitSearch = false;
  isEditFunding = false;
  showElasticBand = false;
  isEditMode = false;
  invalidData = false;
  fundingEditIndex = null;

  constructor(private _elasticsearchService: ElasticService,
    public keyPressEvent: KeyPressEvent,
    private _fundingSourceModalService: FundingSourceModalService,
    public activeModal: NgbActiveModal, private _http: HttpClient) {
    this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
    this.options.index = this._elasticsearchService.AWARD_INDEX;
    this.options.type = 'award';
    this.options.size = 20;
    this.options.contextField = 'sponsor_name';
    this.options.debounceTime = 500;
    this.options.theme = '#a31f34';
    this.options.width = '150%';
    this.options.fontSize = '16px';
    this.options.defaultValue = '';
    this.options.formatString =
      'award_number : account_number | sponsor_name | pi_name | sponsor_award_id | title | lead_unit_number | lead_unit_name';
    this.options.fields = {
      award_number: {},
      sponsor_name: {},
      pi_name: {},
      account_number: {},
      lead_unit_number: {},
      lead_unit_name: {},
      sponsor_award_id: {},
      title: {}
    };
    this.elasticPlaceHolder = 'Search for Award';
  }

  ngOnInit() {
    const todate = new Date();
    const endDate = new Date(this.fundingSourceExemptStudy.exemptProtocolEndDate);
    if ((this.fundingSourceExemptStudy.createdUser === this.userDTO.personID || this.userDTO.role === 'DEPT_ADMIN' ||
      this.fundingSourceExemptStudy.loggedInUserFacultySponsor === true || this.fundingSourceExemptStudy.loggedInUserPI === true)
      && todate <= endDate ) {
      this.isEditMode = true;
    } else {
      this.isEditMode = false;
    }
    this._fundingSourceModalService.loadFundingSourceDetails(this.fundingSourceExemptStudy.exemptFormID).subscribe(
      data => {
        this.result = data;
        this.fundingSourceTypes = this.result.protocolFundingSourceTypes;
        this.fundingSourceList = this.result.exemptFundingSourceList != null ? this.result.exemptFundingSourceList : [];
        this.exemptFundingSource = this.result.exemptFundingSource;
      });
  }

  /**
   * @param  {} fundingTypeCode changes the elastic filed based on type
   */
  changeFundingType(fundingTypeCode) {
    this.exemptFundingSource.fundingSource = null;
    this.exemptFundingSource.fundingSourceName = null;
    this.sponsorDetails = {};
    this.showElasticBand = false;
    // tslint:disable-next-line:no-construct
    this.clearField = new String('true');
    this.options.defaultValue = '';
    if (fundingTypeCode === '4') { // Development Proposal
      this.options.index = this._elasticsearchService.DP_INDEX + '/';
      this.options.type = 'devproposal';
      this.options.contextField = 'sponsor';
      this.options.formatString =
        'proposal_number : sponsor | person_name | lead_unit_number | lead_unit_name | status | title';
      this.options.fields = {
        proposal_number: {},
        sponsor: {},
        lead_unit_number: {},
        lead_unit_name: {},
        sponsor_award_id: {},
        status: {},
        title: {},
        person_name: {}
      };
      this.elasticPlaceHolder = 'Search for Development Proposal';
    } else if (fundingTypeCode === '5') { // Institute Proposal
      this.options.index = this._elasticsearchService.IP_INDEX + '/';
      this.options.type = 'proposal';
      this.options.contextField = 'sponsor_name';
      this.options.formatString =
        'proposal_number : sponsor_name | pi_name | lead_unit_number | lead_unit_name | status | title';
      this.options.fields = {
        proposal_number: {},
        sponsor_name: {},
        lead_unit_number: {},
        lead_unit_name: {},
        sponsor_award_id: {},
        status: {},
        title: {},
        pi_name: {}
      };
      this.elasticPlaceHolder = 'Search for Institute Proposal';
    } else if (fundingTypeCode === '6') { // Award
      this.options.url = this._elasticsearchService.URL_FOR_ELASTIC + '/';
      this.options.index = this._elasticsearchService.AWARD_INDEX;
      this.options.type = 'award';
      this.options.contextField = 'sponsor_name';
      this.options.formatString =
        'award_number : account_number | sponsor_name | pi_name | sponsor_award_id | title | lead_unit_number | lead_unit_name';
      this.options.fields = {
        award_number: {},
        sponsor_name: {},
        pi_name: {},
        account_number: {},
        lead_unit_number: {},
        lead_unit_name: {},
        sponsor_award_id: {},
        title: {}
      };
      this.elasticPlaceHolder = 'Search for Award';
    }
  }

  getSponsorList() {
    this.exemptFundingSource.fundingSource = null;
    this.result.searchString = this.exemptFundingSource.fundingSourceName;
    this._fundingSourceModalService.loadSponsorTypes(this.result).subscribe(
      (data: any) => {
        this.sponsorSearchResult = data.sponsorSearchResult;
      });
  }

  getHomeUnitList() {
    this.exemptFundingSource.fundingSource = null;
    this.unitSearchString = this.exemptFundingSource.fundingSourceName;
    this._fundingSourceModalService.loadHomeUnits(this.unitSearchString).subscribe(
      (data: any) => {
        this.homeUnitSearchResult = data.homeUnits;
      });
  }
  /**
   * @param  {} sponsorCode used to set sponsor details when type of funding source is 'Sponsor'
   * @param  {} sponsorName
   */
  selectedSponsor(sponsorCode, sponsorName) {
    this.exemptFundingSource.fundingSource = sponsorCode;
    this.exemptFundingSource.fundingSourceName = sponsorName;
    this.isSponsorFundingSearch = false;
  }
  /**
   * @param  {} unitNumber used to set sponsor details when type of funding source is 'Unit'
   * @param  {} unitName
   */
  selectedHomeUnit(unitNumber, unitName) {
    this.exemptFundingSource.fundingSource = unitNumber;
    this.exemptFundingSource.fundingSourceName = unitName;
    this.isHomeUnitSearch = false;
  }
  /**
   * @param  {} result used to set sponsor details when type of funding source is 'Award, IP, DP'
   */
  selectFundingElasticResult(result) {
    this.showElasticBand = result != null ? true : false;
    this.sponsorDetails = result;
    if (this.exemptFundingSource.fundingSourceTypeCode === '4') {// DP
      this.exemptFundingSource.fundingSource = result != null ? result.proposal_number : null;
      this.exemptFundingSource.fundingSourceName = result != null ? result.sponsor : null;
    } else if (this.exemptFundingSource.fundingSourceTypeCode === '5') {// IP
      this.exemptFundingSource.fundingSource = result != null ? result.proposal_number : null;
      this.exemptFundingSource.fundingSourceName = result != null ? result.sponsor_name : null;
    } else if (this.exemptFundingSource.fundingSourceTypeCode === '6') {// Award
      this.exemptFundingSource.fundingSource = result != null ? result.award_number : null;
      this.exemptFundingSource.fundingSourceName = result != null ? result.sponsor_name : null;
    }
  }

  /**
   * @param  {} mode used to add or edit funding details
   */
  addEditFundingDetails(mode) {
    if (this.exemptFundingSource.fundingSourceTypeCode != null && this.exemptFundingSource.fundingSource != null) {
      this.invalidData = false;
      this.exemptFundingSource.acType = mode;
      this.exemptFundingSource.irbExemptFormId = this.fundingSourceExemptStudy.exemptFormID;
      this.result.exemptFundingSource = this.exemptFundingSource;
      this.result.updateUser = this.userDTO.userName;
      if (this.exemptFundingSource.fundingSourceTypeCode === '3') {// 'OTHERS' setting the source to sourceNAme field of POJO
        this.exemptFundingSource.fundingSourceName = this.exemptFundingSource.fundingSource;
        this.exemptFundingSource.fundingSource = null;
      }
      this._fundingSourceModalService.updateExemptFundingSource(this.result).subscribe(
        (data: any) => {
          this.fundingSourceList = data.exemptFundingSourceList;
          this.exemptFundingSource = {};
          this.exemptFundingSource.fundingSourceTypeCode = null;
          this.isEditFunding = false;
          this.showElasticBand = false;
          this.fundingEditIndex = null;
          this.options.editHighlight = false;
        });
    } else {
      this.invalidData = true;
    }

  }
  /**
   * @param  {} item setting edit selected object to fields
   * @param  {} index
   */
  editFundingDetails(item, index) {
    this.exemptFundingSource.acType = 'U';
    this.exemptFundingSource.fundingSourceTypeCode = item.FUNDING_SOURCE_TYPE_CODE;
    this.changeFundingType(item.FUNDING_SOURCE_TYPE_CODE);
    this.exemptFundingSource.description = item.DESCRIPTION;
    this.exemptFundingSource.exemptFundingSourceId = item.EXEMPT_FUNDING_SOURCE_ID;
    this.exemptFundingSource.fundingSource = item.FUNDING_SOURCE;
    this.exemptFundingSource.fundingSourceName = item.FUNDING_SOURCE_NAME;
    this.exemptFundingSource.irbExemptFormId = item.IRB_EXEMPT_FORM_ID;
    if (item.FUNDING_SOURCE_TYPE_CODE === '4' || item.FUNDING_SOURCE_TYPE_CODE === '5' || item.FUNDING_SOURCE_TYPE_CODE === '6') {
      this.options.defaultValue = item.FUNDING_SOURCE_NAME;
      this.options.editHighlight = true;
    }
    this.isEditFunding = true;
    this.fundingEditIndex = index;
  }
  /**
   * @param  {} item delete funding detail
   */
  deleteFundingDetails(item) {
    this.result.exemptFundingSource.acType = 'D';
    this.result.exemptFundingSource.exemptFundingSourceId = item.EXEMPT_FUNDING_SOURCE_ID;
    this.result.exemptFundingSource.irbExemptFormId = item.IRB_EXEMPT_FORM_ID;
    this.result.updateUser = this.userDTO.userName;
    if (this.exemptFundingSource.exemptFundingSourceId != null) { // Clearing input fields if any funding source was selected for edit
      this.exemptFundingSource = {};
      this.exemptFundingSource.fundingSourceTypeCode = null;
      this.isEditFunding = false;
      this.showElasticBand = false;
      this.fundingEditIndex = null;
      this.options.editHighlight = false;
    }
    this._fundingSourceModalService.updateExemptFundingSource(this.result).subscribe(
      (data: any) => {
        this.fundingSourceList = data.exemptFundingSourceList != null ? data.exemptFundingSourceList : [];
      });
  }
  /**
   * @param  {} item open Award,IP,DP in KC
   */
  openKcLink(item) {
    let BASE_URL_KC = null;
    let URL_FOR_KC_FRAME = null;
    let URL_FOR_KC_AWARD = null;
    let URL_FOR_KC_IP = null;
    // LOCAL BUILD
    // BASE_URL_KC = 'http://192.168.1.139:8080/kc-dev';
    // URL_FOR_KC_FRAME = '/kc-krad/landingPage?viewId=Kc-Header-IframeView&href=';
    // URL_FOR_KC_AWARD = '/awardHome.do?methodToCall=docHandler&placeHolderAwardId={awardId}&docId={docId}&command=displayDocSearchView&businessObjectClassName=org.kuali.kra.award.home.Award&docFormKey=88888888&__login_user={user}&kcComp=true';
    // URL_FOR_KC_IP = '/institutionalProposalHome.do?viewDocument=true&docId={docId}&docTypeName=InstitutionalProposalDocument&methodToCall=docHandler&docOpenedfromIPSearch=true&command=displayDocSearchView&formKey=ed9b8b81-209b-4ec9-b2bb-4f401f3edcf0&__login_user={user}&kcComp=true';
    // if (item.FUNDING_SOURCE_TYPE_CODE === '6') {
    //   URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{awardId}', item.MODULE_ITEM_KEY);
    //   URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{docId}', item.DOCUMENT_NUMBER);
    //   URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{user}', this.userDTO.userName);

    //   const url_KCAward = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_AWARD;
    //   const win = window.open('about:blank', '_blank');
    //   win.location.href = url_KCAward;
    // } else if (item.FUNDING_SOURCE_TYPE_CODE === '5' || item.FUNDING_SOURCE_TYPE_CODE === '4') {
    //   URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{docId}', item.DOCUMENT_NUMBER);
    //   URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{user}', this.userDTO.userName);

    //   const url_KCIp = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_IP;
    //   const win = window.open('about:blank', '_blank');
    //   win.location.href = url_KCIp;
    // }
    // PRODUCTION BUILD
    this.get_externalLinks_config().subscribe(data => {
      const externalLinks_config: any = data;
      BASE_URL_KC = externalLinks_config.KC_BASE_URL;
      URL_FOR_KC_FRAME = externalLinks_config.URL_FOR_KC_FRAME;
      URL_FOR_KC_AWARD = externalLinks_config.URL_FOR_KC_AWARD;
      URL_FOR_KC_IP = externalLinks_config.URL_FOR_KC_IP;

      if (item.FUNDING_SOURCE_TYPE_CODE === '6') {
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{awardId}', item.MODULE_ITEM_KEY);
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{docId}', item.DOCUMENT_NUMBER);
        URL_FOR_KC_AWARD = URL_FOR_KC_AWARD.replace('{user}', this.userDTO.userName);

        const url_KCAward = BASE_URL_KC + URL_FOR_KC_FRAME + BASE_URL_KC + URL_FOR_KC_AWARD;
        const win = window.open('about:blank', '_blank');
        win.location.href = url_KCAward;
      } else if (item.FUNDING_SOURCE_TYPE_CODE === '5' || item.FUNDING_SOURCE_TYPE_CODE === '4') {
        URL_FOR_KC_IP = URL_FOR_KC_IP.replace('{docId}', item.DOCUMENT_NUMBER);
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
}
