import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Client, SearchResponse } from 'elasticsearch';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ElasticService {
    private _client: Client;
    URL_FOR_ELASTIC: string;
    IRB_INDEX: string;
    AWARD_INDEX: string;
    IP_INDEX: string;
    DP_INDEX: string;
    constructor( private _http: HttpClient ) {
       /* this.URL_FOR_ELASTIC = 'http://192.168.1.76:9200';
        this.IRB_INDEX = 'irbprotocol';
        this.AWARD_INDEX = 'award';
        this.IP_INDEX = 'proposal';
        this.DP_INDEX = 'devproposal';
        if ( !this._client ) {
            this._connect();
       }*/
         this.get_elastic_config().subscribe(
            data => {
                 const elastic_config: any = data;
                 if (elastic_config) {
                     this.URL_FOR_ELASTIC = elastic_config.URL_FOR_ELASTIC;
                     this.IRB_INDEX = elastic_config.IRB_INDEX;
                     this.AWARD_INDEX = elastic_config.AWARD_INDEX;
                     this.IP_INDEX = elastic_config.IP_INDEX;
                     this.DP_INDEX = elastic_config.DP_INDEX;
                     if ( !this._client ) {
                         this._connect();
                    }
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
        return this._http.get('irb/resources/elastic_config_json');
    }
    irbSearch(value): any {
      if ( value ) {
        return this._client.search({
          index: this.IRB_INDEX,
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
                          pi_name: {
                          query: value,
                          operator: 'or'
                        }
                      }
                    },
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
                    /*{
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
                      },*/
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
                //  lead_unit_number: {},
                 // lead_unit_name: {},
                  status: {},
                  protocol_type: {},
                  status_code: {},
                  person_id: {},
                  pi_name: {},
                }
              }
            }
      });
      } else {
          return Promise.resolve({});
      }
    }

  /*All Award search query*/
  awardSearch( term): any {
    if ( term ) {
      return this._client.search({
        index: this.AWARD_INDEX,
        type: 'award',
        body: {
          query: {
            bool: {
              should: [{
                match: {
                  award_number: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  sponsor_name: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  pi_name: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  account_number: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  lead_unit_number: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  lead_unit_name: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  sponsor_award_id: {
                    query: term,
                    operator: 'or'
                  }
                }
              },
              {
                match: {
                  title: {
                    query: term,
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
              award_number: {},
              pi_name: {},
              sponsor_name: {},
              account_number: {},
              lead_unit_number: {},
              lead_unit_name: {},
              title: {},
              sponsor_award_id: {}
            }
          }
        }
        });
        } else {
               return Promise.resolve({});
             }
  }

  /*IP search query*/
  searchIP( term, typeCode): any {
    let index = this.IP_INDEX;
    let type = 'proposal';
    if (typeCode === '4') {
      index = this.DP_INDEX;
      type = 'devproposal';
    } else if (typeCode === '5') {
      index = this.IP_INDEX;
      type = 'proposal';
    }
    if ( term ) {
      return this._client.search({
        index: index,
        type: type,
        body: {
          query: {
            bool: {
              must: [{
                bool: {
                  should: [{
                    match: {
                      proposal_number: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      sponsor_name: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      pi_name: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      lead_unit_number: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      lead_unit_name: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      sponsor_award_id: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      status: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  },
                  {
                    match: {
                      title: {
                        query: term,
                        operator: 'or'
                      }
                    }
                  }
                      ]
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
                  proposal_number: {},
                  pi_name: {},
                  sponsor_name: {},
                  lead_unit_number: {},
                  lead_unit_name: {},
                  title: {},
                  sponsor_award_id: {},
                  status: {}
                }
              }
              }
        });
        } else {
               return Promise.resolve({});
             }
  }
  }
