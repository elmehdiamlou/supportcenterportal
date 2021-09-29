import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Observable, Subject } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardGuard implements CanActivate {

  token = localStorage.getItem("token");
  constructor(private authService: AuthService, private http: HttpClient){}

  canActivate(): Observable<boolean>{  
    const headers = { 'Authorization': this.token!};  
    var response = new Subject<boolean>();
    this.http.get<any>(`${environment.baseUrl}/api/management/role`, {headers}).subscribe( data => {
      if(this.token != null && data['message'] == 'Admin'){
        response.next(true);
      } else {               
        this.authService.logout();
        response.next(false);
      }    
    })
   return response.asObservable();
 }
}
