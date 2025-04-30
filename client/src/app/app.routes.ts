import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'sign-up'
  },
  {
    path: 'sign-up', 
    loadComponent: () => import('./page/sign-up/sign-up.component').then(c => c.SignUpComponent)
  },
  {
    path: 'login', 
    loadComponent: () => import('./page/login/login.component').then(c => c.LoginComponent)
  },
  {
    path: 'user',
    loadComponent: () => import('./page/user/user.component').then(c => c.UserComponent)
  },
  {
    path: 'validator',
    loadComponent: () => import('./page/validator/validator.component').then(c => c.ValidatorComponent)
  }
];
