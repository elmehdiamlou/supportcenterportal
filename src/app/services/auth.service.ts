import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private http: HttpClient, private router: Router) {}
  
  /* registration */
  register(user: any): Observable<any> {
    return this.http.post<any>(`${environment.baseUrl}/api/auth/register`, user);
  }

  /* Login */
  login(username: string, password: string): Observable<any>{
    return this.http.post<any>(`${environment.baseUrl}/api/auth/login`, {username, password})
    .pipe(map
      ( data => {
           localStorage.setItem('username', username);
           localStorage.setItem('role', data.roles);
           localStorage.setItem('token', data.accessToken);
           return data;
          }
       )
    )
  }

  /* logout */
  logout(){
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.router.navigate(["/login"]);
  }
}