import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register-page.html',
  styleUrl: './register-page.css'
})
export class RegisterPage {
  private authService = inject(Auth);
  private router      = inject(Router);

  username  = '';
  email     = '';
  password  = '';
  error     = '';
  loading   = false;

  onSubmit(): void {
    if (!this.username.trim() || !this.email.trim() || !this.password.trim()) {
      this.error = 'Please fill in all fields.';
      return;
    }

    if (this.password.length < 4) {
      this.error = 'Password must be at least 4 characters.';
      return;
    }

    this.loading = true;
    this.error   = '';

    this.authService.register(this.username.trim(), this.email.trim(), this.password).subscribe({
      next: () => this.router.navigate(['/home']),
      error: err => {
        this.loading = false;
        if (err.status === 409) {
          this.error = 'Username or email already taken.';
        } else {
          this.error = 'Registration failed. Please try again.';
        }
      }
    });
  }
}
