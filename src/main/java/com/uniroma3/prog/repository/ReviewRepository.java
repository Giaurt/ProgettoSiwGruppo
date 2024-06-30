package com.uniroma3.prog.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long>{
	List<Review> findByProdotto(Product product);
}
