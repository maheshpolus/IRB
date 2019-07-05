import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommitteeScheduleListComponent } from './committee-schedule-list.component';

describe('CommitteeScheduleListComponent', () => {
  let component: CommitteeScheduleListComponent;
  let fixture: ComponentFixture<CommitteeScheduleListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommitteeScheduleListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommitteeScheduleListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
