import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbCreateHeaderComponent } from './irb-create-header.component';

describe('IrbCreateHeaderComponent', () => {
  let component: IrbCreateHeaderComponent;
  let fixture: ComponentFixture<IrbCreateHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbCreateHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbCreateHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
