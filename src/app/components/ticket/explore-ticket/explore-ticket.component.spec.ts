import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExploreTicketComponent } from './explore-ticket.component';

describe('ExploreTicketComponent', () => {
  let component: ExploreTicketComponent;
  let fixture: ComponentFixture<ExploreTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExploreTicketComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExploreTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
