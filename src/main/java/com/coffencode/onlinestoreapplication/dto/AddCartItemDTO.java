package com.coffencode.onlinestoreapplication.dto;

public class AddCartItemDTO {
    private Long productId;
    private Integer quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity == null ? 1 : quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

}
