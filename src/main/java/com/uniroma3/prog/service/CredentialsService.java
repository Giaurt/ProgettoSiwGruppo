package com.uniroma3.prog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.repository.CredentialsRepository;

import java.util.Optional;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }
    @Transactional
    public Credentials findByUsername(String username) {
    	Credentials result = new Credentials();
    	Iterable<Credentials> iterable = this.credentialsRepository.findAll();
    	for(Credentials c : iterable) {
    		if(c.getUsername().equals(username)) {
    			result = c;
    		}
    	}
    	return result;
    }


    @Transactional
    public Credentials getCredentials(String username) {
        Credentials result = this.findByUsername(username);
        return result;
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
}
