import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbSummaryDetailsComponent } from './irb-summary-details.component';

describe('IrbSummaryDetailsComponent', () => {
  let component: IrbSummaryDetailsComponent;
  let fixture: ComponentFixture<IrbSummaryDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbSummaryDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbSummaryDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
