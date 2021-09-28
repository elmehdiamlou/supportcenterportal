import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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

  constructor(private authService: AuthService, private ticketService: TicketServiceService) { }

  ngOnInit(): void {
    this.user_role;
    this.username;
  }

  logout(){
    this.authService.logout();
  }
}
