package com.uniroma3.prog.controller;

import com.uniroma3.prog.model.Product;
import com.uniroma3.prog.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    ProductService productService;

    @PostMapping(value="/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Product> searchResults = productService.searchProduct(keyword);
        model.addAttribute("products", searchResults);
        return "search-products";
    }

}
