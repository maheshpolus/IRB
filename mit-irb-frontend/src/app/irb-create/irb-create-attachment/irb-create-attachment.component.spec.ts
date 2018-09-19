import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IrbCreateAttachmentComponent } from './irb-create-attachment.component';

describe('IrbCreateAttachmentComponent', () => {
  let component: IrbCreateAttachmentComponent;
  let fixture: ComponentFixture<IrbCreateAttachmentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IrbCreateAttachmentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IrbCreateAttachmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
