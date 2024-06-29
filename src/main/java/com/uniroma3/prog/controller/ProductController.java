package com.uniroma3.prog.controller;

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

import com.uniroma3.prog.model.Image;
import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.repository.ImageRepository;
import com.uniroma3.prog.repository.ProductRepository;
import com.uniroma3.prog.service.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

	@Autowired ProductService productService;
	@Autowired ProductRepository productRepository;
	@Autowired ImageRepository imageRepository;
	

	
	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable("id")Long id, Model model) {
		model.addAttribute("product", this.productService.findById(id));
		model.addAttribute("products", this.productService.findAll());
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
	
	@GetMapping(value="/formNewProduct")
	public String formNewProduct(Model model) {
		model.addAttribute("product", new Product());
		return "formNewProduct.html";
	}
	
	@PostMapping(value={"/product"}, consumes = "multipart/form-data")
	public String newMovie(@Valid @ModelAttribute("product") Product product,@RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		
		//this.movieValidator.validate(movie, bindingResult);
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
}
