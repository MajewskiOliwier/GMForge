import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PartyLog } from '../../../shared/models';
import {Navbar} from '../../../shared/components/navbar/navbar';
import {Footer} from '../../../shared/components/footer/footer';
import {PartyService} from '../../../core/services/party';
import {Auth} from '../../../core/services/auth';

@Component({
  selector: 'app-party-logs-page',
  standalone: true,
  imports: [Navbar, Footer, RouterLink, FormsModule],
  templateUrl: './party-logs-page.html',
  styleUrl: './party-logs-page.css'
})
export class PartyLogsPage implements OnInit {
  private route        = inject(ActivatedRoute);
  private partyService = inject(PartyService);
  private authService  = inject(Auth);

  partyId!: number;
  logs: PartyLog[] = [];
  loading = true;
  error   = '';

  // Add log
  newLogText  = '';
  addingLog   = false;
  addLogError = '';

  currentUser = this.authService.currentUser;

  ngOnInit(): void {
    this.partyId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadLogs();
  }

  loadLogs(): void {
    this.loading = true;
    this.error   = '';

    this.partyService.getLogs(this.partyId).subscribe({
      next: logs => {
        this.logs    = logs;
        this.loading = false;
      },
      error: () => {
        this.error   = 'Failed to load logs.';
        this.loading = false;
      }
    });
  }

  submitLog(): void {
    if (!this.newLogText.trim()) return;

    this.addingLog   = true;
    this.addLogError = '';

    this.partyService.addLog(this.partyId, { description: this.newLogText.trim() }).subscribe({
      next: log => {
        this.logs      = [log, ...this.logs];
        this.newLogText = '';
        this.addingLog  = false;
      },
      error: () => {
        this.addingLog   = false;
        this.addLogError = 'Failed to add log entry.';
      }
    });
  }

  formatDate(iso: string): string {
    return new Date(iso).toLocaleString(undefined, {
      dateStyle: 'medium',
      timeStyle: 'short'
    });
  }
}
