import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import { TicketServiceService } from '../ticket-service.service';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-explore-ticket',
  templateUrl: './explore-ticket.component.html',
  styleUrls: ['./explore-ticket.component.css']
})
export class ExploreTicketComponent implements OnInit {


  userRole = localStorage.getItem('role');
  ticketId !: number ;
  ticketForm !: FormGroup;
  isDisable !: boolean;
  messages : any;
  messageText !: string;
  ticketStatus !: string;
  loading : boolean = false;
 
  constructor(private route: ActivatedRoute,
              private ticketService: TicketServiceService) {
                this.ticketForm = new FormBuilder().group({
                  ticketId:[{value: '', disabled:true}],
                  status:[{value: '', disabled:true}],
                  openDate:[{value: '', disabled:true}],
                  productName:[{value: '', disabled:true}],
                  description:[{value: '', disabled:true}]
                })
              }

  ngOnInit(): void {
    this.userRole;
    this.getTicket();
  }

  getTicket(){
    this.loading = true;
    this.route.queryParams.subscribe(
      params =>{ 
        this.ticketId = params.id;
      }
    )
    
    this.ticketService.getTicketById(this.ticketId).subscribe(
      data =>{
        this.loading = false;
        console.log(data);
        this.ticketForm.controls['ticketId'].setValue(data.id);
        this.ticketForm.controls['status'].setValue(data.status);
        this.ticketForm.controls['openDate'].setValue(data.openDate);
        this.ticketForm.controls['description'].setValue(data.description);  
        this.ticketForm.controls['productName'].setValue(data.productName);  
        this.messages = data.messages;  
        this.isDisable = data.status === 'Close'?true:false;
        console.log(`=====> ${this.isDisable}`);
      },
      error =>{
        this.loading = false;
        console.log(error.error.message);
      }
    )
  }
  
  changeTicketStatus(){
    console.log(this.ticketStatus);
    this.ticketService.changeTicketStatus(this.ticketStatus, this.ticketId).subscribe(
      response =>{
        this.getTicket();
      },
      error =>{
        console.log("Operation Failed Status Doesn't Change It.")
      }
    )
  }

  sendMessage(){
      this.ticketService.sendMsg(this.messageText, this.ticketId).subscribe(res=>{
      this.messageText = '';
      this.getTicket();
     },
     error=>{
      console.log("SomeThing went Wrong.")
      }
     )
  }
}
