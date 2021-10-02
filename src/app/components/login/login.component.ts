import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  error!: string;
  loading = false;
  submitted = false;

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authService: AuthService        
    ) { }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required]
    })
    
  }

  get f(){ return this.loginForm.controls;}

  onSubmit(){
    
    this.submitted = true;

    if(this.loginForm.invalid){
      return;
    }

    this.loading = true;
    this.authService.login(this.f.username.value, this.f.password.value)
    .pipe(first())
    .subscribe
    (
      data => {
        this.router.navigate(['/user/ticket'])
      },
      error =>{
        this.loading = false;  
      }
    )
  }
}
