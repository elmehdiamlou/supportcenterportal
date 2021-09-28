import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Product } from 'src/app/models/Products';
import { ProductServiceService } from '../product-service.service';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['./edit-product.component.css']
})
export class EditProductComponent implements OnInit {

  productId !: number ;
  productForm !: FormGroup;

  message : string = '';
  msgColor! : string;
  loading !: boolean;

 
  constructor(private route: ActivatedRoute,
              private productService: ProductServiceService) 
              {
                this.productForm = new FormBuilder().group({
                  id: [''],
                  name: [''],
                  category: [''],
                  description: ['']
                })
              }


  ngOnInit(): void {
    this.getProduct();
  }

  /* show Product in Edit Form */
  getProduct(){
    this.loading = true;
    this.route.queryParams.subscribe(
      params =>{
        this.productId = params.id;
      }
    )
    this.productService.getProductById(this.productId).subscribe(
      data =>{
        this.loading =false;
        this.productForm.controls['id'].setValue(data.id);
        this.productForm.controls['name'].setValue(data.name);
        this.productForm.controls['category'].setValue(data.category);
        this.productForm.controls['description'].setValue(data.description);
      },
      error =>{
        this.loading =false;
        console.log(`There is no product with this email ${this.productId}`);
      }
    )
  }


  onEditProduct(){
    this.loading = true;
    const msgBox = document.querySelector('.message');
    this.productService.editProduct(this.productForm.value).subscribe(
      data =>{
        this.loading = false;
        this.message = 'Product updated successfully.';
        this.msgColor = 'Green';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      },
      error =>{
        this.loading = false;
        this.message = 'Operation Failed, Please Try Again.';
        this.msgColor = 'Red';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      }
    )
  }
}
