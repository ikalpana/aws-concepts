package com.dynamodb.demo.service;

import java.util.List;

import com.dynamodb.demo.entity.Product;

public interface ProductService {
    List<Product> getProductList();
    Product saveProduct(Product product);
    Product getProductById(String id);
    Product updateProduct(String id,Product product);
    void deleteProduct(String id);
}
