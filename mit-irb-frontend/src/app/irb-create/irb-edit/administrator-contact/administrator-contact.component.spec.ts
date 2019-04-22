import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministratorContactComponent } from './administrator-contact.component';

describe('AdministratorContactComponent', () => {
  let component: AdministratorContactComponent;
  let fixture: ComponentFixture<AdministratorContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdministratorContactComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdministratorContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
