import { Component, OnInit } from '@angular/core';
import {  FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  error!: string;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService  
  ) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
      phone: ['', [Validators.required, Validators.pattern('(\\+212|0)([\\-_/]*)(\\d[\\-_/]*){9}')]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmpassword: ['', Validators.required]
  })
  }

  get f(){ return this.registerForm.controls;}

  onSubmit(){

    this.submitted = true;
    if (this.registerForm.invalid) {
        return;
    }

      if (this.f.password.value !== this.f.confirmpassword.value) {
        this.f.confirmpassword.setErrors({ notEqual: true });
        return
      } else {
        this.f.confirmpassword.setErrors(this.f.confirmpassword.errors ? { ...this.f.confirmpassword.errors } : null);
      }
      
    this.loading = true;
    this.authService.register(this.registerForm.value)
    .pipe(first())
    .subscribe(
        data => {
          this.router.navigate(['../login']);
        },
        error => {
          this.loading = false;
        });
  }
}