package com.uniroma3.prog.controller;

import java.util.List;

import com.uniroma3.prog.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.uniroma3.prog.repository.ImageRepository;
import com.uniroma3.prog.repository.ProductRepository;
import com.uniroma3.prog.repository.ReviewRepository;
import com.uniroma3.prog.service.CredentialsService;
import com.uniroma3.prog.service.ProductService;
import com.uniroma3.prog.service.ReviewService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import static com.uniroma3.prog.model.Credentials.ADMIN_ROLE;

@Controller
public class ProductController {

	@Autowired ProductService productService;
	@Autowired ImageRepository imageRepository;
	@Autowired ReviewRepository reviewRepository;
	@Autowired ReviewService reviewService;
	@Autowired CredentialsService credentialsService;

	@GetMapping(value = "/index")
	public String showIndex(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "index";
	}

	@GetMapping(value = "/admin/index")
	public String showAdminIndex(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "admin/indexAdmin";
	}

	@GetMapping("/products/{id}")
	public String getProduct(@PathVariable("id")Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Product product = productService.findById(id);
		List<Review> reviews = reviewRepository.findByProdotto(product);

		int totalStars = reviews.stream().mapToInt(Review::getStars).sum();
		int userCount = reviews.size();
		double averageRating = (userCount != 0) ? (int) Math.round((double) totalStars / userCount) : 0;
		System.out.println(averageRating);

		model.addAttribute("averageRating", averageRating);
		model.addAttribute("product", this.productService.findById(id));
		model.addAttribute("reviews", this.reviewRepository.findByProdotto(product));
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
		if (credentials.getRole().equals(ADMIN_ROLE)) {
			return "/admin/admin-product.html";
		}
		return "product.html";
	}
	
	@GetMapping(value="/admin/newproduct")
	public String formNewProduct(Model model) {
		model.addAttribute("product", new Product());
		return "/admin/formNewProduct.html";
	}
	
	@PostMapping(value={"/product"}, consumes = "multipart/form-data")
	public String newProduct(@Valid @ModelAttribute("product") Product product,@RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		
		if (!bindingResult.hasErrors()) {
			try {
				Image i=new Image();
				i.setImageData(file.getBytes());

				product.setImage(i);
				this.imageRepository.save(i);
			} catch (Exception e) {
				System.out.println("erroreeee");
			}
			this.productService.saveProduct(product);
			model.addAttribute("product", product);
			return "product.html";
		} else {
			return "formNewProduct.html"; 
		}
	}
	
	@PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable ("productId") Long productId) {
		
        productService.deleteProduct(productId);
        return "index.html";
    }

	@GetMapping(value="/formNewReview/{id}")
	public String formNewReview(@PathVariable Long id, Model model) {
	    Product product = productService.findById(id);
	    if (product == null) {
			return "redirect:/";
		}
	    model.addAttribute("review", new Review());
	    model.addAttribute("product", product);
	    return "formNewReview.html";
	}

	@PostMapping("/newReview/{id}")
	@Transactional
	public String newReview(@PathVariable Long id, @Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
	    
	    Product product = productService.findById(id);
	    if (product == null) {
	        return "redirect:/";
	    }

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("product", product);
	        return "formNewReview.html";
	    }

	    boolean reviewExists = product.getReview().stream()
	            .anyMatch(r -> r.getNomeUtente().equals(credentials.getUsername()));

	    if (!reviewExists) {
	        Review newReview = new Review();
	        newReview.setNomeUtente(credentials.getUsername());
	        newReview.setProdotto(product);
	        newReview.setStars(review.getStars());
	        newReview.setDescrizione(review.getDescrizione());

	        product.getReview().add(newReview);

	        reviewRepository.save(newReview);
	        productService.saveProduct(product);

	        return "redirect:/products/" + product.getId();
	    } else {
	        return "redirect:/products/" + product.getId();
	    }
	}

	@PostMapping(value = "/product/{productId}/delete/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Long id, @PathVariable("productId") String productId) {
        
        reviewService.deleteReviewById(id);
        
        return "redirect:/products/" + productId;
    }

	@GetMapping(value = "/category/{category}")
	public String getCategory(@PathVariable("category") Category category, Model model) {
	    Iterable<Product> products = this.productService.searchProductsByCategory(category);
	    model.addAttribute("products", products);
	    model.addAttribute("category", category);
	    return "category.html";
	}

}
