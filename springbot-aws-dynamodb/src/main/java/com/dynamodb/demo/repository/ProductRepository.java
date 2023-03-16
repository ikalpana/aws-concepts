package com.dynamodb.demo.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dynamodb.demo.entity.Product;

@Repository
@EnableScan
public interface ProductRepository extends CrudRepository<Product,String> {
}
