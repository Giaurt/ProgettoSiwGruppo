package com.uniroma3.prog.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
