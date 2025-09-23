package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.ProductCreateDTO;
import com.coffencode.onlinestoreapplication.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductCreateDTO dto);
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO updateProduct(Long id, ProductCreateDTO dto);
    void deleteProduct(Long id);
    void decreaseStock(Long productId, int qty);
}
