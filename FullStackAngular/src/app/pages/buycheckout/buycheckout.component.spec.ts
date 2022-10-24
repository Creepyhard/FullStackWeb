import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuycheckoutComponent } from './buycheckout.component';

describe('BuycheckoutComponent', () => {
  let component: BuycheckoutComponent;
  let fixture: ComponentFixture<BuycheckoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuycheckoutComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuycheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
