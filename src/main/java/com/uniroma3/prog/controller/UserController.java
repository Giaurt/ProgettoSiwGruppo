package com.uniroma3.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.UserService;

import jakarta.transaction.Transactional;

@Controller
public class UserController {

	@Autowired
    private UserService userService;
	@Autowired 
	private CredentialsService credentialsService;
	
	@GetMapping(value = "/user/{nomeUtente}")
	@Transactional
	public String getUser(@PathVariable("nomeUtente") String nomeUtente, Model model) {
		Credentials userCredential = this.credentialsService.getCredentialsByUsername(nomeUtente);
		model.addAttribute("userCredential", userCredential);
		model.addAttribute("reviews", userService.getUserReview(nomeUtente));

		return "user.html";
	}

}
