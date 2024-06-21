package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
