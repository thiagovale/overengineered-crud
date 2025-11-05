import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'clients',
    component: AppComponent,
    canActivate: [authGuard],
  },
  {
    path: '',
    redirectTo: '/clients',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: '/clients',
  },
];
