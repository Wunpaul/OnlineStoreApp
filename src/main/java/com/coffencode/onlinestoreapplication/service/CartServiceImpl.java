package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CartDTO;
import com.coffencode.onlinestoreapplication.entities.Cart;
import com.coffencode.onlinestoreapplication.entities.CartItem;
import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.entities.Product;
import com.coffencode.onlinestoreapplication.exceptions.CartNotFoundException;
import com.coffencode.onlinestoreapplication.exceptions.ProductNotFoundException;
import com.coffencode.onlinestoreapplication.mapper.CartMapper;
import com.coffencode.onlinestoreapplication.repositories.CartRepository;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import com.coffencode.onlinestoreapplication.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CustomerRepository customerRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartDTO createCart(Long customerId) {
        Customer c = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Optional<Cart> existing = cartRepository.findByCustomerId(customerId);
        if (existing.isPresent()) return CartMapper.toDTO(existing.get());
        Cart cart = new Cart();
        cart.setCustomer(c);
        Cart saved = cartRepository.save(cart);
        return CartMapper.toDTO(saved);
    }

    @Override
    public CartDTO getCartByCustomer(Long customerId) {
        return cartRepository.findByCustomerId(customerId).map(CartMapper::toDTO)
                .orElseThrow(() -> new CartNotFoundException(customerId));
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long customerId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        Cart cart = cartRepository.findByCustomerId(customerId).orElseGet(() -> {
            Customer c = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
            Cart newCart = new Cart();
            newCart.setCustomer(c);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        if (product.getQuantity() != null && product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for product id " + productId);
        }

        Optional<CartItem> existing = cart.getCartItems().stream()
                .filter(ci -> ci.getProduct() != null && productId.equals(ci.getProduct().getId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            cart.addCartItem(item);
        }

        Cart saved = cartRepository.save(cart);
        return CartMapper.toDTO(saved);
    }
}
