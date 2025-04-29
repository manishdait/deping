import { Routes } from '@angular/router';

export const routes: Routes = [
  {path: 'sign-up', loadComponent: () => import('./page/sign-up/sign-up.component').then(c => c.SignUpComponent)},
  {path: 'login', loadComponent: () => import('./page/login/login.component').then(c => c.LoginComponent)}
];
