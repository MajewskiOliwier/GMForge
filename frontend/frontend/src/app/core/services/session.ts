import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Session, CreateSessionRequest } from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SessionService {
  private http    = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/session`;

  /** GET /api/session/:partyId — all sessions for a party */
  getSessions(partyId: number): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.baseUrl}/${partyId}`);
  }

  /** GET /api/session/single/:id */
  getSession(id: number): Observable<Session> {
    return this.http.get<Session>(`${this.baseUrl}/single/${id}`);
  }

  /** POST /api/session/:partyId — GM only */
  createSession(partyId: number, body: CreateSessionRequest = {}): Observable<Session> {
    return this.http.post<Session>(`${this.baseUrl}/${partyId}`, body);
  }

  /** DELETE /api/session/:id — GM only */
  deleteSession(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
