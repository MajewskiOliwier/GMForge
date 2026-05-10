import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartyPage } from './party-page';

describe('PartyPage', () => {
  let component: PartyPage;
  let fixture: ComponentFixture<PartyPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PartyPage],
    }).compileComponents();

    fixture = TestBed.createComponent(PartyPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
