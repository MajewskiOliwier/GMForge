import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CharacterModel } from '../../../../../shared/models';

@Component({
  selector: 'app-character-card',
  standalone: true,
  imports: [],
  templateUrl: './character-card.html',
  styleUrl: './character-card.css'
})
export class CharacterCard{
  @Input() character!: CharacterModel;
  @Input() selected = false;
  @Output() cardClicked = new EventEmitter<CharacterModel>();

  onClick(): void {
    this.cardClicked.emit(this.character);
  }
}

