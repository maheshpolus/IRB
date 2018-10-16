import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonnelInfoComponent } from './personnel-info.component';

describe('PersonnelInfoComponent', () => {
  let component: PersonnelInfoComponent;
  let fixture: ComponentFixture<PersonnelInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonnelInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonnelInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
