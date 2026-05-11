import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CharacterModel } from '../../../../shared/models';
import { CharacterCard } from './character-card/character-card';

@Component({
  selector: 'app-character-list',
  standalone: true,
  imports: [CharacterCard],
  templateUrl: './character-list.html',
  styleUrl: './character-list.css'
})

export class CharacterList {
  @Input() title = '';
  @Input() characters: CharacterModel[] = [];
  @Input() canAdd = false;
  @Input() selectedCharacterId: number | null = null;

  @Output() characterSelected = new EventEmitter<CharacterModel>();
  @Output() addClicked        = new EventEmitter<void>();

  onCardClicked(character: CharacterModel): void {
    this.characterSelected.emit(character);
  }

  onAddClicked(): void {
    this.addClicked.emit();
  }
}
