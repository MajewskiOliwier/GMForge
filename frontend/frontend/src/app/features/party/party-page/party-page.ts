import {Component, OnInit, inject, Output, EventEmitter} from '@angular/core';
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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserService} from '../../../core/services/user';

@Component({
  selector: 'app-party-page',
  standalone: true,
  imports: [
    Navbar,
    Footer,
    SidePanel,
    CharacterList,
    RouterLink,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './party-page.html',
  styleUrl: './party-page.css'
})

export class PartyPage implements OnInit {
  private route            = inject(ActivatedRoute);
  private partyService     = inject(PartyService);
  private characterService = inject(CharacterService);
  private userService = inject(UserService);
  private authService      = inject(Auth);

  partyId!: number;
  party: Party | null           = null;
  members: PartyMember[]        = [];
  playerCharacters: CharacterModel[] = [];
  npcCharacters: CharacterModel[]    = [];

  loading = true;
  error   = '';

  selectedCharacter: CharacterModel | null = null;
  panelMode: 'character' | 'gm-panel' | 'members' = 'members';
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
        this.isGm = username == this.party?.gameMaster.username;
        console.log("is gm ? "+this.isGm+" username: "+username+", game master=" +this.party?.gameMaster.username);
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

  onCharacterSelected(character: CharacterModel): void {
    this.selectedCharacter = character;
    this.panelMode         = 'character';
    this.panelVisible      = true;

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  openGmPanel(): void {
    this.selectedCharacter = null;
    this.panelMode         = 'gm-panel';
    this.panelVisible      = true;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  closePanel(): void {
    this.panelVisible = false;
  }

  openMembersPanel(): void {
    if (!this.party) return;

    this.partyService.getMembers(this.party.id).subscribe({
      next: members => {
        console.log(members);
        this.members = members;
        this.panelMode = 'members';
      }
    });
  }

  onSessionCreated(): void {
    // Reload party to show updated currentSession
    this.partyService.getParty(this.partyId).subscribe({
      next: party => this.party = party
    });
  }

  onAddPlayerClicked(): void {
    // Future: open add character overlay
  }

  onAddNpcClicked(): void {
    // Future: open add NPC overlay
  }

  showNewPlayerOverlay = false;
  newPlayerUsername        = '';
  addingNewPlayer    = false;
  addPlayerError = '';

  @Output() userAdded = new EventEmitter<void>();

  openNewPlayerOverlay(): void {
    this.newPlayerUsername     = '';
    this.addPlayerError = '';
    this.showNewPlayerOverlay = true;
  }

  closeNewPlayerOverlay(): void {
    this.showNewPlayerOverlay = false;
  }

  confirmAddPlayer(): void {
    if (!this.party) return;

    this.addingNewPlayer = true;

    this.userService
      .getUserByUserName(this.newPlayerUsername)
      .subscribe({

        next: (user) => {

          const userId = user.id;

          this.partyService.addMember(this.party!.id, userId).subscribe({

            next: () => {
              this.addingNewPlayer = false;
              this.showNewPlayerOverlay = false;

              this.loadMembers();
            },

            error: () => {
              this.addingNewPlayer = false;
              this.addPlayerError =  'Failed to add member';
            }

          });

        },

        error: () => {
          this.addingNewPlayer = false;
          this.addPlayerError = 'User does not exist';
        }

      });
  }
}
