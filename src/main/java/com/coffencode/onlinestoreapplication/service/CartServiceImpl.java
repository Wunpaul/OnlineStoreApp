package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CartDTO;
import com.coffencode.onlinestoreapplication.entities.Cart;
import com.coffencode.onlinestoreapplication.entities.CartItem;
import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.entities.Product;
import com.coffencode.onlinestoreapplication.exceptions.ResourceNotFoundException;
import com.coffencode.onlinestoreapplication.mapper.CartMapper;
import com.coffencode.onlinestoreapplication.repositories.CartRepository;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import com.coffencode.onlinestoreapplication.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CustomerRepository customerRepository,
                           CartRepository cartRepository,
                           ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CartDTO addItemToCartByEmail(String email, Long productId, int quantity) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setCustomer(customer);
                    Cart saved = cartRepository.save(c);
                    customer.setCart(saved);
                    customerRepository.save(customer);
                    return saved;
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        // Try to find existing cart item for this product in the cart
        Optional<CartItem> existing = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.addCartItem(newItem);
        }

        Cart saved = cartRepository.save(cart);
        return CartMapper.toDTO(saved);
    }

    @Override
    public CartDTO getCartByCustomerEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    // create empty cart if not present
                    Cart c = new Cart();
                    c.setCustomer(customer);
                    Cart saved = cartRepository.save(c);
                    customer.setCart(saved);
                    customerRepository.save(customer);
                    return saved;
                });
        return CartMapper.toDTO(cart);
    }

    @Override
    public CartDTO createCartForCustomerEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));
        Cart c = new Cart();
        c.setCustomer(customer);
        Cart saved = cartRepository.save(c);
        customer.setCart(saved);
        customerRepository.save(customer);
        return CartMapper.toDTO(saved);
    }

    @Override
    public void clearCartByCustomerEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));
        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer: " + email));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

}
