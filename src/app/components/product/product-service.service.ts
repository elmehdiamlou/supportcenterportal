import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'src/app/models/Products';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {

  token : any = localStorage.getItem('token');

  constructor(private http: HttpClient) { }


  getAllProducts(){
    const headers = { 'Authorization': this.token};
    return this.http.get<Product[]>(`${environment.baseUrl}/api/product/all`, {headers});
  }

  deleteProductById(id: number){
    const headers = { 'Authorization': this.token};
    return this.http.delete(`${environment.baseUrl}/api/product/delete`, {
                                headers, 
                                params: new HttpParams().set('productId', id)
                              } 
                           );
  }

  
  addProduct(product: Product){
    const headers = {'Authorization' : this.token};
    return this.http.post<Product>(`${environment.baseUrl}/api/product/add`,product, {headers});
  }

  getProductById(id: number){
    const headers = { 'Authorization': this.token };
    return this.http.get<Product>(`${environment.baseUrl}/api/product/get`, {
                             headers,
                             params: new HttpParams().set('productId', id)
                            });
  }
  editProduct(product: Product){
    const headers = {'Authorization': this.token };
    return this.http.post<Product>(`${environment.baseUrl}/api/product/edit`,product, {headers});
  }

}
