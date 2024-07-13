package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long>{

    List<Product> findByNameContainingIgnoreCase(String keyword);
}
