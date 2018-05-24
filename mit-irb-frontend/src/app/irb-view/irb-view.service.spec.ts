import { TestBed, inject } from '@angular/core/testing';

import { IrbViewService } from './irb-view.service';

describe('IrbViewService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IrbViewService]
    });
  });

  it('should be created', inject([IrbViewService], (service: IrbViewService) => {
    expect(service).toBeTruthy();
  }));
});
