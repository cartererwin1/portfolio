import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DonationHistoryComponent } from './donatehistory.component';

describe('DonatehistoryComponent', () => {
  let component: DonationHistoryComponent;
  let fixture: ComponentFixture<DonationHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DonationHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DonationHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
