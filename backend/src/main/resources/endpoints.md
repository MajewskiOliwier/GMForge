# GMForge API Endpoints

Base URL: `http://localhost:8080`

All endpoints except `/api/auth/**` require `Authorization: Bearer <token>` header.

---

## Auth

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/auth/register` | `{ "username": "john", "email": "john@email.com", "password": "pass" }` |
| POST | `/api/auth/login` | `{ "username": "john", "password": "pass" }` |

---

## User

| Method | Endpoint | Body |
|--------|----------|------|
| GET | `/api/users/all` | — |
| GET | `/api/users/me` | — |
| GET | `/api/users/me/preferences` | — |
| PUT | `/api/users/me/preferences/theme` | `{ "theme": "DARK" }` |

---

## Party

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/party` | `{ "name": "The Shadow Collective" }` |
| GET | `/api/party` | — |
| GET | `/api/party/:id` | — |
| PUT | `/api/party/:id` | `{ "name": "Updated Name" }` |
| DELETE | `/api/party/:id` | — |

> PUT and DELETE are GM only.

---

## Party Member

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/party-member/:partyId` | `{ "userId": 1 }` |
| GET | `/api/party-member/:partyId` | — |
| DELETE | `/api/party-member/:partyId/:userId` | — |

> POST is GM only. DELETE is GM or self.

---

## Party Log

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/party-log/:partyId` | `{ "description": "The party entered the fortress" }` |
| GET | `/api/party-log/:partyId` | — |
| GET | `/api/party-log/:partyId/count` | — |
| DELETE | `/api/party-log/:partyId/oldest` | `{ "count": 5 }` |

---

## Session

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/session/:partyId` | `{ "name": "The Heist" }` or `{}` (name optional) |
| GET | `/api/session/:partyId` | — |
| GET | `/api/session/single/:id` | — |
| DELETE | `/api/session/:id` | — |

> POST and DELETE are GM only.

---

## Character

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/character/:partyId` | See below |
| GET | `/api/character/:partyId` | — |
| GET | `/api/character/:partyId/players` | — |
| GET | `/api/character/:partyId/npcs` | — |
| GET | `/api/character/single/:id` | — |
| PUT | `/api/character/:id/name` | `{ "name": "Updated Corvo" }` |
| DELETE | `/api/character/:id` | — |

> PUT and DELETE are GM only.
> Players can only create characters for themselves.
> Only GM can create NPCs (userId omitted).

**POST body — Player character:**
```json
{
  "name": "Corvo",
  "userId": 1,
  "attributes": [
    { "skill": "FIGHT", "level": 4 },
    { "style": "BOLDLY", "level": 4 }
  ],
  "focuses": [
    { "level": 2, "text": "Swordsmanship" }
  ],
  "items": [
    { "name": "Sword", "description": "A sharp blade", "type": "WEAPON" }
  ]
}
```

**POST body — NPC (GM only):**
```json
{
  "name": "The Outsider",
  "attributes": [
    { "skill": "TALK", "level": 6 }
  ],
  "focuses": [
    { "level": 3, "text": "Manipulation" }
  ],
  "items": [
    {
      "name": "Dagger",
      "description": "Ornate blade",
      "type": "WEAPON",
      "isHidden": true,
      "hiddenName": "???",
      "hiddenDescription": "Unknown origin"
    }
  ]
}
```

---

## Attribute

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/attribute/:characterId` | See below |
| GET | `/api/attribute/:characterId` | — |
| GET | `/api/attribute/:characterId/skills` | — |
| GET | `/api/attribute/:characterId/styles` | — |
| PUT | `/api/attribute/:id` | See below |

> GM or character owner only. Attributes cannot be deleted.

**POST body — Skill:**
```json
{ "skill": "FIGHT", "level": 4 }
```

**POST body — Style:**
```json
{ "style": "BOLDLY", "level": 4 }
```

**PUT body:**
```json
{ "skill": "FIGHT", "level": 6 }
```

**Available skills:** `FIGHT`, `MOVE`, `STUDY`, `SURVIVE`, `TALK`, `TINKER`

**Available styles:** `BOLDLY`, `CAREFULLY`, `CLEVERLY`, `FORCEFULLY`, `QUIETLY`, `SWIFTLY`

---

## Focus

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/focus/:characterId` | `{ "level": 2, "text": "Swordsmanship" }` |
| GET | `/api/focus/:characterId` | — |
| PUT | `/api/focus/:id` | `{ "level": 3, "text": "Updated focus" }` |
| DELETE | `/api/focus/:id` | — |

> GM or character owner only.

**Focus level range:** 2–5

---

## Truth

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/truth/:characterId` | `{ "name": "I am the last assassin" }` |
| GET | `/api/truth/:characterId` | — |
| PUT | `/api/truth/:id` | `{ "name": "Updated truth" }` |
| DELETE | `/api/truth/:id` | — |

> GM or character owner only.

---

## Character Item

| Method | Endpoint | Body |
|--------|----------|------|
| POST | `/api/character-item/:characterId` | See below |
| GET | `/api/character-item/:characterId` | — |
| PUT | `/api/character-item/:id/hidden` | See below |
| DELETE | `/api/character-item/:id` | — |

> POST — GM or character owner. PUT hidden flags — GM only.

**POST body — Player:**
```json
{ "name": "Sword", "description": "A sharp blade", "type": "WEAPON" }
```

**POST body — GM (with hidden flags):**
```json
{
  "name": "Dagger",
  "description": "Ornate blade",
  "type": "WEAPON",
  "isHidden": true,
  "hiddenName": "???",
  "hiddenDescription": "Unknown origin"
}
```

**PUT body:**
```json
{ "isHidden": false, "hiddenName": null, "hiddenDescription": null }
```

**Available item types:** `WEAPON`, `RUNE`, `BONECHARMS`

---

## Enums Reference

| Enum | Values |
|------|--------|
| Theme | `LIGHT`, `DARK` |
| SkillType | `FIGHT`, `MOVE`, `STUDY`, `SURVIVE`, `TALK`, `TINKER` |
| StyleType | `BOLDLY`, `CAREFULLY`, `CLEVERLY`, `FORCEFULLY`, `QUIETLY`, `SWIFTLY` |
| ItemType | `WEAPON`, `RUNE`, `BONECHARMS` |