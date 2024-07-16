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
	@Autowired ProductRepository productRepository;
	@Autowired ImageRepository imageRepository;
	@Autowired ReviewRepository reviewRepository;
	@Autowired ReviewService reviewService;
	@Autowired CredentialsService credentialsService;
	

	@GetMapping("/products/{id}")
	public String getProduct(@PathVariable("id")Long id, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			Product product = productService.findById(id);
			List<Review> reviews = reviewRepository.findByProdotto(product);

	        int totalStars = reviews.stream().mapToInt(Review::getStars).sum();
	        int userCount = reviews.size();
	        double averageRating = (userCount != 0) ? (int) Math.round((double) totalStars / userCount) : 0;
	        System.out.println(averageRating);

	        model.addAttribute("averageRating", averageRating);
			model.addAttribute("product", this.productService.findById(id));
			model.addAttribute("reviews", this.reviewRepository.findByProdotto(product));
			return "product.html";
		}else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		    if(credentials.getRole().equals(ADMIN_ROLE)) {
		    	Product product = productService.findById(id);
				List<Review> reviews = reviewRepository.findByProdotto(product);

		        int totalStars = reviews.stream().mapToInt(Review::getStars).sum();
		        int userCount = reviews.size();
		        double averageRating = (userCount != 0) ? (int) Math.round((double) totalStars / userCount) : 0;
		        System.out.println(averageRating);

		        model.addAttribute("averageRating", averageRating);
				model.addAttribute("product", this.productService.findById(id));
				model.addAttribute("reviews", this.reviewRepository.findByProdotto(product));
				return "/admin/admin-product.html";
		    
		    }else {
		    	Product product = productService.findById(id);
				List<Review> reviews = reviewRepository.findByProdotto(product);

		        int totalStars = reviews.stream().mapToInt(Review::getStars).sum();
		        int userCount = reviews.size();
		        double averageRating = (userCount != 0) ? (int) Math.round((double) totalStars / userCount) : 0;
		        System.out.println(averageRating);

		        model.addAttribute("averageRating", averageRating);
				model.addAttribute("product", this.productService.findById(id));
				model.addAttribute("reviews", this.reviewRepository.findByProdotto(product));
				return "product.html";
		    }
		}
		
	    
		
	}
	
	@GetMapping("/products")
	public String showProduct(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "products.html";
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
			this.productRepository.save(product); 
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

	//@GetMapping(value="/formNewReview/{id}")
	//public String formNewIngrediente(@PathVariable Long id,Model model) {
	//	model.addAttribute("review", new Review());
	//	model.addAttribute("product", this.productRepository.findById(id).orElse(null));
	//	return "formNewReview.html";
	//}


//	@GetMapping(value="/formNewReview/{id}")
//	public String formNewReview(@PathVariable Long id,Model model) {
//		model.addAttribute("review", new Review());
//		model.addAttribute("product", this.productRepository.findById(id).orElse(null));
//		return "formNewReview.html";
//	}
//	
//	@PostMapping("/newReview/{id}")
//	@Transactional
//	public String newReview(@PathVariable Long id,@Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
//		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//		Product p=productRepository.findById(id).orElse(null);
//		System.out.println(p.getReview());
////		boolean b=false;
////		for(Review r:p.getReview()) {
////			if(r.getNomeUtente().equals(review.getNomeUtente())) {
////				System.out.println("entraaaaaaaaaaaaaaaaaa------------------------------------------------------------");
////				b=true;
////			}
////		}
//		
//		boolean reviewExists = p.getReview().stream()
//		        .anyMatch(r -> r.getNomeUtente().equals(review.getNomeUtente()));
//		
//		if(!reviewExists) {
//			System.out.println("entraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//			System.out.println(review.getId());
//			System.out.println(p.getReview());
//			for(Review r : p.getReview()) {
//				System.out.println(r.getId());
//			}
//			review.setNomeUtente(credentials.getUsername());
//			review.setProdotto(p);
//			this.reviewRepository.save(review);
//			p.getReview().add(review);
//			for(Review r : p.getReview()) {
//				System.out.println(r.getId());
//			}
//			this.productRepository.save(p);
//			return "redirect:/product/"+p.getId();
//		}else {
//			return "redirect:/product/"+p.getId();
//		}
//	}
//>>>>>>> Stashed changes
	
	@GetMapping(value="/formNewReview/{id}")
	public String formNewReview(@PathVariable Long id, Model model) {
	    Product product = productRepository.findById(id).orElse(null);
	    if (product == null) {
	        // Handle product not found scenario
	        return "redirect:/"; // or appropriate error handling
	    }

	    model.addAttribute("review", new Review());
	    model.addAttribute("product", product);
	    return "formNewReview.html";
	}

	@PostMapping("/newReview/{id}")
	@Transactional
	public String newReview(@PathVariable Long id, @Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    
	    Product product = productRepository.findById(id).orElse(null);
	    if (product == null) {
	        // Handle product not found scenario
	        return "redirect:/"; // or appropriate error handling
	    }

	    if (bindingResult.hasErrors()) {
	        // Handle validation errors appropriately
	        model.addAttribute("product", product);
	        return "formNewReview.html";
	    }

	    // Check if a review with the same username already exists
	    boolean reviewExists = product.getReview().stream()
	            .anyMatch(r -> r.getNomeUtente().equals(credentials.getUsername()));

	    if (!reviewExists) {
	        // Create new Review object and set necessary fields
	        Review newReview = new Review();
	        newReview.setNomeUtente(credentials.getUsername());
	        newReview.setProdotto(product);
	        newReview.setStars(review.getStars()); // Set other fields as needed
	        newReview.setDescrizione(review.getDescrizione());

	        // Add new review to the product's reviews collection
	        product.getReview().add(newReview);

	        // Save the new review
	        reviewRepository.save(newReview);

	        // Update the product to reflect the new review
	        productRepository.save(product);

	        return "redirect:/products/" + product.getId();
	    } else {
	        // Redirect with appropriate message indicating review already exists
	        return "redirect:/products/" + product.getId();
	    }
	}
	
	
	
	@PostMapping(value = "/product/delete/{id}")
    public String deleteRicetta(@PathVariable("id") Long id) {
        
        reviewService.deleteRicettaById(id);
        
        return "redirect:/product";
    }
	
	
//	@GetMapping(value = "/{category}")
//	public String getCategory(@PathVariable ("category") Category category, Model model) {
//		model.addAttribute("products", this.productRepository.findAll());
//		model.addAttribute("category", category);
//		
//		return "category.html";
//	}
	

	@GetMapping(value = "/category/{category}")
	public String getCategory(@PathVariable("category") Category category, Model model) {
	    Iterable<Product> products = this.productRepository.findAll();
	    model.addAttribute("products", products);
	    model.addAttribute("category", category);
	    return "category.html";
	}
	
	
	
}
