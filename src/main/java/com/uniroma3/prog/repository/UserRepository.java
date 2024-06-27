package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
