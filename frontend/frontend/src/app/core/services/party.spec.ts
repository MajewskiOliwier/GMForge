import { TestBed } from '@angular/core/testing';

import { PartyService } from './party';

describe('Party', () => {
  let service: PartyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
