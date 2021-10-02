import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/models/Products';
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
  
  constructor(private productService: ProductServiceService ) { }

  ngOnInit(): void {
    this.user_role;
    this.allProducts();
  }

  allProducts(){
    this.loading = true;
    this.productService.getAllProducts().subscribe(
      data =>{
        this.loading = false;
        console.log(data);
        this.products = data;
      },
      error =>{
        console.log("Something went wrong!!! no products");
      }
    )
  }

    deleteProduct(id: number){
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.add('show');
      deleteBtn?.classList.add('active');
      this.productId = id;
    }

    cancelDeleteCart(){
      const deleteBtn = document.querySelector('.content');
      const cart = document.querySelector('.cart');
      cart?.classList.remove('show');
      deleteBtn?.classList.remove('active');
    }

  deleteProductById(){
    this.productService.deleteProductById(this.productId).subscribe(
      response =>{
        console.log(response);
        this.cancelDeleteCart();
        this.allProducts();
      },
      error =>{
        console.log("SomeThing Went Wrong.");
        this.cancelDeleteCart();
      }
    )
  }

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
