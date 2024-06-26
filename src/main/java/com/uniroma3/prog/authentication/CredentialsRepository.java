package com.uniroma3.prog.authentication;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialsRepository  extends CrudRepository<Credentials, Long> {

    public Optional<Credentials> findByUsername(String username);

}
