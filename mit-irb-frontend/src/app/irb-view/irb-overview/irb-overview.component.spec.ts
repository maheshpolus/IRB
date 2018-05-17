import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbOverviewComponent } from './irb-overview.component';

describe('IrbOverviewComponent', () => {
  let component: IrbOverviewComponent;
  let fixture: ComponentFixture<IrbOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
