package com.dynamodb.demo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.dynamodb.demo.entity.Product;

@Repository
public class ProductRepository {

	@Autowired
	DynamoDBMapper dynamoDBMapper;

	static DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.standard().build());
	static Table table = dynamoDB.getTable("products");

	public Product save(Product product) {
		dynamoDBMapper.save(product);
		return product;
	}

	public Optional<Product> findById(String id) {
		Product product = dynamoDBMapper.load(Product.class, id);
		return Optional.ofNullable(product);
	}

	public List<Product> findAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(Product.class, scanExpression);
	}

	public void delete(Product product) {
		dynamoDBMapper.delete(product);
	}

//	public List<Product> findByCategory(String category) {
//		Map<String, AttributeValue> eav = new HashMap<>();
//		eav.put(":v1", new AttributeValue().withS(category));
//
//		DynamoDBQueryExpression<Product> query = new DynamoDBQueryExpression<Product>()
//				.withIndexName(Product.CATEGORY_INDEX).withConsistentRead(false)
//				.withKeyConditionExpression("category = :v1").withExpressionAttributeValues(eav);
//
//		return dynamoDBMapper.query(Product.class, query);
//	}

	public List<Product> findByCategory(String category) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":category", new AttributeValue().withS(category));
		DynamoDBQueryExpression<Product> queryExpression = new DynamoDBQueryExpression<Product>()
				.withKeyConditionExpression("category = :category").withExpressionAttributeValues(eav);
		return dynamoDBMapper.query(Product.class, queryExpression);
	}

}
