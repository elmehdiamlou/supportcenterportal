import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable} from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TicketServiceService {

  token : any = localStorage.getItem("token");

  constructor(private http: HttpClient) { }

   getGuestTickets(){
  
    return this.http.get<any>(`${environment.baseUrl}/api/ticket/all`, {params: new HttpParams().set('authorization', this.token)});
  }

  addTicket(ticket : any): Observable<any>{    
    const headers = { 'Authorization': this.token};
    console.log(`${environment.baseUrl}/api/ticket/add`, ticket, {headers})
    return this.http.post<any>(`${environment.baseUrl}/api/ticket/add`, ticket, {headers});
  }

  getTicketById(id: number){
    const params = new HttpParams()
                          .set("authorization", this.token)
                          .set("ticketId", id)
    return this.http.get<any>(`${environment.baseUrl}/api/ticket/get`,{'params': params}, 
                              ).pipe(
                                catchError((err)=>{
                                  console.error(err);
                                  throw err;
                                })
                              );
  }

  deleteTicket(id: number){
    const params = new HttpParams().set('authorization', this.token)
                                   .set('ticketId', id)
    return this.http.delete(`${environment.baseUrl}/api/ticket/delete`,{'params': params},
                    ).pipe(
                      catchError((err)=>{
                        console.log(err);
                        throw err;
                      })
                    );
  }


  sendMsg(message: string, ticketId: number){
    const params = new HttpParams()
                          .set('token', this.token)
                          .set('ticketId', ticketId)
                          .set('message', message);
    return this.http.get<any>(`${environment.baseUrl}/api/ticket/sendMessage`,{'params':params},
                        ).pipe(
                          catchError((err)=>{
                            console.error(err);
                            throw err;
                          })
                        )
  }

   getUnAssignTickets(){
    const headers = {'Authorization': this.token};
    return this.http.get<any>(`${environment.baseUrl}/api/ticket/unassignTickets`, {headers});
   }

   getAssignTickets(){
     const headers = {'Authorization': this.token};
     return this.http.get<any>(`${environment.baseUrl}/api/ticket/assignTickets`, {headers})
                                         .pipe(
                                           catchError((err)=>{
                                             console.error(err);
                                             throw err;
                                           })
                                         );
   }

   assignOrUnassignTicketToTech(id: number){
     //const headers = new HttpHeaders({'Authorization': this.token});
     const params = new HttpParams().set('token', this.token)
     return this.http.get<any>(`${environment.baseUrl}/api/ticket/assign/${id}`, {'params': params})
                          .pipe(
                            catchError((err)=>{
                              console.error(err);
                              throw err;
                            })
                          );
   }

   changeTicketStatus(status: string, ticketId: number){
     const headers = { 'Authorization': this.token};
     return this.http.get(`${environment.baseUrl}/api/ticket/changeStatus`,{ 
                               headers,
                               params: new HttpParams()
                                                .set("ticketStatus", status)
                                                .set("ticketId", ticketId)
                             }
                          );
   }

   getAllTickets(){
     const headers = { 'Authorization': this.token};
     return this.http.get(`${environment.baseUrl}/api/ticket/allTickets`,{headers});
   }

}
