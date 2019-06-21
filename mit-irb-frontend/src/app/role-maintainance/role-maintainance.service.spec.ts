import { TestBed, inject } from '@angular/core/testing';

import { RoleMaintainanceService } from './role-maintainance.service';

describe('RoleMaintainanceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RoleMaintainanceService]
    });
  });

  it('should be created', inject([RoleMaintainanceService], (service: RoleMaintainanceService) => {
    expect(service).toBeTruthy();
  }));
});
