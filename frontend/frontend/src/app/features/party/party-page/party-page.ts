import { Component, OnInit, inject, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Navbar } from '../../../shared/components/navbar/navbar';
import { Footer } from '../../../shared/components/footer/footer';
import { SidePanel } from '../../../shared/components/side-panel/side-panel';
import { CharacterList } from './character-list/character-list';
import { PartyService } from '../../../core/services/party';
import { CharacterService } from '../../../core/services/character';
import { Auth } from '../../../core/services/auth';
import { UserService } from '../../../core/services/user';
import { Party, PartyMember, CharacterModel, SkillType, StyleType, ItemType } from '../../../shared/models';

interface SkillEntry { skill: SkillType; level: number; }
interface StyleEntry { style: StyleType; level: number; }
interface FocusEntry { level: number; text: string; }
interface ItemEntry  { name: string; description: string; type: ItemType; isHidden: boolean; hiddenName: string; hiddenDescription: string; }

@Component({
  selector: 'app-party-page',
  standalone: true,
  imports: [Navbar, Footer, SidePanel, CharacterList, RouterLink, ReactiveFormsModule, FormsModule],
  templateUrl: './party-page.html',
  styleUrl: './party-page.css'
})

export class PartyPage implements OnInit {
  private route            = inject(ActivatedRoute);
  private partyService     = inject(PartyService);
  private characterService = inject(CharacterService);
  private userService      = inject(UserService);
  private authService      = inject(Auth);

  partyId!: number;
  party: Party | null            = null;
  members: PartyMember[]         = [];
  playerCharacters: CharacterModel[] = [];
  npcCharacters: CharacterModel[]    = [];

  loading = true;
  error   = '';

  selectedCharacter: CharacterModel | null = null;
  panelMode: 'character' | 'gm-panel' | 'members' = 'members';
  panelVisible = false;
  isGm         = false;

  currentUser = this.authService.currentUser;

  readonly skillOptions: SkillType[]   = ['FIGHT','MOVE','STUDY','SURVIVE','TALK','TINKER'];
  readonly styleOptions: StyleType[]   = ['BOLDLY','CAREFULLY','CLEVERLY','FORCEFULLY','QUIETLY','SWIFTLY'];
  readonly itemTypeOptions: ItemType[] = ['WEAPON','RUNE','BONECHARMS'];

  @Output() userAdded = new EventEmitter<void>();

  showNewPlayerOverlay = false;
  newPlayerUsername    = '';
  addingNewPlayer      = false;
  addMemberError       = '';

  private isValidAttribute(a: any): boolean {
    return a.level >= 4 && a.level <= 8;
  }

  openNewPlayerOverlay(): void {
    this.newPlayerUsername = '';
    this.addMemberError    = '';
    this.showNewPlayerOverlay = true;
  }

  closeNewPlayerOverlay(): void {
    this.showNewPlayerOverlay = false;
  }

  confirmAddPlayer(): void {
    if (!this.party) return;
    this.addingNewPlayer = true;
    this.addMemberError  = '';

    this.userService.getUserByUserName(this.newPlayerUsername.trim()).subscribe({
      next: user => {
        this.partyService.addMember(this.party!.id, user.id).subscribe({
          next: () => {
            this.addingNewPlayer      = false;
            this.showNewPlayerOverlay = false;
            this.loadMembers();
          },
          error: () => {
            this.addingNewPlayer = false;
            this.addMemberError  = 'Failed to add member.';
          }
        });
      },
      error: () => {
        this.addingNewPlayer = false;
        this.addMemberError  = 'User does not exist.';
      }
    });
  }

  showAddPlayerOverlay = false;
  newPlayerName        = '';
  playerSkills: SkillEntry[] = this.skillOptions.map(s => ({ skill: s, level: 4 }));
  playerStyles: StyleEntry[] = this.styleOptions.map(s => ({ style: s, level: 4 }));
  playerFocuses: FocusEntry[] = [];
  playerItems:   ItemEntry[]  = [];
  creatingPlayer  = false;
  addPlayerError  = '';

  onAddPlayerClicked(): void {
    this.newPlayerName  = '';
    this.playerSkills   = this.skillOptions.map(s => ({ skill: s, level: 4 }));
    this.playerStyles   = this.styleOptions.map(s => ({ style: s, level: 4 }));
    this.playerFocuses  = [];
    this.playerItems    = [];
    this.addPlayerError = '';
    this.showAddPlayerOverlay = true;
  }

  closeAddPlayerOverlay(): void { this.showAddPlayerOverlay = false; }

  addPlayerFocus():            void { this.playerFocuses.push({ level: 2, text: '' }); }
  removePlayerFocus(i: number): void { this.playerFocuses.splice(i, 1); }
  addPlayerItem():             void { this.playerItems.push({ name: '', description: '', type: 'WEAPON', isHidden: false, hiddenName: '', hiddenDescription: '' }); }
  removePlayerItem(i: number):  void { this.playerItems.splice(i, 1); }

  submitAddPlayer(): void {
    if (!this.newPlayerName.trim()) {
      this.addPlayerError = 'Character name is required.';
      return;
    }
    const userId = this.currentUser()?.id;
    if (!userId) {
      this.addPlayerError = 'Could not resolve your user ID.';
      return;
    }

    this.creatingPlayer = true;
    this.addPlayerError = '';


    console.log("adding user: "+userId);

    const body = {
      name: this.newPlayerName.trim(),
      userId,
      attributes: [
        ...this.playerSkills
          .filter(s => this.isValidAttribute(s) && s.skill)
          .map(s => ({
            skill: s.skill as SkillType,
            style: undefined,
            level: s.level ?? 4
          })),

        ...this.playerStyles
          .filter(s => this.isValidAttribute(s) && s.style)
          .map(s => ({
            skill: undefined,
            style: s.style as StyleType,
            level: s.level ?? 4
          }))
      ],
      focuses: this.playerFocuses.map(f => ({ level: Number(f.level), text: f.text })),
      items: this.playerItems
        .filter(i => i.name.trim())
        .map(i => ({ name: i.name.trim(), description: i.description.trim(), type: i.type, isHidden: false }))
    };

    this.characterService.createCharacter(this.partyId, body).subscribe({
      next: () => {
        this.creatingPlayer       = false;
        this.showAddPlayerOverlay = false;
        this.loadCharacters();
      },
      error: () => {
        this.creatingPlayer = false;
        this.addPlayerError = 'Failed to create character.';
      }
    });
  }

  showAddNpcOverlay = false;
  newNpcName        = '';
  npcSkills: SkillEntry[] = this.skillOptions.map(s => ({ skill: s, level: 4 }));
  npcStyles: StyleEntry[] = this.styleOptions.map(s => ({ style: s, level: 4 }));
  npcFocuses: FocusEntry[] = [];
  npcItems:   ItemEntry[]  = [];
  creatingNpc  = false;
  addNpcError  = '';

  onAddNpcClicked(): void {
    this.newNpcName  = '';
    this.npcSkills   = this.skillOptions.map(s => ({ skill: s, level: 4 }));
    this.npcStyles   = this.styleOptions.map(s => ({ style: s, level: 4 }));
    this.npcFocuses  = [];
    this.npcItems    = [];
    this.addNpcError = '';
    this.showAddNpcOverlay = true;
  }

  closeAddNpcOverlay(): void { this.showAddNpcOverlay = false; }

  addNpcFocus():            void { this.npcFocuses.push({ level: 2, text: '' }); }
  removeNpcFocus(i: number): void { this.npcFocuses.splice(i, 1); }
  addNpcItem():             void { this.npcItems.push({ name: '', description: '', type: 'WEAPON', isHidden: false, hiddenName: '', hiddenDescription: '' }); }
  removeNpcItem(i: number):  void { this.npcItems.splice(i, 1); }

  submitAddNpc(): void {
    if (!this.newNpcName.trim()) {
      this.addNpcError = 'NPC name is required.';
      return;
    }

    this.creatingNpc = true;
    this.addNpcError = '';

    const body: any = {
      name: this.newNpcName.trim(),
      attributes: [
        ...this.npcSkills
          .filter(s => this.isValidAttribute(s) && s.skill)
          .map(s => ({ skill: s.skill as SkillType, style: null, level: s.level ?? 4 })),
        ...this.npcStyles
          .filter(s => this.isValidAttribute(s) && s.style)
          .map(s => ({ skill: null, style: s.style as StyleType, level: s.level ?? 4 }))
      ],
      focuses: this.npcFocuses.map(f => ({ level: Number(f.level), text: f.text })),
      items: this.npcItems
        .filter(i => i.name.trim())
        .map(i => ({
          name: i.name.trim(),
          description: i.description.trim(),
          type: i.type,
          isHidden: i.isHidden,
          hiddenName: i.isHidden && i.hiddenName.trim() ? i.hiddenName.trim() : null,
          hiddenDescription: i.isHidden && i.hiddenDescription.trim() ? i.hiddenDescription.trim() : null
        }))
    };

    this.characterService.createCharacter(this.partyId, body).subscribe({
      next: () => {
        this.creatingNpc       = false;
        this.showAddNpcOverlay = false;
        this.loadCharacters();
      },
      error: err => {
        this.creatingNpc = false;
        this.addNpcError = err.status === 403 ? 'Only the GM can create NPCs.' : 'Failed to create NPC.';
      }
    });
  }

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
        this.isGm = username === this.party?.gameMaster.username;
      },
      error: () => {}
    });
  }

  loadCharacters(): void {
    this.characterService.getPlayerCharacters(this.partyId).subscribe({
      next: players => { this.playerCharacters = players; this.loading = false; },
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

  openMembersPanel(): void {
    if (!this.party) return;
    this.partyService.getMembers(this.party.id).subscribe({
      next: members => {
        this.members   = members;
        this.panelMode = 'members';
        this.panelVisible = true;
      }
    });
  }

  closePanel(): void { this.panelVisible = false; }

  onSessionCreated(): void {
    this.partyService.getParty(this.partyId).subscribe({
      next: party => this.party = party
    });
  }

  onMemberKicked(): void {
    this.loadMembers();
  }

  onCharacterSaved(): void {
    if (!this.selectedCharacter) return;
    this.characterService.getCharacter(this.selectedCharacter.id).subscribe({
      next: updated => {
        this.selectedCharacter = updated;
        const pi = this.playerCharacters.findIndex(c => c.id === updated.id);
        if (pi > -1) this.playerCharacters[pi] = updated;
        const ni = this.npcCharacters.findIndex(c => c.id === updated.id);
        if (ni > -1) this.npcCharacters[ni] = updated;
      }
    });
  }
}
