import { TestBed, inject } from '@angular/core/testing';

import { PiElasticService } from './pi-elastic.service';

describe('PiElasticService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PiElasticService]
    });
  });

  it('should be created', inject([PiElasticService], (service: PiElasticService) => {
    expect(service).toBeTruthy();
  }));
});
