package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.OrderDTO;
import com.coffencode.onlinestoreapplication.entities.Cart;
import com.coffencode.onlinestoreapplication.entities.CartItem;
import com.coffencode.onlinestoreapplication.entities.Order;
import com.coffencode.onlinestoreapplication.entities.OrderItem;
import com.coffencode.onlinestoreapplication.entities.Product;
import com.coffencode.onlinestoreapplication.exceptions.*;
import com.coffencode.onlinestoreapplication.mapper.OrderMapper;
import com.coffencode.onlinestoreapplication.repositories.CartRepository;
import com.coffencode.onlinestoreapplication.repositories.OrderRepository;
import com.coffencode.onlinestoreapplication.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderDTO createOrderFromCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(() -> new CartNotFoundException(customerId));
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) throw new EmptyCartException(customerId);

        Order order = new Order();
        order.setCustomer(cart.getCustomer());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        for (CartItem ci : cart.getCartItems()) {
            Product p = ci.getProduct();
            Integer available = p.getQuantity() == null ? 0 : p.getQuantity();
            if (available < ci.getQuantity()) throw new InsufficientStockException(p.getId());
            // deduct stock
            p.setQuantity(available - ci.getQuantity());
            productRepository.save(p);

            OrderItem oi = new OrderItem(order, p, ci.getQuantity(), p.getPrice()); // snapshot price
            order.addOrderItem(oi);
        }

        double total = order.getOrderItems().stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // clear cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toDTO(saved);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(OrderMapper::toDTO)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(OrderMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        // Only allow cancel if pending (example)
        if (!"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new BadRequestException("Order cannot be cancelled in status " + order.getStatus());
        }
        order.setStatus("CANCELLED");
        Order saved = orderRepository.save(order);
        return OrderMapper.toDTO(saved);
    }
}
