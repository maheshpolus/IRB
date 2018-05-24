import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbAttachmentsComponent } from './irb-attachments.component';

describe('IrbAttachmentsComponent', () => {
  let component: IrbAttachmentsComponent;
  let fixture: ComponentFixture<IrbAttachmentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbAttachmentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbAttachmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
