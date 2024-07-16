package com.uniroma3.prog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional
    public Credentials getCredentialsById(Long id) {
        return this.credentialsRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean existsByUsername(String username) {
        return credentialsRepository.existsByUsername(username);
    }

    @Transactional
    public Credentials getCredentialsByUsername(String username) {
        return this.credentialsRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
}
