package com.uniroma3.prog.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long>{

    List<Product> findByNameContainingIgnoreCase(String keyword);

    @Query("SELECT p FROM Product p WHERE LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    public List<Product> findByCategoryNameContainingIgnoreCase(@Param("keyword") String keyword);

}
