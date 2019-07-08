import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExemptQuestionaireComponent } from './exempt-questionaire.component';

describe('ExemptQuestionaireComponent', () => {
  let component: ExemptQuestionaireComponent;
  let fixture: ComponentFixture<ExemptQuestionaireComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExemptQuestionaireComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExemptQuestionaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
