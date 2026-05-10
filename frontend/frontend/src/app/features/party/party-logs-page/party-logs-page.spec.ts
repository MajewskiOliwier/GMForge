import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartyLogsPage } from './party-logs-page';

describe('PartyLogsPage', () => {
  let component: PartyLogsPage;
  let fixture: ComponentFixture<PartyLogsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartyLogsPage],
    }).compileComponents();

    fixture = TestBed.createComponent(PartyLogsPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
