package io.github.diegomardu.springboot.models;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TB_PRODUCTS")
@Data
public class ProductModel extends RepresentationModel<ProductModel>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID idProduct;
	private String name;
	private BigDecimal value;
}
