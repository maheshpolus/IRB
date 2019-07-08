import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbProtocolComponent } from './irb-protocol.component';

describe('IrbProtocolComponent', () => {
  let component: IrbProtocolComponent;
  let fixture: ComponentFixture<IrbProtocolComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbProtocolComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
