package com.uniroma3.prog.service;

import com.uniroma3.prog.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

	@Transactional(readOnly = true)
	public List<Product> searchProduct(String keyword) {
		return productRepository.findByNameContainingIgnoreCase(keyword);
	}

	@Transactional(readOnly = true)
	public List<Product> searchProductsByCategory(Category category) {
		return productRepository.findByCategoryNameContainingIgnoreCase(category.name());
	}

}
