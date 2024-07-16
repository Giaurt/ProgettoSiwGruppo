package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Category;
import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.ProductRepository;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.ProductService;
import com.uniroma3.prog.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;
    
    @Autowired 
    private ProductService productService;
    @Autowired 
    private ProductRepository productRepository;

    @GetMapping(value = "/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof AnonymousAuthenticationToken) {
        	model.addAttribute("products", this.productRepository.findAll());
            model.addAttribute("categories", Category.values());
            return "index";
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            	model.addAttribute("products", this.productRepository.findAll());
                model.addAttribute("categories", Category.values());
                return "admin/indexAdmin";
            }else {
            	model.addAttribute("products", this.productRepository.findAll());
                model.addAttribute("categories", Category.values());
                return "index.html";
            }
        }
    }

    @GetMapping(value = "/profile")
    @Transactional
    public String showProfile(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();
		model.addAttribute("credentials", credentials);
		model.addAttribute("reviews", userService.getUserReview(credentials.getUsername()));
		return "profile.html";
	}

    @GetMapping(value = "/login")
    public String showLoginForm(Model model) {
        return "loginForm";
    }

    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
    	model.addAttribute("products", this.productService.findAll());
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
        return "index.html";
    }

    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "registerForm";
    }

    @PostMapping(value = "/register")
    public String registerUser(
             @Valid @ModelAttribute("user") User user,
             BindingResult userBindingResult,
             @Valid @ModelAttribute("credentials") Credentials credentials,
             BindingResult credentialsBindingResult,
             Model model)
    {
        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "loginForm";
        }
        return "register";
    }

    @GetMapping(value = "/admin/page")
    public String showAdminPage(Model model) {
        return "admin/admin-page";
    }

}
