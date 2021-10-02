import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { TicketServiceService } from '../ticket-service.service';




@Component({
  selector: 'app-list-ticket',
  templateUrl: './list-ticket.component.html',
  styleUrls: ['./list-ticket.component.css']
})
export class ListTicketComponent implements OnInit{

  user_role: any  = localStorage.getItem('role');
  

  guest_tickets : any[] = [];
  ticketId !: number;

  tech_unassign_tickets : any = [] ;
  tech_assign_tickets : any = [] ;

  all_tickets : any = [] ;

  message !: any;
  msgColor !: string;
  loading : boolean = false;

  constructor(private ticketService: TicketServiceService, private authService : AuthService) { }

  ngOnInit(){
    this.user_role;
    this.allGuestTickets();
    this.allUnAssignTickets();
    this.allAssignTickets();
    this.allTickets();
  }

  /* ================ Guest User ====================*/
  /* Getting tickets of existing guest */
  allGuestTickets(){
    this.loading = true;
    this.ticketService.getGuestTickets().subscribe(
      data => {
        this.loading = false;
        this.guest_tickets = data;
      }, 
      error =>{
        this.loading = false;
      })}

    /*=================== Technician User====================*/
    
    /*Getting no assign tickets for existing technician */
    allUnAssignTickets(){
      this.loading= true;
      this.ticketService.getUnAssignTickets().subscribe(
        data => {
          this.loading = false;
          this.tech_unassign_tickets = data;
        },
        error =>{
          this.loading = false;
        }
      )
    }

    /*Getting assign tickets for existing technician */
    allAssignTickets(){
      this.loading = true;
      this.ticketService.getAssignTickets().subscribe(
        data =>{
          this.loading = false;
          this.tech_assign_tickets = data;
        },
        error =>{
          this.loading = false;
        }
      )
    }

    /* Assigned Or Unassign ticket to current technician */
    assignOrUnassignTicket(id: number){
      const msgBox = document.querySelector('#message2');
      this.ticketService.assignOrUnassignTicketToTech(id).subscribe(
        response =>{
          this.message = "Operation is done Successfully";
          this.msgColor = 'Green';
          msgBox?.classList.add('active');
          this.allAssignTickets();
          this.allUnAssignTickets();
          setTimeout(()=>{msgBox?.classList.remove('active')},4400);
        },
        error =>{
          this.message = "Operation Failed, Please Try Again!";
          this.msgColor = 'Red';
          msgBox?.classList.add('active');
          setTimeout(()=>{msgBox?.classList.remove('active')},4400);
        }
      )
    }


  /* ================ Guest User ====================*/
  /* Getting ALl Tickets */
  allTickets(){
    this.loading = true;
    this.ticketService.getAllTickets().subscribe(
      data =>{
        this.loading = false;
        this.all_tickets = data;
      },
      error =>{
        this.loading = false;
      }
    )
  } 


  /* =============Common Methods============== */
  
  
    /*filtering table with status*/
    searchTicket(key: string):void{
      const result = [];
      let tickets = [];
      switch(this.user_role){
        case 'Admin':{
          tickets = this.all_tickets;
          break;
        }
        case 'Technician':{
          tickets = this.tech_unassign_tickets;
          break;
        }
        default: {
          tickets = this.guest_tickets;
          break;
        }
      }
      for(const ticket of tickets){
        let status = (ticket.status?'open':'close');
        if(status.indexOf(key.toLowerCase()) !== -1 || 
           ticket.id.toString().indexOf(key.toString()) !== -1||
           ticket.openDate.toString().indexOf(key.toString()) !== -1||
           ticket.product?.name.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
           ticket.description.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
           ticket.guest.userName.toLowerCase().indexOf(key.toLowerCase()) !== -1){
             result.push(ticket);
        }
      }
      switch(this.user_role){
        case 'Admin':{
          this.all_tickets = result;
          break;
        }
        case 'Technician':{
          this.tech_unassign_tickets = result;
          break;
        }
        default: {
          this.guest_tickets = result;
          break;
        }
      }
      if(result.length === 0 || !key){
        this.allUnAssignTickets();
        this.allGuestTickets();
        this.allTickets();
  
      }
    }

    /*show delete ticket cart*/
    deleteTicket(id: number){
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.add('show');
      deleteBtn?.classList.add('active');
      this.ticketId = id;
    }

    /*Cancel Delete Cart */
    cancelDeleteCart(){
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.remove('show');
      deleteBtn?.classList.remove('active');
    }

    /* Delete Ticket Cart By Id */
    deleteTicketById(){
      const msgBox = document.querySelector('#message1');
      this.ticketService.deleteTicket(this.ticketId).subscribe(
        response =>{
          this.message = "Ticket Deleted Successfully.";
          this.msgColor = 'Green';
          msgBox?.classList.add('active'); 
          setTimeout(()=>{msgBox?.classList.remove('active')},5000);
          this.cancelDeleteCart();
          this.allGuestTickets();
          this.allTickets();
          this.allAssignTickets();
        },
        error =>{
          this.cancelDeleteCart();
          this.message = "Please Try Again. Operation Failed.";
          this.msgColor = 'Red';
          msgBox?.classList.add('active'); 
          setTimeout(()=>{msgBox?.classList.remove('active')},5000);
          this.cancelDeleteCart();
        }
      )
    }


}
