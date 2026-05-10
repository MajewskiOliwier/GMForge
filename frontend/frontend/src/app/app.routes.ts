import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  // Public
  {
    path: 'welcome',
    loadComponent: () =>
      import('./features/auth/welcome-page/welcome-page')
        .then(m => m.WelcomePage)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login-page/login-page')
        .then(m => m.LoginPage)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register-page/register-page')
        .then(m => m.RegisterPage)
  },

  // Protected
  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/home/home-page/home-page')
        .then(m => m.HomePage)
  },
  {
    path: 'party/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/party/party-page/party-page')
        .then(m => m.PartyPage)
  },
  {
    path: 'party/:id/logs',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/party/party-logs-page/party-logs-page')
        .then(m => m.PartyLogsPage)
  },
  {
    path: 'settings/preferences',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/settings/preferences-page/preferences-page')
        .then(m => m.PreferencesPage)
  },

  // Redirects
  { path: '',        redirectTo: 'welcome', pathMatch: 'full' },
  {
    path: '**',
    loadComponent: () =>
      import('./features/errors/not-found-page/not-found-page')
        .then(m => m.NotFoundPage)
  },
];
