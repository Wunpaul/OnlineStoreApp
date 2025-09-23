package com.coffencode.onlinestoreapplication.dto;

public class ProductCreateDTO {
    private String name;
    private String description;
    private double price;
    private Integer quantity;

    public ProductCreateDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
