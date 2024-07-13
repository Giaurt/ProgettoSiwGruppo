package com.uniroma3.prog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.uniroma3.prog.model.Credentials;
import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.model.Review;
import com.uniroma3.prog.model.User;
import com.uniroma3.prog.repository.ImageRepository;
import com.uniroma3.prog.repository.ProductRepository;
import com.uniroma3.prog.repository.ReviewRepository;
import com.uniroma3.prog.service.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

	@Autowired ProductService productService;
	@Autowired ProductRepository productRepository;
	@Autowired ImageRepository imageRepository;
	@Autowired ReviewRepository reviewRepository;

	@GetMapping("/products/{id}")
	public String getProduct(@PathVariable("id")Long id, Model model) {
		Product product = productService.findById(id);
		List<Review> reviews = reviewRepository.findByProdotto(product);

        int totalStars = reviews.stream().mapToInt(Review::getStars).sum();
        int userCount = reviews.size();
        double averageRating = (userCount != 0) ? (double) totalStars / userCount : 0;

        model.addAttribute("averageRating", Math.round(averageRating));
		model.addAttribute("product", this.productService.findById(id));
		return "product.html";
	}
	
	@GetMapping("/products")
	public String showProduct(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "products.html";
	}
	
	@GetMapping("/tools")
	public String showTools(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "tools.html";
	}
	
	@GetMapping(value="/newProduct")
	public String formNewProduct(Model model) {
		model.addAttribute("product", new Product());
		return "formNewProduct.html";
	}
	
	@PostMapping(value={"/product"}, consumes = "multipart/form-data")
	public String newMovie(@Valid @ModelAttribute("product") Product product,@RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		
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

	@GetMapping(value="/formNewReview/{id}")
	public String formNewIngrediente(@PathVariable Long id,Model model) {
		model.addAttribute("review", new Review());
		model.addAttribute("product", this.productRepository.findById(id).orElse(null));
		return "formNewReview.html";
	}
	
	@PostMapping("/newReview/{id}")
	public String newIngrediente(@PathVariable Long id,@Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
		Product p=productRepository.findById(id).orElse(null);
		boolean b=false;
		for(Review r:p.getReview()) {
			if(r.equals(review)) {
				b=true;
			}
		}
		if(!b) {
			p.getReview().add(review);
			this.reviewRepository.save(review);
			this.productRepository.save(p);
			return "redirect:/product/"+p.getId();
		}else {
			return "redirect:/product/"+p.getId();
		}
	}
	
	
	
	
}
