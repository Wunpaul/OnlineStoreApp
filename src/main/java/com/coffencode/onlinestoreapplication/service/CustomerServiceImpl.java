package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.*;
import com.coffencode.onlinestoreapplication.entities.Cart;
import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.entities.Role;
import com.coffencode.onlinestoreapplication.exceptions.CustomerNotFoundException;
import com.coffencode.onlinestoreapplication.mapper.CustomerMapper;
import com.coffencode.onlinestoreapplication.repositories.CartRepository;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import com.coffencode.onlinestoreapplication.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CartRepository cartRepository,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // -------------------------
    // PRIVATE CORE CREATION
    // -------------------------
    /**
     * Create Customer entity, hash password, create and link Cart, persist and return the saved Customer.
     */
    @Transactional
    private Customer createCustomerEntity(String name, String email, String rawPassword) {
        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPassword(passwordEncoder.encode(rawPassword));

        // Assign default role
        Role defaultRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Default role not found. Please seed ROLE_CUSTOMER in the database."));
        c.setRole(defaultRole);

        // Save customer to get id
        Customer saved = customerRepository.save(c);

        // Create and persist cart linked to customer
        Cart cart = new Cart();
        cart.setCustomer(saved);
        Cart savedCart = cartRepository.save(cart);

        // Link back and persist
        saved.setCart(savedCart);
        return customerRepository.save(saved);
    }

    // -------------------------
    // PUBLIC METHODS
    // -------------------------
    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO dto) {
        Customer saved = createCustomerEntity(dto.getName(), dto.getEmail(), dto.getPassword());
        return CustomerMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CustomerAuthDTO createCustomerForAuth(SignupRequest dto) {
        Customer saved = createCustomerEntity(dto.getName(), dto.getEmail(), dto.getPassword());
        return new CustomerAuthDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(CustomerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        Customer saved = customerRepository.save(existing);
        return CustomerMapper.toDTO(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }
}
