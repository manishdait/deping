import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RegistrationRequest, Role } from '../../model/auth.type';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  authService = inject(AuthService);
  router = inject(Router);

  role = signal<Role>('User');
  formError = signal<boolean>(false);
  
  form: FormGroup;

  constructor() {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    });
  }

  get control() {
    return this.form.controls;
  }

  toggleRole(role: Role) {
    this.role.set(role);
  }

  submit() {
    if (this.form.invalid) {
      this.formError.set(true);
      return;
    }

    this.formError.set(false);

    const req: RegistrationRequest = {
      email: this.form.get('email')!.value,
      password: this.form.get('password')!.value,
      role: this.role()
    }

    this.authService.registerUser(req).subscribe({
      next: (res) => {
        if (res.role === 'User') {
          this.router.navigate(['/user'], {replaceUrl: true});
        } else {
          this.router.navigate(['/validator'], {replaceUrl: true});
        }
      }
    })
  }
}
