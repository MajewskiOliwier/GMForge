import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';
import { UserService } from '../../../core/services/user';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css'
})
export class LoginPage {
  private authService = inject(Auth);
  private userService = inject(UserService);
  private router      = inject(Router);

  username  = '';
  password  = '';
  error     = '';
  loading   = false;

  onSubmit(): void {
    if (!this.username.trim() || !this.password.trim()) {
      this.error = 'Please fill in all fields.';
      return;
    }

    this.loading = true;
    this.error   = '';

    this.authService.login(this.username.trim(), this.password).subscribe({
      next: res => {
        // Fetch and apply theme from DB after login
        this.userService.getPreferences().subscribe({
          next: pref => this.userService.applyTheme(pref.theme),
          error: () => {}
        });
        this.router.navigate(['/home']);
      },
      error: err => {
        this.loading = false;
        this.error = err.status === 401
          ? 'Invalid username or password.'
          : 'Something went wrong. Please try again.';
      }
    });
  }
}
