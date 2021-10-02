import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserServiceService } from '../user-service.service';

@Component({
  selector: 'app-list-users',
  templateUrl: './list-users.component.html',
  styleUrls: ['./list-users.component.css']
})
export class ListUsersComponent implements OnInit {
  users: any = [];
  message !: any;
  userId !: number;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserServiceService) { }

  ngOnInit(): void {
    this.allUsers();
  }

  allUsers(){
    this.userService.getAllUsers().subscribe(
      data => {
        this.users = data;
      }, 
      error =>{
        console.log(error.error.message);
      }
    )
  }
  deleteUser(id: any){
    this.userService.getUser(id).subscribe(
      data => {this.message = 'Are you sure about deleting user named '+data.userName+' ?'}
      )
    const deleteBtn = document.querySelector('.content');
    const cart = document.querySelector('.cart');
    cart?.classList.add('show');
    deleteBtn?.classList.add('active');
    this.userId = id;
  }

  confirmDelete(){
    this.userService.deleteUser(this.userId).subscribe( 
      response =>{
        this.cancelDelete();
        this.allUsers();
      },
      error =>{
        console.log(error.error.message);
        this.cancelDelete();
      }
    )
  }

  cancelDelete(){
    const deleteBtn = document.querySelector('.content');
    const cart = document.querySelector('.cart');
    cart?.classList.remove('show');
    deleteBtn?.classList.remove('active');
  }
}
