import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbDetailComponent } from './irb-detail.component';

describe('IrbDetailComponent', () => {
  let component: IrbDetailComponent;
  let fixture: ComponentFixture<IrbDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
