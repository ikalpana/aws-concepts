package com.dynamodb.demo.service;

import java.util.List;

import com.dynamodb.demo.entity.Product;
import com.dynamodb.demo.request.CreateProductRequest;
import com.dynamodb.demo.response.Response;

public interface ProductService {
    List<Product> getProductList();
    Response saveProduct(CreateProductRequest product);
    Product getProductById(String id);
    Response updateProduct(String id,CreateProductRequest product);
    Response deleteProduct(String id);
    List<Product> getProductsByProductName(String productName);

  

}
