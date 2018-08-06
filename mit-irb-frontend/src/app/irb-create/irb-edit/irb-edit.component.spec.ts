import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbEditComponent } from './irb-edit.component';

describe('IrbEditComponent', () => {
  let component: IrbEditComponent;
  let fixture: ComponentFixture<IrbEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
