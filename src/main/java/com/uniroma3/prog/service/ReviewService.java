package com.uniroma3.prog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniroma3.prog.model.Review;
import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.repository.ReviewRepository;


@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductService productService;
	
	public Review findById(long id) {
		return reviewRepository.findById(id).get();
	}
	public Iterable<Review> findAll(){
		return reviewRepository.findAll();
	}
	 public List<Review> findReviewsByProduct(Long productId) {
	        Product product = productService.findById(productId);

	        return reviewRepository.findByProdotto(product);
	    }
	
}
