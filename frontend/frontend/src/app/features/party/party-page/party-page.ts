// ============================================================
// GMForge — Party Page
// src/app/features/party/party-page/party-page.component.ts
// ============================================================

import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Navbar } from '../../../shared/components/navbar/navbar';
import { Footer } from '../../../shared/components/footer/footer';
import { SidePanel } from '../../../shared/components/side-panel/side-panel';
import { CharacterList } from './character-list/character-list';
import { PartyService } from '../../../core/services/party';
import { CharacterService } from '../../../core/services/character';
import { Auth } from '../../../core/services/auth';
import { Party, PartyMember, CharacterModel } from '../../../shared/models';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-party-page',
  standalone: true,
  imports: [
    Navbar,
    Footer,
    SidePanel,
    CharacterList,
    RouterLink
  ],
  templateUrl: './party-page.html',
  styleUrl: './party-page.css'
})
export class PartyPage implements OnInit {
  private route            = inject(ActivatedRoute);
  private partyService     = inject(PartyService);
  private characterService = inject(CharacterService);
  private authService      = inject(Auth);

  partyId!: number;
  party: Party | null           = null;
  members: PartyMember[]        = [];
  playerCharacters: CharacterModel[] = [];
  npcCharacters: CharacterModel[]    = [];

  loading = true;
  error   = '';

  selectedCharacter: CharacterModel | null = null;
  panelMode: 'character' | 'gm-panel' = 'character';
  panelVisible = false; // controls mobile visibility

  isGm = false;

  currentUser = this.authService.currentUser;

  ngOnInit(): void {
    this.partyId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.error   = '';

    // Load party detail
    this.partyService.getParty(this.partyId).subscribe({
      next: party => {
        this.party = party;
        this.loadMembers();
        this.loadCharacters();
      },
      error: () => {
        this.error   = 'Failed to load party.';
        this.loading = false;
      }
    });
  }

  loadMembers(): void {
    this.partyService.getMembers(this.partyId).subscribe({
      next: members => {
        this.members = members;
        const username = this.currentUser()?.username;
        this.isGm = members.some(m => m.username === username && m.isGameMaster);
      },
      error: () => {}
    });
  }

  loadCharacters(): void {
    this.characterService.getPlayerCharacters(this.partyId).subscribe({
      next: players => {
        this.playerCharacters = players;
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });

    this.characterService.getNpcs(this.partyId).subscribe({
      next: npcs => this.npcCharacters = npcs,
      error: () => {}
    });
  }

  // ---- Character selection --------------------------------

  onCharacterSelected(character: CharacterModel): void {
    this.selectedCharacter = character;
    this.panelMode         = 'character';
    this.panelVisible      = true;

    // On mobile — scroll to top so the panel appears full screen
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  // ---- GM panel toggle ------------------------------------

  openGmPanel(): void {
    this.selectedCharacter = null;
    this.panelMode         = 'gm-panel';
    this.panelVisible      = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  closePanel(): void {
    this.panelVisible = false;
  }

  // ---- Session created callback ---------------------------

  onSessionCreated(): void {
    // Reload party to show updated currentSession
    this.partyService.getParty(this.partyId).subscribe({
      next: party => this.party = party
    });
  }

  // ---- Add character (placeholder) ----------------------
  onAddPlayerClicked(): void {
    // Future: open add character overlay
  }

  onAddNpcClicked(): void {
    // Future: open add NPC overlay
  }
}
