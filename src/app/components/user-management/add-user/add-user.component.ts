import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserServiceService } from '../user-service.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  userForm!: FormGroup;
  error!: string;
  loading = false;
  submitted = false;
  roles: any = ["Admin", "Technician", "Guest"]
  message : string = '';
  msgColor! : string;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserServiceService,
  ) { }

  ngOnInit(): void {
    this.userForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
      phone: ['', [Validators.required, Validators.pattern('(\\+212|0)([\\-_/]*)(\\d[\\-_/]*){9}')]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: [null, Validators.required]
  })
  }

  get f(){ return this.userForm.controls;}

  onAddUser(){

    this.submitted = true;
    console.log(this.userForm.value)
    if (this.userForm.invalid) {
      return;
    }

    this.loading = true;
    const msgBox = document.querySelector('.message');
    this.userService.addUser(this.userForm.value)
    .subscribe(
        data => {
        this.loading = false;
        this.message = "User is Added Successfully";
        this.msgColor = 'Green';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      },
      error => {
        this.loading = false;
        this.message = error.error.message;
        this.msgColor = 'Red';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      });
  }
}