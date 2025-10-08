package demo.ecommerce.usecase.product;

import demo.ecommerce.entity.Product;

import java.util.List;

public record SearchProductOutput(List<Product> products) {}