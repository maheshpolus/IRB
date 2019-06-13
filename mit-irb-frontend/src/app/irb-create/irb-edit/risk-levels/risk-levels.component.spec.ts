import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RiskLevelsComponent } from './risk-levels.component';

describe('RiskLevelsComponent', () => {
  let component: RiskLevelsComponent;
  let fixture: ComponentFixture<RiskLevelsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RiskLevelsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RiskLevelsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
