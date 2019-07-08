import { TestBed, inject } from '@angular/core/testing';

import { AssignModalServiceService } from './assign-modal.service';

describe('AssignModal.ServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AssignModalServiceService]
    });
  });

  it('should be created', inject([AssignModalServiceService], (service: AssignModalServiceService) => {
    expect(service).toBeTruthy();
  }));
});
