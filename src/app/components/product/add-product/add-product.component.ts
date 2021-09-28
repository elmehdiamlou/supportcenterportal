import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductServiceService } from '../product-service.service';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {

  productForm !: FormGroup;

  loading !: boolean;
  message : string = '';
  msgColor! : string;

  constructor(private fb: FormBuilder, private productService: ProductServiceService) { }

  ngOnInit(): void {
    this.productForm = this.fb.group({
      name : ['', Validators.required],
      category : ['', Validators.required],
      description : ['', Validators.required]
    })
  }

  onAddProduct(){
    this.loading = true;
    const msgBox = document.querySelector('.message');
    this.productService.addProduct(this.productForm.value).subscribe(
      response =>{
        this.loading = false;
        this.message = 'Product added successfully.';
        this.msgColor = 'Green';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
        this.productForm.reset();
      },
      error =>{
        this.loading = false;
        this.message = 'Operation Add Ticket Failed!';
        this.msgColor = 'Red';
        msgBox?.classList.add('active');
        setTimeout(()=>{msgBox?.classList.remove('active')},4500);
      }
    )
  }
}
