import {Component, Input, Output, EventEmitter, inject, SimpleChanges} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {CharacterModel, Party, CharacterAttribute, SkillType, StyleType, PartyMember} from '../../models';
import { SessionService } from '../../../core/services/session';
import {CharacterService} from '../../../core/services/character';
import {PartyService} from '../../../core/services/party';

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
  @Input() partyId!: number;

  @Output() sessionCreated = new EventEmitter<void>();
  @Output() panelClose     = new EventEmitter<void>();
  @Output() memberKicked   = new EventEmitter<void>();
  @Output() characterSaved = new EventEmitter<void>();

  private sessionService = inject(SessionService);
  private partyService     = inject(PartyService);
  private characterService = inject(CharacterService);

  showSessionOverlay = false;
  sessionName        = '';
  creatingSession    = false;
  sessionError       = '';

  editMode   = false;
  savingEdit = false;
  saveError  = '';

  editName:    string = '';
  editSkills:  Array<{ id: number; skill: SkillType;  level: number }> = [];
  editStyles:  Array<{ id: number; style: StyleType;  level: number }> = [];
  editFocuses: Array<{ id: number; level: number; text: string }>      = [];
  editTruths:  Array<{ id: number; name: string }>                     = [];
  editItems:   Array<{ id: number; name: string; description: string }> = [];

  kickingUserId: number | null = null;
  kickError = '';

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['character']) {
      this.editMode  = false;
      this.saveError = '';
    }
  }

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

  kickMember(member: PartyMember): void {
    this.kickingUserId = member.id;
    this.kickError     = '';
    this.partyService.removeMember(this.partyId, member.id).subscribe({
      next: () => {
        this.kickingUserId = null;
        this.memberKicked.emit();
      },
      error: () => {
        this.kickingUserId = null;
        this.kickError     = `Failed to remove ${member.username}.`;
      }
    });
  }

  enterEditMode(): void {
    if (!this.character) return;
    this.editName    = this.character.name;
    this.editSkills  = this.getSkills().map(a => ({ id: a.id, skill: a.skill!, level: a.level }));
    this.editStyles  = this.getStyles().map(a => ({ id: a.id, style: a.style!, level: a.level }));
    this.editFocuses = (this.character.focuses ?? []).map(f => ({ id: f.id, level: f.level, text: f.text }));
    this.editTruths  = (this.character.truths  ?? []).map(t => ({ id: t.id, name: t.name }));
    this.editItems   = (this.character.items   ?? []).map(i => ({ id: i.id, name: i.name, description: i.description }));
    this.saveError   = '';
    this.editMode    = true;
  }

  cancelEdit(): void {
    this.editMode  = false;
    this.saveError = '';
  }

  saveAll(): void {
    if (!this.character) return;
    const character = this.character;

    this.savingEdit = true;
    this.saveError  = '';

    const calls: any[] = [];

    if (this.editName.trim() && this.editName.trim() !== character.name) {
      calls.push(this.characterService.renameCharacter(character.id, this.editName.trim()));
    }

    for (const s of this.editSkills) {
      // @ts-ignore
      const orig = character.attributes.find(a => a.id === s.id);
      if (orig && orig.level !== s.level) {
        calls.push(this.characterService.updateAttribute(s.id, s.skill, s.level));
      }
    }

    for (const s of this.editStyles) {
      // @ts-ignore
      const orig = character.attributes.find(a => a.id === s.id);
      if (orig && orig.level !== s.level) {
        calls.push(this.characterService.updateAttribute(s.id, s.style as any, s.level));
      }
    }

    for (const f of this.editFocuses) {
      const orig = character.focuses?.find(o => o.id === f.id);
      if (orig && (orig.level !== f.level || orig.text !== f.text)) {
        calls.push(this.characterService.updateFocus(f.id, f.level, f.text));
      }
    }

    for (const t of this.editTruths) {
      const orig = character.truths?.find(o => o.id === t.id);
      if (orig && orig.name !== t.name) {
        calls.push(this.characterService.updateTruth(t.id, t.name));
      }
    }

    for (const i of this.editItems) {
      const orig = character.items?.find(o => o.id === i.id);
      if (orig && (orig.name !== i.name || orig.description !== i.description)) {
        calls.push(this.characterService.updateItem(i.id, i.name, i.description));
      }
    }

    if (calls.length === 0) {
      this.savingEdit = false;
      this.editMode   = false;
      return;
    }

    let completed = 0;
    let errored   = false;

    for (const call of calls) {
      call.subscribe({
        next: () => {
          completed++;
          if (completed === calls.length && !errored) {
            this.savingEdit = false;
            this.editMode   = false;
            this.characterSaved.emit();
          }
        },
        error: () => {
          errored         = true;
          this.savingEdit = false;
          this.saveError  = 'Some changes failed to save.';
        }
      });
    }
  }

  getSkills(): CharacterAttribute[] {
    return this.character?.attributes?.filter(a => !!a.skill) ?? [];
  }

  getStyles(): CharacterAttribute[] {
    return this.character?.attributes?.filter(a => !!a.style) ?? [];
  }

  levelRange(): number[] {
    return [4, 5, 6, 7, 8];
  }

  levelDots(): number[] {
    return [4, 5, 6, 7, 8];
  }

  close(): void {
    this.panelClose.emit();
  }
}

