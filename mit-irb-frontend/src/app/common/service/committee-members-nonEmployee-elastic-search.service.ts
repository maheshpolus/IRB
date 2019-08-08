import { Injectable } from '@angular/core';
// import { Observable } from 'rxjs';
import { Client, SearchResponse } from 'elasticsearch';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CommitteeMemberNonEmployeeElasticService {
    private _client: Client;
    NON_EMPLOYEE_INDEX: string;
    URL_FOR_ELASTIC: string;

    constructor( private _http: HttpClient  ) {
        // this.URL_FOR_ELASTIC = 'http://192.168.1.76:9200';
        // this.NON_EMPLOYEE_INDEX = 'mitrolodex';
        // if ( !this._client ) {
        //     this._connect();
        // }

           this.get_elastic_config().subscribe(
          data => {
               const elastic_config: any = data;
               if (elastic_config) {
                   this.URL_FOR_ELASTIC = elastic_config.URL_FOR_ELASTIC;
                   this.NON_EMPLOYEE_INDEX = elastic_config.NON_EMPLOYEE_INDEX;
              if ( !this._client ) {
                       this._connect();
                  }
              }
           }
       );
    }

    private _connect() {
        this._client = new Client( {
            host: this.URL_FOR_ELASTIC
        } );
    }

    search( value ): any {
        if ( value ) {
            return this._client.search( {
                index: this.NON_EMPLOYEE_INDEX,
                size: 20,
                type: 'rolodex',
                body: {
                    query: {
                        bool: {
                            should: [

                                {
                                    match: {
                                        first_name: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        middle_name: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        last_name: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        organization: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                }]
                        }
                    },
                    sort: [{
                        _score: {
                            order: 'desc'
                        }
                    }],
                    highlight: {
                        pre_tags: ['<b>'],
                        post_tags: ['</b>'],
                        fields: {
                            rolodex_id: {},
                            first_name: {},
                            middle_name: {},
                            last_name: {},
                            organization: {},
                        }
                    }
                }
            } );
        } else {
            return Promise.resolve( {} );
        }
    }

    addToIndex( value ): any {
        return this._client.create( value );
    }

    isAvailable(): any {
        return this._client.ping( {
            requestTimeout: Infinity,
            hello: 'elasticsearch!'
        } );
    }
    get_elastic_config() {
        return this._http.get('mit-irb/resources/elastic_config_json');
    }
}
