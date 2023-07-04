package io.github.diegomardu.springboot.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.diegomardu.springboot.controllers.ProductController;
import io.github.diegomardu.springboot.dtos.ProductRecordDto;
import io.github.diegomardu.springboot.models.ProductModel;
import io.github.diegomardu.springboot.repository.ProductRepository;
import jakarta.validation.Valid;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(productRepository.save(productModel));
	}
	
	public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> product = productRepository.findById(id);
		  
		  if(product.isEmpty()) { 
			  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."); 
		}
		product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel()); 
		return ResponseEntity.status(HttpStatus.OK).body(product.get()); 
		
	}
	
	public ResponseEntity<List<ProductModel>> getAllProducts(){
		List<ProductModel> productList = productRepository.findAll(); 
		  if(!productList.isEmpty()) { 
			  for(ProductModel product: productList) { 
				  UUID id = product.getIdProduct();
				  product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel()); 
			  }
		  }
		  return ResponseEntity.status(HttpStatus.OK).body(productList);
	}
	
	public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto){
		Optional<ProductModel> product = productRepository.findById(id);

		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		var productModel = product.get();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
	}
	
	public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
		Optional<ProductModel> product = productRepository.findById(id);

		if (product.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		productRepository.delete(product.get());
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
	}	

}
