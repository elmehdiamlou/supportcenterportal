import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  CanActivate } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardGuard implements CanActivate {

  token = localStorage.getItem("token");
  constructor(private authService: AuthService, private http: HttpClient){}
  response!: boolean;

  canActivate(): boolean{  
    const headers = { 'Authorization': this.token!};
    this.http.get<any>(`${environment.baseUrl}/api/management/role`, {headers}).subscribe( data => {
      if(this.token != null && data['message'] == 'Admin'){
        this.response = true;
      } else {        
        this.authService.logout();
        this.response = false;        
      }    
    })
   return this.response;
 }
}
