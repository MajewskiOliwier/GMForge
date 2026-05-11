import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Navbar } from '../../../shared/components/navbar/navbar';
import { Footer } from '../../../shared/components/footer/footer';
import { PartyService } from '../../../core/services/party';
import { Auth } from '../../../core/services/auth';
import { PartyCard } from '../../../shared/models';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [RouterLink, Navbar, Footer, FormsModule],
  templateUrl: './home-page.html',
  styleUrl: './home-page.css'
})

export class HomePage implements OnInit {
  private partyService = inject(PartyService);
  private authService  = inject(Auth);

  currentUser = this.authService.currentUser;

  parties: PartyCard[] = [];
  loading = true;
  error   = '';

  showCreateOverlay = false;
  newPartyName      = '';
  creating          = false;
  createError       = '';

  ngOnInit(): void {
    this.loadParties();
  }

  loadParties(): void {
    this.loading = true;
    this.error   = '';

    this.partyService.getParties().subscribe({
      next: data => {
        this.parties = data;
        this.loading = false;
      },
      error: () => {
        this.error   = 'Failed to load parties.';
        this.loading = false;
      }
    });
  }

  openCreateOverlay(): void {
    this.newPartyName  = '';
    this.createError   = '';
    this.showCreateOverlay = true;
  }

  closeCreateOverlay(): void {
    this.showCreateOverlay = false;
  }

  submitCreateParty(): void {
    if (!this.newPartyName.trim()) {
      this.createError = 'Party name cannot be empty.';
      return;
    }

    this.creating    = true;
    this.createError = '';

    this.partyService.createParty({ name: this.newPartyName.trim() }).subscribe({
      next: () => {
        this.creating          = false;
        this.showCreateOverlay = false;
        this.loadParties();
      },
      error: () => {
        this.creating    = false;
        this.createError = 'Failed to create party. Try again.';
      }
    });
  }
}
