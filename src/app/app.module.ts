import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from "@angular/forms";

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthService } from './services/auth.service';
import { AuthGuardGuard } from './services/guards/auth-guard.guard';
import { HttpClientModule } from '@angular/common/http';
import { TicketServiceService } from './components/ticket/ticket-service.service';
import { AccessGuardGuard } from './services/guards/access-guard.guard';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [AuthService, AuthGuardGuard, TicketServiceService, AccessGuardGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
