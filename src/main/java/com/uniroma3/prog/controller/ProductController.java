package com.uniroma3.prog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniroma3.prog.service.ProductService;

@Controller
public class ProductController {

	@Autowired ProductService productService;
	
	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable("id")Long id, Model model) {
		model.addAttribute("product", this.productService.findById(id));
		model.addAttribute("products", this.productService.findAll());
		return "product.heml";
	}
	
	@GetMapping("/products")
	public String showProduct(Model model) {
		model.addAttribute("products", this.productService.findAll());
		return "products.html";
	}
}
