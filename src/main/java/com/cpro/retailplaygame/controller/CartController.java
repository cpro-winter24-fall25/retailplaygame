package com.cpro.retailplaygame.controller;

import com.cpro.retailplaygame.entity.Cart;
import com.cpro.retailplaygame.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // View cart
    @GetMapping("/view")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            // Get the username of the logged-in user
            String username = userDetails.getUsername();
            // Get the cart for the logged-in user using CartServiceImpl
            Cart cart = cartService.getCartByUsername(username);

            if (cart != null) {
                model.addAttribute("cart", cart);
                // Calculate the total price dynamically using the service
                double totalPrice = cartService.calculateTotalPrice(cart);
                model.addAttribute("totalPrice", totalPrice);
            } else {
                model.addAttribute("cart", null);
                model.addAttribute("totalPrice", 0.0);  // Default total if cart is empty
            }

            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("cart", null); // No cart for guest user
            model.addAttribute("totalPrice", 0.0); // Default total for guest
        }
        return "cart";
    }

    // Add to cart
    @PostMapping("/add")
        public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            Principal principal) {
        String username = principal.getName();
        cartService.addToCart(username, productId, quantity);
        return "redirect:/products";
    }

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        String username = principal.getName();
        Cart cart = cartService.getCartByUsername(username);
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "cart";
    }

    // Delete form cart
    @PostMapping("/delete")
    public String deleteFromCart(@RequestParam("cartItemId") Long cartItemId, Principal principal) {
        String username = principal.getName();
        cartService.deleteFromCart(username, cartItemId);
        return "redirect:/cart";
    }
}
