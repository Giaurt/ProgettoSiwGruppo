package com.uniroma3.prog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product findById(long id) {
		return productRepository.findById(id).get();
	}
	public Iterable<Product> findAll(){
		return productRepository.findAll();
	}

}