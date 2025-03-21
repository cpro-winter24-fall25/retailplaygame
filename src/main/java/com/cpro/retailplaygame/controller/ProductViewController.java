package com.cpro.retailplaygame.controller;


import com.cpro.retailplaygame.entity.Product;
import com.cpro.retailplaygame.service.CartService;
import com.cpro.retailplaygame.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductViewController {

    @Autowired
    private CartService cartService;

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("/products")
//    public String showProducts(Model model) {
//        List<Product> products = productService.getAllProducts();
//        model.addAttribute("products", products);
//        return "products";
//    }

    @GetMapping("/products")
    public String showProducts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            cartService.applyCoupon(userDetails.getUsername(), "DEFAULT");
        } else {
            model.addAttribute("username", "Guest");
        }


        return "products";
    }
}
