import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CharacterModel,
  CharacterAttribute,
  Focus,
  Truth,
  CharacterItem,
  CreateCharacterRequest,
  SkillType,
  StyleType,
  ItemType,
} from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CharacterService {
  private http    = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api`;

  // ---- Character -------------------------------------------

  /** GET /api/character/:partyId — all characters */
  getCharacters(partyId: number): Observable<CharacterModel[]> {
    return this.http.get<CharacterModel[]>(`${this.baseUrl}/character/${partyId}`);
  }

  /** GET /api/character/:partyId/players */
  getPlayerCharacters(partyId: number): Observable<CharacterModel[]> {
    return this.http.get<CharacterModel[]>(`${this.baseUrl}/character/${partyId}/players`);
  }

  /** GET /api/character/:partyId/npcs */
  getNpcs(partyId: number): Observable<CharacterModel[]> {
    return this.http.get<CharacterModel[]>(`${this.baseUrl}/character/${partyId}/npcs`);
  }

  /** GET /api/character/single/:id */
  getCharacter(id: number): Observable<CharacterModel> {
    return this.http.get<CharacterModel>(`${this.baseUrl}/character/single/${id}`);
  }

  /** POST /api/character/:partyId */
  createCharacter(partyId: number, body: CreateCharacterRequest): Observable<CharacterModel> {
    return this.http.post<CharacterModel>(`${this.baseUrl}/character/${partyId}`, body);
  }

  /** PUT /api/character/:id/name — GM only */
  renameCharacter(id: number, name: string): Observable<CharacterModel> {
    return this.http.put<CharacterModel>(`${this.baseUrl}/character/${id}/name`, { name });
  }

  /** DELETE /api/character/:id — GM only */
  deleteCharacter(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/character/${id}`);
  }

  // ---- Attribute -------------------------------------------

  /** GET /api/attribute/:characterId */
  getAttributes(characterId: number): Observable<CharacterAttribute[]> {
    return this.http.get<CharacterAttribute[]>(`${this.baseUrl}/attribute/${characterId}`);
  }

  /** POST /api/attribute/:characterId — skill */
  addSkill(characterId: number, skill: SkillType, level: number): Observable<CharacterAttribute> {
    return this.http.post<CharacterAttribute>(`${this.baseUrl}/attribute/${characterId}`, { skill, level });
  }

  /** POST /api/attribute/:characterId — style */
  addStyle(characterId: number, style: StyleType, level: number): Observable<CharacterAttribute> {
    return this.http.post<CharacterAttribute>(`${this.baseUrl}/attribute/${characterId}`, { style, level });
  }

  /** PUT /api/attribute/:id */
  updateAttribute(id: number, skill: SkillType, level: number): Observable<CharacterAttribute> {
    return this.http.put<CharacterAttribute>(`${this.baseUrl}/attribute/${id}`, { skill, level });
  }

  // ---- Focus -----------------------------------------------

  /** GET /api/focus/:characterId */
  getFocuses(characterId: number): Observable<Focus[]> {
    return this.http.get<Focus[]>(`${this.baseUrl}/focus/${characterId}`);
  }

  /** POST /api/focus/:characterId */
  addFocus(characterId: number, level: number, text: string): Observable<Focus> {
    return this.http.post<Focus>(`${this.baseUrl}/focus/${characterId}`, { level, text });
  }

  /** PUT /api/focus/:id */
  updateFocus(id: number, level: number, text: string): Observable<Focus> {
    return this.http.put<Focus>(`${this.baseUrl}/focus/${id}`, { level, text });
  }

  /** DELETE /api/focus/:id */
  deleteFocus(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/focus/${id}`);
  }

  // ---- Truth -----------------------------------------------

  /** GET /api/truth/:characterId */
  getTruths(characterId: number): Observable<Truth[]> {
    return this.http.get<Truth[]>(`${this.baseUrl}/truth/${characterId}`);
  }

  /** POST /api/truth/:characterId */
  addTruth(characterId: number, name: string): Observable<Truth> {
    return this.http.post<Truth>(`${this.baseUrl}/truth/${characterId}`, { name });
  }

  /** PUT /api/truth/:id */
  updateTruth(id: number, name: string): Observable<Truth> {
    return this.http.put<Truth>(`${this.baseUrl}/truth/${id}`, { name });
  }

  /** DELETE /api/truth/:id */
  deleteTruth(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/truth/${id}`);
  }

  // ---- Character Item --------------------------------------

  /** GET /api/character-item/:characterId */
  getItems(characterId: number): Observable<CharacterItem[]> {
    return this.http.get<CharacterItem[]>(`${this.baseUrl}/character-item/${characterId}`);
  }

  /** POST /api/character-item/:characterId */
  addItem(characterId: number, body: {
    name: string;
    description: string;
    type: ItemType;
    isHidden?: boolean;
    hiddenName?: string;
    hiddenDescription?: string;
  }): Observable<CharacterItem> {
    return this.http.post<CharacterItem>(`${this.baseUrl}/character-item/${characterId}`, body);
  }

  /** PUT /api/character-item/:id — update name/description */
  updateItem(id: number, name: string, description: string): Observable<CharacterItem> {
    return this.http.put<CharacterItem>(`${this.baseUrl}/character-item/${id}`, { name, description });
  }

  /** PUT /api/character-item/:id/hidden — GM only */
  updateItemHidden(id: number, body: {
    isHidden: boolean;
    hiddenName: string | null;
    hiddenDescription: string | null;
  }): Observable<CharacterItem> {
    return this.http.put<CharacterItem>(`${this.baseUrl}/character-item/${id}/hidden`, body);
  }

  /** DELETE /api/character-item/:id */
  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/character-item/${id}`);
  }
}
