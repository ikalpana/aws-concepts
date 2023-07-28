package com.dynamodb.demo.service;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamodb.demo.common.Constants;
import com.dynamodb.demo.entity.Product;
import com.dynamodb.demo.exception.ResourceNotFoundException;
import com.dynamodb.demo.repository.ProductRepository;
import com.dynamodb.demo.request.CreateProductRequest;
import com.dynamodb.demo.response.Response;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = Logger.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductRepository productRepository;

	@Override
	public List<Product> getProductList() {
		LOG.info("Returned the list of products");
		return productRepository.findAll();

	}

	@Override
	public Response saveProduct(CreateProductRequest product) {

		Product pd = new Product();
		pd.setCategory(product.getCategory());
		pd.setPrice(product.getPrice());
		pd.setProductName(product.getProductName());
		productRepository.save(pd);
		LOG.info("Product saved successfully in DynamoDb");

		return new Response(pd.getId(), Constants.SUCCESS, Constants.PRODUCT_SAVED_SUCCESSFULLY);
	}

	@Override
	public Product getProductById(String id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found with the given id :" + id));
		LOG.info("Product returned successfully with the given id: " + id);
		return product;

	}

	@Override
	public Response updateProduct(String id, CreateProductRequest product) {
		LOG.info("checking product existance with the given id: " + id);

		Optional<Product> proOptional = productRepository.findById(id);

		if (proOptional.isEmpty()) {
			LOG.info("checking product not found with the given id: " + id);
			throw new ResourceNotFoundException("Product Not Found with the given id : " + id);
		}
		if (!product.getProductName().equalsIgnoreCase(proOptional.get().getProductName())) {
			// Table with a composite primary key, it uniquely identifies an item based on
			// both the partition key and the sort key.
			throw new ResourceNotFoundException("Product can not be updated please provide exisitng product name: "
					+ proOptional.get().getProductName());

		}
		proOptional.get().setProductName(product.getProductName());
		proOptional.get().setCategory(product.getCategory());
		proOptional.get().setPrice(product.getPrice());

		LOG.info("Product updated successfully in DynamoDb with the given product Name: " + product.getProductName());

		productRepository.save(proOptional.get());
		return new Response(proOptional.get().getId(), Constants.SUCCESS, Constants.PRODUCT_UPDATED_SUCCESSFULLY);

	}

	@Override
	public Response deleteProduct(String id) {
		Optional<Product> proOptional = productRepository.findById(id);
		if (proOptional.isEmpty()) {
			LOG.info("checking product not found with the given id: " + id);
			throw new ResourceNotFoundException("Product Not Found with the given id : " + id);
		}
		productRepository.delete(proOptional.get());
		LOG.info("Product deleted successfully from DynamoDb with the given id: " + id);

		return new Response(proOptional.get().getId(), Constants.SUCCESS, Constants.PRODUCT_DELETED_SUCCESSFULLY);

	}

	@Override
	public List<Product> getProductsByProductName(String productName) {

		List<Product> products = productRepository.findByProductName(productName);
		LOG.info("Products returned successfully with the given product name: " + productName);
		return products;

	}
}
