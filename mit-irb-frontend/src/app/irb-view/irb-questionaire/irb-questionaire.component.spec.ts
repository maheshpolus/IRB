import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbQuestionaireComponent } from './irb-questionaire.component';

describe('IrbQuestionaireComponent', () => {
  let component: IrbQuestionaireComponent;
  let fixture: ComponentFixture<IrbQuestionaireComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbQuestionaireComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbQuestionaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
