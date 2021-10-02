import { Component, OnInit, OnDestroy } from '@angular/core';
import { Product } from 'src/app/models/Products';
import { AuthService } from 'src/app/services/auth.service';
import { ProductServiceService } from '../product-service.service';

@Component({
  selector: 'app-list-product',
  templateUrl: './list-product.component.html',
  styleUrls: ['./list-product.component.css']
})
export class ListProductComponent implements OnInit {

  user_role = localStorage.getItem('role');
  products : Product[] = [];
  productId !: number;
  loading : boolean = true;
  message : string = '';

  constructor(private productService: ProductServiceService, private authService: AuthService ) { }

  ngOnInit(){
    this.user_role;
    this.allProducts();
  }

 
  /* Show All Products */
  allProducts(){
    this.loading = true;
    this.productService.getAllProducts().subscribe(
      data =>{
        this.loading = false;
        this.products = data;
      }
    )
  }

   /*show delete ticket cart*/
    deleteProduct(id: number){
      this.productService.getProductById(id).subscribe(
        resp => {this.message = resp.status?'Some tickets have this product, are you sure you want to delete it?':'Are you sure you want to delete this product?'}
        )
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.add('show');
      deleteBtn?.classList.add('active');
      this.productId = id;
    }

    /*Cancel Delete Cart */
    cancelDeleteCart(){
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.remove('show');
      deleteBtn?.classList.remove('active');
    }
  /* Delete Product */
  deleteProductById(){
    this.productService.deleteProductById(this.productId).subscribe(
      response =>{
        this.cancelDeleteCart();
        this.allProducts();
      },
      error =>{
        this.cancelDeleteCart();
      }
    )
  }

  /* Filtering Table */
      searchProduct(key: any):void{
      const result = [];
      for(const product of this.products){
        if(product.id.toString().indexOf(key.toString()) !== -1 ||
           product.name.toLowerCase().indexOf(key.toLowerCase()) !== -1 || 
           product.description.toLowerCase().indexOf(key.toLowerCase()) !== -1 ||
           product.category.toLowerCase().indexOf(key.toLowerCase()) !== -1){
             result.push(product);
        }
      }
      this.products = result;
      if(result.length === 0 || !key){
        this.allProducts();
      }
    }


}
