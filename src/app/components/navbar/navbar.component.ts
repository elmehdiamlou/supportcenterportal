import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { TicketServiceService } from '../ticket/ticket-service.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  
  user_role: any = localStorage.getItem("role");
  username: any = localStorage.getItem("username");
  token: any = localStorage.getItem("token");

  constructor(private authService: AuthService,
             private ticketService: TicketServiceService) { }

  ngOnInit(){
    this.username;
    this.user_role;
  }

  logout(){
    this.authService.logout();
  }

  }
