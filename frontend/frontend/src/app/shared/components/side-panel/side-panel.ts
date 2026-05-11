import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {CharacterModel, Party, CharacterAttribute, SkillType, StyleType, PartyMember} from '../../models';
import { SessionService } from '../../../core/services/session';

@Component({
  selector: 'app-side-panel',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './side-panel.html',
  styleUrl: './side-panel.css'
})
export class SidePanel {
  @Input() mode: 'character' | 'gm-panel' | 'members' = 'members';
  @Input() character: CharacterModel | null = null;

  @Input() party: Party | null = null;
  @Input() isGm = false;
  @Input() members: PartyMember[] = [];

  @Output() sessionCreated = new EventEmitter<void>();
  @Output() panelClose     = new EventEmitter<void>();

  private sessionService = inject(SessionService);

  showSessionOverlay = false;
  sessionName        = '';
  creatingSession    = false;
  sessionError       = '';

  get nextSessionNumber(): number {
    return (this.party?.currentSession?.sessionNumber ?? 0) + 1;
  }

  openSessionOverlay(): void {
    this.sessionName     = '';
    this.sessionError    = '';
    this.showSessionOverlay = true;
  }

  closeSessionOverlay(): void {
    this.showSessionOverlay = false;
  }

  confirmStartSession(): void {
    if (!this.party) return;

    this.creatingSession = true;
    this.sessionError    = '';

    const body = this.sessionName.trim()
      ? { name: this.sessionName.trim() }
      : {};

    this.sessionService.createSession(this.party.id, body).subscribe({
      next: () => {
        this.creatingSession    = false;
        this.showSessionOverlay = false;
        this.sessionCreated.emit();
      },
      error: () => {
        this.creatingSession = false;
        this.sessionError    = 'Failed to start session. Try again.';
      }
    });
  }

  getSkills(): CharacterAttribute[] {
    return this.character?.attributes?.filter(a => !!a.skill) ?? [];
  }

  getStyles(): CharacterAttribute[] {
    return this.character?.attributes?.filter(a => !!a.style) ?? [];
  }

  levelDots(level: number): number[] {
    return Array.from({ length: 6 }, (_, i) => i + 1);
  }

  close(): void {
    this.panelClose.emit();
  }
}

