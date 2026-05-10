import { Component, OnInit, inject } from '@angular/core';
import { Navbar } from '../../../shared/components/navbar/navbar';
import { Footer } from '../../../shared/components/footer/footer';
import { UserService } from '../../../core/services/user';

@Component({
  selector: 'app-preferences-page',
  standalone: true,
  imports: [Navbar, Footer],
  templateUrl: './preferences-page.html',
  styleUrl: './preferences-page.css'
})
export class PreferencesPage implements OnInit {
  private userService = inject(UserService);

  currentTheme: 'LIGHT' | 'DARK' = 'DARK';
  saving  = false;
  saved   = false;
  error   = '';

  ngOnInit(): void {
    this.userService.getPreferences().subscribe({
      next: pref => this.currentTheme = pref.theme,
      error: () => {}
    });
  }

  selectTheme(theme: 'LIGHT' | 'DARK'): void {
    if (this.currentTheme === theme) return;

    this.currentTheme = theme;
    this.saving = true;
    this.saved  = false;
    this.error  = '';

    this.userService.updateTheme(theme).subscribe({
      next: () => {
        this.saving = false;
        this.saved  = true;
        setTimeout(() => this.saved = false, 2000);
      },
      error: () => {
        this.saving = false;
        this.error  = 'Failed to save preference.';
      }
    });
  }
}
