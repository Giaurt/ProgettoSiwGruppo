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
	@Transactional
	public Product findById(long id) {
		return productRepository.findById(id).orElse(null);
	}
	@Transactional
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

	@Transactional
	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
    }

}
