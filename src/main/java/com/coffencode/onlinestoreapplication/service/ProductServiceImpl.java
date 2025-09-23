package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.ProductCreateDTO;
import com.coffencode.onlinestoreapplication.dto.ProductDTO;
import com.coffencode.onlinestoreapplication.entities.Product;
import com.coffencode.onlinestoreapplication.exceptions.ProductNotFoundException;
import com.coffencode.onlinestoreapplication.mapper.ProductMapper;
import com.coffencode.onlinestoreapplication.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) { this.productRepository = productRepository; }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductCreateDTO dto) {
        Product p = ProductMapper.toEntity(dto);
        Product saved = productRepository.save(p);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toDTO)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductCreateDTO dto) {
        Product existing = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setQuantity(dto.getQuantity());
        Product saved = productRepository.save(existing);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new ProductNotFoundException(id);
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, int qty) {
        Product p = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        Integer q = p.getQuantity() == null ? 0 : p.getQuantity();
        if (q < qty) throw new RuntimeException("Insufficient stock for product " + productId);
        p.setQuantity(q - qty);
        productRepository.save(p);
    }
}
