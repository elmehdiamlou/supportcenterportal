import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {  ReactiveFormsModule, FormsModule } from "@angular/forms";
import { UserRoutingModule } from './user-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarComponent } from './navbar/navbar.component';
import { TicketComponent } from './ticket/ticket.component';
import { ProductComponent } from './product/product.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { AddTicketComponent } from './ticket/add-ticket/add-ticket.component';
import { ExploreTicketComponent } from './ticket/explore-ticket/explore-ticket.component';
import { ListTicketComponent } from './ticket/list-ticket/list-ticket.component';
import { ListProductComponent } from './product/list-product/list-product.component';
import { AddProductComponent } from './product/add-product/add-product.component';
import { EditProductComponent } from './product/edit-product/edit-product.component';
import { ListUsersComponent } from './user-management/list-users/list-users.component';
import { AddUserComponent } from './user-management/add-user/add-user.component';
import { EditUserComponent } from './user-management/edit-user/edit-user.component';



@NgModule({
  declarations: [
                 DashboardComponent,
                 NavbarComponent,
                 TicketComponent,
                 ProductComponent,
                 UserManagementComponent,
                 AddTicketComponent,
                 ExploreTicketComponent,
                 ListTicketComponent,
                 ListProductComponent,
                 AddProductComponent,
                 EditProductComponent,
                 ListUsersComponent,
                 AddUserComponent,
                 EditUserComponent
                ],
  imports: [
    CommonModule,
    UserRoutingModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class UserModule { }
