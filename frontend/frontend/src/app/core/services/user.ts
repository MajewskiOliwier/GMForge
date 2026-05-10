import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { User, UserPreference, UpdateThemeRequest } from '../../shared/models';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UserService {
  private http    = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/users`;

  /** GET /api/users/all */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/all`);
  }

  /** GET /api/users/me */
  getMe(): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/me`);
  }

  /** GET /api/users/me/preferences */
  getPreferences(): Observable<UserPreference> {
    return this.http.get<UserPreference>(`${this.baseUrl}/me/preferences`);
  }

  /**
   * PUT /api/users/me/preferences/theme
   * Saves the theme to the DB and applies the CSS class to <body>.
   */
  updateTheme(theme: 'LIGHT' | 'DARK'): Observable<UserPreference> {
    const body: UpdateThemeRequest = { theme };
    return this.http
      .put<UserPreference>(`${this.baseUrl}/me/preferences/theme`, body)
      .pipe(tap(() => this.applyTheme(theme)));
  }

  /** Applies the theme CSS class to the document body. */
  applyTheme(theme: 'LIGHT' | 'DARK'): void {
    const body = document.body;
    body.classList.remove('theme-dark', 'theme-light');
    body.classList.add(theme === 'DARK' ? 'theme-dark' : 'theme-light');
  }
}
