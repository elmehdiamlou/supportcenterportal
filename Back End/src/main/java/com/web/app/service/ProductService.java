package com.web.app.service;

import java.util.List;

import com.web.app.entity.Product;

public interface ProductService {

	Product addProduct(Product product);
	
	void deleteProduct(Long productId);
	
	Product getProductById(Long productId);
	
	List<Product> allProducts();
}