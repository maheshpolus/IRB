import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbActionsComponent } from './irb-actions.component';

describe('IrbActionsComponent', () => {
  let component: IrbActionsComponent;
  let fixture: ComponentFixture<IrbActionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbActionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
