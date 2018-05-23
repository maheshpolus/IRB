import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Client, SearchResponse } from 'elasticsearch';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ElasticService {
    private _client: Client;
    URL_FOR_ELASTIC: string;
    IRB_INDEX: string;
    constructor( private _http: HttpClient ) {
      // this.URL_FOR_ELASTIC = 'http://192.168.1.76:9200';
         this.get_elastic_config().subscribe(
            data => {
                 const elastic_config: any = data;
                 if (elastic_config) {
                     this.URL_FOR_ELASTIC = elastic_config.URL_FOR_ELASTIC;
                     this.IRB_INDEX = elastic_config.IRB_INDEX;
                     if ( !this._client ) {
                         this._connect();
                    }
                }
                 if ( !this._client ) {
                     this._connect();
                   }
             }
         );
    }

    private _connect() {
        this._client = new Client( {
            host:  this.URL_FOR_ELASTIC
        } );
    }
    get_elastic_config() {
        return this._http.get('mit-irb/resources/elastic_config_json');
    }
    irbSearch(value): any {
      if ( value ) {
        return this._client.search({
          index: this.IRB_INDEX ,
          size: 20 ,
          type: 'irbprotocol',
          body: {
              query: {
                bool: {
                  should: [
                    // {
                    //   match: {
                    //       protocol_id: {
                    //       query: value,
                    //       operator: 'or'
                    //     }
                    //   }
                    // },
                    // {
                    //   match: {
                    //       document_number: {
                    //       query: value,
                    //       operator: 'or'
                    //     }
                    //   }
                    // },
                    {
                      match: {
                          protocol_number: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
                    {
                      match: {
                          title: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
                    {
                      match: {
                          lead_unit_number: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
                    {
                        match: {
                          lead_unit_name: {
                            query: value,
                            operator: 'or'
                          }
                        }
                      },
                      {
                          match: {
                              status: {
                              query: value,
                              operator: 'or'
                              }
                          }
                      },
                      {
                          match: {
                              protocol_type: {
                              query: value,
                              operator: 'or'
                              }
                          }
                      },
                      // {
                      //     match: {
                      //         status_code: {
                      //         query: value,
                      //         operator: 'or'
                      //       }
                      //     }
                      //   },
                      // {
                      //     match: {
                      //         person_id: {
                      //             query: value,
                      //             operator: 'or'
                      //         }
                      //     }
                      // },
                      // {
                      //     match: {
                      //         person_name: {
                      //         query: value,
                      //         operator: 'or'
                      //         }
                      //     }
                      // }
                  ]
                }
              } ,
              /*filter: {
                  term: {
                      person_id: personId
                  }
                },*/
            sort: [{
                _score: {
                  order: 'desc'
                }
              }],
              highlight: {
                pre_tags: ['<b>'],
                post_tags: ['</b>'],
                fields: {
                  protocol_id: {},
                  document_number: {},
                  protocol_number: {},
                  title: {},
                  lead_unit_number: {},
                  lead_unit_name: {},
                  status: {},
                  protocol_type: {},
                  status_code: {},
                  person_id: {},
                  person_name: {},
                }
              }
            }
      });
      } else {
          return Promise.resolve({});
      }
    }
  }
