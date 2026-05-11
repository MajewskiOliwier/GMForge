import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Party,
  PartyCard,
  PartyMember,
  PartyLog,
  CreatePartyRequest,
  CreatePartyLogRequest,
} from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PartyService {
  private http    = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api`;

  // ---- Party -----------------------------------------------

  /** GET /api/party — list of all parties the user belongs to */
  getParties(): Observable<PartyCard[]> {
    return this.http.get<PartyCard[]>(`${this.baseUrl}/party`);
  }

  /** GET /api/party/:id — full party detail */
  getParty(id: number): Observable<Party> {
    return this.http.get<Party>(`${this.baseUrl}/party/${id}`);
  }

  /** POST /api/party */
  createParty(body: CreatePartyRequest): Observable<Party> {
    return this.http.post<Party>(`${this.baseUrl}/party`, body);
  }

  /** PUT /api/party/:id — GM only */
  updateParty(id: number, name: string): Observable<Party> {
    return this.http.put<Party>(`${this.baseUrl}/party/${id}`, { name });
  }

  /** DELETE /api/party/:id — GM only */
  deleteParty(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/party/${id}`);
  }

  /** GET /api/party-member/:partyId */
  getMembers(partyId: number): Observable<PartyMember[]> {
    return this.http.get<PartyMember[]>(`${this.baseUrl}/party-member/${partyId}`);
  }

  /** POST /api/party-member/:partyId — GM only */
  addMember(partyId: number, userId: number): Observable<PartyMember> {
    return this.http.post<PartyMember>(`${this.baseUrl}/party-member/${partyId}`, { userId });
  }

  /** DELETE /api/party-member/:partyId/:userId — GM or self */
  removeMember(partyId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/party-member/${partyId}/${userId}`);
  }

  /** GET /api/party-log/:partyId */
  getLogs(partyId: number): Observable<PartyLog[]> {
    return this.http.get<PartyLog[]>(`${this.baseUrl}/party-log/${partyId}`);
  }

  /** GET /api/party-log/:partyId/count */
  getLogCount(partyId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/party-log/${partyId}/count`);
  }

  /** POST /api/party-log/:partyId */
  addLog(partyId: number, body: CreatePartyLogRequest): Observable<PartyLog> {
    return this.http.post<PartyLog>(`${this.baseUrl}/party-log/${partyId}`, body);
  }

  /** DELETE /api/party-log/:partyId/oldest */
  deleteOldestLogs(partyId: number, count: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/party-log/${partyId}/oldest`, {
      body: { count }
    });
  }
}
