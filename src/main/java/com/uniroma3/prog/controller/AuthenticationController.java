package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.service.CredentialsService;
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

    @GetMapping(value = "/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/index";
        }
        else {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
                return "redirect:/admin/index";
            }
        }
        return "redirect:/index";
    }

    @GetMapping(value = "/profile")
    @Transactional
    public String showProfile(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
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
        return "redirect:/";
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
        if (credentialsService.existsByUsername(credentials.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "registerForm";
        }
        if (userBindingResult.hasErrors() || credentialsBindingResult.hasErrors() ) {
            model.addAttribute("error", "Binding errors occurred");
            return "registerForm";
        }

        credentials.setUser(user);
        userService.saveUser(user);
        credentialsService.saveCredentials(credentials);
        model.addAttribute("user", user);
        return "loginForm";
    }

}
