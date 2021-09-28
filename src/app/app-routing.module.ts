import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuardGuard } from './services/guards/auth-guard.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component:RegisterComponent },
  { 
    path: 'user', canActivate: [AuthGuardGuard],
    loadChildren: () => import('./components/user.module').then((m)=>m.UserModule),
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path:'**', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  declarations: [],
})
export class AppRoutingModule { }
