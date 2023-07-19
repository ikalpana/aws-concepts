package com.dynamodb.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dynamodb.demo.entity.Product;
import com.dynamodb.demo.request.CreateProductRequest;
import com.dynamodb.demo.response.Response;
import com.dynamodb.demo.service.ProductService;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<Response> saveProduct(@RequestBody CreateProductRequest product){
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProductList(){
        return ResponseEntity.ok(productService.getProductList());
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable String id,@RequestBody CreateProductRequest product){
        return ResponseEntity.ok(productService.updateProduct(id,product));
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Response> deleteProductById(@PathVariable String id){
    	return ResponseEntity.ok(productService.deleteProduct(id));
        
    }
    
    @GetMapping("/products/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category){
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
    
    
}
