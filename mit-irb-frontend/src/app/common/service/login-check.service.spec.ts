import { TestBed, inject } from '@angular/core/testing';

import { LoginCheckService } from './login-check.service';

describe('LoginCheckService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LoginCheckService]
    });
  });

  it('should be created', inject([LoginCheckService], (service: LoginCheckService) => {
    expect(service).toBeTruthy();
  }));
});
