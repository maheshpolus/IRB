import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbPermissionComponent } from './irb-permission.component';

describe('IrbPermissionComponent', () => {
  let component: IrbPermissionComponent;
  let fixture: ComponentFixture<IrbPermissionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbPermissionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbPermissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
