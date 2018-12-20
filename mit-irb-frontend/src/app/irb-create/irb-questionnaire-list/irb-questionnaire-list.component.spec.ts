import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbQuestionnaireListComponent } from './irb-questionnaire-list.component';

describe('IrbQuestionnaireListComponent', () => {
  let component: IrbQuestionnaireListComponent;
  let fixture: ComponentFixture<IrbQuestionnaireListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbQuestionnaireListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbQuestionnaireListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
