import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FundingSourceModalComponent } from './funding-source-modal.component';

describe('FundingSourceModalComponent', () => {
  let component: FundingSourceModalComponent;
  let fixture: ComponentFixture<FundingSourceModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FundingSourceModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FundingSourceModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
