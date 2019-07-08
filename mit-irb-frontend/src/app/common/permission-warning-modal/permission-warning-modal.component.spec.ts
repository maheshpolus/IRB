import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionWarningModalComponent } from './permission-warning-modal.component';

describe('PermissionWarningModalComponent', () => {
  let component: PermissionWarningModalComponent;
  let fixture: ComponentFixture<PermissionWarningModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PermissionWarningModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PermissionWarningModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
