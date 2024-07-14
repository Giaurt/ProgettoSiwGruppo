package com.uniroma3.prog.repository;

import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.Credentials;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface CredentialsRepository  extends CrudRepository<Credentials, Long> {
	
}
