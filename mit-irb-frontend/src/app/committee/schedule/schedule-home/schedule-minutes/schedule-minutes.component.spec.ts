import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleMinutesComponent } from './schedule-minutes.component';

describe('ScheduleMinutesComponent', () => {
  let component: ScheduleMinutesComponent;
  let fixture: ComponentFixture<ScheduleMinutesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScheduleMinutesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScheduleMinutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
