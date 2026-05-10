import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreferencesPage } from './preferences-page';

describe('PreferencesPage', () => {
  let component: PreferencesPage;
  let fixture: ComponentFixture<PreferencesPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreferencesPage],
    }).compileComponents();

    fixture = TestBed.createComponent(PreferencesPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
