// ============================================================
// GMForge — Shared Models
// src/app/shared/models/index.ts
// ============================================================

// ---- Auth ------------------------------------------------

export interface AuthResponse {
  token: string;
  user: User;
}

// ---- User ------------------------------------------------

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface UserPreference {
  id: number;
  theme: 'LIGHT' | 'DARK';
}

// ---- Party -----------------------------------------------

/**
 * Used on the home page — compact card representation.
 * Returned by GET /api/party (list endpoint).
 */
export interface PartyCard {
  id: number;
  name: string;
  gameMasterUsername: string;
  memberCount: number;
  currentSessionName: string | null;
  currentSessionNumber: number;
}

/**
 * Full party detail — returned by GET /api/party/:id.
 */
export interface Party {
  id: number;
  name: string;
  gameMaster: User;
  memberCount: number;
  currentSession: Session | null;
}

// ---- Party Member ----------------------------------------

export interface PartyMember {
  id: number;
  username: string;
  email: string;
  isGameMaster: boolean;
}

// ---- Session ---------------------------------------------

export interface Session {
  id: number;
  name: string | null;
  sessionNumber: number;
  createdAt: string; // ISO date string
}

// ---- Party Log -------------------------------------------

export interface PartyLog {
  id: number;
  description: string;
  createdByUsername: string;
  createdAt: string; // ISO date string
}

// ---- Character -------------------------------------------

export interface CharacterModel {
  id: number;
  name: string;
  isNpc: boolean;
  playerUsername: string | null; // null for NPCs
  attributes?: CharacterAttribute[];
  focuses?: Focus[];
  truths?: Truth[];
  items?: CharacterItem[];
}

// ---- Attribute -------------------------------------------

export type SkillType = 'FIGHT' | 'MOVE' | 'STUDY' | 'SURVIVE' | 'TALK' | 'TINKER';
export type StyleType = 'BOLDLY' | 'CAREFULLY' | 'CLEVERLY' | 'FORCEFULLY' | 'QUIETLY' | 'SWIFTLY';

export interface CharacterAttribute {
  id: number;
  skill?: SkillType;
  style?: StyleType;
  level: number;
}

// ---- Focus -----------------------------------------------

export interface Focus {
  id: number;
  level: number; // 2–5
  text: string;
}

// ---- Truth -----------------------------------------------

export interface Truth {
  id: number;
  name: string;
}

// ---- Character Item --------------------------------------

export type ItemType = 'WEAPON' | 'RUNE' | 'BONECHARMS';

export interface CharacterItem {
  id: number;
  name: string;
  description: string;
  type: ItemType;
  isHidden: boolean;
  hiddenName: string | null;
  hiddenDescription: string | null;
}

export interface CreatePartyRequest {
  name: string;
}

export interface CreateSessionRequest {
  name?: string;
}

export interface CreatePartyLogRequest {
  description: string;
}

export interface CreateCharacterRequest {
  name: string;
  userId?: number; // omit for NPC
  attributes?: Array<{ skill?: SkillType; style?: StyleType; level: number }>;
  focuses?: Array<{ level: number; text: string }>;
  items?: Array<{ name: string; description: string; type: ItemType; isHidden?: boolean; hiddenName?: string; hiddenDescription?: string }>;
}

export interface UpdateThemeRequest {
  theme: 'LIGHT' | 'DARK';
}
