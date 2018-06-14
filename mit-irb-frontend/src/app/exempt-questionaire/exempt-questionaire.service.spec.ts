import { TestBed, inject } from '@angular/core/testing';

import { ExemptQuestionaireService } from './exempt-questionaire.service';

describe('ExemptQuestionaireService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExemptQuestionaireService]
    });
  });

  it('should be created', inject([ExemptQuestionaireService], (service: ExemptQuestionaireService) => {
    expect(service).toBeTruthy();
  }));
});
