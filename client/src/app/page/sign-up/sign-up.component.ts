import { Component, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Role } from '../../model/role.type';
import { UserRegistration, ValidatorRegistration } from '../../model/auth.type';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  role = signal<Role>('User');
  formError = signal<boolean>(false);
  
  form: FormGroup;

  constructor() {
    this.form = new FormGroup({
      uname: new FormControl(''),
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

    if (this.role() === 'User' && this.form.get('uname')!.value === '') {
      this.formError.set(false);
      return;
    }

    this.formError.set(false);

    if (this.role() === 'User') {
      this.createUser();
    } else {
      this.createValidator();
    }
  }

  private createUser() {
    const req: UserRegistration = {
      uname: this.form.get('uname')!.value,
      email: this.form.get('email')!.value,
      password: this.form.get('password')!.value
    }
  }

  private createValidator() {
    const req: ValidatorRegistration = {
      email: this.form.get('email')!.value,
      password: this.form.get('password')!.value
    }
  }
}
