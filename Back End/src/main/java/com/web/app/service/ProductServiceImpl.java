package com.web.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.app.entity.Product;
import com.web.app.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;
	
	
	@Override
	public Product addProduct(Product product) {
		return this.productRepo.save(product);
	}

	@Override
	public void deleteProduct(Long productId) {
		this.productRepo.deleteById(productId);
	}

	@Override
	public Product getProductById(Long productId) {
		return this.productRepo.findById(productId).get();
	}

	@Override
	public List<Product> allProducts() {
		return this.productRepo.findAll();
	}

}