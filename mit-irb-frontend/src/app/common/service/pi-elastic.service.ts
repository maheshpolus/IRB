import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Client, SearchResponse } from 'elasticsearch';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class PiElasticService {
  private _client: Client;
  URL_FOR_ELASTIC: string;
  IRB_INDEX: string;
  PERSON_INDEX: string;
  NON_EMPLOYEE_INDEX: string;
  constructor( private _http: HttpClient ) {
  /*    this.URL_FOR_ELASTIC = 'http://192.168.1.76:9200';
      this.IRB_INDEX = 'kcperson';
      this.PERSON_INDEX = 'fibirolodex';
      this.NON_EMPLOYEE_INDEX = 'mitrolodex';*/
       this.get_elastic_config().subscribe(
          data => {
               const elastic_config: any = data;
               if (elastic_config) {
                   this.URL_FOR_ELASTIC = elastic_config.URL_FOR_ELASTIC;
                   this.IRB_INDEX = elastic_config.PERSON_INDEX;
                   this.NON_EMPLOYEE_INDEX = elastic_config.NON_EMPLOYEE_INDEX;
                   
			if ( !this._client ) {
                       this._connect();
                  }
              }
           }
       );
     /*if ( !this._client ) {
           this._connect();
      }*/
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
        index: this.IRB_INDEX,
        size: 20 ,
        type: 'person',
        body: {
            query: {
              bool: {
                should: [
                  {
                    match: {
                        full_name: {
                        query: value,
                        operator: 'or'
                      }
                    }
                  },
                  // {
                  //   match: {
                  //       person_id: {
                  //         query: value,
                  //         operator: 'or'
                  //       }
                  //     }
                  //   }
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
                // person_id: {},
                full_name: {},
              }
            }
          }
    });
    } else {
        return Promise.resolve({});
    }
  }

  personSearch(value, personType): any {
    let type = 'person';
    let index = '';
    if (personType === 'non-employee') {
      type = 'rolodex';
      index = this.NON_EMPLOYEE_INDEX;
    } else {
      type = 'person';
      index =  this.IRB_INDEX;
    }
    if ( value ) {
      return this._client.search({
        index: index,
        size: 20 ,
        type: type,
        body: {
            query: {
              bool: {
                should: [
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
                      first_name: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
                    {
                      match: {
                          user_name: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
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
                 first_name: {},
                full_name: {},
              }
            }
          }
    });
    } else {
        return Promise.resolve({});
    }
  }
}
