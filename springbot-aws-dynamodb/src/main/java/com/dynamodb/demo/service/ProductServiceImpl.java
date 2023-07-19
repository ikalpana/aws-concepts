package com.dynamodb.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dynamodb.demo.common.Constants;
import com.dynamodb.demo.entity.Product;
import com.dynamodb.demo.exception.ResourceNotFoundException;
import com.dynamodb.demo.repository.ProductRepository;
import com.dynamodb.demo.request.CreateProductRequest;
import com.dynamodb.demo.response.Response;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public List<Product> getProductList() {
		return productRepository.findAll();
	}

	@Override
	public Response saveProduct(CreateProductRequest product) {
		Product pd = new Product();
		pd.setCategory(product.getCategory());
		pd.setPrice(product.getPrice());
		pd.setProductName(product.getProductName());
		productRepository.save(pd);
		return new Response(pd.getId(), Constants.SUCCESS, Constants.PRODUCT_SAVED_SUCCESSFULLY);
	}

	@Override
	public Product getProductById(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found with the given id :" + id));
	}

	@Override
	public Response updateProduct(String id, CreateProductRequest product) {
		Optional<Product> proOptional = productRepository.findById(id);
		if (proOptional.isEmpty()) {
			throw new ResourceNotFoundException("Product Not Found with the given id : " + id);
		}
		proOptional.get().setProductName(product.getProductName());
		proOptional.get().setCategory(product.getCategory());
		proOptional.get().setPrice(product.getPrice());

		productRepository.save(proOptional.get());
		return new Response(proOptional.get().getId(), Constants.SUCCESS, Constants.PRODUCT_UPDATED_SUCCESSFULLY);

	}

	@Override
	public Response deleteProduct(String id) {
		Optional<Product> proOptional = productRepository.findById(id);
		if (proOptional.isEmpty()) {
			throw new ResourceNotFoundException("Product Not Found with the given id : " + id);
		}
		productRepository.delete(proOptional.get());
		return new Response(proOptional.get().getId(), Constants.SUCCESS, Constants.PRODUCT_DELETED_SUCCESSFULLY);

	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.findByCategory(category);
	}
}
