package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {

}
