import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExemptCardComponent } from './exempt-card.component';

describe('ExemptCardComponent', () => {
  let component: ExemptCardComponent;
  let fixture: ComponentFixture<ExemptCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExemptCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExemptCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
