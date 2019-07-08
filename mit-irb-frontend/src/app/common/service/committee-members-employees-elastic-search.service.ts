import { Injectable } from '@angular/core';
import { Client} from 'elasticsearch';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CommitteeMemberEmployeeElasticService {
    private _client: Client;
    URL_FOR_ELASTIC: string;
    PERSON_INDEX: string;


    constructor( private _http: HttpClient  ) {
        // this.URL_FOR_ELASTIC = 'http://192.168.1.76:9200';
        // this.PERSON_INDEX = 'mitperson';
        // if ( !this._client ) {
        //     this._connect();
        // }
                this.get_elastic_config().subscribe(
          data => {
               const elastic_config: any = data;
               if (elastic_config) {
                   this.URL_FOR_ELASTIC = elastic_config.URL_FOR_ELASTIC;
                   this.PERSON_INDEX = elastic_config.PERSON_INDEX;
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
                index: this.PERSON_INDEX,
                size: 20,
                type: 'person',
                body: {
                    query: {
                        bool: {
                            should: [
                                {
                                    match: {
                                        prncpl_id: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        full_name: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        prncpl_nm: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                                {
                                    match: {
                                        email_addr: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                              {
                                    match: {
                                        unit_number: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                            {
                                    match: {
                                        unit_name: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                          {
                                    match: {
                                        addr_line_1: {
                                            query: value,
                                            operator: 'or'
                                        }
                                    }
                                },
                          {
                                    match: {
                                       phone_nbr: {
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
                            prncpl_id: {},
                            full_name: {},
                            prncpl_nm: {},
                            email_addr: {},
                              unit_number: {},
                                unit_name: {},
                                  addr_line_1: {},
                                    phone_nbr: {},
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
        return this._http.get('resources/elastic_config_json');
    }
}
