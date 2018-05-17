import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbHistoryComponent } from './irb-history.component';

describe('IrbHistoryComponent', () => {
  let component: IrbHistoryComponent;
  let fixture: ComponentFixture<IrbHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
