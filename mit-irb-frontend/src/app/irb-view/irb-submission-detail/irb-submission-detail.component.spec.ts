import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbSubmissionDetailComponent } from './irb-submission-detail.component';

describe('IrbSubmissionDetailComponent', () => {
  let component: IrbSubmissionDetailComponent;
  let fixture: ComponentFixture<IrbSubmissionDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbSubmissionDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbSubmissionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
