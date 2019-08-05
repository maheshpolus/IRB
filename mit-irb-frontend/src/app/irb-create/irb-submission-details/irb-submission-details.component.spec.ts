import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbSubmissionDetailsComponent } from './irb-submission-details.component';

describe('IrbSubmissionDetailsComponent', () => {
  let component: IrbSubmissionDetailsComponent;
  let fixture: ComponentFixture<IrbSubmissionDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbSubmissionDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbSubmissionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
