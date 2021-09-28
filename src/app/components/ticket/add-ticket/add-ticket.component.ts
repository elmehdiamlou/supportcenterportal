import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, Validators,FormBuilder } from '@angular/forms';
import { TicketServiceService } from '../ticket-service.service';
import { ProductServiceService } from '../../product/product-service.service';


@Component({
  selector: 'app-add-ticket',
  templateUrl: './add-ticket.component.html',
  styleUrls: ['./add-ticket.component.css']
})
export class AddTicketComponent implements OnInit {
  
  ticketForm !: FormGroup;
  products : any;
  msgText : string = '';
  msgColor! : string;
  loading : boolean = false;

  constructor(private ticketService: TicketServiceService,
              private productService: ProductServiceService,
              private fb : FormBuilder) { }

  ngOnInit(): void {
    this.ticketForm = this.fb.group({
      openDate : ['', Validators.required],
      productId : ['', Validators.required],
      description: ['', Validators.required],
      message: [null]
    })

    this.productService.getAllProducts().subscribe(res=>{
      this.products = res;
      console.log(res); 
    })
  }
 
  /* Add a new Ticket by existing guest */
  onAddTicket(){
    this.loading = true;
    const msgBox = document.querySelector('.message');
    this.ticketService.addTicket(this.ticketForm.value).subscribe(
      response => {
        this.loading = false;
        this.msgText = 'Ticket added successfully.';
        this.msgColor = 'Green';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
        this.ticketForm.reset();
      },
      error =>{
        this.loading = false;
        this.msgText = 'Operation Add Ticket Failed!';
        this.msgColor = 'red';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      }
    )
  }





}
