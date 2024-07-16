package com.uniroma3.prog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.model.Review;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.CredentialsRepository;
import com.uniroma3.prog.repository.UserRepository;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.UserService;

import jakarta.transaction.Transactional;

@Controller
public class UserController {
	
	@Autowired
	private CredentialsRepository credentialsRepository;
	@Autowired
    private UserService userService;
	@Autowired 
	private CredentialsService credentialsService;
	
	@GetMapping(value = "/user/{nomeUtente}")
	@Transactional
	public String getUser(@PathVariable("nomeUtente") String nomeUtente, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Credentials userCredential = this.credentialsService.findByUsername(nomeUtente);
		User user = credentials.getUser();
		model.addAttribute("userCredential", userCredential);
		System.out.println(userCredential.getUsername());
		model.addAttribute("reviews", userService.getUserReview(nomeUtente));
		return "user.html";
		
	}

}
