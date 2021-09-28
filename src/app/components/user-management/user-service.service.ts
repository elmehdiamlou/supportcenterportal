import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  token : any = localStorage.getItem("token");

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<User> {
    const headers = { 'Authorization': this.token};
    return this.http.get<User>(`${environment.baseUrl}/api/management/all`, {headers});
  }
  getUser(id: any): Observable<User> {
    const headers = { 'Authorization': this.token};
    return this.http.get<User>(`${environment.baseUrl}/api/management/edit/`+ id, {headers});
  }
  saveUser(user: any) {
    const headers = { 'Authorization': this.token};
    return this.http.put<any>(`${environment.baseUrl}/api/management/save`, user, {headers});
  }
  deleteUser(id: any) {
    const headers = { 'Authorization': this.token};
    return this.http.delete<any>(`${environment.baseUrl}/api/management/delete/`+ id, {headers});
  }
  addUser(user: any){
    const headers = { 'Authorization': this.token};
    return this.http.post<any>(`${environment.baseUrl}/api/management/adduser`, user, {headers});
  }
}
