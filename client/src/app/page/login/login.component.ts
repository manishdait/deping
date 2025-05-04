import { Component, inject, signal } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthRequest } from '../../model/auth.type';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  authService = inject(AuthService);

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

  submit() {
    if (this.form.invalid) {
      this.formError.set(true);
      return;
    }

    this.formError.set(false);

    const req: AuthRequest = {
      email: this.form.get('email')!.value,
      password: this.form.get('password')!.value
    }

    this.authService.authenticateUser(req).subscribe({
      next: (res) => {
        console.log(res);
      }
    })
  }
}
