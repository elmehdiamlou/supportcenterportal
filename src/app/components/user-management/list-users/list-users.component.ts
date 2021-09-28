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
  deleteUser(id: any)
  {
    if(confirm("Are you sure about deleting user id "+id+"?")){
      this.userService.deleteUser(id).subscribe( 
        data => {
          this.allUsers();
        }
      );
    }
  }
}
