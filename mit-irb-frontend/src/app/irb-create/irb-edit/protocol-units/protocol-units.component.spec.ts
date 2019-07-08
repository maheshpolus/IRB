import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolUnitsComponent } from './protocol-units.component';

describe('ProtocolUnitsComponent', () => {
  let component: ProtocolUnitsComponent;
  let fixture: ComponentFixture<ProtocolUnitsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProtocolUnitsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProtocolUnitsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
