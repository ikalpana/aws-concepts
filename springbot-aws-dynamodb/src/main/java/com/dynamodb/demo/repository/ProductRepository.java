package com.dynamodb.demo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.dynamodb.demo.entity.Product;
import com.dynamodb.demo.exception.ResourceNotFoundException;

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
		List<Product> products = this.findAll().stream().filter(p -> p.getId().equals(id)).collect(Collectors.toList());
		String productName = products.size() == 1 ? products.get(0).getProductName() : "";
		Product product = !productName.equals("") ? dynamoDBMapper.load(Product.class, id, productName) : null;

		return Optional.ofNullable(product);
	}

	public List<Product> findAll() {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		return dynamoDBMapper.scan(Product.class, scanExpression);
	}

	public void delete(Product product) {
		dynamoDBMapper.delete(product);
	}

	public List<Product> findByProductName(String productName) {
		HashMap<String, AttributeValue> eav = new HashMap<>();
		eav.put(":v1", new AttributeValue().withS(productName));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				// Filter Expression
				.withFilterExpression("begins_with(ProductName,:v1)").withExpressionAttributeValues(eav);

		return dynamoDBMapper.scan(Product.class, scanExpression);
	}

	public List<Product> findByCategory(String category) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":category", new AttributeValue().withS(category));
		DynamoDBQueryExpression<Product> queryExpression = new DynamoDBQueryExpression<Product>()
				.withKeyConditionExpression("category = :category").withExpressionAttributeValues(eav);
		return dynamoDBMapper.query(Product.class, queryExpression);
	}

}
