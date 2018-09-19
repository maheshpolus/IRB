import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbCreateHistroyComponent } from './irb-create-histroy.component';

describe('IrbCreateHistroyComponent', () => {
  let component: IrbCreateHistroyComponent;
  let fixture: ComponentFixture<IrbCreateHistroyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbCreateHistroyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbCreateHistroyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
